package com.freshdirect.framework.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


import junit.framework.TestCase;

public class CSVUtilsTestCase extends TestCase {
	
	private static final String CSVUnixSimple = "a,b,c,d\nd,c,b,a\nx,y,z,";
	private static final String CSVWindowsSimple = "a,b,c,d\r\nd,c,b,a\r\nx,y,z,";
	
	private static final String ESCAPED 
		 = "\" \"\"a v \",a\t  b,\n,,,,z,,\n,\"hello, baby!\",,\n,ok,\",,\n\nok\\\",i";
	
	private static final String REDUNDANT =  ", , \n   a   ,    b,    \" c, d\"\" \" , \n, k, \n";
	
	private static final String EMPTY_COLUMNS = "\n\n\n\na,b\n\nc\r\n\n\r\n";
	
	
	public void testSimple() {
	
		int counter = 0;
		StringBuffer result = new StringBuffer();
		for(Iterator i = CSVUtils.iterator(new ByteArrayInputStream(CSVUnixSimple.getBytes())); i.hasNext(); ++counter) {
			result.append(i.next());
		}
		
		assertTrue(result.toString().equals(CSVUnixSimple));
		assertTrue(counter == 23);
		
		counter = 0;
		result = new StringBuffer();
		for(Iterator i = CSVUtils.iterator(new ByteArrayInputStream(CSVWindowsSimple.getBytes())); i.hasNext(); ++counter) {
			result.append(i.next());
		}
		
		assertTrue(result.toString().equals(CSVWindowsSimple));
		assertTrue(counter == 23);
	}
	
	public void testEscaped() throws IOException {
		
		List result = CSVUtils.parse(new ByteArrayInputStream(ESCAPED.getBytes()), true, false);
	
		StringBuffer copy = new StringBuffer();
		for(Iterator i = result.iterator(); i.hasNext(); ) {
			for(Iterator j = ((List)(i.next())).iterator(); j.hasNext(); ) {
				copy.append(CSVUtils.escape((String)j.next()));
				if (j.hasNext()) copy.append(',');
			}
			if (i.hasNext()) copy.append('\n');
		}
		
		assertTrue(copy.toString().equals(ESCAPED));
		assertTrue(((List)result.get(0)).get(1).equals("a\t  b"));

	}
	
	public void testRedundant() throws IOException {
		List result = CSVUtils.parse(new ByteArrayInputStream(REDUNDANT.getBytes()), true, false);
		
		assertTrue(((List)result.get(0)).get(1).equals(""));
		assertTrue(((List)result.get(1)).get(0).equals("a"));
		assertTrue(((List)result.get(1)).get(2).equals(" c, d\" "));
	}
	
	public void testEmptyColumn() throws IOException {
		List result = CSVUtils.parse(new ByteArrayInputStream(EMPTY_COLUMNS.getBytes()), true, false);
		assertTrue(result.size() == 2);
	}

}
