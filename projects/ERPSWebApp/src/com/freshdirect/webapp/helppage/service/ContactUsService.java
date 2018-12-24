package com.freshdirect.webapp.helppage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCSContactHoursUtil;
import com.freshdirect.webapp.util.FDFaqUtil;

public class ContactUsService {

    private static final ContactUsService INSTANCE = new ContactUsService();
    private static final String FAQ_PAGE = "req_feedback";

    private ContactUsService() {
    }

    public static ContactUsService defaultService() {
        return INSTANCE;
    }

    public Map<String, Object> collectContactUsFormData() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        collectRequestFeedbackParams(dataMap);
        dataMap.put("csHours", FDCSContactHoursUtil.getFDCSHours());
        return dataMap;
    }

    private void collectRequestFeedbackParams(Map<String, Object> dataMap) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("baseUrl", "");
        boolean isDefaultFtl = true;
        if (null != FDStoreProperties.getFaqSections()) {
            params.put("faqNodes", FDFaqUtil.getFaqsByCategory(FAQ_PAGE));
            StringTokenizer st = new StringTokenizer(FDStoreProperties.getFaqSections(), ",");
            while (st.hasMoreTokens()) {
                String nextToken = st.nextToken().trim();
                params.put(nextToken, FDFaqUtil.getFaqsByCategory(nextToken));
                if (nextToken.equalsIgnoreCase(FAQ_PAGE) && isDefaultFtl) {
                    isDefaultFtl = false;
                }
            }
            params.put("faqContact", "true");
        }
        dataMap.put("requestFeedbackParams", params);
    }
}
