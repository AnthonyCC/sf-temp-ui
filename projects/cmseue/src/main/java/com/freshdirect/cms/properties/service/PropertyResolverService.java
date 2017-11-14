package com.freshdirect.cms.properties.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertyResolverService {

    @Value("${cms.adminapp.path}")
    private String cmsAdminServiceUri;

    @Value("${cms.adminapp.ui.url}")
    private String cmsAdminUiUri;

    @Value("${cms.index.path}")
    private String cmsIndexPath;

    @Value("${fdstore.feed.publish.url:http://localhost:7001/crm/feed_publish}")
    private String feedPublishUri;

    @Value("${gmaps.api.key:AIzaSyAALx7g2uVEDP46IaGU_zxYT5gBSKac2ks}")
    private String googleMapsApiKey;

    public String getCmsAdminServiceUri() {
        return cmsAdminServiceUri;
    }

    public String getCmsAdminUiUri() {
        return cmsAdminUiUri;
    }

    public String getCmsIndexPath() {
        return cmsIndexPath;
    }

    public String getFeedPublishUri() {
        return feedPublishUri;
    }

    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }

}
