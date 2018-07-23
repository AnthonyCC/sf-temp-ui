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
        String eventSourceName = eventSource != null ? eventSource.getName() : null;
        String variantId = variant != null ? variant.getId() : null;
        return createCarouselData(id, name, items, user, eventSourceName, variantId, FDStoreProperties.getMinimumItemsCountInCarousel());
    }

    public CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDUserI user, String eventSource, String variantId) {
        return createCarouselData(id, name, products, user, eventSource, variantId, 1);
    }

    /**
     * Creates carousel based on the given parameters.
     * 
     * @param id
     * @param name
     * @param products
     * @param user
     * @param eventSource
     * @param variantId
     * @param minProductLimit
     * @return
     */
    public CarouselData createCarouselData(String id, String name, List<ProductModel> products, FDUserI user, String eventSource, String variantId, int minProductLimit) {
        CarouselData carousel = null;
        List<ProductData> productDatas = createCarouselProductData(user, products, variantId);
        if (minProductLimit <= productDatas.size()) {
            carousel = new CarouselData();
            carousel.setId(id);
            carousel.setName(name);
            carousel.setEventSource(eventSource);
            carousel.setProducts(productDatas);
        }
        return carousel;
    }

    private List<ProductData> createCarouselProductData(FDUserI user, List<ProductModel> products, String variantId) {
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
        }
        return productDatas;
    }

    public List<ProductModel> collectNewProducts(boolean isRandomizeProductOrderEnabled) {
        CategoryModel newProductsCategory = ((CategoryModel) ContentFactory.getInstance()
                .getContentNodeByKey(ContentKeyFactory.get(FDStoreProperties.getNewProductsCarouselSourceCategoryContentKey())));

        List<ProductModel> filteredProducts = new ArrayList<ProductModel>();
        if (newProductsCategory != null) {
            filteredProducts = filterAvailableProducts(newProductsCategory.getAllChildProductsAsList());
        }

        int maximumItemsCountInCarousel = FDStoreProperties.getMaximumItemsCountInNewProductCarousel();
        if (filteredProducts.size() > maximumItemsCountInCarousel) {
            filteredProducts = filteredProducts.subList(0, maximumItemsCountInCarousel);
        }

        if (isRandomizeProductOrderEnabled) {
            Collections.shuffle(filteredProducts);
        }

        return filteredProducts;
    }

    private List<ProductModel> filterAvailableProducts(List<ProductModel> products) {
        List<ProductModel> filteredProducts = new ArrayList<ProductModel>();
        for (ProductModel product : products) {
            if (!product.isUnavailable()) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

}
