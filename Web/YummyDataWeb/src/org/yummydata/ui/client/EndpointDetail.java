package org.yummydata.ui.client;

import java.util.ArrayList;
import java.util.List;

import org.yummydata.ui.shared.Endpoint;
import org.yummydata.ui.shared.TimePoint;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

public class EndpointDetail extends Composite {

	Endpoint endpoint;
	EndpointList endpointList;
	Label lblUri, lblTitle;	
	VerticalPanel verticalPanel;
	
	private final EndpointServiceAsync endpointService = GWT
			.create(EndpointService.class);
	
	
	
	/**
	 * @wbp.parser.constructor
	 */
	public EndpointDetail() {
		
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		lblTitle = new Label("Title");
		lblTitle.setStyleName("heading");
		verticalPanel.add(lblTitle);
		
		lblUri = new Label("URI");
		lblUri.setStyleName("subheading");
		verticalPanel.add(lblUri);
		
		
		Button b = new Button("Back to list");
		verticalPanel.add(b);
		final EndpointDetail ed = this;
		b.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ev) {
				RootPanel.get("mainContainer").remove(ed);
				RootPanel.get("mainContainer").add(endpointList);
			}
		});
	}
	
	public EndpointDetail(Endpoint e, EndpointList list) {
		this();
		endpoint = e;
		endpointList = list;
		lblTitle.setText(e.getName());
		lblUri.setText(e.getURI());
		
		addSeriesChart("Sparkles", "scores/sparkles", e.getURI(), "line");
		addStatusChart("Server status", e.getURI());
		addSeriesChart("Response time", "measures/endpointResponseTime", e.getURI(), "line");
		
	}
	
	public void addSeriesChart(final String title, final String predicate,
			final String URI, final String type) {
		try {
			endpointService.getTimeSeriesDouble(predicate,
					URI,
					new AsyncCallback<List<TimePoint<Double>>>() {
						public void onSuccess(List<TimePoint<Double>> points) {
							CoreChart c = makeTimeChart(title, points, type);
							verticalPanel.add(new Label(title));
							verticalPanel.add(c);
						}

						public void onFailure(Throwable caught) {
							Window.alert("Unable to get " + predicate + " data");
						}
					});
		} catch (Exception e) {
			Window.alert("Unable to get " + predicate + " data");
		}
	}
	
	List<TimePoint<Double>> translateStatus(List<TimePoint<String>> points) {
		List<TimePoint<Double>> r = new ArrayList<TimePoint<Double>> ();
		for (TimePoint<String> p : points) {
			r.add(new TimePoint<Double>(p.getDate(), translateStatus(p.getValue())));			
		}
		return r;
	}
	
	public static Double translateStatus(String status) {
		if (status.equals("NOSERVER")) {
			return 0.0;			
		} else if (status.equals("endpointup")) {
			return 1.0;			
		} else if (status.equals("tempoff")) {
			return 0.5;			
		} else if (status.equals("BADREQUEST")) {
			return 0.5;			
		} else if (status.equals("NOTFOUND")) {
			return 0.0;			
		} else {
			//??
			return 0.0;
		}
	}
	
	void addStatusChart(final String title, final String URI) {
		try {
			endpointService.getTimeSeriesString("measures/endpointstate",
					URI,
					new AsyncCallback<List<TimePoint<String>>>() {
						public void onSuccess(List<TimePoint<String>> points) {
							List<TimePoint<Double>> tran = translateStatus(points);
							
							CoreChart c = makeTimeChart(title, tran, "column");
							verticalPanel.add(new Label(title));
							verticalPanel.add(c);

						}

						public void onFailure(Throwable caught) {
							Window.alert("Unable to get status data");
						}
					});
		} catch (Exception e) {
			Window.alert("Unable to get status data");
		}
	}


	public CoreChart makeTimeChart(String name, List<TimePoint<Double>> points, String type) {
		DataTable dt = DataTable.create();
		dt.addColumn(ColumnType.DATE, "Date");
		dt.addColumn(ColumnType.NUMBER, name);		
		
		int i = 0;
		for(TimePoint<Double> tp: points) {
			dt.addRow();
			dt.setValue(i, 0, tp.getDate());
			dt.setValue(i, 1, tp.getValue());
			i += 1;
		}
		
		Options o = Options.create();
		o.setReverseCategories(true);
		CoreChart r;
		if (type.equals("line")) {
			r = new LineChart(dt, o);
		} else {
			r = new ColumnChart(dt, o);
		}
		
		r.setSize("700px", "150px");
		return r;
		
	}
	

}
