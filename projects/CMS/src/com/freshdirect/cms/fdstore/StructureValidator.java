package com.freshdirect.cms.fdstore;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

/**
 * Ensures nodes of the following type have a one and only one parent:
 * <ul>
 * 	<li>Department</li>
 * 	<li>Category</li>
 * 	<li>Sku</li>
 * 	<li>ComponentGroup</li>
 * 	<li>Domain</li>
 * 	<li>DomainValue</li>
 * 	<li>Recipe</li>
 * 	<li>RecipeSection</li>
 * 	<li>RecipeVariant</li>
 * </ul>
 * 
 * @TODO make the type list configurable from hivemind
 */
public class StructureValidator implements ContentValidatorI {

	private final static Set<ContentType> TYPES = new HashSet<ContentType>();
	static {
		TYPES.add(FDContentTypes.DEPARTMENT);
		TYPES.add(FDContentTypes.CATEGORY);
		TYPES.add(FDContentTypes.SKU);
		TYPES.add(FDContentTypes.COMPONENT_GROUP);
		TYPES.add(FDContentTypes.DOMAIN);
		TYPES.add(FDContentTypes.DOMAINVALUE);
		TYPES.add(FDContentTypes.RECIPE);
		TYPES.add(FDContentTypes.RECIPE_SECTION);
		TYPES.add(FDContentTypes.RECIPE_VARIANT);
		TYPES.add(FDContentTypes.STARTER_LIST);
	}
	
	private final static Set<ContentType> IGNORABLE_TYPES = new HashSet<ContentType>();
	static{
		IGNORABLE_TYPES.add(FDContentTypes.IMAGE_BANNER);
		IGNORABLE_TYPES.add(FDContentTypes.ANCHOR);
		IGNORABLE_TYPES.add(FDContentTypes.SECTION);
		IGNORABLE_TYPES.add(FDContentTypes.WEBPAGE);
		IGNORABLE_TYPES.add(FDContentTypes.PICK_LIST);
		IGNORABLE_TYPES.add(FDContentTypes.DARKSTORE);
	}

	public void validate( ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode ) {
		if(TYPES.contains(node.getKey().getType())){
			Set<ContentKey> parentKeys = service.getParentKeys( node.getKey() );
			Set<ContentKey> nonIgnorableKeys = new HashSet<ContentKey>();
			for(ContentKey contenkey:parentKeys){
				if(!IGNORABLE_TYPES.contains(contenkey.getType())){
					nonIgnorableKeys.add(contenkey);
				}
			}
			if ( nonIgnorableKeys.size() > 1 ) {
				delegate.record( node.getKey(), "Cannot have multiple parents. " + nonIgnorableKeys );
			}
		}
	}
}