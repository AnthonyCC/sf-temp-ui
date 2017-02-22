package com.freshdirect.cms.publish.flow.tasks;

import java.util.Collection;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.fdstore.EnumEStoreId;

public class StorePublisherTask extends PublisherTask {

    public StorePublisherTask(String publishId, Phase phase, Collection<ContentNodeI> input, String targetPath, EnumEStoreId storeId) {
        super(publishId, phase, input, targetPath, "Store.xml.gz", storeId);
    }

    @Override
    public String getName() {
        return "Store.xml publish to " + targetPath;
    }

}
