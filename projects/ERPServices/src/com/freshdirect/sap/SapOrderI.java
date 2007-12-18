/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.sap;

import java.io.Serializable;
import java.util.Date;
import com.freshdirect.common.pricing.CreditMemo;
import com.freshdirect.common.pricing.Discount;
import java.util.List;

public interface SapOrderI extends Serializable {

	/**@link aggregationByValue*/
	/*#SapOrderLineI orderLines;*/

	/**
	 * Get the date the order was requested for.
	 *
	 * @return requested date 
	 */
	public Date getRequestedDate();

	/**
	 * Get an order line by ordinal number.
	 *
	 * @param ordinal number of order line
	 *
	 * @return SapOrderLineI
	 *
	 * @throws IndexOutOfBoundsException
	 */
	public SapOrderLineI getOrderLine(int num);

	/**
	 * Get the number of order lines.
	 *
	 * @return number of SapOrderLines
	 */
	public int numberOfOrderLines();

	/**
	 * Get the web order ID for this order.
	 */
	public String getWebOrderNumber();

	/**
	 * Get the customer associated with this order
	 *
	 * @return Customer object
	 */
	public SapCustomerI getCustomer();

	public String getCompanyName();

	public Date getDeliveryStartTime();

	public Date getDeliveryEndTime();

	public String getDeliveryZone();

	public String getDeliveryRegionId();

	public Date getCutoffTime();

	public String getDeliveryModel();

	public SapChargeLineI[] getChargeLines();

	/**
	 * Get the credit memos to apply for this order.
	 *
	 * @return array of CreditMemo objects
	 */
	public CreditMemo[] getCreditMemos();

	/**
	 * Get the optional order-level discount to apply.
	 *
	 * @return Discount object, null if none
	 */
	public Discount getDiscount();

	public boolean isFreeOrder();

	public boolean isRecipeOrder();
	
	//
	// various text fields
	//

	public String getMarketingMessage();

	public String getCustomerServiceMessage();

	public String getDeliveryInstructions();

	public String getBillingRef();
	
	//
	//customer rating
	//

	public String getCustomerStarRating();

	public String getChefsTableMembership();

	public String getProfileSegment();
	
	public String getGlCode();
	
	public List getDiscounts();

}

