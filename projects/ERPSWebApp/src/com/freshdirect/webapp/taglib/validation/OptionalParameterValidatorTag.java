package com.freshdirect.webapp.taglib.validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class OptionalParameterValidatorTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(OptionalParameterValidatorTag.class);

    private String parameter;
    private String parameterType;

    @Override
    public void doTag() throws JspException {

        PageContext ctx = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();

        String requestParamValue = request.getParameter(parameter);
        boolean validationError = false;

        if (requestParamValue != null) {
            try {
                if (parameterType.equals(Integer.class.getSimpleName())) {
                    Integer.parseInt(requestParamValue);
                } else if (parameterType.equals(Double.class.getSimpleName())) {
                    Double.parseDouble(requestParamValue);
                } else if (parameterType.equals(Long.class.getSimpleName())) {
                    Long.parseLong(requestParamValue);
                }
            } catch (NumberFormatException e) {
                validationError = true;
            }
        }

        if (validationError) {
            String errorMessage = String.format("The URI: %1s was called with incorrect parameter the parameters: %2s = %3s", request.getRequestURI(), parameter,
                    requestParamValue);

            throw new FDNotFoundException(errorMessage);
            }
        }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    }
