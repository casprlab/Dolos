
import csv

workerid = 1

reviewersinfofile1 = "allreviews/allrevieweinformation1.csv"
reviewersinfolines1 = open(reviewersinfofile1).read().splitlines()

reviewersinfofile2 = "allreviews/allrevieweinformation2.csv"
reviewersinfolines2 = open(reviewersinfofile2).read().splitlines()

reviewersinfofile3 = "allreviews/allrevieweinformation3.csv"
reviewersinfolines3 = open(reviewersinfofile3).read().splitlines()

reviewfile = open("workersreviewdata/allreviewsofworkernew" + str(workerid) + ".csv",'w')

count = 0
reviewersinfoHash = {}
userdict = {}
ssum = 0

filename = "wokersreviewers/wokersreviewers" + str(workerid) + ".csv"
alluserids = open(filename).read().splitlines()


def calc(reviewersinfofile):
	#print workerid

	filename = "wokersreviewers/wokersreviewers" + str(workerid) + ".csv"
	alluserids = open(filename).read().splitlines()

	global count
	global ssum
	for reviewerinfoline in reviewersinfofile:
		#print reviewerinfoline
		splits = reviewerinfoline.split(",")

		#print splits[1]

		#print reviewerinfoline

		userid =  splits[1].strip().replace("/store/people/details?id=","")
		appid = splits[6].strip()

		value = splits[5].strip()

		if value != '100%':
			continue

		if userid in alluserids:
			reviewersinfoHash[appid] = ""
			
			if userid not in userdict.keys():
				count = count + 1
				userdict[userid] = [appid]
				ssum = ssum + len(splits[4].split())
				reviewfile.write(reviewerinfoline + '\n')
			else:
				userapplist = userdict[userid]
				if appid not in userapplist:
					count = count + 1
					ssum = ssum + len(splits[4].split())
					userapplist.append(appid)
					userdict[userid] = userapplist
					reviewfile.write(reviewerinfoline + '\n')
		
for x in xrange(1,2):
	workerid = x

	count = 0
	reviewersinfoHash = {}
	userdict = {}
	ssum = 0

	filename = "wokersreviewers/wokersreviewers" + str(workerid) + ".csv"
	alluserids = open(filename).read().splitlines()

	reviewfile = open("workersreviewdata/allreviewsofworkernew" + str(workerid) + ".csv",'w')

	calc(reviewersinfolines1)
	calc(reviewersinfolines2)
	calc(reviewersinfolines3)

	avgwordcount = ssum/count
	print str(len(alluserids)) + "," + str(len(userdict.keys())) + "," + str(len(reviewersinfoHash.keys())) + "," + str(count) + "," + str(avgwordcount)

#print userdict

# c = csv.writer(open("output.csv", "wb"))

# for line in lines:
# 	split = line.split(",")
# 	c.writerow([split[5],split[6]]) 




