import csv
import random



#usersinfo = open("../WorkerProfile/0.alldata/usersinfo_40apps&19workers.csv").read().splitlines()
#otherusers = open("../WorkerProfile/0.alldata/usersall40.csv").read().splitlines()

usersinfo = open("usersinfo_40+37&19w.csv").read().splitlines()
otherusers = open("boosterusers.csv").read().splitlines()

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

	for worker in xrange(1,24):
		print str(worker)

		users = open("../WorkerProfile/0.alldata/wokersreviewers/wokersreviewers" + str(worker) + ".csv").read().splitlines()

		f1 = open('workersco-reviewwith20apps/adjcoreview' + str(worker) + '.csv','w')
		#f2 = open('adjusers' + str(worker) + '.csv','w')
 
		for user1 in users:
			if user1 in userdict.keys():
				applist1 = userdict[user1]
				for user2 in otherusers:
					if user2 in userdict.keys():
						applist2 = userdict[user2]
						count = len(list(set(applist1).intersection(applist2)))
						if count > 8:				
							#print user1 + "," + user2 + "," + str(count)
							f1.write(user1 + "," + user2 + "," + str(count) + '\n')
							#f2.write(user2 + '\n')
							
		f1.close()
		#f2.close()

	
buildReviewsInfo()
buildCoreviewWithOtherWorkrs()