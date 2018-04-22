#/usr/bin/python
from bs4 import BeautifulSoup
import traceback
import urllib, urllib2
import sys,os,time
import datetime
import csv
import os
import sys
import json


reload(sys)
sys.setdefaultencoding("utf-8")

biddercsvfilepath = "BidderList.csv"
biddercsvfile = csv.writer(open(biddercsvfilepath, "wb"))

def fetch_thing(url, params, method):
	data = urllib.urlencode(params)
	req = urllib2.Request(url, data)
	#req.add_header("Content-Type", "application/json")
	req.add_header("User-agent", "Mozilla/5.0")
	f = urllib2.urlopen(req)
	return (f.read().decode('utf-8'), f.code)

def fetchBiddersofProject(projecturl,pageno):

	urlparts = projecturl.split('/')

	projectid = urlparts[len(urlparts) - 2]
	print projectid


	url = 'https://www.elance.com/php/bid/main/proposalSubmitAHR.php?t=1434832312776'

	content, response_code = fetch_thing(
                          url, 
                          {'action': 'proposalResults', 'jobid': projectid, 'page': pageno}, 
                          'POST'
                     )


	#print content

	jsoncontent = json.loads(content)
	#print jsoncontent['data']['html']

	finalHTML = '<html><head><title></title></head><body>' + jsoncontent['data']['html'] + '</body></html>'

	soup = BeautifulSoup(finalHTML)
	bidderdivs = soup.findAll("div",{"class":"bidCard"})

	print str(len(bidderdivs))

	biddercount = len(bidderdivs)

	if len(bidderdivs) == 0:
		return 0

	for bidderdiv in bidderdivs:
		a = bidderdiv.find("a",{"class":"bidCard-title"})
		bidderurl = a['href']
		biddername = a.text
		print biddername

		print bidderurl

		winnertag = bidderdiv.find("img",{"alt":"Winner"})

		status = ''


		if winnertag == None:
			status =  "Bidder"
		else:
			status =  "Winner"
			#print 'winner'	

		info = bidderdiv.find("div",{"class":"bidCard-ft"}).text.strip().replace("\n\n","|").replace("\n","").replace("\t","")
		print info

		bidderstatus = 	bidderdiv.find("div",{"class":"bidCard-bidStatus"}).text.strip()
		print bidderstatus

		newbiddertagimg = bidderdiv.find("img",{"src":"/media/images/4.0/new_provider.gif"})

		newbiddertag = ''


		if newbiddertagimg == None:
			newbiddertag =  ""
		else:
			newbiddertag =  "New Freelancer"

		biddercsvfile.writerow([projecturl,bidderurl,biddername,status,bidderstatus,newbiddertag,info])

		pass

	return biddercount

	# pass

profiles = open("AllFakeReviewProjects.csv").readlines()	

for profile in profiles:
	pagecount = 1
	while True:
		count = fetchBiddersofProject(profile,pagecount)
		pagecount = pagecount + 1 
		if count == 0:
			break
		pass	
