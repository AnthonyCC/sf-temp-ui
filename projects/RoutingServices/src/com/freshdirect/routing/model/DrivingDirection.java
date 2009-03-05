package com.freshdirect.routing.model;

import java.util.List;

public class DrivingDirection extends BaseModel implements IDrivingDirection {
	
	private int time;

	private double distance;
	 
	private List pathDirections;

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public List getPathDirections() {
		return pathDirections;
	}

	public void setPathDirections(List pathDirections) {
		this.pathDirections = pathDirections;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
