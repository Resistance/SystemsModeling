#!/usr/bin/python
import os, re
from os.path import join, abspath, isfile, isdir, exists, basename
from shutil import copyfile, copytree, rmtree
from time import strftime, strptime, localtime

import sys
import logging

from xml.dom import minidom

TOP_ID = "### TOP ###"

unknown = 0

def getUnknownName(onlyNum=False):
    global unknown
    if onlyNum:
        name = str(unknown)
    else:
        name = "Unknown_" + str(unknown)
    unknown += 1
    return name

def getId(node):
    if node.nodeName == "UML:StateMachine.top":
        return TOP_ID
    if not node.attributes.get("xmi.id"):
        logging.error(node.nodeName + " has no ID")
    return node.attributes["xmi.id"].value

def getRef(node):
    return node.attributes["xmi.idref"].value

def isConcurrent(node):
    if node.attributes.get("isConcurrent"):
        return node.attributes["isConcurrent"].value == "true"
    return False

def isFinal(node):
    if node.nodeName == "UML:FinalState":
        return True
    return False

def getParent(node):
    parent = node.parentNode
    if parent.nodeName == "UML:CompositeState.subvertex":
        parent = parent.parentNode
    return parent

def getChildren(node, types):
    if len(types) <= 0:
        return []
    subs = node.getElementsByTagName(types.pop())
    s = []
    for sub in subs: s.append(sub)    
    other = getChildren(node, types)
    s.extend(other)
    return s

def getChildState(node):
    s = getChildren(node, ["UML:SimpleState", "UML:CompositeState", "UML:FinalState", "UML:Pseudostate", "UML:SubmachineState"])
    if len(s) > 1:
        logging.warning("Node has multiple states.")
    if len(s) == 0:
        logging.warning("Node has no child states.")
        return None
    return s[0]

def hasState(states, name, parent):
    for v in states.values():
        if v["name"] == name and v["parent"] == parent:
            return True
    return False

def getCollapsedExpression(node, types):
    subs = getChildren(node, types)
    if len(subs) <= 0: return ""
    expr = ""
    for e in subs:
        if e.attributes.get("body"):
            expr += e.attributes["body"].value + "\n"
    return expr.strip()

def getAction(node, name):
    subs = node.getElementsByTagName(name)
    if len(subs) <= 0: return ""
    id = getId(node)
    subs = filter(lambda x: filterImmediateChild(id,x), subs)
    if len(subs) <= 0: return ""
    return getCollapsedExpression(subs[0], ["UML:ActionExpression"])

def getGuard(node):
    subs = node.getElementsByTagName("UML:Guard")
    if len(subs) <= 0: return ""
    if len(subs) > 1:
        logging.warning("Multiple guards in one node.")
        return ""
    expr = getCollapsedExpression(subs[0], ["UML:BooleanExpression"])
    return expr

def filterImmediateChild(id, child):
    parent = child.parentNode
    pid = ""
    if parent.attributes.get("xmi.id"):
        pid = parent.attributes["xmi.id"].value
    return id == pid

def getTransitionTrigger(node):
    subs = getChildren(node, ["UML:TimeEvent","UML:SignalEvent","UML:CallEvent","UML:ChangeEvent"])
    if len(subs) <= 0:
        return ""
    s = subs[0]
    return getRef(s)

def getSubmachine(id, node):
    subs = node.getElementsByTagName("UML:SubmachineState.submachine")
    subs = filter(lambda x: filterImmediateChild(id,x), subs)
    if len(subs) == 0:
        return ""
    elif len(subs) > 1:
        logging.warning("Multiple state machines in state")
    subs = subs[0].getElementsByTagName("UML:StateMachine")
    if len(subs) == 0:
        return ""
    elif len(subs) > 1:
        logging.warning("Multiple state machines in state")
    return getRef(subs[0])
    
def processState(s, states):
    id = getId(s)
    if s.attributes.get("name"):
        name = s.attributes["name"].value.replace(" ","_").replace(".","_")
    else:
        logging.warning(s.nodeName + " id=" + id + " has no name")
        name = getUnknownName()
    parent = getId(getParent(s))
    if hasState(states, name, parent):
        logging.warning("Duplicate substate " + name + ".")
        name = name + "-" + getUnknownName(True)
        logging.warning("  adjusted name to: " + name)   
    ns = {"id":id}
    ns["name"] = name
    ns["parent"] = parent
    ns["concurrent"] = isConcurrent(s)
    ns["final"] = isFinal(s)
    ns["do"]    = getAction(s, "UML:State.doActivity")
    ns["entry"] = getAction(s, "UML:State.entry")
    ns["exit"]  = getAction(s, "UML:State.exit")
    ns["submachine"] = getSubmachine(id,s)
    
    return ns

def processTransition(t):
    id = getId(t)
    nt = {"id":id}
    src  = t.getElementsByTagName("UML:Transition.source")
    if t.attributes.get("name"):
        nt["name"] = t.attributes["name"].value
    nt["source"] = False
    nt["target"] = False
    if len(src) > 0:
        src = src[0]
        s = getChildState(src)
        if s:
            nt["source"] = getRef(s)
    else:
        logging.warning("Transition " + id + " has no source.")
    dest = t.getElementsByTagName("UML:Transition.target")
    if len(dest) > 0:
        dest = dest[0]
        d = getChildState(dest)
        if d:
            nt["target"] = getRef(d)
    else:
        logging.warning("Transition " + id + " has no target.")
    
    nt["effect"] = getAction(t, "UML:Transition.effect")
    nt["guard"] = getGuard(t)
    nt["trigger"] = getTransitionTrigger(t)
    return nt

