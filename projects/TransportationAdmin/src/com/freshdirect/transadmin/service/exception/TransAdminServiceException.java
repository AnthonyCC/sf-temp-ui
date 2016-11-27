package com.freshdirect.transadmin.service.exception;

import com.freshdirect.routing.service.exception.Issue;
import com.freshdirect.routing.service.exception.RoutingBaseException;

public class TransAdminServiceException extends RoutingBaseException  {
	
	public TransAdminServiceException(Exception e, String id) {
	      super(null, e, id);     	      
	   }
	
	public TransAdminServiceException(String msg, Exception e, String id) {
	      super(msg, e, id);	      
	      this.issueId = id;
	}
	
	public String getIssueMessage() {
		   return Issue.getMessage(issueId)+(this.getMessage() != null ? " "+this.getMessage() :"");
	 }
}
