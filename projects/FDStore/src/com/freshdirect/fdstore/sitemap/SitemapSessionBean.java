package com.freshdirect.fdstore.sitemap;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.sitemap.config.DefaultSitemapConfigStrategy;
import com.freshdirect.fdstore.sitemap.config.FoodKickSitemapConfigStrategy;
import com.freshdirect.fdstore.sitemap.config.FreshDirectSitemapConfigStrategy;
import com.freshdirect.fdstore.sitemap.config.SitemapCmsPopulator;
import com.freshdirect.fdstore.sitemap.config.SitemapConfigStrategy;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.multistore.MultiStoreContext;
import com.freshdirect.storeapi.multistore.MultiStoreContextUtil;

public class SitemapSessionBean extends SessionBeanSupport {

    private static final long serialVersionUID = -5471311777331918763L;

    private static final Category LOGGER = LoggerFactory.getInstance(SitemapSessionBean.class);

    /**
     * Constructor.
     */
    public SitemapSessionBean() {
        super();
    }

    /**
     * Template method that returns the cache key to use for caching resources.
     *
     * @return the bean's home interface name
     */
    @Override
    protected String getResourceCacheKey() {
        return "com.freshdirect.sitemap.ejb.SitemapHome";
    }

    /**
     * Remove method for container to call.
     *
     * @throws EJBException
     *             throws EJBException if there is any problem.
     */
    @Override
    public void ejbRemove() {
    }

    public void generateSitemap() throws FDResourceException, RemoteException {
        LOGGER.info("Starting sitemap generation");

        EnumEStoreId eStore = CmsManager.getInstance().getEStoreEnum();
        MultiStoreContext context = MultiStoreContextUtil.getContext();
        try {
            if (context.isSingleStore()) {
                SitemapService service = SitemapService.defaultService();
                SitemapCmsPopulator configurator = SitemapCmsPopulator.getInstance();
                SitemapConfigStrategy configStrategy = null;

                switch (eStore) {
                    case FD:
                        configStrategy = new FreshDirectSitemapConfigStrategy(configurator);
                        break;

                    case FDX:
                        configStrategy = new FoodKickSitemapConfigStrategy(configurator);
                        break;

                    default:
                        configStrategy = new DefaultSitemapConfigStrategy();
                        LOGGER.error("Sitemap does not created, it does not support current store id: " + eStore.getContentId());
                        break;
                }

                SitemapGenerator generator = new SitemapGenerator(service, configStrategy);
                generator.generateAllTypes();
                LOGGER.info("All sitemaps are generated for store: " + eStore.getContentId());
            } else {
                LOGGER.info("CMS node does not start in single store mode, start in " + context);
            }

            LOGGER.info("Ending sitemap generation");
        } catch (Exception e) {
            LOGGER.error("Exception :" + e.getMessage());
            throw new FDResourceException(e);
        }
    }

}
