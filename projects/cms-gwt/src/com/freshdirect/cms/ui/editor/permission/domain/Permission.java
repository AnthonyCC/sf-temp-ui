package com.freshdirect.cms.ui.editor.permission.domain;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Permission {
    FD_STORE_EDITOR(1l, "Can modify FD storedata"),
    FDX_STORE_EDITOR(2l, "Can modify FDX storedata"),
    BULK_LOAD(3l, "Can utilize the “Bulk Load” tab"),
    VIEW_CHANGES(4l, "Can utilize the “Changes” tab"),
    STORE_PUBLISH(5l, "Can utilize the “Publish” tab"),
    FEED_PUBLISH(6l, "Can utilize the “Feed Publish” tab"),
    VIEW_ADMINISTRATION(7l, "Can utilize the “Administration” tab"),
    PERMISSION_EDITOR(8l, "Can access the permission manager"),
    FOODKICK_FDFOLDER_EDITOR(9l, "FDFolder content type permission"),
    FOODKICK_SCHEDULE_EDITOR(10l, "Schedule content type permission"),
    FOODKICK_SECTION_EDITOR(11l, "Section content type permission"),
    FOODKICK_WEBPAGE_EDITOR(12l, "WebPage content type permission"),
    OTHER_STORE_EDITOR(13l, "Can modify Other storedata"),
    DRAFT_MANAGER(14l, "Can manage Draft branches"),
    DRAFT_EDITOR(15l, "Can edit Draft branches"),
    FOODKICK_DARKSTORE_EDITOR(16l, "DarkStore content type permission"),
    FOODKICK_IMAGE_BANNER_EDITOR(17l, "ImageBanner content type permission");

    private static final Logger LOG = LoggerFactory.getLogger(Permission.class);
    private Long id;
    private String name;

    Permission(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Permission forValue(Long id) {
        for (Permission permission : values()) {
            if (permission.getId().equals(id)) {
                return permission;
            }
        }
        LOG.warn(MessageFormat.format("UserAssociated permission with id[{0}] was not found in Permission Enum", id));
        return null;
    }
}
