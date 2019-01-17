package com.freshdirect.dataloader.geocodefilter;

import java.util.HashMap;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.CSVFileParser;
import com.freshdirect.dataloader.SynchronousParserClient;

public class GFCSVParser extends CSVFileParser implements  IParser {
	SynchronousParserClient client;
	GFRecord record;
	HashMap<String, Integer> fieldMap = new HashMap<String, Integer>();
	
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
	
	@Override
    protected void makeObjects(String[] recordLine) throws BadDataException {
		
		record = new GFRecord();
		record.setZip(recordLine[fieldMap.get(GFUtil.PROP_ADDRESS_ZIPCODE).intValue()]);
		record.setStreetAddress(recordLine[fieldMap.get(GFUtil.PROP_ADDRESS_STREET1).intValue()]);
		record.setStreetAddress2(recordLine[fieldMap.get(GFUtil.PROP_ADDRESS_STREET2).intValue()]);
		record.setCity(recordLine[fieldMap.get(GFUtil.PROP_ADDRESS_CITY).intValue()]);
		record.setSource(recordLine);
		
		getClient().accept(record);
	}
		
	
}
