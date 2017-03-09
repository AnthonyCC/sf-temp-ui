package com.freshdirect.cms.publish.service;

/**
 * A simple service that provides
 * control over running publish flows.
 *
 * Current implementation supports only eliminating
 * stuck flows.
 *
 * @author segabor
 *
 */
public interface PublishFlowControlService {

    /**
     * Abort publish flows that
     * - stuck in PROGRESS status
     * - for more than two hours.
     */
    void abortStuckPublishFlows();

}
