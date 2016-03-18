package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.payment.reconciliation.paypal.EnumPayPalRecordType;

public class PayPalParser extends PayPalSettlementFileParser {

	@Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
		
		EnumPayPalRecordType type = this.getRecordType(tokens, RECORD_TYPE);
		String record = this.getString(tokens, RECORD_BODY);
		PayPalSettlementParser parser = null;
		
		if(EnumPayPalRecordType.REPORT_HEADER.equals(type)){
			parser = new ReportHeaderParser(type);
		} else if (EnumPayPalRecordType.FILE_HEADER.equals(type)){
			parser = new FileHeaderParser(type);
		} else if(EnumPayPalRecordType.SECTION_HEADER.equals(type)){
			parser = new SectionHeaderParser(type);
		} else if(EnumPayPalRecordType.COLUMN_HEADER.equals(type)){
			parser = new ColumnHeaderParser(type);
		} else if (EnumPayPalRecordType.SECTION_BODY.equals(type)) {
			parser = new SectionBodyParser(type);
		} else if(EnumPayPalRecordType.SECTION_FOOTER.equals(type)) {
			parser = new SectionFooterParser(type);
		} else if(EnumPayPalRecordType.SECTION_COUNT.equals(type)) {
			parser = new SectionRecordCntParser(type);
		} else if(EnumPayPalRecordType.FILE_FOOTER.equals(type)) {
			parser = new FileFooterParser(type);
		} else if(EnumPayPalRecordType.REPORT_FOOTER.equals(type)) {
			parser = new ReportFooterParser(type);
		} else if(EnumPayPalRecordType.REPORT_COUNT.equals(type)) {
			parser = new ReportRecordCntParser(type);
		} else {
            throw new BadDataException("Don't know how to create a parser for PayPal " + type + " records");
        }
		
		if(parser != null){
			parser.setClient(this);
			parser.parseLine(record);
		}
	}


}
