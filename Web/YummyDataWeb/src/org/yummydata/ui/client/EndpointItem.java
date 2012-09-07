package org.yummydata.ui.client;

import org.yummydata.ui.shared.Endpoint;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EndpointItem extends Composite {

	Label lblTitle, lblURI;
	String URI, title;
	Endpoint endpoint;
	EndpointList endpointList;
	private VerticalPanel verticalPanel_1;
	private HorizontalPanel horizontalPanel;
	private Label lblSparkleDisplay;
	private VerticalPanel verticalPanel_2;
	private Label lblScore;
	private VerticalPanel verticalPanel_3;
	private Label lblChange;
	private Label lblStatusDisplay;
	
	/**
	 * @wbp.parser.constructor
	 */
	public EndpointItem() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setBorderWidth(1);
		initWidget(verticalPanel);
		verticalPanel.setWidth("600px");
		
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.add(horizontalPanel);
		
		verticalPanel_1 = new VerticalPanel();
		horizontalPanel.add(verticalPanel_1);
		verticalPanel_1.setWidth("500px");
		
		lblTitle = new Label("Title");
		verticalPanel_1.add(lblTitle);
		lblTitle.setStyleName("heading");
		
		
		lblURI = new Label("http://example.com");
		verticalPanel_1.add(lblURI);
		lblURI.setStyleName("subheading");
		
		verticalPanel_2 = new VerticalPanel();
		verticalPanel_2.setStyleName("margin");
		horizontalPanel.add(verticalPanel_2);
		verticalPanel_2.setWidth("50px");
		
		lblScore = new Label("Sparkle");
		verticalPanel_2.add(lblScore);
		
		lblSparkleDisplay = new Label("10");
		lblSparkleDisplay.setStyleName("bigStat");
		verticalPanel_2.add(lblSparkleDisplay);
		
		verticalPanel_3 = new VerticalPanel();
		verticalPanel_3.setStyleName("margin");
		horizontalPanel.add(verticalPanel_3);
		verticalPanel_3.setWidth("60px");
		
		lblChange = new Label("Status");
		verticalPanel_3.add(lblChange);
		
		lblStatusDisplay = new Label("OK");
		lblStatusDisplay.setStyleName("bigStat");
		verticalPanel_3.add(lblStatusDisplay);
	}
	
	public EndpointItem(final Endpoint ep, EndpointList list) { 
		this();
		this.endpoint = ep;		
		this.endpointList = list;
		lblTitle.setText(ep.getName());
		lblURI.setText(ep.getURI());
		lblSparkleDisplay.setText(endpoint.getLastSparkle().intValue() + "");
		
		
		double statScore = EndpointDetail.translateStatus(endpoint.getLastStatus());
		if (statScore == 1.0) {
			lblStatusDisplay.setStyleName("bigStatOK");
			lblStatusDisplay.setText("OK");
		} else if (statScore == 0.0) {
			lblStatusDisplay.setStyleName("bigStatBad");
			lblStatusDisplay.setText("NG");
		} else {
			lblStatusDisplay.setStyleName("bigStatQuestionable");
			lblStatusDisplay.setText("?");
		}
		
		lblTitle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ce) {
				History.newItem(ep.getID());
//				endpointList.showDetail(endpoint);
			}
		});
	}

}
