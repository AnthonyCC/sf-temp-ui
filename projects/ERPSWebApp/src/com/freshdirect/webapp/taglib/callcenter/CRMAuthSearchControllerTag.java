package com.freshdirect.webapp.taglib.callcenter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDAuthInfoSearchCriteria;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CRMAuthSearchControllerTag extends AbstractControllerTag {
	
	private String id;
	private FDAuthInfoSearchCriteria criteria;
	
	public void setId(String id){
		this.id = id;
	}
	
	public void setCriteria(FDAuthInfoSearchCriteria criteria){
		this.criteria = criteria;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
	
		if(criteria.getCardType() == null){
			actionResult.addError(new ActionError("cardType", SystemMessageList.MSG_REQUIRED));
		}
			
		if(criteria.getCCKnownNum() == null || "".equals(criteria.getCCKnownNum().trim())){
			actionResult.addError(new ActionError("ccKnownNum", SystemMessageList.MSG_REQUIRED));
		}
		
		if(criteria.getChargedAmount() <= 0){
			actionResult.addError(new ActionError("chargedAmount", SystemMessageList.MSG_REQUIRED));
		}
			
		if(!"".equals(criteria.getTransDate()) || !"".equals(criteria.getTransMonth()) || !"".equals(criteria.getTransYear())){
			if(criteria.getTransactionDate() == null){
				actionResult.addError(new ActionError("transactionDate", "You must enter a valid transaction date."));
			}
		}
				
		try{
			if(actionResult.isSuccess()){
				System.out.println("run search ");
				pageContext.setAttribute(this.id, CallCenterServices.runAuthInfoSearch(criteria));
			}
		}catch (FDResourceException e){
			actionResult.addError(new ActionError("technical difficulties", SystemMessageList.MSG_TECHNICAL_ERROR));
		}
		
		return true;
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
					"java.util.List",
					true,
					VariableInfo.NESTED )
					
			};
		}
	}

}
