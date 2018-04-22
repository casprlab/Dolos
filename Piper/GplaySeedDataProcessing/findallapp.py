
import csv

appidfile = open("allappidsfromreviewofworker.csv",'w')

count = 0

for i in xrange(1,24):
	reviews = open("workersreviewdata/allreviewsofworkernew" + str(i) + ".csv").read().splitlines()
	for review in reviews:
		splits = review.split(",")
		appid = splits[6]
		appidfile.write(appid + '\n')
		count = count + 1

print count		