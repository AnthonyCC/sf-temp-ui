/*
 * Created on Apr 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PDE0018DRecord;

/**
 * @author jng
 *
 */
public class PDE0018DDataParser extends PaymentechSettlementParser {

	private SynchronousParserClient client;
	
	private static final String RESERVE_1 = "RESERVE_1";
	private static final String RESERVE_2 = "RESERVE_2";
	private static final String STATUS_FLAG = "STATUS_FLAG";
	private static final String SEQUENCE_NUM = "SEQUENCE_NUM";
	private static final String MERCHANT_ORDER_NUM = "MERCHANT_ORDER_NUM";
	private static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
	private static final String REASON_CODE = "REASON_CODE";
	private static final String TRANSACTION_DATE = "TRANSACTION_DATE";
	private static final String ECP_RETURN_DATE = "ECP_RETURN_DATE";
	private static final String ACTIVITY_DATE = "ACTIVITY_DATE";
	private static final String ECP_RETURN_AMOUNT = "ECP_RETURN_AMOUNT";
	private static final String USAGE_CODE = "USAGE_CODE";
	
	public PDE0018DDataParser () {
		super();
		this.fields.add(new Field(ENTITY_TYPE, 2, true));
		this.fields.add(new Field(ENTITY_NUMBER, 10, true));
		this.fields.add(new Field(RESERVE_1, 0, false));
		this.fields.add(new Field(RESERVE_2, 0, false));
		this.fields.add(new Field(CURRENCY, 3, true));
		this.fields.add(new Field(CATEGORY, 6, true));
		this.fields.add(new Field(STATUS_FLAG, 1, false));
		this.fields.add(new Field(SEQUENCE_NUM, 10, true));
		this.fields.add(new Field(MERCHANT_ORDER_NUM, 22, true));
		this.fields.add(new Field(ACCOUNT_NUMBER, 19, true));
		this.fields.add(new Field(REASON_CODE, 3, true));
		this.fields.add(new Field(TRANSACTION_DATE, 10, true));
		this.fields.add(new Field(ECP_RETURN_DATE, 10, true));
		this.fields.add(new Field(ACTIVITY_DATE, 10, true));
		this.fields.add(new Field(ECP_RETURN_AMOUNT, 17, false));
		this.fields.add(new Field(USAGE_CODE, 1, true));
		
	}
	
	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		PDE0018DRecord record = new PDE0018DRecord();
		
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
		record.setEcpReturnDate(getDate(tokens, ECP_RETURN_DATE, "MM/dd/yyyy"));
		record.setActivityDate(getDate(tokens, ACTIVITY_DATE, "MM/dd/yyyy"));
		record.setEcpReturnAmount(getDouble(tokens, ECP_RETURN_AMOUNT));
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
