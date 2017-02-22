package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.application.StoreContentSource;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.term.Term;
import com.freshdirect.fdstore.FDStoreProperties;

public class SearchUtils {
    
    private static final String KEYWORD = "keyword";
    private static final String PRIMARY_HOME = "PRIMARY_HOME";
    private static final String KEYWORDS = "KEYWORDS";
    
    public static List<Term> collectValues(ContentNodeI node, AttributeIndex index, boolean keywordsEnabled, boolean primaryHomeKeywordsEnabled, boolean parentRecursionEnabled,
            StoreContentSource storeContentSource) {
        final DraftContext draftContext = DraftContext.MAIN;
        List<Term> values = new ArrayList<Term>();

        String attributeName = index.getAttributeName();
        String relationshipAttributeName = index.getRelationshipAttributeName();
        if (!primaryHomeKeywordsEnabled && PRIMARY_HOME.equals(attributeName) && KEYWORDS.equals(relationshipAttributeName))
            return values;
        boolean isKeyword = relationshipAttributeName != null && relationshipAttributeName.toLowerCase().startsWith(KEYWORD) || attributeName.toLowerCase().startsWith(KEYWORD);
        if (!keywordsEnabled && isKeyword)
            return values;
        Object attributeValue = node.getAttributeValue(attributeName);
        if (attributeValue == null)
            return values;

        AttributeI attribute = node.getAttribute(attributeName);

        if (attribute.getDefinition().getAttributeType().equals(EnumAttributeType.RELATIONSHIP) && relationshipAttributeName != null) {
            if (attribute.getDefinition().getCardinality().equals(EnumCardinality.ONE)) {
                ContentKey key = (ContentKey) attributeValue;
                ContentNodeI relNode = storeContentSource.getContentNode(key, draftContext);
                if (relNode != null) {
                    Object relValue = relNode.getAttributeValue(relationshipAttributeName);
                    if (relValue != null)
                        addValues(values, relValue.toString(), isKeyword);
                    if (parentRecursionEnabled && index.isRecurseParent())
                        collectParentValues(values, relNode, relationshipAttributeName, isKeyword, draftContext, storeContentSource);
                }
            } else { // EnumCardinality.MANY
                @SuppressWarnings("unchecked")
                List<ContentKey> relNodes = (List<ContentKey>) attributeValue;

                for (ContentKey key : relNodes) {
                    ContentNodeI relNode = storeContentSource.getContentNode(key, draftContext);
                    if (relNode != null) {
                        Object relValue = relNode.getAttributeValue(relationshipAttributeName);
                        if (relValue != null)
                            addValues(values, relValue.toString(), isKeyword);
                    }
                }
            }
        } else {
            addValues(values, attributeValue.toString(), isKeyword);
            if (parentRecursionEnabled && index.isRecurseParent())
                collectParentValues(values, node, attributeName, isKeyword, draftContext, storeContentSource);
        }
        return values;
    }

    private static void addValues(List<Term> values, String value, boolean isKeyword) {
        if (isKeyword)
            values.addAll(Term.keywordize(value, ",;"));
        else
            values.add(new Term(value));
    }

    private static void collectParentValues(List<Term> values, ContentNodeI node, String attributeName, boolean isKeyword, DraftContext draftContext,
            StoreContentSource storeContentSource) {
        if (!FDStoreProperties.isSearchRecurseParentAttributesEnabled())
            return;
        ContentKey key = node.getKey();
        ContentKey parentKey = null;
        while ((parentKey = getParentKey(key, draftContext, storeContentSource)) != null) {
            ContentNodeI parentNode = storeContentSource.getContentNode(parentKey, draftContext);
            if (parentNode != null) {
                Object parentValue = parentNode.getAttributeValue(attributeName);
                if (parentValue != null)
                    addValues(values, parentValue.toString(), isKeyword);
            } else
                break; // something wrong
            key = parentKey;
        }
    }

    private static ContentKey getParentKey(ContentKey key, DraftContext draftContext, StoreContentSource storeContentSource) {
        if (FDContentTypes.STORE.equals(key.getType())) {
            return null;
        }

        ContentKey parentKey = null;

        if (FDContentTypes.PRODUCT.equals(key.getType())) {
            parentKey = storeContentSource.getPrimaryHomeKey(key, draftContext);
        }

        if (parentKey == null) {
            Set<ContentKey> keys = storeContentSource.getParentKeys(key, draftContext);
            if (keys.size() == 0) {
                return null;
            }

            Iterator<ContentKey> i = keys.iterator();
            parentKey = i.next();
        }

        return parentKey;
    }
}
