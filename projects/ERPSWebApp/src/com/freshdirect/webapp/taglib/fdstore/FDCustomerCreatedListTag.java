package com.freshdirect.webapp.taglib.fdstore;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.webapp.taglib.AbstractGetterTag;


public class FDCustomerCreatedListTag extends AbstractGetterTag implements SessionName {
	
	private static final long serialVersionUID = 5421728015240957952L;
	
	private String action;
	
	public void setAction(String action) {
		this.action = action;
	}

	// loadLists, loadListFrames
	@Override
	public Object getResult() throws Exception {
		   
	   HttpSession session = pageContext.getSession();
	   HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	   FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
	
	   List<FDCustomerListInfo> lists = null;
	   if ("loadLists".equals(action)) {
	       lists = (List<FDCustomerListInfo>)request.getAttribute(CUSTOMER_CREATED_LISTS);
    
	       if (lists == null) {
	           lists = user.getCustomerCreatedListInfos();
	     	   request.setAttribute(CUSTOMER_CREATED_LISTS,lists);
	       }	      
	   }
	   
	   return lists;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		@Override
		protected String getResultType() {
			return "java.util.List<FDCustomerListInfo>";
		}
	}
}