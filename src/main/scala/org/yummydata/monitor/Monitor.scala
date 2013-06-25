package org.yummydata.monitor

import org.yummydata.monitoraux.Querier

object Monitor extends App{
  val endpoints=EndPointsTeller.getEndpointsList();
  for (e <- endpoints) {
	 
	  println("Testing endpoint: "+e);
	  //First we just make a dumb query to measure state and time
	  val simpleQueryString="select ?x ?y ?z where {?x ?y ?z} limit 10";
	  val simpleQuery=new Querier(simpleQueryString,e);
	  if(!simpleQuery.execute()) {
	    println("Something went wrong");
	    println("Response code: "+simpleQuery.getResponseCode());
	  }
	  else {
		println("Server is UP");
		println("Response time: "+simpleQuery.getTimeDelay());
		val queries=QueriesTeller.queriesForEndpoint(e);
		for(q<-queries) {
		 println("Making query: "+QueriesTeller.getQueryTitle(q));
		 val query=new Querier(QueriesTeller.getQueryStringForQuery(q),e);
		 val resultCorrect=query.execute();		//TODO side effects!
		 if(resultCorrect) {
			println("query OK");
		    val res=query.getResult();
		    if(res.length>0) println("Result: "+res(0));
		 }
		 else {
		   println("query KO");
		   println("Response code: "+query.getResponseCode());
		 }
		
		 println("Response time: "+query.getTimeDelay());
		}
	  }
	println
  }
	
	
}