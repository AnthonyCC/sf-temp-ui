package com.freshdirect.fdstore.content.browse.filter;

import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;
import com.freshdirect.storeapi.content.ProductContainer;

public class ContentNodeFilter extends AbstractProductItemFilter {

    private final Set<ContentKey> childrenKeys;

    public ContentNodeFilter(ProductContainer contentNodeModel, String parentId) { // 'virtual' contentnode for search page
        super(contentNodeModel.getContentName(), parentId, contentNodeModel.getFullName());
        childrenKeys = contentNodeModel.getAllChildProductKeys();
    }

    @Override
    public boolean apply(FilteringProductItem ctx) throws FDResourceException {
        if (ctx == null || (ctx.getProductModel() == null)) {
            return false;
        }

        boolean fits = childrenKeys.contains(ctx.getProductModel().getContentKey());

        return invertChecker(fits);
    }

    public FilterCacheStrategy getCacheStrategy() {
        return FilterCacheStrategy.CMS_ONLY;
    }

}
