package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;

import com.freshdirect.mobileapi.model.Sku;

/**
 * Comparator described in i_product_initialize.jspf
 * @author fgarcia
 *
 */
public class SkuSecondaryDomainValueComparator implements Comparator<Sku> {

    @Override
    public int compare(Sku o1, Sku o2) {
        if (o1.getSecondaryDomainValuePriority() > o2.getSecondaryDomainValuePriority()) {
            return 1;
        } else {
            return (o1.getSecondaryDomainValuePriority() < o2.getSecondaryDomainValuePriority()) ? -1 : 0;
        }
    }

}
