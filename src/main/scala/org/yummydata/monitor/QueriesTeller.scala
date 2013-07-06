package org.yummydata.monitor

import org.yummydata.monitoraux.QEWrapper
import java.util.ArrayList

/**
 * This is a stub objects that returns a few queries.
 * It's real implementation should query a triplestore to return queries in a triple-store specific manner.
 */
object QueriesTeller {
	val allQueries="select ?x where {graph ?g {?x a <http://yummydata.org/lang#Test> . ?x <http://yummydata.org/lang#appliesTo> <http://yummydata.org/lang#all>}}";

	def queriesForEndpoint(endpoint:String): Array[String] = {
	  val endPointSpecificQuery="select ?x where {graph ?g{?x a <http://yummydata.org/lang#Test> . ?x <http://yummydata.org/lang#appliesTo> <"+endpoint+">}}";
	  println("Looking for queries for: "+endpoint);
	  val allQuery=new QEWrapper(allQueries,YummyInstance.yummyEndpoint);
	  val specificQuery=new QEWrapper(endPointSpecificQuery,YummyInstance.yummyEndpoint);
	  var result: Array[String]=new Array[String](0);
	  if(!allQuery.execute()) {
	    println("Something went wrong with default query");
	  }
	  else {
	    result=result++allQuery.extractListOfResources();
	  }
	  if(!specificQuery.execute()) {
	    println("Something went wrong with specific query");
	  }
	  else {
	     result=result++specificQuery.extractListOfResources();
	  }
	  for(r<-result) {
	    println("found "+r);
	  }
	  return result;
	
	}
}

