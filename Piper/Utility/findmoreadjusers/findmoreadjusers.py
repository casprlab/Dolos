import csv
import random

usersinfo = open("usersinfo20&19.csv").read().splitlines()
otherusers = open("usersall20.csv").read().splitlines()
users = open("re-wokersreviewers6.csv").read().splitlines()

userdict = {}
allusers = []

def buildReviewsInfo():
	for reviewerinfo in usersinfo:
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


def buildCoreviewWithOtherWorkrs():
	#currentworkersID = x
	#currentuserids = open("wokersreviewers" + str(currentworkersID) + ".csv").read().splitlines()
	#edgecount = 0

	f1 = open('adjcoreview6.csv','w')
	f2 = open('adjusers6.csv','w')




	for user1 in users:
		if user1 in userdict.keys():
			applist1 = userdict[user1]
			for user2 in otherusers:
				if user2 in userdict.keys():
					applist2 = userdict[user2]
					count = len(list(set(applist1).intersection(applist2)))
					if count > 10:				
						print user1 + "," + user2 + "," + str(count)
						f1.write(user1 + "," + user2 + "," + str(count) + '\n')
						f2.write(user2 + '\n')
						
	f1.close()
	f2.close()
	
buildReviewsInfo()
buildCoreviewWithOtherWorkrs()











