#!/usr/bin/env python

import urllib
import csv

filename = "201reviewerscoreviewinfo.csv"

lines = open(filename).read().splitlines() 

c = csv.writer(open("output.csv", "wb"))

for line in lines:
	split = line.split(",")
	c.writerow([split[5],split[6]]) 