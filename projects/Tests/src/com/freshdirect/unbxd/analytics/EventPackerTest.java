package com.freshdirect.unbxd.analytics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.eventsink.EventPacker;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;


public class EventPackerTest {

    Visitor visitor;
    LocationInfo location;
    
    @Before
    public void setUp() throws Exception {
        visitor = new Visitor("test");
        location = LocationInfo.withUrl("http://test/location");
    }

    @Test
    public void testVisitorEvent() throws JsonProcessingException, IOException {
        AnalyticsEventI event = AnalyticsEventFactory.createEvent(AnalyticsEventType.VISITOR, visitor, location, null, null, null);

        final String query =  EventPacker.pack(event);
        
        testBasicProperties(query);
    }


    // FIXME : test crashes due to missing CMS data 
    @Test
    public void testBrowseEvent() {
        AnalyticsEventI event = TestEventFactory.createBrowseEvent(visitor, location);
        
        final String query =  EventPacker.pack(event);
        
        testBasicProperties(query);

    }
    
    @Test
    public void testSearchEvent() {
        AnalyticsEventI event = TestEventFactory.createSearchEvent(visitor, location);

        final String query =  EventPacker.pack(event);
        
        testBasicProperties(query);
    }
    
    // FIXME : test crashes due to missing CMS data 
    @Test
    public void testClickEvent() {
        AnalyticsEventI event = TestEventFactory.createClickEvent(visitor, location);

        final String query =  EventPacker.pack(event);
        
        testBasicProperties(query);

    }
    
    // FIXME : test crashes due to missing CMS data 
    @Test
    public void testAddToCartEvent() {
        AnalyticsEventI event = TestEventFactory.createAddToCartEvent(visitor, location);

        final String query =  EventPacker.pack(event);
        
        testBasicProperties(query);

    }
    
    // FIXME : test crashes due to missing CMS data 
    @Test
    public void testOrderEvent() {
        AnalyticsEventI event = TestEventFactory.createOrderEvent(visitor, location);

        final String query =  EventPacker.pack(event);
        
        testBasicProperties(query);

    }

    private void testBasicProperties(final String query) {
        // test input
        assertNotNull(query);
        
        final Map<String,String> params = unpackQuery(query);
        
        assertNotNull(params.get("data"));

        JsonNode result = unpackJsonData(params.get("data"));

        // test visitor
        assertEquals(visitor.getUID(), params.get("uid"));
        assertNotNull(result.get("visit_type"));
        assertEquals(visitor.getVisitType(), result.get("visit_type").asText());
        assertNotNull(result.get("url"));

        // test location info
        assertEquals(location.url, result.get("url").asText());
        assertNull(result.get("referer"));
        
        // -- test other query params --
        assertNotNull(params.get("uid"));
        
        assertNotNull(params.get("UnbxdKey"));
        assertNotNull(params.get("action"));
        assertNotNull(params.get("t"));
    }
    
    private Map<String,String> unpackQuery(String query) {
        String[] paramsArray = query.split("&");
        assertNotNull(paramsArray);
        
        Map<String,String> params = new HashMap<String,String>();
        for (String p : paramsArray) {
            String[] pair = p.split("=");
            assertNotNull(pair);
            try {
                params.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
        return params;
    }

    private JsonNode unpackJsonData(String rawJsonString) {
        JsonNode result = null;
        
        // test input
        assertNotNull(rawJsonString);
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readTree(rawJsonString);
        } catch (JsonProcessingException e) {
        } catch (IOException e) {
        }
        
        // test output
        assertNotNull(result);
        
        return result;
    }
}
