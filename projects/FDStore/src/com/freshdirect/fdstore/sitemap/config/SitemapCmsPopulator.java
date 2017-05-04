package com.freshdirect.fdstore.sitemap.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class SitemapCmsPopulator {

    private static final SitemapCmsPopulator INSTANCE = new SitemapCmsPopulator();

    public static SitemapCmsPopulator getInstance() {
        return INSTANCE;
    }

    private SitemapCmsPopulator() {
    }

    public Map<String, List<ContentKey>> getCategoriesByDepartment() {
        Map<String, List<ContentKey>> paths = new HashMap<String, List<ContentKey>>();
        getCategories(paths, getNodesByType(FDContentTypes.SUPER_DEPARTMENT));
        getCategories(paths, getNodesByType(FDContentTypes.DEPARTMENT));
        return paths;
    }

    private void getCategories(Map<String, List<ContentKey>> contentPaths, Collection<ContentNodeI> nodes) {
        for (ContentNodeI node : nodes) {
            if (isSitemapValid(node)) {
                List<ContentKey> paths = new ArrayList<ContentKey>();
                populateCategories(node, paths);
                contentPaths.put(node.getKey().getId(), paths);
            }
        }
    }

    private void populateCategories(ContentNodeI node, List<ContentKey> paths) {
        if (doesContentTypeContainProducts(node.getKey()) && isSitemapValid(node)) {
            paths.add(node.getKey());
        }
        for (ContentNodeI categoryNode : getContentNodes(getNodeChildren(node))) {
            if (doesContentTypeContainProducts(categoryNode.getKey()) && isSitemapValid(categoryNode)) {
                populateCategories(categoryNode, paths);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Set<ContentKey> getNodeChildren(ContentNodeI node) {
        if (FDContentTypes.SUPER_DEPARTMENT.equals(node.getKey().getType())) {
            Set<ContentKey> keys = new HashSet<ContentKey>();
            ArrayList<ContentKey> attributeValue = (ArrayList<ContentKey>) node.getAttributeValue("departments");
            if (attributeValue != null) {
                keys.addAll(attributeValue);
            }
            return keys;
        }
        return node.getChildKeys();
    }

    public Map<String, List<ContentKey>> getProductsByDepartment() {
        Map<String, List<ContentKey>> paths = new HashMap<String, List<ContentKey>>();
        getProducts(paths, getNodesByType(FDContentTypes.SUPER_DEPARTMENT));
        getProducts(paths, getNodesByType(FDContentTypes.DEPARTMENT));
        return paths;
    }

    private void getProducts(Map<String, List<ContentKey>> paths, Collection<ContentNodeI> nodes) {
        for (ContentNodeI node : nodes) {
            if (isSitemapValid(node)) {
                List<ContentKey> productPaths = new ArrayList<ContentKey>();
                populateProducts(node, productPaths);
                paths.put(node.getKey().getId(), productPaths);
            }
        }
    }

    private void populateProducts(ContentNodeI node, List<ContentKey> paths) {
        for (ContentNodeI categoryNode : getContentNodes(getNodeChildren(node))) {
            if (FDContentTypes.PRODUCT.equals(categoryNode.getKey().getType()) && isSitemapValid(categoryNode)) {
                if (node.getKey().equals(getPrimaryHomeKey(categoryNode.getKey()))) {
                    paths.add(categoryNode.getKey());
                }
            }
            if (doesContentTypeContainProducts(categoryNode.getKey()) && isSitemapValid(categoryNode)) {
                populateProducts(categoryNode, paths);
            }
        }
    }

    private boolean doesContentTypeContainProducts(ContentKey key) {
        ContentType type = key.getType();
        return (FDContentTypes.SUPER_DEPARTMENT.equals(type) || FDContentTypes.DEPARTMENT.equals(type) || FDContentTypes.CATEGORY.equals(type));
    }

    private Boolean isSitemapValid(ContentNodeI node) {
        return !Boolean.TRUE.equals((Boolean) node.getAttributeValue("SKIP_SITEMAP"));
    }

    private Collection<ContentNodeI> getNodesByType(ContentType type) {
        return getContentNodes(CmsManager.getInstance().getContentKeysByType(type, DraftContext.MAIN));
    }

    private Collection<ContentNodeI> getContentNodes(Set<ContentKey> keys) {
        return CmsManager.getInstance().getContentNodes(keys, DraftContext.MAIN).values();
    }

    public ContentKey getPrimaryHomeKey(ContentKey key) {
        return CmsManager.getInstance().getPrimaryHomeKey(key, DraftContext.MAIN);
    }

}
