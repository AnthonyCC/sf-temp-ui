package com.freshdirect.cms.search;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class StemmerTest extends TestCase {
	public void testStemmer() throws IOException {
		Reader reader = new StringReader("stirfry stir fry stir fried stir fries stirfries stirfried skies skis sky skied"); 
		WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(reader);
		TokenFilter stemmer = new SnowballFilter(tokenizer, "English");
		TermAttribute termAttr = stemmer.getAttribute(TermAttribute.class);
		List<String> tokens = new ArrayList<String>();
		while(stemmer.incrementToken())
			tokens.add(termAttr.term());
		List<String> expected = new ArrayList<String>();
		expected.add("stirfri");
		expected.add("stir");
		expected.add("fri");
		expected.add("stir");
		expected.add("fri");
		expected.add("stir");
		expected.add("fri");
		expected.add("stirfri");
		expected.add("stirfri");
		expected.add("sky");
		expected.add("ski");
		expected.add("sky");
		expected.add("ski");
		assertEquals(expected, tokens);
	}
}
