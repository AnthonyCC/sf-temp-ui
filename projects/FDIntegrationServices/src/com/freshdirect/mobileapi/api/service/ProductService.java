package com.freshdirect.mobileapi.api.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.Product;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.util.BrowseUtil;

@Component
public class ProductService {

    private static Category LOGGER = LoggerFactory.getInstance(ProductService.class);

    public List<com.freshdirect.mobileapi.controller.data.Product> getProducts(SessionUser user, List<String> productKeys, List<String> errors) {
        List<com.freshdirect.mobileapi.controller.data.Product> products = new ArrayList<com.freshdirect.mobileapi.controller.data.Product>();
        if (productKeys != null) {
            for (String productKey : productKeys) {
                try {
                    com.freshdirect.mobileapi.model.Product product = com.freshdirect.mobileapi.model.Product.getProduct(ContentKey.decode(productKey).getId(), null, null, user);
                    if (product != null) {
                        products.add(new Product(product));
                    }
                } catch (Exception e) {
                    errors.add(productKey);
                    LOGGER.error("Could not get product model.", e);
                }
            }
        }
        return products;
    }

    public List<com.freshdirect.mobileapi.catalog.model.Product> getProducts(List<String> productIds, String plantId, PricingContext pricingContext, List<String> errors) {
        List<com.freshdirect.mobileapi.catalog.model.Product> products = new ArrayList<com.freshdirect.mobileapi.catalog.model.Product>();
        if (productIds != null) {
            for (String productId : productIds) {
                try {
                    ProductModel productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.decode("Product:" + productId));
                    if (productModel != null) {
                        com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder productBuilder = new com.freshdirect.mobileapi.catalog.model.Product.ProductBuilder(
                                productModel.getContentName(), productModel.getFullName());
                        productBuilder.brandTags(productModel.getBrands()).minQty(productModel.getQuantityMinimum()).maxQty(productModel.getQuantityMaximum())
                                .incrementQty(productModel.getQuantityIncrement()).quantityText(productModel.getQuantityText()).images(BrowseUtil.getImages(productModel))
                                .tags(productModel.getTags()).generateWineAttributes(productModel).addKeyWords(productModel.getKeywords())
                                .generateAdditionTagsFromProduct(productModel).skuInfo(BrowseUtil.getSkuInfo(productModel, plantId, pricingContext))
                                .productLayout(productModel.getProductLayout().getId());
                        products.add(productBuilder.build());
                    } else {
                        errors.add(productId);
                    }
                } catch (Exception e) {
                    errors.add(productId);
                    LOGGER.error("Could not get product model.", e);
                }
            }
        }
        return products;
    }
}
