package com.freshdirect.mobileapi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.PopulatorUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;
import com.freshdirect.mobileapi.controller.data.response.CartDetail;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.AffiliateCartDetail;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.CartLineItem;
import com.freshdirect.mobileapi.controller.data.response.CartDetail.ProductLineItem;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.ProductExtraDataPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductExtraData;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;



/**
 * A simple tool that makes product potatoes available for mobile API.
 * Currently, product potatoes are served only for foodkick mobile web clients. 
 * 
 * @author segabor
 *
 */
public class ProductPotatoUtil {
    private static final Logger LOGGER = LoggerFactory.getInstance(ProductPotatoUtil.class);
    
    private ProductPotatoUtil() {}

    
    /**
     * Fetch product from CMS catalog and turns it into a JSONifiable flat product potato
     * 
     * @param product Existing product model
     * @param servletContext Servlet context required for building product potatoes
     * @param user
     * @param requiresExtraFields Also populates extra fields
     * 
     * @see ProductPotatoData
     * @see ProductDetailPopulator
     * @see ProductExtraDataPopulator
     * 
     * @return potato populated or null if either product is not found or making a potato failed
     */
    public static ProductPotatoData getProductPotato(final ProductModel product, final FDUserI user, final boolean requiresExtraFields, boolean enableProductIncomplete) {
        if (product != null) {
            final String productId = product.getContentKey().getId();

            try {
                final ProductPotatoData data = new ProductPotatoData();

                data.setProductData( ProductDetailPopulator.createProductData(user, product, enableProductIncomplete) );
                if (requiresExtraFields) {
                    ProductExtraData extraData = null;
                    if (enableProductIncomplete && PopulatorUtil.isProductIncomplete(product) && !PopulatorUtil.isNodeArchived(product)) {
                        extraData = ProductExtraDataPopulator.createLightExtraData(user, product);
                    } else {
                        extraData = ProductExtraDataPopulator.createExtraData(user, product, null, null, false);
                    }
                    data.setProductExtraData( extraData);
                }
                return data;
            } catch (FDRuntimeException e) {
                LOGGER.error("Failed to load product (runtime exc) " + productId);
            } catch (FDResourceException e) {
                LOGGER.error("Failed to load product " + productId);
            } catch (FDSkuNotFoundException e) {
                LOGGER.error("No SKU for product " + productId);
            } catch (HttpErrorResponse e) {
                LOGGER.error("Failed to load product " + productId);
            }
        }
        return null;
    }



    /**
     * Convenience method
     * 
     * @param productId CMS ID of product
     * @param categoryId CMS ID of home category (optional)
     * @param servletContext Servlet context required for building product potatoes
     * @param user
     * @param requiresExtraFields Also populates extra fields
     * @return
     */
    public static ProductPotatoData getProductPotato(final String productId, final String categoryId, final FDUserI user, final boolean requiresExtraFields) {
        return getProductPotato(productId, categoryId, user, requiresExtraFields, !FDStoreProperties.getPreviewMode());
    }

    public static ProductPotatoData getProductPotato(final String productId, final String categoryId, final FDUserI user, final boolean requiresExtraFields, boolean enableProductIncomplete) {
        final ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
        if (product != null) {
            return getProductPotato(product, user, requiresExtraFields, enableProductIncomplete);
        } else {
            LOGGER.error("Product " + productId + " not found in CMS database");
        }
        return null;
    }



    /**
     * Finds and collects {@link ProductConfiguration} items by walking input collection recursively
     * 
     * @param items source, cartline items
     * @param result output, collected product line items
     */
    public static void collectProductConfigurations(Collection<CartLineItem> items, Collection<ProductConfiguration> result) {
        for (final CartLineItem lineItem : items) {
            
            if (lineItem instanceof CartDetail.Group) {
                collectProductConfigurations( ((CartDetail.Group) lineItem).getLineItems(), result);
            } else if (lineItem instanceof CartDetail.ProductLineItem) {
                result.add( ((ProductLineItem) lineItem).getProductConfiguration() );
            }
        }

    }



    private static void populateProductConfiguration(final FDUserI user, final ProductConfiguration productConfiguration) {
        final String productId = productConfiguration.getProduct().getId();
        final String categoryId = productConfiguration.getProduct().getCategoryId();

        ProductPotatoData potato =  getProductPotato( productId, categoryId, user, false );

        productConfiguration.setProductPotato(potato);
    }



    /**
     * Populate cart detail object with product potatoes
     * 
     * @param user
     * @param cartDetail
     * @param servletContext
     */
    public static void populateCartDetailWithPotatoes(final FDUserI user, final CartDetail cartDetail) {
        List<ProductConfiguration> configurations = new ArrayList<ProductConfiguration>();
        
        for (final AffiliateCartDetail affiliate : cartDetail.getAffiliates()) {
            collectProductConfigurations(affiliate.getLineItems(), configurations);
        }
        
        for (final ProductConfiguration prodConf : configurations) {
            populateProductConfiguration(user, prodConf);
        }
    }
}
