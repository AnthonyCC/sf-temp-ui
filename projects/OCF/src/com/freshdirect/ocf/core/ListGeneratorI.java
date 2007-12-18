/**
 * @author ekracoff
 * Created on Apr 22, 2005*/

package com.freshdirect.ocf.core;

import com.freshdirect.fdstore.FDResourceException;


public interface ListGeneratorI {

	public OcfTableI getList() throws FDResourceException;
}