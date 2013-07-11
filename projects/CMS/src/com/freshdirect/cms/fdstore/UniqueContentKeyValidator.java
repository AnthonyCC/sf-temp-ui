package com.freshdirect.cms.fdstore;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.framework.conf.FDRegistry;

/**
 * Ensures that ContentKey IDs for certain types are unique with respect to
 * nodes of all such content types. Quirks in the FD Storefront mapping layer
 * necessitate this. The following types are mandated uniqueness:
 * <ul>
 * 	<li>Department</li>
 * 	<li>Category</li>
 * 	<li>Product</li>
 * 	<li>Sku</li>
 * 	<li>ComponentGroup</li>
 * 	<li>ConfiguredProduct</li>
 * 	<li>ConfiguredProductGroup</li>
 * 	<li>Recipe</li>
 * 	<li>RecipeDepartment</li>
 * 	<li>RecipeCategory</li>
 * 	<li>RecipeSubcategory</li>
 * 	<li>RecipeSource</li>
 * 	<li>RecipeVariant</li>
 * 	<li>RecipeSection</li>
 * 	<li>RecipeAuthor</li>
 *  <li>FDFolder</li>
 *  <li>BookRetailer</li>
 * </ul>
 * 
 */
public class UniqueContentKeyValidator implements ContentValidatorI {
	private Set<ContentType> cmsTypes = null;

	public void setCmsTypes(Set<ContentType> types) {
		this.cmsTypes = types;
	}


	/**
	 * Collect all content types of the CMS Store region.
	 * 
	 * @return
	 */
	protected Set<ContentType> getStoreContentTypes() {
		ContentTypeServiceI svc = (ContentTypeServiceI) FDRegistry
				.getInstance()
				.getService("com.freshdirect.cms.StoreDef", ContentTypeServiceI.class);

		return svc != null ? svc.getContentTypes() : null;
	}


	public void validate( ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode ) {
		// lazy init
		if (cmsTypes == null) {
			cmsTypes = getStoreContentTypes();
		}

		if (cmsTypes == null) {
			delegate.record("Unique Key Validation skipped due to empty types list");
			return;
		}

		
		ContentType type = node.getKey().getType();
		if ( cmsTypes.contains( type ) ) {

			Set<ContentKey> keys = new HashSet<ContentKey>( cmsTypes.size() - 1 );
			for ( ContentType t : cmsTypes ) {
				if ( !t.equals( type ) ) {
					keys.add( new ContentKey( t, node.getKey().getId() ) );
				}
			}

			Map<ContentKey, ContentNodeI> nodes = service.getContentNodes( keys );
			if ( !nodes.isEmpty() ) {
				delegate.record( node.getKey(), "Content with the same ID already exists: " + nodes.keySet() );
			}
		}
	}

}
