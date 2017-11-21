package com.freshdirect.cms.ui.editor.publish.flow.tasks;

import com.freshdirect.cms.ui.editor.publish.flow.domain.Phase;

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
    protected Long publishId;

    /**
     * Current publish phase.
     * Never null.
     */
    protected Phase phase;

    /**
     * Task name in human readable form
     * @return
     */
    public abstract String getName();

    /**
     * @deprecated No longer used as subclasses became Spring beans!
     *
     * @param publishId
     * @param phase
     */
    @Deprecated
    protected PublishTask(Long publishId, Phase phase) {
        if (publishId == null || phase == null) {
            throw new NullPointerException("Publish ID and Phase may not be null values");
        }

        this.publishId = publishId;
        this.phase = phase;
    }

    protected PublishTask() {
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
    }
}
