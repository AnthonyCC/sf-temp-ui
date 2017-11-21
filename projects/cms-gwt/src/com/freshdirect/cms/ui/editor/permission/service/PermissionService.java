package com.freshdirect.cms.ui.editor.permission.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.draft.service.DraftContextHolder;
import com.freshdirect.cms.ui.editor.permission.CmsPermissionManager;
import com.freshdirect.cms.ui.editor.permission.ContentChangeSource;
import com.freshdirect.cms.ui.editor.permission.domain.Permit;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishStatus;
import com.freshdirect.cms.ui.editor.publish.service.StorePublishService;
import com.freshdirect.cms.ui.model.CmsPermissionHolder;
import com.freshdirect.cms.ui.model.GwtNodePermission;
import com.freshdirect.cms.ui.model.GwtUser;

@Service
public class PermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionService.class);

    @Autowired
    private DraftContextHolder draftContextHolder;

    @Autowired
    private StorePublishService storePublishService;

    public boolean isNodeModificationEnabled() {
        boolean canSave = true;
        StorePublishStatus lastPublishStatus = StorePublishStatus.valueOf(getLastPublishStatusName());
        if (draftContextHolder.getDraftContext().isMainDraft() && lastPublishStatus == StorePublishStatus.PROGRESS) {
            canSave = false;
        }

        return canSave;
    }

    public boolean isSaveAllowed(GwtUser user, Map<ContentKey, Map<Attribute, Object>> changedNodes, ContentChangeSource origin) {
        Permit permit = CmsPermissionManager.requestSaveChangesetPermission(user, changedNodes, origin);
        return permit.equals(Permit.ALLOW);
    }

    public GwtNodePermission setupClientPermissions(final ContentKey contentKey, final CmsPermissionHolder permissionHolder) {
        return CmsPermissionManager.setupClientPermissions(contentKey, permissionHolder);
    }

    private String getLastPublishStatusName() {
        return storePublishService.getLastStorePublishStatus();
    }

}
