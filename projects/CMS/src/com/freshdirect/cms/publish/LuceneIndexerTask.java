package com.freshdirect.cms.publish;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.search.ContentSearchServiceI;
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
    private final ContentSearchServiceI searchService;

    public LuceneIndexerTask(ContentServiceI contentService, ContentSearchServiceI searchService) {
        this.contentService = contentService;
        this.searchService = searchService;
    }

    @Override
    public void execute(Publish publish) {
        LOGGER.info("loading keys");
        Set<ContentKey> keys = contentService.getContentKeys();
        LOGGER.info("loading nodes:"+keys.size());
        Map<ContentKey, ContentNodeI> nodes = contentService.getContentNodes(keys);
        LOGGER.info("indexing nodes:"+nodes.size());
        searchService.index(nodes.values(), true);
        publish.getMessages().add(new PublishMessage(PublishMessage.INFO, "Indexed " + nodes.size() + " node"));
        LOGGER.info("indexing spelling:"+nodes.size());
        searchService.indexSpelling(nodes.values());
        publish.getMessages().add(new PublishMessage(PublishMessage.INFO, "Spell Index calculated for " + nodes.size() + " node"));
    }

    @Override
    public String getComment() {
        return "Lucene search index building";
    }

}
