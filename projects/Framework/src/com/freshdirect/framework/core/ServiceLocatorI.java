package com.freshdirect.framework.core;

import javax.naming.NamingException;


public interface ServiceLocatorI {

	public Object getRemoteHome(String jndiHomeName) throws NamingException;

}
