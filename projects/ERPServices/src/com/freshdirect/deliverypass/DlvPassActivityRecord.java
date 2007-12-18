package com.freshdirect.deliverypass;
/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/
import com.freshdirect.customer.ErpActivityRecord;

public class DlvPassActivityRecord extends ErpActivityRecord {
	private String deliveryPassId;
	private String changeOrderId;
	private String reason;
	
	/**
	 * @return Returns the changeOrderId.
	 */
	public String getChangeOrderId() {
		return changeOrderId;
	}

	/**
	 * @param changeOrderId The changeOrderId to set.
	 */
	public void setChangeOrderId(String changeOrderId) {
		this.changeOrderId = changeOrderId;
	}

	/**
	 * @return Returns the deliveryPassId.
	 */
	public String getDeliveryPassId() {
		return deliveryPassId;
	}

	/**
	 * @param deliveryPassId The deliveryPassId to set.
	 */
	public void setDeliveryPassId(String deliveryPassId) {
		this.deliveryPassId = deliveryPassId;
	}

	/**
	 * @return Returns the reason.
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason The reason to set.
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	public DlvPassActivityRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

}
