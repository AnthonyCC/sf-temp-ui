package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.response.recommender.CartRecommenderResponse;
import com.freshdirect.mobileapi.controller.data.response.recommender.ProductRecommendationData;
import com.freshdirect.mobileapi.controller.data.response.recommender.RecommendationTrackingData;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.MessageCodes;
import com.freshdirect.mobileapi.model.RecommenderTitleAndDescription;
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

        if (user == null) {
            responseMessage = new Message();
            responseMessage.setStatus(Message.STATUS_FAILED);
            responseMessage =  getErrorMessage(ERR_SESSION_EXPIRED, "Session does not exist in the server.");
        } else {
            final FDUserI customer = user.getFDSessionUser();
            if (ACTION_FK_CART.equals(action)) {
                responseMessage = processRecommendForCartRequest(request, customer);
            } else {
                final String error = String.format("Unrecognized action '%s'.", (action != null ? action : "<null>"));
                responseMessage = getErrorMessage(MessageCodes.ERR_SYSTEM, error);
            }
        }

        setResponseMessage(model, responseMessage, user);
        return model;
    }

    private Message processRecommendForCartRequest(HttpServletRequest request, FDUserI customer) {
        Message responseMessage;
        final boolean newCustomer = isUserAlreadyOrdered(customer);

        List<Recommendations> actualRecommendations = recommenderService.recommendForFoodKickCart(customer, newCustomer);
        if (actualRecommendations != null && !actualRecommendations.isEmpty()) {
            List<ProductRecommendationData> payload = new ArrayList<ProductRecommendationData>();
            for (Recommendations recommendations: actualRecommendations) {
                RecommenderTitleAndDescription displayableFields = recommenderService.getRecommenderDisplayableFields(recommendations.getVariant());
                ProductRecommendationData recommendationData = buildResponseDataFromRecommendations(recommendations, displayableFields, request, customer);
                payload.add(recommendationData);
            }
            responseMessage = new CartRecommenderResponse(payload);
        } else {
            responseMessage = new CartRecommenderResponse(null);
        }
        return responseMessage;
    }

    private ProductRecommendationData buildResponseDataFromRecommendations(Recommendations recommendations, RecommenderTitleAndDescription displayableFields, HttpServletRequest request, FDUserI customer) {
        ProductRecommendationData responsePayload = new ProductRecommendationData();

        final EnumSiteFeature siteFeature = recommendations.getVariant().getSiteFeature();
        responsePayload.setId(siteFeature.getName());
        responsePayload.setTitle(displayableFields.getTitle());
        responsePayload.setDealInfo(displayableFields.getDescription());
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

    private RecommendationTrackingData makeRecommendenderTrackingInfo(Recommendations recommendations, FDUserI customer) {
        RecommendationTrackingData data = new RecommendationTrackingData();

        data.setSiteFeature(recommendations.getVariant().getSiteFeature().getName() );
        data.setVariant(recommendations.getVariant().getId());
        data.setCohortName(customer.getCohortName());

        return data;
    }

    private boolean isUserAlreadyOrdered(FDUserI user) {
        boolean currentUser = false;
        try {
            currentUser = user.getLevel() > FDUserI.RECOGNIZED && user.getAdjustedValidOrderCount(EnumEStoreId.FD) >= 3;
        } catch (FDResourceException e) {
            currentUser = false;
        }
        return currentUser;
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
