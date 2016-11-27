package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;
import java.util.List;

import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.SkuModel;


/**
 * Comparator described in i_product_initialize.jspf
 * @author fgarcia
 *
 */
public class SkuDomainValueComparator implements Comparator<SkuModel> {

    @Override
    public int compare(SkuModel sku1, SkuModel sku2) {
        List<DomainValue> atr1 = sku1.getVariationMatrix();
        List<DomainValue> atr2 = sku2.getVariationMatrix();

        DomainValue dv1 = atr1.get(0);
        DomainValue dv2 = atr2.get(0);
        int diff = dv1.getPriority() - dv2.getPriority();
        // sort by secondary domain
        if (diff == 0 && (atr1.size() > 1) && (atr2.size() > 1)) {
            dv1 = atr1.get(1);
            dv2 = atr2.get(1);
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
