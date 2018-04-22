import csv
import random

freelancersreviewers1 = open("usersinfo_20apps&19workers.csv").read().splitlines()
appsdict = {}

currentworkersID = 20
currentuserids = open("wokersreviewers/wokersreviewers" + str(currentworkersID) + ".csv").read().splitlines()
f = open("findtop5pps/"+ "applist" + str(currentworkersID) + ".csv",'w')
	
for reviewerinfo in freelancersreviewers1:
		splits = reviewerinfo.split(",")
		appid = splits[4].strip()
		userid = splits[2].strip().replace("/store/people/details?id=","")

		if userid in currentuserids:
			if appid in appsdict.keys():
				count = int(appsdict[appid])
				count = count + 1
				appsdict[appid] = count

			else:
				appsdict[appid] = 1

for appid in appsdict.keys():
	print appid + "," + str(appsdict[appid])
	f.write(appid + "," + str(appsdict[appid]) + "\n")

f.close()

