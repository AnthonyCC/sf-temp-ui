package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.HashMap;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.FIN0011DataRecord;

public class FIN0011DataParser extends PaymentechSettlementParser {
	private SynchronousParserClient client;
	
	private static final String SUB_CATEGORY = "SUB_CATEGORY";
	private static final String FEE_SCHEDULE = "FEE_SCHEDULE";
	private static final String INTERCHANGE_QUALIFICATION = "INTERCHANGE_QUALIFICATION";
	private static final String FEE_TYPE_DESC = "FEE_TYPE_DESC";
	private static final String ACTION_TYPE = "ACTION_TYPE";
	private static final String UNIT_QUANTITY = "UNIT_QUANTITY";
	private static final String UNIT_FEE = "UNIT_FEE";
	private static final String PERCENT_RATE = "PERCENT_RATE";
	private static final String TOTAL_CHARGE = "TOTAL_CHARGE";
	
	public FIN0011DataParser () {
		super();
		this.fields.add(new Field(CATEGORY, 6, true));
		this.fields.add(new Field(SUB_CATEGORY, 6, true));
		this.fields.add(new Field(ENTITY_TYPE, 2, true));
		this.fields.add(new Field(ENTITY_NUMBER, 10, true));
		this.fields.add(new Field(FUNDS_TRANSFER_INS_NUM, 0, false));
		this.fields.add(new Field(SECURE_BA_NUM, 0, false));
		this.fields.add(new Field(CURRENCY, 3, true));
		this.fields.add(new Field(FEE_SCHEDULE, 10, false));
		this.fields.add(new Field(MOP, 0, false));
		this.fields.add(new Field(INTERCHANGE_QUALIFICATION, 4, false));
		this.fields.add(new Field(FEE_TYPE_DESC, 30, true));
		this.fields.add(new Field(ACTION_TYPE, 1, false));
		this.fields.add(new Field(UNIT_QUANTITY, 10, false));
		this.fields.add(new Field(UNIT_FEE, 11, false));
		this.fields.add(new Field(AMOUNT, 17, false));
		this.fields.add(new Field(PERCENT_RATE, 8, false));
		this.fields.add(new Field(TOTAL_CHARGE, 17, true));
		
	}
	protected void makeObjects(HashMap tokens) throws BadDataException {
		FIN0011DataRecord record = new FIN0011DataRecord();
		
		record.setCategory(getCategory(tokens, CATEGORY));
		record.setSubCategory(getSubCategory(tokens, SUB_CATEGORY));
		record.setEntityType(getString(tokens, ENTITY_TYPE));
		record.setEntityNumber(getString(tokens, ENTITY_NUMBER));
		record.setFundTransferInsNum(getString(tokens, FUNDS_TRANSFER_INS_NUM));
		record.setSecureBANum(getString(tokens, SECURE_BA_NUM));
		record.setCurrency(getString(tokens, CURRENCY));
		record.setFeeSchedule(getInt(tokens, FEE_SCHEDULE));
		record.setMop(getString(tokens, MOP));
		record.setInterchangeQualification(getString(tokens, INTERCHANGE_QUALIFICATION));
		record.setFeeTypeDescription(unQuoteString(getString(tokens, FEE_TYPE_DESC)));
		record.setActionType(getString(tokens, ACTION_TYPE));
		record.setUnitQuantity(getInt(tokens, UNIT_QUANTITY));
		record.setUnitFee(getDouble(tokens, UNIT_FEE));
		record.setAmount(getDouble(tokens, AMOUNT) * -1);
		record.setPercentageRate(getDouble(tokens, PERCENT_RATE));
		record.setTotalCharge(getDouble(tokens, TOTAL_CHARGE) * -1);
		
		this.client.accept(record);

	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

}
