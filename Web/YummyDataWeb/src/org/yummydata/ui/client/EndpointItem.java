package org.yummydata.ui.client;

import org.yummydata.ui.shared.Endpoint;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
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
	private Label lblScoreDisplay;
	private VerticalPanel verticalPanel_2;
	private Label lblScore;
	private VerticalPanel verticalPanel_3;
	private Label lblChange;
	private Label lblChangeDisplay;
	
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
		
		lblScore = new Label("Score");
		verticalPanel_2.add(lblScore);
		
		lblScoreDisplay = new Label("10");
		lblScoreDisplay.setStyleName("bigStat");
		verticalPanel_2.add(lblScoreDisplay);
		
		verticalPanel_3 = new VerticalPanel();
		verticalPanel_3.setStyleName("margin");
		horizontalPanel.add(verticalPanel_3);
		verticalPanel_3.setWidth("60px");
		
		lblChange = new Label("Change");
		verticalPanel_3.add(lblChange);
		
		lblChangeDisplay = new Label("+ 2");
		lblChangeDisplay.setStyleName("bigStat");
		verticalPanel_3.add(lblChangeDisplay);
	}
	
	public EndpointItem(Endpoint ep, EndpointList list) { 
		this();
		this.endpoint = ep;		
		this.endpointList = list;
		lblTitle.setText(ep.getName());
		lblURI.setText(ep.getURI());
		lblScoreDisplay.setText(endpoint.getScore() + "");
		lblChangeDisplay.setText(endpoint.getScoreChange() + "");
		
		lblTitle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent ce) {
				endpointList.showDetail(endpoint);
//				DialogBox db = new DialogBox(true, true);
//				db.add(new EndpointDetail(endpoint));
//				db.setPopupPosition(Window.getClientWidth()/2 + Window.getScrollLeft() - 100, 
//						Window.getClientHeight()/2 + Window.getScrollTop() - 100);
//				
//				db.show();
			}
		});
	}

}
