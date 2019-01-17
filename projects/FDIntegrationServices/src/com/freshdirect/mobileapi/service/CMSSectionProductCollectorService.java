package com.freshdirect.mobileapi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.CMSPotatoSectionModel;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.ProductPotatoUtil;
import com.freshdirect.storeapi.content.CMSPageRequest;
import com.freshdirect.storeapi.content.CMSSectionModel;
import com.freshdirect.storeapi.content.CMSWebPageModel;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SortOptionModel;
import com.freshdirect.storeapi.content.SortStrategyType;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.webapp.ajax.browse.data.NavDepth;
import com.freshdirect.webapp.ajax.filtering.BrowseDataBuilderFactory;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

/**
 *
 * Collects the products for the sections
 *
 */
public class CMSSectionProductCollectorService {

    private static final Category LOGGER = LoggerFactory.getInstance(CMSSectionProductCollectorService.class);

    private static final CMSSectionProductCollectorService INSTANCE = new CMSSectionProductCollectorService();
    private static final String[] SECTIONS_TYPES_THAT_SHOULD_HAVE_PRODUCTS = { "HorizontalPickList", "VerticalPickList" };

    public static CMSSectionProductCollectorService getDefaultService() {
        return INSTANCE;
    }

    private CMSSectionProductCollectorService() {
        // hiding default constructor
    }

    public void addProductsToSection(SessionUser user, CMSWebPageModel page, CMSPageRequest cmsPageRequest) {
        if (page != null) {
            List<CMSSectionModel> sectionWithProducts = new ArrayList<CMSSectionModel>();
            List<String> errorProductKeys = new ArrayList<String>();
            for (final CMSSectionModel section : page.getSections()) {
                LOGGER.debug("Loading section data: " + section.getName());
                final CMSPotatoSectionModel potatoSection = CMSPotatoSectionModel.withSection(section);
                potatoSection.setProducts(collectProductPotatos(user, section.getProductList(), errorProductKeys, cmsPageRequest.isPreview()));
                potatoSection.getProducts().addAll(collectProductPotatosFromCategories(user, potatoSection, cmsPageRequest.isPreview()));
                appendProductListFieldOnSection(potatoSection);
                if (areMustHaveSectionProductsAvailable(section.getMustHaveProdList(), section.getProductList(), errorProductKeys, user, cmsPageRequest.isPreview())) {
                    sectionWithProducts.add(potatoSection);
                }
                errorProductKeys.clear();
            }
            page.setSections(sectionWithProducts);
            applySectionLimits(page, cmsPageRequest.isPreview());
        }
    }

    private List<ProductPotatoData> makeProductPotatos(SessionUser user, Collection<ProductModel> products, boolean enableProductIncomplete) {
        final List<ProductPotatoData> potatoes = new ArrayList<ProductPotatoData>();
        if (products != null) {
            for (final ProductModel product : products) {
                final ProductPotatoData data = ProductPotatoUtil.getProductPotato(product, user.getFDSessionUser(), false, enableProductIncomplete);
                if (data != null && data.getProductData() != null && data.getProductData().isAvailable()) {
                    potatoes.add(data);
                }
            }
        }
        return potatoes;
    }

    private List<ProductPotatoData> collectProductPotatos(SessionUser user, List<String> productKeys, List<String> errorProductKeys, boolean enableProductIncomplete) {
        final List<ProductPotatoData> potatoes = new ArrayList<ProductPotatoData>();
        List<String> productKeysToRemove = new ArrayList<String>();
        if (productKeys != null) {
            for (final String productKey : productKeys) {
                // extract CMS id
                final String prodId = productKey.substring(FDContentTypes.PRODUCT.name().length() + 1);
                final ProductPotatoData data = ProductPotatoUtil.getProductPotato(prodId, null, user.getFDSessionUser(), false, enableProductIncomplete);
                if (data != null && data.getProductData() != null && data.getProductData().isAvailable()) {
                    potatoes.add(data);
                } else {
                    errorProductKeys.add(productKey);
                    LOGGER.debug("Removing product key = " + productKey);
                    productKeysToRemove.add(productKey);
                }
            }
            productKeys.removeAll(productKeysToRemove);
        }
        return potatoes;
    }

    private boolean areMustHaveSectionProductsAvailable(List<String> mustHaveProductkeys, List<String> productKeys, List<String> errorProductKeys, SessionUser user,
            boolean enableProductIncomplete) {
        boolean allMustHaveProductsAreAvailable = true;
        if (mustHaveProductkeys != null && productKeys != null) {
            for (String mustHaveProductId : mustHaveProductkeys) {
                if (!productKeys.contains(mustHaveProductId) || errorProductKeys.contains(mustHaveProductId)) {
                    allMustHaveProductsAreAvailable = false;
                    break;
                }

                final String prodId = mustHaveProductId.substring(FDContentTypes.PRODUCT.name().length() + 1);
                final ProductPotatoData data = ProductPotatoUtil.getProductPotato(prodId, null, user.getFDSessionUser(), false, enableProductIncomplete);
                if (data == null || !data.getProductData().isAvailable()) {
                    allMustHaveProductsAreAvailable = false;
                    break;
                }
            }
        }
        return allMustHaveProductsAreAvailable;
    }

