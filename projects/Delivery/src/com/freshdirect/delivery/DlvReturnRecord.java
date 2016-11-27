/*
 * Created on Jun 2, 2003
 */
package com.freshdirect.delivery;

/**
 * @author knadeem
 */
public class DlvReturnRecord {
	private final String saleId;
	private final boolean fullReturn;
	private final boolean alcoholReturn;

	public DlvReturnRecord(String saleId, boolean fullReturn, boolean alcoholReturn) {
		this.saleId = saleId;
		this.fullReturn = fullReturn;
		this.alcoholReturn = alcoholReturn;
	}

	public String getSaleId() {
		return this.saleId;
	}

	public boolean isFullReturn() {
		return this.fullReturn;
	}

	public boolean isAlocholReturn() {
		return this.alcoholReturn;
	}

}