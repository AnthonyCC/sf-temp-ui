package com.freshdirect.webapp.ajax.modulehandling;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.coremetrics.util.CoremetricsUtil;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.filtering.InvalidFilteringArgumentException;
import com.freshdirect.webapp.ajax.modulehandling.data.ModuleContainerData;
import com.freshdirect.webapp.ajax.modulehandling.service.ModuleHandlingService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ModuleHandlingPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(ModuleHandlingPotatoTag.class);

    private String name;
    private String moduleContainerId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleContainerId() {
        return moduleContainerId;
    }

    public void setModuleContainerId(String moduleContainerId) {
        this.moduleContainerId = moduleContainerId;
    }

    @Override
    public void doTag() throws JspException {
        LOGGER.info("Creating data potato: " + name);

        PageContext ctx = (PageContext) getJspContext();
        HttpSession session = ctx.getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

        if (moduleContainerId == null || moduleContainerId == "") {
            LOGGER.info("ModuleContainerId was empty, loading default moduleContainer based on user");
            moduleContainerId = CoremetricsUtil.defaultService().getCustomerTypeByOrderCount(user);
        }

        LOGGER.info("Loading module container: " + moduleContainerId + " for user: " + user.getUserId() + " with cookie: " + user.getCookie());

        try {
            ModuleContainerData result = ModuleHandlingService.getDefaultService().loadModuleContainer(moduleContainerId, user, session);
            ctx.setAttribute("moduleContainerId", moduleContainerId);
            ctx.setAttribute(name, SoyTemplateEngine.convertToMap(result));
        } catch (FDResourceException e) {
            throw new JspException(e);
        } catch (InvalidFilteringArgumentException e) {
            throw new JspException(e);
        }

    }

}
