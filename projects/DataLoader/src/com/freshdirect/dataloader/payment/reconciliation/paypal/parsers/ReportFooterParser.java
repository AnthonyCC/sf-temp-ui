package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paypal.ReportFooterDataRecord;

public class ReportFooterParser extends PayPalSettlementParser implements FooterDataParserHelper {

	private final EnumPayPalRecordType type;

	private static final String REPORT_RECORD_COUNT = "REPORT_RECORD_COUNT";
	
	public ReportFooterParser(EnumPayPalRecordType type){
		super();
		this.type = type;
		fields.add(new Field(CURRENCY_OF_BALANCE, 3, true, "String"));
		fields.add(new Field(TOTAL_GROSS_AMOUNT_CREDITS, 26, true));
		fields.add(new Field(TOTAL_GROSS_AMOUNT_DEBITS, 26, true));
		fields.add(new Field(TOTAL_TRANSACTION_FEE_CREDITS, 26, true));
		fields.add(new Field(TOTAL_TRANSACTION_FEE_DEBITS, 26, true));
		fields.add(new Field(BEGINNING_AVAILABLE_BALANCE_DEBIT_OR_CREDIT, 2, true, "String"));
		fields.add(new Field(BEGINNING_AVAILABLE_BALANCE_IN_THIS_CURRENCY, 26, true));
		fields.add(new Field(ENDING_AVAILABLE_BALANCE_DEBIT_OR_CREDIT, 2, true, "String"));
		fields.add(new Field(ENDING_AVAILABLE_BALANCE_IN_THIS_CURRENCY, 26, true));
		fields.add(new Field(BEGINNING_TOTAL_BALANCE_DEBIT_OR_CREDIT, 2, true, "String"));
		fields.add(new Field(BEGINNING_TOTAL_BALANCE_IN_THIS_CURRENCY, 26, true));
		fields.add(new Field(ENDING_TOTAL_BALANCE_DEBIT_OR_CREDIT, 2, true, "String"));
		fields.add(new Field(ENDING_TOTAL_BALANCE_IN_THIS_CURRENCY, 26, true));
		fields.add(new Field(BEGINNING_PAYABLE_BALANCE_DEBIT_OR_CREDIT, 2, true, "String"));
		fields.add(new Field(BEGINNING_PAYABLE_BALANCE, 26, true));
		fields.add(new Field(ENDING_PAYABLE_BALANCE_DEBIT_OR_CREDIT, 2, true, "String"));
		fields.add(new Field(ENDING_PAYABLE_BALANCE, 26, true));
		fields.add(new Field(REPORT_RECORD_COUNT, 26, true));
	}
	
	@Override
	protected void makeObjects(Map<String, String> tokens)
			throws BadDataException {
		ReportFooterDataRecord record = new ReportFooterDataRecord();
		
		record.setCurrencyOfBalance(getString(tokens, CURRENCY_OF_BALANCE));
		record.setTotalGrossAmountCredits(getLong(tokens, TOTAL_GROSS_AMOUNT_CREDITS));
		record.setTotalGrossAmountDebits(getLong(tokens, TOTAL_GROSS_AMOUNT_DEBITS));
		record.setTotalTransactionFeeCredits(getLong(tokens, TOTAL_TRANSACTION_FEE_CREDITS));
		record.setTotalTransactionFeeDebits(getLong(tokens, TOTAL_TRANSACTION_FEE_DEBITS));
		record.setBeginningAvailableBalanceDebitOrCredit(getString(tokens, BEGINNING_AVAILABLE_BALANCE_DEBIT_OR_CREDIT));
		record.setBeginningAvailableBalanceInThisCurrency(getLong(tokens, BEGINNING_AVAILABLE_BALANCE_IN_THIS_CURRENCY));
		record.setEndingAvailableBalanceDebitOrCredit(getString(tokens, ENDING_AVAILABLE_BALANCE_DEBIT_OR_CREDIT));
		record.setEndingAvailableBalanceInThisCurrency(getLong(tokens, ENDING_AVAILABLE_BALANCE_IN_THIS_CURRENCY));
		record.setBeginningTotalBalanceDebitOrCredit(getString(tokens, BEGINNING_TOTAL_BALANCE_DEBIT_OR_CREDIT));
		record.setBeginningTotalBalanceInThisCurrency(getLong(tokens, BEGINNING_TOTAL_BALANCE_IN_THIS_CURRENCY));
		record.setEndingTotalBalanceDebitOrCredit(getString(tokens, ENDING_TOTAL_BALANCE_DEBIT_OR_CREDIT));
		record.setEndingTotalBalanceInThisCurrency(getLong(tokens, ENDING_TOTAL_BALANCE_IN_THIS_CURRENCY));
		record.setBeginningPayableBalanceDebitOrCredit(getString(tokens, BEGINNING_PAYABLE_BALANCE_DEBIT_OR_CREDIT));
		record.setBeginningPayableBalance(getLong(tokens, BEGINNING_PAYABLE_BALANCE));
		record.setEndingPayableBalanceDebitOrCredit(getString(tokens, ENDING_PAYABLE_BALANCE_DEBIT_OR_CREDIT));
		record.setEndingPayableBalance(getLong(tokens, ENDING_PAYABLE_BALANCE));
		
		client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
}
