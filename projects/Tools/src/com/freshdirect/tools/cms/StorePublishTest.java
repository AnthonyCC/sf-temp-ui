package com.freshdirect.tools.cms;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.DraftContext;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.publish.EnumPublishStatus;
import com.freshdirect.cms.publish.LuceneIndexerTask;
import com.freshdirect.cms.publish.Publish;
import com.freshdirect.cms.publish.PublishTask;
import com.freshdirect.cms.publish.PublishXmlTask;
import com.freshdirect.cms.search.ContentSearchServiceI;
import com.freshdirect.framework.conf.FDRegistry;

/**
 * A simple XML Publish test tool
 * 
 * Do not forget to prepend classpath configuration with the reference 'properties' folder
 * in run configuration
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

        publish.setId( UUID.randomUUID().toString() );
        
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
        publish.setPath(basePath + "/" + publish.getId() + "/" + publish.getStoreId());
        
        ContentSearchServiceI ssvc = (ContentSearchServiceI) FDRegistry.getInstance().getService( com.freshdirect.cms.search.ContentSearchServiceI.class );

        // store content writer
        PublishTask task = new PublishXmlTask(svc, "Store.xml");
        task.execute(publish);

        
        // content indexer
        task = new LuceneIndexerTask(svc, ssvc);
        task.execute(publish);
	}
}
