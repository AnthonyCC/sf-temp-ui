/*
 * ErpAdjustmentModel.java
 *
 * Created on May 6, 2002, 1:16 PM
 */

package com.freshdirect.customer;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.util.Date;

public class ErpAdjustmentModel extends ErpPaymentModel{
	
    private java.util.Date batchDate;
    private String batchNumber;
    private java.util.Date depositDate;
    private java.util.Date processDate;
    private double adjustmentAmount;
    private String referenceNumber;
    private String cardholderNumber;
    private java.util.Date transactionDate;
    private String adjustmentDescription;
    private String sequenceNumber;
    private double netSalesAmount;

	public ErpAdjustmentModel(){
		super(EnumTransactionType.ADJUSTMENT);
	}
	
	public Date getBatchDate(){
		return this.batchDate;
	}
	public void setBatchDate(Date batchDate){
		this.batchDate = batchDate;
	}
	
	public String getBatchNumber(){
		return this.batchNumber;
	}
	public void setBatchNumber(String batchNumber){
		this.batchNumber = batchNumber;
	}
	
	public Date getDepositDate(){
		return this.depositDate;
	}
	public void setDepositDate(Date depositDate){
		this.depositDate = depositDate;
	}
	
	public Date getProcessDate(){
		return this.processDate;
	}
	public void setProcessDate(Date processDate){
		this.processDate = processDate;
	}
	
	public double getAdjustmentAmount(){
		return this.adjustmentAmount;
	}
	public void setAdjustmentAmount(double adjustmentAmount){
		this.adjustmentAmount = adjustmentAmount;
	}
	
	public String getReferenceNumber(){
		return this.referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber){
		this.referenceNumber = referenceNumber;
	}
	
	public String getCardholderNumber(){
		return this.cardholderNumber;
	}
	public void setCardholderNumber(String cardholderNumber){
		this.cardholderNumber = cardholderNumber;
	}
	
	public Date getTransactionDate(){
		return this.transactionDate;
	}
	public void setTransactionDate(Date transactionDate){
		this.transactionDate = transactionDate;
	}
	
	public String getAdjustmentDescription(){
		return this.adjustmentDescription;
	}
	public void setAdjustmentDescription(String adjustmentDescription){
		this.adjustmentDescription = adjustmentDescription;
	}
	
	public String getSequenceNumber(){
		return this.sequenceNumber;
	}
	public void setSequenceNumber(String sequenceNumber){
		this.sequenceNumber = sequenceNumber;
	}
	
	public double getNetSalesAmount(){
		return this.netSalesAmount;
	}
	public void setNetSalesAmount(double netSalesAmount){
		this.netSalesAmount = netSalesAmount;
	}
	
}
