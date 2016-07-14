/*
 * Created on Jan 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.cms.application;

import java.util.Collections;
import java.util.Set;

import com.freshdirect.cms.ContentType;

/**
 * Simple implementation of {@link com.freshdirect.cms.application.UserI}.
 */
public class CmsUser implements UserI, CmsPermissionI {

    private final String name;
    private String personaName;
    private String loginErrorMessage;

    // List of permissions
    private boolean hasAccessToPermissionEditorApp = false;
    private boolean hasAccessToAdminTab = false;
    private boolean hasAccessToBulkLoaderTab = false;
    private boolean hasAccessToChangesTab = false;
    private boolean hasAccessToPublishTab = false;
    private boolean hasAccessToFeedPublishTab = false;
    private boolean hasAccessToDraftBranches = false;
    private boolean canChangeFDStore = false;
    private boolean canChangeFDXStore = false;
    private boolean canChangeOtherNodes = false;
    private Set<ContentType> permittedContentTypes = Collections.emptySet();

    private DraftContext draftContext = DraftContext.MAIN;
    
    /**
     * 
     * @param name
     */
    public CmsUser(String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.security.Principal#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    @Deprecated
    @Override
    public boolean isAllowedToWrite() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isHasAccessToPermissionEditorApp() {
        return hasAccessToPermissionEditorApp;
    }

    public void setHasAccessToPermissionEditorApp(boolean hasAccessToPermissionEditorApp) {
        this.hasAccessToPermissionEditorApp = hasAccessToPermissionEditorApp;
    }

    @Override
    public boolean isHasAccessToAdminTab() {
        return hasAccessToAdminTab;
    }

    public void setHasAccessToAdminTab(boolean hasAccessToAdminTab) {
        this.hasAccessToAdminTab = hasAccessToAdminTab;
    }

    @Override
    public boolean isHasAccessToBulkLoaderTab() {
        return hasAccessToBulkLoaderTab;
    }

    public void setHasAccessToBulkLoaderTab(boolean hasAccessToBulkLoaderTab) {
        this.hasAccessToBulkLoaderTab = hasAccessToBulkLoaderTab;
    }

    @Override
    public boolean isHasAccessToChangesTab() {
        return hasAccessToChangesTab;
    }

    public void setHasAccessToChangesTab(boolean hasAccessToChangesTab) {
        this.hasAccessToChangesTab = hasAccessToChangesTab;
    }

    @Override
    public boolean isHasAccessToPublishTab() {
        return hasAccessToPublishTab;
    }

    public void setHasAccessToPublishTab(boolean hasAccessToPublishTab) {
        this.hasAccessToPublishTab = hasAccessToPublishTab;
    }

    @Override
    public boolean isHasAccessToFeedPublishTab() {
        return hasAccessToFeedPublishTab;
    }

    public void setHasAccessToFeedPublishTab(boolean hasAccessToFeedPublishTab) {
        this.hasAccessToFeedPublishTab = hasAccessToFeedPublishTab;
    }

    @Override
    public boolean isCanChangeFDStore() {
        return canChangeFDStore;
    }

    public void setCanChangeFDStore(boolean canChangeFDStore) {
        this.canChangeFDStore = canChangeFDStore;
    }

    @Override
    public boolean isCanChangeFDXStore() {
        return canChangeFDXStore;
    }

    public void setCanChangeFDXStore(boolean canChangeFDXStore) {
        this.canChangeFDXStore = canChangeFDXStore;
    }

    @Override
    public boolean isCanChangeOtherNodes() {
        return canChangeOtherNodes;
    }

    public void setCanChangeOtherNodes(boolean canChangeOtherNodes) {
        this.canChangeOtherNodes = canChangeOtherNodes;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public void setLoginErrorMessage(String loginErrorMessage) {
        this.loginErrorMessage = loginErrorMessage;
    }

    @Override
    public boolean isAnyContentTypeBasedPermission() {
        return !permittedContentTypes.isEmpty();
    }

    @Override
    public boolean isContentTypeBasedPermissionEnabled(ContentType contentType) {
        return permittedContentTypes.contains(contentType);
    }

    public void setPermittedContentTypes(Set<ContentType> permittedContentTypes) {
        this.permittedContentTypes = permittedContentTypes;
    }

    public boolean isHasAccessToDraftBranches() {
        return hasAccessToDraftBranches;
    }

    public void setHasAccessToDraftBranches(boolean hasAccessToDraftBranches) {
        this.hasAccessToDraftBranches = hasAccessToDraftBranches;
    }

    
    public DraftContext getDraftContext() {
        return draftContext;
    }

    
    public void setDraftContext(DraftContext draftContext) {
        this.draftContext = draftContext;
    }
}
