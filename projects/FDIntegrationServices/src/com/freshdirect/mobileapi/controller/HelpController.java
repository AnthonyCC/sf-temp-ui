package com.freshdirect.mobileapi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.SafetyDetails;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.model.data.HelpTopic;
import com.freshdirect.mobileapi.model.data.HelpTopics;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ProductUtil;

public class HelpController extends BaseController {
    private static Category LOGGER = LoggerFactory.getInstance(HelpController.class);
    
    private static final String CONTACT_US_ACTION = "contactUs";
    private static final String LEARN_MORE_PROMO_ACTION = "learnMorePromo";
    private static final String TERMS_OF_USE_ACTION = "termsOfUse";
    private static final String DP_TERMS_AND_CONDITIONS_ACTION = "deliveryPassTermsAndConditions";
    private static final String FOOD_SAFETY_ACTION = "foodSafety";
    private static final String ABOUT_US_ACTION = "aboutUsFdx";
    private static final String SMS_TERMS_OF_USE_ACTION = "smstermsofuse";
    private static final String ALCOHOL_RESTRICTIONS_ACTION = "alcoholrestrictions";
    private static final String ALCOHOL_AGE_VERIFICATION_ACTION = "alcoholageverification";
    private static final String BACKUP_DELI_AUTHORIZATION_ACTION = "backupdeliveryauthorization";
    private static final String HELP_PATH = "/media/mobile/iphone/help/help.json";
    private static final String FDX_HELP_PATH = "/media/mobile/iphone/help/help_fdx.json";
    private static final String FDX_WEB_HELP_PATH = "/media/mobile/mobile_web/help/help_fdx.json";
    private static final String COSTUMER_SERVICE_PATH = "/media/mobile/iphone/contact_us/customer_service_hours.json";
    private static final String LEARN_MORE_PROMO_PATH = "/media/mobile/iphone/home/learn_more_promo.json";
    private static final String CUSTOMER_AGREEMENT_PATH = "/media/mobile/iphone/customer_agreement/customer_agreement.json";
    private static final String DP_AGREEMENT_PATH = "/media/editorial/site_pages/deliverypass/DP_terms.json";
    private static final String SMS_TERMS_OF_USE_PATH = "/media/mobile/iphone/sms_terms_of_use/sms_terms_of_use.json";
    private static final String ALCOHOL_RESTRICTIONS_PATH = "/media/mobile/iphone/alcohol_restrictions/alcohol_restrictions.json";
    private static final String ALCOHOL_AGE_VERIFICATION_PATH = "/media/mobile/iphone/alcohol_restrictions/alcoholAgeVerification.json";
    private static final String BACKUP_DELIVERY_AUTHORIZATION_PATH = "/media/mobile/iphone/backup_delivery_authorization/backupDeliveryAuthorization.json";
    private static final String PRODUCT_RECALLS_PATH = "/media/editorial/food_safety/food_safety_product_recalls.html";
    private static final String COOKING_STOREAGE_PATH = "/media/editorial/food_safety_fdx/food_safety_cookstore_fdx.html";
    private static final String HANDLING_FOOD_SAFETY_PATH = "/media/editorial/food_safety_fdx/food_safety_handling_fdx.html";
    private static final String ABOUT_US_PATH = "/media/editorial/about/overview_fdx/about_us_fdx.html";

    protected boolean validateUser() {
        return false;
    }

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
        String data = "";
        
        if (CONTACT_US_ACTION.equals(action)) {
            data = ProductUtil.readContent(COSTUMER_SERVICE_PATH);
        } else if (LEARN_MORE_PROMO_ACTION.equals(action)) {
            data = ProductUtil.readContent(LEARN_MORE_PROMO_PATH);
        } else if (TERMS_OF_USE_ACTION.equals(action)) {
            data = ProductUtil.readContent(CUSTOMER_AGREEMENT_PATH);
        } else if (SMS_TERMS_OF_USE_ACTION.equals(action)) {
            data = ProductUtil.readContent(SMS_TERMS_OF_USE_PATH);
        } else if (ALCOHOL_RESTRICTIONS_ACTION.equals(action)) {
            data = ProductUtil.readContent(ALCOHOL_RESTRICTIONS_PATH);
        } else if (ALCOHOL_AGE_VERIFICATION_ACTION.equals(action)) {
            data = ProductUtil.readContent(ALCOHOL_AGE_VERIFICATION_PATH);
        } else if (BACKUP_DELI_AUTHORIZATION_ACTION.equals(action)) {
            data = ProductUtil.readContent(BACKUP_DELIVERY_AUTHORIZATION_PATH);
        } else if (DP_TERMS_AND_CONDITIONS_ACTION.equalsIgnoreCase(action)) {
            data = ProductUtil.readContent(DP_AGREEMENT_PATH);
        } else if (FOOD_SAFETY_ACTION.equalsIgnoreCase(action)) {
            try {
                String recallContent = ProductUtil.readContent(PRODUCT_RECALLS_PATH);
                StringBuilder sb = new StringBuilder(recallContent);
                recallContent = sb.substring(sb.indexOf("<table") - 1, sb.indexOf("</table")) + "</table>";
                SafetyDetails prodRecall = createSafetyDetails("Product Recalls", "prodRecall", recallContent);
                SafetyDetails cookingStorage = createSafetyDetails("Cooking & Storage", "cookStorage", ProductUtil.readContent(COOKING_STOREAGE_PATH));
                SafetyDetails foodSafety = createSafetyDetails("Handling Food Safety", "foodSafety", ProductUtil.readContent(HANDLING_FOOD_SAFETY_PATH));
                HelpTopic foodSafetyTopic = createHelpTopic("Food Safety", "foodSafety", prodRecall, cookingStorage, foodSafety);
                data = getJsonString(createHelpTopics(foodSafetyTopic));
            } catch (Exception e) {
                LOGGER.warn("Unable to serialize data", e);
                data = "";
            }
        }else if (ABOUT_US_ACTION.equalsIgnoreCase(action)) {
            try {
                HelpTopic aboutUs = createHelpTopic("About Us", "aboutUs", ProductUtil.readContent(ABOUT_US_PATH));
                data = getJsonString(createHelpTopics(aboutUs));
            } catch (Exception e) {
                LOGGER.warn("Unable to serialize data", e);
                data = "";
            }
        } else {
            if (isCheckLoginStatusEnable(request)) {
                data = ProductUtil.readContent(FDX_WEB_HELP_PATH);
            } else {
                if (EnumEStoreId.FDX.equals(CmsManager.getInstance().getEStoreEnum())) {
                    data = ProductUtil.readContent(FDX_HELP_PATH);
                } else {
                    data = ProductUtil.readContent(HELP_PATH);
                }
            }
        }

        model.addObject("data", data);
        return model;
    }

    private HelpTopic createHelpTopic(String title, String path, Object... htmlDatas) {
        HelpTopic helpTopic = new HelpTopic();
        helpTopic.setTitle(title);
        helpTopic.setPath(path);
        for (Object htmlData : htmlDatas) {
            helpTopic.addEntries(htmlData);
        }
        return helpTopic;
    }

    private HelpTopics createHelpTopics(HelpTopic helpTopic) {
        HelpTopics helpTopics = new HelpTopics();
        helpTopics.addHelpTopics(helpTopic);
        return helpTopics;
    }

    private SafetyDetails createSafetyDetails(String title, String path, String detail) {
        SafetyDetails safetyDetails = new SafetyDetails();
        safetyDetails.setTitle(title);
        safetyDetails.setPath(path);
        safetyDetails.setDetail(detail);
        return safetyDetails;
    }
}
