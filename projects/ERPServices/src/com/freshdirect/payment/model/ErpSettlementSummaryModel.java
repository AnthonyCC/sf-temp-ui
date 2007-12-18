package com.freshdirect.payment.model;


import java.util.*;

import com.freshdirect.framework.core.*;


/**
 *
 * @author  knadeem
 * @version
 */


public class ErpSettlementSummaryModel extends ModelSupport{
	
	private List summaryDetails = new ArrayList(); //list of detail records
	private List invoices = new ArrayList(); //list of invoices
	
	private Date processPeriodStart = null;
	private Date processPeriodEnd = null;
	private Date batchDate = null;
	private String batchNumber = null;
	private Date processDate = null;
	private Date depositDate = null;
	private double netSalesAmount;
	private int numberOfAdjustments;
	private double adjustmentAmount;
	private Date settlementFileDate = null;
	
	/**
	 * no argument Constructor
	 */
	public ErpSettlementSummaryModel () {
		super();
	}
	
	/**
	 * all argument Constructor
	 */
	public ErpSettlementSummaryModel(Date processPeriodStart, Date processPeriodEnd, Date batchDate, String batchNumber, Date processDate, Date depositDate,
									  double netSalesAmount, int numberOfAdjustments, double adjustmentAmount, Date settlementFileDate){
		this.processPeriodStart = processPeriodStart;
		this.processPeriodEnd = processPeriodEnd;
		this.batchDate = batchDate;
		this.batchNumber = batchNumber;
		this.processDate = processDate;
		this.depositDate = depositDate;
		this.netSalesAmount = netSalesAmount;
		this.numberOfAdjustments = numberOfAdjustments;
		this.adjustmentAmount = adjustmentAmount;
		this.settlementFileDate = settlementFileDate;
	}
	
	public Date getProcessPeriodStart(){
		return this.processPeriodStart;
	}
	public void setProcessPeriodStart(Date processPeriodStart){
		this.processPeriodStart = processPeriodStart;
	}
	
	public Date getProcessPeriodEnd(){
		return this.processPeriodEnd;
	}
	public void setProcessPeriodEnd(Date processPeriodEnd){
		this.processPeriodEnd = processPeriodEnd;
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
	
	public Date getProcessDate(){
		return this.processDate;
	}
	public void setProcessDate(Date processDate){
		this.processDate = processDate;
	}
	
	public Date getDepositDate(){
		return this.depositDate;
	}
	public void setDepositDate(Date depositDate){
		this.depositDate = depositDate;
	}
	
	public double getNetSaleAmount(){
		return this.netSalesAmount;
	}
	public void setNetSaleAmount(double netSalesAmount){
		this.netSalesAmount = netSalesAmount;
	}
	
	public int getNumberOfAdjustments(){
		return this.numberOfAdjustments;
	}
	public void setNumberOfAdjustments(int numberOfAdjustments){
		this.numberOfAdjustments = numberOfAdjustments;
	}
	
	public double getAdjustmentAmount(){
		return this.adjustmentAmount;
	}
	public void setAdjustmentAmount(double adjustmentAmount){
		this.adjustmentAmount = adjustmentAmount;
	}
	
	public Date getSettlementFileDate(){
		return this.settlementFileDate;
	}
	public void setSettlementFileDate(Date settlementFileDate){
		this.settlementFileDate = settlementFileDate;
	}
	
	public List getSummaryDetails(){
		return this.summaryDetails;
	}
	public void setSummaryDetails(List summaryDetails){
		this.summaryDetails = summaryDetails;
	}
	
	public void addSummaryDetail(ErpSummaryDetailModel detail){
		this.summaryDetails.add(detail);
	}
	
	public List getInvoices(){
		return this.invoices;
	}
	public void setInvoices(List invoices){
		this.invoices = invoices;
	}
	public void addInvoice(ErpSettlementInvoiceModel invoice){
		this.invoices.add(invoice);
	}
	
	public String toString() {
		return super.toString() + " " + System.identityHashCode(this); 
	}
}
