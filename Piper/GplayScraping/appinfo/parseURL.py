#!/usr/bin/env python
from bs4 import BeautifulSoup
import urllib
import urllib2
import datetime
import csv
import os
import time
import sys

servername = 'server3'

reload(sys)
sys.setdefaultencoding("utf-8")

print 'App information Fetcing Job stated on ' + str(datetime.datetime.now())

#from similarapps import fetchAllSimilarApps
#from samedeveloper import fetchAllAppsSameDev

start_time = time.time()

today = datetime.date.today()
date = today.strftime("%m-%d-%Y")

logfname = '/tmp/appinfoFetchinglog.txt'
logf = open(logfname,'a')
logf.write('Job stated on ' + date + '\n')
logf.close()


fname1 = "allinfo.csv"

if os.path.isfile(fname1) == False:
		c = csv.writer(open(fname1, "wb"))
		c.writerow(["appid","Date","Name","Developer","AppType","BadgeTitle","Size","AndroidVersion","downloadCount","version","updateDate","reviewsCount","avg_rating","developer_email","developer_website","price"])
		

def getElementValue(soup,elementName,attibute,attibuteVal):
	contentDiv = soup.findAll(elementName,{attibute:attibuteVal})

	if len(contentDiv) < 1:
		return ""

	contentValue = contentDiv[0]
	return contentValue.getText()	


def getContentValue(soup,contentName):
	return getElementValue(soup,'div','itemprop',contentName)

def fetch_thing(url):
	try:
		f = urllib.urlopen(url)
		return (f.read(), f.code)
	except:
		return (None, 404)
    

def storeInCSV(html,appid):

	print "app infor for " + appid

	path = 'data/' + appid
	
	lastDate = None

	soup = BeautifulSoup(html)

	#print getContentValue(soup,'numDownloads')
	#print getContentValue(soup,'datePublished')
	#print getContentValue(soup,'softwareVersion')
	#print getElementValue(soup,'span','class','reviews-num')
	#print today.strftime("%m-%d-%Y")

	fname = path + "/data.csv"
	if os.path.isfile(fname) == False:
		c = csv.writer(open(fname, "wb"))
		c.writerow(["Date","Name","Developer","AppType","BadgeTitle","Size","AndroidVersion","downloadCount","version","updateDate","reviewsCount","avg_rating","developer_email","developer_website","price"])
		#print "CSV created"
		 

	name = getElementValue(soup,'div','class','document-title')
	downloadCount = getContentValue(soup,'numDownloads')
	version = getContentValue(soup,'softwareVersion')
	updateDate = getContentValue(soup,'datePublished')
	reviewCount = getElementValue(soup,'span','class','reviews-num')

	developer = soup.findAll("a",{"class":"document-subtitle primary"})[0].getText()
	
	apptypecontent = soup.findAll("a",{"class":"document-subtitle category"})

	apptype = ''
	if len(apptypecontent) > 0:
		apptype = apptypecontent[0].getText()



	badgeTitle = getElementValue(soup,'span','class','badge-title') 
	size = getContentValue(soup,'fileSize')
	androidVersion = getContentValue(soup,'operatingSystems')
	avg_score = getElementValue(soup,'div','class','score')
	price = getElementValue(soup,'button','class','price buy')  

	developer_website = ""
	developer_email = ""


	developer_website_a_all = soup.findAll("a",{"class":"dev-link"})
	for dev_link in developer_website_a_all:

		#print "**** dev link" + dev_link.getText()

		if dev_link.getText() == " Visit Developer's Website ":
			developer_website = dev_link['href']
			#print " dev link" + dev_link.getText()

		if dev_link.getText() == " Email Developer ":
			developer_email = dev_link['href']
			#print " dev link" + dev_link.getText()


	c = csv.writer(open(fname, "a"))
	c.writerow([date,name,developer,apptype,badgeTitle,size,androidVersion,downloadCount,version,updateDate,reviewCount,avg_score,developer_email,developer_website,price])

	allinfofile = csv.writer(open(fname1, "a"))
	allinfofile.writerow([appid,date,name,developer,apptype,badgeTitle,size,androidVersion,downloadCount,version,updateDate,reviewCount,avg_score,developer_email,developer_website,price])


def mainJob():

	if not  os.path.isdir('data'):
			os.mkdir('data')	

	inoutFileName = "input.txt"
	lines = open(inoutFileName).read().splitlines()

	for line in lines:
		html,code = fetch_thing('https://play.google.com/store/apps/details?id=' + line)

		if html == None:
			#print '********ERROR******** WHILE FETCHING APP' + line
			f = open("data/report.csv",'a')
			f.write(date + ',' + line + ',' + 'Problem Occured\n')
			f.close()
			continue

		if code == 404:
			#print '********ERROR******** WHILE FETCHING APP' + line
			f = open("data/report.csv",'a')
			f.write(date + ',' + line + ',' + 'Deleted\n')
			f.close()
			continue

		if not  os.path.isdir('data/' + line):
			os.mkdir('data/' + line) 

		storeInCSV(html,line)	
		

		#print "app info for " + line		
		dateDirPath = 'data/' + line + '/' + date
		#print dateDirPath

		if not  os.path.isdir(dateDirPath):
			os.mkdir(dateDirPath)

		htmlarchiveRootPath = '../htmlarchive'

		if not  os.path.isdir(htmlarchiveRootPath):
			os.mkdir(htmlarchiveRootPath)

		if not  os.path.isdir(htmlarchiveRootPath + "/" + servername):
			os.mkdir(htmlarchiveRootPath + "/" + servername)	

		if not  os.path.isdir(htmlarchiveRootPath + "/" + servername + '/' +date):
			os.mkdir(htmlarchiveRootPath + "/" + servername + '/' + date)
				
		htmlarchievepath =  htmlarchiveRootPath + "/" + servername + '/'+ date

		htmlFilePath = htmlarchievepath + '/' + line + '.html'
		if not os.path.isfile(htmlFilePath):
			htmlFile = open(htmlFilePath, "w")
			htmlFile.write(html) 
			htmlFile.close()



mainJob()

rtt = time.time() - start_time
print "It took " + str(rtt) + " seconds to get data"

print 'App information Fetching Job ended on ' + str(datetime.datetime.now())


# try:
	
# 	logf = open(logfname,'a')
# 	logf.write('Job ended on ' + date + '\n')
# 	logf.close()
# except Exception, e:
# 	print str(e)
# 	logf = open(logfname,'a')
# 	logf.write('Job occured error on ' + date + '\n' + str(e) + '\n')
# 	logf.close()
