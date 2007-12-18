package com.freshdirect.dataloader.payment.reconciliation.paymentech;

public class FIN0011DataRecord extends DFRDataRecord {
	
	private EnumPaymentechSubCategory subCategory;
	private int feeSchedule;
	private String interchangeQualification;
	private String feeTypeDescription;
	private String actionType;
	private int unitQuantity;
	private double unitFee;
	private double percentageRate;
	private double totalCharge;

	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public int getFeeSchedule() {
		return feeSchedule;
	}
	public void setFeeSchedule(int feeSchedule) {
		this.feeSchedule = feeSchedule;
	}
	public String getFeeTypeDescription() {
		return feeTypeDescription;
	}
	public void setFeeTypeDescription(String feeTypeDescription) {
		this.feeTypeDescription = feeTypeDescription;
	}
	public String getInterchangeQualification() {
		return interchangeQualification;
	}
	public void setInterchangeQualification(String interchangeQualification) {
		this.interchangeQualification = interchangeQualification;
	}
	public double getPercentageRate() {
		return percentageRate;
	}
	public void setPercentageRate(double percentageRate) {
		this.percentageRate = percentageRate;
	}
	public EnumPaymentechSubCategory getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(EnumPaymentechSubCategory subCategory) {
		this.subCategory = subCategory;
	}
	public double getTotalCharge() {
		return totalCharge;
	}
	public void setTotalCharge(double totalCharge) {
		this.totalCharge = totalCharge;
	}
	public double getUnitFee() {
		return unitFee;
	}
	public void setUnitFee(double unitFee) {
		this.unitFee = unitFee;
	}
	public int getUnitQuantity() {
		return unitQuantity;
	}
	public void setUnitQuantity(int unitQuantity) {
		this.unitQuantity = unitQuantity;
	}
}
