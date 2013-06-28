package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.ModelFactory
import io.Source
import java.net.URL
import java.net.URL
import java.util.UUID
import java.util.Date
import org.yummydata.monitoraux.QEWrapper

class VoidAnalyzer (endpoint:String) {
	val result=new QueryResultModel(endpoint,YummyInstance.yummyLang+"voidTest")
	val resultModel=result.resultModel
	val resultURI=result.resultURI
	
	val voidModel=ModelFactory.createDefaultModel();
	var hasVoid: Boolean=false;
	/**
	 * Testing possible void locations
	 */
	
	/**
	 * Void via http content negotiation on endpoint URL
	 */
	val connection = new URL(endpoint).openConnection
	connection.setRequestProperty("Accept","application/rdf+xml");
	print("Testing for VOID via content negotiation...");
	try {
		val content=Source.fromInputStream(connection.getInputStream).getLines
		val input=connection.getInputStream
		voidModel.read(input,endpoint);
		println("OK");
	  hasVoid=true;
	} catch {
	  case ex:Exception => {
	    hasVoid=false;
	    println("NO");
	  }
	}
	/**
	 * Void in endpoint dataset (within dataset)
	 */
	val voidQuery="select ?x where {?x a <http://rdfs.org/ns/void#Dataset>}"
	val qe=new QEWrapper(voidQuery,endpoint)
	print("Testing for VOID via description in endpoint");
	qe.execute()
	if(qe.extractListOfResources.size>0) {
	  hasVoid=hasVoid||true;
	  println("OK");
	}
	else {
	  println("NO");
	}
	/**
	 * Void in ttl file
	 */
	
	if(endpoint.endsWith("sparql")) {
	  print("Testing for VOID in ttl file");
	  val voidFile=endpoint.replace("sparql","void.ttl");
	  println("Looking for "+voidFile);
	  try {
	    voidModel.read(voidFile,"Turtle");
	    hasVoid=hasVoid&&true;
	    println("OK");
	  }
	  catch {
	    case ex:Exception => {
	       println("NO");
	    }
	  }
	  
	}
	
	
	
	/* TODO here we should read the void, but first we need to be a bit sure 
	 * that we have a general way to find the latest void id and associated data
	 */
	
	if(hasVoid) {
		println("VOID found") 
		println("Void size: "+voidModel.size())
		println("Building results entity :"+resultURI);
		val shortDate=YummyInstance.getShortDate()
		val date=new Date()
		
		resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"result"),
					resultModel.createLiteral("Found"));
		// TODO here we should fill a bit the response model, fetching information from void.
	}
	else println("No void");
	
	
}