    private List<ProductPotatoData> collectProductPotatosFromCategories(SessionUser user, CMSSectionModel sectionModel, boolean enableProductIncomplete) {
        final FDSessionUser fdUser = user.getFDSessionUser();
        List<ProductPotatoData> productsFromCategories = new ArrayList<ProductPotatoData>();
        List<CategoryModel> categories = new ArrayList<CategoryModel>();
        if (sectionModel.getCategoryList() != null && !sectionModel.getCategoryList().isEmpty()) {
            for (String categoryKey : sectionModel.getCategoryList()) {
                CategoryModel modelToAddd = (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(ContentKeyFactory.get(categoryKey));
                if (modelToAddd != null) {
                    categories.add(modelToAddd);
                } else {
                    LOGGER.debug("Try to load category for section but it was not found. Category key: " + categoryKey);
                }
            }

            for (CategoryModel category : categories) {
                // collect products first from the given category
                Set<ProductModel> grabbedProducts = new HashSet<ProductModel>();
                BrowseDataBuilderFactory.getInstance().collectAllProducts(category, NavDepth.getMaxLevel(), fdUser, grabbedProducts);

                // figure out sorting strategy, pick first or use default
                List<SortOptionModel> sortOptions = category.getSortOptions();
                SortStrategyType sortStrategy = !sortOptions.isEmpty()
                        ? sortOptions.get(0).getSortStrategyType()
                        : null;

                // sort products
                List<ProductModel> productsToSort = new ArrayList<ProductModel>(grabbedProducts);
                List<ProductModel> sortedProducts = ProductRecommenderUtil.sortProducts(fdUser, productsToSort, sortStrategy, false);

                // append sorted set to the whole result set keeping sort order
                productsFromCategories.addAll(makeProductPotatos(user, sortedProducts, enableProductIncomplete));
            }
        }
        return productsFromCategories;
    }

    private void appendProductListFieldOnSection(CMSPotatoSectionModel potatoSectionModel) {
        List<ProductPotatoData> productsInSection = potatoSectionModel.getProducts();
        List<String> productKeys = new ArrayList<String>();
        for (ProductPotatoData productPotato : productsInSection) {
            productKeys.add(productPotato.getProductData().getCMSKey());
        }
        potatoSectionModel.setProductList(productKeys);
    }

    private void applySectionLimits(CMSWebPageModel webPageModel, boolean isPreviewRequest) {
        List<CMSSectionModel> sections = webPageModel.getSections();
        List<CMSSectionModel> limitAppliedSections = new ArrayList<CMSSectionModel>();
        for (CMSSectionModel section : sections) {
            if (shouldDisplaySection(section) || isPreviewRequest) {
                if (shouldLimitProductNumber(section) && section.getProductList().size() > section.getMaximumProductLimit()) {
                    section.setProductList(section.getProductList().subList(0, section.getMaximumProductLimit()));
                    if (section instanceof CMSPotatoSectionModel) {
                        filterProductPotatoesInSection((CMSPotatoSectionModel) section);
                    }
                }
                limitAppliedSections.add(section);
            }
        }
        webPageModel.setSections(limitAppliedSections);
    }

    private void filterProductPotatoesInSection(CMSPotatoSectionModel potatoSection) {
        List<ProductPotatoData> productPotatoes = potatoSection.getProducts();
        List<ProductPotatoData> filteredProductPotatoes = new ArrayList<ProductPotatoData>();
        for (ProductPotatoData product : productPotatoes) {
            if (potatoSection.getProductList().contains(product.getProductData().getCMSKey())) {
                filteredProductPotatoes.add(product);
            }
        }
        potatoSection.setProducts(filteredProductPotatoes);
    }

    private boolean shouldDisplaySection(CMSSectionModel sectionModel) {
        Integer minimumLimit = (sectionModel.getMinimumProductLimit() == null) ? 0 : sectionModel.getMinimumProductLimit();
        int numberOfProducts = (sectionModel.getProductList() == null) ? 0 : sectionModel.getProductList().size();
        boolean sectionConformsMinimumLimit = numberOfProducts >= minimumLimit;
        boolean shouldHaveProducts = sectionShouldHaveProduct(sectionModel);
        boolean sectionSourceIsCategory = sectionHasCategorySource(sectionModel);

        return !shouldHaveProducts || (!sectionSourceIsCategory && shouldHaveProducts && numberOfProducts > 0)
                || (sectionSourceIsCategory && shouldHaveProducts && sectionConformsMinimumLimit);
    }

    private boolean sectionShouldHaveProduct(CMSSectionModel sectionModel) {
        return Arrays.asList(SECTIONS_TYPES_THAT_SHOULD_HAVE_PRODUCTS).contains(sectionModel.getDisplayType());
    }

    private boolean sectionHasCategorySource(CMSSectionModel sectionModel) {
        return (sectionModel.getCategoryList() != null && !sectionModel.getCategoryList().isEmpty());
    }

    private boolean shouldLimitProductNumber(CMSSectionModel sectionModel) {
        boolean sectionSourceIsCategory = sectionHasCategorySource(sectionModel);
        boolean sectionDisplayTypeNeedsLimit = sectionShouldHaveProduct(sectionModel);

        return (sectionSourceIsCategory && sectionDisplayTypeNeedsLimit);
    }
}
