package com.freshdirect.fdstore.content.sort;

import java.util.Comparator;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.smartstore.sorting.ScriptedContentNodeComparator;
import com.freshdirect.storeapi.content.ComparatorChain;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;

public class CustomerPopularityComparator implements Comparator<ContentNodeModel> {

    private ComparatorChain<ProductModel> comparator;

    public CustomerPopularityComparator(String userId, PricingContext pricingContext) {
        this.comparator = ComparatorChain.create(ScriptedContentNodeComparator.createUserComparator(userId, pricingContext))
                .chain(ScriptedContentNodeComparator.createGlobalComparator(null, null));
    }

    @Override
    public int compare(ContentNodeModel o1, ContentNodeModel o2) {
        ProductModel prod1 = o1 instanceof ProductModel ? (ProductModel) o1 : (o1 instanceof SkuModel ? ((SkuModel) o1).getProductModel() : null);
        ProductModel prod2 = o2 instanceof ProductModel ? (ProductModel) o2 : (o2 instanceof SkuModel ? ((SkuModel) o2).getProductModel() : null);

        if (prod1 == null && prod2 == null)
            return 0;
        else if (prod1 == null)
            return 1;
        else if (prod2 == null)
            return -1;

        return comparator.compare(prod1, prod2);
    }

    @Override
    public String toString() {
        return CustomerPopularityComparator.class.getSimpleName();
    }

}
