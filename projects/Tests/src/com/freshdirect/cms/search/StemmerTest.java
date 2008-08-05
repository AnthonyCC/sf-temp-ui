package com.freshdirect.cms.search;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Token;

import junit.framework.TestCase;

public class StemmerTest extends TestCase {
	
	public void testPorterStemmer() throws IOException {
		
		String words = "cat cats X appl apples apple X cherry cherries X news new X bus buses X fish fishes";
		
		TokenStream stream = new PorterStemFilter(new LowerCaseTokenizer(new StringReader(words)));
		
		
		
		Token t;
		
		while((t = stream.next()) != null) {
			System.out.println(t.termText());
		}
	}

}
