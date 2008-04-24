/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;

import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;

import com.freshdirect.common.customer.EnumServiceType;

import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

import com.freshdirect.webapp.util.RobotRecognizer;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class CheckLoginStatusTag extends com.freshdirect.framework.webapp.TagSupport implements SessionName {
    
    private static Category LOGGER = LoggerFactory.getInstance( CheckLoginStatusTag.class );
    
    private String id;
    private String redirectPage;
    private boolean guestAllowed = true;
    private boolean recognizedAllowed = true;
    
    private boolean redirected = false;
    
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setRedirectPage(String redirectPage){
        this.redirectPage = redirectPage;
    }
    
    public void setGuestAllowed(boolean guestAllowed){
        this.guestAllowed = guestAllowed;
    }
    
    public void setRecognizedAllowed(boolean recognizedAllowed){
        this.recognizedAllowed = recognizedAllowed;
    }
    
    
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		FDSessionUser user = (FDSessionUser) session.getAttribute(USER);

		if (RobotRecognizer.isHostileRobot((HttpServletRequest)pageContext.getRequest())) {
            doRedirect(true);
            return SKIP_BODY;
        }
        
        if (user == null) {
            // try to figure out user identity based on persistent cookie
            try {
                LOGGER.debug("attempting to load user from cookie");
                user = CookieMonster.loadCookie( (HttpServletRequest)pageContext.getRequest() );

            } catch (FDResourceException ex) {
                LOGGER.warn(ex);
            }
            if (user != null) {
                LOGGER.debug("user was found!  placing in session");
                session.setAttribute( SessionName.USER, user);
            }
            
//          // new COS changes redirct corporate user to corporate page
            if(user!=null)
              LOGGER.debug("entering the corporate check"+user.getUserServiceType());
            
            LOGGER.debug("request.getRequestURI() :"+request.getRequestURI());
                                        
            if (user!=null && EnumServiceType.CORPORATE.equals(user.getUserServiceType())) {
            	// only index page request will be redirected to corporate page
            	if(request.getRequestURI().indexOf("index.jsp")!=-1){
            			this.redirectPage = "/department.jsp?deptId=COS";
            			doRedirect(true);
            			return SKIP_BODY;
            	}
            }
            
        }
      
        if ((user==null) ||
        (user.getLevel()==FDSessionUser.GUEST && !guestAllowed) ||
        (user.getLevel()==FDSessionUser.RECOGNIZED && !recognizedAllowed) ||
        (user.isNotServiceable() && !user.isDepotUser() && !guestAllowed)) {
            //
            // redirect, unless this is a request from a friendly robot we want to let in
            //
            if (guestAllowed && RobotRecognizer.isFriendlyRobot((HttpServletRequest)pageContext.getRequest())) {
                //
                // make sure the robot has a user in it's session so that pages won't blow up for it
                //
                if (user == null) {
                    FDUser robotUser = new FDUser(new PrimaryKey("robot"));
                    Set availableServices = new HashSet();
                    availableServices.add(EnumServiceType.HOME);
                    robotUser.setSelectedServiceType(EnumServiceType.HOME);
                    robotUser.setAvailableServices(availableServices);
                    robotUser.isLoggedIn(false);
                    user = new FDSessionUser(robotUser, session);
                    session.setAttribute(USER, user);
                }
            } else {
                doRedirect(user==null);
                return SKIP_BODY;
            }
        }
        if (this.id!=null) {
            pageContext.setAttribute( this.id, user );
        }
        
        /*
         * 
         *  if this is a new session and the user types http://www.bestcellarsnewyork.com/
         * redirect to http://www.bestcellarsnewyork.com/department.jsp?deptId=win
         * 
         * 
         */
         
         if (request.getServerName().toLowerCase().indexOf("bestcellarsnewyork") > -1 && session.isNew()) {
			this.redirectPage = "/department.jsp?deptId=win";
			doRedirect(true);
         }
         

        
        return EVAL_BODY_INCLUDE;
    }
    
    private void doRedirect(boolean firstRequest) throws JspException {
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        try {
            response.sendRedirect(response.encodeRedirectURL( this.getRedirectURL(firstRequest) ));
            JspWriter writer = pageContext.getOut();
            writer.close();
            this.redirected = true;
        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }
    
    protected String getRedirectURL(boolean firstRequest) {
        if (this.redirectPage!=null) {
            return this.redirectPage;
        }
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		StringBuffer redirBuf =
			new StringBuffer(
				this.guestAllowed
					? "/site_access/site_access.jsp?successPage="
					: (firstRequest ? "/login/login_main.jsp?successPage=" : "/login/login.jsp?successPage="));
        redirBuf.append(request.getRequestURI());
        
        String requestQryString = request.getQueryString();
        if (requestQryString !=null && requestQryString.trim().length() > 0 ) {
            redirBuf.append(URLEncoder.encode("?"+request.getQueryString()));
        }
        return redirBuf.toString();
    }
    
    public int doEndTag() throws JspException {
        if (this.redirected) {
            return SKIP_PAGE;
        } else return EVAL_PAGE;
    }
    
    
}
