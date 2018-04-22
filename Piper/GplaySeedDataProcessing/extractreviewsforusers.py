import csv

outputfile = open("wokersreviewersallreviews.csv",'w')
users = open("wokersreviewersall.csv").read().splitlines()
reviews = open("allreviews/allrevieweinformation1.csv").read().splitlines()

for review in reviews:
	splits = review.split(",")
 	userid = splits[1].strip().replace("/store/people/details?id=","")
 	if userid in users:
 		outputfile.write(review + "\n")

reviews = open("allreviews/allrevieweinformation2.csv").read().splitlines()

for review in reviews:
	splits = review.split(",")
 	userid = splits[1].strip().replace("/store/people/details?id=","")
 	if userid in users:
 		outputfile.write(review + "\n")

reviews = open("allreviews/allrevieweinformation3.csv").read().splitlines()

for review in reviews:
	splits = review.split(",")
 	userid = splits[1].strip().replace("/store/people/details?id=","")
 	if userid in users:
 		outputfile.write(review + "\n")
