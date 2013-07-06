package org.yummydata.monitoraux;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;
/**
 * A minimal wrapper for query functionalites
 * @author andreasplendiani
 *
 */
public class QEWrapper {
	private String queryString=null;
	private String endpoint=null;
	private int responseCode=200; //If nothing went wrong, we assume things are ok
	private long responseTime=0;
	private int timeout=10000;
	private ResultSet rs=null;
	private int intResult=-1;
	private ArrayList<String> arrayResult=null;

	/**
	 * 
	 * @param query the query text
	 * @param endpoint the endpoint this query should be asked to
	 */
	public QEWrapper(String queryString, String endpoint) {
		super();
		this.queryString = queryString;
		this.endpoint = endpoint;
	}
	
	public ResultSet getSelectResult() {
		return rs;
	}

	public boolean execute() { 
		System.out.println("Query execution :"+queryString+" -on- "+endpoint);
		arrayResult=new ArrayList<String>();
		QueryEngineHTTP qe=new QueryEngineHTTP(endpoint,queryString);
		if(timeout>0) qe.setTimeout(timeout);
		long startTime=System.nanoTime();
		try {
		    rs = qe.execSelect();
		    responseTime=System.nanoTime()-startTime;
		    //We try to extract possible results straight away
		    
			List<String> vars=rs.getResultVars();
			while( rs.hasNext()) {
				QuerySolution qs=rs.next();
			    for(String var:vars) {
			    	RDFNode resNode=qs.get(var);
			    	if(resNode.isResource())  {
			    		arrayResult.add(resNode.asResource().getURI());
			    	}
			    	else if(resNode.isLiteral()) {
			    		try {
			    			intResult=Integer.parseInt(resNode.asLiteral().getValue().toString());
			    		} catch(Exception e) {
			    			System.out.println(e.getMessage());
			    		}
			    	}
			    }
			}
			

			
			
			////
		    return true;
		}
		catch (QueryExceptionHTTP we) {
			responseCode=we.getResponseCode();
			responseTime=-1;
			if(responseCode==QueryExceptionHTTP.noResponseCode) responseCode=0;
			return false;
		}
		catch (Exception ee) {
			System.out.println("Something really bad went wrong");
			responseTime=-1;
			responseCode=-1;
			System.out.println(ee.getMessage());
			return false;
		}
		finally {
			qe.close();
		}
		
	}
	public int getResponseCode() {
		return responseCode;
	}
	public double getTimeDelay() {
		return responseTime;
	}
	
	public String[] extractListOfResources() {
		return arrayResult.toArray(new String[0]);
		/*
		ArrayList<String> result=new ArrayList<String>();
		List<String> vars=rs.getResultVars();
		while( rs.hasNext()) {
			QuerySolution qs=rs.next();
		    for(String var:vars) {
		    	RDFNode resNode=qs.get(var);
		    	if(resNode.isResource())  {
		    		result.add(resNode.asResource().getURI());
		    	}
		    }
		}
		return result.toArray(new String[0]);
		*/
	}
	
	public int extractSingleValue() {
		return intResult;
		/*
		if(rs==null) return -1;
		int result=-1;
		List<String> vars=rs.getResultVars();
		if( rs.hasNext()) {
			QuerySolution qs=rs.next();
		    for(String var:vars) {
		    	RDFNode resNode=qs.get(var);
		    	if(resNode.isLiteral())  {
		    		try {
		    			result=Integer.parseInt(resNode.asLiteral().getValue().toString());
		    		} catch(Exception e) {
		    			System.out.println(e.getMessage());
		    		}
		    	}
		    }
		}
		return result;
		*/
	}
	

	
}
