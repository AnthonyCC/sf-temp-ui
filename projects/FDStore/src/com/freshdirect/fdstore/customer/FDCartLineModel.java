/*
 * $Workfile:FDCartLineModel.java$
 *
 * $Date:6/30/2003 5:28:13 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.content.ProductRef;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.sap.PosexUtil;

/**
 *
 * @version    $Revision:16$
 * @author     $Author:Kashif Nadeem$
 * @stereotype fd-model
 */
public class FDCartLineModel extends AbstractCartLine {

	private EnumEventSource source;
	
	public FDCartLineModel(ErpOrderLineModel orderLine) {
		this(orderLine, null, null, null);
	}

	public FDCartLineModel(
		ErpOrderLineModel orderLine,
		ErpInvoiceLineI firstInvoiceLine,
		ErpInvoiceLineI lastInvoiceLine,
		ErpReturnLineModel returnLine) {
		super(orderLine, firstInvoiceLine, lastInvoiceLine, returnLine);
	}

	public FDCartLineModel(FDSku sku, ProductRef productRef, FDConfigurableI configuration) {
		super(sku, productRef, configuration);
		this.orderLine.setCartlineId(ID_GENERATOR.getNextId());
	}
	
	public FDCartLineModel(FDSku sku, ProductRef productRef, FDConfigurableI configuration, String cartlineId, String recipeSourceId, boolean requestNotification) {
		super(sku, productRef, configuration);
		this.orderLine.setCartlineId(cartlineId);
		this.orderLine.setRecipeSourceId(recipeSourceId);
		this.orderLine.setRequestNotification(requestNotification);
	}

	public List buildErpOrderLines(int baseLineNumber) throws FDResourceException, FDInvalidConfigurationException {
		this.refreshConfiguration();
		ErpOrderLineModel ol = (ErpOrderLineModel) this.orderLine.deepCopy();

		ol.setOrderLineNumber(PosexUtil.getPosex(baseLineNumber));

		List ols = new ArrayList(1);
		ols.add(ol);
		return ols;
	}

	public int getErpOrderLineSize() {
		return 1;
	}

	public FDCartLineI createCopy() {
		FDCartLineModel newLine = new FDCartLineModel(this.getSku(), this
				.getProductRef(), this.getConfiguration());
		newLine.setRecipeSourceId(this.getRecipeSourceId());
		newLine.setRequestNotification(this.isRequestNotification());
		return newLine;
	}

	/**
	 *  Set the source of the event.
	 *  
	 *  @param source the part of the site this event was generated from.
	 */
	public void setSource(EnumEventSource source) {
		this.source = source;
	}
	
	/**
	 *  Get the source of the event.
	 *  
	 *  @return the part of the site this event was generated from.
	 */
	public EnumEventSource getSource() {
		return source;
	}

}
