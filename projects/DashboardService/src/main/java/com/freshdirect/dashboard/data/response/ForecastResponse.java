package com.freshdirect.dashboard.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.dashboard.model.ForecastModel;

public class ForecastResponse {
	
	private List<ForecastModel> order;
	
	private List<ForecastModel> capacity;
	
	private List<ForecastModel> projection;
	
	private List<ForecastModel> bounce;
	
	private List<ForecastModel> roll;
	
	public ForecastResponse() {
		order = new ArrayList<ForecastModel>();
		capacity = new ArrayList<ForecastModel>();
		projection = new ArrayList<ForecastModel>();
		bounce = new ArrayList<ForecastModel>();
		roll = new ArrayList<ForecastModel>();
	}

	public List<ForecastModel> getOrder() {
		return order;
	}

	public void setOrder(List<ForecastModel> order) {
		this.order = order;
	}

	public List<ForecastModel> getCapacity() {
		return capacity;
	}

	public void setCapacity(List<ForecastModel> capacity) {
		this.capacity = capacity;
	}

	public List<ForecastModel> getProjection() {
		return projection;
	}

	public void setProjection(List<ForecastModel> projection) {
		this.projection = projection;
	}

	public List<ForecastModel> getBounce() {
		return bounce;
	}

	public void setBounce(List<ForecastModel> bounce) {
		this.bounce = bounce;
	}

	public List<ForecastModel> getRoll() {
		return roll;
	}

	public void setRoll(List<ForecastModel> roll) {
		this.roll = roll;
	}	
	
	
}
