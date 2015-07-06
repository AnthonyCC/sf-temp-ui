package com.freshdirect.webapp.ajax;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCoremetricsResponse implements ICoremetricsResponse{

	private List<List<String>> coremetrics = new ArrayList<List<String>>();

	public List<List<String>> getCoremetrics() {
		return coremetrics;
	}
	
	public void setCoremetrics( List<List<String>> coremetrics ) {
		this.coremetrics = coremetrics;
	}

	public void addCoremetrics( List<String> cm ) {
		if ( cm != null ) {
			this.coremetrics.add( cm );
		}
	}
	
}
