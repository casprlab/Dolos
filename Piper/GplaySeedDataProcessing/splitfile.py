
import csv

appids = open("640appsfakeappsusers_notfound.csv").read().splitlines()

splitcount = int(len(appids)/3)



for x in xrange(1,4):
	filename = open("640appsfakeappsusers_notfound" + str(x) + ".txt",'w')

	for i in xrange(splitcount*(x-1),splitcount*x):
		filename.write(appids[i] + "\n")
