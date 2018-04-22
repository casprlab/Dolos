import csv
import random



def buildCoreviewWithOtherWorkrs():

	currentworkersID = 15
	currentuserids = open("wokersreviewers" + str(currentworkersID) + ".csv").read().splitlines()
	for i in xrange(1,24):

		userids = open("wokersreviewers"+str(i) + ".csv").read().splitlines()

		print i

		for user1 in userids:
			if user1 in currentuserids:
				print user1 + "," + str(i)

buildCoreviewWithOtherWorkrs()
