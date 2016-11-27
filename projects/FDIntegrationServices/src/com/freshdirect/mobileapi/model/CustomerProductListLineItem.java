package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;

public class CustomerProductListLineItem {

    private FDCustomerListItem target;

    /**
     * @param target
     * @return
     */
    public static CustomerProductListLineItem wrap(FDCustomerListItem target) {
        CustomerProductListLineItem newInstance = new CustomerProductListLineItem();
        newInstance.target = target;
        return newInstance;
    }
    public static List<CustomerProductListLineItem> wrap(List<FDCustomerListItem> lineItems) {
        List<CustomerProductListLineItem> items = new ArrayList<CustomerProductListLineItem>();
        for (FDCustomerListItem lineItem : lineItems) {
            items.add(wrap(lineItem));
        }
        return items;
    }
}
