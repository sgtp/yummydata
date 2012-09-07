package org.yummydata.ui.shared;

import java.io.Serializable;
import java.util.List;

public class TimeSeries implements Serializable {

	String name;
	List<TimePoint<Double>> points;
	
	public TimeSeries() {
		
	}
	
	public TimeSeries(String name, List<TimePoint<Double>> points) {
		this.name = name;
		this.points = points;
	}
	
	public String getName() {
		return name;
	}
	
	public List<TimePoint<Double>> getPoints() {
		return points;
	}
}
