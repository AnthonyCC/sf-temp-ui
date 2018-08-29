/*Created on Nov 20, 2003*/
package com.freshdirect.fdstore.survey;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FDSurveyAnswer implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1024613242306132060L;
	private final String name;
	private final String description;
	private final String group;
	
	
	public FDSurveyAnswer(String name, String description){
		this.name = name;
		this.description = description;
		group="";
		
	}

	public FDSurveyAnswer(@JsonProperty("name") String name, @JsonProperty("description") String description,
			@JsonProperty("group") String group) {
		this.name = name;
		this.description = description;
		this.group = group;

	}
	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}
}