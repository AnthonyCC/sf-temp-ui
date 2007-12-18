package com.freshdirect.webapp.action;

import java.io.Serializable;

public interface Action extends Serializable {

	/** Successfully completed, go to next page */
	public final static String SUCCESS = "success";

	/** There was an error, the current page needs to be redisplayed */
	public final static String ERROR = "error";

	/** There was a redirect, no page to be rendered */	
	public final static String NONE = "none";


	public String execute() throws Exception; 

}
