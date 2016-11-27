/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.framework.webapp;

import javax.ejb.*;
import javax.naming.*;

/**
 * Base class for jsp tags with a body
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class TagSupport extends javax.servlet.jsp.tagext.TagSupport {
    
    /** looks up a home interface based on its binding to the local
     * comp/env space in the webapp deployment descriptor
     * @param envName the name the homne interface is bound to in the web deployment descriptor
     * @throws NamingException any errors while trying to locate the home interface
     * @return an EJBHome interface
     */    
    protected EJBHome getHome(String envName) throws NamingException {
        Context ctx = new InitialContext();
        EJBHome home = (EJBHome) ctx.lookup("java:comp/env/" + envName);
        ctx.close();
        return home;
    }
    
    /** looks up a home interface based on its binding to the local comp/env
     * space in the webapp deployment descriptor
     * @param envName the name to which home interface is bound to in the web deployment descriptor
     * @param ctx context in which the bean is deployed
     * @throws NamingException any errors while trying to locate the home interface
     * @return an EJBHome interface
     */
    protected EJBHome getHome(String envName, Context ctx) throws NamingException {
    	EJBHome home = (EJBHome) ctx.lookup(envName);
    	return home;
    }
    
    
}
