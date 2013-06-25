#!/usr/bin/python
import urllib2
import sys
import time
import sys

queryString="?query=select+%3Fs+%3Fp+%3Fo%0D%0Awhere+%7B%3Fs+%3Fp+%3Fo%7D%0D%0Alimit+10"
endpointURI=sys.argv[1]
endpointURL=sys.argv[2]
#print "testEndpointUrl on "+endpointURL
#print "testEndpointUri on "+endpointURI
start = time.time()
try:	
	connection = urllib2.urlopen(endpointURL+queryString)
	answer=connection.getcode()
	connection.close()
except urllib2.HTTPError, e:
	answer=e.getcode()
except urllib2.URLError, e2:
	print "<"+endpointURI+"> <http://yummydata.org/measures/endpointstate> <http://yummydata.org/values/NOSERVER>"
	sys.exit();
duration=time.time()-start
if(answer==200):	
	print "<"+endpointURI+"> <http://yummydata.org/measures/endpointstate> <http://yummydata.org/values/endpointup>"
elif(answer==503):	
	print "<"+endpointURI+"> <http://yummydata.org/measures/endpointstate> <http://yummydata.org/values/tempoff>"
elif(answer==404):	
	print "<"+endpointURI+"> <http://yummydata.org/measures/endpointstate> <http://yummydata.org/values/NOTFOUND>"
elif(answer==400):	
	print "<"+endpointURI+"> <http://yummydata.org/measures/endpointstate> <http://yummydata.org/values/BADREQUEST>"
else:
	print "<"+endpointURI+"> <http://yummydata.org/measures/endpointResponseCode> "+str(answer)
print "<"+endpointURI+"> <http://yummydata.org/measures/endpointResponseTime> "+str(duration)