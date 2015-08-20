/**
 * 
 */
package com.freshdirect.sap.command;

import java.util.List;

import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.sap.SapOrderSettlementInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiInfo;
import com.freshdirect.sap.bapi.BapiSendPickEligibleOrders;
import com.freshdirect.sap.bapi.BapiSendSettlementByCommand;
import com.freshdirect.sap.ejb.SapException;

/**
 * @author tbalumuri
 *
 */
public class SapSendOrderPickEligibleCommand extends SapCommandSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7157406177767481687L;
	protected BapiInfo[] bapiInfos = null;
	protected List<SapOrderPickEligibleInfo> orders;	
		
	public BapiInfo[] getBapiInfos() {
		return bapiInfos;
	}

	public List<SapOrderPickEligibleInfo> getOrders() {
		return orders;
	}

	public SapSendOrderPickEligibleCommand(List<SapOrderPickEligibleInfo> orders) {
		super();
		this.orders = orders;
	}

	@Override
	public void execute() throws SapException {
		this.bapiInfos = null;
		BapiSendPickEligibleOrders bapi = BapiFactory.getInstance().getBapiSendPickEligibleOrders();
		bapi.sendOrders(this.orders);		
		this.invoke(bapi);
		bapiInfos = bapi.getInfos();

	}
	
	
	

}
