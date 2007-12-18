/**
 * @author ekracoff
 * Created on Apr 15, 2005*/

package com.freshdirect.ocf.core;

public interface ListGeneratorServiceI {
	
	public OcfTableI getCustomers(Object criteria);
	
	public OcfTableI getCustomers(Object dataSource, Object criteria);

}
