import csv
import random

#usersinfo = open("freelancersreviewersall.csv").read().splitlines()
#userids = open("wokersreviewers3.csv").read().splitlines()

userdict = {}
allusers = []

wokersreviewersforindexing = open("wokersreviewersforindexing.csv").read().splitlines()

for testuser in wokersreviewersforindexing:
	found = False	
	for x in xrange(1,24):
		worker = x

		userids = open("wokersreviewers/wokersreviewers" + str(worker) + ".csv").read().splitlines()

		if testuser in userids:
			found = True
			print testuser +  " " + str(x)

	if found == False:
		print testuser + " " + str(0)
		#print testuser
		




