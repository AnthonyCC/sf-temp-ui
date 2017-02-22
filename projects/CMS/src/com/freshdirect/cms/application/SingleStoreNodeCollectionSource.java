package com.freshdirect.cms.application;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.util.PrimaryHomeUtil;

public class SingleStoreNodeCollectionSource extends ContentNodeCollectionSource implements StoreContentSource {

    private final ContentKey storeKey;

    private final Map<ContentKey, ContentKey> primaryHomeMap = new HashMap<ContentKey, ContentKey>();
    
    public SingleStoreNodeCollectionSource(Collection<ContentNodeI> nodes, ContentKey storeKey) {
        super(nodes);

        this.storeKey = storeKey;
        
        for (ContentKey key : nodeMap.keySet()) {
            if (FDContentTypes.PRODUCT.equals(key.getType())) {
                ContentKey phKey = PrimaryHomeUtil.pickPrimaryHomeForStore(key, storeKey, this, DraftContext.MAIN);
                if (phKey != null) {
                    primaryHomeMap.put(key, phKey);
                }
            }
        }
    }

    @Override
    public ContentNodeI getContentNode(ContentKey contentKey, DraftContext draftContext) {
        return super.getContentNode(contentKey, draftContext);
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey contentKey, DraftContext draftContext) {
        return super.getParentKeys(contentKey, draftContext);
    }

    @Override
    public ContentKey getStoreKey() {
        return storeKey;
    }

    @Override
    public ContentKey getPrimaryHomeKey(ContentKey aKey, DraftContext draftContext) {
        return primaryHomeMap.get(aKey);
    }

}
