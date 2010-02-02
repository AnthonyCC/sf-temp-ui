package com.freshdirect.framework.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.ejb.EJBHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Caching J2EE service locator. See pattern description at:
 * <blockquote><pre>
 *     <a href="http://java.sun.com/blueprints/corej2eepatterns/Patterns/ServiceLocator.html">http://java.sun.com/blueprints/corej2eepatterns/Patterns/ServiceLocator.html</a>
 * </pre></blockquote> 
 */
public class ServiceLocator {

	private final static Category LOGGER = LoggerFactory.getInstance( ServiceLocator.class );

	private final Context ctx;
	private final Map cache = Collections.synchronizedMap(new HashMap());

	public ServiceLocator() {
		try {
			this.ctx = new InitialContext();
		} catch (NamingException e) {
			LOGGER.error("Unable to get InitialContext", e);
			throw new RuntimeException("Unable to get InitialContext "+e.getMessage());
		}
	}
	
	public ServiceLocator(Context ctx) {
		this.ctx = ctx;
	}

	public EJBHome getRemoteHome(String jndiHomeName, Class className) throws NamingException {
		EJBHome home = (EJBHome) cache.get(jndiHomeName);
		if (home==null) {
			Object objref = this.ctx.lookup(jndiHomeName);
			home = (EJBHome)objref;
			//home = (EJBHome) PortableRemoteObject.narrow(objref, className);
			cache.put(jndiHomeName, home);
		}
		return home;
	}
	
}
