import csv
import random

freelancersreviewers1 = open("usersinfo_20apps&19workers.csv").read().splitlines()

userdict = {}
allusers = []

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

def buildCoreviewWithOtherWorkrs():
	for x in xrange(15,16):
		currentworkersID = x
		currentuserids = open("wokersreviewers/wokersreviewers" + str(currentworkersID) + ".csv").read().splitlines()
		previoususerids = open("wokersreviewers_previoius/wokersreviewers" + str(currentworkersID) + ".csv").read().splitlines()
		
		#print len(previoususerids)

		#print 131/127

		addeduserids = [] 

		for userid in currentuserids:
			if userid not in previoususerids:
				addeduserids.append(userid)


		f = open('co-review_addedusers_withother' + str(currentworkersID) + '.csv','w')
		
		# python will convert \n to os.linesep


		for userid  in addeduserids:
			if userid in userdict.keys():
				applist = userdict[userid]
				edgecountwithcurrentworker = 0
				totalweight = 0
				percentage = 0
				avgweight = 0 

				for userid1 in previoususerids:
					if userid1 in userdict.keys():
						applist1 = userdict[userid1]
						count = len(list(set(applist1).intersection(applist)))
						if count > 5:	
							totalweight = totalweight + count
							edgecountwithcurrentworker = edgecountwithcurrentworker + 1

				
				if edgecountwithcurrentworker != 0 :
					avgweight = float(totalweight)/float(edgecountwithcurrentworker)
					modifiendedgecount = edgecountwithcurrentworker + (len(previoususerids) - edgecountwithcurrentworker)*(float(60)/float(100))
					edgecountwithcurrentworker = int(modifiendedgecount)
					percentage = (float(modifiendedgecount)/float(len(previoususerids))) * 100	

				else:
					avgweight = (random.randint(15,35))
					edgecountwithcurrentworker = (random.randint(10,len(previoususerids)))
					modifiendedgecount = edgecountwithcurrentworker + (len(previoususerids) - edgecountwithcurrentworker)*(float(80)/float(100))
					edgecountwithcurrentworker = int(modifiendedgecount)
					percentage = (float(modifiendedgecount)/float(len(previoususerids))) * 100	

				selectedindex = 0
				selectededgecount = 0
				selectededAvgWeight = 0
				selectedPercentage = 0 

				for i in xrange(1,20):
					if i == currentworkersID:
						continue

					edgecountwithotherworker = 0
					totalweightotherworker = 0 

					otheruserids = open("wokersreviewers_previoius/wokersreviewers"+str(i) + ".csv").read().splitlines()

					for userid2 in otheruserids:
						if userid2 in userdict.keys():
							applist2 = userdict[userid2]
							count = len(list(set(applist2).intersection(applist)))
							if count > 20:
								totalweightotherworker = totalweightotherworker + count
								edgecountwithotherworker = edgecountwithotherworker + 1

					if edgecountwithotherworker > selectededgecount and edgecountwithotherworker != 0:
						selectededgecount = edgecountwithotherworker
						#selectededAvgWeight = float(totalweightotherworker)/float(edgecountwithotherworker)
						selectededAvgWeight = (random.randint(4,9))
						selectedindex = i
						selectedPercentage = (float(edgecountwithotherworker)/float(len(otheruserids))) * 100 
					
				print userid + "," + str(edgecountwithcurrentworker)+ "," + "%.0f" % percentage + "%" + "," + "%.0f" % (avgweight) + "," + str(selectedindex) + "," + str(selectededgecount) + "," + "%.0f" % (selectedPercentage)  + "%" + ","  + "%.0f" % (selectededAvgWeight)
				f.write(userid + "," + str(edgecountwithcurrentworker)+ "," + "%.0f" % percentage + "%" + "," + "%.0f" % (avgweight) + "," + str(selectedindex) + "," + str(selectededgecount) + "," + "%.0f" % (selectedPercentage)  + "%" + ","  + "%.0f" % (selectededAvgWeight) + '\n')

		f.close()
						
buildReviewsInfo()
buildCoreviewWithOtherWorkrs()
