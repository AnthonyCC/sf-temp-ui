package com.freshdirect.webapp.unbxdanalytics.event;

import java.util.LinkedList;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class AnalyticsEventFactory {

    /**
     * Create UNBXD analytics event based on input
     *
     * @param type
     *            event type (required)
     * @param visitor
     *            visitor info (required)
     * @param loc
     *            site location (required)
     * @param searchQuery
     *            query term (SEARCH only)
     * @param model
     *            either product (CLICK_THRU) or department / category (BROWSE only)
     * @param cartline
     *            cartline or orderline (ATC and ORDER only)
     * 
     * @return created event
     * 
     * @throws IllegalArgumentException
     *             if input is either null or unavailable for the particular event type
     * 
     * @see AnalyticsEventType
     * @see Visitor
     * @see LocationInfo
     */
    public static AnalyticsEventI createEvent(AnalyticsEventType type, Visitor visitor, LocationInfo loc, String searchQuery, ContentNodeModel model, FDCartLineI cartline) {
        if (type == null && visitor == null || loc == null) {
            throw new IllegalArgumentException("Missing event type, visitor and/or location info");
        }

        AnalyticsEventI event = null;

        switch (type) {
            case VISITOR:

                event = new VisitorEvent(visitor, loc);

                break;
            case BROWSE:

                if (model instanceof ProductContainer) {
                    final String breadcrumb = createBreadCrumb((ProductContainer) model);
                    event = new BrowseEvent(visitor, loc, breadcrumb);
                } else {
                    throw new IllegalArgumentException("Content Node parameter is either missing or not a dept/category");
                }

                break;
            case SEARCH:

                if (searchQuery != null) {
                    event = new SearchEvent(visitor, loc, searchQuery);
                } else {
                    throw new IllegalArgumentException("Missing search term parameter");
                }

                break;
            case CLICK_THRU:

                if (model instanceof ProductModel) {
                    final ProductModel product = (ProductModel) model;

                    event = new ClickThruEvent(visitor, loc, product.getContentName(), product.getCategory().getContentName());
                } else {
                    throw new IllegalArgumentException("Content Node parameter is either null or not a product");
                }

                break;
            case ATC:

                if (cartline != null) {

                    event = new AddToCartEvent(visitor, loc, cartline.getSkuCode(), cartline.getQuantity());

                } else {
                    throw new IllegalArgumentException("Missing cartline parameter");
                }

                break;
            case ORDER:

                if (cartline != null) {

                    event = new OrderEvent(visitor, loc, cartline.getSkuCode(), cartline.getPrice(), cartline.getQuantity());

                } else {
                    throw new IllegalArgumentException("Missing cartline parameter");
                }
                break;
        }

        return event;
    }

    public static String createBreadCrumb(ProductContainer container) {
        LinkedList<String> ll = new LinkedList<String>();

        ContentNodeModel pathComponent = container;
        while (pathComponent != null) {
            if (FDContentTypes.STORE != pathComponent.getContentKey().getType()) {
                ll.push(pathComponent.getFullName());
            }
            pathComponent = pathComponent.getParentNode();
        }
        return StringUtil.join(ll, BrowseEvent.SEP);
    }
}
