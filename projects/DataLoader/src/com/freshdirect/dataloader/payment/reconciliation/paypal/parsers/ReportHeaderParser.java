package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.FlatFileParser.Field;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paypal.ReportHeaderDataRecord;

public class ReportHeaderParser extends PayPalSettlementParser {

	private final EnumPayPalRecordType type;
	
	private static final String REPORT_GEN_DATE = "REPORT_GEN_DATE";
	private static final String REPORTING_WINDOW = "REPORTING_WINDOW";
	private static final String ACCOUNT_ID = "ACCOUNT_ID";
	private static final String REPORT_VERSION = "REPORT_VERSION";
		
	public ReportHeaderParser(EnumPayPalRecordType type){
		super();
		this.type = type;
		fields.add(new Field(REPORT_GEN_DATE, 26, true));
		fields.add(new Field(REPORTING_WINDOW, 1, true, "String")); //only A is applicable for FD
		fields.add(new Field(ACCOUNT_ID, 127, true, "String"));
		fields.add(new Field(REPORT_VERSION, 3, true));
	}
	
	@Override
	protected void makeObjects(Map<String, String> tokens)
			throws BadDataException {
		ReportHeaderDataRecord record = new ReportHeaderDataRecord();
		
		record.setReportGenerationDate(getDate(tokens, REPORT_GEN_DATE, "yyyy/MM/dd HH:mm:ss Z"));
		record.setReportingWindow(getString(tokens, REPORTING_WINDOW).charAt(0));
		record.setAccountId(getString(tokens, ACCOUNT_ID));
		record.setReportVersion(getString(tokens, REPORT_VERSION));
		
		client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
}
