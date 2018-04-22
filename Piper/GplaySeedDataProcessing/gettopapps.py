import csv

appstatfile = open("allappsstatfromworker.csv",'w')

allapplist = open("24workers_allappids2.csv").read().splitlines()

allappidlists = []

def getreviewCountfromOnefile(reviewfile):
	reviews = open(reviewfile).read().splitlines()
	count = 0
	for review in reviews:
		split = review.split(",")
	 	if appid == split[6].strip():
	 		count = count + 1

	return count

def getreviewCount(appid):
	reviewfiles = ["reviews3rdset/allrevieweinformation1.csv" , "reviews3rdset/allrevieweinformation2.csv" , "reviews3rdset/allrevieweinformation3.csv"]
	userdict = {}

	for i in xrange(0,3):
		count = getreviewCountfromOnefile(reviewfiles[0])
		if count != 0:
			return count	


for i in xrange(1,24):
	go = 0

	if i == 6 or i == 13 or i == 15 or i == 18 or i == 20 or i == 22 or i == 23 :
		go = 1

	if go == 0:
		continue 

	reviews = open("workersreviewdata/allreviewsofworkernew" + str(i) + ".csv").read().splitlines()
	appids = []
	for review in reviews:
		splits = review.split(",")
		appid = splits[6].strip()
		appids.append(appid)

	allappidlists.append(appids)

for appid in allapplist:
	totalreviewcount = getreviewCount(appid)	
	count = 0
	reviewcount = 0
	s=""
	i = 0
	for appidlist in allappidlists:
		i = i + 1
		if appid in appidlist:
			if appidlist.count(appid) > 4:
				reviewcount = reviewcount + appidlist.count(appid)	
				count = count + 1
				s = s+","+ str(i) + "," +  str(appidlist.count(appid))

	if count  >= 1:
		print appid + "," + str(totalreviewcount) + "," + str(reviewcount) + "," + str(count) + s
		appstatfile.write(appid + "," + str(totalreviewcount) + "," + str(reviewcount) + "," + str(count) + s)


	#print appid + "," + s