/*
 * Created on Mar 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.fdstore.customer;

import java.io.Serializable;

import com.freshdirect.customer.ErpCartonDetails;

/**
 * @author htai
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FDCartonDetail implements Serializable {

	private FDCartonInfo cartonInfo;
	private ErpCartonDetails cartonDetail;
	private FDCartLineI cartLine;

	public FDCartonDetail(FDCartonInfo cartonInfo, ErpCartonDetails cartonDetail, FDCartLineI cartLine) {
		this.cartonInfo = cartonInfo;
		this.cartonDetail = cartonDetail;
		this.cartLine = cartLine;
	}

	public ErpCartonDetails getCartonDetail() {
		return this.cartonDetail;
	}

	public FDCartonInfo getCartonInfo() {
		return this.cartonInfo;
	}

	public FDCartLineI getCartLine() {
		return cartLine;
	}

}
