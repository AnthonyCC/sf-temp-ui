package com.freshdirect.cms.publish.flow;

/**
 * Publish Flow Task
 * 
 * @author segabor
 */
public abstract class PublishTask {

    /**
     * ID of the ongoing publish.
     * Never null.
     */
    protected final String publishId;

    /**
     * Current publish phase.
     * Never null.
     */
    protected final Phase phase;

    /**
     * Task name in human readable form
     * @return
     */
    public abstract String getName();

    protected PublishTask(String publishId, Phase phase) {
        if (publishId == null || phase == null) {
            throw new NullPointerException("Publish ID and Phase may not be null values");
        }

        this.publishId = publishId;
        this.phase = phase;
    }
}
