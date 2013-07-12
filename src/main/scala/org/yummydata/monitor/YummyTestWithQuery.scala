package org.yummydata.monitor

import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP
import org.yummydata.monitoraux.QEWrapper
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import com.hp.hpl.jena.rdf.model.ModelFactory

class YummyTestWithQuery (testURI:String,endpoint:String) extends YummyTest(testURI,endpoint) {
	// Retrieving test details (query)
	val queryForTestDetails="Construct {<"+testURI+"> ?p ?o} where { graph ?g{ <"+testURI+"> ?p ?o}}";
	val queryForTestDetailsEngine=new QueryEngineHTTP(YummyInstance.yummyEndpoint,queryForTestDetails);
	val testDetailModel=queryForTestDetailsEngine.execConstruct();
	var nodeIter=testDetailModel.listObjectsOfProperty(testDetailModel.createProperty(YummyInstance.yummyLang+"query"));
	val testQueryString=nodeIter.nextNode().asLiteral().getValue().toString();
	nodeIter=testDetailModel.listObjectsOfProperty(testDetailModel.createProperty("http://www.w3.org/2000/01/rdf-schema#label"));
	val testLabel=nodeIter.nextNode().asLiteral().getValue().toString();
	//Setting up query execution
	val queryWrapper=new QEWrapper(testQueryString,endpoint);
		
	var resultStatus=false
	var responseCode=0
	var singleResult : Int = -1
	var responseTime : Double = -1
	def execute:Unit ={
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
	  markAsCompleted(resultModel)
	}
}