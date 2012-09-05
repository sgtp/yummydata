package org.yummydata.ui.client;

import java.util.List;

import org.yummydata.ui.shared.Endpoint;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface EndpointServiceAsync {
	void getEndpoints(AsyncCallback<List<Endpoint>> callback)
			throws IllegalArgumentException;
}
