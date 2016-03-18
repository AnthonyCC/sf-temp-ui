package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;

public abstract class PayPalSettlementFileParser extends PayPalSettlementParser implements SynchronousParserClient {

	private SynchronousParserClient client = null;
	protected static final String RECORD_BODY = "RECORD_BODY";

	public PayPalSettlementFileParser() {
		super();
		fields.add(new Field(RECORD_TYPE, 8, true, "String"));
		fields.add(new Field(RECORD_BODY, -1, true));
	}
	
	public void parseFile(InputStream fileStream) {
		BufferedReader lines = null;
		try {
			lines = new BufferedReader(new InputStreamReader(fileStream));
			String line = null;
			int lineNumber = 0;
			while (null != (line = lines.readLine())) {
				++lineNumber;
				if (!processLine(lineNumber, line)){
					continue;
				}
				try {
					parseLine(line);
				} catch (BadDataException e) {
					exceptions.add(new BadDataException(e, "Error at line " + lineNumber + ": " + e.getMessage()));
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException ioe) {
			exceptions.add(new BadDataException(ioe));
		} finally {
			if (lines != null) {
				try {
					lines.close();
				} catch (IOException e) {
					exceptions.add(new BadDataException(e));
				}
			}
		}
	}
	
	@Override
    protected HashMap<String, String> tokenize(String line) throws BadDataException {
		HashMap<String, String> tokenMap = new HashMap<String, String>();
		String[] tokens = line.split(",", 2);
		for(int i = 0, size = this.fields.size(); i < size; i++) {
			Field f = this.fields.get(i);
			String v = tokens[i].trim();
			if (f.getType() != null && f.getType().equals("String")) {
				try {
					v = v.substring(v.indexOf("\"") + 1, v.lastIndexOf("\""));
				} catch (Exception e) {
					throw new BadDataException("Issue while parsing PayPal tokens - " + " token :" + f.getName());
				}
			}
			tokenMap.put(f.getName(), v);
		}
		return tokenMap;
	}
	
	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}
	
	public void accept(Object o) {
		client.accept(o);
	}
}
