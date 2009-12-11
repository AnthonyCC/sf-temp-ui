package com.freshdirect.mobileapi.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Category;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.framework.util.QueryStringBuilder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.model.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.GeneralCacheAdministratorFactory;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class Oas247Service implements OasService {

    private static final int REFRESH_PERIOD = 300;

    private static Category LOG = LoggerFactory.getInstance(Oas247Service.class);

    private static GeneralCacheAdministrator cacheAdmin = GeneralCacheAdministratorFactory.getCacheAdminInstance();

    private SessionUser user;

    /**
     * This can be injected configuration
     */
    private static String path = "adstream_sx.ads/iPhone/home@SystemMessage";

    private String queryString = "";

    @Override
    public Map<String, Object> getMessages() throws ServiceException {
        HttpClient client = new HttpClient();

        //This was declared static at first but added it inline to that it be refreshed on prop file update
        String server = FDStoreProperties.getAdServerUrl();
        if ((server != null) && (!server.toLowerCase().startsWith("http"))) {
            server = MobileApiProperties.getOasCommunicationProtocol() + "://" + server;
        }

        String fullpath = server + path + queryString;
        HttpMethod method = new GetMethod(fullpath);
        String cacheKey = fullpath;
        Map<String, Object> messages = null;
        try {
            messages = (Map<String, Object>) cacheAdmin.getFromCache(cacheKey, REFRESH_PERIOD);
        } catch (NeedsRefreshException nre) {

            try {
                int statusCode = client.executeMethod(method);
                if (statusCode != HttpStatus.SC_OK) {
                    LOG.error("Method failed: " + method.getStatusLine());
                } else {
                    byte[] responseBody = method.getResponseBody();
                    ObjectMapper mapper = new ObjectMapper();
                    messages = mapper.readValue(responseBody, 0, responseBody.length, HashMap.class);
                    cacheAdmin.putInCache(cacheKey, messages);
                }
            } catch (Throwable ex) {
                LOG.error("Throwable caught at cache update", ex);
                messages = (Map<String, Object>) nre.getCacheContent();
                LOG.debug("Cancelling cache update. Exception encountered.");
                cacheAdmin.cancelUpdate(cacheKey);
                if (null == messages) {
                    throw new ServiceException(ex.getMessage(), ex);
                }
            }
        }

        return messages;
    }

    @Override
    public Map<String, Object> getMessages(SessionUser user) throws ServiceException {
        if (user != null) {
            QueryStringBuilder builder = new QueryStringBuilder();

            try {
                ProfileModel profile = user.getFDSessionUser().getFDCustomer().getProfile();
                boolean test = profile.isOASTest();
                String lastOrderZone = user.getFDSessionUser().getOrderHistory().getLastOrderZone();
                Date lastOrderDlvDate = user.getFDSessionUser().getOrderHistory().getLastOrderDlvDate();
                String zip = user.getFDSessionUser().getAddress().getZipCode();
                int deliveredOrders = user.getFDSessionUser().getAdjustedValidOrderCount();
                String county = user.getFDSessionUser().getDefaultCounty();
                String marketingPromo = profile.getMarketingPromo();
                int chefTable = profile.isChefsTable() ? 1 : 0;
                String chefTableInductionDay = profile.getChefsTableInduction();
                String chefTableSegment = profile.getAttribute("ChefsTableSegment");
                builder.addParam("test", test);
                builder.addParam("lozn", lastOrderZone);
                builder.addParam("zip", zip);
                builder.addParam("county", county);
                builder.addParam("do", deliveredOrders);
                builder.addParam("nod", lastOrderDlvDate);
                builder.addParam("mktpro", marketingPromo);
                builder.addParam("ct", chefTable);
                builder.addParam("cti", chefTableInductionDay);
                builder.addParam("cts", chefTableSegment);

                String queryString = builder.toString();
                LOG.debug("QueryString: " + queryString);
                this.queryString = "?" + queryString;
            } catch (FDResourceException e) {
                throw new ServiceException(e);
            }
        }
        return getMessages();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SessionUser getUser() {
        return user;
    }

    public void setUser(SessionUser user) {
        this.user = user;
    }

}
