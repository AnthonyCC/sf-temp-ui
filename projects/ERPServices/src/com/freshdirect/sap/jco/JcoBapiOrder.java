/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.sap.jco;

import java.util.Date;

import com.freshdirect.sap.bapi.BapiOrder;
import com.sap.mw.jco.JCO;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
abstract class JcoBapiOrder extends JcoBapiFunction implements BapiOrder {

	private JCO.Table orderText;
	//private JCO.Table ccard;
	private JCO.Table extension;
	private JCO.Table orderConditionsIn;

	protected JCO.Table orderItemIn;
	protected JCO.Table schedule;
	private JCO.Table cfgsInst;
	private JCO.Table cfgs;
	private JCO.Table cfgsRef;

	protected JCO.Table orderPartner;

	/**
	 * NOTE: this.orderText and this.orderConditionsIn is lazy-initialized, to avoid problems w/ SO simulate.
	 */
	public JcoBapiOrder(String functionName) {
		super(functionName);

		// credit card
		//this.ccard = this.function.getTableParameterList().getTable("ORDER_CCARD");

		// extension
		this.extension = this.function.getTableParameterList().getTable("EXTENSIONIN");

		// orderlines, configs, schedule
		this.orderItemIn = this.function.getTableParameterList().getTable( this.getOrderItemInName() );
		this.schedule = this.function.getTableParameterList().getTable( this.getOrderScheduleInName() );
		this.cfgsInst = this.function.getTableParameterList().getTable( "ORDER_CFGS_INST" );
		this.cfgs = this.function.getTableParameterList().getTable( "ORDER_CFGS_VALUE" );
		this.cfgsRef = this.function.getTableParameterList().getTable( "ORDER_CFGS_REF" );
		
		// partners
		this.orderPartner = this.function.getTableParameterList().getTable( this.getOrderPartnersName() );
	}

	public void addOrderText(int posex, String textId, String text) {
		// order text
		if (this.orderText==null) {
			this.orderText = this.function.getTableParameterList().getTable("ORDER_TEXT");
		}

	    this.orderText.appendRow();
	    this.orderText.setValue(posex,	"ITM_NUMBER");
	    this.orderText.setValue(textId, "TEXT_ID");
	    this.orderText.setValue("E",	"LANGU");
	    this.orderText.setValue("*",	"FORMAT_COL");
	    this.orderText.setValue(text,	"TEXT_LINE");
	}


	/**
	 * Adds and RFC_BAPICCARD
	 */
	/*
	public void addCreditCard(String ccType, String ccNumber, Date ccValidTo, String ccName) {
		this.ccard.appendRow();
		this.ccard.setValue(ccType,		"CC_TYPE");
		this.ccard.setValue(ccNumber,	"CC_NUMBER");
		this.ccard.setValue(ccValidTo,	"CC_VALID_T");
		this.ccard.setValue(ccName,		"CC_NAME");
	}
	*/
	
	public void addExtension(String structure, String valuePart1) {
		this.extension.appendRow();
	    this.extension.setValue(structure,	"STRUCTURE");
	    this.extension.setValue(valuePart1,	"VALUEPART1");
	}


	public void addCondition(int posex, String condType, double condValue, String currency) {
		this.addCondition(posex, condType, condValue, currency, 0, null);	
	}
	
	public void addCondition(int posex, String condType, double condValue, String currency, double condPerUnit, String condUnit) {
		// conditions
		if (this.orderConditionsIn==null) {
			this.orderConditionsIn = this.function.getTableParameterList().getTable( this.getOrderConditionsInName() );
		}

		this.orderConditionsIn.appendRow();
		this.orderConditionsIn.setValue(posex,		"ITM_NUMBER");
		this.orderConditionsIn.setValue(condType,	"COND_TYPE");
		this.orderConditionsIn.setValue(condValue,	"COND_VALUE");
		this.orderConditionsIn.setValue(currency,	"CURRENCY");
		if (condUnit!=null) {
			this.orderConditionsIn.setValue(condPerUnit,	"COND_P_UNT");
			this.orderConditionsIn.setValue(condUnit,		"COND_UNIT");
		}
	}
	
	public void addPartner(String partnerRole, String partnerNumber) {
		this.orderPartner.appendRow();
		this.orderPartner.setValue(partnerRole,		"PARTN_ROLE");
		this.orderPartner.setValue(partnerNumber,	"PARTN_NUMB");
	}

