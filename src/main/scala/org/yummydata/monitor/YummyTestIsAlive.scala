package org.yummydata.monitor
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype


class YummyTestIsAlive(endpoint:String) extends YummyTestWithQuery(YummyInstance.yummyQueries+"defaultPing",endpoint) {
	override def execute:Unit ={
	  queryWrapper.setTimeOut(2000)
	  resultStatus=queryWrapper.execute();
	  responseCode=queryWrapper.getResponseCode();
	  //if(responseCode>0) singleResult=queryWrapper.extractSingleValue();
	  responseTime=queryWrapper.getTimeDelay();
	  
	  resultModel.add(resultModel.createResource(resultURI),
	    				resultModel.createProperty(YummyInstance.yummyLang+"responseCode"),
	    				resultModel.createLiteral((new Integer(responseCode)).toString())
	    				);
	  /*
	  if(responseCode==200) {
	    resultModel.add(resultModel.createResource(resultURI),
		    			resultModel.createProperty(YummyInstance.yummyLang+"result"),
		    			resultModel.createTypedLiteral(new Integer(singleResult))
		    			);
	  }
	  */	  
	  if(responseTime>0) {
	     resultModel.add(resultModel.createResource(resultURI),
						resultModel.createProperty(YummyInstance.yummyLang+"responseTime"),
						resultModel.createTypedLiteral(responseTime,XSDDatatype.XSDdouble)
					);
	  } 
	  markAsCompleted(resultModel)
	}
}