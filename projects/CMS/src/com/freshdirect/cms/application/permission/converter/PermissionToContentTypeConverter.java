package com.freshdirect.cms.application.permission.converter;

import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.permission.domain.Permission;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class PermissionToContentTypeConverter {

    private static final PermissionToContentTypeConverter INSTANCE = new PermissionToContentTypeConverter();

    private PermissionToContentTypeConverter() {
    }

    public static PermissionToContentTypeConverter defaultService() {
        return INSTANCE;
    }

    public ContentType convert(Permission permission) {
        final ContentType result;
        switch (permission) {
            case FOODKICK_FDFOLDER_EDITOR:
                result = FDContentTypes.FDFOLDER;
                break;
            case FOODKICK_SCHEDULE_EDITOR:
                result = FDContentTypes.SCHEDULE;
                break;
            case FOODKICK_SECTION_EDITOR:
                result = FDContentTypes.SECTION;
                break;
            case FOODKICK_WEBPAGE_EDITOR:
                result = FDContentTypes.WEBPAGE;
                break;
            case FOODKICK_DARKSTORE_EDITOR:
                result = FDContentTypes.DARKSTORE;
                break;
            case FOODKICK_IMAGE_BANNER_EDITOR:
                result = FDContentTypes.IMAGE_BANNER;
                break;
            default:
                result = null;
                break;
        }
        return result;
    }
}
