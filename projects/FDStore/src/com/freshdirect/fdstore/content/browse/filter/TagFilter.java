package com.freshdirect.fdstore.content.browse.filter;

import java.util.Set;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductFilterModel;
import com.freshdirect.storeapi.content.TagModel;

public class TagFilter extends AbstractProductItemFilter {
	
	private TagModel includeValue;
	
	public TagFilter(ProductFilterModel filterModel, String parentId) {
		super(filterModel, parentId);
		includeValue = filterModel.getTag();
	}

	public TagFilter(TagModel tagModel, String parentId) {
		super(tagModel.getContentName(), parentId, tagModel.getName());
		includeValue = tagModel;
	}

    @Override
    public boolean apply(FilteringProductItem prod) throws FDResourceException {
        if (prod != null && prod.getProductModel() != null) {
            Set<TagModel> allTags = prod.getProductModel().getAllTags();
            if (allTags.contains(includeValue)) {
                return invertChecker(true);
            }
        }

        return invertChecker(false);
    }

	@Override
	public FilterCacheStrategy getCacheStrategy() {
		return FilterCacheStrategy.CMS_ONLY;
	}

}
