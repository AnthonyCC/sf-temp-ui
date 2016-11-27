package com.freshdirect.framework.util;

import junit.framework.TestCase;


import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class CartesianProductTestCase extends TestCase {
	
	
	private static Integer ONE = new Integer(1);
	private static Integer TWO = new Integer(2);
	private static Integer THREE = new Integer(3);

	private static List L1 = new ArrayList(Arrays.asList(new Integer[] {ONE, TWO, THREE}));
	private static List L2 = new ArrayList(Arrays.asList(new Integer[] {ONE, TWO}));
	private static List L3 = new ArrayList(Arrays.asList(new Integer[] {ONE, TWO}));

	
	
	private static Integer tupleValue(List tuple) {
		int numValue = 0;
		
		
		for(Iterator t = tuple.iterator(); t.hasNext(); ) {
			
			Integer number = (Integer)t.next();
			numValue += number.intValue();
			if (t.hasNext()) numValue *= 10;
		}
		return new Integer(numValue);
	}
	
	public void testIterator() {
		CartesianProduct CP = new CartesianProduct();
		CP.addSequence(L1);
		CP.addSequence(L2);
		CP.addSequence(L3);
		
		assertTrue(CP.size() == L1.size()*L2.size()*L3.size());
		
		Set numbers = new HashSet(CP.size());
		for(Iterator i = CP.iterator(); i.hasNext(); ) {
			List tuple = (List)i.next();
			Integer value = tupleValue(tuple);
			assertTrue(!numbers.contains(value));
			numbers.add(value);

		}
		assertTrue(numbers.size() == CP.size());

		
		numbers.clear();
		for(ListIterator i = CP.listIterator(CP.size() + 1); i.hasPrevious(); ) {
			List tuple = (List)i.previous();
			Integer value = tupleValue(tuple);
			assertTrue(!numbers.contains(value));
			numbers.add(value);
		}

		assertTrue(numbers.size() == CP.size());
		
	}
	
	public void testIndexing() {
		CartesianProduct CP = new CartesianProduct();
		CP.addSequence(L1);
		CP.addSequence(L2);
		CP.addSequence(L3);
		
		for(int i=0; i< L1.size()*L2.size()*L3.size(); ++i) {
			Object tuple = CP.get(i);
			assertTrue(CP.indexOf(tuple) == i);
		}
	}
	
	public void testCartesianCartesianProduct() {
		CartesianProduct CPIn = new CartesianProduct();
		CPIn.addSequence(L1);
		CPIn.addSequence(L2);
		CPIn.addSequence(L3);

		CartesianProduct CPOut = new CartesianProduct();
		CPOut.addSequence(CPIn);
		CPOut.addSequence(CPIn);
		CPOut.addSequence(CPIn);
		CPOut.addSequence(CPIn);
		CPOut.addSequence(CPIn);
		CPOut.addSequence(CPIn);
	
		assertTrue(CPOut.size() == 2985984);

		int count = 0;
		for(CartesianProduct.TupleIterator i= (CartesianProduct.TupleIterator)CPOut.iterator(); i.hasNext(); i.advance(), ++count);
		assertTrue(count == 2985984);
		
		List tuple = CPOut.getTuple(1367883);
		assertTrue(CPOut.indexOf(tuple) == 1367883);
		
		CartesianProduct CPHuge = new CartesianProduct();
		CPHuge.addSequence(CPOut);
		CPHuge.addSequence(CPIn);
		CPHuge.addSequence(CPIn);
		
		List htuple = CPHuge.getTuple(417901696);
		assertTrue(CPHuge.indexOf(htuple) == 417901696);
	}
	

}
