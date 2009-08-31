package com.freshdirect.cms.ui.client;

import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.service.BaseCallback;
import com.freshdirect.cms.ui.service.ContentService;
import com.freshdirect.cms.ui.service.ContentServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */

public class CmsGwt implements EntryPoint {

    private MainLayout mainLayout;

    private static GwtUser currentUser;
    private static ContentServiceAsync contentService;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        contentService = (ContentServiceAsync) GWT.create(ContentService.class);
        contentService.getUser(new BaseCallback<GwtUser>() {
            @Override
            public void onSuccess(GwtUser result) {
                CmsGwt.currentUser = result;
                mainLayout.userChanged();
            }
        });

        mainLayout = new MainLayout();
        RootPanel.get().add(mainLayout);
    }

    public static GwtUser getCurrentUser() {
        return currentUser;
    }

    public static ContentServiceAsync getContentService() {
        return contentService;
    }
}
