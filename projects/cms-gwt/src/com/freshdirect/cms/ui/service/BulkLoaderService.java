package com.freshdirect.cms.ui.service;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bulkLoaderService")
public interface BulkLoaderService extends RemoteService {
	public GwtBulkLoadHeader getPreviewHeader();

	public PagingLoadResult<GwtBulkLoadRow> getPreviewRows(PagingLoadConfig config);

	public boolean hasAnyError();

	GwtSaveResponse save() throws ServerException;
}
