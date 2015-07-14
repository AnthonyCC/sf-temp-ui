/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore.depot;

import java.util.Collections;

import com.freshdirect.fdlogistics.model.FDDeliveryDepotModel;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.logistics.controller.data.PickupData;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class GetDepotLocationsTag extends AbstractGetterTag {

	private String depotCode;
	
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	protected Object getResult() throws FDResourceException {
		FDDeliveryDepotModel depot = FDDeliveryManager.getInstance().getDepot( this.depotCode );
		return (depot != null ? depot.getLocations() : Collections.EMPTY_LIST);
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}