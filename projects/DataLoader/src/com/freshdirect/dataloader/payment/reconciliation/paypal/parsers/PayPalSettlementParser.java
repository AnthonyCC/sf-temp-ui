package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;

public abstract class PayPalSettlementParser extends CommaDelimitedFileParser {

	public static String RECORD_TYPE = "RECORD_TYPE";
	SynchronousParserClient client;
	
	public EnumPayPalRecordType getRecordType(Map<String, String> tokens, String fieldName) throws BadDataException {
		String s = this.getString(tokens, fieldName);
		EnumPayPalRecordType type = EnumPayPalRecordType.getEnum(s);
		if(type == null) {
			throw new BadDataException("BAD RECORD TYPE: " +s);
		}
		return type;
	}
	
	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
}
