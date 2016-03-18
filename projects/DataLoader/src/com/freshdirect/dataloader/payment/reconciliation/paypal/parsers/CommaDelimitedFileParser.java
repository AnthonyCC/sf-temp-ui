package com.freshdirect.dataloader.payment.reconciliation.paypal.parsers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.FlatFileParser;
import com.freshdirect.dataloader.FlatFileParser.Field;

public abstract class CommaDelimitedFileParser extends FlatFileParser {

	protected CommaDelimitedFileParser(){
		super();
	}

	@Override
    protected HashMap<String, String> tokenize(String line) throws BadDataException {
		HashMap<String, String> tokenMap = new HashMap<String, String>();
		String[] tokens = line.split(",", -2);
		for(int i = 0, size = this.fields.size(); i < size; i++){
			Field f = this.fields.get(i);
			String v = tokens[i].trim();
			if (f.getType() != null && f.getType().equals("String")) {
				if (StringUtils.isEmpty(v)) {
					tokenMap.put(f.getName(), v);
					continue;
				}
				v = v.substring(v.indexOf("\"") + 1, v.lastIndexOf("\""));
			}
			tokenMap.put(f.getName(), v);
		}
		
		return tokenMap;
	}

}
