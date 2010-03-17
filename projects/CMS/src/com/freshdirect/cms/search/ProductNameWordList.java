package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.AutocompleteService.HitCounter;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ProductNameWordList implements WordListI{
    private final static Logger        LOGGER   = LoggerFactory.getInstance(ContentSearch.class);
    private List<String> words = null;
	private TreeSet<String> wordSet;
    private SortedSet<HitCounter> prefixSet;
    
    private SortedSet<HitCounter> initWords(Collection<String> words) {
        LOGGER.info("word list size:" + words.size());
        HashMap<String, HitCounter> counters = new HashMap<String, HitCounter>();
        for (String fullname : words) {
            if (fullname != null) {
                fullname = fullname.toLowerCase().replace('&', ' ').replace('"', ' ').replace('.', ' ').replace(':', ' ').replace(',', ' ').replace('-', ' ')
                        .replace('(', ' ').replace(')', ' ').replace(/* NBSP */(char)160, ' ').replace(/* REG TRADEMARK */ (char) 174, ' ');

                createCounters(counters, fullname);

                char[] buf = fullname.toCharArray();
                String unaccented = ISOLatin1AccentFilter.removeAccents(buf, buf.length);
                // String.replace returns the same object if no change occured, so == is good for comparison 
                if (!unaccented.equals(fullname)) {
                    createCounters(counters, unaccented);
                }
                
            }
        }
        LOGGER.info("prefix map size:" + counters.size());
        TreeSet<HitCounter> set = new TreeSet<HitCounter>();
        for (HitCounter hc : counters.values()) {
            // skip words which occures only one
            //if (hc.number > 1) {
                // skip autocomplete suggest
                if (!(hc.followCount == 1 && hc.wordCount < 3)) {
                    set.add(hc);
                }
            //}
        }
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

    private void createCounters(HashMap<String, HitCounter> counters, String fullname) {
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
            if (!skipWordsInAutoCompletion.contains(words[i])) {
                result.add(words[i]);
            }
        }
        return result;
    }

    private final static Set<String> skipWordsInAutoCompletion = new HashSet<String>();
    static {
        skipWordsInAutoCompletion.addAll(LuceneSearchService.stopWords);
        skipWordsInAutoCompletion.add("all");
        skipWordsInAutoCompletion.add("non");
        skipWordsInAutoCompletion.add("without");
    }

    
    
	private void generateWords() {
        LOGGER.info("createAutocompleteService");
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);
        LOGGER.info("contentKeysByType loaded :"+contentKeysByType.size());
        
        words = new ArrayList<String>(contentKeysByType.size());
        
        ContentFactory contentFactory = ContentFactory.getInstance();
        for (Iterator<ContentKey> keyIterator = contentKeysByType.iterator();keyIterator.hasNext();) {
            ContentKey key = keyIterator.next();
            ContentNodeModel nodeModel = contentFactory.getContentNodeByKey(key);
            if (nodeModel instanceof ProductModel) {
                ProductModel pm = (ProductModel) nodeModel;
                //if (pm.isDisplayableBasedOnCms()) {
                if (pm.isDisplayable()) {
                    words.add(pm.getFullName());
                }
            }
        }
        LOGGER.info("product names extracted:"+words.size());				
	}
	
	public List<String> getWords() {
		if(words == null) {
			generateWords();
		}
		return words;
	}
	
	public SortedSet<HitCounter> getPrefixSet() {
		if(prefixSet == null) {
			prefixSet = initWords(getWords());			
		}
		return prefixSet;
	}
}
