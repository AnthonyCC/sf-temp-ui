package com.freshdirect.transadmin.parser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

import com.freshdirect.transadmin.model.Asset;

public class AssetCSVProcessor extends AssetBaseProcessor  {
		
	private List<String[]> results = null;
	
	@Override
    public void initialize() {
		results = new ArrayList<String[]>();
	}
	
	@Override
    public synchronized void addResult(Asset record, boolean processed) {
		
		String[] originalRow = (String[])record.getSource();
		String[] newRow = new String[originalRow.length+1];
		int intCount = 0;
		for(;intCount < originalRow.length; intCount++) {
			newRow[intCount] = originalRow[intCount];
		}		
		newRow[originalRow.length-1] = (processed ? "PROCESSED":"NOT PROCESSED");
		results.add(newRow);
	}
	
	public void flushResults(String destination) throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(destination), ',');	
	    writer.writeAll(results);
		writer.close();
	}
	
}
