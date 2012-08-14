package com.freshdirect.athena.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.athena.config.Parameter;

public class ApiKey implements Serializable {
	
	private String apiName;
	
	private List<Parameter> parameters = new ArrayList<Parameter>();

	public ApiKey(String apiName, List<Parameter> parameters) {
		super();
		this.apiName = apiName;
		this.parameters = parameters;
	}

	public String getApiName() {
		return apiName;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return "ApiKey [apiName=" + apiName + ", parameters=" + parameters
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiName == null) ? 0 : apiName.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiKey other = (ApiKey) obj;
		if (apiName == null) {
			if (other.apiName != null)
				return false;
		} else if (!apiName.equals(other.apiName))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		return true;
	}
	
	
}
