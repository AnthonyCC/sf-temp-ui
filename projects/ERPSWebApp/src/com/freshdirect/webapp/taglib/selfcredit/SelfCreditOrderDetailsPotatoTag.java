package com.freshdirect.webapp.taglib.selfcredit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderDetailsData;
import com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderItemData;
import com.freshdirect.backoffice.selfcredit.service.SelfCreditOrderDetailsService;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class SelfCreditOrderDetailsPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(SelfCreditOrderDetailsPotatoTag.class);

    private String name = "orderDetails";
    private String orderId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public void doTag() throws JspException, IOException {
        
        PageContext ctx = (PageContext) getJspContext();
        HttpSession session = ctx.getSession();
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);

        SelfCreditOrderDetailsData orderDetails = new SelfCreditOrderDetailsData();

        try {
        	orderDetails = SelfCreditOrderDetailsService.defaultService().collectOrderDetails(user, orderId);
        } catch (FDResourceException e) {
            LOGGER.error("Failed to load details of order: " + orderId, e);
        } catch (FDSkuNotFoundException e) {
        	LOGGER.error("Failed to load details of order: " + orderId, e);
		} catch (HttpErrorResponse e) {
			LOGGER.error("Failed to load details of order: " + orderId, e);
		}

        Map<String, Object> potato = SoyTemplateEngine.convertToMap(orderDetails);
        ctx.setAttribute(name, potato);
    }
}
