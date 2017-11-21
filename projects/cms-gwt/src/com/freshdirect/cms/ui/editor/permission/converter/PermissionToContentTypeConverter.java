package com.freshdirect.cms.ui.editor.permission.converter;

import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.ui.editor.permission.domain.Permission;

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
                result = ContentType.FDFolder;
                break;
            case FOODKICK_SCHEDULE_EDITOR:
                result = ContentType.Schedule;
                break;
            case FOODKICK_SECTION_EDITOR:
                result = ContentType.Section;
                break;
            case FOODKICK_WEBPAGE_EDITOR:
                result = ContentType.WebPage;
                break;
            case FOODKICK_DARKSTORE_EDITOR:
                result = ContentType.DarkStore;
                break;
            case FOODKICK_IMAGE_BANNER_EDITOR:
                result = ContentType.ImageBanner;
                break;
            default:
                result = null;
                break;
        }
        return result;
    }
}
