package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.Statement
import com.hp.hpl.jena.rdf.model.ModelFactory

import java.io.FileOutputStream
import java.io.File

import sys.process.Process

object Monitor extends App{
  val endpoints=EndPointsTeller.getEndpointsList();
  val totalResults=ModelFactory.createDefaultModel();
  
  for (e <- endpoints) {
	  println("***************************");
	  println("Testing endpoint: "+e);
	  println("***************************");
	  //Void provided ?
	  val voidAnalyzer=new VoidAnalyzer(e);
	  totalResults.add(voidAnalyzer.resultModel);
	  
	  val simpleQueryURI=YummyInstance.yummyQueries+"defaultPing";
	  val simpleQuery=new Query(simpleQueryURI,e);
	  simpleQuery.execute();
	  println("Response code: "+simpleQuery.responseCode);
	  if(!simpleQuery.resultStatus) {
	    println("Something went wrong, server may be down")
	  }
	  else {
		println("Server is UP");
		println("Response time: "+simpleQuery.responseTime);
		totalResults.add(simpleQuery.resultModel);
		val queries=QueriesTeller.queriesForEndpoint(e);
		for(q<-queries) {
		 val query=new Query(q,e); 
		 println("Making query: "+query.queryLabel);
		 query.execute();
		 println("Response code:"+query.responseCode);
		 totalResults.add(query.resultModel);	    
		}
		
		
		
	  }
	
  }
  val resultFile=new File(YummyInstance.yummyDir,YummyInstance.getShortDate+"-result.ttl")
  val resultGraph="http://yummydata.org/data/"+YummyInstance.getShortDate
  
  val fo=new FileOutputStream(resultFile);
  
  totalResults.write(fo,"Turtle");
  fo.flush()
  
  val proc=Process("s-put "+YummyInstance.yummyEndpointUpdate+" "+resultGraph+" "+resultFile)
  print ("Attempting to execute: "+proc+" ... ")
  val res=(proc !)
  if(res==0) println("OK");
  else println("KO")
  
  
	
}