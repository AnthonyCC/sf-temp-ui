/**
 * @author ekracoff
 * Created on Mar 30, 2005*/

package com.freshdirect.webapp.taglib.crm;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;


public class CrmSessionStatus implements HttpSessionBindingListener{
    private static Category LOGGER = LoggerFactory.getInstance( CrmStatus.class );
    private CrmStatus status;
    private HttpSession session;
    
	public CrmSessionStatus(CrmStatus status, HttpSession session) {
		super();
		this.status = status;
		this.session = session;
	}
	
	public void setFDUser(FDUserI user){
		this.status.setErpCustomerId(user.getIdentity().getErpCustomerPK());
	}
	
	public void setCase(CrmCaseModel cm){
		this.status.setCaseId(cm.getPK().getId());
	}
	
	public void setSaleId(String saleId){
		this.status.setSaleId(saleId);
	}
	
	public String getRedirectUrl(){
		
		if(this.status.getCaseId() != null){
			try {
				CrmCaseModel cm = CrmManager.getInstance().getCaseByPk(status.getCaseId());
				CrmSession.setLockedCase(session, cm);
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
		}
		
		if(this.status.getSaleId() != null){
			return "/main/order_details.jsp?orderId=" + status.getSaleId();
		} 
		
		if(this.status.getErpCustomerId() != null){
			try {
				FDCustomerModel cust = FDCustomerFactory.getFDCustomerFromErpId(status.getErpCustomerId());
				return "/main/account_details.jsp?erpCustId=" + cust.getErpCustomerPK() + "&fdCustId=" + cust.getPK().getId();
			} catch (FDResourceException e) {
				e.printStackTrace();
			}
		}
		
		return null;
		
		
	}
	
	public void clear(boolean clearCase){
		if(clearCase){
			status.setCaseId(null);
		}
		status.setErpCustomerId(null);
		status.setSaleId(null);
	}
	
	public void valueBound(HttpSessionBindingEvent event) {
		
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		System.out.println(status.getErpCustomerId());
		System.out.println(">>>> Session unbound, storing status...");	
		try {
			CrmManager.getInstance().saveSessionStatus(status);
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
