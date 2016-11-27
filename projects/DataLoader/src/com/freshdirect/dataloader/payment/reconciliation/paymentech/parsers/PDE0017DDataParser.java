package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PDE0017DRecord;

public class PDE0017DDataParser extends PaymentechSettlementParser {
	
	private SynchronousParserClient client;
	
	private static final String ISSUER_CBK_AMOUNT = "ISSUER_CBK_AMOUNT";
	private static final String PARTIAL_REPRESENTMENT = "PARTIAL_REPRESENTMENT";
	private static final String STATUS_FLAG = "STATUS_FLAG";
	private static final String SEQUENCE_NUM = "SEQUENCE_NUM";
	private static final String MERCHANT_ORDER_NUM = "MERCHANT_ORDER_NUM";
	private static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
	private static final String REASON_CODE = "REASON_CODE";
	private static final String TRANSACTION_DATE = "TRANSACTION_DATE";
	private static final String CBK_DATE = "CBK_DATE";
	private static final String ACTIVITY_DATE = "ACTIVITY_DATE";
	private static final String FEE_AMOUNT = "FEE_AMOUNT";
	private static final String USAGE_CODE = "USAGE_CODE";
	
	public PDE0017DDataParser () {
		super();
		this.fields.add(new Field(ENTITY_TYPE, 2, true));
		this.fields.add(new Field(ENTITY_NUMBER, 10, true));
		this.fields.add(new Field(ISSUER_CBK_AMOUNT, 17, true));
		this.fields.add(new Field(PARTIAL_REPRESENTMENT, 1, true));
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
		this.fields.add(new Field(FEE_AMOUNT, 17, false));
		this.fields.add(new Field(USAGE_CODE, 1, true));
		
	}
	
	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		PDE0017DRecord record = new PDE0017DRecord();
		
		record.setEntityType(getString(tokens, ENTITY_TYPE));
		record.setEntityNumber(getString(tokens, ENTITY_NUMBER));
		record.setIssuerCBKAmount(getDouble(tokens, ISSUER_CBK_AMOUNT));
		record.setPartialRepresentement(getString(tokens, PARTIAL_REPRESENTMENT));
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
		record.setFeeAmount(getDouble(tokens, FEE_AMOUNT));
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
