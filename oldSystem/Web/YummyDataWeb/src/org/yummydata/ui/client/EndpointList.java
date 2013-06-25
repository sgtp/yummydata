package org.yummydata.ui.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yummydata.ui.shared.Endpoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;

public class EndpointList extends Composite {

	private final EndpointServiceAsync endpointService = GWT
			.create(EndpointService.class);
	
	private EndpointList el = this;
	private Map<String, Endpoint> endpoints = new HashMap<String, Endpoint>();
	
	public EndpointList() {
		Runnable onLoadChart = new Runnable() {
			public void run() {
				
			}
		};		
		
		VisualizationUtils
				.loadVisualizationApi("1.1", onLoadChart, "corechart");
		
		
		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(5);
		initWidget(verticalPanel);
		
		endpointService.getEndpoints(new AsyncCallback<List<Endpoint>>() {
			public void onSuccess(List<Endpoint> eps) {
				for (Endpoint ep :eps) {
					endpoints.put(ep.getID(), ep);
					EndpointItem ed = new EndpointItem(ep, el);
					verticalPanel.add(ed);				
				}
			}
			public void onFailure(Throwable caught) {				
				Window.alert("Unable to get endpoints: " + caught.getMessage());
			}
		});
	}
	
	void showDetail(Endpoint ep) {
		RootPanel.get("mainContainer").remove(this);
		RootPanel.get("mainContainer").add(new EndpointDetail(ep, this));
	}
	
	void showDetail(String id) {		
		showDetail(endpoints.get(id));
	}

}
