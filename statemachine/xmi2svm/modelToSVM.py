#!/usr/bin/python
import os, re
from os.path import join, abspath, isfile, isdir, exists, basename
from shutil import copyfile, copytree, rmtree
from time import strftime, strptime, localtime

from state import *

TOP_ID = "### TOP ###"

mapIdState = {}
machines = {}

def createSubStates(svm, states, parent, parentId):
    substates = filter(lambda x: x["parent"] == parentId, states.values())
    for s in substates:
        ns = svm.newState(parent, s["name"])
        mapIdState[s["id"]] = ns
        createSubStates(svm, states, ns, s["id"])

def createStates(svm, model):
    global mapIdState
    states = model["states"]
    topId = ""
    for s in states.values():
        if s["parent"] == TOP_ID:
            topId = s["id"]
    mapIdState[topId] = svm.top
    createSubStates(svm, states, svm.top, topId)

def createTransitions(svm, model):
    global mapIdState
    transitions = model["transitions"]
    events = model["events"]
    for t in transitions.values():
        # id,source,target,effect,guard,trigger
        source = mapIdState[t["source"]]
        target = mapIdState[t["target"]]
        nt = svm.newTransition(source, target)
        if t.get("guard"):
            nt.condition = t["guard"]
        if t.get("effect"):
            nt.output = t["effect"]
        if t.get("trigger"):
            ev = events[t["trigger"]]
            if ev["kind"] == "time":
                nt.time = float(ev["body"])
            else:
                nt.event = ev["name"]

def createEntryExitActions(svm, model):
    global mapIdState
    states = model["states"]
    # do, entry, exit
    for s in states.values():
        state = mapIdState[s["id"]]
        if s.get("entry"):
            a = svm.newEnterAction(state)
            a.output = s["entry"]
            
        if s.get("exit"):
            a = svm.newExitAction(state)
            a.output = s["exit"]
            
        if s.get("do"):
            nt = svm.newTransition(state, state)
            nt.time = 1
            out = s["do"]
            v = out.split("!",1)
            if len(v) > 1:
                nt.time = utils.asFloat(v[0], 1.0)
                nt.output = v[1]
            else:
                nt.output = s["do"]
            pass
    pass

def assignProperties(svm, model):
    global mapIdState
    states = model["states"]
    # concurrent, final
    # history, deepHistory
    for s in states.values():
        state = mapIdState[s["id"]]
        if s.get("default"):
            state.default = True
        if s.get("final"):
            state.final = True
        if s.get("history"):
            state.history = True
        if s.get("deepHistory"):
            state.deepHistory = True
        if s.get("concurrent"):
            for sub in state.substates:
                sub.concurrent = True
                sub.default = True
        if s.get("submachine"):
            subref = s["submachine"]
            if machines.get(subref):
                subname = machines[subref]["name"]
                state.importation = svm.newImportation(subname, subname + ".des")
            else:
                logging.warning("No submachine with id " + subref + " found.")
    pass

def markSingleSubstateDefault(parent):
    if len(parent.substates) == 1:
        parent.substates[0].default = True
    for s in parent.substates:
        markSingleSubstateDefault(s)

def processMachine(machine, events):
    global mapIdState
    mapIdState = {}
    svm = SVM(machine["name"])
    machine["events"] = events
    createStates(svm, machine)
    createTransitions(svm, machine)
    assignProperties(svm, machine)
    createEntryExitActions(svm, machine)
    markSingleSubstateDefault(svm.top)
    svm.sanityCheck()
    with open(machine["name"] + ".des", "w") as f:
        f.write(str(svm))
    return svm

def modelToSVM(model):
    global machines
    machines = model["machines"]
    main = False
    for m in machines.values():
        s = processMachine(m, model["events"])
        if not main:
            main = s
    return main