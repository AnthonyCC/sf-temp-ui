package com.freshdirect.mobileapi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryRef;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentRef;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.util.SortStrategyElement;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.data.WhatsGoodCategory;
import com.freshdirect.mobileapi.model.tagwrapper.GetDealsSKUTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.GetPeakProduceTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ItemGrabberTagWrapper;
import com.freshdirect.mobileapi.model.tagwrapper.ItemSorterTagWrapper;
import com.freshdirect.mobileapi.util.GeneralCacheAdministratorFactory;
import com.freshdirect.mobileapi.util.MobileApiProperties;
import com.freshdirect.webapp.taglib.fdstore.layout.LayoutManager.Settings;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * @author Rob
 *
 */
public class WhatsGood {

    // 5minutes
    private static final int REFRESH_PERIOD = 300;

    private static GeneralCacheAdministrator cacheAdmin = GeneralCacheAdministratorFactory.getCacheAdminInstance();

    private static final Logger LOG = Logger.getLogger(WhatsGood.class);

    /**
     * Returns list of category Ids configured in the property file (or CMS)
     * @return
     */
    private static String[] getWhatsGoodCategoryIds() {
        /*
         * DUP: FDWebSite/docroot/whatsgood.jsp
         * LAST UPDATED ON: 12/10/2009
         * LAST UPDATED WITH SVN#: 4266
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: The duplicated code retrieves category ids for what's good section
         */
        String strWGRows = "";

        //get property with rows
        strWGRows = MobileApiProperties.getWhatsGoodCatIds();

        //check for config
        if (strWGRows.toLowerCase().indexOf("useconfig:") > -1) {
            LOG.debug("PAGE config: config found!");

            String[] resultConfig = strWGRows.split(":");
            DomainValue configTest = null;

            //make sure we get an actual DomainValue
            try {
                LOG.debug("PAGE config: using domainValueId: " + resultConfig[1]);
                configTest = ContentFactory.getInstance().getDomainValueById(resultConfig[1]);
                //log(myDebug, "PAGE config: config.getLabel()="+configTest.getLabel());
                LOG.debug("PAGE config: Using config: " + configTest.getLabel());
                LOG.debug("PAGE config: config.getTheValue()=" + configTest.getTheValue());
                strWGRows = configTest.getTheValue();
            } catch (Exception e) {
                LOG.error("PAGE config: DomainValue has an ERROR. Check fdstore.properties file.", e);
            }

        }
        List<String> whatsGoodCategories = new ArrayList<String>();
        String[] resultWGRows = strWGRows.split(",");
        for (String row : resultWGRows) {
            if (row.indexOf(".") < 0) {
                whatsGoodCategories.add(row);
            }
        }
        return whatsGoodCategories.toArray(new String[0]);
    }

    /**
     * Returns list of category Ids with names
     * @return
     */
    public static List<WhatsGoodCategory> getWhatsGoodCategories() {

        String[] categoryIds = getWhatsGoodCategoryIds();

        List<WhatsGoodCategory> categories = new ArrayList<WhatsGoodCategory>();

        for (String categoryId : categoryIds) {
            CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(categoryId);
            if (null != category) {
                categories.add(new WhatsGoodCategory(categoryId, category.getFullName(), "/media/mobile/iphone/whats_good/whats_good_"
                        + categoryId + ".png"));
            } else if ("wg_deals".equals(categoryId)) {
                categories.add(new WhatsGoodCategory(categoryId, "Brand-Name Deals", "/media/mobile/iphone/whats_good/whats_good_" + categoryId
                        + ".png"));
            }
        }

        return categories;
    }

    private String name;

    private String id;

    private String headerImage;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getHeaderImage() {
        return headerImage;
    }

    /**
     * DUP: /shared/includes/layouts/i_peak_produce_all.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Retrieves the peak produce product list
     * 
     * @return
     * @throws FDException
     * @throws ModelException
     */
    public static List<Product> getPeakProduceProductList(SessionUser user) throws FDException, ModelException {
        List<Product> result = new ArrayList<Product>();
        String deptIds[] = { "fru", "veg" };

        String cacheKey = Product.class.toString() + "getPeakProduceProductList";

        try {
            result = (List<Product>) cacheAdmin.getFromCache(cacheKey, REFRESH_PERIOD);
        } catch (NeedsRefreshException nre) {
            try {
                LOG.debug("Refreshing peak produce product list from CMS to cache with key" + cacheKey);
                for (String deptId : deptIds) {
                    GetPeakProduceTagWrapper wrapper = new GetPeakProduceTagWrapper(deptId, user);
                    List<ProductModel> productModels = wrapper.getPeakProduct();
                    for (ProductModel pm : productModels) {
                        if (!pm.isUnavailable()) {
                            try {
                                result.add(Product.wrap(pm, user.getFDSessionUser().getUser()));
                            } catch (ModelException e) {
                                LOG.warn("ModelException in a product in peak produce. continuing on, instead of failing on entire list.",
                                        e);
                            }
                        }
                    }
                }
                LOG.debug("Updating cache with products");
                cacheAdmin.putInCache(cacheKey, result);
            } catch (Throwable ex) {
                LOG.error("Throwable caught at cache update", ex);
                result = (List<Product>) nre.getCacheContent();
                LOG.debug("Cancelling cache update. Exception encountered.");
                cacheAdmin.cancelUpdate(cacheKey);
            }
        }
        return result;
    }

