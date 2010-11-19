#!/usr/bin/python
import os, re
from os.path import join, abspath, isfile, isdir, exists, basename
from shutil import copyfile, copytree, rmtree
from time import strftime, strptime, localtime

import utils
import logging

def doErr(err, log):
    logging.error(err + "\n-----\n" + log + "\n")
    
class SVM(object):
    def __init__(self, name = "", *args):
        self.name = name
        self.states = []
        self.transitions = []
        self.actions = []
        self.importations = []
        self.ports = []
        
        self.top = State(None, "top")
        self.top.default = True
        self.states.append(self.top)
    
    def newState(self, parent, name, *args):
        if not parent in self.states:
            print "State " + parent.name + " not in SVM"
        s = State(parent, name, *args)
        self.states.append(s)
        return s
    
    def newEnterAction(self, state):
        if not state in self.states:
            print "State " + state.fullName() + " not in SVM"
        a = EnterAction(state)
        self.actions.append(a)
        return a
    
    def newExitAction(self, state):
        if not state in self.states:
            print "State " + state + " not in SVM"
        a = ExitAction(state)
        self.actions.append(a)
        return a
    
    def newTransition(self, A, B):
        if not (A in self.states and B in self.states):
            print "State " + A.fullName() + " or " + B.fullName() + " not in SVM"
        t = Transition(A,B)
        self.transitions.append(t)
        return t
    
    def newImportation(self, id, filename):
        i = Importation(id, filename)
        self.importations.append(i)
        return i
    
    def newPort(self, name, type = "in"):
        p = Port(name, type)
        self.ports.append(p)
        return p
    
    def __repr__(self):
        r = ""
        
        if len(self.importations) > 0:
            r += utils.caption("Imports")
            for t in self.importations:
                r += str(t) + "\n"
            r += "\n"
        
        r += utils.caption("Statechart")
        r += self.stateChart() + "\n"
        
        if len(self.actions) > 0:
            r += utils.caption("Actions")
            for a in self.actions:
                r += str(a) + "\n"
            r += "\n"
            
        if len(self.transitions) > 0:
            r += utils.caption("Transitions")
            for t in self.transitions:
                r += str(t) + "\n"
            r += "\n"

        return r       
    
    def stateChart(self):
        r = "STATECHART:\n"
        
        def recurse(state, pad):
            r = (" " * pad) + str(state) + "\n"
            for s in state.substates:
                r += recurse(s, pad + 4)
            return r
        
        r += recurse(self.top, 2)
        return r
    
    def sanityCheck(self):
        logging.info(" # Sanity Checks")
        for s in self.states:
            s.sanityCheck()
        for t in self.transitions:
            t.sanityCheck()
        for a in self.actions:
            a.sanityCheck()
        for i in self.importations:
            i.sanityCheck()
        for p in self.ports:
            p.sanityCheck()

class State(object):
    def __init__(self, parent, name, *args):
        self.name = name
        self.parent = parent
        if parent != None:
            self.parent.substates.append(self)
        self.substates = []
        self.importation = None
        
        self.default = False
        self.final = False
        self.concurrent = False
        self.history = False
        self.deepHistory = False
        self.innerTransitionFirst = False
        self.outerTransitionFirst = False
        self.reverseTransitionOrder = False
    
    def __repr__(self):
        return self.fullName()
    
    def __str__(self):
        return self.properties()
        
    def fullName(self):
        name = self.name
        state = self
        while True:
            state = state.parent
            if state == None: break
            name = state.name + "." + name
        return name
        
    def properties(self):
        name = self.name
        if self.concurrent : name += " [CS]"
        if self.default : name += " [DS]"
        if self.final   : name += " [FS]"
        if self.history     : name += " [HS]"
        if self.deepHistory : name += " [HS*]"
        if self.innerTransitionFirst   : name += " [ITF]"
        if self.outerTransitionFirst   : name += " [OTF]"
        if self.reverseTransitionOrder : name += " [RTO]"
        if self.importation : name += " [{0}]".format(self.importation.id)
        return name
    
    ###############
    # Sanity Checks
    ###############
    
    def sanityCheck(self):
        err = ""
        if re.search("\n", self.name):
            err += "Name contains newline.\n"
        if self.importation and len(self.substates) > 0:
            err += "Imporation and Substates both defined.\n"
        err += self.propertiesSanityCheck()
        err += self.substatesSanityCheck()
        if err:
            err += str(self)
        if err:
            doErr("State failed sanity check:", err)
    
    def propertiesSanityCheck(self):
        err = ""
        if self.concurrent and not self.default: err += "Concurrent requires Default to be set.\n"
        if self.history and self.deepHistory: err += "History and Deep History both set.\n"
        if self.innerTransitionFirst + self.outerTransitionFirst \
           + self.reverseTransitionOrder > 1:
           err += "More than one transition order defined.\n"
        return err
    
    def substatesSanityCheck(self):
        err = ""
        concurrent = 0        
        for state in self.substates:
            concurrent += state.concurrent
        if (concurrent != 0) and (concurrent != len(self.substates)):
            err += "If one substate is concurrent then all other must be aswell.\n"
        return err

class Importation(object):
    def __init__(self, id, filename, *args):
        self.id = id
        self.filename = filename
    
    def __repr__(self):
        return "[{0}] in {1}".format(self.id, self.filename)
    
    def __str__(self):
        r = "IMPORTATION:\n"
        r += "  {0} = {1}".format(self.id, self.filename)
        return r
    
    def sanityCheck(self):
        err = ""
        if re.search("\n", self.id):
            err += "ID contains newline.\n"
        if re.search("\n", self.filename):
            err += "FileName contains newline.\n"            
        if err:
            err += str(self)
        if err:
            doErr("Port failed sanity check:", err)        

