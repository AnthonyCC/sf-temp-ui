package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.mobileapi.model.comparator.PriceComparator;
import com.freshdirect.mobileapi.model.comparator.SkuSecondaryDomainValueComparator;

public class ProductDomain {
    private DomainValue domainValue;

    private List<Sku> skus = new ArrayList<Sku>();

    private Boolean allSamePrice;

    private Boolean allSameOption;

    private Product product;

    public ProductDomain(Product product, DomainValue domainValue) {
        this.product = product;
        this.domainValue = domainValue;
    }

    public void addSku(Sku sku) {
        skus.add(sku);
    }

    public List<Sku> getSkus() {
        if (product.hasSecondaryDomain()) {
            Collections.sort(skus, new SkuSecondaryDomainValueComparator());
        }

        return skus;
    }

    public boolean isAllSamePrice() {
        if (allSamePrice == null) {
            allSamePrice = false;
            if (product.hasSecondaryDomain() || skus.size() > 1) {
                PriceComparator comparator = new PriceComparator();
                comparator.reset();
                Collections.sort(skus, comparator);
                allSamePrice = comparator.allTheSame();
            }
        }
        return allSamePrice;
    }

    public boolean isAllSameOption() {
        if (allSameOption == null) {
            allSameOption = false;
            Sku firstSku = skus.get(0);
            if (firstSku.hasVariationOptions()) {
                String dvId = firstSku.getDomainValueId();

                if (dvId != null) {
                    Iterator<Sku> skuIter = skus.iterator();
                    while (skuIter.hasNext()) {
                        Sku sku = skuIter.next();
                        allSameOption &= dvId.equals(sku.getDomainValueId());
                    }
                }
            }

        }
        return allSameOption;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public String getDomainValueLabel() {
        return domainValue.getLabel();
    }

}
