package org.yummydata.monitor

import org.yummydata.monitoraux.QEWrapper
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.File
import java.io.FileOutputStream
import sys.process.Process


object EndPointsCrawler extends App {
	val totalResults=ModelFactory.createDefaultModel()
	val ckanEndpoint="http://semantic.ckan.net/sparql"
	val mondecaEndpoint="http://labs.mondeca.com/endpoint/ends"
	val datasetQuery="SELECT distinct  ?dataset where {" +
			"?dataset <http://www.w3.org/ns/dcat#keyword> ?val ." +
			"values ?val {\"health\" \"biomedicine\" \"biology\" \"lifesciences\"}}"
			
	val eqe=new QEWrapper(datasetQuery,ckanEndpoint)
	eqe.execute()
	val potentialDatasets=eqe.extractListOfResources()
	println("Total potential datasets "+potentialDatasets.length);
	val potentialDatasetsScreened=potentialDatasets.filter(x => x.startsWith("http://datahub.io"))
	println("After screening "+potentialDatasetsScreened.length);
	val potentialDatasesToTest=potentialDatasetsScreened.map(x => x.replace("datahub.io","thedatahub.org"))
	val potentialEndpoint=potentialDatasesToTest.map(x=> {
	  val query="construct {?ee a <"+YummyInstance.yummyLang+"endPoint>} where { <"+x+"> <http://rdfs.org/ns/void#sparqlEndpoint> ?e . BIND(URI(?e) AS ?ee)}"
	  println(query)
	  val queryEngine=new QueryEngineHTTP(mondecaEndpoint,query);
	  val resultModel=queryEngine.execConstruct();
	  totalResults.add(resultModel)
	  //println("Size: "+resultModel.size())
	 
	})
	
	val resultFile=new File(YummyInstance.yummyDir,YummyInstance.getShortDate+"-endpoints.ttl")
	val resultGraph="http://yummydata.org/endpoints"
  
	val fo=new FileOutputStream(resultFile);
  
	totalResults.write(fo,"Turtle");
	fo.flush()
  
	val proc=Process("s-put "+YummyInstance.yummyEndpointUpdate+" "+resultGraph+" "+resultFile)
	print ("Attempting to execute: "+proc+" ... ")
	val res=(proc !)
	if(res==0) println("OK");
	else println("KO")
	
}