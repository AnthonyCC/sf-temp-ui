package com.freshdirect.cms.search;

import java.util.HashMap;

import com.freshdirect.cms.search.AutocompleteService.HitCounter;

/**
 * @author robi
 * 
 * SimpleCounterCreator just dumps the text in the autocomplete's hashmap. 
 * Use only when the you don't need break up the string into words
 *
 */
public class SimpleCounterCreator implements CounterCreatorI {

	public void createCounters(HashMap<String, HitCounter> counters, String fullname) {
        accumulate(counters, fullname, 1, null);
    }

    private HitCounter accumulate(HashMap<String, HitCounter> counters, String str, int wordCount, HitCounter before) {
        HitCounter hc = (HitCounter) counters.get(str);
        if (hc == null) {
            hc = new HitCounter(str, wordCount, before);
            counters.put(str, hc);
        } else {
            hc.inc();
        }
        return hc;
    }

}
