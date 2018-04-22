#!/usr/bin/env python

import urllib
import csv

lines1 = open("24workers_allappids3_new_ex.csv").read().splitlines() 
lines2 = open("new_appids_fromworkers.csv").read().splitlines()

#c = csv.writer(open("combinedfiles.csv", "wb"))

for line2 in lines2:
	if line2 not in lines1:
		print line2