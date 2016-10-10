package com.freshdirect.webapp.taglib.unbxd;

import javax.servlet.http.HttpServletRequest;

public class RequestUrlUtil {

    private static RequestUrlUtil instance;
    
    public static RequestUrlUtil getInstance() {
        if(instance == null){
            instance = new RequestUrlUtil();
        }
        return instance;
    }
    
    private RequestUrlUtil(){
    }
    
    /**
     * Utility method for assembling the full request url
     * @param request
     * @return the request url with the query parameters
     */
    public String getFullRequestUrl(HttpServletRequest request){
        StringBuffer urlBuilder = request.getRequestURL();
        if(request.getQueryString() != null && !request.getQueryString().isEmpty()){
            urlBuilder.append("?").append(request.getQueryString());
        }
        return urlBuilder.toString();
    }
}
