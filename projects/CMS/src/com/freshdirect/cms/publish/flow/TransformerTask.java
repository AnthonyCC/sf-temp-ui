package com.freshdirect.cms.publish.flow;

import java.util.concurrent.Callable;


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

    protected final T input;
    
    protected TransformerTask(String publishId, Phase phase, T input) {
        super(publishId, phase);
        
        this.input = input;
    }
}
