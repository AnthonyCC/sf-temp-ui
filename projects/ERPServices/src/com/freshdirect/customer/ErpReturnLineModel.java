/*
 * 
 * ErpReturnLineModel.java
 * Date: Oct 15, 2002 Time: 1:06:23 PM
 */
package com.freshdirect.customer;

import com.freshdirect.framework.core.ModelSupport;

/**
 * 
 * @author knadeem
 */

public class ErpReturnLineModel extends ModelSupport implements ErpReturnLineI {
	
	private String lineNumber;
	private double quantity;
	private boolean restockingOnly;
	
	public ErpReturnLineModel(){
		super();
	}

	public String getLineNumber(){
		return this.lineNumber;
	}
	
	public void setLineNumber(String lineNumber){
		this.lineNumber = lineNumber;
	}

	
	public double getQuantity(){
		return this.quantity;
	}
	
	public void setQuantity(double quantity){
		this.quantity = quantity;
	}
	
	public void setRestockingOnly(boolean restockingOnly){
		this.restockingOnly = restockingOnly;
	}
	
	public boolean isRestockingOnly(){
		return this.restockingOnly;
	}

}
