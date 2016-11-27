package com.freshdirect.sap.bapi;

import java.util.List;

import com.freshdirect.sap.SapEBTOrderSettlementInfo;
import com.freshdirect.sap.SapOrderSettlementInfo;

/**
 * 
 * @author ksriram
 *
 */
public interface BapiSendSettlementByCommand extends BapiFunctionI {
	
	void sendOrders(List<SapOrderSettlementInfo> ebtOrders);

	public static final String TABLE_T_DELVY_CONF = "T_DELVY_CONF";
	
	public static final String FIELD_VDATU = "VDATU"; //Requested Delivery Date
	public static final String FIELD_BELNR = "BELNR"; //Accounting Number
	public static final String FIELD_VBELN = "VBELN"; //Sap Sales Order
	public static final String FIELD_WEBID = "WEBID"; //Web Sales Order
	public static final String FIELD_WRBTR = "WRBTR"; //Amount
	public static final String FIELD_WAERS = "WAERS"; //Currency
	public static final String FIELD_CCODE = "CCODE"; //Company code
	public static final String FIELD_TYPE = "TYPE";
	public static final String FIELD_ID = "ID";
	public static final String FIELD_NUMBER = "NUMBER";
	public static final String FIELD_MESSAGE = "MESSAGE";
	public static final String FIELD_LOG_NO = "LOG_NO";
	public static final String FIELD_LOG_MSG_NO = "LOG_MSG_NO";
	public static final String FIELD_MESSAGE_V1 = "MESSAGE_V1";
	public static final String FIELD_MESSAGE_V2 = "MESSAGE_V2";
	public static final String FIELD_MESSAGE_V3 = "MESSAGE_V3";
	public static final String FIELD_MESSAGE_V4 = "MESSAGE_V4";
	public static final String FIELD_I_BUKRS = "I_BUKRS";//Header paramater -Company code
}
