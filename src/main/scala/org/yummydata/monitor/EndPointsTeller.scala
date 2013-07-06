package org.yummydata.monitor

import org.yummydata.monitoraux.QEWrapper
/**
 * Returns a list of endpoint.
 * Queries the yummyData endpoint now, but could directly try to access CKAN.
 */
object EndPointsTeller {
	val endpointQuery="select ?x where {graph ?g{?x a <http://yummydata.org/lang#endPoint>}}"
	def getEndpointsList(): Array[String] = {
	  val qwrap=new QEWrapper(endpointQuery,YummyInstance.yummyEndpoint);
	  qwrap.execute();
	  val result=qwrap.extractListOfResources();
	  for(r<-result) {
	    println("endpoint to check: "+r);
	  }
	  return result
	}
}
	
