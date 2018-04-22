#!/usr/bin/env python

import urllib
import csv

lines1 = open("244+53apps.csv").read().splitlines() 
lines2 = open("244apps.csv").read().splitlines()

for line1 in lines1:
	if line1 in lines2:
		print line1