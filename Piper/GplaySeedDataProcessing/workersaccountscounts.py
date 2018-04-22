import csv
import random


for i in xrange(1,24):
	userids = open("wokersreviewers/wokersreviewers" + str(i) + ".csv").read().splitlines()
	seeduserids = open("Wokers.Seeds/seeds_worker" + str(i) + ".csv").read().splitlines()

	print str(len(seeduserids)) + "," + str(len(userids) - len(seeduserids))
	

