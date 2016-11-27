package com.freshdirect.mobileapi.model.tagwrapper;

import javax.servlet.jsp.PageContext;

public class UtilsWrapper extends HttpContextWrapper {

    public UtilsWrapper() {
    }
    
    public static interface UtilsExecutionWrapper {
        public Object executeUtils(PageContext pageContext);
    }

    public UtilsExecutionWrapper utilsExecutionWrapper;

    public void setUtilsExecutionWrapper(UtilsExecutionWrapper utilsExecutionWrapper) {
        this.utilsExecutionWrapper = utilsExecutionWrapper;
    }

    public Object executeUtil() {
        return utilsExecutionWrapper.executeUtils(pageContext);
    }

}
