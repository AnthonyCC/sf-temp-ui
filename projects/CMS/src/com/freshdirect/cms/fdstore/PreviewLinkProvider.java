package com.freshdirect.cms.fdstore;

import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.util.PrimaryHomeUtil;

/**
 * Provides links to the preview site for content objects.
 * 
 * @TODO refactor as a configurable hivemind service
 */
public class PreviewLinkProvider {
	/**
	 * Get a preview link for a content object.
	 * 
	 * @param key content key (never null)
	 * @return URI string or null if no preview link exists
	 */
	@Deprecated
	public static String getLink(ContentKey key) {		
		return PreviewLinkProvider.getLink(key, null);
	}

	/**
	 * Generate preview link for the given content node
	 * 
	 * @param key content node key
	 * @param storeKey optional, except for products.
	 * 	No store key results relative URI
	 * 
	 * @return Preview URL
	 */
	public static String getLink(ContentKey key, ContentKey storeKey, boolean isAbsoluteUrl) {
		ContentType type = key.getType();
		String id = key.getId();

		String uri = null;
		
		final ContentServiceI svc = CmsManager.getInstance();
		
		if (FDContentTypes.PRODUCT.equals(type)) {
			ContentNodeI productNode = key.lookupContentNode();
			if (productNode != null) {
				if (storeKey == null) {
					// storeKey is essential!
					return null;
				}

				ContentKey homeKey = PrimaryHomeUtil.pickPrimaryHomeForStore(key, storeKey, svc);
				if (homeKey != null) {
					uri = "/product.jsp?catId=" + homeKey.getId() + "&productId=" + id;
				}
			}
			
		} else if (FDContentTypes.CATEGORY.equals(type)) {
			uri = "/category.jsp?catId=" + id;

		} else if (FDContentTypes.DEPARTMENT.equals(type)) {
			uri = "/department.jsp?deptId=" + id;

		} else if (FDContentTypes.RECIPE.equals(type)) {
			uri = "/recipe.jsp?recipeId=" + id;
			
		} else if (FDContentTypes.RECIPE_CATEGORY.equals(type)) {
			uri = "/recipe_cat.jsp?catId=" + id;
			
		} else if (FDContentTypes.RECIPE_SUBCATEGORY.equals(type)) {
			Set<ContentKey> parentKeys=CmsManager.getInstance().getParentKeys(key);
			if (parentKeys.size() > 0 ) {
				ContentKey parentKey = parentKeys.iterator().next();
				uri = "/recipe_subcat.jsp?catId="+parentKey.getId()+"&subCatId=" + id;
			}
		} else if (FDContentTypes.RECIPE_DEPARTMENT.equals(type)) {
			uri = "/department.jsp?deptId=" + id;
			
		} else if (FDContentTypes.RECIPE_SEARCH_PAGE.equals(type)) {
			uri = "/recipe_search.jsp?deptId=" + id;
		} else if(FDContentTypes.HTML.equals(type)){
			ContentNodeI node = key.getContentNode(); 
			if (node != null) {
				uri = "/test/content/preview.jsp?template="+node.getAttributeValue("path");
			}
		} else if(FDContentTypes.YMAL_SET.equals(type)){
			uri = "/test/content/ymal_set_preview.jsp?ymalSetId=" + id;
		} else if (FDContentTypes.PAGE.equals(type)) {
			uri = "/page.jsp?pageId=" + id;
		} else if (FDContentTypes.TAG.equals(type)) {
			uri = "/test/migration/products_tagged.jsp?tag=" + id;
		} else if (FDContentTypes.SUPER_DEPARTMENT.equals(type)) {
			uri = "/browse.jsp?id=" + id;
		} else if (FDContentTypes.WEBPAGE.equals(type)){
			ContentNodeI node = key.getContentNode();
			if(node.getAttributeValue("URL") != null){
				uri = (String) node.getAttributeValue("URL");
				if(uri != null && !uri.contains("?")){
					uri += ("?");
				}
			}
		}

		if (uri != null && isAbsoluteUrl) {
			return uri;
		}
		if (uri != null) {
			if (storeKey != null) {
				ContentNodeI theStoreNode = storeKey.lookupContentNode();
				if (theStoreNode != null) {
					String previewHostName = (String) theStoreNode.getAttributeValue("PREVIEW_HOST_NAME");
					if (previewHostName != null) {
						return "//"+previewHostName+(uri.startsWith("/") ? uri : "/" + uri);
					}
				}
			}
		}
		return uri;
	}
	
	
	public static String getLink(ContentKey key, ContentKey storeKey) {
		return getLink(key, storeKey, true); 
	}
}
