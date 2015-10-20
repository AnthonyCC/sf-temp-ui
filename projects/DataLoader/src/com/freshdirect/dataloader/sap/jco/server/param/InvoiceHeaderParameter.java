/**
 * 
 */
package com.freshdirect.dataloader.sap.jco.server.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kkanuganti 
 */
public class InvoiceHeaderParameter implements Serializable
{
	private static final long serialVersionUID = -2968864407761305134L;

	private double headerDiscount;
	private String creditMemoSalesOrderNo;
	private String webOrderNo;
	private String salesOrderNo;
	private double invoiceTotal;
	private int regularCartonCnt;
	private int alcoholCartonCnt;
	private String creditWebReferenceNo;
	private String invoiceNo;
	private String stopSequence;
	private int freezerCartonCnt;
	private double invoiceSubTotal;
	private double creditAmount;
	private List<InvoiceEntryParameter> entries;
	private List<InvoiceCreditParameter> creditEntries = new ArrayList<InvoiceCreditParameter>();
	private String deliveryDate;
	private String billingType;
	private String truckNumber;
	private double bottleDepositAmount;
	private double invoiceTax;
	private String creditMemoNo;
	
	/**
	 * @return the headerDiscount
	 */
	public double getHeaderDiscount()
	{
		return headerDiscount;
	}
	/**
	 * @param headerDiscount the headerDiscount to set
	 */
	public void setHeaderDiscount(double headerDiscount)
	{
		this.headerDiscount = headerDiscount;
	}
	/**
	 * @return the creditMemoSalesOrderNo
	 */
	public String getCreditMemoSalesOrderNo()
	{
		return creditMemoSalesOrderNo;
	}
	/**
	 * @param creditMemoSalesOrderNo the creditMemoSalesOrderNo to set
	 */
	public void setCreditMemoSalesOrderNo(String creditMemoSalesOrderNo)
	{
		this.creditMemoSalesOrderNo = creditMemoSalesOrderNo;
	}
	/**
	 * @return the webOrderNo
	 */
	public String getWebOrderNo()
	{
		return webOrderNo;
	}
	/**
	 * @param webOrderNo the webOrderNo to set
	 */
	public void setWebOrderNo(String webOrderNo)
	{
		this.webOrderNo = webOrderNo;
	}
	/**
	 * @return the salesOrderNo
	 */
	public String getSalesOrderNo()
	{
		return salesOrderNo;
	}
	/**
	 * @param salesOrderNo the salesOrderNo to set
	 */
	public void setSalesOrderNo(String salesOrderNo)
	{
		this.salesOrderNo = salesOrderNo;
	}
	/**
	 * @return the invoiceTotal
	 */
	public double getInvoiceTotal()
	{
		return invoiceTotal;
	}
	/**
	 * @param invoiceTotal the invoiceTotal to set
	 */
	public void setInvoiceTotal(double invoiceTotal)
	{
		this.invoiceTotal = invoiceTotal;
	}
	/**
	 * @return the regularCartonCnt
	 */
	public int getRegularCartonCnt()
	{
		return regularCartonCnt;
	}
	/**
	 * @param regularCartonCnt the regularCartonCnt to set
	 */
	public void setRegularCartonCnt(int regularCartonCnt)
	{
		this.regularCartonCnt = regularCartonCnt;
	}
	/**
	 * @return the alcoholCartonCnt
	 */
	public int getAlcoholCartonCnt()
	{
		return alcoholCartonCnt;
	}
	/**
	 * @param alcoholCartonCnt the alcoholCartonCnt to set
	 */
	public void setAlcoholCartonCnt(int alcoholCartonCnt)
	{
		this.alcoholCartonCnt = alcoholCartonCnt;
	}
	/**
	 * @return the creditWebReferenceNo
	 */
	public String getCreditWebReferenceNo()
	{
		return creditWebReferenceNo;
	}
	/**
	 * @param creditWebReferenceNo the creditWebReferenceNo to set
	 */
	public void setCreditWebReferenceNo(String creditWebReferenceNo)
	{
		this.creditWebReferenceNo = creditWebReferenceNo;
	}
	/**
	 * @return the invoiceNo
	 */
	public String getInvoiceNo()
	{
		return invoiceNo;
	}
	/**
	 * @param invoiceNo the invoiceNo to set
	 */
	public void setInvoiceNo(String invoiceNo)
	{
		this.invoiceNo = invoiceNo;
	}
	/**
	 * @return the stopSequence
	 */
	public String getStopSequence()
	{
		return stopSequence;
	}
	/**
	 * @param stopSequence the stopSequence to set
	 */
	public void setStopSequence(String stopSequence)
	{
		this.stopSequence = stopSequence;
	}
	/**
	 * @return the freezerCartonCnt
	 */
	public int getFreezerCartonCnt()
	{
		return freezerCartonCnt;
	}
	/**
	 * @param freezerCartonCnt the freezerCartonCnt to set
	 */
	public void setFreezerCartonCnt(int freezerCartonCnt)
	{
		this.freezerCartonCnt = freezerCartonCnt;
	}
	/**
	 * @return the invoiceSubTotal
	 */
	public double getInvoiceSubTotal()
	{
		return invoiceSubTotal;
	}
	/**
	 * @param invoiceSubTotal the invoiceSubTotal to set
	 */
	public void setInvoiceSubTotal(double invoiceSubTotal)
	{
		this.invoiceSubTotal = invoiceSubTotal;
	}
	/**
	 * @return the creditAmount
	 */
	public double getCreditAmount()
	{
		return creditAmount;
	}
	/**
	 * @param creditAmount the creditAmount to set
	 */
	public void setCreditAmount(double creditAmount)
	{
		this.creditAmount = creditAmount;
	}
	/**
	 * @return the entries
	 */
	public List<InvoiceEntryParameter> getEntries()
	{
		return entries;
	}
	/**
	 * @param entries the entries to set
	 */
	public void setEntries(List<InvoiceEntryParameter> entries)
	{
		this.entries = entries;
	}
	/**
	 * @return the deliveryDate
	 */
	public String getDeliveryDate()
	{
		return deliveryDate;
	}
	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(String deliveryDate)
	{
		this.deliveryDate = deliveryDate;
	}
	/**
	 * @return the billingType
	 */
	public String getBillingType()
	{
		return billingType;
	}
	/**
	 * @param billingType the billingType to set
	 */
	public void setBillingType(String billingType)
	{
		this.billingType = billingType;
	}
	/**
	 * @return the truckNumber
	 */
	public String getTruckNumber()
	{
		return truckNumber;
	}
	/**
	 * @param truckNumber the truckNumber to set
	 */
	public void setTruckNumber(String truckNumber)
	{
		this.truckNumber = truckNumber;
	}
	/**
	 * @return the bottleDepositAmount
	 */
	public double getBottleDepositAmount()
	{
		return bottleDepositAmount;
	}
	/**
	 * @param bottleDepositAmount the bottleDepositAmount to set
	 */
	public void setBottleDepositAmount(double bottleDepositAmount)
	{
		this.bottleDepositAmount = bottleDepositAmount;
	}
	/**
	 * @return the invoiceTax
	 */
	public double getInvoiceTax()
	{
		return invoiceTax;
	}
	/**
	 * @param invoiceTax the invoiceTax to set
	 */
	public void setInvoiceTax(double invoiceTax)
	{
		this.invoiceTax = invoiceTax;
	}
	/**
	 * @return the creditMemoNo
	 */
	public String getCreditMemoNo()
	{
		return creditMemoNo;
	}
	/**
	 * @param creditMemoNo the creditMemoNo to set
	 */
	public void setCreditMemoNo(String creditMemoNo)
	{
		this.creditMemoNo = creditMemoNo;
	}
	public List<InvoiceCreditParameter> getCreditEntries() {
		return creditEntries;
	}
	public void setCreditEntries(List<InvoiceCreditParameter> creditEntries) {
		this.creditEntries = creditEntries;
	}
}
