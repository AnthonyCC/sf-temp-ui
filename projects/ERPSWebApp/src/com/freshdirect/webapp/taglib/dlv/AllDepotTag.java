/*
 * 
 * DepotControllerTag.java
 * Date: Aug 2, 2002 Time: 6:34:55 PM
 */
package com.freshdirect.webapp.taglib.dlv;

/**
 * 
 * @author knadeem
 */
import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.freshdirect.delivery.depot.ejb.*;
import com.freshdirect.delivery.*;

public class AllDepotTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private String id = null;
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public int doStartTag() throws JspException {
		
		Collection depots = null;
		Context ctx = null;
		DlvDepotManagerHome home = null;
		try{
			ctx = DlvProperties.getInitialContext();
			home = (DlvDepotManagerHome) getHome("freshdirect.delivery.DepotManager", ctx);
			DlvDepotManagerSB sb = home.create();
			depots = sb.getDepots();
			
		}catch(NamingException ne){
			throw new JspException(ne);
		}catch(CreateException ce){
			throw new JspException(ce);
		}catch(RemoteException re){
			throw new JspException(re);
		}catch(DlvResourceException de){
			throw new JspException(de);
		}
		
		pageContext.setAttribute(id, depots);
		return EVAL_BODY_BUFFERED;
	}
	
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("id"),
				"java.util.Collection", true, VariableInfo.NESTED)
			};
		}
	}

}
