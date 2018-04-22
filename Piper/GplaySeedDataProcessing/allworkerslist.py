import csv
import random

f = open('wokersreviewers/wokersreviewersall.csv','w')

for i in xrange(1,24):
	#if i == 22:
	#	continue
	userids = open("wokersreviewers/wokersreviewers" + str(i) + ".csv").read().splitlines()

	#print str(len(userids))

	for userid in userids:
		f.write(userid + "\n")
