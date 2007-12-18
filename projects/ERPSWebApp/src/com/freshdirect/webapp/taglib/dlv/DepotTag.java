/*
 * 
 * DepotTag.java
 * Date: Aug 6, 2002 Time: 4:34:09 PM
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

import com.freshdirect.delivery.depot.DlvDepotModel;
import com.freshdirect.delivery.depot.ejb.*;
import com.freshdirect.delivery.*;

public class DepotTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private String id = null;
	private String depotId = null;
	
	public String getId(){
		return this.id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getDepotId(){
		return this.depotId;
	}
	
	public void setDepotId(String depotId){
		this.depotId = depotId;
	}
	
	public int doStartTag() throws JspException {
		
		Context ctx = null;
		DlvDepotManagerHome home = null;
		DlvDepotModel depot = null;
		Collection zones = null;
		try{
			ctx = DlvProperties.getInitialContext();
			home = (DlvDepotManagerHome) getHome("freshdirect.delivery.DepotManager", ctx);
			DlvDepotManagerSB sb = home.create();
			Collection depots = sb.getDepots();
			
			for(Iterator i = depots.iterator(); i.hasNext(); ){
				depot = (DlvDepotModel)i.next();
				if(this.depotId.equals(depot.getPK().getId())){
					break;
				}
			}
			if(depot == null){
				throw new JspException("Cannot find the depot for id: "+this.depotId);
			}
			
			zones = sb.getZonesForRegionId(depot.getRegionId());
			
		}catch(NamingException ne){
			throw new JspException(ne);
		}catch(CreateException ce){
			throw new JspException(ce);
		}catch(RemoteException re){
			throw new JspException(re);
		}catch(DlvResourceException de){
			throw new JspException(de);
		}
		
		pageContext.setAttribute(id, depot);
		pageContext.setAttribute("zones", zones);
		return EVAL_BODY_BUFFERED;
	}
	
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("id"),
				"com.freshdirect.delivery.depot.DlvDepotModel", true, VariableInfo.NESTED),
				
				new VariableInfo("zones", "java.util.Collection", true, VariableInfo.NESTED)
			};
		}
	}

}
