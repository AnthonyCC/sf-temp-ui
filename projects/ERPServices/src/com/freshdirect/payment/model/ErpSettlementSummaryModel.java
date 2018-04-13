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
	private List settlementTrxns = new ArrayList(); //list of transactions
	
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
	
	private String affiliateAccountId = null;
	private long totalGrossCredit = 0;
	private long totalGrossDebit = 0;
	private long totalTransactionFeeCredit = 0;
	private long totalTransactionFeeDebit = 0;
	private String settlementSource = null;
	
	private String isLocked = "";
	private String status = "";
	
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
		
	/**
	 * all argument Constructor
	 */
	public ErpSettlementSummaryModel(Date processPeriodStart, Date processPeriodEnd, Date batchDate, String batchNumber, Date processDate, Date depositDate,
									  double netSalesAmount, int numberOfAdjustments, double adjustmentAmount, Date settlementFileDate,
									  String affiliateAccountId, long totalGrossCredit, long totalGrossDebit, long totalTransactionFeeCredit,
									  long totalTrasactionFeeDebit){
		this(processPeriodStart, processPeriodEnd, batchDate, batchNumber, processDate, depositDate,
									  netSalesAmount, numberOfAdjustments, adjustmentAmount, settlementFileDate);
		this.affiliateAccountId = affiliateAccountId;
		this.totalGrossCredit = totalGrossCredit;
		this.totalGrossDebit = totalGrossDebit;
		this.totalTransactionFeeCredit = totalTransactionFeeCredit;
		this.totalTransactionFeeDebit = totalTrasactionFeeDebit;
	}
	
	
	/**
	 * all argument Constructor
	 */
	public ErpSettlementSummaryModel(Date processPeriodStart, Date processPeriodEnd, Date batchDate, String batchNumber, Date processDate, Date depositDate,
									  double netSalesAmount, int numberOfAdjustments, double adjustmentAmount, Date settlementFileDate,
									  String affiliateAccountId, long totalGrossCredit, long totalGrossDebit, long totalTransactionFeeCredit,
									  long totalTrasactionFeeDebit, String isLocked, String status){
		this(processPeriodStart, processPeriodEnd, batchDate, batchNumber, processDate, depositDate,
									  netSalesAmount, numberOfAdjustments, adjustmentAmount, settlementFileDate);
		this.isLocked = isLocked;
		this.status = status;
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
	
	public List<ErpSummaryDetailModel> getSummaryDetails(){
		return this.summaryDetails;
	}
	public void setSummaryDetails(List summaryDetails){
		this.summaryDetails = summaryDetails;
	}
	
	public void addSummaryDetail(ErpSummaryDetailModel detail){
		this.summaryDetails.add(detail);
	}
	
	public List<ErpSettlementSummaryModel> getInvoices(){
		return this.invoices;
	}
	public void setInvoices(List invoices){
		this.invoices = invoices;
	}
	public void addInvoice(ErpSettlementInvoiceModel invoice){
		this.invoices.add(invoice);
	}
	
	public double getNetSalesAmount() {
		return netSalesAmount;
	}

	public void setNetSalesAmount(double netSalesAmount) {
		this.netSalesAmount = netSalesAmount;
	}

	public String getAffiliateAccountId() {
		return affiliateAccountId;
	}

	public void setAffiliateAccountId(String affiliateAccountId) {
		this.affiliateAccountId = affiliateAccountId;
	}

	public long getTotalGrossCredit() {
		return totalGrossCredit;
	}

	public void setTotalGrossCredit(long totalGrossCredit) {
		this.totalGrossCredit = totalGrossCredit;
	}

	public long getTotalGrossDebit() {
		return totalGrossDebit;
	}

	public void setTotalGrossDebit(long totalGrossDebit) {
		this.totalGrossDebit = totalGrossDebit;
	}

	public long getTotalTransactionFeeCredit() {
		return totalTransactionFeeCredit;
	}

	public void setTotalTransactionFeeCredit(long totalTransactionFeeCredit) {
		this.totalTransactionFeeCredit = totalTransactionFeeCredit;
	}

	public long getTotalTransactionFeeDebit() {
		return totalTransactionFeeDebit;
	}

	public void setTotalTransactionFeeDebit(long totalTransactionFeeDebit) {
		this.totalTransactionFeeDebit = totalTransactionFeeDebit;
	}

	public String getSettlementSource() {
		return settlementSource;
	}

	public void setSettlementSource(String settlementSource) {
		this.settlementSource = settlementSource;
	}
	
	public List<ErpSettlementTransactionModel> getSettlementTrxns() {
		return settlementTrxns;
	}

	public void setSettlementTrxns(List settlementTrxns) {
		this.settlementTrxns = settlementTrxns;
	}
	
	public String getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String toString() {
		return super.toString() + " " + System.identityHashCode(this); 
	}
}
