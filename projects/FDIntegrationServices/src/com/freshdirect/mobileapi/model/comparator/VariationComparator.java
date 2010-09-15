package com.freshdirect.mobileapi.model.comparator;

import java.util.Comparator;

import com.freshdirect.fdstore.FDVariation;

/**
 * Comparator described in i_product_initialize.jspf
 * @author fgarcia
 *
 */

public class VariationComparator implements Comparator<FDVariation> {

    @Override
    public int compare(FDVariation var1, FDVariation var2) {
        // required before optional
        if (var1.isOptional() && !var2.isOptional()) {
            return 1;
        } else if (var2.isOptional() && !var1.isOptional()) {
            return -1;
        } else if (var1.isOptional() && var2.isOptional()) {
            // menus before checkboxes
            if ("checkbox".equalsIgnoreCase(var1.getDisplayFormat()) && !"checkbox".equalsIgnoreCase(var2.getDisplayFormat())) {
                return 1;
            } else if ("checkbox".equalsIgnoreCase(var2.getDisplayFormat()) && !"checkbox".equalsIgnoreCase(var1.getDisplayFormat())) {
                return -1;
            } else {
                // like elements are ordered by priority
                if (var1.getPriority() < var2.getPriority()) {
                    return 1;
                } else if (var1.getPriority() > var2.getPriority()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

}
