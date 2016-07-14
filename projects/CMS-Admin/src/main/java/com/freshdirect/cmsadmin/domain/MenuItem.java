package com.freshdirect.cmsadmin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.freshdirect.cmsadmin.web.DefaultViewController;
import com.freshdirect.cmsadmin.web.DraftPageController;
import com.freshdirect.cmsadmin.web.PersonaController;
import com.freshdirect.cmsadmin.web.PersonaUserAssociationController;

/**
 * Menu item enum.
 */
@JsonFormat(shape = Shape.OBJECT)
public enum MenuItem {
    DEFAULT_PAGE("Home", DefaultViewController.DEFAULT_VIEW_PATH, "home"),
    PERSONA_PAGE("Roles", PersonaController.PERSONA_PAGE_PATH, "lock-open"),
    PERSONA_USER_ASSOCIATION_PAGE("Users", PersonaUserAssociationController.USER_PERSONA_ASSOCIATION_PAGE_PATH, "face"),
    DRAFT_MANAGEMENT_PAGE("Draft management", DraftPageController.DRAFT_PAGE_PATH, "work");

    private final String name;
    private final String path;
    private final String icon;

    MenuItem(String name, String path) {
        this.name = name;
        this.path = path;
        this.icon = "";
    }

    MenuItem(String name, String path, String icon) {
        this.name = name;
        this.path = path;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getIcon() {
        return icon;
    }

}
