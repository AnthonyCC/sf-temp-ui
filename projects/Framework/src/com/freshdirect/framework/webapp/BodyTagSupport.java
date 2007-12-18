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
import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * Base class for simple jsp tags
 *
 * @version $Revision$
 * @author $Author$
 */ 
public class BodyTagSupport extends javax.servlet.jsp.tagext.BodyTagSupport {
    
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
    
    /** ensures that any lingering body content gets flushed to the output
     * @throws JspException any exceptions get rethrown as a JspException
     * @return a constant from javax.servlet.jsp.tagext.TagSupport
     */
    public int doEndTag() throws JspException {
        
        // Render any previously accumulated body content
        BodyContent content = getBodyContent();
        if (content != null) {
            try {
                content.writeOut(getPreviousOut());
            } catch (IOException ioe) {
                // eat it...
            }
        }
        
        // Continue processing this page
        return EVAL_PAGE;
        
    }
}
