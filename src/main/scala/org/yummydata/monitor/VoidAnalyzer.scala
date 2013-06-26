package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.ModelFactory
import io.Source
import java.net.URL
import java.net.URL
import java.util.UUID
import java.util.Date

class VoidAnalyzer (endpoint:String) {
	//Can we read the model?
	val responseModel=ModelFactory.createDefaultModel();
	
	
	val voidModel=ModelFactory.createDefaultModel();
	val connection = new URL(endpoint).openConnection
	var hasVoid: Boolean=false;
	connection.setRequestProperty("Accept","application/rdf+xml");
	try {
		val content=Source.fromInputStream(connection.getInputStream).getLines
		val input=connection.getInputStream
		voidModel.read(input,endpoint);
		//for(c<-content) println(c)
	  hasVoid=true;
	} catch {
	  case ex:Exception => {
	    hasVoid=false;
	  }
	}
	if(hasVoid) {
		println("VOID found") 
		println("Void size: "+voidModel.size())
		//TODO maybe this code should be re-factorized (a result element ?)
		val resultURI=YummyInstance.yummyValues+UUID.randomUUID().toString();
		println("Building results entity :"+resultURI);
		val shortDate=YummyInstance.getShortDate()
		val date=new Date()
		responseModel.add(responseModel.createResource(resultURI),
					responseModel.createProperty(YummyInstance.yummyLang+"hasDayDate"),
					responseModel.createLiteral(shortDate));
		responseModel.add(responseModel.createResource(resultURI),
					responseModel.createProperty(YummyInstance.yummyLang+"hasDate"),
					responseModel.createTypedLiteral(date));
		responseModel.add(responseModel.createResource(resultURI),
					responseModel.createProperty(YummyInstance.yummyLang+"testing"),
					responseModel.createResource(endpoint));
		responseModel.add(responseModel.createResource(resultURI),
					responseModel.createProperty(YummyInstance.yummyLang+"hasTestType"),
					responseModel.createResource(YummyInstance.yummyLang+"voidCheck"));
		responseModel.add(responseModel.createResource(resultURI),
					responseModel.createProperty(YummyInstance.yummyLang+"result"),
					responseModel.createLiteral("Found"));
		// TODO here we should fill a bit the response model
	}
	else println("No void");
	
	
}
