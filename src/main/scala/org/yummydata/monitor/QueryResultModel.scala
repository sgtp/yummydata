package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.ModelFactory
import java.util.UUID
import java.util.Date
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
import java.util.Calendar

/**
 * Provides the stub for a test-result in RDF
 */
class QueryResultModel (endpoint:String, queryURI:String) {
	val resultModel=ModelFactory.createDefaultModel()
	val resultURI=YummyInstance.yummyValues+UUID.randomUUID().toString();
	println("Building results entity :"+resultURI);
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
					resultModel.createResource(queryURI));
}