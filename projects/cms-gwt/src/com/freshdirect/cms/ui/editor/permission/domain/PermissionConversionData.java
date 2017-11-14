package com.freshdirect.cms.ui.editor.permission.domain;



public class PermissionConversionData {

    private Long id;
    private String name;

    public PermissionConversionData() {
    }

    public PermissionConversionData(Long id, String name) {
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
}
