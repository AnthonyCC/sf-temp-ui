package com.freshdirect.cms.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Token;

import junit.framework.TestCase;

public class StemmerTest extends TestCase {
	
	public void testPorterStemmer() throws IOException {
		
		// fails on [bus,buses] !
		String words = "cat cats X apples apple X cherry cherries X news new X fish fishes";
		
		TokenStream stream = new PorterStemFilter(new LowerCaseTokenizer(new StringReader(words)));
		
		
		
		Token t;
		
		Set S = new HashSet();
		
		while(stream.incrementToken()) {
			String term = ts.getAttribute(TermAttribute.class).term();
			if ("x".equals(term)) {
				S.clear();
				continue;
			}
			S.add(term);
			assertTrue(S.size() == 1);
		}
	}

}
