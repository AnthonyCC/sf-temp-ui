package com.freshdirect.webapp.soy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormMetaDataService;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;
import com.google.template.soy.data.SoyMapData;

public class SoyRendererTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(SoyRendererTag.class);

    private String template;
    private Map<String, ?> data;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, ?> getData() {
        return data;
    }

    public void setData(Map<String, ?> data) {
        this.data = data;
    }

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        PageContext pageContext = (PageContext) getJspContext();
        ServletContext servletCtx = pageContext.getServletContext();
        FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
        Cookie[] cookies = ((HttpServletRequest) pageContext.getRequest()).getCookies();

        // LOGGER.debug( "Rendering " + template);

        Map<String, Object> dataObj = new HashMap<String, Object>();
        if (data != null) {
            dataObj.putAll(data);
        }
        
        try {
            dataObj.put("abFeatures", SoyTemplateEngine.convertToMap(FeaturesService.defaultService().getActiveFeaturesMapped(cookies, user)));
            dataObj.put("metadata", SoyTemplateEngine.convertToMap(FormMetaDataService.defaultService().populateFormMetaData(user)));
            dataObj.put("mobWeb", FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(((HttpServletRequest) pageContext.getRequest()).getHeader("User-Agent")));
        } catch (FDResourceException e) {
            LOGGER.error("Can not decorate soy data with additional information.", e);
        }

        try {
            String result = StringEscapeUtils.unescapeHtml(SoyTemplateEngine.getInstance().render(servletCtx, template, new SoyMapData(dataObj)));
            out.write(result);

            // LOGGER.debug( "Rendered " + template + " successfully.");
            // LOGGER.debug( "data = " + data );
            // LOGGER.debug( "result = " + result );

        } catch (IOException e) {
            throw new JspException("Failed to render template: " + template, e);
        } catch (RuntimeException e) {
            throw new JspException("Failed to render template: " + template + " => " + e.getMessage(), e);
        }

    }

}
