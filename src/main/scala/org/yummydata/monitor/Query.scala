package org.yummydata.monitor

import java.text.SimpleDateFormat
import java.util.Date
import java.util.List
import java.util.UUID
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP
import org.yummydata.monitoraux.QEWrapper
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype


class Query (queryURI:String, endpoint:String) {
	val result=new QueryResultModel(endpoint,queryURI)
	val resultModel=result.resultModel
	val resultURI=result.resultURI
	val queryForDetails="Construct {<"+queryURI+"> ?p ?o} where { graph ?g{ <"+queryURI+"> ?p ?o}}";
	val qqe=new QueryEngineHTTP(YummyInstance.yummyEndpoint,queryForDetails);
	val queryDetailModel=qqe.execConstruct();
	
	
	/*
	println("Exection of: "+queryForDetails);
	println("on: "+YummyInstance.yummyEndpoint);
	println("results: ");
	val statsIter=queryDetailModel.listStatements();
	while(statsIter.hasNext()) {
		var stat=statsIter.nextStatement();
		println(stat.getPredicate()+" - "+stat.getObject())
	}
	*/
	
	/*
	 * Here we can extract information about the query, should we need it...
	 */
	var nodeIter=queryDetailModel.listObjectsOfProperty(queryDetailModel.createProperty(YummyInstance.yummyLang+"query"));
	val queryString=nodeIter.nextNode().asLiteral().getValue().toString();
	
	//Title
	nodeIter=queryDetailModel.listObjectsOfProperty(queryDetailModel.createProperty("http://www.w3.org/2000/01/rdf-schema#label"));
	val queryLabel=nodeIter.nextNode().asLiteral().getValue().toString();

	val queryWrapper=new QEWrapper(queryString,endpoint);
	var resultStatus=false
	var responseCode=0
	var singleResult : Int = -1
	var responseTime : Double = -1
	
	
	def execute()  = { 
	  resultStatus=queryWrapper.execute();
	  responseCode=queryWrapper.getResponseCode();
	  if(responseCode>0) singleResult=queryWrapper.extractSingleValue();
	  responseTime=queryWrapper.getTimeDelay();
	  
	  
	  resultModel.add(resultModel.createResource(resultURI),
	    				resultModel.createProperty(YummyInstance.yummyLang+"responseCode"),
	    				resultModel.createLiteral((new Integer(responseCode)).toString())
	    				);
	  
					
	  
	  if(responseCode==200) {
	    resultModel.add(resultModel.createResource(resultURI),
		    			resultModel.createProperty(YummyInstance.yummyLang+"result"),
		    			resultModel.createTypedLiteral(new Integer(singleResult))
		    			);
	  }
		  
	  if(responseTime>0) {
	     resultModel.add(resultModel.createResource(resultURI),
						resultModel.createProperty(YummyInstance.yummyLang+"responseTime"),
						resultModel.createTypedLiteral(responseTime,XSDDatatype.XSDdouble)
					);
	  } 
	  
	 
		   
	
	}
	
	
}