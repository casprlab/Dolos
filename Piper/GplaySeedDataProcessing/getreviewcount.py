import csv

#allapplist = open("data/worker10apps.csv").read().splitlines()
allapplist = open("640apps.csv").read().splitlines()


def getreviewCountfromOnefile(app,reviewfile):
	reviews = open(reviewfile).read().splitlines()
	count = 0
	users = []
	for review in reviews:
		splits = review.split(",")
	 	appid = splits[6].strip()

	 	if appid == app:
	 		userid = splits[2].strip().replace("/store/people/details?id=","")
			if userid not in users:
				users.append(userid)
		 		count = count + 1

	return count


reviewfiles = ["allreviews/allrevieweinformation1.csv" , "allreviews/allrevieweinformation2.csv" , "allreviews/allrevieweinformation3.csv"]

for app in allapplist:
	#print app
	for x in xrange(0,3):
		count = getreviewCountfromOnefile(app,"reviews_640apps_new.csv")
		if count > 0:
			print app + "," + str(count)
			break
