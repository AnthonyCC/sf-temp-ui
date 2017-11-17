package com.freshdirect.cms.ui.editor.bulkloader;

import java.util.List;

import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;

public class BulkUploadResult {

    private String status;
    private GwtBulkLoadHeader header;
    private List<GwtBulkLoadRow> previewRows;

    private BulkUploadResult(String status, GwtBulkLoadHeader header, List<GwtBulkLoadRow> previewRows) {
        this.status = status;
        this.header = header;
        this.previewRows = previewRows;
    }

    public BulkUploadResult(GwtBulkLoadHeader header, List<GwtBulkLoadRow> previewRows) {
        this("OK", header, previewRows);
    }

    public BulkUploadResult(Exception exc) {
        this(exc.getMessage(), null, null);
    }

    public String getStatus() {
        return status;
    }

    public GwtBulkLoadHeader getHeader() {
        return header;
    }

    public List<GwtBulkLoadRow> getPreviewRows() {
        return previewRows;
    }
}
