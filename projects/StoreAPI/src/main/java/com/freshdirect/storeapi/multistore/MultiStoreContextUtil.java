package com.freshdirect.storeapi.multistore;

import org.apache.log4j.Logger;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.framework.util.log.LoggerFactory;

public final class MultiStoreContextUtil {

    private static final Logger LOGGER = LoggerFactory.getInstance(MultiStoreContextUtil.class);

    private MultiStoreContextUtil() {}

    public static MultiStoreContext getContext() {

        ContextualContentProvider cms = CmsServiceLocator.contentProviderService();

        final MultiStoreContext context;

        if (cms == null) {

            LOGGER.debug("CMS Core is not available");

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

        ContextualContentProvider cms = CmsServiceLocator.contentProviderService();

        final boolean dbOnlyServiceFound = cms != null && !cms.isReadOnlyContent();

        return dbOnlyServiceFound;
    }

}
