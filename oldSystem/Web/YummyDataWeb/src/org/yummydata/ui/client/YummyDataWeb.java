package org.yummydata.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class YummyDataWeb implements EntryPoint {


	private EndpointList endpointList = new EndpointList();
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		RootPanel.get("mainContainer").add(endpointList);
		History.newItem("list");
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> vce) {
				String token = vce.getValue();
				if (token.equals("list")) {
					RootPanel.get("mainContainer").clear();
					RootPanel.get("mainContainer").add(endpointList);					
				} else {
					RootPanel.get("mainContainer").clear();
					//assume it's an endpoint ID
					endpointList.showDetail(token);
				}
			}
		}); 
	}	
}
