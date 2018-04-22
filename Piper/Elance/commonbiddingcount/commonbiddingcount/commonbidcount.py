#/usr/bin/python
import csv


allfraudbiddesinfo = open("winnerprojects.csv").readlines()

distinctbidders = []

bidders = []
projects = []

commonjobscountfile = csv.writer(open("commonjobscount.csv", "wb"))

for line in allfraudbiddesinfo:
	split = line.split(",")

	if split[0] not in distinctbidders:
		distinctbidders.append(split[0])


	bidders.append(split[0])
	projects.append(split[1].replace("\r\n",""))

print distinctbidders

print len(distinctbidders)




for i in xrange(0,len(distinctbidders)):
	for j in xrange(i+1,len(distinctbidders)):
		winner1 = distinctbidders[i].replace("\r\n","")
		winner2 = distinctbidders[j].replace("\r\n","")

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
		
	


	




		
