package org.yummydata.monitoraux;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;


public class Querier {
	private String queryString=null;
	private String endpoint=null;
	private int responseCode=200; //If nothing went wrong, we assume things are ok
	private long responseTime=0;
	private ArrayList<String> resultList=null;
	/**
	 * 
	 * @param query the query text
	 * @param endpoint the endpoint this query should be asked to
	 */
	public Querier(String query, String endpoint) {
		super();
		this.queryString = query;
		this.endpoint = endpoint;
	}
	

	public boolean execute() { 
		QueryEngineHTTP qe=new QueryEngineHTTP(endpoint,queryString);
		resultList=new ArrayList<String>();
		//qe.setTimeout(100000);
		long startTime=System.nanoTime();
		try {
		    ResultSet rs = qe.execSelect();
		    //TODO now we are expecting only a single literal result. Everything else is up for inconsistency!
		    List<String> vars=rs.getResultVars();
		    if ( rs.hasNext() ) {
		    	QuerySolution result=rs.next();
		    	for(String var:vars) {
		    		RDFNode resNode=result.get(var);
		    		if(resNode.isLiteral()) 
		    			resultList.add(resNode.asLiteral().getValue().toString());
		    	}
		        //System.out.println(ResultSetFormatter.asText(rs));
		    }
		    responseTime=System.nanoTime()-startTime;
		}
		catch (QueryExceptionHTTP we) {
			responseCode=we.getResponseCode();
			responseTime=-1;
			return false;
		}
		catch (Exception ee) {
			System.out.println("Something really bad went wrong");
			responseTime=-1;
			responseCode=0;
			System.out.println(ee.getMessage());
			return false;
		}
		finally {
			qe.close();
		}
		return true;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public double getTimeDelay() {
		return responseTime;
	}
	
	public String[] getResult() {
		return resultList.toArray(new String[0]);
	}

	
}
