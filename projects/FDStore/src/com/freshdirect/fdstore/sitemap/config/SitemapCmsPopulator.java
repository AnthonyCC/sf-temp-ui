package com.freshdirect.fdstore.sitemap.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDMaterialSalesArea;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.fdstore.FDContentTypes;


public class SitemapCmsPopulator {

	private static final Category LOGGER = LoggerFactory.getInstance(SitemapCmsPopulator.class);
	
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

    private boolean doesContentTypeProducts(ContentKey key) {
        return FDContentTypes.PRODUCT.equals(key.getType());
    }

    private Boolean isSitemapValid(ContentNodeI node) {
        return !Boolean.TRUE.equals(node.getAttributeValue("SKIP_SITEMAP")) && !isKeyOrphan(node.getKey()) && !isSKUUnavailableWithTestStatus(node);
    }

    private Collection<ContentNodeI> getNodesByType(ContentType type) {
        return getContentNodes(CmsManager.getInstance().getContentKeysByType(type));
    }

    private Collection<ContentNodeI> getContentNodes(Set<ContentKey> keys) {
        return CmsManager.getInstance().getContentNodes(keys).values();
    }

    public ContentKey getPrimaryHomeKey(ContentKey key) {
        return CmsManager.getInstance().getPrimaryHomeKey(key);
    }

    private boolean isKeyOrphan(ContentKey key) {
        boolean isOrphan = false;
        if (doesContentTypeProducts(key) || doesContentTypeContainProducts(key)) {
            Set<ContentKey> parentKeys = CmsManager.getInstance().getParentKeys(key);
            if (parentKeys.isEmpty()) {
                isOrphan = true;
            } else {
                for (ContentKey parentKey : parentKeys) {
                    boolean isParentOrphan = isKeyOrphan(parentKey);
                    if (isParentOrphan){
                        isOrphan = true;
                        break;
                    }
                }
            }
        }
        return isOrphan;
    }
    
    @SuppressWarnings("unchecked")
	private boolean isSKUUnavailableWithTestStatus(ContentNodeI productNode) {
    	boolean skuMatch = false;
    	
    	List<ContentKey> skus = (ArrayList<ContentKey>) productNode.getAttributeValue("skus");
   	   	
        if (skus != null) {
    		for (ContentKey contentKey : skus) {
					FDProductInfo fdProductInfo;
					try {
						fdProductInfo = FDCachedFactory.getProductInfo(contentKey.getId());						
						skuMatch = fdProductInfo.isTestInAnyArea();
					} catch (FDResourceException e) {
						// Sku not found in ERPS. Can happen as cms skus are easily created for preview purposes.
						LOGGER.warn("SKU not found during Sitemap generation: "+contentKey.getId());
					} catch (FDSkuNotFoundException e) {
						// Sku not found in ERPS. Can happen as cms skus are easily created for preview purposes.
						LOGGER.warn("SKU not found during Sitemap generation: "+contentKey.getId());
					}															                		
			}
		}
        
        return skuMatch;
    }

}
