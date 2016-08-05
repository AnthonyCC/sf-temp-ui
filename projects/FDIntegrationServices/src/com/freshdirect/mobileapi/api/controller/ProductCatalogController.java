package com.freshdirect.mobileapi.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.api.data.request.ProductCatalogMessageRequest;
import com.freshdirect.mobileapi.api.data.response.ProductCatalogMessageResponse;
import com.freshdirect.mobileapi.api.service.CartService;
import com.freshdirect.mobileapi.api.service.ConfigurationService;
import com.freshdirect.mobileapi.api.service.AccountService;
import com.freshdirect.mobileapi.api.service.ProductCatalogService;
import com.freshdirect.mobileapi.api.service.ProductService;
import com.freshdirect.mobileapi.catalog.model.CatalogKey;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.SessionUser;

@RestController
@RequestMapping(value = "/productcatalog")
public class ProductCatalogController {

    @Autowired
    private AccountService loginService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCatalogService productCatalogService;

    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ProductCatalogMessageResponse getProducts(HttpServletRequest request, HttpServletResponse response, ProductCatalogMessageRequest productRequest) throws FDException {
        ProductCatalogMessageResponse productResponse = new ProductCatalogMessageResponse();
        SessionUser user = loginService.checkLogin(request, response, productRequest.getSource());
        productResponse.setLogin(loginService.createLoginResponseMessage(user));
        productResponse.setCartDetail(cartService.getCartDetail(user));
        productResponse.setConfiguration(configurationService.getConfiguration(user.getFDSessionUser()));
        productResponse.setStatus(Message.STATUS_SUCCESS);
        CatalogKey catalogKey = productCatalogService.getCatalogKey(productRequest.getCatalogKey(), user);
        productResponse.setCatalogKey(catalogKey.toString());
        List<String> errorProductIds = new ArrayList<String>();
        productResponse.setProducts(productService.getProducts(productRequest.getProductIds(), Long.toString(catalogKey.getPlantId()),
                new PricingContext(catalogKey.getPricingZone()), errorProductIds));
        for (String errorProductId : errorProductIds) {
            productResponse.addErrorMessage(errorProductId);
        }
        return productResponse;
    }
}
