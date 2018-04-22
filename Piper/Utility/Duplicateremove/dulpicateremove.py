from bs4 import BeautifulSoup
import urllib
import urllib2
import datetime
import csv
import os


def removeDuplicate():
	print "starting for removing duplicats"
	#for filename in os.listdir("data"):
	filename = "allrevieweinformation640.csv"
	print "started"

	fname = filename
	if os.path.isfile(fname):
		uniqlines = set(open(fname).readlines())
		bar = open("uniquerows.csv", 'w')
		bar.writelines(set(uniqlines))
		bar.close()

				#print "****" + fname

	print "finishing for removing duplicats"
			
removeDuplicate()			








