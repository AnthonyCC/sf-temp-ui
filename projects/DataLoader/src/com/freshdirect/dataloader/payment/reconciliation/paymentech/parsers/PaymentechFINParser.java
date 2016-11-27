package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechRecordType;

public class PaymentechFINParser extends PaymentechParser {
	
	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		
		EnumPaymentechRecordType type = this.getRecordType(tokens, RECORD_TYPE);
		String record = this.getString(tokens, RECORD_BODY);
		PaymentechSettlementParser parser = null;
		
		if(EnumPaymentechRecordType.DFR_START.equals(type)){
			parser = new DFRStartEndParser(type);
		} else if(type.isHeader()){
			parser = new PaymentechHeaderParser(type);
		} else if (EnumPaymentechRecordType.FIN0010_DATA.equals(type)){
			parser = new FIN0010DataParser();
		} else if(EnumPaymentechRecordType.LNK010A_DATA.equals(type)){
			parser = new LNK010ADataParser();
		} else if(EnumPaymentechRecordType.ACT0033_DATA.equals(type)){
			parser = new ACT0033DataParser();
		} else if (EnumPaymentechRecordType.FIN0011_DATA.equals(type)) {
			parser = new FIN0011DataParser();
		} else if(EnumPaymentechRecordType.ACT0010_DATA.equals(type)) {
			parser = new ACT0010DataParser();
		} else if(EnumPaymentechRecordType.DFR_END.equals(type)) {
			parser = new DFRStartEndParser(type);
		} else {
            throw new BadDataException("Don't know how to create a parser for " + type + " records");
        }
		
		if(parser != null){
			parser.setClient(this);
			parser.parseLine(record);
		}
	}
}
