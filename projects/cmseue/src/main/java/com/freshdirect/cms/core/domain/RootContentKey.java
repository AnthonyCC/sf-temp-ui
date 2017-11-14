package com.freshdirect.cms.core.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * This enum collects all pre-defined top keys of CMS
 * including store keys in their respective order
 */
public enum RootContentKey {
    STORE_FRESHDIRECT(ContentKeyFactory.get(ContentType.Store, "FreshDirect")),
    STORE_FOODKICK(ContentKeyFactory.get(ContentType.Store, "FDX")),

    SHARED_RESOURCES(ContentKeyFactory.get(ContentType.FDFolder, "sharedResources")),

    MEDIA_ROOT(ContentKeyFactory.get(ContentType.MediaFolder, "/")),

    CMS_QUERIES(ContentKeyFactory.get(ContentType.FDFolder, "queries")),
    ORPHANS(ContentKeyFactory.get(ContentType.CmsQuery, "orphans")),

    // Various top content folders
    RECIPES(ContentKeyFactory.get(ContentType.FDFolder, "recipes")),
    SMART_YMALS(ContentKeyFactory.get(ContentType.FDFolder, "ymals")),
    STARTER_LISTS(ContentKeyFactory.get(ContentType.FDFolder, "starterLists")),
    DONATION_ORGANIZATIONS(ContentKeyFactory.get(ContentType.FDFolder, "donationOrganizationList"));

    public final ContentKey contentKey;

    RootContentKey(ContentKey contentKey) {
        this.contentKey = contentKey;
    }

    /**
     * Collects and returns available top keys in CMS.
     *
     * @param storeKey when set, only this store key will be included in the result.
     * @return
     */
    public static Set<ContentKey> selectRootContentKeys(ContentKey storeKey) {
        Set<ContentKey> rootKeySet = new HashSet<ContentKey>();
        if (storeKey != null) {
            for (RootContentKey rootKey : RootContentKey.values()) {
                if (ContentType.Store != rootKey.contentKey.type) {
                    rootKeySet.add(rootKey.contentKey);
                }
            }
            rootKeySet.add(storeKey);
        } else {
            for (RootContentKey rootKey : RootContentKey.values()) {
                rootKeySet.add(rootKey.contentKey);
            }
        }
        return rootKeySet;
    }

    public static boolean isRootKey(ContentKey contentKey) {
        for (RootContentKey rootKey : values()) {
            if (rootKey.contentKey.equals(contentKey)) {
                return true;
            }
        }
        return false;
    }
}
