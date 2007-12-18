package com.freshdirect.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;


import junit.framework.TestCase;

public class ListConcatenationTestCase extends TestCase {
	
	
	private static int MAX = 1000;
	private static int smallMAX = 30;
	
	
	
	private ListConcatenation simple() {
		ListConcatenation bundle = new ListConcatenation();
		
		bundle.add("a");
		bundle.addList(Arrays.asList(new String[] { "b", "c"}));
		bundle.add("d");
		bundle.addList(Arrays.asList(new String[] { "e", "f" }));
		bundle.add("g");
		return bundle;
	}
	
	private ListConcatenation random() {
		Random R = new Random();
		ListConcatenation bundle = new ListConcatenation();

		for(int c = 0; c < MAX; ) {
			if (R.nextBoolean()) {
				int n = Math.min(R.nextInt(MAX - c), smallMAX);
				List list = new ArrayList(n);
				for(int i = 0; i< n; ++i, ++c) list.add(new Integer(c));
				bundle.addList(list);
			} else bundle.add(new Integer(c++));
		}
		
		return bundle;
	}
	
	public void testIndexes1() {
		
		ListConcatenation bundle = simple();

		assertTrue(bundle.get(0).equals("a"));
		assertTrue(bundle.get(1).equals("b"));
		assertTrue(bundle.get(2).equals("c"));
		assertTrue(bundle.get(3).equals("d"));
		assertTrue(bundle.get(4).equals("e"));
		assertTrue(bundle.get(5).equals("f"));
		assertTrue(bundle.get(6).equals("g"));
	}
	
	
	public void testIndexes2() {
		ListConcatenation bundle = simple();
		
		assertTrue(bundle.indexOf("a") == 0); assertTrue(bundle.lastIndexOf("a") == 0);
		assertTrue(bundle.indexOf("b") == 1); assertTrue(bundle.lastIndexOf("b") == 1);
		assertTrue(bundle.indexOf("c") == 2); assertTrue(bundle.lastIndexOf("c") == 2);
		assertTrue(bundle.indexOf("d") == 3); assertTrue(bundle.lastIndexOf("d") == 3);
		assertTrue(bundle.indexOf("e") == 4); assertTrue(bundle.lastIndexOf("e") == 4);
		assertTrue(bundle.indexOf("f") == 5); assertTrue(bundle.lastIndexOf("f") == 5);
		assertTrue(bundle.indexOf("g") == 6); assertTrue(bundle.lastIndexOf("g") == 6);

	}
	
	
	public void testIterators() {
		ListConcatenation bundle = random();
		
		assertTrue(bundle.size() == MAX);
		
		int c = 0;
		for(ListIterator i = bundle.listIterator(); i.hasNext(); ++c) {
			assertTrue(((Integer)i.next()).intValue() == c);
		}
		
		assertTrue(c == MAX);
	
		--c;
		for(ListIterator i = bundle.listIterator(bundle.size()); i.hasPrevious(); --c) {
			assertTrue(((Integer)i.previous()).intValue() == c);
		}
		
		assertTrue(c == -1);
	}

}
