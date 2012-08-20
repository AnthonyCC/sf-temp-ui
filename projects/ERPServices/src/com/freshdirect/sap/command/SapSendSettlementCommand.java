package com.freshdirect.sap.command;

import java.util.List;

import com.freshdirect.sap.SapOrderSettlementInfo;
import com.freshdirect.sap.bapi.BapiInfo;

public abstract class SapSendSettlementCommand extends SapCommandSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7157406177767481687L;
	protected BapiInfo[] bapiInfos = null;
	protected List<SapOrderSettlementInfo> orders;	
	
	
	public SapSendSettlementCommand(List<SapOrderSettlementInfo> orders) {
		super();
		this.orders = orders;
	}
	
	public BapiInfo[] getBapiInfos() {
		return bapiInfos;
	}

	public List<SapOrderSettlementInfo> getOrders() {
		return orders;
	}
}
