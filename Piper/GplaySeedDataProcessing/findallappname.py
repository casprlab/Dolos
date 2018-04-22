import csv
import random

usersinfo = open("usersinfo_40+37&19w.csv").read().splitlines()
#userids = open("wokersreviewers/wokersreviewersall.csv").read().splitlines()
f = open("allappname.csv",'w')

def allappnameFiltered():

	for i in xrange(1,24):

		appidDict = {}

		users = open("wokersreviewers/wokersreviewers" + str(i) + ".csv").read().splitlines()
		for reviewerinfo in usersinfo:
		#print reviewerinfo
			splits = reviewerinfo.split(",")
			appid = splits[4].strip()
			userid = splits[2].strip().replace("/store/people/details?id=","")

			if userid in users:
				appid = splits[len(splits) - 4]
				if appid in appidDict.keys():
					appcount = int(appidDict[appid])
					appcount = appcount + 1
					appidDict[appid] = appcount
				else:
					appidDict[appid] = 1

		for appid in appidDict.keys():
			if int(appidDict[appid]) >= 5:
				f.write(appid + "\n")

		print str(i) + "," + str(len(appidDict.keys()))		

					
def allappname():
	f = open("allappname.csv",'w')
	
	for i in xrange(1,24):

		appidDict = {}

		users = open("wokersreviewers/wokersreviewers" + str(i) + ".csv").read().splitlines()

		for reviewerinfo in usersinfo:
			#print reviewerinfo
			splits = reviewerinfo.split(",")
			appid = splits[4].strip()
			userid = splits[2].strip().replace("/store/people/details?id=","")

			if userid in users:
				appid = splits[len(splits) - 4]
				f.write(appid + "\n")


allappname()
#allappnameFiltered()