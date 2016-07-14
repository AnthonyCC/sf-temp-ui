package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.search.term.LowercaseCoder;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SearchRelevancyList {
    
    final static Logger LOGGER = LoggerFactory.getInstance(SearchRelevancyList.class);

    public static final String SEARCH_RELEVANCY_KEY = "FDFolder:searchRelevancyList";
    public static final String SEARCH_RELEVANCY_KEY_FDX = "FDFolder:searchRelevancyListFdx";

    public static final String KEYWORDS = "Keywords";
    public static final String HINTS = "categoryHints";

    private static final String SCORE = "score";

    private static final String CATEGORY = "category";

    private ContentKey listContentKey;
    
    /**
     * List<String>
     */
    String[]                   keywords;

    /**
     * Map<ContentKey,Integer>
     */
    Map<ContentKey,Integer>     categoryScoreMap;

    protected SearchRelevancyList(String[] keywords, Map<ContentKey,Integer> categoryScoreMap, ContentKey listContentKey) {
        super();
        this.keywords = keywords;
        this.categoryScoreMap = categoryScoreMap;
        this.listContentKey = listContentKey;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public Map<ContentKey,Integer> getCategoryScoreMap() {
        return categoryScoreMap;
    }


    /**
     * Returns the root content node that holds the whole relevancy list
     * @return
     */
    public ContentKey getRelevancyListContentKey() {
    	return listContentKey;
    }
    
    /**
     * 
     * @return Map<String,SearchRelevancyList>
     */
    public static Map<String, SearchRelevancyList> createFromCms() {
        LOGGER.info("SearchRelevancyList::createFromCms started");
        Map<String, SearchRelevancyList> result = new HashMap<String, SearchRelevancyList>();

        CmsManager instance = CmsManager.getInstance();
        DraftContext draftContext = DraftContext.MAIN;
        
        // find out root content key of search relevancy list
        
        final boolean isFDX = instance.getSingleStoreKey().getId().equals(EnumEStoreId.FDX.getContentId());
        final ContentKey listKey = isFDX ? ContentKey.decode(SEARCH_RELEVANCY_KEY_FDX) : ContentKey.decode(SEARCH_RELEVANCY_KEY);
        
        
        ContentNodeI searchRelRootNode = instance.getContentNode( listKey, draftContext );
        if (searchRelRootNode!=null) {
            LOGGER.info("SearchRelevancyList:: " + searchRelRootNode + " located.");
            
            Set<ContentKey> searchRelKeys = ContentNodeUtil.collectReachableKeys(searchRelRootNode, FDContentTypes.SEARCH_RELEVANCY_LIST, instance, draftContext);
            Map<ContentKey, ContentNodeI> searchRelNodes = instance.getContentNodes(searchRelKeys, draftContext);
            LOGGER.info("found " + searchRelNodes.size() + " nodes.");
            for (Iterator<ContentNodeI> contentNodeIterator = searchRelNodes.values().iterator(); contentNodeIterator.hasNext();) {
                ContentNodeI node = contentNodeIterator.next();
                String keywords = ContentNodeUtil.getStringAttribute(node, KEYWORDS).toLowerCase();
                String[] kw = StringUtils.split(keywords, ",");
                if (kw.length>0) {
                	@SuppressWarnings("unchecked")
                    List<ContentKey> hints = (List<ContentKey>) node.getAttributeValue(HINTS);
                    if (hints != null) {
                        Map<ContentKey, Integer> scores = new HashMap<ContentKey, Integer>();
                        for (int i = 0; i < hints.size(); i++) {
                            ContentKey key = hints.get(i);
                            ContentNodeI hint = instance.getContentNode(key, draftContext);
                            
                            Integer score = ContentNodeUtil.getIntegerAttribute(hint, SCORE);
                            ContentKey categoryKey = ContentNodeUtil.getContentKeyAttribute(hint, CATEGORY);
                            if (score!=null && categoryKey!=null) {
                                scores.put(categoryKey, score);
                            }
                        }
                        if (!scores.isEmpty()) {
                        	List<String> kwds = new ArrayList<String>();
                        	for (String k : kw) {
                        		List<Term> terms = new LowercaseCoder(new Term(k)).getTerms();
                        		if (terms.isEmpty())
                        			continue;
                        		kwds.add(terms.get(0).toString());
                        	}
                        	if (!kwds.isEmpty()) {
                        		kw = kwds.toArray(new String[kwds.size()]);
	                            SearchRelevancyList srl = new SearchRelevancyList(kw, scores, listKey);
	                            for (int i=0;i<kw.length;i++) {
	                                result.put(kw[i], srl);
	                            }
                        	}
                        }
                    }
                }
            }
        }
        LOGGER.info("SearchRelevancyList::createFromCms finished:"+result.size());
        return result;
    }

    
    /**
     * Return a collection of string from the cms, which is under FDFolder:searchRelevancyList, and their type is WORD_PLURALIZATION_EXCEPTION
     *    
     * @return
     */
    @Deprecated
    public static Collection<String> getBadPluralFormsFromCms() {
        return Collections.<String>emptyList();
    }


    /**
     * Helper method to decide whether node is part of relevancy list
     * 
     * @param node
     * @return
     */
    public static boolean isRelatedContentNode(ContentNodeI node) {
    	if (node == null || node.getKey() == null) {
    		return false;
    	}
    	
    	final ContentKey key = node.getKey();
    	final ContentType t = key.getType();
    	
		if (FDContentTypes.SEARCH_RELEVANCY_LIST.equals(t)
				|| FDContentTypes.SEARCH_RELEVANCY_HINT.equals(t)
				|| FDContentTypes.WORD_STEMMING_EXCEPTION.equals(t)) {
			return true;
		}

        if (FDContentTypes.FDFOLDER.equals(t)) {
        	final String k = key.getEncoded();
        	
			if (k.equals(SearchRelevancyList.SEARCH_RELEVANCY_KEY)
					|| k.equals(SearchRelevancyList.SEARCH_RELEVANCY_KEY_FDX)) {
				return true;
			}
        }

		return false;
    }
}
