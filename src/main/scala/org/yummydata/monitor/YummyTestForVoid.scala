package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.RDFNode
import com.hp.hpl.jena.rdf.model.NodeIterator
import com.hp.hpl.jena.rdf.model.ModelFactory
import io.Source
import java.net.URL
import java.net.URL
import java.util.UUID
import java.util.Date
import org.yummydata.monitoraux.QEWrapper

class YummyTestForVoid (endpoint:String ) extends YummyTest (YummyInstance.yummyLang+"voidTest",endpoint){
  val voidModel=ModelFactory.createDefaultModel();
  var hasVoid: Boolean=false;


  def execute(): Unit = {  
    
	/**
	 * Testing possible void locations
	 */
	
	/**
	 * Void via http content negotiation on endpoint URL
	 */
	val connection = new URL(endpoint).openConnection
	connection.setRequestProperty("Accept","application/rdf+xml");
	try {
		val content=Source.fromInputStream(connection.getInputStream).getLines
		val input=connection.getInputStream
		voidModel.read(input,endpoint);
		println("VOID: cn test for "+endpoint+" OK");
	  hasVoid=true;
	} catch {
	  case ex:Exception => {
	    hasVoid=false;
	    println("VOID: cn test for "+endpoint+" Failed");
	  }
	}
	/**
	 * Void in endpoint dataset (within dataset)
	 */
	val voidQuery="select ?x where {?x a <http://rdfs.org/ns/void#Dataset>}"
	val qe=new QEWrapper(voidQuery,endpoint)
	qe.execute()
	if(qe.extractListOfResources.size>0) {
	  hasVoid=hasVoid||true;
	  println("VOID: in graph test for "+endpoint+" OK");
	}
	else {
	  println("VOID: in graph test for "+endpoint+" Failed");
	}
	/**
	 * Void in ttl file
	 */
	
	if(endpoint.endsWith("sparql")) {
	  val voidFile=endpoint.replace("sparql","void.ttl");
	  println("Looking for "+voidFile);
	  try {
	    voidModel.read(voidFile,"Turtle");
	    hasVoid=hasVoid&&true;
	    println("VOID: file test for "+endpoint+" OK");
	  }
	  catch {
	    case ex:Exception => {
	       println("VOID: file test for "+endpoint+" Failed  ("+voidFile+")");
	    }
	  }
	  
	}
	
	
	/* TODO here we should read the void, but first we need to be a bit sure 
	 * that we have a general way to find the latest void id and associated data
	 */
	
	if(hasVoid) {
		println("VOID size: "+voidModel.size())
		
		resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"result"),
					resultModel.createLiteral("Found"));
		// TODO here we should fill a bit the response model, fetching information from void.
		val licenseNodesIter=voidModel.listObjectsOfProperty(voidModel.createProperty("http://purl.org/dc/terms/license"))
		if(licenseNodesIter.hasNext()){
			val licenseNode=licenseNodesIter.nextNode()
			if(licenseNode.isResource()) {
				resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"hasLicense"),
					resultModel.createResource(licenseNode.asResource()));
			}
			
		}
	}
	else println("No void");
	markAsCompleted(resultModel)
    
    
  }

}