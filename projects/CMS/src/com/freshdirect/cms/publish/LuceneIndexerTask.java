package com.freshdirect.cms.publish;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.cms.search.LuceneSearchService;
import com.freshdirect.cms.util.SingleStoreFilterHelper;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * This task re-indexes the whole cms service with the given search service.
 * 
 * @author zsombor
 *
 */
public class LuceneIndexerTask implements PublishTask {

    private final static Logger LOGGER = LoggerFactory.getInstance(LuceneIndexerTask.class);

    private final ContentServiceI contentService;
    private final DraftContext draftContext = DraftContext.MAIN;
    @SuppressWarnings("unused")
	private final ContentSearchServiceI searchService;

	public LuceneIndexerTask(ContentServiceI contentService, ContentSearchServiceI searchService) {
        this.contentService = contentService;
        // actually this service is not used since it is configured for global (multi-store) scene
        this.searchService = searchService;
    }

    
    /**
     * Create a new instance of {#link {@link LuceneSearchService}
     * This service will index only nodes belonging to a particular store
     * 
     * @param indexLocation Path of index files.
     * Ideally it points to "${cms.publish.basePath}/<publish ID>/<store ID>/index"
     * 
     * @return
     */
    private LuceneSearchService createSearchService(final String indexLocation) {
        LuceneSearchService svc = new LuceneSearchService();

        // pick existing configuration from hivemodule_search.xml
        svc.setIndexes( FDRegistry.getInstance().getConfiguration("com.freshdirect.cms.search.SearchIndex") );
        // but use store dependent index location path
		svc.setIndexLocation( indexLocation );
		svc.initialize();
		
		return svc;
    }


    @Override
    public void execute(Publish publish) {
        LuceneSearchService svc = createSearchService(publish.getPath() + "/index");


        LOGGER.info("loading keys");
        Set<ContentKey> keys = contentService.getContentKeys(draftContext);
        LOGGER.info("loading nodes:"+keys.size());
        Map<ContentKey, ContentNodeI> nodesMap = contentService.getContentNodes(keys, draftContext);
        
        Collection<ContentNodeI> _nodes = nodesMap.values();
        
        LOGGER.info("filtering nodes:"+_nodes.size());
        _nodes = SingleStoreFilterHelper.filterContentNodes(publish.getStoreId(), _nodes, contentService, draftContext);
        LOGGER.info("  ... stripped down to :"+_nodes.size());
        
        LOGGER.info("indexing nodes:"+_nodes.size());
        svc.index(_nodes, true);
        publish.getMessages().add(new PublishMessage(PublishMessage.INFO, "Indexed " + _nodes.size() + " node"));
        LOGGER.info("indexing spelling:"+_nodes.size());
        svc.indexSpelling(_nodes);
        publish.getMessages().add(new PublishMessage(PublishMessage.INFO, "Spell Index calculated for " + _nodes.size() + " node"));
    }

    @Override
    public String getComment() {
        return "Lucene search index building";
    }

}
