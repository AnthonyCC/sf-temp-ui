package com.freshdirect.cms.publish.flow.tasks;

import java.util.Collection;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.fdstore.EnumEStoreId;

public class MediaPublisherTask extends PublisherTask {

    public MediaPublisherTask(String publishId, Phase phase, Collection<ContentNodeI> input, String targetPath, EnumEStoreId storeId) {
        super(publishId, phase, input, targetPath, "Media.xml.gz", storeId);
    }

    @Override
    public String getName() {
        return "Media.xml publish to " + targetPath;
    }

}
