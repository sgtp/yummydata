package org.yummydata.ui.client;

import java.util.List;

import org.yummydata.ui.shared.Endpoint;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface EndpointService extends RemoteService {
	List<Endpoint> getEndpoints() throws Exception;
	
}
