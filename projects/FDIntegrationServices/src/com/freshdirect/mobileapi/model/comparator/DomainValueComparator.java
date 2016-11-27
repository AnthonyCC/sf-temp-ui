package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;

import com.freshdirect.fdstore.content.DomainValue;

/**
 * Comparator described in i_product_initialize.jspf
 * @author fgarcia
 *
 */
public class DomainValueComparator implements Comparator<DomainValue> {

    @Override
    public int compare(DomainValue dv1, DomainValue dv2) {
        int diff = dv1.getPriority() - dv2.getPriority();
        if (diff == 0) {
            return dv1.toString().compareTo(dv2.toString()) ;
        }
        return diff<0 ? -1 : 1;
    }

}
