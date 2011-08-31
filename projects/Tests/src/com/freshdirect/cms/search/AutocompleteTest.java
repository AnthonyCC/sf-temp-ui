/**
 * 
 */
package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.search.term.Term;

import junit.framework.TestCase;

/**
 * @author zsombor
 * 
 */
public class AutocompleteTest extends TestCase {
	Collection<String> words = Arrays.asList(new String[] { "blueberry triangle", "blueberry soup", "bluecheese pie",
			"apple cream", "apple pine", "apple pie", "borring pig", "reddi wip", "baby wipes", "basic wipe" });
	AutocompleteService service;
	{
		List<AutocompleteTerm> terms = new ArrayList<AutocompleteTerm>();
		for (String word : words)
			terms.add(new AutocompleteTerm(new Term(word), new ContentKey(ContentType.get("TestWord"), word)));
		service = new AutocompleteService(terms, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
	}

	/**
	 * Test method for {@link com.freshdirect.cms.search.AutocompleteService#getAutocompletions(java.lang.String)}.
	 */
	public void testGetAutocompletions() {
		// test to get everything which starts with blue in a correct order
		List<String> autocompletions = service.getAutocompletions("blue", 10);
		test("blue-autocomplete", autocompletions, new String[] { "blueberry", "bluecheese pie", "blueberry soup", "blueberry triangle" });
		// test to get everything which starts with 'bluec' in this case, 'bluecheese' shouldn't be returned, because there is only
		// 'bluecheese pie'
		autocompletions = service.getAutocompletions("bluec", 10);
		test("bluecheese-autocomplete", autocompletions, new String[] { "bluecheese pie" });

		// autocomplete should work with words in the middle
		autocompletions = service.getAutocompletions("pi", 10);
		test("pi-autocomplete", autocompletions, new String[] { "pie", "pig", "pine" });
	}

	private void test(String name, List<String> autocompletions, String[] strings) {
		assertNotNull("list:" + name + " not null", autocompletions);
		assertEquals("list:" + name + " size", strings.length, autocompletions.size());
		for (int i = 0; i < strings.length; i++) {
			assertTrue("on " + name + " list found " + strings[i], autocompletions.contains(strings[i]));
		}
		for (int i = 0; i < strings.length; i++) {
			assertEquals("on " + name + " list at " + i + " is " + strings[i], strings[i], autocompletions.get(i));
		}
	}
}
