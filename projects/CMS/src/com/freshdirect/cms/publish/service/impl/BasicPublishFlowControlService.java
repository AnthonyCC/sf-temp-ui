package com.freshdirect.cms.publish.service.impl;

import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.repository.PublishRepository;
import com.freshdirect.cms.publish.service.PublishFlowControlService;


/**
 * Default implementation.
 *
 * @author segabor
 */
public final class BasicPublishFlowControlService implements PublishFlowControlService {

    private static final BasicPublishFlowControlService INSTANCE = new BasicPublishFlowControlService();

    private PublishRepository repository = new PublishRepository();

    private BasicPublishFlowControlService() {
    }

    /**
     * Provide default service instance.
     *
     * @return default instance
     */
    public static BasicPublishFlowControlService defaultInstance() {
        return INSTANCE;
    }

    /**
     * This method essentially updates publish
     * status to {@link EnumPublishStatus#FAILED}
     * of which started for more than two hours
     */
    @Override
    public void abortStuckPublishFlows() {
        repository.updateStuckPublishStatus();
    }

}
