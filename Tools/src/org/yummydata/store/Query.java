package org.yummydata.store;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.repository.sparql.SPARQLRepository;

public class Query {

	public static void main(String[] args) throws Exception {
		TupleQueryResult tqr = doQuery("select ?p where { ?s ?p ?o . } ");
	}

	public static RepositoryConnection getConnection()
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, RepositoryConfigException {
		String url = "http://monomorphic.org:8001/openrdf-sesame";
		RepositoryManager rm = RemoteRepositoryManager.getInstance(url, "bh12",
				"BioHackathon2012");
		rm.initialize();
		Repository r = rm.getRepository("yummy");
		RepositoryConnection rc = r.getConnection();
		return rc;
	}
	
	private static void printEx(Throwable e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
//		if (e.getCause() != null) {
//			System.out.println("Cause is:");
//			printEx(e.getCause());
//		}
	}

	public static TupleQueryResult doQuery(String query)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, RepositoryConfigException {
		// String url =
		// "http://user:pass@monomorphic.org:8001/openrdf-sesame/repositories/yummy/query";
		// SPARQLRepository sr = new SPARQLRepository(url);

		// sr.initialize();
		// RepositoryConnection rc = sr.getConnection();

		RepositoryConnection rc = getConnection();

		System.out.println(query);
		TupleQuery q = rc.prepareTupleQuery(QueryLanguage.SPARQL, query);
		TupleQueryResult tqr = null;
		try {
			tqr = q.evaluate();		
		} catch (QueryEvaluationException e) {
			printEx(e);			
			throw e;
		} finally {
			rc.close();
		}
		return tqr;
	}

	public static void doUpdate(String update) throws RepositoryException,
			MalformedQueryException, QueryEvaluationException,
			RepositoryConfigException, UpdateExecutionException {
		RepositoryConnection rc = getConnection();
		System.out.println(update);
		Update u = rc.prepareUpdate(QueryLanguage.SPARQL, update);
		u.execute();
		rc.close();
	}

	public static TupleQueryResult doQuery(String URI, String query)
			throws RepositoryException, MalformedQueryException,
			QueryEvaluationException, RepositoryConfigException {

		SPARQLRepository sr = new SPARQLRepository(URI);
		sr.initialize();
		RepositoryConnection rc = sr.getConnection();
		System.out.println(query);
		TupleQuery q = rc.prepareTupleQuery(QueryLanguage.SPARQL, query);
		TupleQueryResult tqr = q.evaluate();
		return tqr;
	}
}
