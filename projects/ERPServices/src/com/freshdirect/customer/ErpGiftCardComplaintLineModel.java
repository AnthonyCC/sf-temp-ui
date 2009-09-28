package com.freshdirect.customer;

public class ErpGiftCardComplaintLineModel extends ErpComplaintLineModel {

	private String certificateNumber;
	private String givexNumber;
	private String templateId;	
	
	/**
	 * @return the certificateNumber
	 */
	public String getCertificateNumber() {
		return certificateNumber;
	}
	/**
	 * @param certificateNumber the certificateNumber to set
	 */
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	/**
	 * @return the givexNumber
	 */
	public String getGivexNumber() {
		return givexNumber;
	}
	/**
	 * @param givexNumber the givexNumber to set
	 */
	public void setGivexNumber(String givexNumber) {
		this.givexNumber = givexNumber;
	}
	
	public String getMaskedGivexNum() {
    	int numDisplayedDigits = Math.min(givexNumber.length(), 5); 
    	int numMaskedDigits = 10 - numDisplayedDigits;
    	return  "xxxxxxxx".substring(0, numMaskedDigits) + givexNumber.substring(givexNumber.length()-numDisplayedDigits);  
		//int pmLen = this.accountNumber.length()-4;
		//return "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".substring(0,pmLen) + this.accountNumber.substring(pmLen);
	}
	
	public ErpGiftCardComplaintLineModel(
			ErpComplaintLineModel erpComplaintLineModel) {
		super();
		super.setAmount(erpComplaintLineModel.getAmount());
		super.setOrderLineId(erpComplaintLineModel.getOrderLineId());
		super.setMethod(erpComplaintLineModel.getMethod());
		super.setCartonNumber(erpComplaintLineModel.getCartonNumber());
		super.setComplaintLineNumber(erpComplaintLineModel.getComplaintLineNumber());
		super.setId(erpComplaintLineModel.getId());
		super.setQuantity(erpComplaintLineModel.getQuantity());
		super.setType(erpComplaintLineModel.getType());
		super.setReason(erpComplaintLineModel.getReason());
	}
	/**
	 * @return the templateId
	 */
	public String getTemplateId() {
		return templateId;
	}
	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
