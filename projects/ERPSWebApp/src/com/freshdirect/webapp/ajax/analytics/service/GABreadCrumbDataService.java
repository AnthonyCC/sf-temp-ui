package com.freshdirect.webapp.ajax.analytics.service;

import java.util.List;
import java.util.Map;

public class GABreadCrumbDataService {

    private static final GABreadCrumbDataService INSTANCE = new GABreadCrumbDataService();

    private static final String DIVIDER = " / ";

    private GABreadCrumbDataService() {

    }

    public static GABreadCrumbDataService defaultService() {
        return INSTANCE;
    }

    public String populateBreadCrumbData(Map<String, Object> breadCrumbs) {
        StringBuilder formattedBreadCrumbs = new StringBuilder();

        if (breadCrumbs != null) {
            for (String key : breadCrumbs.keySet()) {
                List<Map<String, String>> breadCrumbList = (List<Map<String, String>>) breadCrumbs.get(key);
                for (Map<String, String> breadCrumb : breadCrumbList) {
                    formattedBreadCrumbs.append(breadCrumb.get("name"));
                    formattedBreadCrumbs.append(DIVIDER);
                }
            }
            formattedBreadCrumbs.setLength(Math.max(formattedBreadCrumbs.length() - DIVIDER.length(), 0));
        }

        return formattedBreadCrumbs.toString();
    }

}
