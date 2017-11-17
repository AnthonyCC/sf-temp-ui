package com.freshdirect.webapp.taglib.warmup;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.webapp.soy.SoyTemplateEngine;
import com.freshdirect.webapp.warmup.WarmupService;
import com.freshdirect.webapp.warmup.data.WarmupPageData;

public class WarmupPageTag extends SimpleTagSupport {

    private String name = "warmupPageData";

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        WarmupPageData warmupPageData = WarmupService.defaultService().populateWarmupPageData();
        pageContext.setAttribute(name, SoyTemplateEngine.convertToMap(warmupPageData));
    }
}
