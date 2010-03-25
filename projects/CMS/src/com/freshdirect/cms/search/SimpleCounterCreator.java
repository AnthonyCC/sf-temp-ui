package com.freshdirect.cms.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

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
        fullname = fullname.toLowerCase().replace('&', ' ').replace('"', ' ').replace('.', ' ').replace(':', ' ').replace(',', ' ').replace('-', ' ')
        .replace('(', ' ').replace(')', ' ').replace(/* NBSP */(char)160, ' ').replace(/* REG TRADEMARK */ (char) 174, ' ');
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

	@Override
	public TreeSet<HitCounter> initWords(Collection<String> words) {
        HashMap<String, HitCounter> counters = new HashMap<String, HitCounter>();
        for (String fullname : words) {
            if (fullname != null) {

                createCounters(counters, fullname);

                char[] buf = fullname.toCharArray();
                String unaccented = ISOLatin1AccentFilter.removeAccents(buf, buf.length);
                if (!unaccented.equals(fullname)) {
                    createCounters(counters, unaccented);
                }
            }
        }
        TreeSet<HitCounter> set = new TreeSet<HitCounter>();
        for (HitCounter hc : counters.values()) {
        	set.add(hc);
        }
		return set;
	}

}
