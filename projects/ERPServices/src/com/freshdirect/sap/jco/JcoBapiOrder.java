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
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
abstract class JcoBapiOrder extends JcoBapiFunction implements BapiOrder {

	private JCoTable orderText;
	private JCoTable extension;
	private JCoTable orderConditionsIn;

	protected JCoTable orderItemIn;
	protected JCoTable schedule;
	private JCoTable cfgsInst;
	private JCoTable cfgs;
	private JCoTable cfgsRef;

	protected JCoTable orderPartner;

	/**
	 * NOTE: this.orderText and this.orderConditionsIn is lazy-initialized, to avoid problems w/ SO simulate.
	 * @throws JCoException 
	 */
	public JcoBapiOrder(String functionName) throws JCoException {
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
	    this.orderText.setValue("ITM_NUMBER", posex);
	    this.orderText.setValue("TEXT_ID", textId);
	    this.orderText.setValue("LANGU", "E");
	    this.orderText.setValue("FORMAT_COL", "*");
	    this.orderText.setValue("TEXT_LINE", text);
	}


	/**
	 * Adds and RFC_BAPICCARD
	 */
	/*
	public void addCreditCard(String ccType, String ccNumber, Date ccValidTo, String ccName) {
		this.ccard.appendRow();
		this.ccard.setValue("CC_TYPE", ccType);
		this.ccard.setValue("CC_NUMBER", ccNumber);
		this.ccard.setValue("CC_VALID_T", ccValidTo);
		this.ccard.setValue("CC_NAME", ccName);
	}
	*/
	
	public void addExtension(String structure, String valuePart1) {
		this.extension.appendRow();
	    this.extension.setValue("STRUCTURE", structure);
	    this.extension.setValue("VALUEPART1", valuePart1);
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
		this.orderConditionsIn.setValue("ITM_NUMBER", posex);
		this.orderConditionsIn.setValue("COND_TYPE", condType);
		this.orderConditionsIn.setValue("COND_VALUE", condValue);
		this.orderConditionsIn.setValue("CURRENCY", currency);
		if (condUnit!=null) {
			this.orderConditionsIn.setValue("COND_P_UNT", condPerUnit);
			this.orderConditionsIn.setValue("COND_UNIT", condUnit);
		}
	}
	
	public void addPartner(String partnerRole, String partnerNumber) {
		this.orderPartner.appendRow();
		this.orderPartner.setValue("PARTN_ROLE", partnerRole);
		this.orderPartner.setValue("PARTN_NUMB", partnerNumber);
	}

	public void addPartner(String partnerRole, String partnerNumber, String name, String name2, String street, String city, String zip, String region, String country, String phoneNumber) {
		this.orderPartner.appendRow();
		
		// set up basic info
		this.orderPartner.setValue("PARTN_ROLE", partnerRole);
		this.orderPartner.setValue("PARTN_NUMB", partnerNumber);

		// set up address
		this.orderPartner.setValue("LANGU", "E");
		this.orderPartner.setValue("NAME", name);
		this.orderPartner.setValue("NAME_2", name2);
		this.orderPartner.setValue("STREET", street);
		this.orderPartner.setValue("CITY", city);
		this.orderPartner.setValue("POSTL_CODE", zip);
		this.orderPartner.setValue("REGION", region.toUpperCase());
		this.orderPartner.setValue("COUNTRY", country.toUpperCase());
		this.orderPartner.setValue("TELEPHONE", phoneNumber);
	}

	/**
	 * Extended in subclasses.
	 */
	public void addOrderItemIn(OrderItemIn item) {
		this.orderItemIn.appendRow();

		this.orderItemIn.setValue("MATERIAL", item.getMaterialNo());
		this.orderItemIn.setValue("PURCH_NO_S", item.getPurchNo());
		this.orderItemIn.setValue("PO_ITM_NO", item.getPoItemNo());
		if (item.getPoItemNoS() != 0) {
			this.orderItemIn.setValue("POITM_NO_S", item.getPoItemNoS());
		}
		this.orderItemIn.setValue("ITM_NUMBER", item.getItemNumber());
		this.orderItemIn.setValue("PO_DAT_S", item.getPurchDate());
		if (item.getReqQty() != null) {
			this.orderItemIn.setValue("REQ_QTY", (int) (item.getReqQty().doubleValue() * 1000));
		}
		this.orderItemIn.setValue("SALES_UNIT", item.getSalesUnit());
		this.orderItemIn.setValue("DLV_GROUP", item.getDeliveryGroup());
		this.orderItemIn.setValue("CUST_MAT35", item.getCustMat35());
		this.orderItemIn.setValue("PLANT", item.getPickingPlantId());
	}


	public void addSchedule(ScheduleIn sched)
	{
		this.schedule.appendRow();
		this.schedule.setValue("ITM_NUMBER", sched.getItemNumber()); // association key with order_items_in
		this.schedule.setValue("REQ_QTY", sched.getReqQty());
		this.schedule.setValue("REQ_DATE", sched.getReqDate());
		
		setScheduleValue("TP_DATE", sched.getTPDate());
		setScheduleValue("MS_DATE", sched.getMSDate());
		setScheduleValue("LOAD_DATE", sched.getLoadDate());
		setScheduleValue("GI_DATE", sched.getGIDate());
	}
	
	private void setScheduleValue(String field, Date date)
	{
		if (date != null)
		{
			this.schedule.setValue(field, date);
		}
	}

	public void addCfgsValue(String vcName, String vcValue, String configID, String instID)
	{
		this.cfgs.appendRow();
		this.cfgs.setValue("CONFIG_ID", configID);
		this.cfgs.setValue("INST_ID", instID);
		this.cfgs.setValue("CHARC", vcName);
		this.cfgs.setValue("VALUE", vcValue);
	}

	public void addCfgsRef(String rootId, String configId, String posex)
	{
		this.cfgsRef.appendRow();
		this.cfgsRef.setValue("ROOT_ID", rootId);
		this.cfgsRef.setValue("CONFIG_ID", configId);
		this.cfgsRef.setValue("POSEX", posex);
	}

	public void addCfgsInst(String configId, String instId, String objKey, double quantity)
	{
		this.cfgsInst.appendRow();
		this.cfgsInst.setValue("CONFIG_ID", configId);
		this.cfgsInst.setValue("INST_ID", instId);
		this.cfgsInst.setValue("OBJ_KEY", objKey);
		this.cfgsInst.setValue("QUANTITY", quantity);
		this.cfgsInst.setValue("OBJ_TYPE", "MARA");
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
