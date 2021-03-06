/**
 * 
 */
package com.freshdirect.cms.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.freshdirect.TestUtils;

import junit.framework.TestCase;

public class AutocompleteTest extends TestCase {

    private static final Collection<String> WORDS =  Arrays.asList(new String[] {
            "blueberry triangle", "blueberry soup", "bluecheese pie", "apple cream", "apple pine", "apple pie", "borring pig",
            "reddi wip", "baby wipes", "basic wipe"
    });
    
    private AutocompleteService service;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
    	TestUtils.initCmsManagerFromXmls("classpath:/com/freshdirect/cms/fdstore/content/FeaturedProducts.xml");
    	service = new AutocompleteService (WORDS); 
   		service.addBadSingular("wip");
    }


    /**
     * Test method for {@link com.freshdirect.cms.search.AutocompleteService#getAutocompletions(java.lang.String)}.
     */
    public void testGetAutocompletions() {
        // test to get everything which starts with blue in a correct order
        List<String> autocompletions = service.getAutocompletions("blue");
        test("blue-autocomplete", autocompletions, new String[] {"blueberry", "blueberry soup", "blueberry triangle", "bluecheese pie"});
        // test to get everything which starts with 'bluec' in this case, 'bluecheese' shouldn't be returned, because there is only 'bluecheese pie'
        autocompletions = service.getAutocompletions("bluec");
        test("bluecheese-autocomplete", autocompletions, new String[] {"bluecheese pie"});

        // autocomplete should work with words in the middle 
        autocompletions = service.getAutocompletions("pi");
        test("pi-autocomplete", autocompletions, new String[] {"pie", "pig", "pine"});
    }

    private void test(String name, List<String> autocompletions, String[] strings) {
        assertNotNull("list:"+name+" not null",autocompletions);
        assertEquals("list:"+name+" size",strings.length, autocompletions.size());
        for (int i=0;i<strings.length;i++) {
            assertTrue("on "+name+" list found "+strings[i], autocompletions.contains(strings[i]));
        }
        for (int i=0;i<strings.length;i++) {
            assertEquals("on "+name+" list at "+i+" is "+strings[i], strings[i], autocompletions.get(i));
        }
    }


    /**
     * Test method for {@link com.freshdirect.cms.search.AutocompleteService#isValidWord(java.lang.String)}.
     */
    public void testIsValidWord() {
        assertTrue("apple is valid", service.isValidWord("apple"));
        assertTrue("pie is valid", service.isValidWord("pie"));
        assertFalse("pidgin is not valid", service.isValidWord("pidgin"));
        assertFalse("wip is not valid", service.isValidWord("wip"));
        assertTrue("wipe is valid", service.isValidWord("wipe"));
    }

    /**
     * Test method for {@link com.freshdirect.cms.search.AutocompleteService#removePlural(java.lang.String)}.
     */
    public void testRemovePlural() {
        assertEquals("handle apples", "apple", service.removePlural("apples"));
        assertEquals("handle hummus", "hummus", service.removePlural("hummus"));
        assertEquals("handle blueberries", "blueberry", service.removePlural("blueberries"));
        assertEquals("handle pines", "pine", service.removePlural("pines"));
        assertEquals("handle wipe", "wipe", service.removePlural("wipe"));
        assertEquals("handle wipes", "wipe", service.removePlural("wipes"));
    }

}
