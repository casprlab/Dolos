import csv
import random

#usersinfo = open("freelancersreviewersall.csv").read().splitlines()
#userids = open("wokersreviewers3.csv").read().splitlines()

#userdict = {}
allusers = []

usersinfo = open("usersinfo_40+37&19w+244+400.csv").read().splitlines()

def combineAllusers():
	for i in xrange(1,24):
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
	for x in xrange(1,24):
		worker = x

		userids = open("wokersreviewers/wokersreviewers" + str(worker) + ".csv").read().splitlines()
		
		f = open("wokercoreview2/wokercoreview" + str(worker) + ".csv",'w')
		
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

def buildCoreviewForAll():
	userids = open("wokersreviewers/wokersreviewersall.csv").read().splitlines()	
	f = open("wokercoreview/wokercoreviewall.csv",'w')
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


def buildCoreviewForUsers():
	#worker = x

	userids = open("wokersreviewers/wokersreviewers7.csv").read().splitlines()
	
	f = open("wokercoreview/wokercoreview7.csv",'w')
	
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


def buildReviewsInfoForUserids(userids):
	usersdictionary = {}

	for reviewerinfo in usersinfo:
		#print reviewerinfo
		splits = reviewerinfo.split(",")

		if len(splits) >= 5:
			#print reviewerinfo
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
	for x in xrange(1,24):
		worker = x

		userids = open("wokersreviewers/wokersreviewers" + str(worker) + ".csv").read().splitlines()
		
		f = open("wokercoreview/wokercoreview" + str(worker) + ".csv",'w')

		usersdictionary = buildReviewsInfoForUserids(userids)

		for i in xrange(0,len(userids)):
			userid1 = userids[i]
			if userid1 in usersdictionary.keys():
				applist1 = usersdictionary[userid1]

				for j in xrange(i+1,len(userids)):
					userid2 = userids[j]
					if userid2 in usersdictionary.keys():
						applist2 = usersdictionary[userid2]
						count = len(list(set(applist1).intersection(applist2)))
						if count > 6:
							print userid1 + "," + userid2 + "," + str(count)
							f.write(userid1 + "," + userid2 + "," + str(count) + '\n') 

		f.close()


def buildCoreviewFromUsers():
	userids = open("wokersreviewersall.csv").read().splitlines()
	
	f = open("wokersreviewersallcoreview.csv",'w')

	usersdictionary = buildReviewsInfoForUserids(userids)

	for i in xrange(0,len(userids)):
		userid1 = userids[i]
		if userid1 in usersdictionary.keys():
			applist1 = usersdictionary[userid1]

			for j in xrange(i+1,len(userids)):
				userid2 = userids[j]
				if userid2 in usersdictionary.keys():
					applist2 = usersdictionary[userid2]
					count = len(list(set(applist1).intersection(applist2)))
					if count > 6:
						print userid1 + "," + userid2 + "," + str(count)
						f.write(userid1 + "," + userid2 + "," + str(count) + '\n') 

	f.close()

buildIndivisualCoreviewDiffentWay()
#buildCoreviewFromUsers() 


					
#buildReviewsInfo()
#buildIndivisualCoreview()
#buildCoreviewForAll()
#buildCoreviewForUsers()



