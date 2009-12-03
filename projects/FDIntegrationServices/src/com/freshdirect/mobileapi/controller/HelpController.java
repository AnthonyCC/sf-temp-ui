package com.freshdirect.mobileapi.controller;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.core.io.Resource;
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

    public static final String HELP_ACTION = "help";

    public static final String CONTACT_US_ACTION = "contactUs";

    public static final String LEARN_MORE_PROMO_ACTION = "learnMorePromo";

    private String costumerServicePath = "/iphone/contact_us/customer_service_hours.json";

    private String learnMorePromoPath = "/iphone/home/learn_more_promo.json";

    private String helpPath = "/iphone/help/help.json";

    private Resource contactUsFile;

    private Resource helpFile;

    private Resource learnMorePromoFile;

    @Override
    protected ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model, String action,
            SessionUser user) throws JsonException, FDException, ServiceException, NoSessionException {
        String data = "";
        URL remoteUrl = null;
        try {
            String mediaPath = FDStoreProperties.getMediaPath();
            if (CONTACT_US_ACTION.equals(action)) {
                try {
                    remoteUrl = new URL(mediaPath + costumerServicePath);
                    data = ProductUtil.readContent(remoteUrl);
                } catch (IOException e) {
                    LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                    data = ProductUtil.readContent(contactUsFile.getURL());
                }
            } else if (LEARN_MORE_PROMO_ACTION.equals(action)) {
                try {
                    remoteUrl = new URL(mediaPath + learnMorePromoPath);
                    data = ProductUtil.readContent(remoteUrl);
                } catch (IOException e) {
                    LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                    data = ProductUtil.readContent(learnMorePromoFile.getURL());
                }
            } else {
                try {
                    remoteUrl = new URL(mediaPath + helpPath);
                    data = ProductUtil.readContent(remoteUrl);
                } catch (IOException e) {
                    LOGGER.warn("Unable to rerieve data from " + remoteUrl.toString());
                    data = ProductUtil.readContent(helpFile.getURL());
                }
            }
        } catch (JsonParseException e) {
            throw new JsonException(e);
        } catch (JsonMappingException e) {
            throw new JsonException(e);
        } catch (IOException e) {
            throw new JsonException(e);
        }
        model.addObject("data", data);

        return model;
    }

    public Resource getContactUsFile() {
        return contactUsFile;
    }

    public void setContactUsFile(Resource contactUsFile) {
        this.contactUsFile = contactUsFile;
    }

    public Resource getHelpFile() {
        return helpFile;
    }

    public void setHelpFile(Resource helpFile) {
        this.helpFile = helpFile;
    }

    public Resource getLearnMorePromoFile() {
        return learnMorePromoFile;
    }

    public void setLearnMorePromoFile(Resource learnMorePromoFile) {
        this.learnMorePromoFile = learnMorePromoFile;
    }

    public String getCostumerServicePath() {
        return costumerServicePath;
    }

    public void setCostumerServicePath(String costumerServicePath) {
        this.costumerServicePath = costumerServicePath;
    }

    public String getLearnMorePromoPath() {
        return learnMorePromoPath;
    }

    public void setLearnMorePromoPath(String learnMorePromoPath) {
        this.learnMorePromoPath = learnMorePromoPath;
    }

    public String getHelpPath() {
        return helpPath;
    }

    public void setHelpPath(String helpPath) {
        this.helpPath = helpPath;
    }

}
