package com.freshdirect.webapp.taglib.dlv;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.delivery.DlvProperties;

public class DlvOrderTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private static final SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
	private String id;
	
	public void setId(String id){
		this.id = id;
	}
	
	public int doStartTag() throws JspException {
		
		Context ctx = null;
		ErpCustomerManagerHome home = null;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String selectedDate = request.getParameter("date_to_confirm");
		String truckNumber = request.getParameter("truck_number");
		List orders = null;
		if(selectedDate != null && !"".equals(selectedDate)){
			try{
				ctx = DlvProperties.getInitialContext();
				home = (ErpCustomerManagerHome) getHome("freshdirect.erp.CustomerManager", ctx);
				ErpCustomerManagerSB sb = home.create();
				orders = sb.getOrdersByTruckNumber(truckNumber, sf.parse(selectedDate));
							
			}catch(NamingException ne){
				throw new JspException(ne);
			}catch(CreateException ce){
				throw new JspException(ce);
			}catch(RemoteException re){
				throw new JspException(re);
			}catch(ParseException pe){
				throw new JspException(pe);
			}
		}else{
			orders = new ArrayList();
		}
		
		pageContext.setAttribute(id, orders);
		return EVAL_BODY_BUFFERED;
	}
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("id"),
				"java.util.List", true, VariableInfo.NESTED)
			};
		}
	}

}
