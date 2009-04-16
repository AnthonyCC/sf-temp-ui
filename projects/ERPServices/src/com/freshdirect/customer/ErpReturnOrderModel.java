/*
 * 
 * ErpReturnOrder.java 
 * Date: June 06/2002 06:09 PM
 */
package com.freshdirect.customer;

/**
 * 
 * @author knadeem
 * @version
 */

public class ErpReturnOrderModel extends ErpAbstractInvoiceModel {
	//Introduced for handling orders that contained or used delivery pass.
	private boolean containsDeliveryPass;
	private boolean dlvPassApplied;
	private String dlvPassId;
	private boolean restockingApplied;
	private double subTotal;
	
	public boolean isRestockingApplied() {
		return restockingApplied;
	}

	public void setRestockingApplied(boolean restockingApplied) {
		this.restockingApplied = restockingApplied;
	}

	public ErpReturnOrderModel() {
        super(EnumTransactionType.RETURN_ORDER);
    }

	public boolean isDlvPassApplied() {
		return dlvPassApplied;
	}

	public void setDlvPassApplied(boolean dlvPassApplied) {
		this.dlvPassApplied = dlvPassApplied;
	}

	public String getDeliveryPassId() {
		return dlvPassId;
	}

	public void setDeliveryPassId(String dlvPassId) {
		this.dlvPassId = dlvPassId;
	}

	public boolean isContainsDeliveryPass() {
		return containsDeliveryPass;
	}

	public void setContainsDeliveryPass(boolean containsDeliveryPass) {
		this.containsDeliveryPass = containsDeliveryPass;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

}
