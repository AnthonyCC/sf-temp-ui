package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.recommender.CartRecommenderResponse;
import com.freshdirect.mobileapi.controller.data.response.recommender.ProductRecommendationData;
import com.freshdirect.mobileapi.controller.data.response.recommender.TrackingData;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ProductRecommenderService;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ProductPotatoUtil;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;

public class ProductRecommenderController extends BaseController {

    private static final String ACTION_FK_CART="cart";

    private ProductRecommenderService recommenderService;

    public void setRecommenderService(ProductRecommenderService recommenderService) {
        this.recommenderService = recommenderService;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action, SessionUser user)
            throws JsonException, FDException, ServiceException, NoSessionException, IOException, TemplateException {

        Message responseMessage = null;

        if (ACTION_FK_CART.equals(action)) {
            FDUserI customer = user.getFDSessionUser();

            Recommendations recommendations = recommenderService.recommendDYFForFoodKickCart(customer);
            if (recommendations != null) {
                List<ProductRecommendationData> listOfRecommendations = new ArrayList<ProductRecommendationData>();

                ProductRecommendationData dyfRecommendation = buildResponseDataFromRecommendations(recommendations, "Custom Title", "Custom Deal Info", request, customer);
                listOfRecommendations.add(dyfRecommendation);

                responseMessage = new CartRecommenderResponse(listOfRecommendations);
            } else {
                responseMessage = getErrorMessage(MessageCodes.ERR_SYSTEM, "FK Cart Recommender failed.");
            }
        } else {
            final String error = String.format("Unrecognized action '%s'.", (action != null ? action : "<null>"));
            responseMessage = getErrorMessage(MessageCodes.ERR_SYSTEM, error);
        }

        setResponseMessage(model, responseMessage, user);

        return model;
    }

    private ProductRecommendationData buildResponseDataFromRecommendations(Recommendations recommendations, String title, String dealInfo, HttpServletRequest request, FDUserI customer) {
        ProductRecommendationData responsePayload = new ProductRecommendationData();

        responsePayload.setId(recommendations.getVariant().getSiteFeature().getName());
        responsePayload.setTitle(title);
        responsePayload.setDealInfo(dealInfo);
        populateRecommenderResponseWithProducts(responsePayload, recommendations, customer, isExtraResponseRequested(request));

        // append recommendation tracking data for analytics
        responsePayload.setTracking( makeRecommendenderTrackingInfo(recommendations, customer) );

        return responsePayload;
    }

    private void populateRecommenderResponseWithProducts(ProductRecommendationData payload, Recommendations source, FDUserI customer, boolean isFKWebTarget) {
        if (isFKWebTarget) {
            // deliver JSON for FK Web
            List<ProductPotatoData> potatoes = new ArrayList<ProductPotatoData>();
            for (ProductModel product : source.getAllProducts()) {
                potatoes.add(ProductPotatoUtil.getProductPotato(product, customer, false, FDStoreProperties.getPreviewMode()));
            }
            payload.setProducts(potatoes);
        } else {
            // deliver JSON for FK mobile devices
            List<String> productKeys = new ArrayList<String>();
            for (ProductModel product : source.getAllProducts()) {
                productKeys.add(product.getContentKey().getEncoded());
            }
            payload.setProductIds(productKeys);
        }
    }

    private TrackingData makeRecommendenderTrackingInfo(Recommendations recommendations, FDUserI customer) {
        TrackingData data = new TrackingData();

        data.setSiteFeature(recommendations.getVariant().getSiteFeature().getName() );
        data.setVariant(recommendations.getVariant().getId());
        data.setCohortName(customer.getCohortName());

        return data;
    }

    /**
     * Grant guests to use controller actions
     *
     * Currently, recommended contents can be reached by guests
     * TODO: revisit when new recommenders are added
     */
    @Override
    protected boolean validateUser() {
        return false;
    }
}
