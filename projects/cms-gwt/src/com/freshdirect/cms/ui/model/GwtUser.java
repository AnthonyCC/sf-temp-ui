package com.freshdirect.cms.ui.model;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is a duplicate of CmsUser, but needed separately because of the GWT serialization requirements.
 * 
 * @author zsombor
 *
 */
public class GwtUser implements Serializable, Cloneable, IsSerializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String personaName;
    private String loginErrorMessage; 

    // List of permissions
    private boolean hasAccessToAdminTab = false;
    private boolean hasAccessToBulkLoaderTab = false;
    private boolean hasAccessToChangesTab = false;
    private boolean hasAccessToPublishTab = false;
    private boolean hasAccessToFeedPublishTab = false;
    private boolean hasAccessToDraftBranches = false;
    private boolean canChangeFDStore = false;
    private boolean canChangeFDXStore = false;
    private boolean canChangeOtherNodes = false;

    private String cmsAdminURL;

    private boolean isDraftActive = false;
    private String draftName;

    public GwtUser() {
        // MESSAGE TO ALL DEVS
        //
        // LEAVE THIS CONSTRUCTOR HERE !!
        // DO NOT USE THIS !!
        // DO NOT TOUCH IT !!
        // GWT REQUIRES EXISTENCE OF DEFAULT CONSTRUCTORS!!
        //
        // For the curious:
        // http://stackoverflow.com/questions/14033402/failed-to-resolve-class-via-deferred-binding
    }

    public GwtUser(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (canChangeFDStore ? 1231 : 1237);
        result = prime * result + (canChangeFDXStore ? 1231 : 1237);
        result = prime * result + (hasAccessToAdminTab ? 1231 : 1237);
        result = prime * result + (hasAccessToBulkLoaderTab ? 1231 : 1237);
        result = prime * result + (hasAccessToChangesTab ? 1231 : 1237);
        result = prime * result + (hasAccessToFeedPublishTab ? 1231 : 1237);
        result = prime * result + (hasAccessToPublishTab ? 1231 : 1237);
        result = prime * result + (hasAccessToDraftBranches ? 1231 : 1237);
        result = prime * result + (canChangeOtherNodes ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((personaName == null) ? 0 : personaName.hashCode());
        result = prime * result + ((loginErrorMessage == null) ? 0 : loginErrorMessage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GwtUser other = (GwtUser) obj;
        if (canChangeFDStore != other.canChangeFDStore)
            return false;
        if (canChangeFDXStore != other.canChangeFDXStore)
            return false;
        if (hasAccessToAdminTab != other.hasAccessToAdminTab)
            return false;
        if (hasAccessToBulkLoaderTab != other.hasAccessToBulkLoaderTab)
            return false;
        if (hasAccessToChangesTab != other.hasAccessToChangesTab)
            return false;
        if (hasAccessToFeedPublishTab != other.hasAccessToFeedPublishTab)
            return false;
        if (hasAccessToPublishTab != other.hasAccessToPublishTab)
            return false;
        if (hasAccessToDraftBranches != other.hasAccessToDraftBranches)
            return false;
        if (canChangeOtherNodes != other.canChangeOtherNodes)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (personaName == null) {
            if (other.personaName != null)
                return false;
        } else if (!personaName.equals(other.personaName))
            return false;
        if (loginErrorMessage == null) {
            if (other.loginErrorMessage != null)
                return false;
        } else if (!loginErrorMessage.equals(other.loginErrorMessage))
            return false;
        return true;
    }

    public String getPersonaName() {
        return personaName;
    }

    public void setPersonaName(String personaName) {
        this.personaName = personaName;
    }

    public String getCmsAdminURL() {
        return cmsAdminURL;
    }

    public void setCmsAdminURL(String cmsAdminURL) {
        this.cmsAdminURL = cmsAdminURL;
    }

    public boolean isHasAccessToAdminTab() {
        return hasAccessToAdminTab;
    }

    public void setHasAccessToAdminTab(boolean hasAccessToAdminTab) {
        this.hasAccessToAdminTab = hasAccessToAdminTab;
    }

    public boolean isHasAccessToBulkLoaderTab() {
        return hasAccessToBulkLoaderTab;
    }

    public void setHasAccessToBulkLoaderTab(boolean hasAccessToBulkLoaderTab) {
        this.hasAccessToBulkLoaderTab = hasAccessToBulkLoaderTab;
    }

    public boolean isHasAccessToChangesTab() {
        return hasAccessToChangesTab;
    }

    public void setHasAccessToChangesTab(boolean hasAccessToChangesTab) {
        this.hasAccessToChangesTab = hasAccessToChangesTab;
    }

    public boolean isHasAccessToPublishTab() {
        return hasAccessToPublishTab;
    }

    public void setHasAccessToPublishTab(boolean hasAccessToPublishTab) {
        this.hasAccessToPublishTab = hasAccessToPublishTab;
    }

    public boolean isHasAccessToFeedPublishTab() {
        return hasAccessToFeedPublishTab;
    }

    public void setHasAccessToFeedPublishTab(boolean hasAccessToFeedPublishTab) {
        this.hasAccessToFeedPublishTab = hasAccessToFeedPublishTab;
    }

    public boolean isCanChangeFDStore() {
        return canChangeFDStore;
    }

    public void setCanChangeFDStore(boolean canChangeFDStore) {
        this.canChangeFDStore = canChangeFDStore;
    }

    public boolean isCanChangeFDXStore() {
        return canChangeFDXStore;
    }

    public void setCanChangeFDXStore(boolean canChangeFDXStore) {
        this.canChangeFDXStore = canChangeFDXStore;
    }

    public boolean isCanChangeOtherNodes() {
        return canChangeOtherNodes;
    }

    public void setCanChangeOtherNodes(boolean canChangeOtherNodes) {
        this.canChangeOtherNodes = canChangeOtherNodes;
    }

    public boolean isHasAccessToDraftBranches() {
        return hasAccessToDraftBranches;
    }

    public void setHasAccessToDraftBranches(boolean hasAccessToDraftBranches) {
        this.hasAccessToDraftBranches = hasAccessToDraftBranches;
    }

    public boolean isDraftActive() {
        return isDraftActive;
    }

    public void setDraftActive(boolean isDraftActive) {
        this.isDraftActive = isDraftActive;
    }

    public String getDraftName() {
        return draftName;
    }

    public void setDraftName(String draftName) {
        this.draftName = draftName;
    }

    public String getLoginErrorMessage() {
        return loginErrorMessage;
    }

    public void setLoginErrorMessage(String loginErrorMessage) {
        this.loginErrorMessage = loginErrorMessage;
    }

}
