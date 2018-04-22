from bs4 import BeautifulSoup
import urllib
import urllib2
import datetime
import csv
import os

i = 0

outputFile = "allreviewersname.csv"

if os.path.isfile(outputFile) == False:
	writer = csv.writer(open(outputFile, "wb"))
else:
	writer = csv.writer(open(outputFile, "a"))

for filename in os.listdir("data"):
	if os.path.isdir("data/" + filename):

		i =  i+1

		fname = "data/" + filename +"/allreviews.csv"

		if os.path.isfile(fname):
			inputfile = open(fname)
			for line in inputfile:
				#print line
				linestrs = line.split(",")    
				print linestrs[0]

				if linestrs[0] == "A Google User":
					continue



				if len(linestrs) > 1:
					print linestrs[1]

					writer.writerow([linestrs[0],linestrs[1],filename])
					
				
				



		print  filename + " %d",i
		

print i		
