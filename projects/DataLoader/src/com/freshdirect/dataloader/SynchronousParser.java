/*
 * SynchronousParser.java
 *
 * Created on August 22, 2001, 12:19 PM
 */

package com.freshdirect.dataloader;

/**
 *
 * @author  knadeem
 * @version 
 */
public interface SynchronousParser {
	
	public void setClient(SynchronousParserClient client);
	
	public SynchronousParserClient getClient();

}

