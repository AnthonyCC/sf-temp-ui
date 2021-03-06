package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrandNameExtractor {

	private static Pattern ampercentPattern = Pattern.compile("\\w+\\s*&\\s*\\w+");
	private static Pattern dashPattern = Pattern.compile("\\w+\\s*-\\s*\\w+");
	private static Pattern dotPattern = Pattern.compile("\\w+\\s*\\.\\s*\\w+");
	private static Pattern apostrophePattern = Pattern.compile("\\w+\\s*\'\\s*\\w+");

	private List<Pattern> patterns = new ArrayList<Pattern>();

	public BrandNameExtractor() {
		patterns.add(ampercentPattern);
		patterns.add(dashPattern);
		patterns.add(dotPattern);
		patterns.add(apostrophePattern);
	}

	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
	}

	public void addPattern(String pattern) {
		patterns.add(Pattern.compile(pattern));
	}
	
	public List<CharSequence> extract(CharSequence string) {		
		
		List<CharSequence> result = new ArrayList<CharSequence>();
		for ( Pattern p : patterns ) {
			Matcher m = p.matcher(string);
			while (m.find()) {
				result.add(string.subSequence(m.start(), m.end()).toString());
			}
		}
		return result;
	}

}
