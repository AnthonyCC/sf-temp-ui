package com.freshdirect.webapp.ajax;

import java.util.List;

public interface ICoremetricsResponse {
	List<List<String>> getCoremetrics();
	
	void setCoremetrics( List<List<String>> coremetrics );
	
	void addCoremetrics( List<String> cm );
}
