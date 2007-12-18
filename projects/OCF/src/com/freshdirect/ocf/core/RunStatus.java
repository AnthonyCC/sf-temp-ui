/**
 * @author ekracoff
 * Created on Aug 15, 2005*/

package com.freshdirect.ocf.core;


public class RunStatus {
	
	/** Run finished (completed or failed) */
	public final static String FINISHED = "FINISHED";
	
	/** Run failed with recoverable error */
	public final static String RETRY    = "RETRY";
	
	/** Run is being processed */
	public final static String RUNNING  = "RUNNING";

}
