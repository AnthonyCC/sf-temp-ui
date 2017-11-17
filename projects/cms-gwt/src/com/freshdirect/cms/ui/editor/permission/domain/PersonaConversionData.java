package com.freshdirect.cms.ui.editor.permission.domain;

import java.util.List;
import java.util.Map;


public class PersonaConversionData {

    private Long id;
    private String name;
    private List<PermissionConversionData> permissions;
    private Map<String, Object> errors;

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

    public List<PermissionConversionData> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionConversionData> permissions) {
        this.permissions = permissions;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }
}
