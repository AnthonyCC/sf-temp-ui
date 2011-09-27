/**
 * 
 */
package com.freshdirect.cms.fdstore;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

/**
 * Check that Category -> "recommender" -> Recommender -> scope doesn't create
 * circular references.
 * 
 * @author zsombor
 * 
 */
public class CmsRecommenderValidator implements ContentValidatorI, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.freshdirect.cms.validation.ContentValidatorI#validate(com.freshdirect
     * .cms.validation.ContentValidationDelegate,
     * com.freshdirect.cms.application.ContentServiceI,
     * com.freshdirect.cms.ContentNodeI,
     * com.freshdirect.cms.application.CmsRequestI)
     */
    @Override
    public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
        // Category
        if (FDContentTypes.CATEGORY.equals(node.getKey().getType())) {
            Object recommenderKey = node.getAttributeValue("recommender");
            if (recommenderKey instanceof ContentKey) {
                ContentNodeI recommender = service.getContentNode((ContentKey) recommenderKey);
                List<ContentKey> scopeNodes = (List<ContentKey>) (recommender != null ? recommender.getAttributeValue("scope") : Collections.EMPTY_LIST);
                checkKey(service, delegate, node.getKey(), recommender.getKey(), node, scopeNodes);
            }
        }
        if (FDContentTypes.RECOMMMENDER.equals(node.getKey().getType())) {
            List<ContentKey> scopedNodes = (List<ContentKey>) node.getAttributeValue("scope");
            for (ContentKey key : scopedNodes) {
                if (searchForRecommender(service, key, node)) {
                    delegate.record(node.getKey(), "scope",  key.getEncoded()
                            + " cause circular references!");
                }
            }
        }

    }

    private boolean searchForRecommender(ContentServiceI service, ContentKey key, ContentNodeI recommenderNode) {
        ContentType type = key.getType();
        if (FDContentTypes.CATEGORY.equals(type) || FDContentTypes.DEPARTMENT.equals(type)) {
            ContentNodeI contentNodeI = service.getContentNode(key);
            List<ContentKey> children;
            if (FDContentTypes.DEPARTMENT.equals(type)) {
                children = (List<ContentKey>) contentNodeI.getAttributeValue("categories");
            } else {
                children = (List<ContentKey>) contentNodeI.getAttributeValue("subcategories");
                ContentKey recommender = (ContentKey) contentNodeI.getAttributeValue("recommender");
                if (recommenderNode.getKey().equals(recommender)) {
                    return true;
                }
            }
            if (children != null) {
                for (ContentKey child : children) {
                    if (searchForRecommender(service, child, recommenderNode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkKey(ContentServiceI service, ContentValidationDelegate delegate, ContentKey originalKey, ContentKey recommenderKey, ContentNodeI node, List<ContentKey> keys) {
        if (node == null) {
            return false;
        }
        if (keys.contains(node.getKey())) {
            delegate.record(originalKey, "recommender", recommenderKey.getEncoded() + " scope contains " + node.getKey()
                    + " which cause circular references!");
            return true;
        }
        ContentType type = node.getKey().getType();
        if (FDContentTypes.PRODUCT.equals(type) || FDContentTypes.CATEGORY.equals(type) || FDContentTypes.DEPARTMENT.equals(type)) { 
            Set<ContentKey> parentKeys = service.getParentKeys(node.getKey());
            for (ContentKey parent : parentKeys) {
                ContentNodeI parentNode = service.getContentNode(parent);
                if (checkKey(service, delegate, originalKey, recommenderKey, parentNode, keys)) {
                    return true;
                }
            }
        }
        return false;
    }

}
