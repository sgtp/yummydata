package org.yummydata.ui.shared;

import java.io.Serializable;

public class Endpoint implements Serializable {

	private String name, URI;
	private String status;
	private Double sparkle;

	public Endpoint() {
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getURI() {
		return URI;
	}
	
	public String getLastStatus() {
		return status;
	}
	
	public Double getLastSparkle() {
		return sparkle;
	}
	
	public Endpoint(String name, String URI, String status, Double sparkle) {
		this.name = name;
		this.URI = URI;
		this.sparkle = sparkle;
		this.status = status;
	}
	
//	public int getScore() {
//		return (int) (Math.random() * 100);
//	}
//	
//	public int getScoreChange() {
//		return (int) (Math.random() * 20) -10;
//	}
}
