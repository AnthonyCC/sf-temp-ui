package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
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

    protected boolean validateUser() {
        return false;
    }
    
    private static final String PAYLOAD_EXTN = ".json";
    
    public static final String HELP_ACTION = "help";
    public static final String CONTACT_US_ACTION = "contactUs";
    public static final String LEARN_MORE_PROMO_ACTION = "learnMorePromo";
    public static final String TERMS_OF_USE_ACTION = "termsOfUse";
    public static final String DP_TERMS_AND_CONDITIONS_ACTION = "deliveryPassTermsAndConditions";
    public static final String FOOD_SAFETY_ACTION = "foodSafety";
    public static final String ABOUTUS_ACTION = "aboutUsFdx";
    
    private String helpPath = "/media/mobile/iphone/help/help";
    private String costumerServicePath = "/media/mobile/iphone/contact_us/customer_service_hours.json";
    private String learnMorePromoPath = "/media/mobile/iphone/home/learn_more_promo.json";
    private String customerAgreementPath = "/media/mobile/iphone/customer_agreement/customer_agreement.json";
    private String dpAgreementPath = "/media/editorial/site_pages/deliverypass/DP_terms.json";
    
    
    public static final String SMS_TERMS_OF_USE = "smstermsofuse";
    public static final String ALCOHOL_REST = "alcoholrestrictions";
    public static final String ALCOHOL_AGE_VERI = "alcoholageverification";
    public static final String BACKUP_DELI_AUTH = "backupdeliveryauthorization";

    private String smsTermsOfUsePath = "/media/mobile/iphone/sms_terms_of_use/sms_terms_of_use.json";
    private String alcoholRestrictions = "/media/mobile/iphone/alcohol_restrictions/alcohol_restrictions.json";
    private String alcoholAgeVerification = "/media/mobile/iphone/alcohol_restrictions/alcoholAgeVerification.json";
    private String backupdeliveryauthorization = "/media/mobile/iphone/backup_delivery_authorization/backupDeliveryAuthorization.json";
          
    private String productRecallsPath = "/media/editorial/food_safety/food_safety_product_recalls.html";
    private String cookingStoragePath = "/media/editorial/food_safety_fdx/food_safety_cookstore_fdx.html";
    private String handlingFoodSafetyPath = "/media/editorial/food_safety_fdx/food_safety_handling_fdx.html";
    private String aboutUsPath = "/media/editorial/about/overview_fdx/about_us_fdx.html";

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
        String data = "";
        URL remoteUrl = null;
        Html html = null;
        String mediaPath = FDStoreProperties.getMediaPath();
        
        String storeKey = ContentFactory.getInstance().getCurrentUserContext() != null 
        									&& ContentFactory.getInstance().getCurrentUserContext().getStoreContext() != null
        										&& ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId() != null
        											&& !EnumEStoreId.FD.equals(ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId()) 
        													? ContentFactory.getInstance().getCurrentUserContext().getStoreContext().getEStoreId().getContentId().toLowerCase() : null;
        
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
        }else if (FOOD_SAFETY_ACTION.equalsIgnoreCase(action)) {
            try {
            	HelpTopics helpTopics = new HelpTopics();
            	HelpTopic foodSafety = new HelpTopic();
            	SafetyDetails sfd = null;
            	 foodSafety.setTitle("Food Safety");
                 foodSafety.setPath("foodSafety");
             	
                 sfd = renderHTMLContent( new Html (mediaPath + productRecallsPath), "prodRecall");
                 if(sfd!=null)
                 foodSafety.setEntries(sfd);
                 
                 sfd = renderHTMLContent( new Html (mediaPath + cookingStoragePath), "storage");
                 if(sfd!=null)
                 foodSafety.setEntries(sfd);
                 
                 sfd = renderHTMLContent( new Html (mediaPath + handlingFoodSafetyPath), "foodSafety");
                 if(sfd!=null)
                 foodSafety.setEntries(sfd);
                 
                 helpTopics.setHelpTopics(foodSafety);
                 data =getJsonString(helpTopics);
                 
            } catch (Exception e) {
                LOGGER.warn("Unable to rerieve data from ");
                data = "";
            }
        }else if (ABOUTUS_ACTION.equalsIgnoreCase(action)) {
            try {
            	HelpTopics helpTopics = new HelpTopics();
            	HelpTopic aboutUs = new HelpTopic();
            	
            	
             	html = new Html (mediaPath + aboutUsPath);
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
            	if(storeKey != null) {
            		remoteUrl = new URL(mediaPath + helpPath + "_" + storeKey + PAYLOAD_EXTN);
            	} else {
            		remoteUrl = new URL(mediaPath + helpPath + PAYLOAD_EXTN);
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
