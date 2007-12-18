/*
 * $Workfile:FDModifyCartModel.java$
 *
 * $Date:5/22/03 7:19:08 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.Iterator;

import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;

/**
 *
 * @version	$Revision:10$
 * @author	 $Author:Viktor Szathmary$
 * @stereotype fd-model
 */
public class FDModifyCartModel extends FDCartModel {

	private final FDOrderAdapter originalOrder;

	/**
	 * Default constructor.
	 */
	public FDModifyCartModel(FDOrderAdapter originalOrder) {
		this.setCharges(originalOrder.getCharges());
		this.originalOrder = originalOrder;

		// populate orderlines from orig.order
		for (Iterator i = originalOrder.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI origLine = (FDCartLineI) i.next();
			FDCartLineI cartLine = new FDModifyCartLineModel(origLine);
			this.addOrderLine(cartLine);
		}

		// initialize from original order
		this.setDeliveryAddress(originalOrder.getCorrectedDeliveryAddress());
		this.setPaymentMethod(originalOrder.getPaymentMethod());

		this.setDeliveryReservation(originalOrder.getDeliveryReservation());

		// !!! partially reconstruct the original zoneInfo (we don't need the full state, as it will be set later)
		DlvZoneInfoModel zoneInfo = new DlvZoneInfoModel(originalOrder.getDeliveryZone(), null, null, EnumZipCheckResponses.DELIVER,false);
		this.setZoneInfo(zoneInfo);

		this.setCustomerServiceMessage(originalOrder.getCustomerServiceMessage());
		this.setMarketingMessage(originalOrder.getMarketingMessage());

	}

	public FDOrderAdapter getOriginalOrder() {
		return this.originalOrder;
	}

	public String getOriginalReservationId() {
		return this.originalOrder.getDeliveryReservationId();
	}

	public boolean isDlvPassAlreadyApplied() {
		if(this.originalOrder.getDeliveryPassId() !=  null){
			//Delivery pass was already applied in this order either during create
			//or during last modification. So preserve the applied pass.
			return true;
		}
		return false;
	}
}