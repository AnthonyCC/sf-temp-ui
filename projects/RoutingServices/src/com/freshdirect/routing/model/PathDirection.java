package com.freshdirect.routing.model;

import java.util.List;

public class PathDirection extends BaseModel implements IPathDirection {
	
	 private int time;

	 private double distance;
	 
	 private List directionsArc;

	public List getDirectionsArc() {
		return directionsArc;
	}

	public void setDirectionsArc(List directionsArc) {
		this.directionsArc = directionsArc;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