class Action(object):
    
    def __init__(self, state, *args):
        self.state = state
        self.output = ""
        self.condition = ""
    
    ###############
    # Sanity Checks
    ###############
    
    def sanityCheck(self):
        err = ""
        if not self.state:
            err += "State is required.\n"
        if not self.output:
            err += "Output is required."
        if err:
            err += str(self)
        if err:
            doErr("Action failed sanity check:", err)

class EnterAction(Action):
    def __init__(self, state, *args):
        Action.__init__(self, state, args)
    
    def __repr__(self):
        if self.condition:
            return "ENTER: " + self.state.fullName() + "[{0}]".format(self.condition)
        else:
            return "ENTER: " + self.state.fullName()
    
    def __str__(self):
        r = "ENTER:\n"
        r += "  N: {0}\n".format(self.state.fullName())
        if self.condition:
            r += "  C: {0}\n".format(utils.padlines(self.condition, 5))
        if self.output:
            r += "  O: {0}\n".format(utils.padlines(self.output, 5))
        return r

class ExitAction(Action):
    def __init__(self, state, *args):
        Action.__init__(self, state, args)
    
    def __repr__(self):
        if self.condition:
            return "EXIT: " + self.state.fullName() + "[{0}]".format(self.condition)
        else:
            return "EXIT: " + self.state.fullName()
        
    def __str__(self):
        r = "EXIT:\n"
        r += "  S: {0}\n".format(self.state.fullName())
        if self.condition:
            r += "  C: {0}\n".format(utils.padlines(self.condition, 5))
        if self.output:
            r += "  O: {0}\n".format(utils.padlines(self.output, 5))
        return r

class Transition(object):
    
    def __init__(self, start, end, *args):
        self.start = start
        self.end = end
        self.event = ""
        self.priority = 0
        self.condition = "" # conditional event
        self.output = ""    # commands executed
        self.time = -1
        self.onceTimed = False

    # A transition can have the following attributes:
    # S: old State (the state where the transition can be triggered)
    # E: Event (the event to trigger the transition)
    # C: Condition (also known as guard, the condition to be satisfied for the transition)
    # N: New state (the new state after the transition)
    # O: Output (single or multiple output commands in which logic such as braching and iteration is not allowed
    def __repr__(self):
        r = self.start.fullName()
        r += " --["
        if self.event:
            r += self.event
        elif self.time >= 0:
            r += str(self.time) + "s"
        if self.condition:
            r += " ! " + self.condition.replace(" ","").replace("\n"," & ")
        r += "]--> "
        r += self.end.fullName()
        return r

    def __str__(self):
        if self.priority == 0:
            r = "TRANSITION:\n"
        else:
            r = "TRANSITION: [{0}]\n".format(self.priority)
        r += "  S: {0}\n".format(self.start.fullName())
        r += "  N: {0}\n".format(self.end.fullName())
        if self.condition:
            r += "  C: {0}\n".format(utils.padlines(self.condition,5))        
        if self.event:
            r += "  E: {0}\n".format(self.event)
        elif self.time >= 0:
            if not self.onceTimed:
                r += "  T: {0}\n".format(self.time)
            else:
                r += "  T: {0} [OTT]\n".format(self.time)
        else:
            logging.warning( "Transition " + self.start.fullName() + "->" + self.end.fullName() + " will be implicitly set time event 0")
            r += "  T: 0\n"
        if self.output.strip() != "":
            r += "  O: {0}\n".format(utils.padlines(self.output,5))
        return r
    
    ###############
    # Sanity Checks
    ###############
    
    def sanityCheck(self):
        err = ""
        if not self.start or not self.end:
            err += "Start and End are mandatory.\n"
        if (self.event == "") and (self.time < 0):
            err += "Event or Time mandatory.\n"
        if re.search("\n", self.event):
            err += "Event contains newline.\n"
        if re.search("for|if|else|elif|while", self.output):
            err += "Output contains iteration/branching.\n"
        if self.event and (self.time >= 0):
            err += "Event and Time both specified.\n"
        if err:
            err += str(self)
        if err:
            doErr("Transition failed sanity check:", err)

class Port(object):
    
    def __init__(self, name, type = "in", *args):
        self.name = name
        self.type = type
        
    def __repr__(self):
        return self.name + " [" + self.type + "]"
    
    def __str__(self):
        r = "PORT:\n"
        r += "  name = {0}\n".format(self.name)
        r += "  type = {0}\n".format(self.type)
        return r
    
    def sanityCheck(self):
        err = ""
        if re.search("\n", self.name):
            err += "Name contains newline.\n"
        if not re.match("in|inout|out", self.type):
            err += "Invalid type.\n"
        if err:
            err += str(self)
        if err:
            doErr("Port failed sanity check:", err)

class Connection(object):
    
    def __init__(self,*args):
        # TODO
        pass

def test():
    s = SVM()
    
    A = s.newState(s.top, "A")
    A.default = True
    A.importation = s.newImportation("aSub", "aSub.des")
    
    B = s.newState(s.top, "B")    
    C = s.newState(B, "C")
    C.default = True
    
    ab = s.newTransition(A,B)
    ab.event = "Go"
    ab.priority = 1
    ab.condition = "z == w\n  x == y"
    ab.output = "print 'hello '\n print 'world!'"
    
    ca = s.newTransition(C,A)
    ca.time = 5
    ca.output = "print '5 sec elapsed'"
    
    ena = s.newEnterAction(A)
    ena.condition = "x != y"
    ena.output = "print 'hello a'"
    exb = s.newExitAction(B)
    exb.output = "print 'bye b'"
    
    return s