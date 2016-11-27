package com.freshdirect.webapp.taglib.smartstore;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public abstract class TabHelperTag extends BodyTagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    TabRecommendation tabs;

    // @return number of tabs recommended
    public int getRecommendedTabs() {
        return this.tabs.size()/* nTabs */;
    }

    public TabRecommendation getTabs() {
        return tabs;
    }

    protected int getSelectedTab(HttpSession session, ServletRequest req) {
        final int numTabs = tabs.size();

        int selectedTab = 0; // default value
        Object selectedTabAttribute = session.getAttribute(SessionName.SS_SELECTED_TAB);

        boolean shouldStoreTabPos = selectedTabAttribute == null; // true == not
                                                                  // stored yet
        if (selectedTabAttribute != null) {
            // get the stored one if exist
            selectedTab = ((Integer) selectedTabAttribute).intValue();
        }

        if (selectedTab == -1) {
            shouldStoreTabPos = true;
            // try to calculate a good tab index
            if (session.getAttribute(SessionName.SS_SELECTED_VARIANT) != null) {
                String tabId = (String) session.getAttribute(SessionName.SS_SELECTED_VARIANT);
                
                selectedTab = tabs.getTabIndex(tabId);
                if (selectedTab == -1 && tabId.indexOf(',') != -1) {
                    String[] variants = tabId.split(",");
                    for (int i=0;i<variants.length && selectedTab == -1; i++) {
                        selectedTab = tabs.getTabIndex(variants[i]);
                        if (selectedTab != -1) {
                            session.setAttribute(SessionName.SS_SELECTED_VARIANT, variants[i]);
                        }
                    }
                }
            }
            // no success, fallback to 0
            if (selectedTab == -1) {
                selectedTab = 0;
            }
            // store in the session
        }

        // tab explicitly set
        String value = req.getParameter("tab");
        if (value != null && !"".equals(value)) {
            selectedTab = Integer.parseInt(value);
            shouldStoreTabPos = true;
        }

        if (selectedTab >= numTabs
                || (session.getAttribute(SessionName.SS_SELECTED_VARIANT) != null && !tabs.get(selectedTab).getId().equals(
                        session.getAttribute(SessionName.SS_SELECTED_VARIANT)))) {
            // reset if selection is out of tab range or the variant of selected
            // tab has changed
            selectedTab = 0;
            shouldStoreTabPos = true;
        }

        Integer iSelectedTab = new Integer(selectedTab);
        if (shouldStoreTabPos) {
            // store changed tab position in session
            session.setAttribute(SessionName.SS_SELECTED_TAB, iSelectedTab);
            session.setAttribute(SessionName.SS_SELECTED_VARIANT, tabs.get(selectedTab).getId());
        }
        pageContext.setAttribute("selectedTabIndex", iSelectedTab);

        return selectedTab;
    }

    protected SessionInput createSessionInput(HttpSession session, FDUserI user, int maxRecommendations) {
        SessionInput input = new SessionInput(user);
        input.setPreviousRecommendations((Map) session.getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));

        FDStoreRecommender.initYmalSource(input, user, pageContext.getRequest());
        input.setCurrentNode(input.getYmalSource());
        input.setMaxRecommendations(maxRecommendations);
        return input;
    }

}
