package com.freshdirect.cms.search;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishTask;

public interface IBackgroundProcessor {

    BackgroundStatus getStatus();
    
    /**
     * Re-index the nodes, which match the given search terms.
     * @param terms
     */
    Future<Integer> backgroundReindex(final Collection<String> terms);
    
    /**
     * Re-index all the nodes.
     * @return 
     */
    Future<Integer> backgroundReindex();

    /**
     * Rebuild the wine index
     * @param callback
     * @return
     */
    Future<Integer> rebuildWineIndex();

    void executePublish(Publish publish);

    void setPublishTasks(List<PublishTask> publishTasks);

    List<PublishTask> getPublishTasks();
}
