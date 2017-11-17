package com.freshdirect.tools.cms;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.flow.tasks.IndexingTask;
import com.freshdirect.cms.publish.flow.tasks.StorePublisherTask;
import com.freshdirect.fdstore.EnumEStoreId;

/**
 * A simple XML Publish test tool
 * 
 * Do not forget to prepend classpath configuration with the reference 'properties' folder in run configuration
 * 
 * @author segabor
 *
 */
public class StorePublishTest {

    public static void main(String[] args) {

        Date date = new Date();

        Publish publish = new Publish();
        publish.setTimestamp(date);
        publish.setUserId("vubul");
        publish.setStatus(EnumPublishStatus.PROGRESS);
        publish.setDescription("teszt");
        publish.setLastModified(date);

        publish.setId(UUID.randomUUID().toString());

        // publish.setStoreId("FreshDirect");
        // publish.setStoreId("FDX");

        String basePath = "/tmp";

        final ContentServiceI svc = CmsManager.getInstance();

        Set<ContentKey> storeKeys = svc.getContentKeysByType(FDContentTypes.STORE, DraftContext.MAIN);

        for (ContentKey key : storeKeys) {
            doPublish(basePath, key.getId(), publish, svc);
        }
    }

    private static void doPublish(String basePath, String storeId, Publish publish, ContentServiceI svc) {
        publish.setStoreId(storeId);
        publish.setPath(basePath + File.separator + publish.getId() + File.separator + publish.getStoreId());

        Collection<ContentNodeI> contentNodes = svc.getContentNodes(svc.getContentKeys(DraftContext.MAIN), DraftContext.MAIN).values();

        // store content writer
        StorePublisherTask task = new StorePublisherTask(publish.getId(), Phase.WRITE_OUT, contentNodes, publish.getBasePath(),
                EnumEStoreId.valueOfContentId(publish.getStoreId()));
        task.run();

        // content indexer
        IndexingTask indexingTask = new IndexingTask(publish.getId(), Phase.INDEXING, publish.getBasePath(), EnumEStoreId.valueOfContentId(storeId), contentNodes);
        indexingTask.run();
    }
}
