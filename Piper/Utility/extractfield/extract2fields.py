#!/usr/bin/env python

import urllib
import csv

filename = "allbenignappsreview.csv"
appname='org.pbskids.video'

lines = open(filename).read().splitlines() 

c = csv.writer(open(appname + ".csv", "wb"))

for line in lines:
	if line.find(appname) != -1:
		split = line.split(",")
		ratingstr = split[5].strip().replace('%','')
		datestr = split[2].strip()
		
			

		if ratingstr.isdigit():
			rating = int(ratingstr)/20
			c.writerow([split[1].strip().replace('/store/people/details?id=',''),rating,datestr]) 
		
		
	