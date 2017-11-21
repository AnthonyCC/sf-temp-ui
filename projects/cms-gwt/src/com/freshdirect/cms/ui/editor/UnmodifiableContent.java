package com.freshdirect.cms.ui.editor;

import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.ui.editor.reports.service.ReportingService;

public class UnmodifiableContent {

    @SuppressWarnings("serial")
    private static final Set<ContentKey> BUILTIN_CONTENT_KEYS = new HashSet<ContentKey>() {
        {
            add(ReportingService.CMS_REPORTS_FOLDER_KEY);
            add(ReportingService.SMARTSTORE_REPORTS_FOLDER_KEY);
            add(ReportingService.CMS_QUERIES_FOLDER_KEY);

            addAll(ReportingService.CMS_REPORT_NODE_KEYS);
            addAll(ReportingService.SMARTSTORE_REPORT_NODE_KEYS);
            addAll(ReportingService.CMS_QUERY_NODE_KEYS);
        }
    };

    private UnmodifiableContent() {
    }

    public static boolean isModifiable(ContentKey contentKey) {
        if (Media.isMediaType(contentKey)) {
            return false;
        } else if (ContentType.ErpMaterial == contentKey.type || ContentType.ErpClass == contentKey.type ||ContentType.ErpCharacteristic == contentKey.type || ContentType.ErpCharacteristicValue == contentKey.type || ContentType.ErpSalesUnit == contentKey.type) {
            return false;
        } else if (BUILTIN_CONTENT_KEYS.contains(contentKey)) {
            return false;
        }

        return true;
    }
}
