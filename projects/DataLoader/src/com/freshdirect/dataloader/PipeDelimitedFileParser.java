package com.freshdirect.dataloader;

import java.util.HashMap;

public abstract class PipeDelimitedFileParser extends FlatFileParser {
	
	protected PipeDelimitedFileParser(){
		super();
	}

	protected HashMap tokenize(String line) throws BadDataException {
		HashMap tokenMap = new HashMap();
		String[] tokens = line.split("\\|", -2);
		for(int i = 0, size = this.fields.size(); i < size; i++){
			Field f = (Field)this.fields.get(i);
			String v = tokens[i].trim();
			tokenMap.put(f.getName(), v);
		}
		
		return tokenMap;
	}

}
