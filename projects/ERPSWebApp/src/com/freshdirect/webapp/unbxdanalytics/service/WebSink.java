package com.freshdirect.webapp.unbxdanalytics.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.eventsink.EventPacker;
import com.freshdirect.webapp.unbxdanalytics.eventsink.EventSinkI;

/**
 * UNBXD Analytics Web Client (aka WebSink)
 * 
 * @author segabor
 *
 */
public class WebSink implements EventSinkI {

    private static final String USER_AGENT = "FreshDirect-AnalyticsClient/1.0";

    private static final Logger LOGGER = LoggerFactory.getInstance(WebSink.class);

    @Override
    public boolean log(AnalyticsEventI event) {

        boolean result = false;

        final AnalyticsEventI visitorEvent = AnalyticsEventFactory.createVisitorEventFor(event);
        
        // check if a separate visitor event needs to be sent
        if (visitorEvent != null) {
            // send visitor event first
            if (!sendEvent(visitorEvent)) {
                LOGGER.error("Failed to sent visitor event, aborting ...");
                return false;
            }
        }

        // send the pooled event now
        result = sendEvent(event);

        return result;
    }

    private boolean sendEvent(AnalyticsEventI event) {

        boolean result = false;

        final HttpUriRequest request = createRequest(event);

        CloseableHttpClient httpclient = null;
        if (request != null) {
            httpclient = HttpClients.createDefault();
            try {
                LOGGER.debug("Executing request with URI: " + request.getURI());
                
                CloseableHttpResponse response = httpclient.execute(request);

                LOGGER.debug("UNBXD Tracking Response Status: " + response.getStatusLine());

                result = true;
            } catch (ClientProtocolException e) {
                LOGGER.error("Failed to deliver event " + event.getType() + " for UID " + event.getUid(), e);
            } catch (IOException e) {
                LOGGER.error("Failed to deliver event " + event.getType() + " for UID " + event.getUid(), e);
            } finally {
                if (httpclient != null) {
                    try {
                        httpclient.close();
                    } catch (IOException e) {
                        LOGGER.error("Failed to close HTTP client", e);
                    }
                }
            }

        }
        return result;
    }

    /**
     * Compose a web request to be sent to UNBXD tracking service
     * 
     * @param event
     *            Analytics Event
     * 
     * @return Web Request or null if
     */
    private HttpUriRequest createRequest(AnalyticsEventI event) {

        final String baseURL = FDStoreProperties.getUnbxdTrackingServiceBaseURL();

        final String query = EventPacker.pack(event);

        HttpUriRequest request = null;

        if (query != null) {
            // compose URL
            StringBuilder buf = new StringBuilder(baseURL);
            buf.append("?").append(query);

            request = new HttpHead(buf.toString());

            // add custom user agent
            request.addHeader("User-Agent", USER_AGENT);
        }

        return request;
    }
}
