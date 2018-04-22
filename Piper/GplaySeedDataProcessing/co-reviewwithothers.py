import csv
import random

freelancersreviewers1 = open("usersinfo_20apps&19workers.csv").read().splitlines()
userids = open("wokersreviewers3.csv").read().splitlines()

userdict = {}
allusers = []

def combineAllusers():
	for i in xrange(1,20):
		users = open("wokersreviewers" + str(i) + ".csv").read().splitlines()
		for user in users:
			if user in allusers:
				print user + "," + str(i)

def buildReviewsInfo():
	for reviewerinfo in freelancersreviewers1:
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
	for i in xrange(0,len(userids)):
		userid1 = userids[i]
		if userid1 in userdict.keys():
			applist1 = userdict[userid1]

			for j in xrange(i+1,len(userids)):
				userid2 = userids[j]
				if userid2 in userdict.keys():
					applist2 = userdict[userid2]
					count = len(list(set(applist1).intersection(applist2)))
					if count > 0:
						print userid1 + "," + userid2 + "," + str(count + random.randint(0,5))

def addworkerid():
	f = open('myfile.csv','a')
	for i in xrange(1,20):
		lines = open("wokercoreview"+str(i) + ".csv").read().splitlines()
		for line in lines:
			f.write(line + "," + str(i) + "," + str(i) + '\n')
	f.close()


def buildCoreviewWithOtherWorkrs():
	for x in xrange(19,20):
		currentworkersID = x
		currentuserids = open("wokersreviewers" + str(currentworkersID) + ".csv").read().splitlines()
		edgecount = 0

		#print currentworkersID

		f = open('co-reviewwithother' + str(currentworkersID) + '.csv','w')
		
		# python will convert \n to os.linesep


		for i in xrange(1,20):
			#if i == currentworkersID: 
			#	continue

			#if i == 15 and currentworkersID == 3: 
			#	continue	
			

			userids = open("wokersreviewers"+str(i) + ".csv").read().splitlines()

			print i

			for user1 in userids:
				#print user1
				if user1 in userdict.keys():
					applist1 = userdict[user1]
					for user2 in currentuserids:
						if user2 in userdict.keys():
							applist2 = userdict[user2]
							count = len(list(set(applist1).intersection(applist2)))
							if count > 8:
								edgecount = edgecount + 1

								print user1 + "," + user2 + "," + str(count) + "," + str(i) + "," + str(currentworkersID)
								f.write(user1 + "," + user2 + "," + str(count) + "," + str(i) + "," + str(currentworkersID)+'\n')

								#print user1 + "," + user2 + "," + str(count - 5) + "," + str(i) + "," + str(currentworkersID)
								#f.write(user1 + "," + user2 + "," + str(count - 5) + "," + str(i) + "," + str(currentworkersID)+'\n')
		print edgecount
		f.close()
						
buildReviewsInfo()
#addworkerid()
#buildIndivisualCoreview()
buildCoreviewWithOtherWorkrs()
