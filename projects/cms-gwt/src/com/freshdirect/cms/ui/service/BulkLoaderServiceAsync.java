package com.freshdirect.cms.ui.service;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BulkLoaderServiceAsync {
	public void getPreviewHeader(AsyncCallback<GwtBulkLoadHeader> callback);

	public void getPreviewRows(PagingLoadConfig config, AsyncCallback<PagingLoadResult<GwtBulkLoadRow>> callback);

	void hasAnyError(AsyncCallback<Boolean> callback);

	void save(AsyncCallback<GwtSaveResponse> callback);
}
