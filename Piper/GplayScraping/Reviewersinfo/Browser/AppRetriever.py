'''
Created on Apr 22, 2015

@author: vallagenah
'''
import time
import urllib2
import sys
import signal
import socket
import errno
import webbrowser
import os
import csv


if __name__ == '__main__':
    try:       
        lines = [line for line in open('mizan.csv')]
        count=0
        killcount=0
        with open('2nd800reviewers.csv',"ab") as fout:
            writer=csv.writer(fout)
            for text in lines:
                D={}
                
                url=text.strip()
                print url
                timeout=0
                
                killcount=killcount+1
                webbrowser.open_new_tab("https://play.google.com"+url)
                time.sleep(22)
                #os.system("taskkill /F /IM firefox.exe")
                file_path="C:\\Users\\vallagenah\\AppData\\Local\\Temp\\google_play.tmp"
                while True and timeout<4:
                    if os.path.exists(file_path)==False:
                        time.sleep(5)
                        timeout=timeout+1
                    elif os.path.exists(file_path)==True:
                        break
                    
                if os.path.exists(file_path)==True:
                    text_file = None
                    try:
                        text_file = open(file_path, "r")
                        content = text_file.read()
                    except Exception, e:
                        raise
                    
                    
                    if content.find("main-title")!=-1:
                        content=content[content.find("main-title")+12:]
                        name=content[:content.find("Google Play")-3]
                    while True:                
                        if content.find("data-docid")==-1:
                            break;
                        while(content.find("data-docid")!=-1):                  
                            content=content[content.find("data-docid"):]
                            extractC=content[12:150]
                            #print extractC
                            if extractC.find(">  </span>")!=-1:
                                count=count+1
                                appid=content[12:content.find(">  </span>")-1]     
                                #print appid                                         
                                
                                content=content[content.find("title=")+7:]
                                title=content[:content.find("\">")]
                                #print title
                                content=content[content.find("stars out of five stars")-4:]
                                apprating=content[:3]
                                #print apprating
                                content=content[content.find("display-price")+15:]
                                price=content[:content.find("</span>")]
                                #print price
                                ratedvalue=""
                                if content.find("Rated it")!=-1:
                                    #print "entered star"
                                    ratedvalue=content[content.find("Rated it"):content.find("Rated it")+16]
                                elif content.find("+1'd this")!=-1:
                                    ratedvalue="+1'd this"
                                elif content.find("Plays this")!=-1:
                                    ratedvalue="Plays this" 
                                #print ratedvalue
                                flag=True
                                if len(D)==0:
                                    D[appid]=ratedvalue
                                    
                                else:
                                    if appid in D and D.get(appid)==ratedvalue:
                                        flag=False
                                    else:
                                        D[appid]=ratedvalue
                                        flag=True 
                                
                                    '''if flag==True:
                                    temp=sqlBuilder(count,name,text.strip(),title,appid,apprating,price,ratedvalue)
                                    db.execute("INSERT INTO fake(id,name,url,appname,appid,apprating,price,ratedvalue) VALUES("+temp+");")
                                    dbLink.commit()'''
                                if flag==True:
                                    row=(count,name,url,title,appid,apprating,price,ratedvalue)
                                    writer.writerow(row)
                            content=content[400:]
                                                        
                    text_file.close()
                    os.remove("C:\\Users\\vallagenah\\AppData\\Local\\Temp\\google_play.tmp")
                    
                if killcount==20:
                    killcount=0
                    os.system("taskkill /F /IM firefox.exe")
                    time.sleep(30)

                    
                    
    except Exception, e:
        try:
            print("MySQL Error [%d]: %s" % (e.args[0], e.args[1]))
        except IndexError:
            print("MySQL Error:", str(e))