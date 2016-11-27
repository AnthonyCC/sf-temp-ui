package com.freshdirect.transadmin.service.exception;

import com.freshdirect.routing.service.exception.Issue;
import com.freshdirect.routing.service.exception.RoutingBaseException;

public class TransAdminProcessException extends RoutingBaseException {
		
	
	public TransAdminProcessException(Exception e, String id) {
	      super(null, e, id);     	      
	}
	
	public TransAdminProcessException(String msg, Exception e, String id) {
	      super(msg, e, id);
	}
	
	 public String getIssueMessage() {
		   return Issue.getMessage(issueId)+(this.getMessage() != null ? " "+this.getMessage() :"");
	 }
	
}
