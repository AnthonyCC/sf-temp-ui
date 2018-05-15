package com.freshdirect.fdstore.ecoupon.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;

public class CouponCart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4156256160889986627L;

	private FDCouponCustomer couponCustomer;
	private List<ErpOrderLineModel> orderLines;
	private String orderId;
	private EnumCouponTransactionType tranType;

	/**
	 * @return the couponCustomer
	 */
	public FDCouponCustomer getCouponCustomer() {
		return couponCustomer;
	}

	/**
	 * @return the orderLines
	 */
	public List<ErpOrderLineModel> getOrderLines() {
		return orderLines;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	public CouponCart(FDCouponCustomer couponCustomer, List<ErpOrderLineModel> orderLines,
			EnumCouponTransactionType tranType) {
		super();
		this.couponCustomer = couponCustomer;
		this.orderLines = orderLines;
		this.tranType = tranType;
	}

	@JsonCreator
	public CouponCart(@JsonProperty("couponCustomer") FDCouponCustomer couponCustomer,
			@JsonProperty("orderLines") List<ErpOrderLineModel> orderLines, @JsonProperty("orderId") String orderId,
			@JsonProperty("tranType") EnumCouponTransactionType tranType) {
		super();
		this.couponCustomer = couponCustomer;
		this.orderLines = orderLines;
		this.orderId = orderId;
		this.tranType = tranType;
	}

	public CouponCart(FDCouponCustomer couponCustomer, String orderId, EnumCouponTransactionType tranType) {
		super();
		this.couponCustomer = couponCustomer;
		this.orderId = orderId;
		this.tranType = tranType;
	}

	/**
	 * @return the tranType
	 */
	public EnumCouponTransactionType getTranType() {
		return tranType;
	}

}
