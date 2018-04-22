#!/usr/bin/env python

import urllib
import csv

lines1 = open("500apps_new.csv").read().splitlines() 
lines2 = open("640apps.csv").read().splitlines()

c = csv.writer(open("combinedfiles.csv", "w"))

for line2 in lines2:
	if line2 not in lines1:
		print line2