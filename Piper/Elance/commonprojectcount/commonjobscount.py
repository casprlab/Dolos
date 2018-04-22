#/usr/bin/python
import csv

fraudwinners = open("fraudwinners.csv").readlines()
biddersprojects = open("BidderProjectListNoDuplicate.csv").readlines()

allfraudbiddesinfo = open("fraudbiddersprojectsCount.csv").readlines()

allfraudbiddes = []

for line in allfraudbiddesinfo:
	split = line.split(",")
	allfraudbiddes.append(split[0])

#print allfraudbiddes
	


bidders = []
projects = []

commonjobscountfile = csv.writer(open("commonjobscount.csv", "wb"))

for line in biddersprojects:
	split = line.split(",")
	bidders.append(split[0])
	projects.append(split[1].replace("\r\n",""))

#print bidders
#print projects	

for i in xrange(0,len(allfraudbiddes)):
	for j in xrange(i+1,len(allfraudbiddes)):
		winner1 = allfraudbiddes[i].replace("\r\n","")
		winner2 = allfraudbiddes[j].replace("\r\n","")

		list1 = []
		list2 = []

		for k in xrange(0,len(bidders)):
			if bidders[k] == winner1:
				list1.append(projects[k])

			if bidders[k] == winner2:
				list2.append(projects[k])

		#print list1
		#print list2
					

		commonlist = list(set(list1) & set(list2))

		if len(commonlist) > 0:
			commonjobscountfile.writerow([winner1,winner2,len(commonlist)])
		
	


	




		
