import csv

outputfile = open("reviews_640apps_new.csv",'w')

allapplist = open("640apps.csv").read().splitlines()

def getreviewCountfromOnefile(reviewfile):
	reviews = open(reviewfile).read().splitlines()
	count = 0
	for review in reviews:
		split = review.split(",")
	 	appid = split[6].strip()

	 	if appid in allapplist:
	 		#print review
	 		outputfile.write(review + "\n")

reviewfiles = ["allreviews/allrevieweinformation1.csv" , "allreviews/allrevieweinformation2.csv" , "allreviews/allrevieweinformation3.csv"]

for i in xrange(0,3):
	getreviewCountfromOnefile(reviewfiles[i])
	