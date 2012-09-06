package org.yummydata.ui.client;

import java.util.List;

import org.yummydata.ui.shared.Endpoint;
import org.yummydata.ui.shared.TimePoint;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface EndpointServiceAsync {
	void getEndpoints(AsyncCallback<List<Endpoint>> callback)
			throws IllegalArgumentException;
	
	public void getTimeSeriesDouble(String name, String endpoint,
			AsyncCallback<List<TimePoint<Double>>> callback) throws Exception;
	
	public void getTimeSeriesString(String name, String endpoint,
			AsyncCallback<List<TimePoint<String>>> callback) throws Exception;
}
