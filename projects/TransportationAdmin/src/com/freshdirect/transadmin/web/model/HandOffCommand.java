package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.Date;

public class HandOffCommand implements Serializable {
	
	private String cutOff;
	
	private String serviceTimeScenario;
	
	private Date deliveryDate;
	
	public String getCutOff() {
		return cutOff;
	}

	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}

	public String getServiceTimeScenario() {
		return serviceTimeScenario;
	}

	public void setServiceTimeScenario(String serviceTimeScenario) {
		this.serviceTimeScenario = serviceTimeScenario;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
}
