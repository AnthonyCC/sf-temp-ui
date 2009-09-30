package com.freshdirect.webapp.taglib.crm;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.InvalidCardException;
import com.freshdirect.giftcard.ejb.GiftCardManagerHome;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmGiftCardBalanceTag extends AbstractControllerTag {
		
		private String id;
		private String givexNum;
		
		private final static ServiceLocator LOCATOR = new ServiceLocator();
		
		public void setId(String id) {
			this.id = id;
		}
		

		protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
			//String givexNum = NVL.apply(request.getParameter("givexNum"), "");
			if(null == givexNum || "".equals(givexNum)){
				actionResult.addError(true, "givexNum", SystemMessageList.MSG_REQUIRED);
				return true;
			}else if(givexNum.length()<16){
				actionResult.addError(true, "fullGivexNum", SystemMessageList.MSG_REQUIRED);
				return true;
			}
			try {
				GiftCardManagerHome home= getGiftCardManagerHome();        		        		        	
        		GiftCardManagerSB remote=home.create();       
        		ErpGiftCardModel model = remote.validateAndGetGiftCardBalance(givexNum);
        		/*
        		Double gcBalance = null;
        		if(model!=null){
        			gcBalance = new Double(model.getBalance());       			
        		}
        		*/
	       		pageContext.setAttribute(this.id, model);
	
				
			} catch (InvalidCardException e) {						
				
					actionResult.addError(true, "invalidCard", e.getMessage());
					return true;
				
			} catch (RemoteException e1) {
				throw new JspException(e1);
			} catch (CreateException e1) {
				throw new JspException(e1);
			}
			return true;
		}
		
		private static GiftCardManagerHome getGiftCardManagerHome() {
			try {
				return (GiftCardManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.GiftCardManager", GiftCardManagerHome.class);
			} catch (NamingException e) {
				throw new EJBException(e);
			}
		}
		
		public static class TagEI extends TagExtraInfo {
			public VariableInfo[] getVariableInfo(TagData data) {
				return new VariableInfo[] {
					 new VariableInfo(
						data.getAttributeString("result"),
						"com.freshdirect.framework.webapp.ActionResult",
						true,
						VariableInfo.NESTED),
					new VariableInfo(
						data.getAttributeString("id"),
						"com.freshdirect.giftcard.ErpGiftCardModel",
						true,
						VariableInfo.NESTED )
						
				};
			}
		}

		public void setGivexNum(String givexNum) {
			this.givexNum = givexNum;
		}
	}



