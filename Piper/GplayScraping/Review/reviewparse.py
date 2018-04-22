import urllib
import json
import sys
import time
import datetime
from bs4 import BeautifulSoup

reload(sys)
sys.setdefaultencoding("utf-8")

today = datetime.date.today()
todayDateStr = today.strftime("%m-%d-%Y")



def fetch_thing(url, params, method):
	try:
  		params = urllib.urlencode(params)
  		if method=='POST':
  			f = urllib.urlopen(url, params)
  		else:
  			f = urllib.urlopen(url+'?'+params)
  		return (f.read().decode('utf-8'), f.code)
	except: 
  		return (None, 404)
    


def fetchPage(index,appid,lastDateInCSV, allreviews,lastreviewcount):
	
	url = "https://play.google.com/store/getreviews";

	content, response_code = fetch_thing(
                              url, 
                              {'reviewType': 0, 'pageNum': index, 'id': appid,'reviewSortOrder':0,'xhr':1}, 
                              'POST'
                         )

	#print "response Code for " + appid + " is %d\n" % (response_code)

	


	if response_code == 404:
		#print '********ERROR******** WHILE FETCHING APP REVIEW FOR PAGE %d Fro APP %s' %(index,appid)
		f = open("data/report.csv",'a')
		f.write(todayDateStr + ',' + appid + ',' + 'APP REVIEW NOT FOUND FOR PAGE\n')
		f.close()
		return True;

	#print "\n\n\n\n" + content + "\n\n\n\n"	

	content = content.replace(")]}'", "")
	content = content.replace("[[", "[")
	content = content.replace("\n", "")
	content = content.replace("]]", "]")

	jsonContent = None


	try:
  		jsonContent = json.loads(content)
	except: 
  		return False


	if len(jsonContent) < 3 :
		return False


	finalHTML = '<html><head><title></title></head><body>' + jsonContent[2] + '</body></html>'

	soup = BeautifulSoup(finalHTML)

	#print soup.prettify()

	divElements = soup.find_all('div')

	lastDate = None

	for divElement in soup.find_all('div',{'class':'single-review'}):
		#print divElement

		authorHrefValue = ''

		authorspan =  divElement.find("span", {'class' : 'author-name'})
		authorHref = authorspan.find('a',{'class' : 'id-no-nav g-hovercard'});

		if authorspan.a:
			authorHrefValue = authorspan.a['href']
			#print authorspan.a['href']

	
		username = ''

		if authorHref is None:
			username = authorspan.getText()
		else:
			username = authorHref.getText()
		#print username

		date = divElement.find("span", {'class' :'review-date'}).getText()
		lastDate = date
		#print date

		title = divElement.find("span", {'class' :'review-title'}).getText()
		#print title

		reviewBody = divElement.find("div", {'class' :'review-body'}).getText().replace(title,"").replace("Full Review","")
		#print reviewBody

					
		ratingValue = divElement.find("div", {'class' :'current-rating'})['style'].split(':')[1].replace(";", "")
		#print ratingValue

		entry = username.lstrip().rstrip() + ';' + authorHrefValue.lstrip().rstrip()  + ';' + date.lstrip().rstrip() + ';' + title.lstrip().rstrip() + ';' + reviewBody.lstrip().rstrip() + ';' + ratingValue.lstrip().rstrip()

		#print entry

		allreviews.append(entry)

	#print '\n****finished fetching page %d for app %s\n' % (index, appid)

	if jsonContent[1] == 2 :
		return False	

	
	if lastreviewcount >= 200:
		print "we have more than 200 reviews"
		if lastDate == None:
			return True

		lastDateInArray = datetime.datetime.strptime(lastDate, '%B %d, %Y').date()
		
		if lastDateInCSV == None:
			return True
		else:
			if (lastDateInArray-lastDateInCSV).days <= 0:
				return False
		

	return True		


def fetchAllReviews(appid, date, lastreviewcount):
	#print date

	haveReview = True	
	allreviews = []
	for x in range(0, 250):

		#print "\n****** started fetching page %d for app %s \n" % (x, appid)

		haveReview = fetchPage(x,appid,date,allreviews,lastreviewcount)

		time.sleep(5)

		if not haveReview:
			#print '\n\n\n****finished fetching all reviews for %s***\n\n\n' % (appid)
			break

			
	return allreviews	



