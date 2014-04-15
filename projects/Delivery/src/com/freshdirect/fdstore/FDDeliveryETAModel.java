package com.freshdirect.fdstore;

import java.io.Serializable;
import java.util.Date;

import com.freshdirect.delivery.model.DeliveryETAModel;

@SuppressWarnings("unused")
public class FDDeliveryETAModel implements Serializable {

	private static final long serialVersionUID = 4623100575013624975L;

	private String orderId;
	private Date startTime;
	private Date endTime;
	private boolean emailETAenabled;
	private boolean manifestETAenabled;
	private boolean smsETAenabled;

	private DeliveryETAModel dlvETAModel;

	public FDDeliveryETAModel(DeliveryETAModel model) {
		this.dlvETAModel = model;
	}

	public String getOrderId() {
		return this.dlvETAModel.getOrderId();
	}

	public Date getStartTime() {
		return this.dlvETAModel.getStartTime();
	}

	public Date getEndTime() {
		return this.dlvETAModel.getEndTime();
	}

	public boolean isEmailETAenabled() {
		return this.dlvETAModel.isEmailETAenabled();
	}

	public boolean isManifestETAenabled() {
		return this.dlvETAModel.isManifestETAenabled();
	}

	public void setManifestETAenabled(boolean manifestETAenabled) {
		this.manifestETAenabled = manifestETAenabled;
	}

	public boolean isSmsETAenabled() {
		return this.dlvETAModel.isSmsETAenabled();
	}
}
