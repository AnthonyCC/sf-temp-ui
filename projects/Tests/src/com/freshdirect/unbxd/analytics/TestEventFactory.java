package com.freshdirect.unbxd.analytics;

import java.util.Collections;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.ProductModelImpl;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.webapp.unbxdanalytics.event.AddToCartEvent;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.BrowseEvent;
import com.freshdirect.webapp.unbxdanalytics.event.ClickThruEvent;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.event.OrderEvent;
import com.freshdirect.webapp.unbxdanalytics.event.SearchEvent;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public class TestEventFactory {
    public static BrowseEvent createBrowseEvent(Visitor visitor, LocationInfo loc) {
        CategoryModel cat = new CategoryModel(ContentKey.getContentKey("Category:apl_apl"));
        BrowseEvent event = (BrowseEvent) AnalyticsEventFactory.createEvent(AnalyticsEventType.BROWSE, visitor, loc, null, cat, null);

        return event;
    }

    public static SearchEvent createSearchEvent(Visitor visitor, LocationInfo loc) {
        SearchEvent event = (SearchEvent) AnalyticsEventFactory.createEvent(AnalyticsEventType.SEARCH, visitor, loc, "milk", null, null);
        return event;
    }

    public static ClickThruEvent createClickEvent(Visitor visitor, LocationInfo loc) {
        ProductModel prd = new ProductModelImpl(ContentKey.getContentKey("Product:veg_fd_fgavcrdypk"));
        ClickThruEvent event = (ClickThruEvent) AnalyticsEventFactory.createEvent(AnalyticsEventType.CLICK_THRU, visitor, loc, null, prd, null);
        return event;
    }

    public static AddToCartEvent createAddToCartEvent(Visitor visitor, LocationInfo loc) {
        UserContext uCtx = UserContext.createUserContext(CmsManager.getInstance().getEStoreEnum());
        FDConfiguration conf = new FDConfiguration(1.0, "EA");
        ProductModel prd = new ProductModelImpl(ContentKey.getContentKey("Product:veg_fd_fgavcrdypk"));
        FDSku sku = new FDSku("VEG1075041", 1);
        FDCartLineI cartline = new FDCartLineModel(sku, prd, conf, "1", null, false, null, uCtx, Collections.<ErpClientCode> emptyList());
        AddToCartEvent event = (AddToCartEvent) AnalyticsEventFactory.createEvent(AnalyticsEventType.ATC, visitor, loc, null, null, cartline);
        return event;
    }

    public static OrderEvent createOrderEvent(Visitor visitor, LocationInfo loc) {
        UserContext uCtx = UserContext.createUserContext(CmsManager.getInstance().getEStoreEnum());
        FDConfiguration conf = new FDConfiguration(1.0, "EA");
        ProductModel prd = new ProductModelImpl(ContentKey.getContentKey("Product:veg_fd_fgavcrdypk"));
        FDSku sku = new FDSku("VEG1075041", 1);
        FDCartLineI cartline = new FDCartLineModel(sku, prd, conf, "1", null, false, null, uCtx, Collections.<ErpClientCode> emptyList());

        OrderEvent event = (OrderEvent) AnalyticsEventFactory.createEvent(AnalyticsEventType.ORDER, visitor, loc, null, null, cartline);
        return event;
    }
}
