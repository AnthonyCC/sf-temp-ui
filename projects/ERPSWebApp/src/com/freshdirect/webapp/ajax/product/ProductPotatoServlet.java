package com.freshdirect.webapp.ajax.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.expresscheckout.validation.data.ValidationResult;
import com.freshdirect.webapp.ajax.product.data.MultiProductPotatoRequestData;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;
import com.freshdirect.webapp.util.FDURLUtil;

public class ProductPotatoServlet extends BaseJsonServlet {

    private static final Logger LOGGER = LoggerFactory.getInstance(ProductPotatoServlet.class);
    private static final long serialVersionUID = 9198486249703339603L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        try {
            MultiProductPotatoRequestData reqData = parseRequestData(request, MultiProductPotatoRequestData.class, true);
            if (reqData != null) {
                Map<String, Object> result = new HashMap<String, Object>();
                ValidationResult validationResult = new ValidationResult();
                List<ProductPotatoData> productPotatos = ProductPotatoService.defaultService().getProductPotatos(user, reqData.getProductPotatos(), validationResult);
                result.put("products", productPotatos);
                if (!validationResult.getErrors().isEmpty()) {
                    result.put("errors", validationResult.getErrors());
                }
                writeResponseData(response, result);
            } else {
                String productId = request.getParameter("productId");
                if (productId == null) {
                    returnHttpError(400, "productId not specified");
                }

                String categoryId = request.getParameter("categoryId");
                if (categoryId == null) {
                    returnHttpError(400, "categoryId not specified");
                }

                ProductModel product = PopulatorUtil.getProduct(productId, categoryId);
                if (product == null) {
                    returnHttpError(400, "Product with id " + productId + " not found");
                }

                ProductPotatoData productPotatoData = new ProductPotatoData();

                ProductData productData = ProductDetailPopulator.createProductDataForCarousel(user, product, FDStoreProperties.getPreviewMode());
                String variantId = request.getParameter("variantId");
                if (variantId != null) {
                    productData.setVariantId(variantId);
                    productData.setProductPageUrl(FDURLUtil.getNewProductURI(product, variantId));
                }
                productPotatoData.setProductData(productData);
                LOGGER.info("Product " + productId + " produce normal potatos");

                String groupId = request.getParameter("groupId");
                String groupVersion = request.getParameter("groupVersion");
                productPotatoData.setProductExtraData(ProductExtraDataPopulator.createExtraData(user, product, groupId, groupVersion, true));
                LOGGER.info("Product " + productId + " produce extra potatos");
                writeResponseData(response, productPotatoData);
            }
        } catch (FDResourceException e) {
            LOGGER.error("Failed to get product potato.", e);
        } catch (HttpErrorResponse e) {
            LOGGER.error("Failed to get product potato.", e);
        } catch (FDSkuNotFoundException e) {
            LOGGER.error("Failed to get product potato.", e);
        }
    }

    @Override
    protected boolean synchronizeOnUser() {
        return false;
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

}
