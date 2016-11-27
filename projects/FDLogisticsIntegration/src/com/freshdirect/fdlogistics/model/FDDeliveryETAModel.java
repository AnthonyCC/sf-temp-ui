package com.freshdirect.fdlogistics.model;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.logistics.controller.data.response.DeliveryETA;

@SuppressWarnings("unused")
public class FDDeliveryETAModel implements Serializable {

	public FDDeliveryETAModel() {
		super();
	}

	public FDDeliveryETAModel(String orderId, Date startTime, Date endTime,
			boolean emailETAenabled, boolean manifestETAenabled,
			boolean smsETAenabled) {
		super();
		this.orderId = orderId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.emailETAenabled = emailETAenabled;
		this.manifestETAenabled = manifestETAenabled;
		this.smsETAenabled = smsETAenabled;
	}

	private static final long serialVersionUID = 4623100575013624975L;

	private String orderId;
	private Date startTime;
	private Date endTime;
	private boolean emailETAenabled;
	private boolean manifestETAenabled;
	private boolean smsETAenabled;

	public String getOrderId() {
		return orderId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public boolean isEmailETAenabled() {
		return emailETAenabled;
	}

	public boolean isManifestETAenabled() {
		return manifestETAenabled;
	}

	public boolean isSmsETAenabled() {
		return smsETAenabled;
	}
}
