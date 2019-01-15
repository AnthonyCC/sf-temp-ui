package com.freshdirect.fdstore.content.browse.filter;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.storeapi.content.AbstractProductItemFilter;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.FilterCacheStrategy;
import com.freshdirect.storeapi.content.FilteringProductItem;

public class DomainValueContentNodeFilter extends AbstractProductItemFilter {

    private ContentKey contentKey;

    public DomainValueContentNodeFilter(DomainValue domainValue, String parentId) { // 'virtual' contentnode for search page
        super(domainValue.getContentName(), parentId, domainValue.getLabel());
        this.contentKey = domainValue.getContentKey();
    }

    @Override
    public boolean apply(FilteringProductItem ctx) throws FDResourceException {
        if (ctx == null || ctx.getRecipe() == null) {
            return false;
        }

        boolean fits = false;
        for (DomainValue domainValue : ctx.getRecipe().getClassifications()) {
            if (contentKey.equals(domainValue.getContentKey())) {
                fits = true;
                break;
            }
        }

        return invertChecker(fits);
    }

    public FilterCacheStrategy getCacheStrategy() {
        return FilterCacheStrategy.CMS_ONLY;
    }

    public ContentKey getContentKey() {
        return contentKey;
    }

    public void setContentKey(ContentKey contentKey) {
        this.contentKey = contentKey;
    }
}
