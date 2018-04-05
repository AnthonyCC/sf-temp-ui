package com.freshdirect.webapp.taglib.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RequiredParameterValidatorTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(RequiredParameterValidatorTag.class);

    private String parameters;


    @Override
    public void doTag() throws JspException {
        
        PageContext ctx = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        
        List<String> splitParams = new ArrayList<String>();
        List<String> missingParams = new ArrayList<String>();

        splitParams.addAll(Arrays.asList(parameters.split(",")));

        for (String parameter : splitParams) {
            String requestParam = request.getParameter(parameter);
            if (null == requestParam || "".equals(requestParam)) {
                missingParams.add(parameter);
            }
        }

        if (!missingParams.isEmpty()) {
            throw new FDNotFoundException("The URI: " + request.getRequestURI() + " was called without the parameters: " + missingParams.toString());
        }
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

}
