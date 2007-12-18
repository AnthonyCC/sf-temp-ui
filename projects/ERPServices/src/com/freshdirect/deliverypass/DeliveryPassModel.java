
package com.freshdirect.deliverypass;
/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/
import java.util.Date;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class DeliveryPassModel extends ModelSupport {
	private String customerId;
	private DeliveryPassType type;
	private String description;
	private String purchaseOrderId;
	private Date purchaseDate;
	private double amount;
	private int totalNoOfDlvs;
	private int remainingDlvs;
	private Date expDate;
	private int usageCount = 0;
	private int noOfCredits = 0;
	private Date orgExpDate;
	
	private EnumDlvPassStatus status;

	public DeliveryPassModel() {
		super();
	}
	/*
	 * This constructor is called only when pass is inserted into the DB.
	 */
	public DeliveryPassModel(PrimaryKey pk, String customerId, DeliveryPassType type,
							String description, String purchaseOrderId,
							Date purchaseDate, double amount,
							int totalNoOfDlvs, int remainingDlvs,
							Date expDate, int usageCnt, EnumDlvPassStatus status) {
		super();
		this.setPK(pk);
		this.setCustomerId(customerId);
		this.setType(type);
		this.setDescription(description);
		this.setPurchaseOrderId(purchaseOrderId);
		this.setPurchaseDate(purchaseDate);
		this.setAmount(amount);
		this.setTotalNoOfDlvs(totalNoOfDlvs);
		this.setRemainingDlvs(remainingDlvs);
		if(expDate != null){
			//Null if BSGS pass.
			//On delivery pass purchase both original exp date and exp date is the same.
			this.setOrgExpirationDate(expDate);
			this.setExpirationDate(expDate);	
		}
		
		this.setUsageCount(usageCnt);
		this.setStatus(status);
	}
	/*
	 * This contructor is called when Delivery pass is loaded up from the Database.
	 */
	public DeliveryPassModel(PrimaryKey pk, String customerId, DeliveryPassType type,
			String description, String purchaseOrderId,
			Date purchaseDate, double amount,
			int totalNoOfDlvs, int remainingDlvs, Date orgExpDate,
			Date expDate, int usageCnt, int numCredits, EnumDlvPassStatus status) {
			super();
			this.setPK(pk);
			this.setCustomerId(customerId);
			this.setType(type);
			this.setDescription(description);
			this.setPurchaseOrderId(purchaseOrderId);
			this.setPurchaseDate(purchaseDate);
			this.setAmount(amount);
			this.setTotalNoOfDlvs(totalNoOfDlvs);
			this.setRemainingDlvs(remainingDlvs);
			this.setOrgExpirationDate(orgExpDate);			
			this.setExpirationDate(expDate);
			this.setUsageCount(usageCnt);
			this.setNoOfCredits(numCredits);
			this.setStatus(status);
}
	
	/**
	 * @return Returns the amount.
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return Returns the purchaseOrderId.
	 */
	public String getPurchaseOrderId() {
		return purchaseOrderId;
	}

	/**
	 * @param purchaseOrderId The purchaseOrderId to set.
	 */
	public void setPurchaseOrderId(String purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	/**
	 * @param amount The amount to set.
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return Returns the customerId.
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId The customerId to set.
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the expDate.
	 */
	public Date getExpirationDate() {
		return expDate;
	}

	/**
	 * @param expDate The expDate to set.
	 */
	public void setExpirationDate(Date expDate) {
		this.expDate = expDate;
	}

	/**
	 * @return Returns the purchaseDate.
	 */
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate The purchaseDate to set.
	 */
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return Returns the remainingDlvs.
	 */
	public int getRemainingDlvs() {
		return remainingDlvs;
	}

	/**
	 * @param remainingDlvs The remainingDlvs to set.
	 */
	public void setRemainingDlvs(int remainingDlvs) {
		this.remainingDlvs = remainingDlvs;
	}

	/**
	 * @return Returns the status.
	 */
	public EnumDlvPassStatus getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(EnumDlvPassStatus status) {
		this.status = status;
	}

	/**
	 * @return Returns the totalNoOfDlvs.
	 */
	public int getTotalNoOfDlvs() {
		return totalNoOfDlvs;
	}

	/**
	 * @param totalNoOfDlvs The totalNoOfDlvs to set.
	 */
	public void setTotalNoOfDlvs(int totalNoOfDlvs) {
		this.totalNoOfDlvs = totalNoOfDlvs;
	}

	/**
	 * @return Returns the type.
	 */
	public DeliveryPassType getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(DeliveryPassType type) {
		this.type = type;
	}
	

	/**
	 * @return Returns the totalNoOfDlvs.
	 */
	public int getUsageCount() {
		return usageCount;
	}

	/**
	 * @param totalNoOfDlvs The totalNoOfDlvs to set.
	 */
	public void setUsageCount(int count) {
		this.usageCount = count;
	}

	public int getNoOfCredits() {
		return noOfCredits;
	}

	public void setNoOfCredits(int noOfCredits) {
		this.noOfCredits = noOfCredits;
	}

	public Date getOrgExpirationDate() {
		return orgExpDate;
	}

	public void setOrgExpirationDate(Date orgExpDate) {
		this.orgExpDate = orgExpDate;
	}

}
