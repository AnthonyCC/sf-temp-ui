package com.freshdirect.cms.multistore;

import org.apache.hivemind.Registry;
import org.apache.log4j.Logger;

import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

public final class MultiStoreContextUtil {
    
    private static final Logger LOGGER = LoggerFactory.getInstance(MultiStoreContextUtil.class);
    
    private MultiStoreContextUtil() {}
    
    public static MultiStoreContext getContext(ContentServiceI svc) {
        
        final ContentTypeServiceI typeService = svc != null ? svc.getTypeService() : null;
        
        final MultiStoreContext context;

        if (typeService != null && typeService.getContentTypes().isEmpty()) {
        
            // Check out CMSServiceConfig_crm.xml and NullContentService
            LOGGER.debug("Either content service is null or type service yields nothing -> no CMS loaded or CMS is backed with NullContentService");

            context = MultiStoreContext.NO_STORE;
        
            LOGGER.info("Context: no-store");
        } else if (isCMSPoweredByDatabase()) {
            // CMS runs on DB == multistore mode is enabled

            final boolean hasCmsStoreID = MultiStoreProperties.hasCmsStoreID();
            
            LOGGER.debug("Database backed Store data ...");

            // Preview Node := multi-store enabled (CMS cluster requires) AND eStore ID is explicitly set (preview node requires)
            final boolean isPreviewNode = hasCmsStoreID;

            if (isPreviewNode) {
                LOGGER.info("Context: single-store / DB preview with store key: " + MultiStoreProperties.getCmsStoreId());
            } else {
                LOGGER.info("Context: multi-store : CMS Editor / CMS Admin node");
            }

            context = isPreviewNode ? MultiStoreContext.SINGLESTORE_PREVIEW : MultiStoreContext.MULTISTORE;

        } else {
            LOGGER.info("Context: single-store, Store data is loaded from Store.xml");

            context = MultiStoreContext.SINGLESTORE;
        }
        
        return context;
    }



    /**
     * Find out whether CMS is backed by database.
     * 
     * @return
     */
    public static boolean isCMSPoweredByDatabase() {
        final Registry registry = FDRegistry.getInstance();

        // look for a service which is only used by DB based CMS config
        final boolean dbOnlyServiceFound = registry.containsService("com.freshdirect.cms.ChangeTracker", ContentServiceI.class);

        return dbOnlyServiceFound;
    }
    
}