def processEvent(node):
    if not node.attributes.get("xmi.id"):
        return False
    id = getId(node)
    if node.attributes.get("name"):
        name = node.attributes["name"].value
    else:
        name = getUnkownName()
    body = ""
    if node.nodeName == "UML:CallEvent":
        kind = "call"
    elif node.nodeName == "UML:SignalEvent":
        kind = "signal"
    elif node.nodeName == "UML:TimeEvent":
        kind = "time"
        body = getCollapsedExpression(node, ["UML:TimeExpression"])
    elif node.nodeName == "UML:ChangeEvent":
        kind = "change"
        body = getCollapsedExpression(node, ["UML:BooleanExpression"])
    
    e = {"id":id}
    e["name"] = name
    e["kind"] = kind
    e["body"] = body
    return e

def processPseudoSingleTransition(s, states, transitions, end, field, value):
    id = getId(s)
    transs = s.getElementsByTagName("UML:Transition")
    if len(transs) <= 0:
        logging.warning("Pseudostate " + id + " has no transitions.")
        return
    elif len(transs) > 1:
        logging.warning("Pseudostate " + id + " has multiple transitions. Something might go wrong!")
    transition = transs[0]
    ref = getRef(transition)
    if not (transitions.get(ref)):
        logging.error("Pseudostate " + id + " transition " + ref + " not found.")
        del transitions[ref]
        return
    if transitions[ref].get(end):
        states[transitions[ref][end]][field] = value
    pseudoTransitionCheck(id, transitions)
    del transitions[ref]

def processPseudoInitial(s, states, transitions):
    processPseudoSingleTransition(s,states,transitions,"target","default",True)

def processPseudoHistory(s, states, transitions):
    processPseudoSingleTransition(s,states,transitions,"target","history",True)

def processPseudoDeepHistory(s, states, transitions):
    processPseudoSingleTransition(s,states,transitions,"target","deepHistory",True)
    
def pseudoTransitionCheck(id, transition):
    if transition.get("guard"):
        logging.warning("Transition " + id + " should have no guard.")
    if transition.get("effect"):
        logging.warning("Transition " + id + " should have no effect.")
    if transition.get("trigger"):
        logging.warning("Transition " + id + " should have no trigger.")

def processPseudoState(s, states, transitions):
    id = getId(s)
    kind = ""
    if s.attributes.get("kind"):
        kind = s.attributes["kind"].value
    if kind == "initial":
        processPseudoInitial(s, states, transitions)
    elif kind == "shallowHistory":
        processPseudoHistory(s, states, transitions)
    elif kind == "deepHistory":
        processPseudoDeepHistory(s, states, transitions)
    # elif kind == "junction":
    # elif kind == "choice":
    # elif kind == "fork":
    # elif kind == "join":
    else:
        logging.error("Don't know how to handle pseudostate " + kind + " id="  + id)

def processSubMachine(data):
    if not data.attributes.get("xmi.id"):
        return False
    machine = {}
    machine["id"] = getId(data)
    
    if data.attributes.get("name"):
        name = data.attributes["name"].value.replace(" ","_").replace(".","_")
    else:
        logging.warning("Machine id=" + machine["id"] + " has no name")
        name = getUnknownName()
        
    machine["name"] = name
    # map for states
    # id -> {id: id, name:name, parent:refid, ...}
    states = {}
    
    # map for transitions
    # id -> {id:id, from:refid, to:refid, guard:guard, trigger:refid, effect:content...}
    transitions = {}
    
    top = data.getElementsByTagName("UML:StateMachine.top")[0]
    transs = data.getElementsByTagName("UML:StateMachine.transitions")
    if len(transs) >= 1 :
        transs = transs[0]
    else:
        transs = False
        
    sts = getChildren(top, ["UML:SimpleState", "UML:CompositeState", "UML:FinalState", "UML:SubmachineState"])
    for s in sts:
        ns = processState(s, states)
        states[ns["id"]] = ns
    
    if transs:
        trs = transs.getElementsByTagName("UML:Transition")
        for t in trs:
            nt = processTransition(t)
            transitions[nt["id"]] = nt
        
    sts = top.getElementsByTagName("UML:Pseudostate")
    for s in sts:
        processPseudoState(s, states, transitions)
    
    machine["states"] = states
    machine["transitions"] = transitions
    return machine


def xmlToModel(data):
    model = {}
    if data.attributes.get("name"):
        model["ModelName"] = data.attributes["name"].value

    machines = {}    
    machins = data.getElementsByTagName("UML:StateMachine")
    for m in machins:
        s = processSubMachine(m)
        if s: machines[s["id"]] = s
    
    events = {}
    evs = getChildren(data, ["UML:TimeEvent","UML:SignalEvent","UML:CallEvent","UML:ChangeEvent"])
    for s in evs:
        e = processEvent(s)
        if e: events[e["id"]] = e
    
    model["machines"] = machines
    model["events"] = events
    
    return model
