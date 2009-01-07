package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

public class AutocompleteService {

    private final Category   LOGGER                    = LoggerFactory.getInstance(AutocompleteService.class);

    private final static int MAX_AUTOCOMPLETE_HITS     = 20;

    private final static Set skipWordsInAutoCompletion = new HashSet();
    static {
        skipWordsInAutoCompletion.addAll(LuceneSearchService.stopWords);
        skipWordsInAutoCompletion.add("all");
        skipWordsInAutoCompletion.add("non");
        skipWordsInAutoCompletion.add("without");
    }

    SortedSet                prefixSet;

    SortedSet               wordSet;
    
    public AutocompleteService() {

    }

    public AutocompleteService(Collection words) {
        prefixSet = initWords(words);
    }

    public List getAutocompletions(String prefix) {
        HitCounter start = new HitCounter(prefix.toLowerCase(), 0, null);
        HitCounter end = new HitCounter(start.prefix + '\uffff', 0, null);

        SortedSet subSet = prefixSet.subSet(start, end);
        // sort according to the number of occurances
        List result = new ArrayList(subSet);
        Collections.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                HitCounter h1 = (HitCounter) o1;
                HitCounter h2 = (HitCounter) o2;
                return h2.number - h1.number;
            }
        });

        List words = new ArrayList(result.size());
        for (int i = 0; i < result.size() && i < MAX_AUTOCOMPLETE_HITS; i++) {
            words.add(((HitCounter) result.get(i)).prefix);
        }
        return words;
    }

    private List filterWords(String[] words) {
        List result = new ArrayList(words.length);
        for (int i = 0; i < words.length; i++) {
            if (!skipWordsInAutoCompletion.contains(words[i])) {
                result.add(words[i]);
            }
        }
        return result;
    }

    public void setWordList(Collection words) {
        prefixSet = initWords(words);
    }

    private SortedSet initWords(Collection words) {
        LOGGER.info("word list size:" + words.size());
        HashMap counters = new HashMap();
        for (Iterator iter = words.iterator(); iter.hasNext();) {
            String fullname = (String) iter.next();
            if (fullname != null) {
                fullname = fullname.toLowerCase().replace('&', ' ').replace('"', ' ').replace('.', ' ').replace(':', ' ').replace(',', ' ').replace('-', ' ')
                        .replace('(', ' ').replace(')', ' ').replace(/* NBSP */(char)160, ' ').replace(/* REG TRADEMARK */ (char) 174, ' ');

                createCounters(counters, fullname);

                String unaccented = ISOLatin1AccentFilter.removeAccents(fullname);
                // String.replace returns the same object if no change occured, so == is good for comparison 
                if (!unaccented.equals(fullname)) {
                    createCounters(counters, unaccented);
                }
                
            }
        }
        LOGGER.info("prefix map size:" + counters.size());
        TreeSet set = new TreeSet();
        for (Iterator iter = counters.values().iterator(); iter.hasNext();) {
            HitCounter hc = (HitCounter) iter.next();
            // skip words which occures only one
            //if (hc.number > 1) {
                // skip autocomplete suggest
                if (!(hc.followCount == 1 && hc.wordCount < 3)) {
                    set.add(hc);
                }
            //}
        }
        wordSet = new TreeSet();
        for (Iterator iter = set.iterator(); iter.hasNext(); ){
            HitCounter hc = (HitCounter) iter.next();
            if (hc.wordCount==1) {
                wordSet.add(hc.prefix);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            for (Iterator iter = set.iterator(); iter.hasNext(); ){
                HitCounter hc = (HitCounter) iter.next();
                if (hc.wordCount==1) {
                    if (hc.prefix.endsWith("s")) {
                        String wordWithoutS = hc.prefix.substring(0, hc.prefix.length()-1);
                        LOGGER.debug("word : "+hc.prefix + " -> "+wordWithoutS + " valid:"+wordSet.contains(wordWithoutS));
                    }
                }
            }
        }
        return set;
    }

    /**
     * Check that the given, lower case word is in the word list, anywhere.
     * @param word
     * @return
     */
    public boolean isValidWord(String word) {
        return wordSet.contains(word);
    }
    
    public String removePlural(String word) {
        if (word.length()>2 && word.endsWith("s")) {
            if (word.endsWith("ies")) {
                // handle strawberries -> strawberry
                String singular = word.substring(0, word.length()-3)+'y';
                if (isValidWord(singular)) {
                    return singular;
                }
            }
            if (word.endsWith("es")) {
                String singular = word.substring(0, word.length()-2);
                if (isValidWord(singular)) {
                    return singular;
                }
            }
            String wordWithoutS = word.substring(0, word.length()-1);
            if (isValidWord(wordWithoutS)) {
                return wordWithoutS;
            }
        }
        return word;
    }
    
    

    private void createCounters(HashMap counters, String fullname) {
        List strings = filterWords(StringUtils.split(fullname));

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

    private HitCounter accumulate(HashMap counters, String str, int wordCount, HitCounter before) {
        HitCounter hc = (HitCounter) counters.get(str);
        if (hc == null) {
            hc = new HitCounter(str, wordCount, before);
            counters.put(str, hc);
        } else {
            hc.inc();
        }
        return hc;
    }


    static class HitCounter implements Comparable {
        String     prefix;

        // the number of occurences of the prefix word in the whole database.
        int        number = 1;
        // number of words in the prefix
        byte       wordCount;

        // the number of distinct word which follows this prefix
        int        followCount;

        HitCounter beforePrefix;

        public HitCounter(String prefix, int wordCount, HitCounter beforePrefix) {
            this.prefix = prefix;
            this.wordCount = (byte) wordCount;
            this.beforePrefix = beforePrefix;
            if (beforePrefix != null) {
                beforePrefix.followCount++;
            }
        }

        void inc() {
            number++;
        }

        public int compareTo(Object o) {
            return prefix.compareTo(((HitCounter) o).prefix);
        }
        
        public String toString() {
            return prefix +'('+number+')';
        }
    }

}
