package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.payment.reconciliation.detail.CCDetailOne;

public class ACT0010DataParser extends PaymentechSettlementParser {
	private SynchronousParserClient client;

	private static final String SUBMISSION_DATE = "SUBMISSION_DATE";
	private static final String PRESENTER_ID = "PRESENTER_ID";
	private static final String PRESENTER_NAME = "PRESENTER_NAME";
	private static final String SUBMISSION_NUM = "SUBMISSION_NUM";
	private static final String RECORD_NUM = "RECORD_NUM";
	private static final String MERCHANT_ORDER_NUM = "MERCHANT_ORDER_NUM";
	private static final String RDFI_NUM = "RDFI_NUM";
	private static final String ACCOUNT_NUM = "ACCOUNT_NUM";
	private static final String EXPIRATION_DATE = "EXPIRATION_DATE";
	private static final String ACTION_CODE = "ACTION_CODE";
	private static final String AUTH_DATE = "AUTH_DATE";
	private static final String AUTH_CODE = "AUTH_CODE";
	private static final String AUTH_RESPONSE_CODE = "AUTH_RESPONSE_CODE";
	private static final String TRACE_NUM = "TRACE_NUM";
	private static final String INVOICE_NUM = "INVOICE_NUM";
	private static final String DEALER_NUM = "DEALER_NUM";
	private static final String MERCHANT_CATEGORY_CODE = "MERCHANT_CATEGORY_CODE";

	public ACT0010DataParser() {
		super();
		this.fields.add(new Field(SUBMISSION_DATE, 10, true));
		this.fields.add(new Field(PRESENTER_ID, 10, true));
		this.fields.add(new Field(PRESENTER_NAME, 6, true));
		this.fields.add(new Field(SUBMISSION_NUM, 11, true));
		this.fields.add(new Field(RECORD_NUM, 10, true));
		this.fields.add(new Field(ENTITY_TYPE, 2, true));
		this.fields.add(new Field(ENTITY_NUMBER, 10, true));
		this.fields.add(new Field(CURRENCY, 3, true));
		this.fields.add(new Field(MERCHANT_ORDER_NUM, 22, true));
		this.fields.add(new Field(RDFI_NUM, 10, true));
		this.fields.add(new Field(ACCOUNT_NUM, 19, true));
		this.fields.add(new Field(EXPIRATION_DATE, 5, false));
		this.fields.add(new Field(AMOUNT, 17, true));
		this.fields.add(new Field(MOP, 2, true));
		this.fields.add(new Field(ACTION_CODE, 2, true));
		this.fields.add(new Field(AUTH_DATE, 10, false));
		this.fields.add(new Field(AUTH_CODE, 6, false));
		this.fields.add(new Field(AUTH_RESPONSE_CODE, 3, false));
		//this.fields.add(new Field(TRACE_NUM, 6, false));
		//this.fields.add(new Field(INVOICE_NUM, 12, false));
		//this.fields.add(new Field(DEALER_NUM, 12, false));
		//this.fields.add(new Field(MERCHANT_CATEGORY_CODE, 4, false));

	}
	
	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		CCDetailOne record = new CCDetailOne();
		
		record.setProductCode(getString(tokens, MOP));
		record.setTransactionCode(getString(tokens, ACTION_CODE));
		record.setAccountNumber(getString(tokens, ACCOUNT_NUM).trim());
		record.setFDMSReferenceNumber(unQuoteString(getString(tokens, SUBMISSION_NUM)));
		record.setMerchantReferenceNumber(getMerchantReferenceNumber(tokens, MERCHANT_ORDER_NUM));
		record.setTransactionAmount(getDouble(tokens, AMOUNT));
		record.setMerchantNumber(getString(tokens, ENTITY_NUMBER));
		record.setAuthCode(getString(tokens, AUTH_CODE).trim());
		
		this.client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

}