package com.freshdirect.cms.publish.flow.tasks.loader;

import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.flow.ProducerTask;
import com.freshdirect.cms.publish.service.ContentLoaderService;
import com.freshdirect.cms.publish.service.impl.DbContentLoaderService;

abstract class ContentLoaderTask<V> extends ProducerTask<V> {

    protected final ContentLoaderService loader = DbContentLoaderService.defaultService();

    protected ContentLoaderTask(String publishId, Phase phase) {
        super(publishId, phase);
    }

}
