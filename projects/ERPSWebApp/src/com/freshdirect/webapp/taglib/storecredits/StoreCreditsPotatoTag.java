package com.freshdirect.webapp.taglib.storecredits;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.storecredits.data.StoreCredits;
import com.freshdirect.webapp.ajax.storecredits.service.StoreCreditsService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class StoreCreditsPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(StoreCreditsPotatoTag.class);

    private String name = "storeCredits";
    private boolean featureActive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFeatureActive() {
        return featureActive;
    }

    public void setFeatureActive(boolean featureActive) {
        this.featureActive = featureActive;
    }

    @Override
    public void doTag() throws JspException, IOException {

        PageContext ctx = (PageContext) getJspContext();
        HttpSession session = ctx.getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

        StoreCredits storeCredits = new StoreCredits();
        try {
            storeCredits = StoreCreditsService.defaultService().collectStoreCredits(user, isFeatureActive());
        } catch (FDResourceException e) {
            LOGGER.error("Failed to load Store Credits", e);
        }

        Map<String, Object> potato = SoyTemplateEngine.convertToMap(storeCredits);
        ctx.setAttribute(name, potato);
    }
}
