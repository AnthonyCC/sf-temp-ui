package com.freshdirect.customer;

import com.freshdirect.framework.core.*;

/**
 * @stereotype fd-model
 */
public class ErpInvoiceLineModel extends ModelSupport implements ErpInvoiceLineI {
    
	private static final long	serialVersionUID	= 3812963637982993467L;
	
	private double price;
    private double customizationPrice;
    private double quantity;
    private double weight;
    private double taxValue;
	private double depositValue;
    private String orderLineNumber;
    private String materialNumber;
    private double actualCost;
    private double actualDiscountAmount;
	
	public ErpInvoiceLineModel(){
		super();
	}
	
	public ErpInvoiceLineModel(ErpInvoiceLineModel invoiceLine){
		this();
		this.price = invoiceLine.getPrice();		
		this.customizationPrice = invoiceLine.getCustomizationPrice();
		this.quantity = invoiceLine.getQuantity();
		this.weight = invoiceLine.getWeight();
		this.taxValue = invoiceLine.getTaxValue();
		this.depositValue = invoiceLine.getDepositValue();
		this.orderLineNumber = invoiceLine.getOrderLineNumber();
		this.materialNumber = invoiceLine.getMaterialNumber();
		this.actualCost = invoiceLine.getActualCost();
		this.actualDiscountAmount=invoiceLine.getActualDiscountAmount();
	}
	
    public double getPrice(){ 
    	return price; 
    }
    
    public double getActualPrice(){ 
    	return price+this.actualDiscountAmount; 
    }
    
    public void setPrice(double price){ 
    	this.price = price; 
    }
    
    public double getCustomizationPrice(){
    	return this.customizationPrice;
    }
    
    public void setCustomizationPrice(double customizationPrice){
    	this.customizationPrice = customizationPrice;
    }

    public double getQuantity(){ 
    	return quantity; 
    }
    public void setQuantity(double quantity){ 
    	this.quantity = quantity; 
    }

	public double getWeight(){
		return this.weight;
	}
	public void setWeight(double weight){
		this.weight = weight;
	}
	
    public double getTaxValue(){ 
    	return taxValue; 
    }
    public void setTaxValue(double taxValue){ 
    	this.taxValue = taxValue; 
    }
    
    public double getDepositValue() {
    	return this.depositValue;
    }

	public void setDepositValue(double depositValue) {
		this.depositValue = depositValue;    
	}

    public String getOrderLineNumber(){ 
    	return orderLineNumber; 
    }
    
    public void setOrderLineNumber(String orderLineNumber){ 
    	this.orderLineNumber = orderLineNumber; 
    }

    public String getMaterialNumber(){ 
    	return materialNumber; 
    }
    public void setMaterialNumber(String materialNumber){ 
    	this.materialNumber = materialNumber; 
    }
    
    public double getActualCost(){
    	return this.actualCost;
    }
    
    public void setActualCost(double actualCost) {
    	this.actualCost = actualCost;
    }

	public double getActualDiscountAmount() {
		return actualDiscountAmount;
	}

	public void setActualDiscountAmount(double actualDiscountAmount) {
		this.actualDiscountAmount = actualDiscountAmount;
	}
}
