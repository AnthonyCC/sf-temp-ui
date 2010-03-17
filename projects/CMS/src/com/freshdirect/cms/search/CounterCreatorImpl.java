package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.search.AutocompleteService.HitCounter;

public class CounterCreatorImpl implements CounterCreatorI {

	public void createCounters(HashMap<String, HitCounter> counters, String fullname) {
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
    
}
