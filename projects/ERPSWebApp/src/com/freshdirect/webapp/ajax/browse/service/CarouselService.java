package com.freshdirect.webapp.ajax.browse.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.storeapi.content.CategoryModel;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.browse.data.CarouselData;
import com.freshdirect.webapp.ajax.browse.data.CarouselNameCase;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.util.FDURLUtil;

public class CarouselService {

	private static final Logger LOGGER = LoggerFactory.getInstance(CarouselService.class);

    private static final CarouselService INSTANCE = new CarouselService();

    public static final String NEW_PRODUCTS_CAROUSEL_NAME = "New Products";

    public static final String NEW_PRODUCTS_CAROUSEL_VIRTUAL_SITE_FEATURE = "NEW_PRODUCTS";

	private CarouselService() {
	}

	/**
	 * Gives the default carousel service.
	 * 
	 * @return the default service instance
	 */
	public static CarouselService defaultService() {
		return INSTANCE;
	}

    public CarouselData createCarouselDataWithMinProductLimit(String id, String name, List<ProductModel> items, FDUserI user, EnumEventSource eventSource, Variant variant) {
        String cmEventSource = eventSource != null ? eventSource.getName() : null;
        String variantId = variant != null ? variant.getId() : null;
        return createCarouselData(id, name, items, user, cmEventSource, variantId, FDStoreProperties.getMinimumItemsCountInCarousel());
    }

    public CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDUserI user, String cmEventSource, String variantId) {
        return createCarouselData(id, name, products, user, cmEventSource, variantId, 1, products.size());
    }

    public CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDUserI user, String cmEventSource, String variantId, int minProductLimit) {
        return createCarouselData(id, name, products, user, cmEventSource, variantId, minProductLimit, products.size());
    }

    /**
     * Creates carousel based on the given parameters.
     * 
     * @param id
     * @param name
     * @param products
     * @param user
     * @param cmEventSource
     * @param variantId
     * @param minProductLimit
     * @param maxProductLimit
     * @return
     */
    public CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDUserI user, String cmEventSource, String variantId, int minProductLimit,
            int maxProductLimit) {
        CarouselData carousel = null;
        List<ProductData> productDatas = createCarouselProductData(user, products, variantId, maxProductLimit);
        if (minProductLimit <= productDatas.size()) {
            carousel = new CarouselData();
            carousel.setId(id);
            carousel.setName(name);
            carousel.setCmEventSource(cmEventSource);
            carousel.setProducts(productDatas);
        }
        return carousel;
    }

    private List<ProductData> createCarouselProductData(FDUserI user, List<ProductModel> products, String variantId, int maxProductLimit) {
        List<ProductData> productDatas = new ArrayList<ProductData>();
        for (ProductModel product : products) {
                try {
                    ProductData productData = ProductDetailPopulator.createProductData(user, product);
                    productData = ProductDetailPopulator.populateBrowseRecommendation(user, productData, product);
                    productData.setVariantId(variantId);
                    productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, variantId));
                    productDatas.add(productData);
                } catch (Exception e) {
                    LOGGER.error("failed to create ProductData", e);
                }
            if (productDatas.size() == maxProductLimit) {
                break;
            }
        }
        return productDatas;
    }

    public CarouselData createNewProductsCarousel(FDUserI user, boolean isRandomizeProductOrderEnabled, CarouselNameCase carouselNameCase) {

        CarouselData carousel = null;

        List<ProductModel> products = collectNewProducts();
        if (products.size() >= FDStoreProperties.getMinimumItemsCountInCarousel()) {
            if (isRandomizeProductOrderEnabled) {
                Collections.shuffle(products);
            }

            String carouselName = (carouselNameCase == CarouselNameCase.UPPER) ? NEW_PRODUCTS_CAROUSEL_NAME.toUpperCase() : NEW_PRODUCTS_CAROUSEL_NAME;
            carousel = createCarouselDataWithMinProductLimit(null, carouselName, products, user, null, null);
        }

        return carousel;
    }

    public List<ProductModel> collectNewProducts() {
        CategoryModel newProductsCategory = ((CategoryModel) ContentFactory.getInstance()
                .getContentNodeByKey(ContentKeyFactory.get(FDStoreProperties.getNewProductsCarouselSourceCategoryContentKey())));

        List<ProductModel> filteredProducts = new ArrayList<ProductModel>();
        if (newProductsCategory != null) {
            filteredProducts = filterProducts(newProductsCategory.getAllChildProductsAsList());
        }

        return filteredProducts;
    }

    private List<ProductModel> filterProducts(List<ProductModel> products) {
        List<ProductModel> filteredProducts = new ArrayList<ProductModel>();
        for (ProductModel product : products) {
            if (!product.isUnavailable()) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

}
