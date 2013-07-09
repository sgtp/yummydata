package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.Statement
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.Model

import java.io.FileOutputStream
import java.io.File

import scala.actors.Futures._
import scala.actors.Future

import sys.process.Process



object Monitor extends App{
  //System.setProperty("actors.corePoolSize", "30")
  //System.setProperty("actors.minPoolSize", "10")
  System.setProperty("actors.enableForkJoin","false")
  val endpoints=EndPointsTeller.getEndpointsList();
  val totalResults=ModelFactory.createDefaultModel();
  
  var active=0;
  var tasks= List[Future[Model]]()
 
  for (e <- endpoints) {
    println("***************************");
    println("Testing endpoint: "+e);
	println("***************************");
	 
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
		
		//Void provided ?
		
		tasks ::= future {
			active+=1
		    println("checking void for "+e+" Thread count: "+active);
			val voidAnalyzer=new VoidAnalyzer(e);
			active-=1
			println("voide checked ("+e+") Thread count: "+active);
			voidAnalyzer.resultModel
			
		}
		
	  
		val queries=QueriesTeller.queriesForEndpoint(e);
		///
		for (q <- queries) {
			tasks ::= future {
				active+=1
				println("Thread count: "+active);
				val result=new Query(q,e); 
				println("Making query: "+result.queryLabel);
				result.execute();
				println("Response code for "+result.queryLabel+":"+result.responseCode);
				active-=1
				println("Thread count: "+active);
				result.resultModel
			}
		}

	
	}

		
  }
  
    val results = awaitAll(30000L, tasks: _*)
  for(r<-results) {
	  r match{
	  	case None => "Println: a future failed"
		case Some(value) =>totalResults.add(value.asInstanceOf[Model])
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