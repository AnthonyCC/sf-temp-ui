package com.freshdirect.dataloader.bcreports;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.dataloader.DBReportCreator;
import com.freshdirect.framework.util.DateUtil;

public class USQReportCreator extends DBReportCreator {

	private static final DecimalFormat NUMBER_FORMATTER = new java.text.DecimalFormat("0.##");

	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("USAGE: java USQReportCreator MM-dd-yyyy /path/where/to/store/files");
			System.exit(0);
		}
		
		Date day = SF.parse(args[0]);
		File f = new File(args[1] + File.separator + SF.format(day));
		if (!f.mkdir()) {
			throw new RuntimeException("The Directory already exists " + f);
		}

		USQReportCreator rc = new USQReportCreator();
		rc.createReports(day, f);
	}

	private void createReports(Date day, File f) throws SQLException, IOException {
		Connection conn = this.getConnection();
		try {
			new SaleReport(day, new File(f, "usqSaleReport.tsv")).run(conn);
			new SettlementReport(day, new File(f, "usqSettlementReport.tsv")).run(conn);
			new StoreCreditReport(day, new File(f, "usqCreditReport.tsv")).run(conn);
			new CashbackReport(day, new File(f, "usqCashbackReport.tsv")).run(conn);
			new ChargebackReport(day, new File(f, "usqChargebackReport.tsv")).run(conn);
			new ReturnReport(day, new File(f, "usqReturnReport.tsv")).run(conn);
			new AppliedGiftCardReport(day, new File(f, "usqAppliedGiftCardReport.tsv")).run(conn);
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	private static class SaleReport extends Report {

		public SaleReport(Date day, File file) {
			super(day, file);
		}

		protected String getQuery() {
			return "select di.first_name, di.last_name, si.id, si.requested_date, "
				+ "(select upc from erps.material where version=ol.version and sap_id=ol.material_number) UPC, "
				+ "ol.material_number, ol.description, il.actual_quantity, "
				+ "(select default_price from erps.product where version=ol.version and sku_code=ol.sku_code) UNIT_PRICE, "
				+ "il.line_tax, il.actual_price as price, (il.actual_price + il.line_tax)  as total "
				+ "from (select s.id, sa.requested_date, sa.id as salesaction_id from cust.salesaction sa, cust.sale s "
				+ "	 where sa.requested_date = ? and sa.action_type in ('CRO', 'MOD') "
				+ "	 and sa.action_date = (select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
				+ "	 and sa.sale_id = s.id and s.status <> 'CAN') si, "
				+ "cust.deliveryinfo di, cust.orderline ol, cust.salesaction sa, cust.invoiceline il "
				+ "where di.salesaction_id = si.salesaction_id and si.salesaction_id = ol.salesaction_id "
				+ "and si.id = sa.sale_id and sa.action_type = 'INV' and sa.action_date = (select min(action_date) from cust.salesaction where sale_id=si.id and action_type = 'INV') "
				+ "and il.salesaction_id = sa.id and ol.affiliate = 'USQ' and ol.orderline_number = il.orderline_number ";
		}

		protected Object[] getParams() {
			return new Object[] { new java.sql.Date(day.getTime())};
		}

		protected void processRow(ResultSet rs) throws SQLException {
			sb.append(rs.getString("FIRST_NAME")).append("\t");
			sb.append(rs.getString("LAST_NAME")).append("\t");
			sb.append(rs.getString("ID")).append("\t");
			sb.append(SF.format(rs.getDate("REQUESTED_DATE"))).append("\t");
			sb.append(rs.getString("UPC")).append("\t");
			sb.append(rs.getString("MATERIAL_NUMBER")).append("\t");
			sb.append(rs.getString("DESCRIPTION")).append("\t");
			sb.append(NUMBER_FORMATTER.format(rs.getDouble("ACTUAL_QUANTITY"))).append("\t");
			sb.append(NUMBER_FORMATTER.format(rs.getDouble("UNIT_PRICE"))).append("\t");
			sb.append(NUMBER_FORMATTER.format(rs.getDouble("LINE_TAX"))).append("\t");
			sb.append(NUMBER_FORMATTER.format(rs.getDouble("PRICE"))).append("\t");
			sb.append(NUMBER_FORMATTER.format(rs.getDouble("TOTAL"))).append("\t");
			sb.append("\n");
		}

		protected List getHeaders() {
			List lst = new ArrayList();
			lst.add("First Name");
			lst.add("Last Name");
			lst.add("Web Order Number");
			lst.add("Delivery Date");
			lst.add("Material UPC");
			lst.add("SAP Material Number");
			lst.add("Material Description");
			lst.add("Quantity Delivered");
			lst.add("Unit Price");
			lst.add("Sales Tax");
			lst.add("Sales Amount");
			lst.add("Total Amount");
			return lst;
		}
	}

	private static class SettlementReport extends SaleReport {

		public SettlementReport(Date day, File file) {
			super(day, file);
		}
		/*
		protected String getQuery() {
			return "select di.first_name, di.last_name, si.id, si.requested_date, "
				+ "(select upc from erps.material where version=ol.version and sap_id=ol.material_number) UPC, "
				+ "ol.material_number, ol.description, il.actual_quantity, "
				+ "(select default_price from erps.product where version=ol.version and sku_code=ol.sku_code) UNIT_PRICE, "
				+ "il.line_tax, il.actual_price as price, (il.actual_price + il.line_tax)  as total "
				+ "from (select s.id, sa.requested_date, sa.id as salesaction_id "
				+ "from cust.salesaction sa, cust.sale s, "
				+ "(select distinct sale_id from cust.salesaction x where x.action_type IN ('STL','POG') and x.action_date between ? and ? ) stl  "
				+ "where stl.sale_id = s.id and sa.action_type in ('CRO', 'MOD') "
				+ "and sa.action_date = (select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
				+ "and sa.sale_id = s.id and s.status <> 'CAN') si, "
				+ "cust.deliveryinfo di, cust.orderline ol, cust.salesaction sa, cust.invoiceline il "
				+ "where di.salesaction_id = si.salesaction_id and si.salesaction_id = ol.salesaction_id "
				+ "and si.id = sa.sale_id and sa.action_type = 'INV' and sa.action_date = (select max(action_date) from cust.salesaction where sale_id=si.id and action_type = 'INV') "
				+ "and il.salesaction_id = sa.id and ol.affiliate = 'USQ' and ol.orderline_number = il.orderline_number ";
		}
	*/
		//Modified query to accomodate GC only orders that were settled with GCs.
		protected String getQuery() {
			return "select di.first_name, di.last_name, si.id, si.requested_date, "
				+ "(select upc from erps.material where version=ol.version and sap_id=ol.material_number) UPC, "
				+ "ol.material_number, ol.description, il.actual_quantity, "
				+ "(select default_price from erps.product where version=ol.version and sku_code=ol.sku_code) UNIT_PRICE, "
				+ "il.line_tax, il.actual_price as price, (il.actual_price + il.line_tax)  as total "
				+ "from (select s.id, sa.requested_date, sa.id as salesaction_id "
				+ "from cust.salesaction sa, cust.sale s, "
				+ "(select distinct sale_id from cust.salesaction x where x.action_type IN ('STL','POG') and x.action_date between ? and ? ) stl "
				+ "where stl.sale_id = s.id and sa.action_type in ('CRO', 'MOD') "
				+ "and sa.action_date = (select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
				+ "and sa.sale_id = s.id and s.status <> 'CAN') si, "
				+ "cust.deliveryinfo di, cust.orderline ol, cust.salesaction sa, cust.invoiceline il "
				+ "where di.salesaction_id = si.salesaction_id and si.salesaction_id = ol.salesaction_id "
				+ "and si.id = sa.sale_id and sa.action_type = 'INV' and sa.action_date = (select max(action_date) from cust.salesaction where sale_id=si.id and action_type = 'INV') "
				+ "and il.salesaction_id = sa.id and ol.affiliate = 'USQ' and ol.orderline_number = il.orderline_number ";
		}
		protected Object[] getParams() {
			Object[] params = new Object[2];
			params[0] = new java.sql.Date(day.getTime());

			Calendar dayCal = DateUtil.toCalendar(day);
			dayCal.add(Calendar.DATE, 1);
			dayCal = DateUtil.truncate(dayCal);

			params[1] = new java.sql.Date(dayCal.getTime().getTime());
			return params;
		}

		protected void processRow(ResultSet rs) throws SQLException {
			sb.append(SF.format(day)).append("\t");
			super.processRow(rs);
		}

		protected List getHeaders() {
			List lst = new ArrayList();
			lst.add("Settlement Date");
			lst.addAll(super.getHeaders());
			return lst;
		}

	}

	private static class AppliedGiftCardReport extends SaleReport {
		private double totalAppliedAmount = 0.0; 
		public AppliedGiftCardReport(Date day, File file) {
			super(day, file);
		}

		protected String getQuery() {
			return "select di.first_name, di.last_name, si.id, si.requested_date, agc.certificate_num, agc.amount, sa.invoice_number  "
			+ "from (select s.id, sa.requested_date, sa.id as salesaction_id from cust.salesaction sa, cust.sale s "
			+ "	 where sa.requested_date = ? and sa.action_type in ('CRO', 'MOD') "
			+ "	 and sa.action_date = (select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
			+ "	 and sa.sale_id = s.id and s.status <> 'CAN') si, "
			+ "cust.deliveryinfo di, cust.salesaction sa, cust.applied_gift_card agc "
			+ "where di.salesaction_id = si.salesaction_id  "
			+ "and si.id = sa.sale_id and sa.action_type = 'INV' and sa.action_date = (select min(action_date) from cust.salesaction where sale_id=si.id and action_type = 'INV') "
			+ "and agc.salesaction_id = sa.id and agc.affiliate = 'USQ'";
		}

		protected Object[] getParams() {
			return new Object[] { new java.sql.Date(day.getTime())};
		}

		protected void processRow(ResultSet rs) throws SQLException {
			sb.append(rs.getString("FIRST_NAME")).append("\t");
			sb.append(rs.getString("LAST_NAME")).append("\t");
			sb.append(rs.getString("ID")).append("\t");
			sb.append(SF.format(rs.getDate("REQUESTED_DATE"))).append("\t");
			sb.append(rs.getString("CERTIFICATE_NUM")).append("\t");
			sb.append(SF.format(rs.getDouble("AMOUNT"))).append("\t");
			totalAppliedAmount += rs.getDouble("AMOUNT");
			sb.append(rs.getString("INVOICE_NUMBER")).append("\n");
		}

		protected List getHeaders() {
			List lst = new ArrayList();
			lst.add("First Name");
			lst.add("Last Name");
			lst.add("Web Order Number");
			lst.add("Delivery Date");			
			lst.add("Certificate Number");
			lst.add("Applied Amount");
			lst.add("Invoice Number");
			return lst;
		}
		protected String getFooter(){
			return ("Total Applied Amount : "+NUMBER_FORMATTER.format(totalAppliedAmount));
		}
	}

	private static abstract class RefundReport extends Report {

		public RefundReport(Date day, File file) {
			super(day, file);
		}

		protected void processRow(ResultSet rs) throws SQLException {
			sb.append(this.getType()).append("\t");
			sb.append(SF.format(rs.getDate("ACTION_DATE"))).append("\t");
			sb.append(rs.getString("SALE_ID")).append("\t");
			sb.append(rs.getString("UPC")).append("\t");
			sb.append(rs.getString("MATERIAL_NUMBER")).append("\t");
			sb.append(NUMBER_FORMATTER.format(rs.getDouble("AMOUNT"))).append("\t");
			sb.append("\n");

		}

		protected List getHeaders() {
			List lst = new ArrayList();
			lst.add("Type");
			lst.add("Date of " + this.getType());
			lst.add("Web Order Number");
			lst.add("Material UPC");
			lst.add("SAP Material Number");
			lst.add("Amount of " + this.getType());
			return lst;
		}

		protected abstract String getType();
	}

	private static class StoreCreditReport extends RefundReport {
		private final String type = "Store Credit";

		public StoreCreditReport(Date day, File file) {
			super(day, file);
		}

		protected String getType() {
			return this.type;
		}

		protected String getQuery() {
			return "select trunc(c.approved_date) as action_date, c.sale_id, (select upc from erps.material where version=ol.version and sap_id=ol.material_number) UPC, "
				+ "ol.material_number, cl.amount "
				+ "from cust.complaint c, cust.complaintline cl, cust.orderline ol "
				+ "where trunc(c.approved_date) = ? and c.id = cl.complaint_id and ol.id = cl.orderline_id and ol.affiliate = 'USQ' "
				+ "and cl.method = ? and c.status = 'APP' ";
		}

		protected Object[] getParams() {
			Object[] params = new Object[2];
			params[0] = new java.sql.Date(day.getTime());
			params[1] = EnumComplaintLineMethod.STORE_CREDIT.getStatusCode();
			return params;
		}
	}

	private static class CashbackReport extends StoreCreditReport {
		private final String type = "Cashback";

		public CashbackReport(Date day, File file) {
			super(day, file);
		}

		protected String getType() {
			return this.type;
		}

		protected Object[] getParams() {
			Object[] params = new Object[2];
			params[0] = new java.sql.Date(day.getTime());
			params[1] = EnumComplaintLineMethod.CASH_BACK.getStatusCode();
			return params;
		}
	}

	private static class ChargebackReport extends RefundReport {
		private final String type = "Chargeback";
		public ChargebackReport(Date day, File file) {
			super(day, file);
		}
		protected String getType() {
			return this.type;
		}
		protected String getQuery() {
			return "select si.action_date, si.sale_id, m.upc, ol.material_number, si.amount "
				+ "from (select sale_id, action_date, amount from cust.salesaction where trunc(action_date) = ? and action_type = 'CBK') si, "
				+ "cust.salesaction sa, cust.orderline ol, erps.material m "
				+ "where sa.sale_id = si.sale_id and sa.action_type in ('CRO', 'MOD') "
				+ "and sa.action_date = (select max(action_date) from cust.salesaction where sale_id=si.sale_id and action_type in ('CRO','MOD')) "
				+ "and ol.salesaction_id = sa.id and ol.affiliate = 'USQ' "
				+ "and ol.version = m.version and ol.material_number = m.sap_id ";
		}

		protected Object[] getParams() {
			return new Object[] { new java.sql.Date(day.getTime())};
		}
	}

	private static class ReturnReport extends RefundReport {
		private final String type = "Return";

		public ReturnReport(Date day, File file) {
			super(day, file);
		}

		protected String getType() {
			return this.type;
		}

		protected String getQuery() {
			return "select trunc(si.action_date) action_date, si.sale_id, (select upc from erps.material where version=ol.version and sap_id=ol.material_number) UPC, "
				+ "ol.material_number, (il3.actual_price - il2.actual_price) amount "
				+ "from (select sa.sale_id, sa.id, sa.action_date from cust.salesaction sa where trunc(sa.action_date) = ? and sa.action_type = 'INV' "
				+ "and (select count(1) from cust.salesaction where sale_id = sa.sale_id and action_type = 'INV') >= 2 "
				+ "and sa.action_date = (select max(action_date) from cust.salesaction where sale_id = sa.sale_id and action_type = 'INV')) si, "
				+ "cust.salesaction sa1, cust.orderline ol, cust.salesaction sa2, cust.invoiceline il2, cust.salesaction sa3, cust.invoiceline il3 "
				+ "where sa1.sale_id = si.sale_id and sa1.action_type in ('CRO', 'MOD') "
				+ "and sa1.action_date = (select max(action_date) from cust.salesaction where sale_id = sa1.sale_id and action_type in ('CRO', 'MOD')) "
				+ "and sa1.id = ol.salesaction_id and ol.affiliate = 'USQ' "
				+ "and sa2.id = si.id and sa2.id = il2.salesaction_id and il2.orderline_number = ol.orderline_number "
				+ "and sa3.sale_id = si.sale_id and sa3.action_type = 'INV' "
				+ "and sa3.action_date = (select min(action_date) from cust.salesaction where sale_id = sa3.sale_id and action_type = 'INV') "
				+ "and sa3.id = il3.salesaction_id and il3.orderline_number = il2.orderline_number "
				+ "and il3.actual_quantity <> il2.actual_quantity";
		}

		protected Object[] getParams() {
			return new Object[] { new java.sql.Date(day.getTime())};
		}
	}
}
