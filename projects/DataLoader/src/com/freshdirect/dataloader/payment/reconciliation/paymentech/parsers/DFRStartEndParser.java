package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.HashMap;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.DFREnd;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.DFRStart;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.DFRStartEnd;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechRecordType;

public class DFRStartEndParser extends PaymentechSettlementParser {
	
	private SynchronousParserClient client = null;
	private final EnumPaymentechRecordType type;
	
	private static final String PRESENTER_ID = "PRESENTER_ID";
	private static final String FREQUENCY = "FREQUENCY";
	
	public DFRStartEndParser(EnumPaymentechRecordType type){
		super();
		this.type = type;
		fields.add(new Field(PRESENTER_ID, 14, true));
		fields.add(new Field(FREQUENCY, 14, true));
		fields.add(new Field(COMPANY_ID, 13, true));
	}

	protected void makeObjects(HashMap tokens) throws BadDataException {
		DFRStartEnd record = null;
		if(EnumPaymentechRecordType.DFR_START.equals(type)){
			record = new DFRStart();
		}else if (EnumPaymentechRecordType.DFR_END.equals(type)){
			record = new DFREnd();
		}else{
			throw new BadDataException("Unknown DFR record: "+type.getName());
		}
		record.setPresenterId(getString(tokens, PRESENTER_ID).split("\\=", 2)[1]);
		record.setFrequency(getString(tokens, FREQUENCY).split("\\=", 2)[1]);
		record.setCompanyId(getString(tokens, COMPANY_ID).split("\\=", 2)[1]);
		
		this.client.accept(record);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

}
