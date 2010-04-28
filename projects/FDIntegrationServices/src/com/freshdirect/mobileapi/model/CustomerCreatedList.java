package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;

/**
 * Wrapper for FDCustomerCreatedList 
 * 
 * @author Rob
 *
 */
public class CustomerCreatedList {

    private FDCustomerList target;

    /**
     * @param list
     * @return
     */
    public static CustomerCreatedList wrap(FDCustomerList list) {
        CustomerCreatedList newInstance = new CustomerCreatedList();
        newInstance.target = list;

        return newInstance;
    }

    /**
     * @param lists
     * @return
     */
    public static List<CustomerCreatedList> wrap(List<FDCustomerList> lists) {
        List<CustomerCreatedList> wrappedItems = new ArrayList<CustomerCreatedList>();
        
        TreeSet<FDCustomerList> sortedLists = new TreeSet<FDCustomerList>(FDCustomerCreatedList.getModificationDateComparator());
        sortedLists.addAll(lists);

        for (FDCustomerList list : sortedLists) {
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
                                                 
        return CustomerProductListLineItem.wrap((List<FDCustomerListItem>)(List)this.target.getLineItems());
    }

}
