package com.freshdirect.framework.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

public class DiscreteRandomSamplerTestCase extends TestCase {
	
	private static int MAX = 100000;
	
	public void testDistro() {
		Random R = new Random(System.currentTimeMillis());
		
		DiscreteRandomSamplerWithReplacement DS = new DiscreteRandomSamplerWithReplacement();
		
		DS.addValue("a",2);
		DS.addValue("b",3);
		DS.addValue("c",4);
		DS.addValue("d",1);
		DS.addValue("e",7);
		DS.addValue("f",8);
		
		Map outcomes = new HashMap();	
		for(int i=0; i< MAX; ++i) {
			Object value = DS.getRandomItem(R);
			
			Long freq = (Long)outcomes.get(value);
			if (freq == null) freq = new Long(1);
			else freq = new Long(freq.longValue() + 1);
			outcomes.put(value,freq);
		}
	
		for(Iterator i = outcomes.keySet().iterator(); i.hasNext(); ) {
			Object value = i.next();
			long tf = (((Long)outcomes.get(value)).longValue()*DS.getTotalFrequency()) /
				DS.getItemFrequency(value);

			// A Bold assertion: assert that the true frequence (tf) is within 10% of the expected
			// distribution frequency !
			// For this to work, leave MAX big!
			assertTrue(Math.abs(tf - MAX) < MAX/10 );
			
			// comment out below to see the actual accuracy
			System.out.println("value: " + value + ", generated: " + outcomes.get(value) + 
					" expected: " + (DS.getItemFrequency(value) * MAX/ DS.getTotalFrequency()) +
					" error: " + 100.0*(double)Math.abs(tf - MAX)/(double)MAX + '%' );
		}
	}
	
	
	public void testZeroFrequencies() {
		
		DiscreteRandomSamplerWithReplacement DS = new DiscreteRandomSamplerWithReplacement();
		
		DS.addValue("mokus",2);
		DS.addValue("roka",1);
		DS.addValue("kacsa",5);
		DS.addValue("mokus",-2);
		DS.addValue("roka",-1);
		DS.addValue("roka",3);
		DS.setItemFrequency("kacsa", 0);
		DS.addValue("csirke",5);
		
		
		
		
		assertTrue(DS.getItemFrequency("mokus") == 0);
		assertTrue(DS.getItemFrequency("roka") == 3);
		assertTrue(DS.getItemFrequency("kacsa") == 0);
		assertTrue(DS.getItemFrequency("csirke") == 5);
		assertTrue(DS.getTotalFrequency() == 8);
		
		Random R = new Random();
		
		int mokus = 0, roka = 0, csirke = 0, kacsa = 0;
		
		for(int i=0; i< MAX; ++i) {
			Object result = DS.getRandomItem(R);
			
			if ("mokus".equals(result)) ++mokus;
			else if ("roka".equals(result)) ++roka;
			else if ("csirke".equals(result)) ++csirke;
			else if ("kacsa".equals(result)) ++kacsa;
		}
		
		assertTrue(kacsa == 0);
		assertTrue(mokus == 0);
		
		// another bold assumption; the relative frequencies of csirke and roka
		// are within 10% of the true ratio. Leave MAX big for this
		double error = Math.abs((100.0*(double)csirke)/(double)roka - 500.0/3.0);
		assertTrue(error < 10); 
		
		// Comment out below to see the real error
		System.out.println("Error: " + error + '%');
	}
	
	public void testOneValue() {	
		DiscreteRandomSamplerWithReplacement DS = new DiscreteRandomSamplerWithReplacement(new Comparator() {

			public int compare(Object arg0, Object arg1) {
				return arg0.toString().compareTo(arg1.toString());
			}
			
		});
		Object csupo = new Object() {
			public String toString() {
				return "csupo";
			}
		};
		
		DS.addValue(csupo, 10);
		
		Random R = new Random();
		for(int i=0; i< MAX; ++i) {
			Object result = DS.getRandomItem(R);
			assertTrue(result == csupo);
		}
	}
	
	public void testWithoutReplacement() {

		Random R = new Random();
		DiscreteRandomSamplerWithoutReplacement sampler = 
			new DiscreteRandomSamplerWithoutReplacement(new Comparator() {

				public int compare(Object o1, Object o2) {
					return o1.toString().compareTo(o2.toString());
				} 
			}
		);		
		
		String[] labels = 
			new String[] {
				"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
				"k", "l", "m", "n", "o", "p", "q", "r", "s", "t" };
		
		Arrays.sort(labels);
		
		int[][] counts = new int[labels.length][labels.length];
		
		for(int i = 0; i< 10000; ++i) {
			
			for(int j = labels.length-1, f = 1; j >= 0; --j, f *= 2) {
				sampler.setItemFrequency(labels[j], f);
			}
			
			int j = 0;
			while(sampler.getItemCount() > 0) {
				Object item = sampler.removeRandomValue(R);
				
				int c = Arrays.binarySearch(labels, item);
				++counts[c][j];
				++j;
			}
		}
		
		for(int i = 0; i < labels.length; ++i) {
			System.out.print(labels[i] + ": ");
			for(int j = 0; j < labels.length; ++j) {
				if (j > 0) System.out.print(',');
				System.out.print(counts[i][j]);
			}
			System.out.println();
		}		
	}

}
