package org.yummydata.store;


import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;

public class Query {

	public static void main(String[] args) throws Exception {
		TupleQueryResult tqr = doQuery("select ?p where { ?s ?p ?o . } ");
	}
	
	public static TupleQueryResult doQuery(String query) throws RepositoryException, 
		MalformedQueryException, QueryEvaluationException, RepositoryConfigException {
//		String url = "http://user:pass@monomorphic.org:8001/openrdf-sesame/repositories/yummy/query";
//		SPARQLRepository sr = new SPARQLRepository(url);
		
//		sr.initialize();
//		RepositoryConnection rc = sr.getConnection();
		      
		String url = "http://monomorphic.org:8001/openrdf-sesame";
		RepositoryManager rm = RemoteRepositoryManager.getInstance(url, "user", "pass");		
		rm.initialize();
		Repository r = rm.getRepository("yummy");
		RepositoryConnection rc = r.getConnection();

		System.out.println(query);
		TupleQuery q = rc.prepareTupleQuery(QueryLanguage.SPARQL, query);
		TupleQueryResult tqr = q.evaluate();
		return tqr;
	}
}
