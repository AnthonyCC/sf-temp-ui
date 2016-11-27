package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PDE0017SRecord;

public class PDE0017SDataParser extends PaymentechSettlementParser {
	
	private SynchronousParserClient client;
	
	private static final String RESERVED_FIELD_1 = "RESERVED_FIELD_1";
	private static final String RESERVED_FIELD_2 = "RESERVED_FIELD_2";
	private static final String IS_FINANCIAL = "IS_FINANACIAL";
	
	public PDE0017SDataParser () {
		super();
		this.fields.add(new Field(ENTITY_TYPE, 2, true));
		this.fields.add(new Field(ENTITY_NUMBER, 10, true));
		this.fields.add(new Field(RESERVED_FIELD_1, 0, false));
		this.fields.add(new Field(RESERVED_FIELD_2, 0, false));
		this.fields.add(new Field(CURRENCY, 3, true));
		this.fields.add(new Field(MOP, 2, true));
		this.fields.add(new Field(CATEGORY, 6, true));
		this.fields.add(new Field(IS_FINANCIAL, 3, true));
		this.fields.add(new Field(COUNT, 10, true));
		this.fields.add(new Field(AMOUNT, 16, false));
	}
	
	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		PDE0017SRecord record = new PDE0017SRecord();
		
		record.setEntityType(getString(tokens, ENTITY_TYPE));
		record.setEntityNumber(getString(tokens, ENTITY_NUMBER));
		record.setCurrency(getString(tokens, CURRENCY));
		// this report MOP will always be CC
		record.setMop(getString(tokens, MOP));
		record.setCategory(getCategory(tokens, CATEGORY));
		record.setFinancialNonFinancial(getString(tokens, IS_FINANCIAL));
		record.setCount(getInt(tokens, COUNT));
		record.setAmount(getDouble(tokens, AMOUNT));
		
		this.client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;

	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

}
