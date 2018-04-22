import csv
import os
import string
#import time
#import sys

#reviewfile = open("allreviewsofworker1.csv",'w')

#allapplist = open("752applist.csv").read().splitlines()

#allapplist = open("allappidsfromreviewunique.csv").read().splitlines()

allapplist = open("24workers_allappids4.csv").read().splitlines()

allappfile = open("allapplist.txt",'w')


def getreviewCountfromOnefile(reviewfile):
	reviews = open(reviewfile).read().splitlines()
	count = 0
	for review in reviews:
		split = review.split(",")
	 	if appid == split[6].strip():
	 		count = count + 1

	return count

def getreviewCount(appid):
	reviewfiles = ["allreviewinformation1stset.csv" , "allreviewinformation2ndset.csv" , "allreviewinformation3rdset.csv"]
	userdict = {}

	for i in xrange(0,3):
		count = getreviewCountfromOnefile(reviewfiles[0])
		if count != 0:
			return count

def addPunc(text):
	lastchar = text[len(text) - 1]
	if lastchar not in set(string.punctuation):
		text = text +"."
	return text

def processAllReviewsofworkers():
	datafilepath = 'data'
	if not  os.path.isdir(datafilepath):
		os.mkdir(datafilepath)

	for i in xrange(1,24):
		
		workerappcount = 0
		#print "file"
		reviews = open("workersreviewdata/allreviewsofworkernew" + str(i) + ".csv").read().splitlines()

		appreviews = ""
		reviewcount = 0

		for review in reviews:
			splits = review.split(",")
			appidinreview = splits[6].strip()
			#print appidinreview
			review = splits[4].strip()

			if review != "":
				if reviewcount == 0:
					appreviews = addPunc(review)
					reviewcount = reviewcount + 1
					continue
				appreviews = appreviews + " " + addPunc(review)
				reviewcount = reviewcount + 1

		f = open("data/"+ "worker" + str(i) + "." + str(reviewcount) + ".txt",'w')
		f.write(appreviews)
		f.close()
			

#processAllReviewsofworkers()			





def processInstancesforusers():
		datafilepath = 'data'
		if not  os.path.isdir(datafilepath):
			os.mkdir(datafilepath)

		for i in xrange(1,20):
			workerfilepath = datafilepath + "/worker" + str(i)			
			if not  os.path.isdir(workerfilepath):
				os.mkdir(workerfilepath)
				#print "file"			


			workerappcount = 0
			#print "file"
			reviews = open("allreviewsofworker" + str(i) + ".csv").read().splitlines()

			users = open("re-wokersreviewers" + str(i) + ".csv").read().splitlines()

			for user in users:
				appreviews = ""
				reviewcount = 0

				for review in reviews:
					splits = review.split(",")
					appidinreview = splits[6].strip()
					#print appidinreview
					review = splits[4].strip()
					ratingvalue = splits[5].strip()
					userid = splits[1].strip().replace("/store/people/details?id=","")

					if ratingvalue != '100%':
						continue

					if userid == user and review != "":
						#print appidinreview
						if reviewcount == 0:
							appreviews = addPunc(review)
							reviewcount = reviewcount + 1
							continue
						appreviews = appreviews + " " + addPunc(review)
						reviewcount = reviewcount + 1
						
				if reviewcount > 2:
					#print appid
					if reviewcount < 5:
						reviewcount = 5
					f = open(workerfilepath+ "/" + user + "." + str(reviewcount) + ".txt",'w')
					f.write(appreviews)
					f.close()
					workerappcount = workerappcount + 1

			print str(i) + "," + str(workerappcount)			


#processInstancesforusers()				
	
	
def processInstances():
	datafilepath = 'data'
	if not  os.path.isdir(datafilepath):
		os.mkdir(datafilepath)

	appidtracker = {}	

	for i in xrange(18,19):
		# go = 0

		# if i == 6 or i == 13 or i == 15 or i == 18 or i == 20 or i == 22 or i == 23 :
		# 	go = 1

		# if go == 0:
		# 	continue 

		instancecount = 0
		workerappcount = 0
		#print "file"
		reviews = open("workersreviewdata/allreviewsofworkernew" + str(i) + ".csv").read().splitlines()
		users = open("wokersreviewers/wokersreviewers" + str(i) + ".csv").read().splitlines()

		#users = open("re-wokersreviewers" + str(i) + ".csv").read().splitlines()

		#print users

		workerfilepath = datafilepath + "/worker" + str(i)			
		if not  os.path.isdir(workerfilepath):
			os.mkdir(workerfilepath)
			#print "file"			

		for appid in allapplist:
			appreviews = ""
			reviewcount = 0
			#print appid

			for review in reviews:
				splits = review.split(",")
				appidinreview = splits[6].strip()
				#print appidinreview
				review = splits[4].strip()
				ratingvalue = splits[5].strip()
				userid = splits[1].strip().replace("/store/people/details?id=","")

				#print userid

				#if userid in users:
					#print userid
					
				if ratingvalue != '100%':
					continue

				if userid not in users:
					continue

				if appidinreview == appid and review != "":
					#print appidinreview
					if reviewcount == 0:
						appreviews = addPunc(review)
						reviewcount = reviewcount + 1
						continue
					appreviews = appreviews + " " + addPunc(review)
					reviewcount = reviewcount + 1
			
			if reviewcount >= 5:

				# if appid in appidtracker.keys():
				# 	wcount =  int(appidtracker[appid])
				# 	appidtracker[appid] = wcount + 1
				# else:
				# 	appidtracker[appid] = 1				

				instancecount = instancecount + 1
				#print appid
				#if reviewcount < 5:
				#	reviewcount = 5

				#f = open(workerfilepath+ "/" + appid + "." + str(reviewcount) + ".txt",'w')
				#f.write(appreviews)
				#f.close()


				workerappcount = workerappcount + 1
				allappfile.write(appid + '\n')
				print appid 

		print str(i) + "," + str(workerappcount)


	#for appid in appidtracker.keys():
	#	print appid + "," + str(int(appidtracker[appid])) 

processInstances()
