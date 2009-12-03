package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.attributes.MultiAttribute;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.DomainValueRef;
import com.freshdirect.fdstore.content.SkuModel;


/**
 * Comparator described in i_product_initialize.jspf
 * @author fgarcia
 *
 */
public class SkuDomainValueComparator implements Comparator<SkuModel> {

    @Override
    public int compare(SkuModel sku1, SkuModel sku2) {
        MultiAttribute atr1 = (MultiAttribute) sku1.getAttribute("VARIATION_MATRIX");
        MultiAttribute atr2 = (MultiAttribute) sku2.getAttribute("VARIATION_MATRIX");

        DomainValue dv1 = ((DomainValueRef) atr1.getValue(0)).getDomainValue();
        DomainValue dv2 = ((DomainValueRef) atr2.getValue(0)).getDomainValue();
        int diff = dv1.getPriority() - dv2.getPriority();
        // sort by secondary domain
        if (diff == 0 && (((List) atr1.getValue()).size() > 1) && (((List) atr2.getValue()).size() > 1)) {
            dv1 = ((DomainValueRef) atr1.getValue(1)).getDomainValue();
            dv2 = ((DomainValueRef) atr2.getValue(1)).getDomainValue();
            diff = dv1.getPriority() - dv2.getPriority();
            if (diff == 0) {
                return dv1.toString().compareTo(dv2.toString());
            }
        } else if (diff == 0) {
            return dv1.toString().compareTo(dv2.toString());
        }
        return diff < 0 ? -1 : 1;
    }

}
