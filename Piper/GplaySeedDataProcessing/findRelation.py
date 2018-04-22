import csv
import random

#usersinfo = open("freelancersreviewersall.csv").read().splitlines()
#userids = open("wokersreviewers3.csv").read().splitlines()

#userdict = {}
allusers = []

usersinfo = open("usersinfo_40+37&19w+244.csv").read().splitlines()


def buildReviewsInfoForUserids(userids):
	usersdictionary = {}

	for reviewerinfo in usersinfo:
		#print reviewerinfo
		splits = reviewerinfo.split(",")
		appid = splits[4].strip()
		userid = splits[2].strip().replace("/store/people/details?id=","")

		if userid in userids:
			if userid in usersdictionary.keys():
				applist = usersdictionary[userid]
				if appid not in applist:
					applist.append(appid)
					usersdictionary[userid] = applist
			else:
				usersdictionary[userid] = [appid]

	return usersdictionary;			


def buildIndivisualCoreviewDiffentWay():
	alluserids = open("wokersreviewers/wokersreviewersall.csv").read().splitlines()
	finduserids = open("findrelationofnodes.csv").read().splitlines()
	
	userids = alluserids + finduserids

	f = open("wokercoreview/wokercoreview_find.csv",'w')

	usersdictionary = buildReviewsInfoForUserids(userids)

	for i in xrange(0,len(finduserids)):
		userid1 = finduserids[i]
		if userid1 in usersdictionary.keys():
			applist1 = usersdictionary[userid1]

			for j in xrange(i+1,len(alluserids)):
				userid2 = alluserids[j]
				if userid2 in usersdictionary.keys():
					applist2 = usersdictionary[userid2]
					count = len(list(set(applist1).intersection(applist2)))
					if count > 6:
						print userid1 + "," + userid2 + "," + str(count)
						f.write(userid1 + "," + userid2 + "," + str(count) + '\n') 

	f.close()


buildIndivisualCoreviewDiffentWay()
					
#buildReviewsInfo()
#buildIndivisualCoreview()
#buildCoreviewForAll()
#buildCoreviewForUsers()



