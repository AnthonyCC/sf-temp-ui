package com.freshdirect.webapp.ajax.modulehandling.data;

import java.io.Serializable;

public class ModuleConfig implements Serializable {

    private static final long serialVersionUID = 686703857540321909L;

    private String moduleInstanceId;
    private String sourceType;

    private String moduleTitle;
    private String moduleTitleTextBanner;
    private String contentTitle;
    private String viewAllButtonLink;

    private boolean showModuleTitleTextBanner;
    private boolean showViewAllButton;
    private boolean showModuleTitle;
    private boolean showContentTitle;
    private boolean showHeroTitle;
    private boolean showHeroSubtitle;
    private boolean showHeaderGraphic;
    private boolean showHeaderTitle;
    private boolean showHeaderSubtitle;

    private boolean hideProductName;
    private boolean hideProductPrice;
    private boolean hideProductBadge;

    private String cmEventSource;

    private ModuleEditorialContainer editorialContainer;

    public String getModuleInstanceId() {
        return moduleInstanceId;
    }

    public void setModuleInstanceId(String moduleInstanceId) {
        this.moduleInstanceId = moduleInstanceId;
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

    public boolean isShowModuleTitleTextBanner() {
        return showModuleTitleTextBanner;
    }

    public void setShowModuleTitleTextBanner(boolean showModuleTitleTextBanner) {
        this.showModuleTitleTextBanner = showModuleTitleTextBanner;
    }

    public boolean isShowViewAllButton() {
        return showViewAllButton;
    }

    public void setShowViewAllButton(boolean showViewAllButton) {
        this.showViewAllButton = showViewAllButton;
    }

    public String getViewAllButtonLink() {
        return viewAllButtonLink;
    }

    public void setViewAllButtonLink(String viewAllButtonLink) {
        this.viewAllButtonLink = viewAllButtonLink;
    }

    public boolean isShowHeroTitle() {
        return showHeroTitle;
    }

    public void setShowHeroTitle(boolean showHeroTitle) {
        this.showHeroTitle = showHeroTitle;
    }

    public boolean isShowHeroSubtitle() {
        return showHeroSubtitle;
    }

    public void setShowHeroSubtitle(boolean showHeroSubtitle) {
        this.showHeroSubtitle = showHeroSubtitle;
    }

    public boolean isShowHeaderGraphic() {
        return showHeaderGraphic;
    }

    public void setShowHeaderGraphic(boolean showHeaderGraphic) {
        this.showHeaderGraphic = showHeaderGraphic;
    }

    public boolean isShowHeaderTitle() {
        return showHeaderTitle;
    }

    public void setShowHeaderTitle(boolean showHeaderTitle) {
        this.showHeaderTitle = showHeaderTitle;
    }

    public boolean isShowHeaderSubtitle() {
        return showHeaderSubtitle;
    }

    public void setShowHeaderSubtitle(boolean showHeaderSubtitle) {
        this.showHeaderSubtitle = showHeaderSubtitle;
    }

    public ModuleEditorialContainer getEditorialContainer() {
        return editorialContainer;
    }

    public void setEditorialContainer(ModuleEditorialContainer editorialContainer) {
        this.editorialContainer = editorialContainer;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public boolean isShowModuleTitle() {
        return showModuleTitle;
    }

    public void setShowModuleTitle(boolean showModuleTitle) {
        this.showModuleTitle = showModuleTitle;
    }

    public boolean isShowContentTitle() {
        return showContentTitle;
    }

    public void setShowContentTitle(boolean showContentTitle) {
        this.showContentTitle = showContentTitle;
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

    public String getCmEventSource() {
        return cmEventSource;
    }

    public void setCmEventSource(String cmEventSource) {
        this.cmEventSource = cmEventSource;
    }
}
