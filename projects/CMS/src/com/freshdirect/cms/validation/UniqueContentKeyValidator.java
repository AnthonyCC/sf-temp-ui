package com.freshdirect.cms.validation;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;

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

	private final static Set<ContentType> UNIQUE_TYPES = new HashSet<ContentType>();
	static {
		UNIQUE_TYPES.add(FDContentTypes.DEPARTMENT);
		UNIQUE_TYPES.add(FDContentTypes.CATEGORY);
		UNIQUE_TYPES.add(FDContentTypes.PRODUCT);
		UNIQUE_TYPES.add(FDContentTypes.COMPONENT_GROUP);
		UNIQUE_TYPES.add(FDContentTypes.CONFIGURED_PRODUCT);
		UNIQUE_TYPES.add(FDContentTypes.CONFIGURED_PRODUCT_GROUP);
		UNIQUE_TYPES.add(FDContentTypes.SKU);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_DEPARTMENT);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_CATEGORY);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_SUBCATEGORY);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_SOURCE);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_VARIANT);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_SECTION);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_AUTHOR);
		UNIQUE_TYPES.add(FDContentTypes.FDFOLDER);
		UNIQUE_TYPES.add(FDContentTypes.BOOK_RETAILER);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_SEARCH_PAGE);
		UNIQUE_TYPES.add(FDContentTypes.RECIPE_SEARCH_CRITERIA);		
		UNIQUE_TYPES.add(FDContentTypes.YMAL_SET);
		UNIQUE_TYPES.add(FDContentTypes.STARTER_LIST);
		UNIQUE_TYPES.add(FDContentTypes.SUPER_DEPARTMENT);
		UNIQUE_TYPES.add(FDContentTypes.CATEGORY_SECTION);
		UNIQUE_TYPES.add(FDContentTypes.GLOBAL_NAVIGATION);
		
	}

	@Override
	public void validate( ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode ) {
		ContentType type = node.getKey().getType();
		if ( UNIQUE_TYPES.contains( type ) ) {
			Set<ContentKey> keys = new HashSet<ContentKey>( UNIQUE_TYPES.size() - 1 );
			for ( ContentType t : UNIQUE_TYPES ) {
				if ( !t.equals( type ) ) {
					keys.add( ContentKey.getContentKey( t, node.getKey().getId() ) );
				}
			}

            Map<ContentKey, ContentNodeI> nodes = service.getContentNodes( keys, draftContext );
			if ( !nodes.isEmpty() ) {
				delegate.record( node.getKey(), "Content with the same ID already exists: " + nodes.keySet() );
			}
		}
	}

}
