package com.freshdirect.cms.publish.flow;

import java.util.concurrent.Callable;

/**
 * {@link ProducerTask} implementations usually generates input data for processing by {@link ConsumerTask} instances.
 * 
 * @author segabor
 *
 * @param <V>
 *            type of data task provides after successful execution
 */
public abstract class ProducerTask<V> extends PublishTask implements Callable<V> {

    protected ProducerTask(String publishId, Phase phase) {
        super(publishId, phase);
    }
}
