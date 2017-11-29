package com.freshdirect.fdstore.content.browse.filter;

import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ContentNodeModelUtil;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;

public class ContentNodeFilter extends AbstractProductItemFilter {

	private static final String DOMAIN_VALUE_CONTENT_TYPE = "V";
	protected String contentType;
	protected ContentKey contentKey;
	private String contentNodeId;
	
//	Not defined in CMS, code builded for search redesign	
//	public ContentNodeFilter(ProductFilterModel model, String parentId) {
//		super(model, parentId);
//		
//		this.contentId = ;
//	}

	public ContentNodeFilter(ContentNodeModel contentNodeModel, String parentId) { //'virtual' contentnode for search page 
		
		super(contentNodeModel.getContentName(), parentId, contentNodeModel.getFullName());
		this.contentType = contentNodeModel.getContentType();
		this.contentKey = contentNodeModel.getContentKey();
		this.contentNodeId = contentNodeModel.getContentKey().getId();
		if (DOMAIN_VALUE_CONTENT_TYPE.equals(contentType)) {
			name = ((DomainValue) contentNodeModel).getLabel();
		}
	}

	@Override
	public boolean apply(FilteringProductItem ctx) throws FDResourceException {
		if (ctx == null || (ctx.getProductModel() == null && !DOMAIN_VALUE_CONTENT_TYPE.equals(contentType)) || (DOMAIN_VALUE_CONTENT_TYPE.equals(contentType) && ctx.getRecipe() == null)) {
			return false;
		}
		
		boolean fits = false;
		if ("D".equals(contentType) || "C".equals(contentType)) {
			fits = ContentNodeModelUtil.isDescendant(ctx.getProductModel().getContentKey(), contentKey) ;
		} else if (DOMAIN_VALUE_CONTENT_TYPE.equals(contentType)) {
			for (DomainValue domainValue : ctx.getRecipe().getClassifications()) {
				if (contentNodeId.equals(domainValue.getContentKey().getId())) {
					fits = true;
					break;
				}
			}
		}
		
		return invertChecker(fits);
	}


	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.CMS_ONLY;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ContentKey getContentKey() {
		return contentKey;
	}

	public void setContentKey(ContentKey contentKey) {
		this.contentKey = contentKey;
	}

	public String getContentNodeId() {
		return contentNodeId;
	}

	public void setContentNodeId(String contentNodeId) {
		this.contentNodeId = contentNodeId;
	}

}