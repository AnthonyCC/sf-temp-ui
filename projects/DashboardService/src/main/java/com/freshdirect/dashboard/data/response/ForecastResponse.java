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
	
	private List<ForecastModel> day1;
	
	private List<ForecastModel> day2;
	
	public ForecastResponse() {
		order = new ArrayList<ForecastModel>();
		capacity = new ArrayList<ForecastModel>();
		projection = new ArrayList<ForecastModel>();
		bounce = new ArrayList<ForecastModel>();
		roll = new ArrayList<ForecastModel>();
		day1 = new ArrayList<ForecastModel>();
		day2 = new ArrayList<ForecastModel>();
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

	public List<ForecastModel> getDay1() {
		return day1;
	}

	public void setDay1(List<ForecastModel> day1) {
		this.day1 = day1;
	}

	public List<ForecastModel> getDay2() {
		return day2;
	}

	public void setDay2(List<ForecastModel> day2) {
		this.day2 = day2;
	}	
	
	
}
