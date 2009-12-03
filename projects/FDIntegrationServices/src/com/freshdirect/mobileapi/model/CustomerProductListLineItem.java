package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;

public class CustomerProductListLineItem {

    private FDCustomerProductListLineItem target;

    /**
     * @param target
     * @return
     */
    public static CustomerProductListLineItem wrap(FDCustomerProductListLineItem target) {
        CustomerProductListLineItem newInstance = new CustomerProductListLineItem();
        newInstance.target = target;
        return newInstance;
    }

    public static List<CustomerProductListLineItem> wrap(List<FDCustomerProductListLineItem> lineItems) {
        List<CustomerProductListLineItem> items = new ArrayList<CustomerProductListLineItem>();
        for (FDCustomerProductListLineItem lineItem : lineItems) {
            items.add(wrap(lineItem));
        }
        return items;
    }
}
