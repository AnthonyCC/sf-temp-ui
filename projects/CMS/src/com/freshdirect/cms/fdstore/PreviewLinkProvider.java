package com.freshdirect.cms.fdstore;

import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;

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
	public static String getLink(ContentKey key) {
		ContentType type = key.getType();
		String id = key.getId();

		if (FDContentTypes.PRODUCT.equals(type)) {
			ContentNodeI productNode = key.lookupContentNode();
			ContentKey priHome = (ContentKey) productNode.getAttribute("PRIMARY_HOME").getValue();
			if (priHome != null) {
				return "/product.jsp?catId=" + priHome.getId() + "&productId=" + id;
			}
			
		} else if (FDContentTypes.CATEGORY.equals(type)) {
			return "/category.jsp?catId=" + id;

		} else if (FDContentTypes.DEPARTMENT.equals(type)) {
			return "/department.jsp?deptId=" + id;

		} else if (FDContentTypes.RECIPE.equals(type)) {
			return "/recipe.jsp?recipeId=" + id;
			
		} else if (FDContentTypes.RECIPE_CATEGORY.equals(type)) {
			return "/recipe_cat.jsp?catId=" + id;
			
		} else if (FDContentTypes.RECIPE_SUBCATEGORY.equals(type)) {
			Set parentKeys=CmsManager.getInstance().getParentKeys(key);
			if (parentKeys.size() > 0 ) {
				ContentKey parentKey = (ContentKey)parentKeys.iterator().next();
				return "/recipe_subcat.jsp?catId="+parentKey.getId()+"&subCatId=" + id;
			}
		} else if (FDContentTypes.RECIPE_DEPARTMENT.equals(type)) {
			return "/department.jsp?deptId=" + id;
			
		} else if (FDContentTypes.RECIPE_SEARCH_PAGE.equals(type)) {
			return "/recipe_search.jsp?deptId=" + id;
		} else if(FDContentTypes.TEMPLATE.equals(type)){
			ContentNodeI node = key.getContentNode(); 
			if (node != null) {
				return "/test/content/preview.jsp?template="+node.getAttribute("path");
			}
		} else if(FDContentTypes.YMAL_SET.equals(type)){
			return "/test/content/ymal_set_preview.jsp?ymalSetId=" + id;
		}

		return null;
	}
}
