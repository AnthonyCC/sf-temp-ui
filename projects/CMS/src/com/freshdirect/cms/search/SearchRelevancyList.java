package com.freshdirect.cms.search;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNodeUtil;

public class SearchRelevancyList {

    public static final String SEARCH_RELEVANCY_KEY = "FDFolder:searchRelevancyList";
    public static final String WORD_STEMMING_EXCEPTION = "FDFolder:wordStemmingException";

    public static final String KEYWORDS = "Keywords";
    public static final String HINTS = "categoryHints";

    private static final String SCORE = "score";

    private static final String CATEGORY = "category";
    
    private static final String WORD = "word";
    
    
    
    /**
     * List<String>
     */
    String[]                   keywords;

    /**
     * Map<ContentKey,Integer>
     */
    Map<ContentKey,Integer>     categoryScoreMap;

    public SearchRelevancyList(String[] keywords, Map<ContentKey,Integer> categoryScoreMap) {
        super();
        this.keywords = keywords;
        this.categoryScoreMap = categoryScoreMap;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public Map<ContentKey,Integer> getCategoryScoreMap() {
        return categoryScoreMap;
    }

    /**
     * 
     * @return Map<String,SearchRelevancyList>
     */
    public static Map<String, SearchRelevancyList> createFromCms() {
        Map<String, SearchRelevancyList> result = new HashMap<String, SearchRelevancyList>();

        CmsManager instance = CmsManager.getInstance();

        ContentNodeI searchRelRootNode = instance.getContentNode(ContentKey.decode(SearchRelevancyList.SEARCH_RELEVANCY_KEY));
        if (searchRelRootNode!=null) {
            Set<ContentKey> searchRelKeys = ContentNodeUtil.collectReachableKeys(searchRelRootNode, FDContentTypes.SEARCH_RELEVANCY_LIST);
            Map<ContentKey, ContentNodeI> searchRelNodes = instance.getContentNodes(searchRelKeys);
    
            for (Iterator<ContentNodeI> contentNodeIterator = searchRelNodes.values().iterator(); contentNodeIterator.hasNext();) {
                ContentNodeI node = contentNodeIterator.next();
                String keywords = ContentNodeUtil.getStringAttribute(node, KEYWORDS).toLowerCase();
                String[] kw = StringUtils.split(keywords, ",");
                if (kw.length>0) {
                    List<ContentKey> hints = (List<ContentKey>) node.getAttribute(HINTS).getValue();
                    if (hints != null) {
                        Map<ContentKey, Integer> scores = new HashMap<ContentKey, Integer>();
                        for (int i = 0; i < hints.size(); i++) {
                            ContentKey key = hints.get(i);
                            ContentNodeI hint = instance.getContentNode(key);
                            
                            Integer score = ContentNodeUtil.getIntegerAttribute(hint, SCORE);
                            ContentKey categoryKey = ContentNodeUtil.getContentKeyAttribute(hint, CATEGORY);
                            if (score!=null && categoryKey!=null) {
                                scores.put(categoryKey, score);
                            }
                        }
                        if (!scores.isEmpty()) {
                            SearchRelevancyList srl = new SearchRelevancyList(kw, scores);
                            for (int i=0;i<kw.length;i++) {
                                result.put(kw[i].trim(), srl);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    
    /**
     * Return a collection of string from the cms, which is under FDFolder:searchRelevancyList, and their type is WORD_PLURALIZATION_EXCEPTION
     *    
     * @return
     */
    public static Collection<String> getBadPluralFormsFromCms() {
        Set<String> result = new HashSet<String>();
        CmsManager instance = CmsManager.getInstance();

        ContentNodeI searchRelRootNode = instance.getContentNode(ContentKey.decode(SearchRelevancyList.WORD_STEMMING_EXCEPTION));
        if (searchRelRootNode!=null) {
            Set<ContentKey> wordPluralException = ContentNodeUtil.collectReachableKeys(searchRelRootNode, FDContentTypes.WORD_STEMMING_EXCEPTION);
            Map<ContentKey, ContentNodeI> wordPluralNodes = instance.getContentNodes(wordPluralException);
    
            for (ContentNodeI node : wordPluralNodes.values()) {
                String word = ContentNodeUtil.getStringAttribute(node, WORD);
                if (word!=null) {
                    result.add(word.toLowerCase().trim());
                }
            }
        }
        return result;
    }
    
}
