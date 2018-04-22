#!/usr/bin/env python

import urllib
import csv

filename = "coreviewertimedatasorted.csv"
reviewersfilename = "allfakereviewersname.csv"


lines = open(filename).read().splitlines()
reviewersname = open(reviewersfilename).read().splitlines()

c = csv.writer(open("output.csv", "wb"))

for line in lines:
	split = line.split(",")
	
	if split[1] in reviewersname and split[3] in reviewersname:
		c.writerow([split[0],split[1],split[2],split[3],split[4],split[5],split[6],split[7]])

		


