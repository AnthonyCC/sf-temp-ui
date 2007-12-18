/*Created on Nov 20, 2003*/
package com.freshdirect.fdstore.survey;


public class FDSurveyAnswer {
	private final String name;
	private final String description;
	
	public FDSurveyAnswer(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

}