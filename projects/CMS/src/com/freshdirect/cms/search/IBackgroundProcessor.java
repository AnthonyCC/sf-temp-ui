package com.freshdirect.cms.search;

import java.util.Collection;
import java.util.concurrent.Future;

import com.freshdirect.cms.publish.Publish;

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

    Future<Object> rebuildAutocomplete();

    void executePublish(Publish publish);
    
}
