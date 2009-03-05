package com.freshdirect.routing.model;

public class DrivingDirectionArc extends BaseModel implements IDrivingDirectionArc  {
	
	private java.lang.String instruction;

    private int time;

    private double distance;

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public java.lang.String getInstruction() {
		return instruction;
	}

	public void setInstruction(java.lang.String instruction) {
		this.instruction = instruction;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
