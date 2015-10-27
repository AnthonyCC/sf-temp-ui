package com.freshdirect.sap.jco;

import com.freshdirect.sap.bapi.BapiSalesOrderChange;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class JcoBapiSalesOrderChange extends JcoBapiOrder implements BapiSalesOrderChange {

	private JCoTable orderItemInX;
	private JCoTable schedulesInX;

	public JcoBapiSalesOrderChange() throws JCoException {
		
		super("Z_BAPI_SALESORDER_CHANGE");
		// X (change) structures
		this.function.getImportParameterList().getStructure("ORDER_HEADER_INX").setValue("UPDATEFLAG", "U");
		this.orderItemInX = this.function.getTableParameterList().getTable("ORDER_ITEM_INX");
		this.schedulesInX = this.function.getTableParameterList().getTable("SCHEDULE_LINESX");
	}

	public void setOrderHeaderIn(OrderHeaderIn hdr) {
		JCoStructure orderHeaderIn = this.function.getImportParameterList().getStructure("ORDER_HEADER_IN");
		JCoStructure orderHeaderInX = this.function.getImportParameterList().getStructure("ORDER_HEADER_INX");

		orderHeaderIn.setValue("SALES_ORG", hdr.getSalesOrg());
		orderHeaderIn.setValue("DISTR_CHAN", hdr.getDistrChan());
		orderHeaderIn.setValue("DIVISION", hdr.getDivision());
		
		// Collective number (Delivery zone/depot number)
		orderHeaderIn.setValue("COLLECT_NO", hdr.getCollectiveNo());
		orderHeaderInX.setValue("COLLECT_NO", "X");

		// Customer group 1 (Delivery Model)
		orderHeaderIn.setValue("CUST_GRP1", hdr.getCustGrp1());
		orderHeaderInX.setValue("CUST_GRP1", "X");

		// Customer's or vendor's internal reference
		orderHeaderIn.setValue("REF_1", hdr.getRef1());
		orderHeaderInX.setValue("REF_1", "X");
		
		orderHeaderInX.setValue("REF_1_S", "X");
		orderHeaderIn.setValue("REF_1_S", hdr.getRef1S());

		orderHeaderIn.setValue("PO_SUPPLEM", hdr.getPoSupplement());
		orderHeaderInX.setValue("PO_SUPPLEM", "X");
		orderHeaderIn.setValue("DUN_DATE", hdr.getDunDate());
		orderHeaderInX.setValue("DUN_DATE", "X");
		orderHeaderIn.setValue("NAME", hdr.getName());
		orderHeaderInX.setValue("NAME", "X");
		orderHeaderIn.setValue("PURCH_NO_S", hdr.getPurchNoS());
		orderHeaderInX.setValue("PURCH_NO_S", "X");

		// requested date
		orderHeaderIn.setValue("REQ_DATE_H", hdr.getRequestedDate());
		orderHeaderInX.setValue("REQ_DATE_H", "X");
		orderHeaderIn.setValue("PO_DAT_S", hdr.getRequestedDate());
		orderHeaderInX.setValue("PO_DAT_S", "X");

		//customer segmentation/rating
		orderHeaderIn.setValue("CUST_GRP2", hdr.getCustGrp2());
		orderHeaderInX.setValue("CUST_GRP2", "X");
		orderHeaderIn.setValue("CUST_GRP3", hdr.getCustGrp3());
		orderHeaderInX.setValue("CUST_GRP3", "X");
		orderHeaderIn.setValue("TELEPHONE",  hdr.getTelephone());
		orderHeaderInX.setValue("TELEPHONE", "X");
		
		orderHeaderIn.setValue("ORD_REASON", hdr.getOrdReason());
		orderHeaderInX.setValue("ORD_REASON", "X");

	}

	public void setSalesDocumentNumber(String salesDocumentNumber) {
		this.function.getImportParameterList().setValue("SALESDOCUMENT", salesDocumentNumber);
	}

	public void addOrderItemIn(OrderItemIn item) {
		super.addOrderItemIn(item);
		this.orderItemIn.setValue("PRICE_DATE", item.getPriceDate());
		this.orderItemIn.setValue("MAX_PL_DLV", item.getMaxPartialDlv());
	}

	public void addOrderItemInX(String itmNumber) {
		this.orderItemInX.appendRow();
		this.orderItemInX.setValue("ITM_NUMBER", itmNumber);
		this.orderItemInX.setValue("UPDATEFLAG", "I"); // insert
		this.orderItemInX.setValue("PO_ITM_NO", "X");
		this.orderItemInX.setValue("POITM_NO_S", "X");
		this.orderItemInX.setValue("MATERIAL", "X");
		this.orderItemInX.setValue("PURCH_NO_S", "X");
		this.orderItemInX.setValue("PO_DAT_S", "X");
		this.orderItemInX.setValue("PRICE_DATE", "X");
		this.orderItemInX.setValue("SALES_UNIT", "X");
		this.orderItemInX.setValue("CUST_MAT35", "X");
	}

	public void addOrderScheduleInX(int itmNumber) {
		this.schedulesInX.appendRow();
		this.schedulesInX.setValue("UPDATEFLAG", "I"); // insert
		this.schedulesInX.setValue("ITM_NUMBER", itmNumber);	// association key with order_items_in
		this.schedulesInX.setValue("REQ_QTY", "X");
		this.schedulesInX.setValue("REQ_DATE", "X");
	}

	protected String getOrderItemInName() {
		return "ORDER_ITEM_IN";
	}

	protected String getOrderScheduleInName() {
		return "SCHEDULE_LINES";
	}

	protected String getOrderPartnersName() {
		return "PARTNERS";
	}

	protected String getOrderConditionsInName() {
		return "CONDITIONS_IN";
	}

}
