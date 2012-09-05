package org.yummydata.ui.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import org.yummydata.store.Query;
import org.yummydata.ui.client.EndpointService;
import org.yummydata.ui.shared.Endpoint;
import org.yummydata.ui.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EndpointServiceImpl extends RemoteServiceServlet implements
		EndpointService {

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
	
	public List<Endpoint> getEndpoints() throws Exception {
//		List<Endpoint> r = new ArrayList<Endpoint>();		
//		r.add(new Endpoint("Test", "http://www.test.com"));
		
		final String query = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"select ?endpoint ?uri where { " +
				"?x <http://yummydata.org/meta#hasEndpoint> ?uri. " +
				"?x rdfs:label ?endpoint. }";
		
		TupleQueryResult tqr = Query.doQuery(query);
		List<Endpoint> eps = new ArrayList<Endpoint>();
		while (tqr.hasNext()) {
			BindingSet bs = tqr.next();
			Iterator<Binding> i = bs.iterator();
			eps.add(new Endpoint(i.next().getValue().stringValue(), 
					i.next().getValue().stringValue()));			
		}
		return eps;				
	}
}
