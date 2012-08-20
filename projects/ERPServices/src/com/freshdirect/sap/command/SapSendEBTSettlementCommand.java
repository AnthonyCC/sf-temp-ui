/**
 * 
 */
package com.freshdirect.sap.command;

import java.util.List;

import com.freshdirect.sap.SapOrderSettlementInfo;
import com.freshdirect.sap.bapi.BapiFactory;
import com.freshdirect.sap.bapi.BapiSendSettlementByCommand;
import com.freshdirect.sap.ejb.SapException;

/**
 * @author ksriram
 *
 */
public class SapSendEBTSettlementCommand extends SapSendSettlementCommand {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6737848413414200570L;

	public SapSendEBTSettlementCommand(List<SapOrderSettlementInfo> ebtOrders) {
		super(ebtOrders);
	}

	@Override
	public void execute() throws SapException {
		this.bapiInfos = null;
		BapiSendSettlementByCommand bapi = BapiFactory.getInstance().getBapiSendEBTSettlementSender();
		bapi.sendOrders(this.orders);		
		this.invoke(bapi);
		bapiInfos = bapi.getInfos();

	}
	
	
	

}
