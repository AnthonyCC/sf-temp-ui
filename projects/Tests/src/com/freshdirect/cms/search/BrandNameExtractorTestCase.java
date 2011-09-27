package com.freshdirect.cms.search;

import java.util.List;

import junit.framework.TestCase;

public class BrandNameExtractorTestCase extends TestCase {

	public void testExtractor() {
		
		BrandNameExtractor extractor = new BrandNameExtractor();
		
		String 
			s1 = "I love A&W root beer",
			s2 = "I hate A & W root beer",
			s3 = "A &W or A. W or A - W";
		
		List result1 = extractor.extract(s1);
		List result2 = extractor.extract(s2);
		List result3 = extractor.extract(s3);
		
		assertEquals("A&W",result1.get(0));
		assertEquals("A & W",result2.get(0));
		assertTrue(result3.contains("A &W"));
		assertTrue(result3.contains("A. W"));
		assertTrue(result3.contains("A - W"));
	}
}
