package com.freshdirect.framework.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

public class DiscreteRandomSamplerTestCase extends TestCase {
	
	private static int MAX = 100000;
	
	public void testDistro() {
		Random R = new Random(System.currentTimeMillis());
		
		DiscreteRandomSampler DS = new DiscreteRandomSampler();
		
		DS.addValue("a",2);
		DS.addValue("b",3);
		DS.addValue("c",4);
		DS.addValue("d",1);
		DS.addValue("e",7);
		DS.addValue("f",8);
		
		Map outcomes = new HashMap();	
		for(int i=0; i< MAX; ++i) {
			Object value = DS.getRandomValue(R);
			
			Long freq = (Long)outcomes.get(value);
			if (freq == null) freq = new Long(1);
			else freq = new Long(freq.longValue() + 1);
			outcomes.put(value,freq);
		}
	
		for(Iterator i = outcomes.keySet().iterator(); i.hasNext(); ) {
			Object value = i.next();
			long tf = (((Long)outcomes.get(value)).longValue()*DS.getTotal()) /
				DS.getFrequency(value);

			// A Bold assertion: assert that the true frequence (tf) is within 10% of the expected
			// distribution frequency !
			// For this to work, leave MAX big!
			assertTrue(Math.abs(tf - MAX) < MAX/10 );
			
			// comment out below to see the actual accuracy
			System.out.println("value: " + value + ", generated: " + outcomes.get(value) + 
					" expected: " + (DS.getFrequency(value) * MAX/ DS.getTotal()) +
					" error: " + 100.0*(double)Math.abs(tf - MAX)/(double)MAX + '%' );
		}
	}
	
	
	public void testZeroFrequencies() {
		
		DiscreteRandomSampler DS = new DiscreteRandomSampler();
		
		DS.addValue("mokus",2);
		DS.addValue("roka",1);
		DS.addValue("kacsa",5);
		DS.addValue("mokus",-2);
		DS.addValue("roka",-1);
		DS.addValue("roka",3);
		DS.setValue("kacsa", 0);
		DS.addValue("csirke",5);
		
		
		
		
		assertTrue(DS.getFrequency("mokus") == 0);
		assertTrue(DS.getFrequency("roka") == 3);
		assertTrue(DS.getFrequency("kacsa") == 0);
		assertTrue(DS.getFrequency("csirke") == 5);
		assertTrue(DS.getTotal() == 8);
		
		Random R = new Random();
		
		int mokus = 0, roka = 0, csirke = 0, kacsa = 0;
		
		for(int i=0; i< MAX; ++i) {
			Object result = DS.getRandomValue(R);
			
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

}
