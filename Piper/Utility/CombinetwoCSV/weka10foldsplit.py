#!/usr/bin/env python

import urllib
import csv

#> dsg1 (117 documents)
#> dsg2 (14 documents)
#> dsg3 (43 documents)
#> dsg4 (25 documents)

lines = open("newdsginstanceslimited.csv").read().splitlines()

dsg1instances = []
dsg2instances = []
dsg3instances = []
dsg4instances = []

dsg1folds = []
dsg2folds = []
dsg3folds = []
dsg4folds = []

for x in xrange(1,118):
	dsg1instances.append(lines[x])
	#print lines[x]
	pass


for x in xrange(118,118+14):
	dsg2instances.append(lines[x])
	#print lines[x]
	pass

for x in xrange(132,132+43):
	dsg3instances.append(lines[x])
	#print lines[x]
	pass

for x in xrange(175,175+25):
	dsg4instances.append(lines[x])
	#print lines[x]
	pass

foldcount1 = int(len(dsg1instances)/10)

for i in xrange(0,10):
	folds = []
	for j in xrange(i*foldcount1,i*foldcount1 + foldcount1):
		folds.append(dsg1instances[j])
		print dsg1instances[j]

	dsg1folds.append(folds)	
	print "\n\n\n"

foldcount2 = int(len(dsg2instances)/10)

for i in xrange(0,10):
	folds = []
	for j in xrange(i*foldcount2,i*foldcount2 + foldcount2):
		folds.append(dsg2instances[j])
		print dsg2instances[j]

	dsg2folds.append(folds)	
	print "\n\n\n"

foldcount3 = int(len(dsg3instances)/10)

for i in xrange(0,10):
	folds = []
	for j in xrange(i*foldcount3,i*foldcount3 + foldcount3):
		folds.append(dsg3instances[j])
		print dsg3instances[j]

	dsg3folds.append(folds)	
	print "\n\n\n"

foldcount4 = int(len(dsg4instances)/10)

for i in xrange(0,10):
	folds = []
	for j in xrange(i*foldcount4,i*foldcount4 + foldcount4):
		folds.append(dsg4instances[j])
		print dsg4instances[j]

	dsg4folds.append(folds)	
	print "\n\n\n"

for i in xrange(0,10):
	trainfile = open("train"+ str(i)+".csv",'w')
	trainfile.write(lines[0] + '\n')
	
	testfile = open("test"+ str(i)+".csv",'w')
	testfile.write(lines[0] + '\n')

	for j in xrange(0,10):
		if j != i:
			fold = dsg1folds[j]
			for instance in fold:
				trainfile.write(instance + '\n')

			fold = dsg2folds[j]
			for instance in fold:
				trainfile.write(instance + '\n')
				
			fold = dsg3folds[j]
			for instance in fold:
				trainfile.write(instance + '\n')

			fold = dsg4folds[j]
			for instance in fold:
				trainfile.write(instance + '\n')

	fold = dsg1folds[i]
	for instance in fold:
		testfile.write(instance + '\n')

	fold = dsg2folds[i]
	for instance in fold:
		testfile.write(instance + '\n')
		
	fold = dsg3folds[i]
	for instance in fold:
		testfile.write(instance + '\n')

	fold = dsg4folds[i]
	for instance in fold:
		testfile.write(instance + '\n')		

	trainfile.close()
	testfile.close()	
