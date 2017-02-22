package com.freshdirect.webapp.ajax.modulehandling.data;

import java.io.Serializable;

public class ModuleConfig implements Serializable {

    private static final long serialVersionUID = 686703857540321909L;

    private String cmEventSource;
    private String moduleId;
    private String sourceType;

    private String moduleTitle;
    private String moduleTitleTextBanner;
    private String contentTitle;
    private String viewAllButtonLink;

    private boolean hideViewAllButton;
    private boolean hideProductName;
    private boolean hideProductPrice;
    private boolean hideProductBadge;

    private String moduleGroupTitle;
    private String moduleGroupTitleTextBanner;
    private boolean hideModuleGroupViewAllButton;
    private String moduleGroupViewAllButtonLink;
    private boolean useViewAllPopup;

    private ModuleEditorialContainer editorialContainer;

    public String getCmEventSource() {
        return cmEventSource;
    }

    public void setCmEventSource(String cmEventSource) {
        this.cmEventSource = cmEventSource;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }

    public String getModuleTitleTextBanner() {
        return moduleTitleTextBanner;
    }

    public void setModuleTitleTextBanner(String moduleTitleTextBanner) {
        this.moduleTitleTextBanner = moduleTitleTextBanner;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getViewAllButtonLink() {
        return viewAllButtonLink;
    }

    public void setViewAllButtonLink(String viewAllButtonLink) {
        this.viewAllButtonLink = viewAllButtonLink;
    }

    public boolean isHideViewAllButton() {
        return hideViewAllButton;
    }

    public void setHideViewAllButton(boolean hideViewAllButton) {
        this.hideViewAllButton = hideViewAllButton;
    }

    public boolean isHideProductName() {
        return hideProductName;
    }

    public void setHideProductName(boolean hideProductName) {
        this.hideProductName = hideProductName;
    }

    public boolean isHideProductPrice() {
        return hideProductPrice;
    }

    public void setHideProductPrice(boolean hideProductPrice) {
        this.hideProductPrice = hideProductPrice;
    }

    public boolean isHideProductBadge() {
        return hideProductBadge;
    }

    public void setHideProductBadge(boolean hideProductBadge) {
        this.hideProductBadge = hideProductBadge;
    }

    public String getModuleGroupTitle() {
        return moduleGroupTitle;
    }

    public void setModuleGroupTitle(String moduleGroupTitle) {
        this.moduleGroupTitle = moduleGroupTitle;
    }

    public String getModuleGroupTitleTextBanner() {
        return moduleGroupTitleTextBanner;
    }

    public void setModuleGroupTitleTextBanner(String moduleGroupTitleTextBanner) {
        this.moduleGroupTitleTextBanner = moduleGroupTitleTextBanner;
    }

    public boolean getHideModuleGroupViewAllButton() {
        return hideModuleGroupViewAllButton;
    }

    public void setHideModuleGroupViewAllButton(boolean hideModuleGroupViewAllButton) {
        this.hideModuleGroupViewAllButton = hideModuleGroupViewAllButton;
    }

    public String getModuleGroupViewAllButtonLink() {
        return moduleGroupViewAllButtonLink;
    }

    public void setModuleGroupViewAllButtonLink(String moduleGroupViewAllButtonLink) {
        this.moduleGroupViewAllButtonLink = moduleGroupViewAllButtonLink;
    }

    public boolean isUseViewAllPopup() {
        return useViewAllPopup;
    }

    public void setUseViewAllPopup(boolean useViewAllPopup) {
        this.useViewAllPopup = useViewAllPopup;
    }

    public ModuleEditorialContainer getEditorialContainer() {
        return editorialContainer;
    }

    public void setEditorialContainer(ModuleEditorialContainer editorialContainer) {
        this.editorialContainer = editorialContainer;
    }

}