    //    public static List<Product> getButchersBlockProductList(SessionUser user) throws FDException, ModelException {
    //        return getProducts("our_picks_meat", user);
    //    }

    public static List<Product> getBrandNameDealsProductList(SessionUser user) throws FDException, ModelException {
        List<Product> result = new ArrayList<Product>();

        String cacheKey = Product.class.toString() + "getBrandNameDealsProductList";

        try {
            result = (List<Product>) cacheAdmin.getFromCache(cacheKey, REFRESH_PERIOD);
        } catch (NeedsRefreshException nre) {
            try {
                LOG.debug("Refreshing brand name deals product list from CMS to cache with key" + cacheKey);
                GetDealsSKUTagWrapper tagWrapper = new GetDealsSKUTagWrapper(user);

                List<SkuModel> skus = tagWrapper.getDealsSku();

                for (SkuModel sku : skus) {
                    ProductModel productModel = sku.getProductModel();
                    result.add(Product.wrap(productModel, user.getFDSessionUser().getUser()));
                }
                cacheAdmin.putInCache(cacheKey, result);
            } catch (Throwable ex) {
                LOG.error("Throwable caught at cache update", ex);
                result = (List<Product>) nre.getCacheContent();
                LOG.debug("Cancelling cache update. Exception encountered.");
                cacheAdmin.cancelUpdate(cacheKey);
            }

        }

        return result;
    }

    public static List<Product> getProducts(String contentNodeId, SessionUser user) {
        List<Product> result = new ArrayList<Product>();

        String cacheKey = Product.class.toString() + "getProductListFromContentNodeModel" + contentNodeId;

        try {
            result = (List<Product>) cacheAdmin.getFromCache(cacheKey, REFRESH_PERIOD);
        } catch (NeedsRefreshException nre) {
            try {
                LOG.debug("Refreshing product list from CMS to cache with key" + cacheKey);
                ContentNodeModel currentFolder = ContentFactory.getInstance().getContentNode(contentNodeId);
                ItemGrabberTagWrapper tagWrapper = new ItemGrabberTagWrapper(user);
                tagWrapper.setId("list");

                if ("wg_deals".equals(contentNodeId) && (null == ContentFactory.getInstance().getContentNode(contentNodeId))) {
                    result = getBrandNameDealsProductList(user);
                } else if ("wgd_produce".equals(contentNodeId) && (null == ContentFactory.getInstance().getContentNode(contentNodeId))) {
                    result = getPeakProduceProductList(user);
                } else {
                    List contents = tagWrapper.getProducts(currentFolder);

                    /*
                     * DUP: FDWebSite/docroot/departments/whatsgood/generic_row.jspf
                     * LAST UPDATED ON: 12/10/2009
                     * LAST UPDATED WITH SVN#: 4266
                     * WHY: The following logic was duplicate because it was specified in a JSP file.
                     * WHAT: The duplicated code sorting what's good products
                     */
                    ItemSorterTagWrapper sortTagWrapper = new ItemSorterTagWrapper(user);
                    Settings layoutSettings = new Settings();
                    layoutSettings.setGrabberDepth(0);
                    layoutSettings.setIgnoreDuplicateProducts(true);
                    layoutSettings.setIgnoreShowChildren(false);
                    layoutSettings.addSortStrategyElement(new SortStrategyElement(SortStrategyElement.PRODUCTS_BY_PRIORITY, false));
                    layoutSettings.setReturnHiddenFolders(false);
                    sortTagWrapper.sort(contents, layoutSettings.getSortStrategy());

                    for (Object content : contents) {
                        if (content instanceof ProductModel) {
                            try {
                                result.add(Product.wrap((ProductModel) content, user.getFDSessionUser().getUser()));
                            } catch (ModelException e) {
                                //Don't let one rotten egg ruin it for the bunch
                                LOG.error("ModelException encountered", e);
                            }
                        }
                    }
                }
                if (result.size() > 0) {
                    cacheAdmin.putInCache(cacheKey, result);
                }
            } catch (Throwable ex) {
                LOG.error("Throwable caught at cache update", ex);
                result = (List<Product>) nre.getCacheContent();
                LOG.debug("Cancelling cache update. Exception encountered.");
                cacheAdmin.cancelUpdate(cacheKey);
            }
        }

        return result;
    }

}
