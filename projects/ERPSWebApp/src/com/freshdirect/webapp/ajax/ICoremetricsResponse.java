package com.freshdirect.webapp.ajax;

import java.util.List;

public interface ICoremetricsResponse {
	public List<List<String>> getCoremetrics();
	
	public void setCoremetrics( List<List<String>> coremetrics );

	public void addCoremetrics( List<String> cm );
	
}
