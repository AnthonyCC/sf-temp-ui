package com.freshdirect.routing.model;

public class ServiceTime implements IServiceTime {

	private double orderServiceTime;
	private double stopServiceTime;
	@Override
	public double getOrderServiceTime() {
		return orderServiceTime;
	}

	@Override
	public void setOrderServiceTime(double orderServiceTime) {
		this.orderServiceTime = orderServiceTime;
	}

	@Override
	public double getStopServiceTime() {
		return stopServiceTime;
	}

	@Override
	public void setStopServiceTime(double stopServiceTime) {
		this.stopServiceTime = stopServiceTime;
	}

	@Override
	public String toString() {
		return "ServiceTime [orderServiceTime=" + orderServiceTime
				+ ", stopServiceTime=" + stopServiceTime + "]";
	}

}