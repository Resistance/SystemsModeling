#!/usr/bin/python
import os, re
from os.path import join, abspath, isfile, isdir, exists, basename
from shutil import copyfile, copytree, rmtree
from time import strftime, strptime, localtime

import sys
import logging

LOG_FILENAME = 'convert.log'
logging.basicConfig(filename=LOG_FILENAME,level=logging.DEBUG)

from state import *

from xml.dom import minidom
import xmlToModel
import modelToSVM

def convertFile(name):
    logging.info("Started converting file: " + name)
    xmldoc = minidom.parse(sys.argv[1])
    models = xmldoc.getElementsByTagName('UML:Model')
    model = xmlToModel.xmlToModel(models[0])
    svm = modelToSVM.modelToSVM(model)
    print str(svm)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print "Usage:"
        print "  " + sys.argv[0] + " file.xmi"
        os.abort()
    convertFile(sys.argv[1])