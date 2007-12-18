/*
 * 
 * DepotRegionTag.java
 * Date: Aug 12, 2002 Time: 6:09:54 PM
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

public class DepotRegionTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	
	public int doStartTag() throws JspException {
		
		Context ctx = null;
		DlvDepotManagerHome home = null;
		
		Collection regions = null;
		
		try{
			ctx = DlvProperties.getInitialContext();
			home = (DlvDepotManagerHome) getHome("freshdirect.delivery.DepotManager", ctx);
			DlvDepotManagerSB sb = home.create();			
			regions = sb.getAllRegions();
			
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
		pageContext.setAttribute("regions", regions);
		return EVAL_BODY_BUFFERED;
	}
	
	
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {			
				new VariableInfo("regions", "java.util.Collection", true, VariableInfo.NESTED)
			};
		}
	}

}
