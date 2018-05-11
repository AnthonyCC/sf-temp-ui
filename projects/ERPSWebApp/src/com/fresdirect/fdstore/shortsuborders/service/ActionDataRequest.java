package com.fresdirect.fdstore.shortsuborders.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActionDataRequest implements Serializable{


	private List<String> orderList=new ArrayList<String>();


	public List<String> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<String> orderList) {
		this.orderList = orderList;
	}

}
