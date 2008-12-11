package com.freshdirect.cms.search;

import java.util.HashMap;
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

    public static final String KEYWORDS = "Keywords";
    public static final String HINTS = "categoryHints";

    private static final String SCORE = "score";

    private static final String CATEGORY = "category";
    
    
    
    /**
     * List<String>
     */
    String[]                   keywords;

    /**
     * Map<ContentKey,Integer>
     */
    Map                        categoryScoreMap;

    public SearchRelevancyList(String[] keywords, Map categoryScoreMap) {
        super();
        this.keywords = keywords;
        this.categoryScoreMap = categoryScoreMap;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public Map getCategoryScoreMap() {
        return categoryScoreMap;
    }

    /**
     * 
     * @return Map<String,SearchRelevancyList>
     */
    public static Map createFromCms() {
        Map result = new HashMap();

        CmsManager instance = CmsManager.getInstance();

        ContentNodeI searchRelRootNode = instance.getContentNode(ContentKey.decode(SearchRelevancyList.SEARCH_RELEVANCY_KEY));
        if (searchRelRootNode!=null) {
            Set searchRelKeys = ContentNodeUtil.collectReachableKeys(searchRelRootNode, FDContentTypes.SEARCH_RELEVANCY_LIST);
            Map searchRelNodes = instance.getContentNodes(searchRelKeys);
    
            for (Iterator contentNodeIterator = searchRelNodes.values().iterator(); contentNodeIterator.hasNext();) {
                ContentNodeI node = (ContentNodeI) contentNodeIterator.next();
                String keywords = ContentNodeUtil.getStringAttribute(node, KEYWORDS).toLowerCase();
                String[] kw = StringUtils.split(keywords, ",");
                if (kw.length>0) {
                    List hints = (List) node.getAttribute(HINTS).getValue();
                    if (hints!=null) {
                        Map scores = new HashMap();
                        for (int i=0;i<hints.size();i++) {
                            ContentKey key = (ContentKey) hints.get(i);
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

}
