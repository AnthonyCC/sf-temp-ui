package com.freshdirect.cms.ui.service;

import com.freshdirect.cms.ui.model.AdminProcStatus;
import com.freshdirect.cms.ui.model.publish.PublishType;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdminServiceAsync {

    public void rebuildIndexes(AsyncCallback<AdminProcStatus> callback);

    public void getBuildIndexStatus(AsyncCallback<AdminProcStatus> callback);

    public void abortStuckPublishFlows(PublishType type, AsyncCallback<AdminProcStatus> callback);
}
