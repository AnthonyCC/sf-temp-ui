package com.freshdirect.webapp.taglib.contactus;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.customer.ErpCustomerInfoModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.helppage.data.ContactUsCustomerData;
import com.freshdirect.webapp.helppage.service.ContactUsService;
import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ContactUsPotatoTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(ContactUsPotatoTag.class);

    private String name = "contactUsPotato";

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
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        Map<String, Object> dataMap = ContactUsService.defaultService().collectContactUsFormData();
        collectCustomerData(dataMap, user, request);
        ctx.setAttribute(name, dataMap);
    }

    private void collectCustomerData(Map<String, Object> dataMap, FDUserI user, HttpServletRequest request) {

        ErpCustomerInfoModel customerInfo = collectCustomerInfo(user);
        boolean hasCustomerInfo = null != customerInfo;
        
        ContactUsCustomerData customer = new ContactUsCustomerData();

        if (hasCustomerInfo) {
            collectCustomerData(customer, customerInfo);
        }
        collectRequestData(customer, request);
        
        String csNumberMedia = user.getCustomerServiceContactMediaPath();
        customer.setCsNumberMedia(csNumberMedia);
        
        dataMap.put("customerData", SoyTemplateEngine.convertToMap(customer));
    }

    private ErpCustomerInfoModel collectCustomerInfo(FDUserI user) {
        ErpCustomerInfoModel customerInfo = null;
        try {
            customerInfo = user.getCustomerInfoModel();
        } catch (FDResourceException e) {
            LOGGER.error("Could not collect Customer Info of customer with id: " + user.getUserId(), e);
        }
        return customerInfo;
    }

    private void collectCustomerData(ContactUsCustomerData customer, ErpCustomerInfoModel customerInfo) {
        customer.setEmail(customerInfo.getEmail());
        customer.setFirstName(customerInfo.getFirstName());
        customer.setLastName(customerInfo.getLastName());
        if (null != customerInfo.getHomePhone()) {
            customer.setHomePhone(customerInfo.getHomePhone().getPhone());
            customer.setHomePhoneExt(customerInfo.getHomePhone().getExtension());
        }
        if (null != customerInfo.getOtherPhone()) {
            customer.setAltPhone(customerInfo.getOtherPhone().getPhone());
            customer.setAltPhoneExt(customerInfo.getOtherPhone().getExtension());
        }
    }

    private void collectRequestData(ContactUsCustomerData customer, HttpServletRequest request) {
        if (null != request.getParameter("subject")) {
            customer.setSubjectIndex(collectSubjectIndex(request.getParameter("subject")));
        }
        if (null != request.getParameter("email")) {
            customer.setEmail(request.getParameter("email"));
        }
        if (null != request.getParameter("first_name")) {
            customer.setFirstName(request.getParameter("first_name"));
        }
        if (null != request.getParameter("last_name")) {
            customer.setLastName(request.getParameter("last_name"));
        }
        if (null != request.getParameter("home_phone")) {
            customer.setHomePhone(request.getParameter("home_phone"));
        }
        if (null != request.getParameter("home_phone_ext")) {
            customer.setHomePhoneExt(request.getParameter("home_phone_ext"));
        }
        if (null != request.getParameter("work_phone")) {
            customer.setWorkPhone(request.getParameter("work_phone"));
        }
        if (null != request.getParameter("work_phone_ext")) {
            customer.setWorkPhoneExt(request.getParameter("work_phone_ext"));
        }
        if (null != request.getParameter("alt_phone")) {
            customer.setAltPhone(request.getParameter("alt_phone"));
        }
        if (null != request.getParameter("alt_phone_ext")) {
            customer.setAltPhoneExt(request.getParameter("alt_phone_ext"));
        }
        if (null != request.getParameter("message")) {
            customer.setBody(request.getParameter("message"));
        }
    }

    private int collectSubjectIndex(String subject) {
        int subjectIndex;
        try {
            subjectIndex = Integer.parseInt(subject);
        } catch (NumberFormatException Ex) {
            subjectIndex = 0;
        }
        return subjectIndex;
    }
}
