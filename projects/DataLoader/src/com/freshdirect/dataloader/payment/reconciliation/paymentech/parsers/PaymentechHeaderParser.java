package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PaymentechHeader;

public class PaymentechHeaderParser extends PaymentechSettlementParser {
	
	private SynchronousParserClient client = null;
	private EnumPaymentechRecordType type = null;
	private PaymentechHeader header;
	
	public PaymentechHeaderParser(EnumPaymentechRecordType type) {
		super();
		this.type = type;
		this.header = new PaymentechHeader(type);
		//The length field in here is the maximum lenght, it is not used in parsing
		//just given for refernce and documentation
		fields.add(new Field(COMPANY_ID, 10, true));
		fields.add(new Field(FROM_DATE, 10, true));
		fields.add(new Field(TO_DATE, 10, true));
		fields.add(new Field(GENERATION_DATE, 10, true));
		fields.add(new Field(GENERATION_TIME, 8, true));
	}

	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		
		this.header.setCompanyId(getString(tokens, COMPANY_ID));
		this.header.setFromDate(getDate(tokens, FROM_DATE, "MM/dd/yyyy"));
		this.header.setToDate(getDate(tokens, TO_DATE, "MM/dd/yyyy"));
		this.header.setGenerationDate(getDate(tokens, GENERATION_DATE, "MM/dd/yyyy"));
		this.header.setGenerationTime(getTime(tokens, GENERATION_TIME, "HH:mm:ss"));
		
		this.client.accept(this.header);
	}

	public void setClient(SynchronousParserClient client) {
		this.client = client;

	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

}
