package com.freshdirect.cms.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsPermissionI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.EnumEStoreId;

public class CmsPermissionUtility {
    private CmsPermissionUtility() {}

    private static final ContentKey FD_STORE_KEY = ContentKey.getContentKey(FDContentTypes.STORE, EnumEStoreId.FD.getContentId());
    private static final ContentKey FOODKICK_STORE_KEY = ContentKey.getContentKey(FDContentTypes.STORE, EnumEStoreId.FDX.getContentId());

    public static boolean isAllowedInAnyStoreContext(final CmsPermissionI permissionHolder) {
        return permissionHolder.isCanChangeFDStore() || permissionHolder.isCanChangeFDXStore();
    }
    
    public static Set<ContentKey> getAllowedStoreKeys(final CmsPermissionI permissionHolder) {
        final Set<ContentKey> storeKeys = new HashSet<ContentKey>();
        if (permissionHolder != null) {
            if (permissionHolder.isCanChangeFDStore()) { storeKeys.add( FD_STORE_KEY ); }
            if (permissionHolder.isCanChangeFDXStore()) { storeKeys.add( FOODKICK_STORE_KEY ); }

            return Collections.unmodifiableSet(storeKeys);
        } else {
            return Collections.emptySet();
        }
    }
}
