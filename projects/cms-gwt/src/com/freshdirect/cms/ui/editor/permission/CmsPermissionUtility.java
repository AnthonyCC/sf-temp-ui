package com.freshdirect.cms.ui.editor.permission;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.RootContentKey;
import com.freshdirect.cms.ui.model.CmsPermissionHolder;

public class CmsPermissionUtility {

    private CmsPermissionUtility() {
    }

    private static final ContentKey FD_STORE_KEY = RootContentKey.STORE_FRESHDIRECT.contentKey;
    private static final ContentKey FOODKICK_STORE_KEY = RootContentKey.STORE_FOODKICK.contentKey;

    public static boolean isAllowedInAnyStoreContext(final CmsPermissionHolder permissionHolder) {
        return permissionHolder.isCanChangeFDStore() || permissionHolder.isCanChangeFDXStore();
    }

    public static Set<ContentKey> getAllowedStoreKeys(final CmsPermissionHolder permissionHolder) {
        final Set<ContentKey> storeKeys = new HashSet<ContentKey>();
        if (permissionHolder != null) {
            if (permissionHolder.isCanChangeFDStore()) {
                storeKeys.add(FD_STORE_KEY);
            }
            if (permissionHolder.isCanChangeFDXStore()) {
                storeKeys.add(FOODKICK_STORE_KEY);
            }

            return Collections.unmodifiableSet(storeKeys);
        } else {
            return Collections.emptySet();
        }
    }
}
