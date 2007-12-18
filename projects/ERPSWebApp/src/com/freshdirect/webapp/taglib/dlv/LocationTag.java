/*
 * 
 * LocationTag.java
 * Date: Aug 6, 2002 Time: 8:25:07 PM
 */
package com.freshdirect.webapp.taglib.dlv;

/**
 * 
 * @author knadeem
 */
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

import com.freshdirect.delivery.depot.DlvLocationModel;
import com.freshdirect.delivery.depot.ejb.*;
import com.freshdirect.delivery.*;

public class LocationTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private String id = null;
	private String depotId = null;
	private String locationId = null;
	
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
	
	public String getLocationId(){
		return this.locationId;
	}
	
	public void setLocationId(String locationId){
		this.locationId = locationId;
	}
	
	public int doStartTag() throws JspException {
		
		Context ctx = null;
		DlvDepotManagerHome home = null;
		DlvLocationModel location = null;
		try{
			ctx = DlvProperties.getInitialContext();
			home = (DlvDepotManagerHome) getHome("freshdirect.delivery.DepotManager", ctx);
			DlvDepotManagerSB sb = home.create();
			location = sb.getLocationForDepotId(depotId, locationId);
			
			if(location == null){
				throw new JspException("Cannot find a location for depotId: "+depotId+" locationId "+locationId);
			}
			
		}catch(NamingException ne){
			throw new JspException(ne);
		}catch(CreateException ce){
			throw new JspException(ce);
		}catch(RemoteException re){
			throw new JspException(re);
		}catch(DlvResourceException de){
			throw new JspException(de);
		}
		
		pageContext.setAttribute(id, location);
		return EVAL_BODY_BUFFERED;
	}
	
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("id"),
				"com.freshdirect.delivery.depot.DlvLocationModel", true, VariableInfo.NESTED)
			};
		}
	}

}
