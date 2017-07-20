package com.freshdirect.webapp.ajax.modulehandling.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.util.ProductPromotionUtil;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.FilteringProductItem;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SortOptionModel;
import com.freshdirect.fdstore.content.SortStrategyType;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.fdstore.content.browse.sorter.ProductItemSorterFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.ValueHolder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SectionDataCointainer;
import com.freshdirect.webapp.ajax.browse.data.BrowseDataContext;
import com.freshdirect.webapp.ajax.browse.data.CmsFilteringFlowResult;
import com.freshdirect.webapp.ajax.browse.data.SectionContext;
import com.freshdirect.webapp.ajax.browse.data.SectionData;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringFlow;
import com.freshdirect.webapp.ajax.filtering.CmsFilteringNavigator;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.filtering.NavigationUtil;
import com.freshdirect.webapp.ajax.filtering.ProductItemComparatorUtil;
import com.freshdirect.webapp.ajax.modulehandling.data.IconData;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

public class ModuleContentService {

    private static final ModuleContentService INSTANCE = new ModuleContentService();
    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleContentService.class);

    private int MAX_ITEMS = FDStoreProperties.getHomepageRedesignProductLimitMax();

    public static ModuleContentService getDefaultService() {
        return INSTANCE;
    }

    private ModuleContentService() {
    }

    public List<ProductData> generateBrowseProductData(CmsFilteringNavigator nav, FDUserI user) throws FDResourceException, InvalidFilteringArgumentException {

        List<ProductData> products = new ArrayList<ProductData>();
        final CmsFilteringFlowResult result = CmsFilteringFlow.getInstance().doFlow(nav, (FDSessionUser) user);

        SectionDataCointainer sectionDataContainer = result.getBrowseDataPrototype().getSections();
        List<SectionData> sections = sectionDataContainer.getSections();
        
        loadProductsFromSections(sectionDataContainer,sections,0,products);

        return products;
    }

    public List<ProductData> limitProductList(List<ProductData> products) {
        if (products.size() > MAX_ITEMS) {
            products = products.subList(0, MAX_ITEMS);
        }
        return products;
    }

    public List<ProductData> setMaxProductLinesForProductList(List<ProductData> productDatas, int productListCarouselLineCount) {
        return productDatas.subList(0, Math.min(productDatas.size(), productListCarouselLineCount * 4));
    }

    public List<ProductData> generateRecommendationProducts(HttpSession session, FDUserI user, String siteFeature) {
        List<ProductModel> products = new ArrayList<ProductModel>();
        Recommendations results = null;
        String variantId = null;

        try {
            FDStoreRecommender recommender = FDStoreRecommender.getInstance();
            results = recommender.getRecommendations(EnumSiteFeature.getEnum(siteFeature), user, ProductRecommenderUtil.createSessionInput(session, user, MAX_ITEMS, null, null));
            products = results.getAllProducts();
            variantId = results.getVariant().getId();
        } catch (FDResourceException e) {
            LOGGER.warn("Failed to get recommendations for siteFeature:" + siteFeature, e);
        }

        List<ProductData> productDatas = new ArrayList<ProductData>();
        if (products.size() > 0) {
            for (ProductModel product : products) {
                try {
                    ProductData productData = ProductDetailPopulator.createProductData(user, product);
                    productData = ProductDetailPopulator.populateBrowseRecommendation(user, productData, product);
                    productData.setVariantId(variantId);
                    productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, variantId));
                    productDatas.add(productData);
                } catch (FDResourceException e) {
                    LOGGER.error("failed to create ProductData", e);
                } catch (FDSkuNotFoundException e) {
                    LOGGER.error("failed to create ProductData", e);
                } catch (HttpErrorResponse e) {
                    LOGGER.error("failed to create ProductData", e);
                }
            }
        }

        return productDatas;
    }

    public SectionDataCointainer loadBrowseSectionDataContainer(String categoryId, FDUserI user) throws FDResourceException, InvalidFilteringArgumentException {
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

            List<ProductData> products = new ArrayList<ProductData>();
            final CmsFilteringFlowResult result = CmsFilteringFlow.getInstance().doFlow(nav, (FDSessionUser) user);

            sectionDataContainer = result.getBrowseDataPrototype().getSections();

        }

        List<SectionData> sections = sectionDataContainer.getSections();
        removeBrowseSectionDataContainerUnavailableProducts(sectionDataContainer, sections, 0);

        return sectionDataContainer;
    }

    public List<ProductData> loadBrowseProducts(String categoryId, FDUserI user) throws FDResourceException, InvalidFilteringArgumentException {

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
        FDSessionUser sessionUser = (FDSessionUser) user;
        DepartmentModel department = (DepartmentModel) ContentFactory.getInstance().getContentNode(departmentId);
        ValueHolder<Variant> out = new ValueHolder<Variant>();
        List<ProductData> products = new ArrayList<ProductData>();

        try {
            List<ProductModel> recommendedItems = ProductRecommenderUtil.getFeaturedRecommenderProducts(department, sessionUser, null, out);
            String variantId = out.isSet() ? out.getValue().getId() : null;

            for (ProductModel product : recommendedItems) {

                ProductData productData = ProductDetailPopulator.createProductData(user, product);
                productData = ProductDetailPopulator.populateBrowseRecommendation(user, productData, product);
                productData.setVariantId(variantId);
                productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, variantId));
                products.add(productData);

            }
        } catch (FDResourceException e) {
            LOGGER.error("failed to create ProductData", e);
        } catch (FDSkuNotFoundException e) {
            LOGGER.error("failed to create ProductData", e);
        } catch (HttpErrorResponse e) {
            LOGGER.error("failed to create ProductData", e);
        }

        return products;
    }

    public List<ProductData> loadPresidentPicksProducts(FDUserI user) {
        List<ProductModel> promotionProducts = new ArrayList<ProductModel>();
        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(FDStoreProperties.getHomepageRedesignPrespicksCategoryId());
        FDSessionUser sessionUser = (FDSessionUser) user;
        String variantId = null;

        // PRODUCT LOADING
        if (category != null) {
            promotionProducts = category.getProducts();
        }

        List<ProductModel> featProds = ProductPromotionUtil.getFeaturedProducts(promotionProducts, false);
        List<ProductModel> nonfeatProds = ProductPromotionUtil.getNonFeaturedProducts(promotionProducts, false);

        List<ProductData> productDatas = new ArrayList<ProductData>();

        List<ProductModel> filteredProductModels = sortPresidentsPicks(featProds, nonfeatProds, user);

        for (ProductModel product : filteredProductModels) {
            try {
                ProductData productData = ProductDetailPopulator.createProductData(sessionUser, product);
                productData = ProductDetailPopulator.populateBrowseRecommendation(sessionUser, productData, product);
                productData.setVariantId(variantId);
                productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, variantId));
                productDatas.add(productData);
            } catch (FDResourceException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (FDSkuNotFoundException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (HttpErrorResponse e) {
                LOGGER.error("failed to create ProductData", e);
            }
        }

        return productDatas;

    }

    public List<ProductData> loadStaffPicksProducts(FDUserI user) {
        List<ProductModel> promotionProducts = new ArrayList<ProductModel>();
        String variantId = null;
        CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode(FDStoreProperties.getHomepageRedesignStaffpicksCategoryId());
        FDSessionUser sessionUser = (FDSessionUser) user;

        if (category != null) {
            promotionProducts = category.getProducts();
        }

        List<ProductModel> availableFeaturedProducts = new ArrayList<ProductModel>();
        for (ProductModel productModel : promotionProducts) {
            if (productModel.isFullyAvailable() && !productModel.isDiscontinued()) {
                availableFeaturedProducts.add(productModel);
            }
        }

        List<ProductData> productDatas = new ArrayList<ProductData>();

        for (ProductModel product : promotionProducts) {
            try {
                ProductData productData = ProductDetailPopulator.createProductData(sessionUser, product);
                productData = ProductDetailPopulator.populateBrowseRecommendation(sessionUser, productData, product);
                productData.setVariantId(variantId);
                productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, variantId));
                productDatas.add(productData);
            } catch (FDResourceException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (FDSkuNotFoundException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (HttpErrorResponse e) {
                LOGGER.error("failed to create ProductData", e);
            }
        }

        return productDatas;
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
        List<ContentKey> featuredProductsContentKeys = (List<ContentKey>) brand.getAttributeValue("FEATURED_PRODUCTS");
        List<ProductModel> featuredProducts = new ArrayList<ProductModel>();
        FDSessionUser sessionUser = (FDSessionUser) user;

        for (ContentKey contentKey : featuredProductsContentKeys) {
            featuredProducts.add((ProductModel) ContentFactory.getInstance().getContentNodeByKey(contentKey));
        }

        for (ProductModel productModel : featuredProducts) {
            if (!productModel.isFullyAvailable() && productModel.isDiscontinued()) {
                featuredProducts.remove(productModel);
            }
        }

        List<ProductData> productDatas = new ArrayList<ProductData>();

        for (ProductModel product : featuredProducts) {
            try {
                ProductData productData = ProductDetailPopulator.createProductData(sessionUser, product);
                productData = ProductDetailPopulator.populateBrowseRecommendation(sessionUser, productData, product);
                productData.setVariantId(null);
                productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, null));
                productDatas.add(productData);
            } catch (FDResourceException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (FDSkuNotFoundException e) {
                LOGGER.error("failed to create ProductData", e);
            } catch (HttpErrorResponse e) {
                LOGGER.error("failed to create ProductData", e);
            }
        }

        return productDatas;

    }

    private List<ProductModel> sortPresidentsPicks(List<ProductModel> featProds, List<ProductModel> nonfeatProds, FDUserI user) {

        List<ProductModel> availableFeaturedProducts = new ArrayList<ProductModel>();
        for (ProductModel productModel : featProds) {
            if (productModel.isFullyAvailable() && !productModel.isDiscontinued()) {
                availableFeaturedProducts.add(productModel);
            }
        }

        List<ProductModel> availableNonFeaturedProducts = new ArrayList<ProductModel>();
        for (ProductModel productModel : nonfeatProds) {
            if (productModel.isFullyAvailable() && !productModel.isDiscontinued()) {
                availableNonFeaturedProducts.add(productModel);
            }
        }

        // PRODUCTMODEL TO FILTERINGPRODUCT CONVERSION
        List<FilteringProductItem> filteringProducts = new ArrayList<FilteringProductItem>();
        if (null != nonfeatProds) {
            for (ProductModel productModel : availableNonFeaturedProducts) {
                FilteringProductItem item = new FilteringProductItem(productModel);
                filteringProducts.add(item);
            }
        }

        // SORTING PREPARATION
        BrowseDataContext sortingBrowseDataContext = new BrowseDataContext();
        SortOptionModel defaultSorter = null;
        Comparator<FilteringProductItem> comparator = null;
        SortStrategyType usedSortStrategy = null;
        SectionContext sortingSectionContext = new SectionContext(filteringProducts);
        List<SectionContext> sortingSectionContexts = new ArrayList<SectionContext>();
        CmsFilteringNavigator nav = new CmsFilteringNavigator();

        // SORTING SETUP
        sortingSectionContexts.add(sortingSectionContext);
        sortingBrowseDataContext.setSectionContexts(sortingSectionContexts);
        
        //defaultSorter = ContentFactory.getInstance().getStore().getPresidentsPicksPageSortOptions().get(0);
        StoreModel storeModel=ContentFactory.getInstance().getStore();
	        if(storeModel!=null && !storeModel.getPresidentsPicksPageSortOptions().isEmpty()) {
	        	 defaultSorter = storeModel.getPresidentsPicksPageSortOptions().get(0);
	        }
        

        if (defaultSorter != null) {
            usedSortStrategy = defaultSorter.getSortStrategyType();
        } else {
            LOGGER.info("No default sorter was present for president picks setting name sortation.");
            usedSortStrategy = SortStrategyType.NAME;
        }

        comparator = ProductItemSorterFactory.createComparator(usedSortStrategy, user, false);
        nav.setPageTypeType(FilteringFlowType.PRES_PICKS);
        nav.setOrderAscending(true);

        // SORTING
        if (comparator == null) {
            comparator = ProductItemSorterFactory.createDefaultComparator();
        }
        ProductItemComparatorUtil.sortSectionDatas(sortingBrowseDataContext, comparator); // sort items/section
        ProductItemComparatorUtil.postProcess(sortingBrowseDataContext, nav, usedSortStrategy, user);

        // ADDING FEATURED PRODUCTS BEFORE ANYTHING ELSE
        List<ProductModel> filteredProductModels = new ArrayList<ProductModel>();
        filteredProductModels.addAll(availableFeaturedProducts);

        // FILTERINGPRODUCT TO PRODUCT MODEL CONVERSION
        List<SectionContext> sectionContextlist=sortingBrowseDataContext.getSectionContexts();
        if(!sectionContextlist.isEmpty() && sectionContextlist.get(0)!=null) {
	        for (FilteringProductItem filteringProduct : sectionContextlist.get(0).getProductItems()) {
	            filteredProductModels.add(filteringProduct.getProductModel());
	        }
        }

        return filteredProductModels;
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
}
