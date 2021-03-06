package org.yummydata.ui.shared;

import java.io.Serializable;
import java.util.Date;

public class TimePoint<T> implements Serializable, Comparable<TimePoint<T>> {

	T value;
	Date date;
	
	public TimePoint() {
		
	}
	
	public TimePoint(Date date, T value) {
		this.value = value;
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
	public T getValue() {
		return value;
	}

	public int compareTo(TimePoint<T> other) {
		return date.compareTo(other.getDate());
	}
}
