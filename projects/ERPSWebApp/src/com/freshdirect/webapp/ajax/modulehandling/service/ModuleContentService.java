package com.freshdirect.webapp.ajax.modulehandling.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.ValueHolder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.DepartmentModel;
import com.freshdirect.storeapi.content.Image;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SortStrategyType;
import com.freshdirect.storeapi.content.StoreModel;
import com.freshdirect.storeapi.node.ContentNodeUtil;
import com.freshdirect.storeapi.util.ProductPromotionUtil;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SectionDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.SectionData;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringFlow;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.filtering.NavigationUtil;
import com.freshdirect.webapp.ajax.modulehandling.data.IconData;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class ModuleContentService {

    private static final ModuleContentService INSTANCE = new ModuleContentService();
    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleContentService.class);

    private static final int HOMEPAGE_REDESIGN_RECOMMENDER_POPUP_PRODUCT_MAX_LIMIT = 48;

    public static ModuleContentService getDefaultService() {
        return INSTANCE;
    }

    private ModuleContentService() {
    }

    public List<ProductData> generateBrowseProductData(CmsFilteringNavigator nav, FDUserI user) throws FDResourceException, InvalidFilteringArgumentException, FDNotFoundException {

        List<ProductData> products = new ArrayList<ProductData>();
        final CmsFilteringFlowResult result = CmsFilteringFlow.getInstance().doFlow(nav, (FDSessionUser) user);

        SectionDataCointainer sectionDataContainer = result.getBrowseDataPrototype().getSections();
        List<SectionData> sections = sectionDataContainer.getSections();

        loadProductsFromSections(sectionDataContainer,sections,0,products);

        return products;
    }

    public List<ProductData> limitProductList(List<ProductData> products) {
        int homepageRedesignProductLimitMax = FDStoreProperties.getHomepageRedesignProductLimitMax();
        if (products.size() > homepageRedesignProductLimitMax) {
            products = products.subList(0, homepageRedesignProductLimitMax);
        }
        return products;
    }

    public List<ProductData> setMaxProductLinesForProductList(List<ProductData> productDatas, int productListCarouselLineCount) {
        return productDatas.subList(0, Math.min(productDatas.size(), productListCarouselLineCount * 4));
    }

    public List<ProductData> generateRecommendationProducts(HttpSession session, FDUserI user, String siteFeature) {
        List<ProductModel> products = new ArrayList<ProductModel>();
        String variantId = null;

        try {
            Recommendations results = ProductRecommenderUtil.doRecommend(user, session, EnumSiteFeature.getEnum(siteFeature), HOMEPAGE_REDESIGN_RECOMMENDER_POPUP_PRODUCT_MAX_LIMIT,
                    null, null);
            products = results.getAllProducts();
            variantId = results.getVariant().getId();
        } catch (FDResourceException e) {
            LOGGER.warn("Failed to get recommendations for siteFeature:" + siteFeature, e);
        }

        return ProductRecommenderUtil.createProductData(user, products, variantId);
    }

    public SectionDataCointainer loadBrowseSectionDataContainer(String categoryId, FDUserI user)
            throws FDResourceException, InvalidFilteringArgumentException, FDNotFoundException {
        SectionDataCointainer sectionDataContainer = new SectionDataCointainer();
        CategoryModel categoryModel = (CategoryModel) ContentFactory.getInstance().getContentNode(categoryId);
        boolean isForbidden = NavigationUtil.isCategoryForbiddenInContext(user, categoryModel);

        if (!isForbidden) {
            CmsFilteringNavigator nav = new CmsFilteringNavigator();

            // Set special layout false to skip content loading from HMB and RecipeKits.
            nav.setSpecialPage(false);
            nav.setPageTypeType(FilteringFlowType.BROWSE);
            nav.setPageSize(FDStoreProperties.getBrowsePageSize());
            nav.setId(categoryId);
            nav.setAll(true);
            nav.setActivePage(0);

            final CmsFilteringFlowResult result = CmsFilteringFlow.getInstance().doFlow(nav, (FDSessionUser) user);
            sectionDataContainer = result.getBrowseDataPrototype().getSections();
        }

        List<SectionData> sections = sectionDataContainer.getSections();
        removeBrowseSectionDataContainerUnavailableProducts(sectionDataContainer, sections, 0);

        return sectionDataContainer;
    }

    public List<ProductData> loadBrowseProducts(String categoryId, FDUserI user) throws FDResourceException, InvalidFilteringArgumentException, FDNotFoundException {
        List<ProductData> availableProducts = new ArrayList<ProductData>();

        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(categoryId);

        boolean isForbidden = true;

        if (category != null) {
            isForbidden = NavigationUtil.isCategoryForbiddenInContext(user, category);
        }

        if (!isForbidden) {
            CmsFilteringNavigator nav = new CmsFilteringNavigator();

            // Set special layout false to skip content loading from HMB and RecipeKits.
            nav.setSpecialPage(false);
            nav.setPageTypeType(FilteringFlowType.BROWSE);
            nav.setPageSize(FDStoreProperties.getBrowsePageSize());
            nav.setId(categoryId);
            nav.setAll(true);
            nav.setActivePage(0);

            List<ProductData> products = generateBrowseProductData(nav, user);

            for (ProductData productData : products) {
                if (productData.isAvailable() && !productData.isDiscontinued()) {
                    availableProducts.add(productData);
                }
            }
        }
        return availableProducts;
    }

    public List<ProductData> loadFeaturedItems(FDUserI user, String departmentId) throws ClassCastException {
        DepartmentModel department = (DepartmentModel) ContentFactory.getInstance().getContentNode(departmentId);
        ValueHolder<Variant> out = new ValueHolder<Variant>();
        List<ProductData> products = new ArrayList<ProductData>();

        try {
            List<ProductModel> recommendedItems = ProductRecommenderUtil.getFeaturedRecommenderProducts(department, user, null, out);
            String variantId = out.isSet() ? out.getValue().getId() : null;
            products.addAll(ProductRecommenderUtil.createProductData(user, recommendedItems, variantId));
        } catch (FDResourceException e) {
            LOGGER.error("failed to create ProductData", e);
        }

        return products;
    }

    public List<ProductData> loadPresidentPicksProducts(FDUserI user) {
        List<ProductModel> promotionProducts = new ArrayList<ProductModel>();
        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(FDStoreProperties.getHomepageRedesignPrespicksCategoryId());
        String variantId = null;

        // PRODUCT LOADING
        if (category != null) {
            promotionProducts = category.getProducts();
        }

        List<ProductModel> filteredProductModels = new ArrayList<ProductModel>();
        filteredProductModels.addAll(getAvailableProducts(ProductPromotionUtil.getFeaturedProducts(promotionProducts, false)));
        filteredProductModels.addAll(ProductRecommenderUtil.sortProducts(user, getAvailableProducts(ProductPromotionUtil.getNonFeaturedProducts(promotionProducts, false)), getPresidentPicksSortStrategy(), false));

        return ProductRecommenderUtil.createProductData(user, filteredProductModels, variantId);
    }

    public List<ProductData> loadStaffPicksProducts(FDUserI user) {
        List<ProductModel> promotionProducts = new ArrayList<ProductModel>();
        String variantId = null;
        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(FDStoreProperties.getHomepageRedesignStaffpicksCategoryId());

        if (category != null) {
            promotionProducts = category.getProducts();
        }

        return ProductRecommenderUtil.createProductData(user, getAvailableProducts(promotionProducts), variantId);
    }

    public IconData populateIconData(ContentNodeI imageBanner) {
        IconData iconData = new IconData();
        Image iconImage = MediaUtils.generateImageFromImageContentKey(imageBanner.getAttributeValue("ImageBannerImage"));

        iconData.setIconImage(iconImage.getPath());
        iconData.setIconLinkText(ContentNodeUtil.getStringAttribute(imageBanner, "Description"));

        String linkUrl = ContentNodeUtil.getStringAttribute(imageBanner, "bannerURL");

        if (linkUrl == null) {
            ContentKey targetContentKey = (ContentKey) imageBanner.getAttributeValue("Target");
            linkUrl = "/browse.jsp?id=" + targetContentKey.getId();
        }

        iconData.setIconLink(linkUrl);
        iconData.setIconId(imageBanner.getKey().getId());

        return iconData;
    }

    public String populateOpenHTML(ContentNodeI module, FDUserI user) {
        return MediaUtils.generateStringFromHTMLContentKey(module.getAttributeValue("openHTML"), user);
    }

    public List<ProductData> loadBrandFeaturedProducts(ContentNodeI brand, FDUserI user) {
        List<ProductModel> featuredProducts = new ArrayList<ProductModel>();
        List<ContentKey> featuredProductsContentKeys = (List<ContentKey>) brand.getAttributeValue("FEATURED_PRODUCTS");

        for (ContentKey contentKey : featuredProductsContentKeys) {
            ProductModel productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(contentKey);
            if (productModel != null) {
                featuredProducts.add(productModel);
            }
        }

        return ProductRecommenderUtil.createProductData(user, getAvailableProducts(featuredProducts), null);
    }

    private SortStrategyType getPresidentPicksSortStrategy() {
        SortStrategyType sortStrategy = null;
        StoreModel storeModel = ContentFactory.getInstance().getStore();
        if (storeModel != null && !storeModel.getPresidentsPicksPageSortOptions().isEmpty()) {
            sortStrategy = storeModel.getPresidentsPicksPageSortOptions().get(0).getSortStrategyType();
        } else {
            LOGGER.info("No default sorter was present for president picks setting name sortation.");
            sortStrategy = SortStrategyType.NAME;
        }
        return sortStrategy;
    }

    private void loadProductsFromSections(SectionDataCointainer sectionDataContainer, List<SectionData> sections, int level, List<ProductData> products) {
        if (sections != null && sectionDataContainer.getSectionMaxLevel() > level) {
            for (SectionData section : sections) {
                if (section.getProducts() != null) {
                    products.addAll(section.getProducts());

                } else {
                    loadProductsFromSections(sectionDataContainer, section.getSections(), level + 1, products);
                }
            }
        }
    }

    private void removeBrowseSectionDataContainerUnavailableProducts(SectionDataCointainer sectionDataContainer, List<SectionData> sections, int level) {

        if (sections != null && sectionDataContainer.getSectionMaxLevel() > level) {
            for (SectionData section : sections) {
                if (section.getProducts() != null) {
                    for (Iterator<ProductData> iter = section.getProducts().iterator(); iter.hasNext();) {
                        ProductData productData = iter.next();
                        if (!productData.isAvailable() || productData.isDiscontinued()) {
                            iter.remove();
                        }
                    }
                } else {
                    removeBrowseSectionDataContainerUnavailableProducts(sectionDataContainer, section.getSections(), level + 1);
                }
            }
        }
    }

    private List<ProductModel> getAvailableProducts(List<ProductModel> products) {
        List<ProductModel> availableProducts = new ArrayList<ProductModel>();
        for (ProductModel product : products) {
            if (product.isFullyAvailable() && !product.isDiscontinued()) {
                availableProducts.add(product);
            }
        }
        return availableProducts;
    }
}
