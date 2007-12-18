package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class AbstractMultiActionController extends MultiActionController implements InitializingBean {
	
	public void afterPropertiesSet() throws Exception {

	}
	
	public void saveMessage(HttpServletRequest request, Object msg) {
        List messages = (List) request.getSession().getAttribute("messages");
    	if (messages == null) {
    		messages = new ArrayList();
        }
        messages.add(msg);
        request.getSession().setAttribute("messages", messages);
    }
	
	public String getMessage(String key, Object[] param) {
		
		return getMessageSourceAccessor().getMessage(key,param);
	}
	
	protected String[] getParamList(HttpServletRequest request) {
		String strID = request.getParameter("id");		
		if (strID != null && strID.trim().length() > 0) {
			return strID.split(",");			
		}
		return null;
	}
	
}
