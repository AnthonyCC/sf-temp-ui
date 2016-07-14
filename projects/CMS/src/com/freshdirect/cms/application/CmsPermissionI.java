package com.freshdirect.cms.application;

import com.freshdirect.cms.ContentType;

/**
 * 
 * @author segabor
 *
 */
public interface CmsPermissionI {
    // Can enter CMS Admin app
    boolean isHasAccessToPermissionEditorApp();

    // GWT tab permissions
    boolean isHasAccessToAdminTab();
    boolean isHasAccessToBulkLoaderTab();
    boolean isHasAccessToChangesTab();
    boolean isHasAccessToPublishTab();
    boolean isHasAccessToFeedPublishTab();
    
    boolean isCanChangeFDStore();
    boolean isCanChangeFDXStore();
    // special right for non-store and shared rsrc nodes
    boolean isCanChangeOtherNodes();

    /**
     * Indicates that permission holder has any type based permission
     * @return
     */
    boolean isAnyContentTypeBasedPermission();

    boolean isContentTypeBasedPermissionEnabled(ContentType contentType);
}
