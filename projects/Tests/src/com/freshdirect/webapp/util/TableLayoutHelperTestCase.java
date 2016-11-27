package com.freshdirect.webapp.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.collections.Predicate;

public class TableLayoutHelperTestCase extends TestCase {
	
	static private class NemMinusz implements Predicate {

		public boolean evaluate(Object z) {
			if (z instanceof String) return !((String)z).startsWith("-");
			return false;
		}
		
	}
	
	static private class Hat implements Predicate {

		public boolean evaluate(Object z) {
			return "6".equals(z);
		}
		
	}
	
	private List zizi = Arrays.asList(new String[] {"1", "2", "3", "-5", "4", "5", "-5", "6", "7", "8", "-9" });
	
	public void testSimple() {
		
		TableLayoutHelper helper = new TableLayoutHelper(zizi.iterator(),new NemMinusz(), 3);
		
		// Expected
		// 1,2,3
		// 4,5,6
		// 7,8
		
		//System.out.println(helper.getRows());
		
		assertTrue(helper.getRows().size() == 3);
		assertTrue(helper.getRow(0).size() == 3);
		assertTrue(helper.getRow(1).size() == 3);
		assertTrue(helper.getRow(2).size() == 2);
		
		assertTrue(helper.getRow(1).get(1).equals("5"));
		assertTrue(helper.getRow(0).get(2).equals("3"));
	}
	
	public void testOwnRow() {
		TableLayoutHelper helper = new TableLayoutHelper(zizi.iterator(),new NemMinusz(), new Hat(), 3);
		
		// Expected
		// 1,2,3
		// 4,5
		// 6
		// 7,8
		
		//System.out.println(helper.getRows());
		
		assertTrue(helper.getRows().size() == 4);
		assertTrue(helper.getRow(0).size() == 3);
		assertTrue(helper.getRow(1).size() == 2);
		assertTrue(helper.getRow(2).size() == 1);
		assertTrue(helper.getRow(3).size() == 2);
		
		assertTrue(helper.getRow(1).get(1).equals("5"));
		assertTrue(helper.getRow(2).get(0).equals("6"));
		assertTrue(helper.getRow(3).get(0).equals("7"));
	}
	
	
	public void testBuilder() {
		final int C = 3;
		TableLayoutHelper helper = new TableLayoutHelper(zizi.iterator(),new NemMinusz(), new Hat(), C);
		
		System.out.println("<table>");
		for(Iterator rowI = helper.getRows().iterator(); rowI.hasNext(); ) {
			System.out.println("   <tr>");
			List row = (List)rowI.next();
			
			int cc = 0;
			for(Iterator colI = row.iterator(); colI.hasNext(); ++cc) {
				System.out.println("      <td><img src=\"" + colI.next() + ".jpg\"></td>");
			}
			for(;cc < C; ++cc) System.out.println("      <td/>");
			System.out.println("   </tr>");
			
			System.out.println("   <tr>");
			cc = 0;
			for(Iterator colI = row.iterator(); colI.hasNext(); ++cc) {
				System.out.println("      <td><a href=\"" + colI.next() + ".html\"></td>");
			}
			for(;cc < C; ++cc) System.out.println("      <td/>");
			System.out.println("   </tr>");
			
		}
		System.out.println("</table>");
	}

}
