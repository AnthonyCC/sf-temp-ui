package com.freshdirect.transadmin.web.model;



public class CopyPlanCommand extends BaseCommand {
	
	private String sourceDate;
	private String dispatchDay;
	
	private String destinationDate;
	
	private String includeEmployees;
	
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

}
