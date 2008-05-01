package com.freshdirect.dataloader.geocodefilter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.CSVFileParser;
import com.freshdirect.dataloader.SynchronousParserClient;

public class GFCSVParser extends CSVFileParser implements  IParser {
	SynchronousParserClient client;
	GFRecord record;
	HashMap fieldMap = new HashMap();
	
	public GFCSVParser() {
        super();
        
        try {
        	fieldMap.put(GFUtil.PROP_ADDRESS_STREET1, new Integer(GFUtil.getProperty(GFUtil.PROP_ADDRESS_STREET1)));
        	fieldMap.put(GFUtil.PROP_ADDRESS_STREET2, new Integer(GFUtil.getProperty(GFUtil.PROP_ADDRESS_STREET2)));
        	fieldMap.put(GFUtil.PROP_ADDRESS_CITY, new Integer(GFUtil.getProperty(GFUtil.PROP_ADDRESS_CITY)));
        	fieldMap.put(GFUtil.PROP_ADDRESS_STATE, new Integer(GFUtil.getProperty(GFUtil.PROP_ADDRESS_STATE)));
        	fieldMap.put(GFUtil.PROP_ADDRESS_ZIPCODE, new Integer(GFUtil.getProperty(GFUtil.PROP_ADDRESS_ZIPCODE)));
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
	
	protected void makeObjects(String[] recordLine) throws BadDataException {
		
		record = new GFRecord();
		record.setZip(recordLine[((Integer)fieldMap.get(GFUtil.PROP_ADDRESS_ZIPCODE)).intValue()]);
		record.setStreetAddress(recordLine[((Integer)fieldMap.get(GFUtil.PROP_ADDRESS_STREET1)).intValue()]);
		record.setStreetAddress2(recordLine[((Integer)fieldMap.get(GFUtil.PROP_ADDRESS_STREET2)).intValue()]);
		record.setCity(recordLine[((Integer)fieldMap.get(GFUtil.PROP_ADDRESS_CITY)).intValue()]);
		record.setSource(recordLine);
		
		getClient().accept(record);
	}
		
	
}
