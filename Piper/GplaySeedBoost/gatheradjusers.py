import csv
import random

allworkersusers = open("../WorkerProfile/0.alldata/wokersreviewers/wokersreviewersall.csv").read().splitlines()


for i in xrange(1,24):
	#if i == 7 or i == 8 or i == 9 or i ==10 or i == 14 or i == 18 or i ==19 :
	#	continue

	workersusers = open("../WorkerProfile/0.alldata/wokersreviewers/wokersreviewers" + str(i) + ".csv").read().splitlines()
 
	coreviews = open("workersco-reviewwith20apps/adjcoreview" + str(i) + ".csv").read().splitlines()
	f = open('40appsadjuserlistofworker/40appsadjuserlistofworker' + str(i) + '.csv','w')
	alladjusers = []

	for coreview in coreviews:
		split = coreview.split(",")
		adjuser = split[1].strip()
		if adjuser not in alladjusers:
			alladjusers.append(adjuser)

	for adjuser in alladjusers:

		if adjuser in allworkersusers:
			continue

		totalweight = 0
		edgecount = 0
		avgedgeweight = 0

		isalreadyinanotherworker = False

		for coreview in coreviews:
			split = coreview.split(",")
			adjuser2 = split[1].strip()
			if adjuser == adjuser2:
				weight = int(split[2])
				totalweight = totalweight + weight
				edgecount = edgecount + 1

		if edgecount > 0:
			avgedgeweight = totalweight/edgecount 		

		avgedgeweight3 = 0
		edgecount3 = 0
		index3 = 0
		totalworker3 = 0

		for j in xrange(1,24):
			#if j == 7 or j == 8 or j == 9 or j ==10 or j == 14 or j == 18 or j ==19 :
			#	continue

			workersusers2 = open("../WorkerProfile/0.alldata/wokersreviewers/wokersreviewers" + str(j) + ".csv").read().splitlines()
 

			if isalreadyinanotherworker == True:
				break

			if j == i:
				continue

			coreviews2 = open("workersco-reviewwith20apps/adjcoreview" + str(j) + ".csv").read().splitlines()

			totalweight2 = 0
			edgecount2 = 0

			for coreview2 in coreviews2:
				split2 = coreview2.split(",")
				adjuser21 = split2[0].strip()

				adjuser22 = split2[1].strip()

				if adjuser == adjuser21:
					isalreadyinanotherworker = True
					continue
				
				if adjuser == adjuser22:
					#if index3 == 8:
					#	if weight2 > 7:
					#		print adjuser22

					weight2 = int(split2[2])
					totalweight2 = totalweight2 + weight2
					edgecount2 = edgecount2 + 1

			if edgecount2 > 0:	
				avgedgeweight2 = totalweight2/edgecount2
				if avgedgeweight2 > avgedgeweight3:
					
					avgedgeweight3 = avgedgeweight2
					edgecount3 = edgecount2
					index3 = j
					totalworker3 = len(workersusers2)


		#if avgedgeweight > avgedgeweight3 and avgedgeweight != 0:
		if isalreadyinanotherworker == False:
			f.write(adjuser + "," + str(edgecount) + "," + str(totalweight/edgecount) + "," + str(index3) + "," + str(edgecount3) + "," + str(avgedgeweight3) + "\n")
			