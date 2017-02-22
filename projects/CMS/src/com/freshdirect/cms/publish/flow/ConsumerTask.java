package com.freshdirect.cms.publish.flow;

/**
 * Implement ConsumerTask to create publish material
 * consuming data provided by {@link Input}
 * 
 * @author segabor
 *
 */
public abstract class ConsumerTask<T> extends PublishTask implements Runnable {

    protected final T input;

    protected ConsumerTask(String publishId, Phase phase, T input) {
        super(publishId, phase);
        
        this.input = input;
    }    
}
