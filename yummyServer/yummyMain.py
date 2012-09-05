#!/usr/bin/python
import os
from rdflib.graph import Graph
from rdflib import RDF
from rdflib import RDFS
from rdflib import URIRef
from time import gmtime, strftime

dataDir="/Users/andreasplendiani/temp/yummy"
endpointClass=URIRef("http://yummydata.org/meta#endpoint");
scriptClass=URIRef("http://yummydata.org/meta#script");
hasCommandProperty=URIRef("http://yummydata.org/meta#hasCommand");
scriptsLabels={}
scriptsCommands={}

timeString=strftime("%Y-%m-%d", gmtime())
print "Starting yummyRobot : "+timeString
d = os.path.join(dataDir,timeString)
if not os.path.exists(d):
	os.makedirs(d)

scriptsDir=os.path.join(os.getcwd(),"yummyScripts");
configData=Graph()
for confFile in os.listdir(scriptsDir): 
	if confFile.endswith('.ttl'):
		configData.parse(os.path.join(scriptsDir,confFile),format="turtle");
print "checking Modules : "
triples=configData.triples((None,RDF.type,scriptClass))
for (s,p,o) in triples:
	commands=configData.objects(s,hasCommandProperty)
	for command in commands:
		scriptsCommands[s]=command
	labels=configData.objects(s,RDFS.label)
	for label in labels:
		scriptsLabels[s]=label
	print "Found "+s+" ("+scriptsLabels[s]+") @ "+scriptsCommands[s]
		

tStoresDesc=Graph()
tStoresDesc.parse("../Resources/EndPointList.ttl",format="turtle");

triples=tStoresDesc.triples((None,RDF.type,endpointClass))

for (s,p,o) in triples:
		currentEndpoint=s
		print "Testing endpoint "+currentEndpoint
		for k,v in scriptsCommands.iteritems():
			print("executing: "+scriptsLabels[k]+" ... ")
			
			print "OK"