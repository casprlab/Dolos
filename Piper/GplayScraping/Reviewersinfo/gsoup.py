#/usr/bin/python
from bs4 import BeautifulSoup
import traceback
import urllib2
import sys,os,time
from datetime import datetime
import csv
import os


def readurls_reviewers(reviewer_filename):
	directory_name = "Reviewer_Content_%s"%datetime.now().strftime('%m-%d-%y')
	if not os.path.exists(directory_name):
		os.makedirs(directory_name)
	profiles = open(reviewer_filename).readlines()
	
	download_reviewers(profiles,directory_name)	

def download_reviewers(profiles,directory):
	curl_cmd = """curl '$PROFILE_URL' -H 'origin: https://play.google.com' -H 'accept-encoding: gzip,deflate,sdch' -H 'accept-language: en-US,en;q=0.8' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36' -H 'content-type: application/x-www-form-urlencoded;charset=UTF-8' -H 'accept: */*' -H 'referer: https://play.google.com/store/apps/details?id=com.dashlabs.dash.android' -H 'cookie: PREF=ID=220b328c96540eb4:U=48379d6fa5a0508b:FF=0:LD=en:TM=1406333325:LM=1406350757:GM=1:S=E-bA9nC4ukSH_E2V; PLAY_PREFS=CgJVUxDfj8eb9yg:S:ANO1ljK2v4hnUprq; S=payments=u4e0RqgGZQATVvAbbgXKRg:static_files=pZj4dWuPB4Q; NID=67=OmWstMi_r7YlIbRVdasnxdgFZfAKZ6TxQHMGu6rnu9vqICEqCkZc6wYwCqdm-mKdESzjic_-N0rPf4BpdcZ0VufADiDO5AjG7QZo4Qvkr7XrS5gVqQfmJRzZZylhP-_kkvchhWyeCBJCZpS_ETxurdmmvL4qr1u9YAItJIUBGkZBqeMQe_X9lI0R5zU8n89Fa6PPtHo1ifqooHIObR3XmSxvXH2rasnpqNP2ldvxum5aLvQDNlfupQ; PLAY_ACTIVE_ACCOUNT=ICrt_XL61NBE_S0rhk8RpG0k65e0XwQVdDlvB6kxiQ8=srinivasareddy.manda@gmail.com; SID=DQAAAN0AAAAfJn6CM3gHuKgTRqci9ffviCLxQiZM6Qo45AGkziowiWc2A-aeVuh1Znh6lOe9_uGefU7ERUBVscnxp_-5nBw4vIgHpAsuG915Juy8_U1uSmN87OWGmw-j4SgdR24OmmJ5oeLs6QtHkvpcwpQREN60zb8bNIkhUVTOlNZBFqX9ebfedFbRuZrqzXhFJOgzrnivgEoJ0KU5MsoIYVF4Q9VXAsrfT_1GxMBq3WTLwlib2sdv96ax-FPfW0f5HfSANAjRVyCUmSe2WhImMvZVAjFMXS5P-FL_VpUP216p24xb0Q; HSID=Asb_faamJ5acfiFFt; SSID=AZlCnQ0vNpDhTPbZf; APISID=LjvcrofG49BubLCL/AKbU6kGBzUiFVgDEd; SAPISID=Ie4muzQISUJKi1G0/AG9lixZ63quQjaIRK; _ga=GA1.3.12154081.1406387544' -H 'x-client-data: CLa1yQEIj7bJAQijtskBCKm2yQEIwbbJAQi4iMoBCOyIygE=' --data 'ipf=1&xhr=1&token=a-vdoIkpjqzdi1KfiwAqL_z2OPw%3A1406391642295' --compressed > $FILE_NAME.html"""
	
	curl_cmd2 = "curl '$PROFILE_URL' -H 'accept-encoding: gzip,deflate,sdch' -H 'accept-language: en-US,en;q=0.8' -H 'user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36' -H 'accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8' -H 'cache-control: max-age=0' -H 'cookie: PREF=ID=130b54652e0317f2:U=8b42689944176780:FF=4:LD=en:TM=1390438656:LM=1401145673:GM=1:S=j1ludfL_5J6sHBJX; HSID=Ac9D8f70jG7i32cuR; SSID=ApY1Mx1Fy5aELT0pz; APISID=8I8uv11cCTvgCEKk/Ao3_67VjydLQWkI-p; SAPISID=jpL036KzOoReelCC/AtvpM64M7tGzP7yk6; SID=DQAAADACAAB9l_Ywoe0IPFhyKLSiVL67U0Jv_Guv8G_ZzQNCaMexKOQBXdwZuOC1aIXEbOO3KYCghtvzc31DlwZBr_P4tWAWa06dhnhqLTFg8HOM79gmRW0zFa9zlElaPSNICIk4520tgBRzT99kcyqvQZHoQikGHJ281yY_9yUdat42wAbFhNgacI3taFULGgc3J0BV-dwt736e4-sn-U8dY57yO3-tnF4Hm0IpBw2zEF1Vac_SXqGycscmF3qE_3GH67m4MXKKGCMCcVKBjy0j27ubvQ8VNU4INpc4Yo5rdhO2NZA1fyGTCsI4cHKrgjCiCt448mNmn3LeRIxqUdlN4Ic7eAKihUCAbyoh6FcRrKbCGj5Tj9enzQUagzyH223Kszm-EY0uEaDwY1oOQhxxJCo3Vf8LFsLKYuCgRDwS0pbOPZZgIaUFoJAykxkbDYIuJMMnlljMBB2HxOHA5ZKXUtubzosjz6OPBfmKMLyCyYSawGRA6khylbkIeTDck8jSr9dwnUP4WsFMt_o-uaZf-HK_c7Y04Hk8Hw3PU7Z0Wn-NSmCHnMO3m3kAi-9ejhJQETaChzKpt4g9hOUs8vC0PWE8A-c7NM2Bu24DuR2MmiThAVilVPWwBAEeNLGAsjXQMBimY8s-gYVdu69OO8TeaeVVL5Qbte5fAjlDNQVkrKQojBxMfmBObofO8Jif6hg9wGtAGtLpfYX4XvTfBsgj9mXSsEzLWQTFO5x20B5iwxPxG6xrCw; NID=67=rKAnC29-idDyk6caz5yg3Vx-7gond9Fc50GrBQ3Teg7JTRqa0UKQtapm1qtcT1EZHHgJcmYAlfhTT3BP1djq6uHIXOG6Riu7CwMj65xJ0B-XifXlRfd2rAp2t07OEXBs81ytLfYpVbnNf2PRtDX0BuYXjl_odmHLsTNbDhGmdoo6xPyuyUS6ObqUar1aCdmZxe261nyKDbQX53_OTO5rhpMqPEc3AF0VTTMh6KYOr5vl2146NUxjL3Gfouoyq626sSUli6rIJIdOqGX3rZQ7Px1IQftD9ba1IXwPt_v5_FubfR56eJICfSDI-N0m; PLAY_ACTIVE_ACCOUNT=ICrt_XL61NBE_S0rhk8RpG0k65e0XwQVdDlvB6kxiQ8=mazy.buetster@gmail.com; PLAY_PREFS=CgJVUxCMlYee_Sg:S:ANO1ljJunqqLeILg; S=grandcentral=dd6i9xcW-rL_jmbpZmUPuA:photos_html=5V2H55Su0IJ7KYgomPxLbA:billing-ui-v3=47ogUFYZ0JYGGfVcqY_xoA:billing-ui-v3-efe=47ogUFYZ0JYGGfVcqY_xoA; _ga=GA1.3.325281556.1401427754' -H 'x-client-data: CLu1yQEIh7bJAQijtskBCKm2yQEIxLbJAQi4iMoBCO2IygEI9pPKAQ==' --compressed > $FILE_NAME"


	filename = "allreviewersinfo.csv"

	if os.path.isfile(filename) == False:
		writer = csv.writer(open(filename, "wb"))
	else:
		writer = csv.writer(open(filename, "a"))
		

	for profile in profiles:

		url = "https://play.google.com" + profile

		print url

		try:
			file_name = "reviewersinfo.html"
			print "Downloading Reviewer HTML" + url + "to" + file_name
			os.system(curl_cmd2.replace("$PROFILE_URL", url.strip()).replace("$FILE_NAME", file_name.strip()))

			f=urllib2.urlopen("file:reviewersinfo.html")

			html = f.read()




			if "We're sorry, the requested URL was not found on this server." in html: 
				writer.writerow(["deleted",profile,"deleted","deleted","deleted","deleted","deleted","deleted"])
				continue


			soup = BeautifulSoup(html)
		

			Reviewername = soup.find('div',attrs={'class' : 'person-name'}).text.strip('').encode('utf8')	
			Gplusurl = unicode(soup.find('a',attrs={'class':'g-plus-link'}).get('href')).encode('utf8')	
			list = soup.findAll('div',attrs={'class':'card-list'})
			for app_card in list:
				for detail in app_card.findAll('div',attrs={'class':'card-content'}):
				
					Reviewer_Name = Reviewername
					G_Plus_URL = Gplusurl
					App_Name = unicode(detail.findAll('a',attrs={'class':'title'})[0].text.strip("")).encode('utf8')	
					App_Url = unicode(detail.findAll('a',attrs={'class':'title'})[0].get('href')).strip("").split('?')[1].encode('utf8')

					detailfields = detail.findAll('div',attrs={'class':'tiny-star'})

					App_Rating = ''

					if len(detailfields) > 0:
						App_Rating = unicode(detailfields[0].get('aria-label').strip().split(" ")[1]).encode('utf8')
						
					priceFields = detail.findAll('button',attrs={'class':'price'})

					App_Price = ''								
					

					if len(priceFields) > 0:

						App_Price = unicode(priceFields[0].find('span').text).encode('utf8')								
					

					Given_Rating = unicode(detail.findAll('a',attrs={'class':'reason-body'})[0].text.strip('')).encode('utf8')
									
					writer.writerow([Reviewername,profile,G_Plus_URL,App_Name,App_Url,App_Rating,App_Price,Given_Rating])




		except:
			print "Skipping Download of" + url + "due to URL open error."
			continue

