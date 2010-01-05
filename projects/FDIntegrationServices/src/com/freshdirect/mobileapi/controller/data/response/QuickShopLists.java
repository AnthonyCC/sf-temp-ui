package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.CustomerCreatedList;
import com.freshdirect.mobileapi.model.OrderInfo;
import com.freshdirect.mobileapi.util.CommonFormatter;

/**
 * Generic form for lists used in Quickshop
 * @author Rob
 *
 */
public class QuickShopLists extends Message {

    private static final Logger LOG = Logger.getLogger(QuickShopLists.class);

    private List<QuickShopList> lists = new ArrayList<QuickShopList>();

    private Integer totalResultCount = 0;    
    
    /**
     * @param lists
     * @return
     */
    public static QuickShopLists initWithOrder(List<OrderInfo> lists) {
        QuickShopLists newInstance = new QuickShopLists();
        for (OrderInfo list : lists) {
            try {
                newInstance.lists.add(new QuickShopList(list.getId(), newInstance.formatter.format(list.getRequestedDate()),
                        CommonFormatter.formatCurrency(list.getTotal())));
            } catch (PricingException e) {
                //If we get error on an order, just move on...
                LOG.warn("Unable to retrieve total amount for order: " + list.getId(), e);
            }
        }
        return newInstance;
    }

    public Integer getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(Integer totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    /**
     * @param lists
     * @return
     */
    public static QuickShopLists initWithList(List<CustomerCreatedList> lists) {
        QuickShopLists newInstance = new QuickShopLists();
        for (CustomerCreatedList list : lists) {
            newInstance.lists.add(new QuickShopList(list.getId(), list.getName(), Integer.toString(list.getCount())));
        }
        return newInstance;
    }

    public List<QuickShopList> getLists() {
        return lists;
    }

    public void setLists(List<QuickShopList> lists) {
        this.lists = lists;
    }

    public static class QuickShopList {

        public QuickShopList(String id, String label, String caption) {
            this.label = label;
            this.caption = caption;
            this.id = id;
        }

        private String id;

        private String label;

        private String caption;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            this.caption = caption;
        }
    }

}
