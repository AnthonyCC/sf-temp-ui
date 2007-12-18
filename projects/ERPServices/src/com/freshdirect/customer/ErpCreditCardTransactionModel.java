package com.freshdirect.customer;

public abstract class ErpCreditCardTransactionModel extends ErpTransactionModel{
	
	private int returnCode;
	private String returnMessage;
	private String approvalCode;
	private String responseCode;
	private String responseMessage;
	private String sequenceNumber;
	private String addressMatch;
	private String zipMatch;
	private String processorResponseCode;
	private String processorAVSResult;
	private double amount;
	private double tax;
	

	/** Creates new ErpAbstractCredtiCardTransactionModel */
    public ErpCreditCardTransactionModel(EnumTransactionType transType) {
		super(transType);
    }
	
	public int getReturnCode(){
		return this.returnCode;
	}
	public void setReturnCode(int returnCode){
		this.returnCode = returnCode;
	}
	
	public String getReturnMessage(){
		return this.returnMessage;
	}
	public void setReturnMessage(String returnMessage){
		this.returnMessage = returnMessage;
	}
	
	public String getApprovalCode(){
		return this.approvalCode;
	}
	public void setApprovalCode(String approvalCode){
		this.approvalCode = approvalCode;
	}
	
	public String getResponseCode(){
		return this.responseCode;
	}
	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}
	
	public String getResponseMessage(){
		return this.responseMessage;
	}
	public void setResponseMessage(String responseMessage){
		this.responseMessage = responseMessage;
	}
	
	public String getSequenceNumber(){
		return this.sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber){
		this.sequenceNumber = sequenceNumber;
	}
	
	public String getAddressMatch(){
		return this.addressMatch;
	}
	public void setAddressMatch(String addressMatch){
		this.addressMatch = addressMatch;
	}
	
	public String getZipMatch(){
		return this.zipMatch;
	}
	public void setZipMatch(String zipMatch){
		this.zipMatch = zipMatch;
	}
	
	public String getProcessorResponseCode(){
		return this.processorResponseCode;
	}
	public void setProcessorResponseCode(String processorResponseCode){
		this.processorResponseCode = processorResponseCode;
	}
	
	public String getProcessorAVSResult(){
		return this.processorAVSResult;
	}
	public void setProcessorAVSResult(String processorAVSResult){
		this.processorAVSResult = processorAVSResult;
	}
	
	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount){
		this.amount = amount;
	}
	
	public double getTax(){
		return this.tax;
	}
	public void setTax(double tax){
		this.tax = tax;
	}
}
