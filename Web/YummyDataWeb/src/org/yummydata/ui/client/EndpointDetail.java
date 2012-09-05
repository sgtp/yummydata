package org.yummydata.ui.client;

import java.util.Date;

import org.yummydata.ui.shared.Endpoint;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

public class EndpointDetail extends Composite {

	Endpoint endpoint;
	EndpointList endpointList;
	Label lblUri, lblTitle;
	SimplePanel uptimePanel, scorePanel, sizePanel;
	/**
	 * @wbp.parser.constructor
	 */
	public EndpointDetail() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		lblTitle = new Label("Title");
		lblTitle.setStyleName("heading");
		verticalPanel.add(lblTitle);
		
		lblUri = new Label("URI");
		lblUri.setStyleName("subheading");
		verticalPanel.add(lblUri);
		
		Label lblOverallScore = new Label("Overall score");
		verticalPanel.add(lblOverallScore);
		
		scorePanel = new SimplePanel();
		verticalPanel.add(scorePanel);
		
		Label lblUptime = new Label("Uptime");
		verticalPanel.add(lblUptime);
		
		uptimePanel = new SimplePanel();
		verticalPanel.add(uptimePanel);
		
		Label lblNumberOfTriples = new Label("Size (number of triples)");
		verticalPanel.add(lblNumberOfTriples);
		
		sizePanel = new SimplePanel();
		verticalPanel.add(sizePanel);
		
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
		
		scorePanel.add(makeTimeChart("Score"));
		uptimePanel.add(makeTimeChart("Uptime"));
		sizePanel.add(makeTimeChart("Size"));
		
	}
	
	public CoreChart makeTimeChart(String name) {
		DataTable dt = DataTable.create();
		dt.addColumn(ColumnType.DATE, "Date");
		dt.addColumn(ColumnType.NUMBER, name);
		
		for (int i = 0; i < 10; ++i) {
			dt.addRow();
			dt.setValue(i, 0, new Date(112, i, 1));
			dt.setValue(i, 1, Math.random() * 100);
		}
		
		Options o = Options.create();
		o.setReverseCategories(true);
		ColumnChart bc = new ColumnChart(dt, o);
		bc.setSize("500px", "100px");
		return bc;
		
	}

}
