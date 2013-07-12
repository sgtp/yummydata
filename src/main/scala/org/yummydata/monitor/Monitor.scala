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
  System.setProperty("actors.corePoolSize", "30")
  System.setProperty("actors.minPoolSize", "10")
  System.setProperty("actors.enableForkJoin","false")
  val endpoints=EndPointsTeller.getEndpointsList();
  val totalResults=ModelFactory.createDefaultModel();
  
  var threadsActive=0;
  var tasks= List[Future[Model]]()
 
  for (e <- endpoints) {
    println("***************************");
    println("Testing endpoint: "+e);
	println("***************************");
	 
	//println("Blocking test is alive for "+e);
	val isAlive=new YummyTestIsAlive(e)
	isAlive.execute();
	if(!isAlive.resultStatus) {
		println("SERVER DOWN: "+e)
	}
	else {
		println("UP ("+e+") answ. in "+isAlive.responseTime);
		totalResults.add(isAlive.resultModel);
		
		//Void provided ?
		
		tasks ::= future {
			threadsActive+=1
		    //println("checking void for "+e+" Thread count: "+threadsActive);
			val voidAnalyzer=new YummyTestForVoid(e);
			voidAnalyzer.execute()
			threadsActive-=1
			println("void checked ("+e+") Thread count: "+threadsActive);
			voidAnalyzer.resultModel
			
		}
		
	  
		val queries=QueriesTeller.queriesForEndpoint(e);
		///
		for (q <- queries) {
			tasks ::= future {
				threadsActive+=1
				//println("Thread count: "+threadsActive);
				val result=new YummyTestWithQuery(q,e); 
				//println("Making query: "+result.testLabel);
				result.execute();
				val response=result.responseCode
				print("Response code for "+result.testLabel+" on "+e+" :"+response);
				if(response==200) println(" value "+result.singleResult) 
				else println
				threadsActive-=1
				println("Active threads count: "+threadsActive);
				result.resultModel
			}
		}

	
	}

		
  }
  
    val results = awaitAll(3000000L, tasks: _*)
  for(r<-results) {
	  r match{
	  	case None => "+++ Println: a future failed"
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