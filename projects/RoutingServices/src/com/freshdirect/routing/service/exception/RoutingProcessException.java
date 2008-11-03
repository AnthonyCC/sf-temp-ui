package com.freshdirect.routing.service.exception;

public class RoutingProcessException extends RoutingBaseException {
		
	
	public RoutingProcessException(Exception e, String id) {
	      super(null, e, id);     	      
	}
	
	public RoutingProcessException(String msg, Exception e, String id) {
	      super(msg, e, id);
	}
	
	 public String getIssueMessage() {
		   return Issue.getMessage(issueId)+(this.getMessage() != null ? " "+this.getMessage() :"");
	 }
	
}
