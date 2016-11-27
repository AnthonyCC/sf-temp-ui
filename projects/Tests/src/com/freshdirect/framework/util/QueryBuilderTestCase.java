package com.freshdirect.framework.util;

import junit.framework.TestCase;
import com.freshdirect.framework.util.QueryStringBuilder;
import com.freshdirect.framework.util.StringUtil;


public class QueryBuilderTestCase extends TestCase {

	
	public void testQuery1() {
		QueryStringBuilder builder = new QueryStringBuilder();
		
		Object flood = new String("\u00E1rv\u00EDz"),
		       NewYork = new String("New York"),
		       five = new Integer(5),
		       pi = new Double(3.14159);
		
		String keyNull = new String("key null"), 		keyNullV = StringUtil.escapeUri(keyNull),
		       keyNY = new String("key New York"), 		keyNYV = StringUtil.escapeUri(keyNY),
		       keyN = new String("key szam = number"), 	keyNV = StringUtil.escapeUri(keyN),
		       keyH = new String("key magyar"), 		keyHV = StringUtil.escapeUri(keyH);

		builder.
			addParam(keyNull, null).
			addParam(keyNY, NewYork).
			addParam(keyN, pi).
			addParam(keyN, five).
			addParam(keyH,flood);
		
		// test right order
		assertTrue(builder.getParameterValues(keyN).get(0).equals(pi));
		assertTrue(builder.getParameterValues(keyN).get(1).equals(five));
			
		String [] params = builder.toString().split("&");
	
		// test right number of params
		assertTrue(params.length== 4);
		
		for(int i = 0; i< params.length; ++i) {
			String[] param = params[i].split("=");
			
			
			// must be a key value pair
			assertTrue(param.length == 2);
			
			if (param[0].equals(keyNullV)) assertTrue(false);
			
			String stringVal = param[1];
			
			if (param[0].equals(keyNullV)) assertTrue(false);
			
			else if (param[0].equals(keyNYV)) {
				assertTrue(StringUtil.escapeUri((String)NewYork).equals(stringVal));
				builder.removeParam(keyNY);
				
			} else if (param[0].equals(keyNV)) {
				assertTrue(
					StringUtil.escapeUri(pi.toString()).equals(stringVal) ||
					StringUtil.escapeUri(five.toString()).equals(stringVal));
				builder.removeParam(keyN);
				
			} else if (param[0].equals(keyHV)) {
				assertTrue(StringUtil.escapeUri((String)flood).equals(stringVal));
				builder.removeParam(keyH);
			}
		}
		
		builder.removeParam(keyNull);
		
		
		// must be empty
		assertTrue(builder.size() == 0);
		
		
	}
}
