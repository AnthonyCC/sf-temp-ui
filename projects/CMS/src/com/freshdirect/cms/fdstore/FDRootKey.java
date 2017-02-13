package com.freshdirect.cms.fdstore;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.content.GlobalMenuItemModel;
import com.freshdirect.fdstore.content.YoutubeVideoModel;

/**
 * Enumeration designed to represents root keys in FD content topology
 * 
 * @see EnumEStoreId The list of available store keys
 * 
 * @author segabor
 *
 */
public enum FDRootKey {
    FRESHDIRECT("Store:FreshDirect"),
    FDX("Store:FDX"),
    SHARED_RESOURCES("FDFolder:sharedResources"),
    MEDIA("MediaFolder:/"),
    CMS_FORMS("CmsFolder:forms"),
    CMS_QUERIES("CmsQueryFolder:queries"),
    ORPHANS("CmsQuery:orphans"),
    RECIPES("FDFolder:recipes"),
    SMART_YMAL("FDFolder:ymals"),
    GLOBAL_MENU(GlobalMenuItemModel.DEFAULT_MENU_FOLDER),
    STARTER_LISTS("FDFolder:starterLists"),
    DONATION_ORGS("FDFolder:donationOrganizationList"),
    YOUTUBE_MEDIA(YoutubeVideoModel.DEFAULT_YOUTUBE_FOLDER);

    final ContentKey contentKey;

    FDRootKey(String key) {
        this.contentKey = ContentKey.getContentKey(key);
    };

    public ContentKey getContentKey() {
        return contentKey;
    }

    /**
     * Return list of root keys
     * 
     * @return ordered list of root keys eligible for display
     */
    public static List<ContentKey> getAllRootKeysIncluding() {
        final List<ContentKey> result = new ArrayList<ContentKey>();
        for (final FDRootKey rootKey : FDRootKey.values()) {
            result.add(rootKey.contentKey);
        }
        return result;
    }

}
