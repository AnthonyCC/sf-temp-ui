package com.freshdirect.fdstore.content;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.LowerCaseTokenizer;
import org.apache.lucene.analysis.PorterStemFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import com.freshdirect.cms.search.ISOLatin1AccentFilter;

public interface SearchQueryStemmer {
	
	public String stemToken(String s);
	
	public final static SearchQueryStemmer Porter = new SearchQueryStemmer() {

		public String stemToken(String s) {
		    TokenStream ts =  new PorterStemFilter(new ISOLatin1AccentFilter(new LowerCaseTokenizer(new StringReader(s))));
		    try {
		        ts.incrementToken();
                        } catch (IOException e) {
                            return LowerCase.stemToken(s);
                        }
		    return ts.getAttribute(TermAttribute.class).term();
//			PorterStemFilter stemFilter = new PorterStemFilter(new ISOLatin1AccentFilter(new LowerCaseTokenizer(new StringReader(s))));
//			try {
//				Token t = stemFilter.incrementToken();
//				return t.termText();
//			} catch (Exception e) {
//				return LowerCase.stemToken(s);
//			}
		}

	};
	
	public final static SearchQueryStemmer LowerCase = new SearchQueryStemmer() {

		public String stemToken(String s) {
			return s.toLowerCase();
		}
		
	};
	
	
}
