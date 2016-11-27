/**
 * @author ekracoff
 * Created on Mar 15, 2005*/

package com.freshdirect.cms;

/**
 * Exception indicating a parallel publish attempt.
 */
public class ConcurrentPublishException extends CmsRuntimeException {

	public ConcurrentPublishException() {
		super();
	}

}
