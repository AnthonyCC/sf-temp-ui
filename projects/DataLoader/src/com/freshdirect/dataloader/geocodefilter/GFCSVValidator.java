package com.freshdirect.dataloader.geocodefilter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

import com.freshdirect.common.address.AddressModel;

public class GFCSVValidator extends GFBaseValidator  {
		
	private List<String[]> results = null;
	
	@Override
    public AddressModel getAddressModel(GFRecord record) {
		
		AddressModel address = new AddressModel();
		address.setAddress1(record.getStreetAddress());
		address.setAddress2(record.getStreetAddress2());
		address.setZipCode(record.getZip());
		address.setCity(record.getCity());
		return address;
	}
	
	@Override
    public boolean isAddressValid(boolean homeDelivery, boolean corporateDelivery) {
		return homeDelivery || corporateDelivery;
	}
	
	@Override
    public void initialize() {
		results = new ArrayList<String[]>();
	}
	
	@Override
    public synchronized void addResult(GFRecord record, boolean homeDelivery, boolean corporateDelivery) {
		
		String[] originalRow = (String[])record.getSource();
		String[] newRow = new String[originalRow.length+2];
		int intCount = 0;
		for(;intCount < originalRow.length; intCount++) {
			newRow[intCount] = originalRow[intCount];
		}
		newRow[originalRow.length-2] = (homeDelivery?"DELIVER":"DONOT DELIVER");
		newRow[originalRow.length-1] = (corporateDelivery?"DELIVER":"DONOT DELIVER");
		results.add(newRow);
	}
	
	public void flushResults(String destination) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(destination), ',');
		//results.add(arg0, arg1)
	    writer.writeAll(results);
		writer.close();
	}
	
}
