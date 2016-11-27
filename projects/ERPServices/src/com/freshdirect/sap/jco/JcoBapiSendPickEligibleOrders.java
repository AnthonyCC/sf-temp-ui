package com.freshdirect.sap.jco;

import java.util.List;

import com.freshdirect.sap.SapOrderPickEligibleInfo;
import com.freshdirect.sap.bapi.BapiSendPickEligibleOrders;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 * 
 * @author tbalumuri
 *
 */
public class JcoBapiSendPickEligibleOrders extends JcoBapiFunction implements
		BapiSendPickEligibleOrders {
	
	private JCoTable orderRows;

	public JcoBapiSendPickEligibleOrders() throws JCoException
	{
		super("ZFDX_SORDER_CUTOFF_TIME");
	}

	@Override
	public void sendOrders(List<SapOrderPickEligibleInfo> orders)
	{
		orderRows = this.function.getTableParameterList().getTable("T_FDX_SORD_CUTOFF");
		
		for(SapOrderPickEligibleInfo order : orders)
		{
			orderRows.insertRow(1); 
			orderRows.setValue(FIELD_VDATU, order.getDeliveryDate()); //Requested Delivery Date
			orderRows.setValue(FIELD_VBELN, order.getSapSalesOrder()); //Sap Sales Order
			orderRows.setValue(FIELD_WEBID, order.getWebSalesOrder()); //Web Sales Order
			orderRows.nextRow();
		}
	
	}

}
