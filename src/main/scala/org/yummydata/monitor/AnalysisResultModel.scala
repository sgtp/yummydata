package org.yummydata.monitor

import com.hp.hpl.jena.rdf.model.ModelFactory
import java.util.UUID
import java.util.Date
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype
//TODO to merge/subclass with QueryResultModel
/**
 * Provides the stub for a test-result in RDF
 */
class AnalysisResultModel (endpoint:String) {
	val resultModel=ModelFactory.createDefaultModel()
	val resultURI=YummyInstance.yummyValues+UUID.randomUUID().toString();
	println("Building (analysis) results entity :"+resultURI);
	val shortDate=YummyInstance.getShortDate()
	val date=new Date()
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"hasDayDate"),
					resultModel.createLiteral(shortDate));
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"hasDate"),
					resultModel.createTypedLiteral(date,XSDDatatype.XSDdate));
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"testing"),
					resultModel.createResource(endpoint));
	resultModel.add(resultModel.createResource(resultURI),
					resultModel.createProperty(YummyInstance.yummyLang+"hasResultType"),
					resultModel.createResource(YummyInstance.yummyLang+"analysisResults"));
}