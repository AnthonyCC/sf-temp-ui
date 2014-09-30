package com.freshdirect.delivery.model;

/**
 *
 * @author  kkanuganti
 * @version
 */
import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;

public class DeliveryETAModel extends ModelSupport {

	private static final long serialVersionUID = -5927431270004358008L;

	private String orderId;
	private Date startTime;
	private Date endTime;
	private boolean emailETAenabled;	
	private boolean manifestETAenabled;
	private boolean smsETAenabled;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public boolean isEmailETAenabled() {
		return emailETAenabled;
	}
	public void setEmailETAenabled(boolean emailETAenabled) {
		this.emailETAenabled = emailETAenabled;
	}
	public boolean isManifestETAenabled() {
		return manifestETAenabled;
	}
	public void setManifestETAenabled(boolean manifestETAenabled) {
		this.manifestETAenabled = manifestETAenabled;
	}
	public boolean isSmsETAenabled() {
		return smsETAenabled;
	}
	public void setSmsETAenabled(boolean smsETAenabled) {
		this.smsETAenabled = smsETAenabled;
	}
	@Override
	public String toString() {
		return "DeliveryETAModel [orderId=" + orderId + ", startTime="
				+ startTime + ", endTime=" + endTime + ", emailETAenabled="
				+ emailETAenabled + ", manifestETAenabled="
				+ manifestETAenabled + ", smsETAenabled=" + smsETAenabled + "]";
	}
	
	
}
