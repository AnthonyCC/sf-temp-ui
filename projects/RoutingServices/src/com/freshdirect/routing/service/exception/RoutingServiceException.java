package com.freshdirect.routing.service.exception;

public class RoutingServiceException extends RoutingBaseException  {
	
	public RoutingServiceException(Exception e, String id) {
	      super(null, e, id);     	      
	   }
	
	public RoutingServiceException(String msg, Exception e, String id) {
	      super(msg, e, id);	      
	      this.issueId = id;
	}
	
	public String getIssueMessage() {
		   return Issue.getMessage(issueId)+(this.getMessage() != null ? " "+this.getMessage() :"");
	 }
}
