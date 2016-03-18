package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.FlatFileParser.Field;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paypal.SectionHeaderDataRecord;

public class SectionHeaderParser extends PayPalSettlementParser {

	private final EnumPayPalRecordType type;

	private static final String REPORT_PERIOD_START_DATE = "REPORT_PERIOD_START_DATE";
	private static final String REPORT_PERIOD_END_DATE = "REPORT_PERIOD_END_DATE";
	private static final String ACCOUNT_ID = "ACCOUNT_ID";
	private static final String PARTNER_ACCOUNT_ID = "PARTNER_ACCOUNT_ID";
	
	public SectionHeaderParser(EnumPayPalRecordType type){
		super();
		this.type = type;
		fields.add(new Field(REPORT_PERIOD_START_DATE, 26, true));
		fields.add(new Field(REPORT_PERIOD_END_DATE, 26, true)); //only A is applicable for FD
		fields.add(new Field(ACCOUNT_ID, 127, true, "String"));
		fields.add(new Field(PARTNER_ACCOUNT_ID, 127, false, "String"));
	}
	
	@Override
	protected void makeObjects(Map<String, String> tokens)
			throws BadDataException {
		SectionHeaderDataRecord record = new SectionHeaderDataRecord();
		
		record.setReportPeriodStartDate(getDate(tokens, REPORT_PERIOD_START_DATE, "yyyy/MM/dd"));
		record.setReportPeriodEndDate(getDate(tokens, REPORT_PERIOD_END_DATE, "yyyy/MM/dd"));
		record.setAccountId(getString(tokens, ACCOUNT_ID));
		record.setPartnerAccountId(getString(tokens, PARTNER_ACCOUNT_ID));
		
		client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

}
