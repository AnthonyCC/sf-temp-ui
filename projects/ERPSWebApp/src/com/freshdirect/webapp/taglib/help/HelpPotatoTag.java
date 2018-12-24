package com.freshdirect.webapp.taglib.help;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.helppage.service.HelpService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class HelpPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(HelpPotatoTag.class);

    private String name = "helpPotato";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext ctx = (PageContext) getJspContext();
        HttpSession session = ctx.getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        Map<String, Object> dataMap = HelpService.defaultService().collectHelpPageData(user);
        ctx.setAttribute(name, dataMap);
    }
}
