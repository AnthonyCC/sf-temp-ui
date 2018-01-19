package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class RouteInfo extends Message{

	private String routeno;
	public String getRouteNo() {
		return routeno;
	}
	public void setRouteNo(String routeno) {
		this.routeno = routeno;
	}
}