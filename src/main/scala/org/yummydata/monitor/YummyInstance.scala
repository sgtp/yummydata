package org.yummydata.monitor

import java.text.SimpleDateFormat
import java.util.Date
import java.io.File
import com.hp.hpl.jena.rdf.model.ModelFactory

object YummyInstance {
	val yummyPrefix="http://yummydata.org"
	val yummyValues=yummyPrefix+"/values#";
	val yummyLang=yummyPrefix+"/lang#";
	val yummyQueries=yummyPrefix+"/query#";
	
	var yummyEndpoint=""
	var yummyEndpointUpdate=""
	var yummyDirString=""
	
	val uDir=System.getProperty("user.dir")
	val confModel=ModelFactory.createDefaultModel()
	println("YummyInstance in creation. User.dir="+uDir)
	confModel.read("file://"+uDir+"/"+"config.ttl","Turtle");
	var nodeIter=confModel.listObjectsOfProperty(confModel.createProperty(yummyLang+"yummyEndpoint"));
	if(nodeIter.hasNext()) {
	  val nextElem=nodeIter.nextNode();
	  if(nextElem.isResource()) yummyEndpoint=nextElem.asResource().getURI().toString()
	  println("YummyEndpoint OK: "+yummyEndpoint)
	}
	nodeIter=confModel.listObjectsOfProperty(confModel.createProperty(yummyLang+"yummyEndpointUpdate"));
	if(nodeIter.hasNext()) {
	  val nextElem=nodeIter.nextNode();
	  if(nextElem.isResource()) yummyEndpointUpdate=nextElem.asResource().getURI().toString()
	  println("YummyEndpoint for Update OK: "+yummyEndpointUpdate)
	}
	nodeIter=confModel.listObjectsOfProperty(confModel.createProperty(yummyLang+"yummyDirString"));
	if(nodeIter.hasNext()) {
	  val nextElem=nodeIter.nextNode();
	  if(nextElem.isLiteral()) yummyDirString=nextElem.asLiteral().getValue().toString()
	  println("YummyDir results OK: "+yummyDirString)
	}
	
	
	//val yummyEndpoint="http://yummydata.org:3030/yummy/query"
	//val yummyEndpointUpdate="http://127.0.0.1:3030/yummy/data"
	//val yummyDirString=System.getProperty("user.home")+"/yummyResults";
	val yummyDir=new File(yummyDirString)
	if(!yummyDir.isDirectory()) yummyDir.mkdir()
	
	
	def getShortDate(): String = {
	  val dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	  val date=dateFormat.format(new Date());
	  return date;
	}
	
}