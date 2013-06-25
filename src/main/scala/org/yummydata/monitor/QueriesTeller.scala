package org.yummydata.monitor
/**
 * This is a stub objects that returns a few queries.
 * It's real implementation should query a triplestore to return queries in a triple-store specific manner.
 */
object QueriesTeller {
	val yummyEndpoint="http://yummydata.org:3030/yummy/";
	
	/*
	 * TODO Note that this is only temporal data. Ultimately we would ask the triplestore for queries.
	 */
	val totalNumberOfTriplesURI="http://yummydata.org/queries#void_totalNumberOfTriples";
	val totalNumberOfTriplesString="SELECT (COUNT(*) AS ?no) { ?s ?p ?o  }";
	val totalNumberOfTriplesLabel="total number of triples";
	val totalNumberOfTriplesType="http://yummydata.org/lang#singleValueQuery";
	
	val totalNumberOfEntitiesURI="http://yummydata.org/queries#void_totalNumberOfEntities";
	val totalNumberOfEntitiesString="SELECT (COUNT(*) AS ?no) { ?s ?p ?o  }";
	val totalNumberOfEntitiesLabel="total number of entities";
	val totalNumberOfEntitiesType="http://yummydata.org/lang#singleValueQuery";
	
	
	val totalNumberOfDistinctPredicatesURI="http://yummydata.org/queries#void_totalNumberOfDistinctPredicates";
	val totalNumberOfDistinctPredicatesString="SELECT COUNT(distinct ?o) AS ?no { ?s rdf:type ?o }";
	val totalNumberOfDistinctPredicatesLabel="total number of distinct predicates";
	val totalNumberOfDistinctPredicatesType="http://yummydata.org/lang#singleValueQuery";
	
		
	val totalNumberOfDistinctSubjectNodesURI="http://yummydata.org/queries#void_totalNumberOfDistinctSubjectNodes";
	val totalNumberOfDistinctSubjectNodesString="SELECT count(distinct ?p) { ?s ?p ?o }";
	val totalNumberOfDistinctSubjectNodesLabel="total number of distinct subject nodes";
	val totalNumberOfDistinctSubjectNodesType="http://yummydata.org/lang#singleValueQuery";
	
	
	val propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyURI="http://yummydata.org/queries#void_propertyVsTotalNumberOfDistinctObjectsInTriplesUsingTheProperty";
	val propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyString="SELECT  ?p (COUNT(DISTINCT ?o ) AS ?count ) { ?s ?p ?o } GROUP BY ?p ORDER BY ?count";
	val propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyLabel="property vs. total number of distinct objects in triples using the property";
	val propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyType="http://yummydata.org/lang#attributeValuesQuery";
	

	
	
	
	def queriesForEndpoint(endpoint:String): Array[String] = {
	  val dummyResult=Array(totalNumberOfTriplesURI,
	      totalNumberOfEntitiesURI,
	      totalNumberOfDistinctPredicatesURI,
	      totalNumberOfDistinctSubjectNodesURI
	  )
		return dummyResult;
	}
	
	def getQueryStringForQuery(query:String): String = {
	  //println("Resolving "+query)
	   
	  if(query==totalNumberOfTriplesURI) {
	    return totalNumberOfTriplesString
	  }
	  else if(query==totalNumberOfEntitiesURI) {
	    return totalNumberOfEntitiesString
	  }
	  else if(query==totalNumberOfDistinctPredicatesURI) {
	    return totalNumberOfDistinctPredicatesString
	  }
	  else if(query==totalNumberOfDistinctSubjectNodesURI) {
	    return totalNumberOfDistinctSubjectNodesString
	  }
	  else if(query==propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyURI) {
	    return propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyString
	  }
	  else throw new Exception("Undefined Query: "+query);		
	  return "";
	}
	
	
	
	def getQueryTitle(query:String): String = {
	  if(query==totalNumberOfTriplesURI) {
	    return totalNumberOfTriplesLabel
	  }
	  else if(query==totalNumberOfEntitiesURI) {
	    return totalNumberOfEntitiesLabel
	  }
	  else if(query==totalNumberOfDistinctPredicatesURI) {
	    return totalNumberOfDistinctPredicatesLabel
	  }
	  else if(query==totalNumberOfDistinctSubjectNodesURI) {
	    return totalNumberOfDistinctSubjectNodesLabel
	  }
	  else if(query==propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyURI) {
	    return propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyLabel
	  }
	  else throw new Exception("Undefined Query: "+query);		
	  return "";
	}
	
	def isSingleValueQuery(query:String) :Boolean = {
	  if(query==totalNumberOfTriplesURI) {
	    return true
	  }
	  else if(query==totalNumberOfEntitiesURI) {
	    return true
	  }
	  else if(query==totalNumberOfDistinctPredicatesURI) {
	    return true
	  }
	  else if(query==totalNumberOfDistinctSubjectNodesURI) {
	    return true
	  }
	  else if(query==propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyURI) {
	    return false
	  }
	  else throw new Exception("Undefined Query: "+query);		
	  return false;
	}
	
	def isAttributeValuesQuery(query:String) :Boolean = {
	  if(query==totalNumberOfTriplesURI) {
	    return false
	  }
	  else if(query==totalNumberOfEntitiesURI) {
	    return false
	  }
	  else if(query==totalNumberOfDistinctPredicatesURI) {
	    return false
	  }
	  else if(query==totalNumberOfDistinctSubjectNodesURI) {
	    return false
	  }
	  else if(query==propertyVsTotalNumberOfDistinctObjectsInTriplesUsingThePropertyURI) {
	    return true
	  }
	  else throw new Exception("Undefined Query: "+query);		
	  return false;
	}
}

