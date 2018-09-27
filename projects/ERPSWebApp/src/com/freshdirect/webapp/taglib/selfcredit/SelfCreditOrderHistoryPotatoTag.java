package com.freshdirect.webapp.taglib.selfcredit;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderHistoryData;
import com.freshdirect.backoffice.selfcredit.service.SelfCreditOrderHistoryService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SelfCreditOrderHistoryPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(SelfCreditOrderHistoryPotatoTag.class);

    private String name = "orderHistory";

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

        SelfCreditOrderHistoryData orders = new SelfCreditOrderHistoryData();
        try {
            orders = SelfCreditOrderHistoryService.defaultService().collectSelfCreditOrders(user);
        } catch (FDResourceException e) {
            LOGGER.error("Failed to load orders for self-credit process.", e);
        }

        Map<String, Object> potato = SoyTemplateEngine.convertToMap(orders);
        ctx.setAttribute(name, potato);
    }


}
