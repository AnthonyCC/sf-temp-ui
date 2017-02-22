package com.freshdirect.cms.publish.service.impl;

import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.repository.PublishRepository;
import com.freshdirect.cms.publish.service.PublishStatusService;

public final class BasicPublishStatusService implements PublishStatusService {

    private static final BasicPublishStatusService INSTANCE = new BasicPublishStatusService();

    private PublishRepository publishRepository = null;

    public static BasicPublishStatusService defaultService() {
        return INSTANCE;
    }

    private BasicPublishStatusService() {
        publishRepository = new PublishRepository();
    }

    @Override
    public Publish fetchPublish(String publishId) {
        return publishRepository.findOne(publishId);
    }

    @Override
    public void updatePublishStatus(Publish publish) {
        publishRepository.save(publish);
    }

    @Override
    public Publish getLastPublish() {
        return publishRepository.findLast();
    }

}
