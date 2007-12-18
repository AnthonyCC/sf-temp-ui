package com.freshdirect.cms.search;

import junit.framework.TestCase;

public class EditDistanceTest extends TestCase {

	
	public void testLevenshteinDistance() {
		String s1 = "kiskutya", s2 = "kismacska";
		
		EditDistanceCalculator LD = new EditDistanceCalculator.Levenshtein();
		
		int d1 = LD.calculate(s1, s2);
		
		System.out.println(LD.getTrace());
		
		int d2 = LD.calculate(s2, s1);
		
		System.out.println(LD.getTrace());
		
		assertEquals(d1, 5);
		assertEquals(d2, 5);
	}
	
	public void testLCSS() {
		String s1 = "kiskutya", s2 = "kismacska";
		
		EditDistanceCalculator LD = new EditDistanceCalculator.LCSS();
		
		int d1 = LD.calculate(s1, s2);
		
		System.out.println(LD.getTrace());
		
		int d2 = LD.calculate(s2, s1);
		
		System.out.println(LD.getTrace());
		
		assertEquals(d1, 3);
		assertEquals(d2, 3);
	}
	
	public void testModifiedLevenshtein() {
		String s1 = "12345678", s2 = "13245768";
		
		EditDistanceCalculator LD = new EditDistanceCalculator.ModifiedLevenshtein();
		
		int d1 = LD.calculate(s1, s2);
		
		System.out.println(LD.getTrace());
		
		int d2 = LD.calculate(s2, s1);
		
		System.out.println(LD.getTrace());
		
		assertEquals(d1, 2);
		assertEquals(d2, 2);
	}
}
