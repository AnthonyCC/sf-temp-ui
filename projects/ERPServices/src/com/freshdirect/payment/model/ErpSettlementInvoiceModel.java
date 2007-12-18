/*
 * 
 * ErpSettlementInvoiceModel.java
 * Date: 06/04/02 2:51 PM
 */

package com.freshdirect.payment.model;

/**
 * 
 * @author knadeem
 * @version
 */
 
import java.util.Date;

import com.freshdirect.framework.core.*;

public class ErpSettlementInvoiceModel extends ModelSupport {
	
	private Date invoiceDate;
	private double invoiceAmount;
	private String invoiceNumber;
	private String description;
	
	public ErpSettlementInvoiceModel(){
		super();
	}
	
	public ErpSettlementInvoiceModel(Date invoiceDate, double invoiceAmount, String invoiceNumber, String description){
		this.invoiceDate = invoiceDate;
		this.invoiceAmount = invoiceAmount;
		this.invoiceNumber = invoiceNumber;
		this.description = description;
	}
	
	public Date getInvoiceDate(){
		return this.invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate){
		this.invoiceDate = invoiceDate;
	}
	
	public double getInvoiceAmount(){
		return this.invoiceAmount;
	}
	public void setInvoiceAmount(double invoiceAmount){
		this.invoiceAmount = invoiceAmount;
	}
	
	public String getInvoiceNumber(){
		return this.invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber){
		this.invoiceNumber = invoiceNumber;
	}
	
	public String getDescription(){
		return this.description;
	}
	public void setDescription(String description){
		this.description = description;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof ErpSettlementInvoiceModel)){
			return false;
		}
		
		ErpSettlementInvoiceModel comp = (ErpSettlementInvoiceModel)o;
		
		return invoiceDate.equals(comp.invoiceDate) && invoiceAmount == comp.invoiceAmount 
			&& invoiceNumber.equals(comp.invoiceNumber) && description.equals(comp.description);
	}

}
