package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.webapp.taglib.AbstractGetterTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import javax.servlet.jsp.JspException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;


public class FDCustomerCreatedListTag extends AbstractGetterTag implements SessionName {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5421728015240957952L;
	
	private String action;
	
	public void setAction(String action) {
		this.action = action;
	}

	// loadLists, loadListFrames
	public Object getResult() throws Exception {
		   
	   HttpSession session = pageContext.getSession();
	   HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
	   FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
	
	   List lists = null;
	   if ("loadLists".equals(action)) {
	       lists = (List)request.getAttribute(CUSTOMER_CREATED_LISTS);
    
	       if (lists == null) {
	           lists = user.getCustomerCreatedListInfos();
	     	   request.setAttribute(CUSTOMER_CREATED_LISTS,lists);
	       }	      
	   }
	   
	   return lists;
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
}