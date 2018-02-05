package com.freshdirect.webapp.ajax.product;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationError;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoRequestData;

public class ProductPotatoService {

    private static final Logger LOGGER = LoggerFactory.getInstance(ProductPotatoService.class);
    private static final ProductPotatoService INSTANCE = new ProductPotatoService();

    private ProductPotatoService() {
    }

    public static ProductPotatoService defaultService() {
        return INSTANCE;
    }

    public List<ProductPotatoData> getProductPotatos(FDUserI user, List<ProductPotatoRequestData> potatoRequests, ValidationResult validationResult) {
        List<ProductPotatoData> productPotatos = new ArrayList<ProductPotatoData>();

        for (ProductPotatoRequestData potatoRequest : potatoRequests) {
            ProductPotatoData productPotato = getProductPotato(user, potatoRequest, validationResult);
            if (productPotato != null) {
                productPotatos.add(productPotato);
            }
        }
        return productPotatos;
    }

    public ProductPotatoData getProductPotato(FDUserI user, ProductPotatoRequestData data, ValidationResult validationResult) {
        return getProductPotato(user, data.getProductId(), data.getCategoryId(), data.getGroupId(), data.getGroupVersion(), data.isExtraField(), validationResult);
    }

    public ProductPotatoData getProductPotato(FDUserI user, String productId, String categoryId, String groupId, String groupVersion, boolean extraField,
            ValidationResult validationResult) {
        ProductModel product = PopulatorUtil.getProduct(productId, categoryId);
        ProductData productData = getProductPotato(user, product);
        if (productData != null) {
            ProductPotatoData productPotato = new ProductPotatoData();
            productPotato.setProductData(getProductPotato(user, product));
            if (extraField) {
                productPotato.setProductExtraData(getProductExtraPotato(user, product, groupId, groupVersion));
            }
            return productPotato;
        } else {
            validationResult.addError(new ValidationError(MessageFormat.format("Failed to load product by category id: {0}, product id {1}", categoryId, productId)));
        }
        return null;
    }

    public ProductData getProductPotato(FDUserI user, String productId, String categoryId) {
        ProductModel product = PopulatorUtil.getProduct(productId, categoryId);
        return getProductPotato(user, product);
    }

    public ProductData getProductPotato(FDUserI user, ProductModel product) {
        ProductData productData = null;
        try {
            productData = ProductDetailPopulator.createProductData(user, product, FDStoreProperties.getPreviewMode());
        } catch (FDRuntimeException e) {
            LOGGER.error("Failed to load product (runtime exc) " + product.getContentName());
        } catch (FDResourceException e) {
            LOGGER.error("Failed to load product " + product.getContentName());
        } catch (FDSkuNotFoundException e) {
            LOGGER.error("No SKU for product " + product.getContentName());
        } catch (HttpErrorResponse e) {
            LOGGER.error("Failed to load product " + product.getContentName());
        }
        return productData;
    }

    public ProductExtraData getProductExtraPotato(FDUserI user, String productId, String categoryId, String groupId, String groupVersion) {
        ProductModel product = PopulatorUtil.getProduct(productId, categoryId);
        return getProductExtraPotato(user, product, groupId, groupVersion);
    }

    public ProductExtraData getProductExtraPotato(FDUserI user, ProductModel product, String groupId, String groupVersion) {
        try {
            ProductExtraData extraData = null;
            if (FDStoreProperties.getPreviewMode() && PopulatorUtil.isProductIncomplete(product) && !PopulatorUtil.isNodeArchived(product)) {
                extraData = ProductExtraDataPopulator.createLightExtraData(user, product);
            } else {
                extraData = ProductExtraDataPopulator.createExtraData(user, product, groupId, groupVersion, true);
            }
            return extraData;
        } catch (FDRuntimeException e) {
            LOGGER.error("Failed to load product (runtime exc) " + product.getContentName());
        } catch (FDResourceException e) {
            LOGGER.error("Failed to load product " + product.getContentName());
        } catch (FDSkuNotFoundException e) {
            LOGGER.error("No SKU for product " + product.getContentName());
        } catch (HttpErrorResponse e) {
            LOGGER.error("Failed to load product " + product.getContentName());
        }
        return null;
    }

}
