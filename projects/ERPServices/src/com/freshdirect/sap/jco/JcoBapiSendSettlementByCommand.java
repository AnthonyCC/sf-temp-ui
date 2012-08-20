package com.freshdirect.sap.jco;

import java.util.List;

import com.freshdirect.sap.SapOrderSettlementInfo;
import com.freshdirect.sap.bapi.BapiSendSettlementByCommand;
import com.sap.mw.jco.JCO;

/**
 * 
 * @author ksriram
 *
 */
public class JcoBapiSendSettlementByCommand extends JcoBapiFunction implements
		BapiSendSettlementByCommand {
	
	private JCO.Table orderRows;

	public JcoBapiSendSettlementByCommand() {
		super("ZBAPI_DELIVERY_CONF");
	}

	@Override
	public void sendOrders(List<SapOrderSettlementInfo> orders) {
		orderRows = this.function.getTableParameterList().getTable("T_DELVY_CONF");
		for(SapOrderSettlementInfo order : orders){
			orderRows.insertRow(1); 
			orderRows.setValue(order.getDeliveryDate(), FIELD_VDATU); //Requested Delivery Date
			orderRows.setValue(order.getAcctNumber(), FIELD_BELNR);   //Accounting Number
			orderRows.setValue(order.getSapSalesOrder(), FIELD_VBELN); //Sap Sales Order
			orderRows.setValue(order.getWebSalesOrder(), FIELD_WEBID); //Web Sales Order
			orderRows.setValue(order.getAmount(), FIELD_WRBTR); //Amount
			orderRows.setValue(order.getCurrency(), FIELD_WAERS); //Currency
			orderRows.setValue(order.getCompanyCode(), FIELD_CCODE)	;//Company Code		
			orderRows.nextRow();
		}
		this.function.getImportParameterList().setValue("",FIELD_I_BUKRS);//Non-mandatory field.
		
	}

}
