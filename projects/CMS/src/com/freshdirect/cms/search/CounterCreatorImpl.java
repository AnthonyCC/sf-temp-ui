package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.search.AutocompleteService.HitCounter;

public class CounterCreatorImpl implements CounterCreatorI {

	public void createCounters(HashMap<String, HitCounter> counters, String fullname) {
        fullname = fullname.toLowerCase().replace('&', ' ').replace('"', ' ').replace('.', ' ').replace(':', ' ').replace(',', ' ').replace('-', ' ')
        .replace('(', ' ').replace(')', ' ').replace(/* NBSP */(char)160, ' ').replace(/* REG TRADEMARK */ (char) 174, ' ');
        List<String> strings = filterWords(StringUtils.split(fullname));

        final int len = strings.size();
        for (int j = 0; j < len; j++) {
            String word = (String) strings.get(j);
            HitCounter prefix = accumulate(counters, word, 1, null);
            if (j + 1 < len) {
                String word2 = (String) strings.get(j + 1);
                prefix = accumulate(counters, word + ' ' + word2, 2, prefix);
                if (j + 2 < len) {
                    String word3 = (String) strings.get(j + 2);
                    accumulate(counters, word + ' ' + word2 + ' ' + word3, 3, prefix);
                }
            }
        }
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

    private List<String> filterWords(String[] words) {
        List<String> result = new ArrayList<String> (words.length);
        for (int i = 0; i < words.length; i++) {
            if (!AutocompleteService.skipWordsInAutoCompletion.contains(words[i])) {
                result.add(words[i]);
            }
        }
        return result;
    }

	@Override
	public TreeSet<HitCounter> initWords(Collection<String> words) {
        HashMap<String, HitCounter> counters = new HashMap<String, HitCounter>();
        for (String fullname : words) {
            if (fullname != null) {

                createCounters(counters, fullname);

                char[] buf = fullname.toCharArray();
                String unaccented = ISOLatin1AccentFilter.removeAccents(buf, buf.length);
                // String.replace returns the same object if no change occured, so == is good for comparison 
                if (!unaccented.equals(fullname)) {
                    createCounters(counters, unaccented);
                }
            }
        }
        TreeSet<HitCounter> set = new TreeSet<HitCounter>();
        for (HitCounter hc : counters.values()) {
            if (!(hc.followCount == 1 && hc.wordCount < 3)) {
                set.add(hc);
            }
        }
		return set;
	}
}
