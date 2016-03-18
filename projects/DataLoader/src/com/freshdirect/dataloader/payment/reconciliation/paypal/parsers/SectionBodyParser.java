package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import org.apache.log4j.helpers.ISO8601DateFormat;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.FlatFileParser.Field;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paypal.SectionBodyDataRecord;

public class SectionBodyParser extends PayPalSettlementParser {

	EnumPayPalRecordType type;
	
	private static final String TRANSACTION_ID = "TRANSACTION_ID";
	private static final String INVOICE_ID = "INVOICE_ID";
	private static final String PAYPAL_REFERENCE_ID = "PAYPAL_REFERENCE_ID";
	private static final String PAYPAL_REFERENCE_ID_TYPE = "PAYPAL_REFERENCE_ID_TYPE";
	private static final String TRANSACTION_EVENT_CODE = "TRANSACTION_EVENT_CODE";
	private static final String TRANSACTION_INITIATION_DATE = "TRANSACTION_INITIATION_DATE";
	private static final String TRANSACTION_COMPLETION_DATE = "TRANSACTION_COMPLETION_DATE";
	private static final String TRANSACTION_DEBIT_OR_CREDIT = "TRANSACTION_DEBIT_OR_CREDIT";
	private static final String GROSS_TRANSACTION_AMOUNT = "GROSS_TRANSACTION_AMOUNT";
	private static final String GROSS_TRANSACTION_CURRENCY = "GROSS_TRANSACTION_CURRENCY";
	private static final String FEE_DEBIT_OR_CREDIT = "FEE_DEBIT_OR_CREDIT";
	private static final String FEE_AMOUNT = "FEE_AMOUNT";
	private static final String FEE_CURRENCY = "FEE_CURRENCY";
	private static final String CUSTOM_FIELD = "CUSTOM_FIELD";
	private static final String CONSUMER_ID = "CONSUMER_ID";
	private static final String PAYMENT_TRACKING_ID = "PAYMENT_TRACKING_ID";
	private static final String STORE_ID = "STORE_ID";
	private static final String BANK_REFERENCE_ID = "BANK_REFERENCE_ID";
	private static final String CREDIT_TRANSACTIONAL_FEE = "CREDIT_TRANSACTIONAL_FEE";
	private static final String CREDIT_PROMOTIONAL_FEE = "CREDIT_PROMOTIONAL_FEE";
	private static final String CREDIT_TERM = "CREDIT_TERM";
	
	public SectionBodyParser(EnumPayPalRecordType type) {
		super();
		this.type = type;
		fields.add(new Field(TRANSACTION_ID, 24, true, "String"));
		fields.add(new Field(INVOICE_ID, 127, true, "String"));
		fields.add(new Field(PAYPAL_REFERENCE_ID, 24, true, "String"));
		fields.add(new Field(PAYPAL_REFERENCE_ID_TYPE, 127, true, "String"));
		fields.add(new Field(TRANSACTION_EVENT_CODE, 24, true, "String"));
		fields.add(new Field(TRANSACTION_INITIATION_DATE, 127, true));
		fields.add(new Field(TRANSACTION_COMPLETION_DATE, 24, true));
		fields.add(new Field(TRANSACTION_DEBIT_OR_CREDIT, 127, true, "String"));
		fields.add(new Field(GROSS_TRANSACTION_AMOUNT, 24, true));
		fields.add(new Field(GROSS_TRANSACTION_CURRENCY, 127, true, "String"));
		fields.add(new Field(FEE_DEBIT_OR_CREDIT, 24, true, "String"));
		fields.add(new Field(FEE_AMOUNT, 127, true));
		fields.add(new Field(FEE_CURRENCY, 24, true, "String"));
		fields.add(new Field(CUSTOM_FIELD, 127, false, "String"));
		fields.add(new Field(CONSUMER_ID, 24, true, "String"));
		fields.add(new Field(PAYMENT_TRACKING_ID, 127, false, "String"));
		fields.add(new Field(STORE_ID, 24, false, "String"));
		fields.add(new Field(BANK_REFERENCE_ID, 127, false, "String"));
		fields.add(new Field(CREDIT_TRANSACTIONAL_FEE, 24, false));
		fields.add(new Field(CREDIT_PROMOTIONAL_FEE, 127, false));
		fields.add(new Field(CREDIT_TERM, 24, false, "String"));
	}
	
	@Override
	protected void makeObjects(Map<String, String> tokens)
			throws BadDataException {
		SectionBodyDataRecord record = new SectionBodyDataRecord();

		record.setTransactionId(getString(tokens, TRANSACTION_ID));
		record.setInvoiceId(getString(tokens, INVOICE_ID));
		record.setPaypalReferenceId(getString(tokens, PAYPAL_REFERENCE_ID));
		record.setPaypalReferenceIdType(getString(tokens, PAYPAL_REFERENCE_ID_TYPE)); //Only one type is sufficient "TXN". Others NA
		record.setTransactionEventCode(getString(tokens, TRANSACTION_EVENT_CODE));
		record.setTransactionInitiationDate(getDate(tokens, TRANSACTION_INITIATION_DATE, "yyyy/MM/dd HH:mm:ss Z"));
		record.setTransactionCompletionDate(getDate(tokens, TRANSACTION_COMPLETION_DATE, "yyyy/MM/dd HH:mm:ss Z"));
		record.setTransactionDebitOrCredit(getString(tokens, TRANSACTION_DEBIT_OR_CREDIT));
		record.setGrossTransactionAmount(getLong(tokens, GROSS_TRANSACTION_AMOUNT));
		record.setGrossTransactionCurrency(getString(tokens, GROSS_TRANSACTION_CURRENCY));
		record.setFeeDebitOrCredit(getString(tokens, FEE_DEBIT_OR_CREDIT));
		record.setFeeAmount(getLong(tokens, FEE_AMOUNT));
		record.setFeeCurrency(getString(tokens, FEE_CURRENCY));
		record.setCustomField(getString(tokens, CUSTOM_FIELD));
		record.setConsumerId(getString(tokens, CONSUMER_ID));
		record.setPaymentTrackingId(getString(tokens, PAYMENT_TRACKING_ID));
		record.setStoreId(getString(tokens, STORE_ID));
		record.setBankReferenceId(getString(tokens, BANK_REFERENCE_ID));
		record.setCreditTransactionalFee(getLong(tokens, CREDIT_TRANSACTIONAL_FEE));
		record.setCreditPromotionalFee(getLong(tokens, CREDIT_PROMOTIONAL_FEE));
		record.setCreditTerm(getString(tokens, CREDIT_TERM));
		
		client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
}
