package com.freshdirect.webapp.util.json;

import com.metaparadigm.jsonrpc.Serializer;
import com.metaparadigm.jsonrpc.StringSerializer;

public class FDStringSerializer extends StringSerializer implements Serializer {

	private static final long serialVersionUID = 1L;

	private static Class[] _JSONClasses = new Class[]
                                { String.class, Integer.class, Double.class };

	public Class[] getJSONClasses() {
		return _JSONClasses;
	}
	
}
