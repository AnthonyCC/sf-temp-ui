package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ProductUtil;

public class HelpController extends BaseController {
    private static Category LOGGER = LoggerFactory.getInstance(HelpController.class);

    protected boolean validateUser() {
        return false;
    }
    
    public static final String HELP_ACTION = "help";

    public static final String CONTACT_US_ACTION = "contactUs";
    public static final String TERMS_OF_USE_ACTION = "termsOfUse";

    public static final String LEARN_MORE_PROMO_ACTION = "learnMorePromo";
    public static final String SMS_TERMS_OF_USE = "smstermsofuse";
    public static final String ALCOHOL_REST = "alcoholrestrictions";
    public static final String ALCOHOL_AGE_VERI = "alcoholageverification";
    public static final String BACKUP_DELI_AUTH = "backupdeliveryauthorization";

    private String costumerServicePath = "/media/mobile/iphone/contact_us/customer_service_hours.json";

    private String learnMorePromoPath = "/media/mobile/iphone/home/learn_more_promo.json";

    private String helpPath = "/media/mobile/iphone/help/help.json";
    
    private String customerAgreementPath = "/media/mobile/iphone/customer_agreement/customer_agreement.json";
        
    private String smsTermsOfUsePath = "/media/mobile/iphone/sms_terms_of_use/sms_terms_of_use.json";
    
    private String alcoholRestrictions = "/media/mobile/iphone/alcohol_restrictions/alcohol_restrictions.json";
    
    private String alcoholAgeVerification = "/media/mobile/iphone/alcohol_restrictions/alcoholAgeVerification.json";
    
    private String backupdeliveryauthorization = "/media/mobile/iphone/backup_delivery_authorization/backupDeliveryAuthorization.json";
    
    private String dpAgreementPath = "/media/editorial/site_pages/deliverypass/DP_terms.json";
    
    public static final String DP_TERMS_AND_CONDITIONS_ACTION = "deliveryPassTermsAndConditions";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
        String data = "";
        URL remoteUrl = null;
        String mediaPath = FDStoreProperties.getMediaPath();
        if (CONTACT_US_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + costumerServicePath);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        } else if (LEARN_MORE_PROMO_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + learnMorePromoPath);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        } else if (TERMS_OF_USE_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + customerAgreementPath);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        } else if (SMS_TERMS_OF_USE.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + smsTermsOfUsePath);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (ALCOHOL_REST.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + alcoholRestrictions);                
                data = ProductUtil.readContent(remoteUrl);                
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (ALCOHOL_AGE_VERI.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + alcoholAgeVerification);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (BACKUP_DELI_AUTH.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + backupdeliveryauthorization);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (DP_TERMS_AND_CONDITIONS_ACTION.equalsIgnoreCase(action)) {
            try {
                remoteUrl = new URL(mediaPath + dpAgreementPath);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        } else {
            try {
                remoteUrl = new URL(mediaPath + helpPath);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        }
        model.addObject("data", data);

        return model;
    }
}
