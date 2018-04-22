import csv
import random

#usersinfo = open("freelancersreviewersall.csv").read().splitlines()
#userids = open("wokersreviewers3.csv").read().splitlines()

userdict = {}
allusers = []

usersinfo = open("usersinfo.csv").read().splitlines()
userids = open("users.csv").read().splitlines()


def combineAllusers():
	for i in xrange(1,20):
		users = open("wokersreviewers" + str(i) + ".csv").read().splitlines()
		for user in users:
			if user in allusers:
				print user + "," + str(i)

def buildReviewsInfo():
	for reviewerinfo in usersinfo:
		#print reviewerinfo
		splits = reviewerinfo.split(",")
		appid = splits[4].strip()
		userid = splits[2].strip().replace("/store/people/details?id=","")

		if userid in userdict.keys():
			applist = userdict[userid]
			if appid not in applist:
				applist.append(appid)
				userdict[userid] = applist
		else:
			userdict[userid] = [appid]

def buildIndivisualCoreview():
	f = open('coreview.csv','w')
	
	for i in xrange(0,len(userids)):
		userid1 = userids[i]
		if userid1 in userdict.keys():
			applist1 = userdict[userid1]

			for j in xrange(i+1,len(userids)):
				userid2 = userids[j]
				if userid2 in userdict.keys():
					applist2 = userdict[userid2]
					count = len(list(set(applist1).intersection(applist2)))
					if count > 6:
						print userid1 + "," + userid2 + "," + str(count)
						f.write(userid1 + "," + userid2 + "," + str(count) + '\n') 

	f.close()
					

buildReviewsInfo()
buildIndivisualCoreview()