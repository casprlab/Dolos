import csv
import random

apps = open("appsforcoreview.csv").read().splitlines()
reviews = open("allrevieweinformation244+53+400.csv").read().splitlines()
reviewersinfo = open("usersinfo_40+37&19w+244.csv").read().splitlines()

userdict = {}
userids = []

def buildReviewsInfo():
	print 'building applist'

	for reviewerinfo in reviewersinfo:
		#print reviewerinfo
		reviewstring = reviewerinfo.replace('"','')
		splits = reviewstring.split(",")

		if len(splits) < 7:
			continue 

		appid = splits[4].strip().replace('"','').replace('id=','')
		userid = splits[2].strip().replace("/store/people/details?id=","")

		if userid == '':
			continue

		if userid not in userids:
			continue

		#print userid	

		if userid in userdict.keys():
			applist = userdict[userid]
			if appid not in applist:
				applist.append(appid)
				userdict[userid] = applist
		else:
			userdict[userid] = [appid]

	
for app in apps:
	userids = []
	userdict = {}
	for review in reviews:
		splits = review.split(",")
		appid = splits[len(splits) -1].strip()
		if appid == app:
			userid = splits[1].strip().replace("/store/people/details?id=","")
			if userid not in userids:
				userids.append(userid)

	print app + " total users found " + str(len(userids))
	#print userids 			

	buildReviewsInfo()			


	f = open("co-review/" + app + ".csv",'w')
	
	for i in xrange(0,len(userids)):
		userid1 = userids[i]
		if userid1 in userdict.keys():
			applist1 = userdict[userid1]

			for j in xrange(i+1,len(userids)):
				userid2 = userids[j]
				if userid2 in userdict.keys():
					applist2 = userdict[userid2]
					count = len(list(set(applist1).intersection(applist2)))
					if count >= 3:
						#print userid1 + "," + userid2 + "," + str(count)
						f.write(userid1 + "," + userid2 + "," + str(count) + '\n') 

	f.close()		
