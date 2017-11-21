package com.freshdirect.cms.ui.editor.bulkloader;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;

public class BulkLoadPreviewSessionStore {

    public static final String BULKLOAD_PREVIEW_HEADER = "previewHeader";
    public static final String BULKLOAD_PREVIEW_ROWS = "previewRows";

    public static void setPreviewHeader(HttpSession session, GwtBulkLoadHeader header) {
        session.setAttribute(BULKLOAD_PREVIEW_HEADER, header);
    }

    public static GwtBulkLoadHeader getPreviewHeader(HttpSession session) {
        return (GwtBulkLoadHeader) session.getAttribute(BULKLOAD_PREVIEW_HEADER);
    }

    public static void setPreviewRows(HttpSession session, List<GwtBulkLoadRow> rows) {
        session.setAttribute(BULKLOAD_PREVIEW_ROWS, rows);
    }

    @SuppressWarnings("unchecked")
    public static List<GwtBulkLoadRow> getPreviewRows(HttpSession session) {
        return (List<GwtBulkLoadRow>) session.getAttribute(BULKLOAD_PREVIEW_ROWS);
    }
}
