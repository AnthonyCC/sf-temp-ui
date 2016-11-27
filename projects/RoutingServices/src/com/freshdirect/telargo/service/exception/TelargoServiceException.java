package com.freshdirect.telargo.service.exception;

public class TelargoServiceException extends TelargoBaseException  {
	
	public TelargoServiceException(Exception e, String id) {
	      super(null, e, id);     	      
	   }
	
	public TelargoServiceException(String msg, Exception e, String id) {
	      super(msg, e, id);	      
	      this.issueId = id;
	}
	
	public String getIssueMessage() {
		   return Issue.getMessage(issueId)+(this.getMessage() != null ? " "+this.getMessage() :"");
	 }
}
