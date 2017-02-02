package com.freshdirect.cms.fdstore;

import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;

/**
 * Makes sure that a recipe node has a default recipe variant, and that recipe
 * variant has a main recipe section when created.
 */
public class RecipeChildNodeValidator implements ContentValidatorI {

    @Override
	public void validate(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request, ContentNodeI oldNode) {

		ContentType type = node.getKey().getType();
        if (FDContentTypes.RECIPE.equals(type)) {
			addDefaultVariantToRecipe(delegate, service, draftContext, node, request);
		} else if (FDContentTypes.RECIPE_VARIANT.equals(type)) {
			addMainSectionToVariant(delegate, service, draftContext, node, request);			
		}
	}

	private void addDefaultVariantToRecipe(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request) {
		
		ContentKey variantKey = ContentKey.getContentKey(FDContentTypes.RECIPE_VARIANT, node.getKey().getId() + "_default");
		
		ContentNodeI variantNode = checkForChild(delegate, service, draftContext, node, "variants", variantKey, request);
		if (variantNode!=null) {
			variantNode.getAttribute("name").setValue("default");
			request.addNode(variantNode);
			addMainSectionToVariant(delegate, service, draftContext, variantNode, request);
		}
	}
	
	private void addMainSectionToVariant(ContentValidationDelegate delegate, ContentServiceI service, DraftContext draftContext, ContentNodeI node, CmsRequestI request) {
		
		ContentKey sectionKey = ContentKey.getContentKey(FDContentTypes.RECIPE_SECTION, node.getKey().getId() + "_main");
		
		ContentNodeI sectionNode = checkForChild(delegate, service, draftContext, node, "sections", sectionKey, request);
		if (sectionNode != null) {
			sectionNode.getAttribute("name").setValue("main");
			request.addNode(sectionNode);
		}
	}

	/**
	 *  Check for the existence of a certain child key in a relationship.
	 *  Create the node if no such key is in the relationship.
	 *  
	 *  @param delegate the validation delegate object, to collect errors
	 *  @param service the service that stores all the data
	 *  @param node the node, to add the variant to
	 *  @param relationshipName the name of the relationship
	 *  @param childKey the expected child key
	 *  @param request the request collecting all the changes to the service
	 *  
	 *  @return null, or the child node created
	 */
	private ContentNodeI checkForChild(ContentValidationDelegate delegate,
			ContentServiceI service, DraftContext draftContext,  ContentNodeI node,
			String relationshipName, ContentKey childKey, CmsRequestI request) {
		
		RelationshipI rel = (RelationshipI) node.getAttribute(relationshipName);
		if (rel.getValue() != null && ((List<Object>) rel.getValue()).contains(childKey)) {
			return null;
		}
		
		if (request == null) {
			delegate.record(node.getKey(), relationshipName, "Expected child " + childKey.getEncoded());
			return null;
		}

		// create a new recipe variant
		ContentNodeI childNode = service.getContentNode(childKey, draftContext);
		if (childNode == null) {
			childNode = service.createPrototypeContentNode(childKey, draftContext);
		}

		ContentNodeUtil.addRelationshipKey(rel, childNode.getKey());
		
		return childNode;

	}
	
}