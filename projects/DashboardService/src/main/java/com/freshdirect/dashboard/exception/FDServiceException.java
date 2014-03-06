package com.freshdirect.dashboard.exception;

public class FDServiceException extends FDBaseException  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3830751929435262666L;

	public FDServiceException(Exception e, String id) {
	      super(null, e, id);     	      
	   }
	
	public FDServiceException(String msg, Exception e, String id) {
	      super(msg, e, id);	      
	      this.issueId = id;
	}
	
	public String getIssueMessage() {
		   return Issue.getMessage(issueId)+(this.getMessage() != null ? " "+this.getMessage() :"");
	 }
}
