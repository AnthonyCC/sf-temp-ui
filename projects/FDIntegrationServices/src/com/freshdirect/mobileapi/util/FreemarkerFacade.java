package com.freshdirect.mobileapi.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletContext;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerFacade {

    private Configuration config = new Configuration();

    public void setContext(ServletContext context) {
        config.setServletContextForTemplateLoading(context, "WEB-INF/templates");
    }

    public String getTemplateValue(String templateName, Object root) throws IOException, TemplateException {
        Writer out = new StringWriter();
        Template temp = config.getTemplate(templateName);
        temp.process(root, out);
        return out.toString();
    }

}
