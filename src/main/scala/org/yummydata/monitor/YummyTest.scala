package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.Model
import java.util.UUID
import java.util.Date
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype

/**
 * Common to all test types (essentially test metadata)
 */
abstract class YummyTest (testURI:String,endpoint:String) {
	val resultURI=YummyInstance.yummyValues+UUID.randomUUID().toString() 
	//println("Building test entity :"+resultURI+"\n"+"For endpoint "+endpoint+"\n"+"testURI: "+testURI);
	
	
	val resultModel=ModelFactory.createDefaultModel()
	
	// Matadata init
	val shortDate=YummyInstance.getShortDate()
	val date=new Date()
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"hasDayDate"),
					resultModel.createLiteral(shortDate));
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"hasDate"),
					resultModel.createTypedLiteral(shortDate,XSDDatatype.XSDdate));
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"testing"),
					resultModel.createResource(endpoint));
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"hasTestType"),
					resultModel.createResource(testURI));
	
	/**
	 * convenience method for children to mark test as completed
	 */
	def markAsCompleted(m:Model) ={
		m.add(m.createResource(resultURI),
					m.createProperty(YummyInstance.yummyLang+"hasTerminated"),
					resultModel.createTypedLiteral("True",XSDDatatype.XSDboolean));
	} 
	/**
	 * Must be implemented by the actual test
	 */
	def execute()
	
	
}