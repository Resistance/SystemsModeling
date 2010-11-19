#!/usr/bin/python
import os, re
from os.path import join, abspath, isfile, isdir, exists, basename
from shutil import copyfile, copytree, rmtree
from time import strftime, strptime, localtime

def padlines(inp, pad):
    """ pads every line with pad spaces except first
        also removes empty lines"""
    lines = inp.splitlines()
    lines=map(lambda x: x.strip(), lines)
    lines=filter(lambda x: len(x) > 0, lines)
    return ("\n"+(" "*pad)).join(lines)

def caption(text):
    return "##################\n# {0}\n##################\n\n".format(text)

def asFloat(text, default):
    try:
        return float(text)
    except:
        return default
    