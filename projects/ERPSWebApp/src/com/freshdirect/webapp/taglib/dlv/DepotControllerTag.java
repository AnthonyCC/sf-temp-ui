/*
 * 
 * DepotController.java
 * Date: Aug 8, 2002 Time: 8:32:22 PM
 */
package com.freshdirect.webapp.taglib.dlv;

/**
 * 
 * @author knadeem
 */
import java.util.*;
import java.io.IOException;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.ejb.*;
import com.freshdirect.delivery.*;


public class DepotControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport { 
	
	private String depotId;
	private String redirectPage;
	
	public void setDepotId(String depotId){
		this.depotId = depotId;
	}
	
	public void setRedirectPage(String redirectPage){
		this.redirectPage = redirectPage;
	}
	
	public int doStartTag() throws JspException {
		
		Context ctx = null;
		DlvDepotManagerHome home = null;
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String action = request.getParameter("action");
		String method = request.getMethod();
		
		try{
			ctx = DlvProperties.getInitialContext();
			home = (DlvDepotManagerHome) getHome("freshdirect.delivery.DepotManager", ctx);
			DlvDepotManagerSB sb = home.create();
			
			if("POST".equalsIgnoreCase(method)){
				
				if("add_depot".equalsIgnoreCase(action)){	
					sb.addNewDepot(getDepotFromRequest(request));
				}
				
				if("edit_depot".equalsIgnoreCase(action)){
					sb.updateDepot(this.depotId, getDepotFromRequest(request)); 
				}
				
				if("delete_depot".equalsIgnoreCase(action)){
					sb.deleteDepot(depotId);
				}
			}
			
			return EVAL_BODY_BUFFERED;
			
		}catch(NamingException ne){
			throw new JspException(ne);
		}catch(CreateException ce){
			throw new JspException(ce);
		}catch(RemoteException re){
			re.printStackTrace();
			throw new JspException(re);
		}catch(DlvResourceException de){
			throw new JspException(de);
		}
	}
	
	public int doEndTag() throws JspException{
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String action = request.getParameter("action");
		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method) && "delete_depot".equalsIgnoreCase(action)){
			try{
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				response.sendRedirect(response.encodeRedirectURL(redirectPage));
				JspWriter writer = pageContext.getOut();
				writer.close();
			}catch(IOException ie){
				throw new JspException(ie.getMessage());
			}
			return SKIP_PAGE;	
		}else{
			return EVAL_PAGE;
		}
	}
	
	private DlvDepotModel getDepotFromRequest(HttpServletRequest request){
		
		String name = request.getParameter("depot_name");
		String registrationCode = request.getParameter("registration_code");
		String regionId = request.getParameter("region_id");
		String depotCode = request.getParameter("depot_code");
        String custServEmail = request.getParameter("cust_serv_email");
        boolean requireEmployeeId = request.getParameter("requireEmployeeId") != null;
        boolean pickup = request.getParameter("pickup") != null;
        boolean corporateDepot = request.getParameter("corporateDepot") != null;
        boolean deactivated = request.getParameter("deactivated") != null;
		
		return new DlvDepotModel(name, registrationCode, regionId, new ArrayList(), depotCode, custServEmail, requireEmployeeId, pickup, corporateDepot, deactivated);	
	}

}
