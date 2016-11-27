package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.FIN0010DataRecord;

public class FIN0010DataParser extends PaymentechSettlementParser {
	
	private SynchronousParserClient client = null;
	
	public FIN0010DataParser(){ 
		super();
		this.fields.add(new Field(ENTITY_TYPE, 2, true));
		this.fields.add(new Field(ENTITY_NUMBER, 10, true));
		this.fields.add(new Field(FUNDS_TRANSFER_INS_NUM, 10, false));
		this.fields.add(new Field(SECURE_BA_NUM, 10, false));
		this.fields.add(new Field(CURRENCY, 3, true));
		this.fields.add(new Field(MOP, 2, false));
		this.fields.add(new Field(CATEGORY, 6, true));
		this.fields.add(new Field(SETTLED_OR_CONVEYED, 1, true));
		this.fields.add(new Field(COUNT, 10, false));
		this.fields.add(new Field(AMOUNT, 17, true));
	}
	
	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		FIN0010DataRecord record = new FIN0010DataRecord();
		
		record.setEntityType(getString(tokens, ENTITY_TYPE));
		record.setEntityNumber(getString(tokens, ENTITY_NUMBER));
		record.setFundTransferInsNum(getString(tokens, FUNDS_TRANSFER_INS_NUM));
		record.setSecureBANum(getString(tokens, SECURE_BA_NUM));
		record.setCurrency(getString(tokens, CURRENCY));
		record.setMop(getString(tokens, MOP));
		record.setCategory(getCategory(tokens, CATEGORY));
		record.setSettledConveyed(getString(tokens, SETTLED_OR_CONVEYED));
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
