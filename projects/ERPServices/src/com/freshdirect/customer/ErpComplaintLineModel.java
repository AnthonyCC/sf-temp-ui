/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import com.freshdirect.framework.core.ModelSupport;

/**
 * ErpComplaintLineModel class
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpComplaintLineModel extends ModelSupport {

	private String orderLineId;
	private String orderLineNumber;
	private double quantity;
	private double amount;
	private ErpComplaintReason reason;
	private EnumComplaintLineType type;
	private EnumComplaintLineMethod method;
	private boolean isOrderLineComplaint = false;

	public String getOrderLineId() {
		return this.orderLineId;
	}

	public void setOrderLineId(String orderlineId) {
		this.orderLineId = orderlineId;
	}

	public String getComplaintLineNumber() {
		return orderLineNumber;
	}
	public void setComplaintLineNumber(String s) {
		this.orderLineNumber = s;
	}

	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double d) {
		this.quantity = d;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double d) {
		this.amount = d;
	}

	public String getDepartmentCode() {
		return reason==null ? null : reason.getDepartmentCode();
	}
	
	public String getDepartmentName() {
		return reason==null ? null : reason.getDepartmentName();
	}

	public ErpComplaintReason getReason() {
		return reason;
	}
	public void setReason(ErpComplaintReason r) {
		this.reason = r;
	}

	public EnumComplaintLineType getType() {
		return type;
	}
	public void setType(EnumComplaintLineType t) {
		this.type = t;
		if (this.type.equals(EnumComplaintLineType.ORDER_LINE)) {
			this.setIsOrderLineComplaint();
		}
	}

	public EnumComplaintLineMethod getMethod() {
		return method;
	}
	public void setMethod(EnumComplaintLineMethod m) {
		this.method = m;
	}

	private void setIsOrderLineComplaint() {
		this.isOrderLineComplaint = true;
	}

	public boolean isValidComplaintLine() {

		boolean isValid = true;

		if (this.reason == null) {
			isValid = false;
		}
		if (isOrderLineComplaint && (Integer.parseInt(this.orderLineNumber) < 0)) {
			isValid = false;
		}
		if (this.method == null || this.type == null) {
			isValid = false;
		}
		return (isValid);
	}

}
