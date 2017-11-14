package com.freshdirect.cms.ui.editor.permission.converter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ui.editor.permission.domain.Permission;
import com.freshdirect.cms.ui.editor.permission.domain.PermissionConversionData;

public class PermissionConverter {

    private static final PermissionConverter INSTANCE = new PermissionConverter();

    private PermissionConverter() {
    }

    public static PermissionConverter defaultConverter() {
        return INSTANCE;
    }

    public List<Permission> convert(List<PermissionConversionData> permissionConversionData) {
        List<Permission> result = new ArrayList<Permission>();
        for (PermissionConversionData permissionData : permissionConversionData) {
            Permission permission = Permission.forValue(permissionData.getId());
            if (permission != null) {
                result.add(permission);
            }
        }

        return result;
    }

}
