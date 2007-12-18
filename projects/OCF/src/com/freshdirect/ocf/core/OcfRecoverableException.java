/**
 * @author ekracoff
 * Created on Aug 15, 2005*/

package com.freshdirect.ocf.core;

import com.freshdirect.fdstore.FDException;

public class OcfRecoverableException extends FDException {

	public OcfRecoverableException(Exception e) {
		super(e);
	}

}