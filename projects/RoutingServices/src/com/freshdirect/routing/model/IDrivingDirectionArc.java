package com.freshdirect.routing.model;

public interface IDrivingDirectionArc {
	
	double getDistance();

	void setDistance(double distance);

	java.lang.String getInstruction();

	void setInstruction(java.lang.String instruction);

	int getTime();

	void setTime(int time);
}
