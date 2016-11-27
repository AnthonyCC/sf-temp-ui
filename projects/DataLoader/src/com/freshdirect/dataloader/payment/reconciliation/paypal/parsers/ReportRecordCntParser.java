package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.FlatFileParser.Field;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paypal.ReportRecordCntDataRecord;

public class ReportRecordCntParser extends PayPalSettlementParser {
	
	private final EnumPayPalRecordType type;
	private static final String ROW_COUNT = "ROW_COUNT";
	
	public ReportRecordCntParser(EnumPayPalRecordType type) {
		super();
		this.type = type;
		fields.add(new Field(ROW_COUNT, 24, true));
	}
	
	@Override
	protected void makeObjects(Map<String, String> tokens)
			throws BadDataException {
		ReportRecordCntDataRecord record = new ReportRecordCntDataRecord();
		
		record.setRowCount(getInt(tokens, ROW_COUNT));
		client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
}
