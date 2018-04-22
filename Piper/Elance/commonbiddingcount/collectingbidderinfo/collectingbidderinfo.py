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

globalBidderList = []

reload(sys)
sys.setdefaultencoding("utf-8")

def fetch_thing(url):
	try:
		f = urllib.urlopen(url)
		return (f.read(), f.code)
	except:
		return (None, 404)


print 'App information Fetcing Job stated on ' + str(datetime.datetime.now())

start_time = time.time()

today = datetime.date.today()
date = today.strftime("%m-%d-%Y")

biddercsvfilepath = "BidderList.csv"
biddercsvfile = csv.writer(open(biddercsvfilepath, "wb"))

bidderProjectFilePath = "BidderProjectList.csv"
bidderProjectFile = csv.writer(open(bidderProjectFilePath, "wb"))

global biddercount

def fetchBidderJobHistory(bidderurl,prohistoryURL):
	opener = urllib2.build_opener()
	print prohistoryURL
	opener.addheaders = [('User-agent', 'Mozilla/5.0')]
	response = opener.open(prohistoryURL)
	html = response.read()

	#print html
	
	soup = BeautifulSoup(html)

	project_list = soup.findAll("div",{"class":"jobname"})

	for project in project_list:
		projecturlhlink = project.find('a')
		if projecturlhlink != None:
			projecturl = projecturlhlink['href']
			bidderProjectFile.writerow([bidderurl,projecturl])
			print projecturl


def fetchBidderProjects(bidderurl):

	try:
		opener = urllib2.build_opener()
		opener.addheaders = [('User-agent', 'Mozilla/5.0')]
		response = opener.open(bidderurl)
		html = response.read()
		
		soup = BeautifulSoup(html)

		project_list = soup.findAll("div",{"class":"jobtitle"})
		prohistoryURL = project_list[0].find('a')['href']

		fetchBidderJobHistory(bidderurl,prohistoryURL)

	except Exception, e:
		print '404'

	# for project in project_list:
	# 	projecturl = project.find('a')['href']
	# 	bidderProjectFile.writerow([projecturl])
	# 	print projecturl
	

def fetchBiddersofProject(projecturl):

	opener = urllib2.build_opener()
	opener.addheaders = [('User-agent', 'Mozilla/5.0')]
	response = opener.open(projecturl)
	html = response.read()
	
	soup = BeautifulSoup(html)

	bidder_summary_divs = soup.findAll("div",{"class":"bidCard-summary"})

	for bidder_summary in bidder_summary_divs:
		
		rating_value = bidder_summary.find("span",{"class":"bold"}).text.strip()
		if float(rating_value) > 0:

			#print rating_value
			bidder_url_div = bidder_summary.find("a",{"class":"bidCard-title"})
			bidder_name = bidder_summary.find("a",{"class":"bidCard-title"}).text.strip()

			bidder_url = bidder_url_div['href']
			print bidder_url
			biddercsvfile.writerow([bidder_name,bidder_url])
			fetchBidderProjects(bidder_url)
			globalBidderList.append(bidder_url)
			pass

	pass

def fetchSingleProjectURLs(pagono):

	opener = urllib2.build_opener()
	opener.addheaders = [('User-agent', 'Mozilla/5.0')]
	response = opener.open('https://www.elance.com/r/jobs/cat-it-programming/p-' + str(pagono))
	html = response.read()
	
	soup = BeautifulSoup(html)


	projectcsvfilepath = "initialProjectList.csv"
	projectcsvfile = csv.writer(open(projectcsvfilepath, "wb"))

	project_list = soup.findAll("a",{"class":"title"})

	for project in project_list:
		projecturl = project['href']
		projectcsvfile.writerow([projecturl])
		fetchBiddersofProject(projecturl)

		if len(globalBidderList) > 1000:
			return

		#print projecturl
		pass
	pass
def fetchInitialProjects():
	for i in xrange(1,21):
		fetchSingleProjectURLs(i)
		
#fetchInitialProjects()

def fetchBidderList():
	projectList = open("BidderProjectListFinal.csv").read().splitlines()

	#print projectList

	print 'Here I am '

	projectbidderfile = csv.writer(open('projectbidders.csv', "wb"))


	for projecturl in projectList:
		print 'Hello'
		opener = urllib2.build_opener()
		opener.addheaders = [('User-agent', 'Mozilla/5.0')]
		response = opener.open(projecturl)
		html = response.read()
	
		soup = BeautifulSoup(html)

		bidderdivs = soup.findAll("div",{"class":"bidCard"})

		print str(len(bidderdivs))



		for bidderdiv in bidderdivs:
			a = bidderdiv.find("a",{"class":"bidCard-title"})
			bidderurl = a['href']
			print bidderurl

			winnertag = bidderdiv.find("img",{"alt":"Winner"})

			status = ''


			if winnertag == None:
				status =  "Bidder"
			else:
				status =  "Winner"	

			info = bidderdiv.find("div",{"class":"bidCard-bidInfo"}).text.strip()
			print info	

			projectbidderfile.writerow([projecturl,bidderurl,status,info])

			pass


def fetchBidderList():
	projectList = open("winnersurl.csv").read().splitlines()

	#print projectList

	print 'Here I am '

	projectbidderfile = csv.writer(open('projectbidders.csv', "wb"))


	for projecturl in projectList:
		print 'Hello'
		opener = urllib2.build_opener()
		opener.addheaders = [('User-agent', 'Mozilla/5.0')]
		response = opener.open(projecturl)
		html = response.read()
	
		soup = BeautifulSoup(html)

		bidderdivs = soup.findAll("div",{"class":"bidCard"})

		print str(len(bidderdivs))





#fetchBidderList()


fraudbidderurlfile = "533fraudbidders.csv"
fraudbidderurl = open(fraudbidderurlfile).read().splitlines()

for url in fraudbidderurl:
	print "\n\nfetching info of " + url
	fetchBidderProjects(url)


# fraudprojecturlfile = "allfraudjobsfromseedsNoFilter.csv"
# fraudprojecturl = open(fraudprojecturlfile).read().splitlines()

# for url in fraudprojecturl:
# 	fetchBiddersofProject(url) 


# rtt = time.time() - start_time
# print "It took " + str(rtt) + " seconds to get data"

# print 'App information Fetching Job ended on ' + str(datetime.datetime.now())


# try:
	
# 	logf = open(logfname,'a')
# 	logf.write('Job ended on ' + date + '\n')
# 	logf.close()
# except Exception, e:
# 	print str(e)
# 	logf = open(logfname,'a')
# 	logf.write('Job occured error on ' + date + '\n' + str(e) + '\n')
# 	logf.close()
