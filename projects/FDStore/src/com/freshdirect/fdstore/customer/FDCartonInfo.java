/*
 * Created on Mar 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.fdstore.customer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.customer.ErpCartonInfo;

/**
 * @author htai
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FDCartonInfo implements Serializable {

	private ErpCartonInfo cartonInfo;
	private List cartonDetails;

	public FDCartonInfo(ErpCartonInfo cartonInfo, List cartonDetails) {
		this.cartonInfo = cartonInfo;
		this.cartonDetails = cartonDetails;
	}

	public List getCartonDetails() {
		return this.cartonDetails;
	}

	public ErpCartonInfo getCartonInfo() {
		return this.cartonInfo;
	}

	public FDCartonDetail containsCartonInfo(String orderlineNumber) {
		if (cartonDetails == null)
			return null;

		for (Iterator j = cartonDetails.iterator(); j.hasNext();) {
			FDCartonDetail detail = (FDCartonDetail) j.next();
			if (detail.getCartonDetail().getOrderLineNumber().equals(orderlineNumber))
				return detail;
		}
		return null;

	}

}
