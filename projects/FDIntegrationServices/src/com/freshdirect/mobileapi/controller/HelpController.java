package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.cms.multistore.MultiStoreProperties;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.Html;
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
        URL remoteUrl = null;
        Html html = null;
        String mediaPath = FDStoreProperties.getMediaPath();
        
        if (CONTACT_US_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + COSTUMER_SERVICE_PATH);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        } else if (LEARN_MORE_PROMO_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + LEARN_MORE_PROMO_PATH);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        } else if (TERMS_OF_USE_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + CUSTOMER_AGREEMENT_PATH);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        } else if (SMS_TERMS_OF_USE_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + SMS_TERMS_OF_USE_PATH);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (ALCOHOL_RESTRICTIONS_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + ALCOHOL_RESTRICTIONS_PATH);                
                data = ProductUtil.readContent(remoteUrl);                
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (ALCOHOL_AGE_VERIFICATION_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + ALCOHOL_AGE_VERIFICATION_PATH);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (BACKUP_DELI_AUTHORIZATION_ACTION.equals(action)) {
            try {
                remoteUrl = new URL(mediaPath + BACKUP_DELIVERY_AUTHORIZATION_PATH);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
            
        } else if (DP_TERMS_AND_CONDITIONS_ACTION.equalsIgnoreCase(action)) {
            try {
                remoteUrl = new URL(mediaPath + DP_AGREEMENT_PATH);
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        }else if (FOOD_SAFETY_ACTION.equalsIgnoreCase(action)) {
            try {
            	HelpTopics helpTopics = new HelpTopics();
            	HelpTopic foodSafety = new HelpTopic();
            	SafetyDetails sfd = null;
            	 foodSafety.setTitle("Food Safety");
                 foodSafety.setPath("foodSafety");
             	
                 sfd = renderHTMLContent( new Html (mediaPath + PRODUCT_RECALLS_PATH), "prodRecall");
                 if(sfd!=null)
                 foodSafety.setEntries(sfd);
                 
                 sfd = renderHTMLContent( new Html (mediaPath + COOKING_STOREAGE_PATH), "storage");
                 if(sfd!=null)
                 foodSafety.setEntries(sfd);
                 
                 sfd = renderHTMLContent( new Html (mediaPath + HANDLING_FOOD_SAFETY_PATH), "foodSafety");
                 if(sfd!=null)
                 foodSafety.setEntries(sfd);
                 
                 helpTopics.setHelpTopics(foodSafety);
                 data =getJsonString(helpTopics);
                 
            } catch (Exception e) {
                LOGGER.warn("Unable to rerieve data from ");
                data = "";
            }
        }else if (ABOUT_US_ACTION.equalsIgnoreCase(action)) {
            try {
            	HelpTopics helpTopics = new HelpTopics();
            	HelpTopic aboutUs = new HelpTopic();
            	
            	
             	html = new Html (mediaPath + ABOUT_US_PATH);
                String  htmlData =  ProductUtil.readHtml(html);
                
                aboutUs.setTitle("About Us");
            	aboutUs.setPath("aboutUs");
                aboutUs.setEntries(htmlData);

                 helpTopics.setHelpTopics(aboutUs);
                 data =getJsonString(helpTopics);
                 
            } catch (Exception e) {
                LOGGER.warn("Unable to rerieve data from " + html.toString());
                data = "";
            }
        } else {
            try {
                if (isCheckLoginStatusEnable(request)) {
                    remoteUrl = new URL(mediaPath + FDX_WEB_HELP_PATH);
                } else {
                    if (EnumEStoreId.FDX.getContentId().equals(MultiStoreProperties.getCmsStoreId())) {
                        remoteUrl = new URL(mediaPath + FDX_HELP_PATH);
                    } else {
                        remoteUrl = new URL(mediaPath + HELP_PATH);
                    }
                }
                data = ProductUtil.readContent(remoteUrl);
            } catch (IOException e) {
                LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                data = "";
            }
        }
        model.addObject("data", data);

        return model;
    }

    private SafetyDetails renderHTMLContent(Html html, String saftyType){
    	SafetyDetails safetyDetails = null;
    	try {
    	String  data =  ProductUtil.readHtml(html);
    	
    	if(saftyType.equalsIgnoreCase("prodRecall")){
	        StringBuilder sb=new StringBuilder(data);
	        data = sb.substring(sb.indexOf("<table")-1, sb.indexOf("</table"));
	        data = data+"</table>";
	        safetyDetails = new SafetyDetails();
	        safetyDetails.setTitle("Product Recalls");
	        safetyDetails.setPath("prodRecall");
	        safetyDetails.setDetail(data);
	    	}
    	else if(saftyType.equalsIgnoreCase("storage")){
	        safetyDetails = new SafetyDetails();
	        safetyDetails.setTitle("Cooking & Storage");
	        safetyDetails.setPath("cookStorage");
	        safetyDetails.setDetail(data);
	    	}
    	else if(saftyType.equalsIgnoreCase("foodSafety")){
	        safetyDetails = new SafetyDetails();
	        safetyDetails.setTitle("Handling Food Safety");
	        safetyDetails.setPath("foodSafety");
	        safetyDetails.setDetail(data);
	    	}
    	 } catch (Exception e) {
             LOGGER.warn("Unable to rerieve data from " + html.toString());
             
         }
    	return safetyDetails;
    }
    
}
