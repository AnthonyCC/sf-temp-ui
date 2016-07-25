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
        
        } else if (isCMSPoweredByDatabase()) {
            // CMS runs on DB == multistore mode is enabled
            // TODO elaborate the case of MultiStoreProperties.isCmsMultiStoreEnabled() ...
            
            LOGGER.debug("Database backed CMS context ...");
            // LOGGER.debug("... multi-store mode enabled: " + MultiStoreProperties.isCmsMultiStoreEnabled());
            LOGGER.debug("... multi-store mode: enabled implicitly");
            LOGGER.debug("... store-key is dedicated: " + MultiStoreProperties.hasCmsStoreID());

            // Preview Node := multi-store enabled (CMS cluster requires) AND eStore ID is explicitly set (preview node requires)
            final boolean isPreviewNode = /* MultiStoreProperties.isCmsMultiStoreEnabled() && */ MultiStoreProperties.hasCmsStoreID();

            if (isPreviewNode) {
                LOGGER.debug("... CMS Preview with store key: " + MultiStoreProperties.getCmsStoreId());
            } else {
                LOGGER.debug("... CMS Editor / Admin node (no dedicated store key)");
            }

            context = isPreviewNode ? MultiStoreContext.MULTISTORE_WITH_KEY : MultiStoreContext.MULTISTORE;
            
        } else {
            LOGGER.debug("Store.xml powered read-only CMS context");

            context = MultiStoreContext.SINGLESTORE;
        }

        LOGGER.info("CMS Multi-Store context: " + context);
        
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
