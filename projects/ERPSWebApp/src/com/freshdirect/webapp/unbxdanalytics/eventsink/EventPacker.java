package com.freshdirect.webapp.unbxdanalytics.eventsink;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductModelImpl;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class EventPacker {

    private static final Logger LOGGER = LoggerFactory.getInstance(EventPacker.class);

    EventPacker() {
    }

    /**
     * Pack up event details and make the query part of the URL of web request
     * 
     * @param event
     * @return
     */
    public static String pack(AnalyticsEventI event) {

        // Assemble 'data' part of payload

        String jsonPayload = null;
        // do the JSON
        try {
            Writer writer = new StringWriter();
            new ObjectMapper().writeValue(writer, event);
            jsonPayload = writer.toString();

            LOGGER.debug(jsonPayload);
        } catch (JsonGenerationException e) {
            LOGGER.error(e);
        } catch (JsonMappingException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        }

        Map<String, String> queryComponents = new HashMap<String, String>();
        if (jsonPayload != null) {
            try {
                queryComponents.put("data", URLEncoder.encode(jsonPayload, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }

        //
        final long timestamp = System.currentTimeMillis();

        queryComponents.put("UnbxdKey", FDStoreProperties.getUnbxdSiteKey());
        queryComponents.put("action", event.getType().getAction());
        queryComponents.put("uid", event.getUid());
        queryComponents.put("t", Long.toString(timestamp));

        Collection<String> params = new ArrayList<String>(queryComponents.size());
        for (Map.Entry<String, String> e : queryComponents.entrySet()) {
            params.add(e.getKey() + "=" + e.getValue());
        }

        final String query = StringUtil.join(params, "&");

        // LOGGER.info(query);

        return query;
    }

    /**
     * Test code
     */
    public static void main(String[] args) {

        final Visitor visitor = new Visitor("1234");
        final LocationInfo loc = LocationInfo.withUrl("http://test.com");

        String query;

        // VISITOR
        AnalyticsEventI event = AnalyticsEventFactory.createEvent(AnalyticsEventType.VISITOR, visitor, loc, null, null, null);
        query = pack(event);
        LOGGER.debug(query);

        // BROWSE
        CategoryModel cat = new CategoryModel(ContentKey.decode("Category:apl_apl"));
        event = AnalyticsEventFactory.createEvent(AnalyticsEventType.BROWSE, visitor, loc, null, cat, null);
        query = pack(event);
        LOGGER.debug(query);

        // SEARCH
        event = AnalyticsEventFactory.createEvent(AnalyticsEventType.SEARCH, visitor, loc, "milk", null, null);
        query = pack(event);
        LOGGER.debug(query);

        // CLICK
        ProductModel prd = new ProductModelImpl(ContentKey.decode("Product:veg_fd_fgavcrdypk"));
        event = AnalyticsEventFactory.createEvent(AnalyticsEventType.CLICK_THRU, visitor, loc, null, prd, null);
        query = pack(event);
        LOGGER.debug(query);

        // ATC
        UserContext uCtx = UserContext.createUserContext(CmsManager.getInstance().getEStoreEnum());
        FDConfiguration conf = new FDConfiguration(1.0, "EA");
        FDSku sku = new FDSku("VEG1075041", 1);
        FDCartLineI cartline = new FDCartLineModel(sku, prd, conf, "1", null, false, null, uCtx, Collections.<ErpClientCode> emptyList());
        event = AnalyticsEventFactory.createEvent(AnalyticsEventType.ATC, visitor, loc, null, null, cartline);
        query = pack(event);
        LOGGER.debug(query);

        // ORDER
        event = AnalyticsEventFactory.createEvent(AnalyticsEventType.ORDER, visitor, loc, null, null, cartline);
        query = pack(event);
        LOGGER.debug(query);
    }
}
