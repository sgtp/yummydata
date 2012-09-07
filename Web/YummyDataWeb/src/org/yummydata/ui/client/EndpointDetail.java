package org.yummydata.ui.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.yummydata.ui.shared.Endpoint;
import org.yummydata.ui.shared.TimePoint;
import org.yummydata.ui.shared.TimeSeries;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.core.java.util.Collections;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
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

	}
	
	public EndpointDetail(Endpoint e, EndpointList list) {
		this();
		endpoint = e;
		endpointList = list;
		lblTitle.setText(e.getName());
		lblUri.setText(e.getURI());
		
		//Add the standard charts
		SimplePanel sp = new SimplePanel();
		addSeriesChart(sp, "Sparkles", "scores/sparkles", e.getURI(), "line");
		sp = new SimplePanel();
		addStatusChart(sp, "Server status (1 = online, 0 = offline, 0.5 = uncertain)", e.getURI());
		sp = new SimplePanel();
		addSeriesChart(sp, "Response time", "measures/endpointResponseTime", e.getURI(), "line");
		sp = new SimplePanel();
		addSeriesChart(sp, "Number of triples", "measures/numTriples", e.getURI(), "column");
		sp = new SimplePanel();
		addSeriesChart(sp, "Number of subjects", "measures/numSubjects", e.getURI(), "column");
		sp = new SimplePanel();
		addSeriesChart(sp, "Number of predicates", "measures/numPredicates", e.getURI(), "column");
		sp = new SimplePanel();
		addSeriesChart(sp, "Number of objects", "measures/numObjects", e.getURI(), "column");

		try {
			endpointService.getCustomSeries(e.getURI(),
					new AsyncCallback<List<TimeSeries>>() {
						public void onSuccess(List<TimeSeries> data) {
							for (TimeSeries ts : data) {
								SimplePanel sp = new SimplePanel();
								verticalPanel.add(new Label(ts.getName()));
								CoreChart c = makeTimeChart(ts.getName(),
										ts.getPoints(), "column");
								sp.add(c);
								verticalPanel.add(sp);
							}
						}

						public void onFailure(Throwable caught) {
							Window.alert("Error getting custom metrics: "
									+ caught.getMessage());
						}
					});
		} catch (Exception ex) {
			Window.alert("Error getting custom metrics: " + ex.getMessage());
		}
		
	}
	
	public void addSeriesChart(final SimplePanel panel, final String title, final String predicate,
			final String URI, final String type) {
		verticalPanel.add(new Label(title));
		verticalPanel.add(panel);
		try {
			endpointService.getTimeSeriesDouble(predicate,
					URI,
					new AsyncCallback<List<TimePoint<Double>>>() {
						public void onSuccess(List<TimePoint<Double>> points) {
							if (points.size() > 0) {
								CoreChart c = makeTimeChart(title, points, type);								
								panel.add(c);
							}
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
	
	void addStatusChart(final SimplePanel panel, final String title, final String URI) {
		verticalPanel.add(new Label(title));
		verticalPanel.add(panel);
		try {
			endpointService.getTimeSeriesString("measures/endpointstate",
					URI,
					new AsyncCallback<List<TimePoint<String>>>() {
						public void onSuccess(List<TimePoint<String>> points) {
							List<TimePoint<Double>> tran = translateStatus(points);							
							CoreChart c = makeTimeChart(title, tran, "column");							
							panel.add(c);
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
		
		java.util.Collections.sort(points);
		
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
