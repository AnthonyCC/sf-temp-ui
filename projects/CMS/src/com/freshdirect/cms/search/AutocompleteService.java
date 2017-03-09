package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.util.log.LoggerFactory;

public class AutocompleteService {

    public interface Predicate {
        boolean allow(String s);
    }
    
    private final static Logger   LOGGER                    = LoggerFactory.getInstance(AutocompleteService.class);

    private final static int MAX_AUTOCOMPLETE_HITS     = 20;
    
	final static Set<String> stopWords = new HashSet<String>(Arrays.asList(new String[] { "I", "a", "about", "an", "and", "are",
			"as", "at", "be", "by", "for", "from", "how", "in", "is", "it", "of", "on", "or", "that", "the", "this", "to", "was",
			"what", "when", "where", "who", "will", "with" }));

    final static Set<String> skipWordsInAutoCompletion = new HashSet<String>();
    static {
        skipWordsInAutoCompletion.addAll(stopWords);
        skipWordsInAutoCompletion.add("all");
        skipWordsInAutoCompletion.add("non");
        skipWordsInAutoCompletion.add("without");
    }
    
    public static Set<String> getStopWords(){
    	Set<ContentKey> stopwordsKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.STOPWORDS);
    	Set<String> stopwords = new HashSet<String>();
    	if(stopwordsKeys != null){
	    	for(final ContentKey key:stopwordsKeys){
	    		ContentNodeI contentNode = CmsManager.getInstance().getContentNode(key);
	    		stopwords.add((String)contentNode.getAttributeValue("word"));
	    	}
    	}
    	stopwords.addAll(skipWordsInAutoCompletion);
    	return stopwords;
    }

    SortedSet<HitCounter>         prefixSet;

    SortedSet<String>             wordSet;

    final SortedSet<String>             badSingularForms = new TreeSet<String>();
    
    private CounterCreatorI counterCreator = new CounterCreatorImpl();
    
    
    public AutocompleteService() {}

    public AutocompleteService(Collection<String> words) {
        prefixSet = initWords(words);
    }

    public List<String> getAutocompletions(String prefix) {
        LOGGER.debug("Autocomplete request for '" + prefix + "'");
        List<HitCounter> result = getAutocompletionHits(prefix);

        List<String> words = new ArrayList<String>(Math.min(result.size(), MAX_AUTOCOMPLETE_HITS));
        for (int i = 0; i < result.size() && i < MAX_AUTOCOMPLETE_HITS; i++) {
            words.add(result.get(i).prefix);
        }
        return words;
    }

    public List<String> getAutocompletions(String prefix, Predicate predicate) {
        List<HitCounter> result = getAutocompletionHits(prefix);
        List<String> words = new ArrayList<String>(result.size());
        for (int i = 0; i < result.size() && words.size() < MAX_AUTOCOMPLETE_HITS; i++) {
            String s = result.get(i).prefix;
            if (predicate.allow(s)) {
                words.add(s);
            }
        }
        return words;
        
    }
    
    List<HitCounter> getAutocompletionHits(String prefix) {
        HitCounter start = new HitCounter(prefix.toLowerCase(), 0, null);
        HitCounter end = new HitCounter(start.prefix + '\uffff', 0, null);

        SortedSet<HitCounter> subSet = prefixSet.subSet(start, end);
        // sort according to the number of occurances
        List<HitCounter> result = new ArrayList<HitCounter>(subSet);
        Collections.sort(result, new Comparator() {
            public int compare(Object o1, Object o2) {
                HitCounter h1 = (HitCounter) o1;
                HitCounter h2 = (HitCounter) o2;
                return h2.number - h1.number;
            }
        });
        return result;
    }


    public void setWordList(Collection<String> words) {
        prefixSet = initWords(words);
    }
    

    private SortedSet<HitCounter> initWords(Collection<String> words) {
        LOGGER.info("word list size:" + words.size());
        
        
        
        TreeSet<HitCounter> set = counterCreator.initWords( words );
        
        wordSet = new TreeSet<String>();
        for (HitCounter hc : set) {
            if (hc.wordCount==1) {
                wordSet.add(hc.prefix);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            for (HitCounter hc : set){
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
        return wordSet.contains(word) && !badSingularForms.contains(word);
    }

    
    
    
    public boolean addBadSingular(String e) {
        return badSingularForms.add(e);
    }

    public boolean addAllBadSingular(Collection<? extends String> c) {
        return badSingularForms.addAll(c);
    }

    public void clearBadSingular() {
        badSingularForms.clear();
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
    
    public void setCounterCreator(CounterCreatorI cc) {
    	this.counterCreator = cc;
    }

    static class HitCounter implements Comparable<HitCounter> {
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

        public int compareTo(HitCounter o) {
            return prefix.compareTo(o.prefix);
        }
        
        public String toString() {
            return prefix +'('+number+')';
        }
    }

}
