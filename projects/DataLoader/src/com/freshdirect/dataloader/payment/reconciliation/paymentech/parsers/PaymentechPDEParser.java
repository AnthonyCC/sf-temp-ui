package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.HashMap;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechRecordType;

public class PaymentechPDEParser extends PaymentechParser {

	protected void makeObjects(HashMap tokens) throws BadDataException {
		
		EnumPaymentechRecordType type = this.getRecordType(tokens, RECORD_TYPE);
		String record = this.getString(tokens, RECORD_BODY);
		PaymentechSettlementParser parser = null;
		
		if(EnumPaymentechRecordType.DFR_START.equals(type)){
			parser = new DFRStartEndParser(type);
		} else if(type.isHeader()){
			parser = new PaymentechHeaderParser(type);
		} else if(EnumPaymentechRecordType.PDE0017_SUMMARY_DATA.equals(type)){
			parser = new PDE0017SDataParser();
		} else if (EnumPaymentechRecordType.PDE0017_DETAIL_DATA.equals(type)){
			parser = new PDE0017DDataParser();
		} else if(EnumPaymentechRecordType.PDE0018_SUMMARY_DATA.equals(type)){
			parser = new PDE0018SDataParser();
		} else if (EnumPaymentechRecordType.PDE0018_DETAIL_DATA.equals(type)){
			parser = new PDE0018DDataParser();
		} else if (EnumPaymentechRecordType.PDE0020_DATA.equals(type)){
			parser = new PDE0020DataParser();
		} else if (EnumPaymentechRecordType.PDE0022_DETAIL_DATA.equals(type)){
			parser = new PDE0022DDataParser();
		} else if(EnumPaymentechRecordType.DFR_END.equals(type)) {
			parser = new DFRStartEndParser(type);
		}else {
            throw new BadDataException("Don't know how to create a parser for " + type + " records");
        }
		
		if(parser != null){
			parser.setClient(this);
			parser.parseLine(record);
		}

	}

}
