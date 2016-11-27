package com.freshdirect.sap.jco;

import java.util.List;

import com.freshdirect.sap.SapOrderSettlementInfo;
import com.freshdirect.sap.bapi.BapiSendSettlementByCommand;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 * 
 * @author ksriram
 *
 */
public class JcoBapiSendSettlementByCommand extends JcoBapiFunction implements
		BapiSendSettlementByCommand {
	
	private JCoTable orderRows;

	public JcoBapiSendSettlementByCommand() throws JCoException
	{
		super("ZBAPI_DELIVERY_CONF");
	}

	@Override
	public void sendOrders(List<SapOrderSettlementInfo> orders)
	{
		orderRows = this.function.getTableParameterList().getTable("T_DELVY_CONF");
		
		for(SapOrderSettlementInfo order : orders)
		{
			orderRows.insertRow(1); 
			orderRows.setValue(FIELD_VDATU, order.getDeliveryDate()); //Requested Delivery Date
			orderRows.setValue(FIELD_BELNR, order.getAcctNumber());   //Accounting Number
			orderRows.setValue(FIELD_VBELN, order.getSapSalesOrder()); //Sap Sales Order
			orderRows.setValue(FIELD_WEBID, order.getWebSalesOrder()); //Web Sales Order
			orderRows.setValue(FIELD_WRBTR, order.getAmount()); //Amount
			orderRows.setValue(FIELD_WAERS, order.getCurrency()); //Currency
			orderRows.setValue(FIELD_CCODE, order.getCompanyCode())	;//Company Code		
			orderRows.nextRow();
		}
		this.function.getImportParameterList().setValue(FIELD_I_BUKRS, "");//Non-mandatory field.
		
	}

}
