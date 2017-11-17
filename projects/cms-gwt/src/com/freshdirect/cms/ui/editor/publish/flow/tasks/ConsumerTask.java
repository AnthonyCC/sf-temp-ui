package com.freshdirect.cms.ui.editor.publish.flow.tasks;

import com.freshdirect.cms.ui.editor.publish.flow.domain.Input;
import com.freshdirect.cms.ui.editor.publish.flow.domain.Phase;

/**
 * Implement ConsumerTask to create publish material
 * consuming data provided by {@link Input}
 *
 * @author segabor
 *
 */
public abstract class ConsumerTask<T> extends PublishTask implements Runnable {

    protected T input;

    protected ConsumerTask() {
        super();
    }

    @Deprecated
    protected ConsumerTask(Long publishId, Phase phase, T input) {
        super(publishId, phase);

        this.input = input;
    }

    public void setInput(T input) {
        this.input = input;
    }
}
