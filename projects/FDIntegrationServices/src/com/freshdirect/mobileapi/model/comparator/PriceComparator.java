package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.mobileapi.model.Sku;

public class PriceComparator implements Comparator<Sku> {
    ProductModel.PriceComparator priceComparator = new ProductModel.PriceComparator();
    @Override
    public int compare(Sku o1, Sku o2) {
        return priceComparator.compare(o1.getOriginalSku(), o2.getOriginalSku());
    }

    public void reset() {
        priceComparator.reset();
    }

    public boolean allTheSame() {
        return priceComparator.allTheSame();
    }

}
