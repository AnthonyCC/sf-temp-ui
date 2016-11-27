package com.freshdirect.dataloader.geocodefilter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.common.address.AddressModel;

public class GFValidator extends GFBaseValidator {
	
	private StringBuffer results = null;
	
	@Override
    public AddressModel getAddressModel(GFRecord record) {
		AddressModel address = new AddressModel();
		address.setAddress1(record.getBldgNum() + " "
				+ (record.getDirectional() != null ? record.getDirectional() + " " : "")
				+ record.getStreetAddress() + " "
				+ (record.getPostDirectional() != null ? record.getPostDirectional() + " " : ""));
		address.setApartment(record.getAptNum());
		address.setZipCode(record.getZip());
		return address;
	}
	
	@Override
    public boolean isAddressValid(boolean homeDelivery, boolean corporateDelivery) {
		return homeDelivery;
	}
	
	@Override
    public void initialize() {
		results = new StringBuffer();
	}
	
	@Override
    public synchronized void addResult(GFRecord record,boolean homeDelivery, boolean corporateDelivery) {
		StringBuffer resultLine = new StringBuffer();
		resultLine.append(StringUtils.rightPad(record.getZip(),6))
		   .append(StringUtils.rightPad(record.getZpf(),5))
		   .append(StringUtils.rightPad(record.getSeqNum(),10))
		   .append(StringUtils.leftPad(record.getBldgNum(),10)).append(" ")
		   .append(StringUtils.rightPad(record.getDirectional(),3))
		   .append(StringUtils.rightPad(record.getStreetAddress(),41))
		   .append(StringUtils.rightPad(record.getPostDirectional(),3))
		   .append(StringUtils.rightPad(record.getAptDesignator(),5))
		   .append(StringUtils.rightPad(record.getAptNum(),16))
		   .append("\n");
		results.append(resultLine.toString());
	}
	
	public void flushResults(String destination) throws IOException {
		writeDestinationFile(results.toString(), destination);
	}
	
	private void writeDestinationFile(String data, String destination) throws IOException {
		 PrintWriter out
		   = new PrintWriter(new BufferedWriter(new FileWriter(destination)));
		 
		 out.write(data.toString());
		 out.close();
	}
}
