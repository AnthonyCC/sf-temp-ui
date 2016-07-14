package com.freshdirect.cms.application.permission.converter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.application.permission.domain.Permission;
import com.freshdirect.cms.application.permission.domain.PermissionConversionData;

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