def generate_csv_datafile(reviewer_directory):
	
		files = os.listdir(reviewer_directory)

		for reviewer_file in files:
			with open(reviewer_directory + "/" + reviewer_file) as f:
				csv_headers = ['Reviewer_Name','G_Plus_URL','App_Name','App_Url','App_Rating','App_type','Given_Rating']
				directory_name = "Reviewer_CSV_Content_%s"%datetime.now().strftime('%m-%d-%y')
        			if not os.path.exists(directory_name):
                			os.makedirs(directory_name)
				
				csv_filename = reviewer_file.split('.')[0] + ".csv"

				with open(directory_name + "/" + csv_filename,'wb') as csv_f:
					writer = csv.writer(csv_f, delimiter=',',quotechar='"',quoting = csv.QUOTE_ALL)
					writer.writerow(csv_headers)
					print "Writing" + str(len(files)) + "reviewer data to " + csv_filename

					try:
						soup = BeautifulSoup(f.read())
					

						Reviewername = soup.find('div',attrs={'class' : 'person-name'}).text.strip('').encode('utf8')	
						Gplusurl = unicode(soup.find('a',attrs={'class':'g-plus-link'}).get('href')).encode('utf8')	
						list = soup.findAll('div',attrs={'class':'card-list'})
						for app_card in list:
							for detail in app_card.findAll('div',attrs={'class':'card-content'}):
							
								Reviewer_Name = Reviewername
								G_Plus_URL = Gplusurl
								App_Name = unicode(detail.findAll('a',attrs={'class':'title'})[0].text.strip("")).encode('utf8')	
								App_Url = unicode(detail.findAll('a',attrs={'class':'title'})[0].get('href')).strip("").split('?')[1].encode('utf8')
								App_Rating = unicode(detail.findAll('div',attrs={'class':'tiny-star'})[0].get('aria-label').strip().split(" ")[1]).encode('utf8')
								App_Price = unicode(detail.find('button',attrs={'class':'price'}).find('span').text).encode('utf8')								
								Given_Rating = unicode(detail.findAll('a',attrs={'class':'reason-body'})[0].text.strip('')).encode('utf8')
												
								writer.writerow([Reviewername,G_Plus_URL,App_Name,App_Url,App_Rating,App_Price,Given_Rating])

					except Exception,excp:
						print 'Error Parsing details from reviewer file ' + reviewer_file + 'Reason:' + str(excp)
						print traceback.print_exc()
	
		print "Done"

def print_usage():
			print "Usage: " + sys.argv[0] + "<python gsoup.py gen_reviewerset reviewer_filename.txt |python gsoup.py gen_reviewercsv reviewer_directory >"

						
# if __name__=="__main__":
# 	if len(sys.argv) < 3:
# 		print_usage()
# 	if sys.argv[1] == 'gen_reviewerset':
# 		readurls_reviewers(sys.argv[2])
# 	elif sys.argv[1] == 'gen_reviewercsv':
# 		generate_csv_datafile(sys.argv[2])
# 	else:
# 		print_usage()


readurls_reviewers("reviewersurl.txt")

		












