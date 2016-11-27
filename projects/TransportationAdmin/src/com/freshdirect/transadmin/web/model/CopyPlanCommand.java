package com.freshdirect.transadmin.web.model;



public class CopyPlanCommand extends BaseCommand {
	
	private String sourceDate;
	private String dispatchDay;
	
	private String destinationDate;
	
	private String includeEmployees;
	
	private String ignoreErrors;
	
	private String errorSourceDate;
	
	private String errorDestinationDate;
	
	public String getIgnoreErrors() {
		return ignoreErrors;
	}


	public void setIgnoreErrors(String ignoreErrors) {
		this.ignoreErrors = ignoreErrors;
	}


	public CopyPlanCommand() {
	
	}
	
	
	public String getDestinationDate() {
		return destinationDate;
	}


	public void setDestinationDate(String destinationDate) {
		this.destinationDate = destinationDate;
	}


	public String getDispatchDay() {
		return dispatchDay;
	}


	public void setDispatchDay(String dispatchDay) {
		this.dispatchDay = dispatchDay;
	}


	public String getIncludeEmployees() {
		return includeEmployees;
	}


	public void setIncludeEmployees(String includeEmployees) {
		this.includeEmployees = includeEmployees;
	}


	public String getSourceDate() {
		return sourceDate;
	}


	public void setSourceDate(String sourceDate) {
		this.sourceDate = sourceDate;
	}


	public String toString() {
		
		return sourceDate+"|"+dispatchDay+"|"
				+destinationDate+"|"+includeEmployees+"\n";
	}


	public String getErrorDestinationDate() {
		return errorDestinationDate;
	}


	public void setErrorDestinationDate(String errorDestinationDate) {
		this.errorDestinationDate = errorDestinationDate;
	}


	public String getErrorSourceDate() {
		return errorSourceDate;
	}


	public void setErrorSourceDate(String errorSourceDate) {
		this.errorSourceDate = errorSourceDate;
	}

}
