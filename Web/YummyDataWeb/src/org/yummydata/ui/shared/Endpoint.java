package org.yummydata.ui.shared;

import java.io.Serializable;

public class Endpoint implements Serializable {

	private String name, URI;

	public Endpoint() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getURI() {
		return URI;
	}
	
	public Endpoint(String name, String URI) {
		this.name = name;
		this.URI = URI;
	}
	
	public int getScore() {
		return (int) (Math.random() * 100);
	}
	
	public int getScoreChange() {
		return (int) (Math.random() * 20) -10;
	}
}
