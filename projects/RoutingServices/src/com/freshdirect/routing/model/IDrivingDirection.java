package com.freshdirect.routing.model;

import java.util.List;

public interface IDrivingDirection {
	
	double getDistance();

	void setDistance(double distance);

	List getPathDirections();

	void setPathDirections(List pathDirections);

	int getTime();

	void setTime(int time);

}