	public void addPartner(String partnerRole, String partnerNumber, String name, String name2, String street, String city, String zip, String region, String country, String phoneNumber) {
		this.orderPartner.appendRow();
		
		// set up basic info
		this.orderPartner.setValue(partnerRole,		"PARTN_ROLE");
		this.orderPartner.setValue(partnerNumber,	"PARTN_NUMB");

		// set up address
		this.orderPartner.setValue("E",		"LANGU");
		this.orderPartner.setValue(name,	"NAME");
		this.orderPartner.setValue(name2,	"NAME_2");
		this.orderPartner.setValue(street,	"STREET");
		this.orderPartner.setValue(city,	"CITY");
		this.orderPartner.setValue(zip,		"POSTL_CODE");
		this.orderPartner.setValue(region.toUpperCase(),	"REGION");
		this.orderPartner.setValue(country.toUpperCase(),	"COUNTRY");
		this.orderPartner.setValue(phoneNumber,	"TELEPHONE");
	}

	/**
	 * Extended in subclasses.
	 */
	public void addOrderItemIn(OrderItemIn item) {
		this.orderItemIn.appendRow();

		this.orderItemIn.setValue(item.getMaterialNo(),	"MATERIAL");
		this.orderItemIn.setValue(item.getPurchNo(),	"PURCH_NO_S");
		this.orderItemIn.setValue(item.getPoItemNo(),	"PO_ITM_NO");
		if (item.getPoItemNoS() != 0) {
			this.orderItemIn.setValue(item.getPoItemNoS(), "POITM_NO_S");
		}
		this.orderItemIn.setValue(item.getItemNumber(),	"ITM_NUMBER");
		this.orderItemIn.setValue(item.getPurchDate(),	"PO_DAT_S");
		if (item.getReqQty() != null) {
			this.orderItemIn.setValue((int) (item.getReqQty().doubleValue() * 1000), "REQ_QTY");
		}
		this.orderItemIn.setValue(item.getSalesUnit(),	"SALES_UNIT");
		this.orderItemIn.setValue(item.getDeliveryGroup(), "DLV_GROUP");
		this.orderItemIn.setValue(item.getCustMat35(),	"CUST_MAT35");
	}


	public void addSchedule(ScheduleIn sched) {
		this.schedule.appendRow();
		this.schedule.setValue(sched.getItemNumber(), "ITM_NUMBER"); // association key with order_items_in
		this.schedule.setValue(sched.getReqQty(), "REQ_QTY");
		this.schedule.setValue(sched.getReqDate(), "REQ_DATE");
		setScheduleValue(sched.getTPDate(), "TP_DATE");
		setScheduleValue(sched.getMSDate(), "MS_DATE");
		setScheduleValue(sched.getLoadDate(), "LOAD_DATE");
		setScheduleValue(sched.getGIDate(), "GI_DATE");
	}
	
	private void setScheduleValue(Date date, String field) {
		if (date != null) {
			this.schedule.setValue(date, field);
		}
	}

	public void addCfgsValue(String vcName, String vcValue, String configID, String instID) {
		this.cfgs.appendRow();
		this.cfgs.setValue(configID,"CONFIG_ID");
		this.cfgs.setValue(instID,	"INST_ID");
		this.cfgs.setValue(vcName,	"CHARC");
		this.cfgs.setValue(vcValue,	"VALUE");
	}

	public void addCfgsRef(String rootId, String configId, String posex) {
		this.cfgsRef.appendRow();
		this.cfgsRef.setValue(rootId,	"ROOT_ID");
		this.cfgsRef.setValue(configId,	"CONFIG_ID");
		this.cfgsRef.setValue(posex,	"POSEX");
	}

	public void addCfgsInst(String configId, String instId, String objKey, double quantity) {
		this.cfgsInst.appendRow();
		this.cfgsInst.setValue(configId,	"CONFIG_ID");
		this.cfgsInst.setValue(instId,		"INST_ID");
		this.cfgsInst.setValue(objKey,		"OBJ_KEY");
		this.cfgsInst.setValue(quantity,	"QUANTITY");
		this.cfgsInst.setValue("MARA",		"OBJ_TYPE");
	}

	/**
	 * Template method.
	 * 
	 *	SO_CREATE:	"order_items_in"
	 *	SO_CHANGE: "order_item_in"
	 *	SO_SIMULATE: "order_items_in"
	 */
	protected abstract String getOrderItemInName();

	/**
	 * Template method.
	 *
	 *	SO_CREATE:	"order_schedules_in"
	 *	SO_CHANGE: "schedule_lines"
	 *	SO_SIMULATE: "order_schedule_in"
	 */
	protected abstract String getOrderScheduleInName();

	/**
	 * Template method.
	 *
	 *  SO_CREATE: "order_partners"
	 * 	 !!!
	 */
	protected abstract String getOrderPartnersName();

	/**
	 * Template method.
	 *
	 *  SO_CREATE: "ORDER_CONDITIONS_IN"
	 *	SO_CHANGE: "CONDITIONS_IN"
	 *	SO_SIMULATE: "ORDER_CONDITIONS_IN"
	 */
	protected abstract String getOrderConditionsInName();


}
