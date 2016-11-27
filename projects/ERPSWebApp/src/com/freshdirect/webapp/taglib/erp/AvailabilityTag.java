/*
 * AvailabilityControllerTag.java
 *
 * Created on March 4, 2002, 11:21 AM
 */

package com.freshdirect.webapp.taglib.erp;

/**
 *
 * @author  knadeem
 * @version 
 */
import javax.ejb.*;
import java.rmi.RemoteException;
import javax.naming.*;
import java.util.*;
import javax.servlet.jsp.*;

import com.freshdirect.framework.core.PrimaryKey;

import com.freshdirect.erp.ejb.*;
import com.freshdirect.erp.model.*;
import com.freshdirect.ErpServicesProperties;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

public class AvailabilityTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(AvailabilityTag.class);
	
	private String materialNumber;
	private String id;
	
	public String getMaterialNumber(){
		return this.materialNumber;
	}
	public void setMaterialNumber(String materialNumber){
		this.materialNumber = materialNumber;
	}
	
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	public int doStartTag() throws JspException {
		ErpInventoryModel inventory = null;
		Context ctx = null;
		try{
			ctx = ErpServicesProperties.getInitialContext();
			ErpInventoryHome home = (ErpInventoryHome) getHome("freshdirect.erp.Inventory", ctx);
			ErpInventoryEB eb = (ErpInventoryEB)home.findByPrimaryKey(new PrimaryKey(materialNumber));
			inventory = (ErpInventoryModel)eb.getModel();
			
		} catch (NamingException ne) {
             throw new JspException(ne.getMessage());
        } catch (RemoteException re) {
			throw new JspException(re.getMessage());
        }catch(FinderException fe){
			inventory = new ErpInventoryModel(materialNumber, new Date(), new ArrayList());
		}finally{
			try{
				if(ctx != null){
					ctx.close();
					ctx = null;
				}
			}catch(NamingException ne){
				LOGGER.warn("Exception while trying to close the Context", ne);
			}
		}
		pageContext.setAttribute(id, inventory);
		return EVAL_BODY_BUFFERED;
	}
}