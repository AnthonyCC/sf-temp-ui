package com.freshdirect.cms.ui.editor.publish.flow.tasks;

import java.util.concurrent.Callable;

import com.freshdirect.cms.ui.editor.publish.flow.domain.Phase;


/**
 * Abstract task that transforms T input to V
 * during execution of {@link #call()}
 *
 * @author segabor
 *
 * @param <T> input data
 * @param <V> output data
 */
public abstract class TransformerTask<T, V> extends PublishTask implements Callable<V> {

    protected T input;

    protected TransformerTask() {
        super();
    }

    @Deprecated
    protected TransformerTask(Long publishId, Phase phase, T input) {
        super(publishId, phase);

        this.input = input;
    }

    public void setInput(T input) {
        this.input = input;
    }
}
