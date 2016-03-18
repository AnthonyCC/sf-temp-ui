package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.FlatFileParser.Field;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paypal.FileHeaderDataRecord;

public class FileHeaderParser extends PayPalSettlementParser {

	private final EnumPayPalRecordType type;
	private static final String FILE_NUMBER = "FILE_NUMBER";
		
	public FileHeaderParser(EnumPayPalRecordType type){
		super();
		this.type = type;
		fields.add(new Field(FILE_NUMBER, 10, true));
	}
	
	@Override
	protected void makeObjects(Map<String, String> tokens)
			throws BadDataException {
		FileHeaderDataRecord record = new FileHeaderDataRecord();
		
		record.setFileNo(getInt(tokens, FILE_NUMBER));
		
		client.accept(record);
	}
	

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
}
