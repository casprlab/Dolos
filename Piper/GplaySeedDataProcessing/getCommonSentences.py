import csv

reviewersinfofile1 = "allreviews/allrevieweinformation1.csv"
reviewersinfolines1 = open(reviewersinfofile1).read().splitlines()

reviewersinfofile2 = "allreviews/allrevieweinformation2.csv"
reviewersinfolines2 = open(reviewersinfofile2).read().splitlines()

reviewersinfofile3 = "allreviews/allrevieweinformation3.csv"
reviewersinfolines3 = open(reviewersinfofile3).read().splitlines()

textcountmaping = {}

def fingcount(reviewersinfofile,sentence):
	count = 0
	for reviewerinfoline in reviewersinfofile:
		#print reviewerinfoline
		splits = reviewerinfoline.split(",")
		reviewtext = splits[4].strip()
		if(reviewtext == sentence):
			count = count + 1

	return count


def processfile(reviewersinfofile):
	for reviewerinfoline in reviewersinfofile:
		splits = reviewerinfoline.split(",")
		reviewtext = splits[4].strip()

		if(reviewtext not in textcountmaping.keys()):
			count = 0

			count = count + fingcount(reviewersinfolines1,reviewtext)
			count = count + fingcount(reviewersinfolines2,reviewtext)
			count = count + fingcount(reviewersinfolines3,reviewtext)

			if(count > 5):
				textcountmaping[reviewtext] = count
				print reviewtext + "," + str(count)

processfile(reviewersinfolines1)
#processfile(reviewersinfolines2)
#processfile(reviewersinfolines3)

#for text in textcountmaping.keys():
#	print text + "," + str(textcountmaping[reviewtext])


