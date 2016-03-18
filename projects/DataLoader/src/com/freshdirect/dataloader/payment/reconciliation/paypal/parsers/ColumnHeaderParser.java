package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.FlatFileParser.Field;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;

public class ColumnHeaderParser extends PayPalSettlementParser {

	private final EnumPayPalRecordType type;

		
	public ColumnHeaderParser(EnumPayPalRecordType type){
		super();
		this.type = type;

	}
	
	@Override
	protected void makeObjects(Map<String, String> tokens)
			throws BadDataException {
		// Left blank as it is not required

	}
}
