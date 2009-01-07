/**
 * 
 */
package com.freshdirect.cms.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author zsombor
 *
 */
public class AutocompleteTest extends TestCase {

    Collection words =  Arrays.asList(new String[] {
            "blueberry triangle", "blueberry soup", "bluecheese pie", "apple cream", "apple pine", "apple pie", "borring pig"
    }); 
    AutocompleteService service = new AutocompleteService (words); 
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
    }


    /**
     * Test method for {@link com.freshdirect.cms.search.AutocompleteService#getAutocompletions(java.lang.String)}.
     */
    public void testGetAutocompletions() {
        // test to get everything which starts with blue in a correct order
        List autocompletions = service.getAutocompletions("blue");
        test("blue-autocomplete", autocompletions, new String[] {"blueberry", "blueberry soup", "blueberry triangle", "bluecheese pie"});
        // test to get everything which starts with 'bluec' in this case, 'bluecheese' shouldn't be returned, because there is only 'bluecheese pie'
        autocompletions = service.getAutocompletions("bluec");
        test("bluecheese-autocomplete", autocompletions, new String[] {"bluecheese pie"});

        // autocomplete should work with words in the middle 
        autocompletions = service.getAutocompletions("pi");
        test("pi-autocomplete", autocompletions, new String[] {"pie", "pig", "pine"});
    }

    private void test(String name, List autocompletions, String[] strings) {
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
    }

    /**
     * Test method for {@link com.freshdirect.cms.search.AutocompleteService#removePlural(java.lang.String)}.
     */
    public void testRemovePlural() {
        assertEquals("handle apples", "apple", service.removePlural("apples"));
        assertEquals("handle hummus", "hummus", service.removePlural("hummus"));
        assertEquals("handle blueberries", "blueberry", service.removePlural("blueberries"));
        assertEquals("handle pines", "pine", service.removePlural("pines"));
    }

}
