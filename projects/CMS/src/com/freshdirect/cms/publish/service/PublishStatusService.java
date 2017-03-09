package com.freshdirect.cms.publish.service;

import com.freshdirect.cms.publish.Publish;

public interface PublishStatusService {

    Publish fetchPublish(String publishId);

    void updatePublishStatus(Publish publish);

    Publish getLastPublish();

}
