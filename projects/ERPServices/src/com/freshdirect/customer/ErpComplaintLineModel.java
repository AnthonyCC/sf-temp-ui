/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.customer;

import org.apache.log4j.Logger;

import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * ErpComplaintLineModel class
 *
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-model
 */
public class ErpComplaintLineModel extends ModelSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance(ErpComplaintLineModel.class);
	
	private String orderLineId;
	private String orderLineNumber;
	private double quantity;
	private double amount;
	private ErpComplaintReason reason;
	private EnumComplaintLineType type;
	private EnumComplaintLineMethod method;
	private boolean isOrderLineComplaint = false;
	private String cartonNumber;
	
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

	public String getCartonNumber() {
		return cartonNumber;
	}

	public void setCartonNumber(String cartonNumber) {
		this.cartonNumber = cartonNumber;
	}

	public boolean isValidComplaintLine() {

		boolean isValid = true;

		if (this.reason == null) {
			isValid = false;
			LOGGER.warn("ComplaintLine reason is missing. "+orderLineId);	
		}
		if (isOrderLineComplaint && (Integer.parseInt(this.orderLineNumber) < 0)) {
			isValid = false;
			LOGGER.warn("ComplaintLine - OrderLineNumer is invalid. "+orderLineId);
		}
		if (this.method == null || this.type == null) {
			isValid = false;
			if(this.method == null){
				LOGGER.warn("ComplaintLine method is missing. "+orderLineId);
			}
			if(this.type == null){
				LOGGER.warn("ComplaintLine type is missing. "+orderLineId);
			}
		}
		return (isValid);
	}
	@Override
	public void setId(String id) {
		if (id != null)
			super.setId(id);
	}
}
