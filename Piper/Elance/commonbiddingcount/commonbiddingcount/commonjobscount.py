#/usr/bin/python
import csv

all40winner = open("all40winner.csv").readlines()
biddersprojects = open("fraudplacedbidding.csv").readlines()

# allfraudbiddesinfo = open("fraudbiddersprojectsCount.csv").readlines()

# allfraudbiddes = []

# for line in allfraudbiddesinfo:
# 	split = line.split(",")
# 	allfraudbiddes.append(split[0])

#print allfraudbiddes
	


bidders = []
projects = []

commonjobscountfile = csv.writer(open("commonjobscountplacebidding.csv", "wb"))

for line in biddersprojects:
	split = line.split(",")
	bidders.append(split[0])
	projects.append(split[1].replace("\r\n",""))

#print bidders
#print projects	

for i in xrange(0,len(all40winner)):
	for j in xrange(i+1,len(all40winner)):
		winner1 = all40winner[i].replace("\r\n","")
		winner2 = all40winner[j].replace("\r\n","")

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
		
	


	




		
