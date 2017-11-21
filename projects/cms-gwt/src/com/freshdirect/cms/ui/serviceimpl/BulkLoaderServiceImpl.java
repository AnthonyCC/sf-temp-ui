package com.freshdirect.cms.ui.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.ui.editor.bulkloader.BulkLoadPreviewSessionStore;
import com.freshdirect.cms.ui.editor.bulkloader.service.ContentBulkLoaderService;
import com.freshdirect.cms.ui.editor.permission.service.PermissionService;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadCell;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;
import com.freshdirect.cms.ui.service.BulkLoaderService;
import com.freshdirect.cms.ui.service.ServerException;

public class BulkLoaderServiceImpl extends GwtServiceBase implements BulkLoaderService {

    private static final long serialVersionUID = 3988173646720452184L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkLoaderServiceImpl.class);

    private PermissionService permissionService = EditorServiceLocator.permissionService();

    private ContentBulkLoaderService contentBulkLoaderService = EditorServiceLocator.contentBulkLoaderService();

    public BulkLoaderServiceImpl() {
        super();
    }

    @Override
    public GwtBulkLoadHeader getPreviewHeader() {
        GwtBulkLoadHeader header = BulkLoadPreviewSessionStore.getPreviewHeader(getThreadLocalRequest().getSession());

        if (header == null)
            throw new RuntimeException("Bulk load preview header have not been loaded correctly");

        return header;
    }

    @Override
    public PagingLoadResult<GwtBulkLoadRow> getPreviewRows(PagingLoadConfig config) {
        List<GwtBulkLoadRow> rows = getBulkLoadRows();

        List<GwtBulkLoadRow> sublist = new ArrayList<GwtBulkLoadRow>();
        int start = config.getOffset();
        int limit = rows.size();
        if (config.getLimit() > 0) {
            limit = Math.min(start + config.getLimit(), limit);
        }
        for (int i = config.getOffset(); i < limit; i++) {
            sublist.add(rows.get(i));
        }
        return new BasePagingLoadResult<GwtBulkLoadRow>(sublist, config.getOffset(), rows.size());
    }

    @Override
    public boolean hasAnyError() {
        List<GwtBulkLoadRow> rows = getBulkLoadRows();

        for (GwtBulkLoadRow row : rows) {
            for (GwtBulkLoadCell cell : row.getCells()) {
                if (cell.getStatus().getState().isError())
                    return true;
            }
        }

        return false;
    }

    private List<GwtBulkLoadRow> getBulkLoadRows() {

        List<GwtBulkLoadRow> rows = BulkLoadPreviewSessionStore.getPreviewRows(getThreadLocalRequest().getSession());

        if (rows == null)
            throw new RuntimeException("Bulk load preview rows have not been loaded correctly");
        return rows;
    }

    @Override
    public GwtSaveResponse save() throws ServerException {
        CmsServiceLocator.draftContextHolder().setDraftContext(getDraftContext());
        if (!permissionService.isNodeModificationEnabled()) {
            throw new ServerException("Can't save nodes as a publish is in progress");
        }

        List<GwtBulkLoadRow> rows = getBulkLoadRows();
        try {
            return contentBulkLoaderService.save(getUser(), rows);
        } catch (RuntimeException e) {
            LOGGER.error("error saving changes", e);
            throw new ServerException("error saving changes", e);
        }

    }
}
