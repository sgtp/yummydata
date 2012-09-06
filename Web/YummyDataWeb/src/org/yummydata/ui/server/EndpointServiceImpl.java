package org.yummydata.ui.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import org.yummydata.store.Query;
import org.yummydata.ui.client.EndpointService;
import org.yummydata.ui.shared.Endpoint;
import org.yummydata.ui.shared.TimePoint;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.icu.text.DateFormat;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class EndpointServiceImpl extends RemoteServiceServlet implements
		EndpointService {

	
	public List<Endpoint> getEndpoints() throws Exception {
		Calendar c = Calendar.getInstance();
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		
		String dateKey = String.format("%02d-%02d-%02d", y, m, d);
		
		final String query = "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
				"select ?endpoint ?uri ?status ?sparkle where { " +
				"?x <http://yummydata.org/meta#hasEndpoint> ?uri. " +
				"?x rdfs:label ?endpoint. " +
				"graph <http://yummydata.org/data/" + dateKey + "> { " +
				"  ?x <http://yummydata.org/scores/sparkles> ?sparkle . " +
				"  ?x <http://yummydata.org/measures/endpointstate> ?status . " +
				"} }";
		
		
		TupleQueryResult tqr = Query.doQuery(query);
		List<Endpoint> eps = new ArrayList<Endpoint>();
		while (tqr.hasNext()) {
			BindingSet bs = tqr.next();
			Iterator<Binding> i = bs.iterator();
			eps.add(new Endpoint(i.next().getValue().stringValue(), 
					i.next().getValue().stringValue(),
					i.next().getValue().stringValue().split("values/")[1],
					Double.valueOf(i.next().getValue().stringValue())));			
		}
		return eps;				
	}
	
	public List<TimePoint<Double>> getTimeSeriesDouble(String name, String endpoint) throws Exception {
		List<TimePoint<Double>> r = new ArrayList<TimePoint<Double>>();
		final String query = "prefix yd:<http://yummydata.org/> " +
				"prefix val:<http://yummydata.org/values/> " +
				"prefix meta:<http://yummydata.org/meta#> " +
				"select ?g ?v where { ?endp meta:hasEndpointString \"" + endpoint + "\" . " +
				"graph ?g { ?endp <http://yummydata.org/" + name + "> ?v } }";
		//Each graph ?g corresponds to a time point.
		//the name of the time series corresponds to a value predicate.
			
		SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
		TupleQueryResult tqr = Query.doQuery(query);
		while(tqr.hasNext()) {
			BindingSet bs = tqr.next();
			Iterator<Binding> i = bs.iterator();
			String date = i.next().getValue().stringValue().split("data/")[1];
			Date d = df.parse(date);
			Double v = Double.valueOf(i.next().getValue().stringValue());
			r.add(new TimePoint<Double>(d, v));
		}
		return r;
	}
	
	public List<TimePoint<String>> getTimeSeriesString(String name, String endpoint) throws Exception {
		List<TimePoint<String>> r = new ArrayList<TimePoint<String>>();
		final String query = "prefix yd:<http://yummydata.org/> " +
				"prefix val:<http://yummydata.org/values/> " +
				"prefix meta:<http://yummydata.org/meta#> " +
				"select ?g ?v where { ?endp meta:hasEndpointString \"" + endpoint + "\" . " +
				"graph ?g { ?endp <http://yummydata.org/" + name + "> ?v } }";
		//Each graph ?g corresponds to a time point.
		//the name of the time series corresponds to a value predicate.
			
		SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
		TupleQueryResult tqr = Query.doQuery(query);
		while(tqr.hasNext()) {
			BindingSet bs = tqr.next();
			Iterator<Binding> i = bs.iterator();
			String date = i.next().getValue().stringValue().split("data/")[1];
			Date d = df.parse(date);			
			r.add(new TimePoint<String>(d, i.next().getValue().stringValue().split("values/")[1]));
		}
		return r;
	}
}
