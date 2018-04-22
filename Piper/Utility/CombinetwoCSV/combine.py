#!/usr/bin/env python

import urllib
import csv

fakedevingp = "uniquerows.csv"
fakedevin201 = "allrevieweinformation.csv"

fakedevingplines = open(fakedevingp).read().splitlines() 
fakedevin201lines = open(fakedevin201).read().splitlines()

c = csv.writer(open("combinedfiles.csv", "wb"))

for app201line in fakedevin201lines:
	split = app201line.split(",")

	for fakeingpline in fakedevingplines:
		if split[0] in fakeingpline:
			split2 = fakeingpline.split(",")
			c.writerow([split2[0],split2[1],split[1]])
	