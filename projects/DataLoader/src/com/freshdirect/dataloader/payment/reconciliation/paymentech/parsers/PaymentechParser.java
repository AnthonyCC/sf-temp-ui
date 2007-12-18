package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;

public abstract class PaymentechParser extends PaymentechSettlementParser implements SynchronousParserClient {

	private SynchronousParserClient client = null;
	protected static final String RECORD_BODY = "RECORD_BODY";

	public PaymentechParser() {
		super();
		fields.add(new Field(RECORD_TYPE, 8, true));
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
				if ("".equals(line.trim()) || line.startsWith("#")){
					continue;
				}
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
	
	protected HashMap tokenize(String line) throws BadDataException {
		HashMap tokenMap = new HashMap();
		String[] tokens = line.split("\\|", 2);
		for(int i = 0, size = this.fields.size(); i < size; i++){
			Field f = (Field)this.fields.get(i);
			String v = tokens[i].trim();
			tokenMap.put(f.getName(), v);
		}
		return tokenMap;
	}
	
	protected abstract void makeObjects(HashMap tokens) throws BadDataException;

	public void setClient(SynchronousParserClient client) {
		this.client = client;
	}

	public SynchronousParserClient getClient() {
		return this.client;
	}

	public void accept(Object o) {
		this.client.accept(o);
	}

}