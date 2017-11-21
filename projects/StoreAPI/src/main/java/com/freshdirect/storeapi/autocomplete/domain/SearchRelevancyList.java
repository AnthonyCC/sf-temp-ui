package com.freshdirect.storeapi.autocomplete.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.storeapi.node.ContentNodeUtil;
import com.freshdirect.storeapi.search.converter.LowerCaseConverter;
import com.google.common.base.Optional;

public class SearchRelevancyList {

    final static Logger LOGGER = LoggerFactory.getInstance(SearchRelevancyList.class);

    public static final String SEARCH_RELEVANCY_KEY = "FDFolder:searchRelevancyList";
    public static final String SEARCH_RELEVANCY_KEY_FDX = "FDFolder:searchRelevancyListFdx";

    public static final String KEYWORDS = "Keywords";
    public static final String HINTS = "categoryHints";

    private static final String SCORE = "score";

    private static final String CATEGORY = "category";

    private ContentKey listContentKey;

    private static ContextualContentProvider contentProviderService = CmsServiceLocator.contentProviderService();
    private static ContentTypeInfoService contentTypeInfoService = CmsServiceLocator.contentTypeInfoService();

    /**
     * List<String>
     */
    String[] keywords;

    /**
     * Map<ContentKey,Integer>
     */
    Map<ContentKey, Integer> categoryScoreMap;

    protected SearchRelevancyList(String[] keywords, Map<ContentKey, Integer> categoryScoreMap, ContentKey listContentKey) {
        super();
        this.keywords = keywords;
        this.categoryScoreMap = categoryScoreMap;
        this.listContentKey = listContentKey;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public Map<ContentKey, Integer> getCategoryScoreMap() {
        return categoryScoreMap;
    }

    /**
     * Returns the root content node that holds the whole relevancy list
     * 
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

        // find out root content key of search relevancy list

        final boolean isFDX = instance.getSingleStoreKey().getId().equals(EnumEStoreId.FDX.getContentId());
        final ContentKey listKey = isFDX ? ContentKeyFactory.get(SEARCH_RELEVANCY_KEY_FDX) : ContentKeyFactory.get(SEARCH_RELEVANCY_KEY);

        ContentNodeI searchRelRootNode = instance.getContentNode(listKey);
        if (searchRelRootNode != null) {
            LOGGER.info("SearchRelevancyList:: " + searchRelRootNode + " located.");

            Set<ContentKey> searchRelKeys = collectReachableKeys(searchRelRootNode.getKey(), FDContentTypes.SEARCH_RELEVANCY_LIST);
            Map<ContentKey, ContentNodeI> searchRelNodes = instance.getContentNodes(searchRelKeys);
            LOGGER.info("found " + searchRelNodes.size() + " nodes.");
            for (Iterator<ContentNodeI> contentNodeIterator = searchRelNodes.values().iterator(); contentNodeIterator.hasNext();) {
                ContentNodeI node = contentNodeIterator.next();
                String keywords = ContentNodeUtil.getStringAttribute(node, KEYWORDS).toLowerCase();
                String[] kw = StringUtils.split(keywords, ",");
                if (kw.length > 0) {
                    @SuppressWarnings("unchecked")
                    List<ContentKey> hints = (List<ContentKey>) node.getAttributeValue(HINTS);
                    if (hints != null) {
                        Map<ContentKey, Integer> scores = new HashMap<ContentKey, Integer>();
                        for (int i = 0; i < hints.size(); i++) {
                            ContentKey key = hints.get(i);
                            ContentNodeI hint = instance.getContentNode(key);

                            Integer score = ContentNodeUtil.getIntegerAttribute(hint, SCORE);
                            Optional<Object> categoryKey = contentProviderService.getAttributeValue(hint.getKey(), ContentTypes.SearchRelevancyHint.category);
                            if (score != null && categoryKey.isPresent()) {
                                scores.put((ContentKey) categoryKey.get(), score);
                            }
                        }
                        if (!scores.isEmpty()) {
                            List<String> kwds = new ArrayList<String>();
                            for (String k : kw) {
                                String terms = new LowerCaseConverter().convert(k);
                                if (terms.isEmpty())
                                    continue;
                                kwds.add(terms);
                            }
                            if (!kwds.isEmpty()) {
                                kw = kwds.toArray(new String[kwds.size()]);
                                SearchRelevancyList srl = new SearchRelevancyList(kw, scores, listKey);
                                for (int i = 0; i < kw.length; i++) {
                                    result.put(kw[i], srl);
                                }
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info("SearchRelevancyList::createFromCms finished:" + result.size());
        return result;
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

        if (FDContentTypes.SEARCH_RELEVANCY_LIST.equals(t) || FDContentTypes.SEARCH_RELEVANCY_HINT.equals(t) || FDContentTypes.WORD_STEMMING_EXCEPTION.equals(t)) {
            return true;
        }

        if (FDContentTypes.FDFOLDER.equals(t)) {
            final String k = key.getEncoded();

            if (k.equals(SearchRelevancyList.SEARCH_RELEVANCY_KEY) || k.equals(SearchRelevancyList.SEARCH_RELEVANCY_KEY_FDX)) {
                return true;
            }
        }

        return false;
    }

    private static Set<ContentKey> collectReachableKeys(ContentKey rootKey, ContentType targetType) {
        Set<ContentKey> navigableChildKeys = contentProviderService.getChildKeys(rootKey, true);
        Set<ContentKey> collectedKeys = new HashSet<ContentKey>();
        for (ContentKey contentKey : navigableChildKeys) {
            if (contentKey.type.equals(targetType)) {
                collectedKeys.add(contentKey);
            }

            List<Relationship> relationships = contentTypeInfoService.selectRelationships(contentKey.type, false);
            for (Relationship relationship : relationships) {
                if (Arrays.asList(relationship.getDestinationTypes()).contains(targetType)) {
                    collectedKeys.addAll(collectReachableKeys(contentKey, targetType));
                }
            }
        }
        return collectedKeys;
    }
}
