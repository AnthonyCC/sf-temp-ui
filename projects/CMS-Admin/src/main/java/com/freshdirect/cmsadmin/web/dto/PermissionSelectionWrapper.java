package com.freshdirect.cmsadmin.web.dto;

import com.freshdirect.cmsadmin.domain.Permission;

public class PermissionSelectionWrapper {

    private Permission permission;
    private boolean selected;

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
