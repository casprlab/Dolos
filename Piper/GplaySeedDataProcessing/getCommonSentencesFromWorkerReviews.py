
import csv

textcountmaping = {}

def findcount(sentence):
	count = 0
	for i in xrange(1,24):
		reviews = open("workersreviewdata/allreviewsofworkernew" + str(i) + ".csv").read().splitlines()
		for review in reviews:
			splits = review.split(",")
			reviewtext = splits[4].strip()
			if(reviewtext == sentence):
				count = count + 1

	return count


for i in xrange(1,24):
	reviews = open("workersreviewdata/allreviewsofworkernew" + str(i) + ".csv").read().splitlines()
	for review in reviews:
		splits = review.split(",")
		reviewtext = splits[4].strip()
		if(reviewtext not in textcountmaping.keys()):
			count = findcount(reviewtext)
			if(count > 5):
				textcountmaping[reviewtext] = count
				print reviewtext + "," + str(count)