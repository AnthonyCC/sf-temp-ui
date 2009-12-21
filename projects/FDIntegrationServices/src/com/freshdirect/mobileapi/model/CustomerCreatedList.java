package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.fdstore.lists.FDCustomerCreatedList;

/**
 * Wrapper for FDCustomerCreatedList 
 * 
 * @author Rob
 *
 */
public class CustomerCreatedList {

    private FDCustomerCreatedList target;

    /**
     * @param list
     * @return
     */
    public static CustomerCreatedList wrap(FDCustomerCreatedList list) {
        CustomerCreatedList newInstance = new CustomerCreatedList();
        newInstance.target = list;

        return newInstance;
    }

    /**
     * @param lists
     * @return
     */
    public static List<CustomerCreatedList> wrap(List<FDCustomerCreatedList> lists) {
        List<CustomerCreatedList> wrappedItems = new ArrayList<CustomerCreatedList>();
        
        TreeSet<FDCustomerCreatedList> sortedLists = new TreeSet<FDCustomerCreatedList>(FDCustomerCreatedList.getModificationDateComparator());
        sortedLists.addAll(lists);

        for (FDCustomerCreatedList list : sortedLists) {
            wrappedItems.add(wrap(list));
        }
        return wrappedItems;
    }

    /**
     * @return
     */
    public String getName() {
        return this.target.getName();
    }

    /**
     * @return
     */
    public int getCount() {
        return this.target.getCount();
    }

    /**
     * @return
     */
    public String getId() {
        return this.target.getId();
    }

    public List<CustomerProductListLineItem> getLineItems() {
        return CustomerProductListLineItem.wrap(this.target.getLineItems());
    }

}
