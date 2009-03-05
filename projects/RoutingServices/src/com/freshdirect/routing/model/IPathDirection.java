package com.freshdirect.routing.model;

import java.util.List;

public interface IPathDirection {
	
	List getDirectionsArc();

	void setDirectionsArc(List directionsArc);

	double getDistance();

	void setDistance(double distance);

	int getTime();

	void setTime(int time);
}
