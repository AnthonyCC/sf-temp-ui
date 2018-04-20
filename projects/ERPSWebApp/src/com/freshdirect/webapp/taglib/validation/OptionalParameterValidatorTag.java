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

public class OptionalParameterValidatorTag extends SimpleTagSupport {

    private static final Logger LOGGER = LoggerFactory.getInstance(OptionalParameterValidatorTag.class);

    private String parameters;
    private String parameterTypes;

    @Override
    public void doTag() throws JspException {

        PageContext ctx = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) ctx.getRequest();
        List<String> incorrectParams = new ArrayList<String>();

        List<String> splitParams = Arrays.asList(parameters.split(","));
        List<String> splitParamTypes = Arrays.asList(parameterTypes.split(","));

        for (int i = 0; i < splitParams.size(); i++) {
            String requestParam = splitParams.get(i).trim();
            String requestParamType = splitParamTypes.get(i).trim();
            String requestParamValue = request.getParameter(requestParam);

            if (requestParamValue != null) {
                try {
                    if (requestParamType.equals(Integer.class.getSimpleName())) {
                        Integer.parseInt(requestParamValue);
                    } else if (requestParamType.equals(Double.class.getSimpleName())) {
                        Double.parseDouble(requestParamValue);
                    } else if (requestParamType.equals(Long.class.getSimpleName())) {
                        Long.parseLong(requestParamValue);
                    }

                } catch (NumberFormatException e) {
                    incorrectParams.add(requestParam);
                }
            }

        }

        if (!incorrectParams.isEmpty()) {
            String errorMessage = "The URI: " + request.getRequestURI() + " was called with incorrect parameter the parameters: ";
            for (String parameter : incorrectParams) {
                errorMessage = errorMessage.concat(parameter + " = " + request.getParameter(parameter));
            }

            throw new FDNotFoundException(errorMessage);
        }
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

}
