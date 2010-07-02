package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PDE0020DataRecord;

public class PDE0020DataParser extends PaymentechSettlementParser {
	
	private SynchronousParserClient client;
	
	private static final String RESERVED_1 = "RESERVED_1";
	private static final String RESERVED_2 = "RESERVED_2";
	private static final String STATUS_FLAG = "STATUS_FLAG";
	private static final String SEQUENCE_NUM = "SEQUENCE_NUM";
	private static final String MERCHANT_ORDER_NUM = "MERCHANT_ORDER_NUM";
	private static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
	private static final String REASON_CODE = "REASON_CODE";
	private static final String TRANSACTION_DATE = "TRANSACTION_DATE";
	private static final String CBK_DATE = "CBK_DATE";
	private static final String ACTIVITY_DATE = "ACTIVITY_DATE";
	private static final String USAGE_CODE = "USAGE_CODE";
	
	
	public PDE0020DataParser () {
		super();
		this.fields.add(new Field(ENTITY_TYPE, 2, true));
		this.fields.add(new Field(ENTITY_NUMBER, 10, true));
		this.fields.add(new Field(RESERVED_1, 0, false));
		this.fields.add(new Field(RESERVED_2, 0, false));
		this.fields.add(new Field(CURRENCY, 3, true));
		this.fields.add(new Field(CATEGORY, 6, true));
		this.fields.add(new Field(STATUS_FLAG, 1, false));
		this.fields.add(new Field(SEQUENCE_NUM, 10, true));
		this.fields.add(new Field(MERCHANT_ORDER_NUM, 22, true));
		this.fields.add(new Field(ACCOUNT_NUMBER, 19, true));
		this.fields.add(new Field(REASON_CODE, 2, true));
		this.fields.add(new Field(TRANSACTION_DATE, 10, true));
		this.fields.add(new Field(CBK_DATE, 10, true));
		this.fields.add(new Field(ACTIVITY_DATE, 10, true));
		this.fields.add(new Field(AMOUNT, 17, true));
		this.fields.add(new Field(USAGE_CODE, 1, true));
	}

	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		PDE0020DataRecord record = new PDE0020DataRecord();
		
		record.setEntityType(getString(tokens, ENTITY_TYPE));
		record.setEntityNumber(getString(tokens, ENTITY_NUMBER));
		record.setCurrency(getString(tokens, CURRENCY));
		record.setCategory(getCategory(tokens, CATEGORY));
		record.setStatusFlag(getString(tokens, STATUS_FLAG));
		record.setSequenceNumber(getString(tokens, SEQUENCE_NUM));
		record.setMerchantReferenceNumber(getMerchantReferenceNumber(tokens, MERCHANT_ORDER_NUM));
		record.setAccountNumber(getString(tokens, ACCOUNT_NUMBER));
		record.setReasonCode(getString(tokens, REASON_CODE));
		record.setTransactionDate(getDate(tokens, TRANSACTION_DATE, "MM/dd/yyyy"));
		record.setChargebackDate(getDate(tokens, CBK_DATE, "MM/dd/yyyy"));
		record.setActivityDate(getDate(tokens, ACTIVITY_DATE, "MM/dd/yyyy"));
		record.setAmount(Math.abs(getDouble(tokens, AMOUNT)));
		record.setUsageCode(getInt(tokens, USAGE_CODE));
		
		this.client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
}
