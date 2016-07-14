package com.freshdirect.cmsadmin.domain;

import java.util.HashMap;
import java.util.Map;

public enum PermissionIdentifier {
    FD_STORE_EDITOR(1l),
    FDX_STORE_EDITOR(2l),
    BULK_LOAD(3l),
    VIEW_CHANGES(4l),
    STORE_PUBLISH(5l),
    FEED_PUBLISH(6l),
    VIEW_ADMINISTRATION(7l),
    PERMISSION_EDITOR(8l),
    FOODKICK_FDFOLDER_EDITOR(9l),
    FOODKICK_SCHEDULE_EDITOR(10l),
    FOODKICK_SECTION_EDITOR(11l),
    FOODKICK_WEBPAGE_EDITOR(12l),
    OTHER_STORE_EDITOR(13l),
    DRAFT_MANAGER(14l),
    DRAFT_EDITOR(15l),
    FOODKICK_DARKSTORE_EDITOR(16l);


    private static final Map<Long, PermissionIdentifier> permissionNameById = new HashMap<Long, PermissionIdentifier>();

    static {
        for (PermissionIdentifier identifier : PermissionIdentifier.values()) {
            permissionNameById.put(identifier.id, identifier);
        }
    }

    public static String getPermissionIdentifierById(Long id) {
        String permissionName = "";
        PermissionIdentifier permission = permissionNameById.get(id);

        if (permission != null) {
            permissionName = permission.toString();
        }

        return permissionName;
    }

    public final Long id;

    private PermissionIdentifier(Long id) {
        this.id = id;
    }

}
