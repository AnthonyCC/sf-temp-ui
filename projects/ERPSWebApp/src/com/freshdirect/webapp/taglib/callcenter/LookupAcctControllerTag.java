package com.freshdirect.webapp.taglib.callcenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class LookupAcctControllerTag extends AbstractControllerTag {
	
	private static Category LOGGER = LoggerFactory.getInstance(LookupAcctControllerTag.class);
	public boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		try {
				String actionName = request.getParameter("actionName");

				HttpSession session = pageContext.getSession();
//				CrmAgentModel agent = CrmSession.getCurrentAgent(session);
				String agent = CrmSession.getCurrentAgentStr(session);

				if(actionName.equals("lookup")){
					//Remove the Session Values.
					session.removeAttribute("acctNum");
					session.removeAttribute("lookupLoc");
					String acctNumber = NVL.apply(request.getParameter("acctNum"),"").trim();
					String lookupLoc =  request.getParameter("lookupLoc");
					
					if(acctNumber.length() == 0){
						LOGGER.error("Account Number is blank - LookupAcctController Tag.");
						actionResult.addError(true, "inputerror", "Account Number cannot be blank. Type in a valid Account Number and Retry.");
						return true;
					}
					if(!validateAccountNumber(acctNumber, actionResult)){
						return true;
					}

					/*if(!agent.isAuthorizedToLookupAcctInfo()){
						actionResult.addError(true, "authentication", "You are not authorized to see this info.");
						return true;
					}*/
					session.setAttribute("acctNum", acctNumber);
					session.setAttribute("lookupLoc", lookupLoc);
					setSuccessPage("/supervisor/acct_lookup_auth.jsp");	

				}
				
				if(actionName.equals("authenticate")){
					String acctNumber = (String) session.getAttribute("acctNum");
					String lookupLoc =  (String) session.getAttribute("lookupLoc");
					String last4Digits = acctNumber.substring(acctNumber.length() - 4);
					
					String password = NVL.apply(request.getParameter("password"), "");
					if("".equals(password)){
						actionResult.addError(true, "inputerror", SystemMessageList.MSG_REQUIRED);
						return true;
					}else{
//						CrmManager.getInstance().loginAgent(agent.getUserId(), password);
						if(lookupLoc.equals("1")){
							//Lookup Customer's Account.
							String userId = CrmManager.getInstance().lookupAccount(acctNumber);
							if(userId == null){
								LOGGER.error("There is no matching User ID found for this Account Number.");
								userId = "Not Found";
							}
							request.setAttribute("userid", userId);	
						}else if(lookupLoc.equals("2")){
							//Lookup Past Orders that used this Account Number.
							List orders = CrmManager.getInstance().lookupOrders(acctNumber);
							if(orders == null || orders.isEmpty()){
								LOGGER.error("There are no matching Orders found for this Account Number.");
							}
							request.setAttribute("matchingOrders", orders);	
						}
						request.setAttribute("last4Digits", last4Digits);
					}
				}
		} catch(FDResourceException e){
			LOGGER.error("System Error ooccurred while performing the Account Lookup.", e);
			actionResult.addError(true, "lookupfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		} /*catch (CrmAuthenticationException e) {
			LOGGER.error("Authentication Failure.", e);
			actionResult.addError(true, "authentication", "Password is wrong");
		}*/catch(Exception e){
			LOGGER.error("Unknown Error occurred while performing the Account Lookup." ,e);
			actionResult.addError(true, "lookupfailure", SystemMessageList.MSG_TECHNICAL_ERROR);
		}
		return true;

	}
	
	
	private boolean validateAccountNumber(String acctNum, ActionResult actionResult){
        try {
        	Long.parseLong(acctNum);
        	int length = acctNum.length();
        	if(length < 10){
    			LOGGER.error("Invalid Account Number "+StringUtil.maskCreditCard(acctNum));
    			actionResult.addError(true, "inputerror", "Invalid Account Number. Account Number should contain minimum of 10 digits.");
    			return false;
        	}
        	
        } catch (NumberFormatException ne) { 
			LOGGER.error("Invalid Account Number "+StringUtil.maskCreditCard(acctNum));
			actionResult.addError(true, "inputerror", "Invalid Account Number. Please make sure Account Number do not contain Characters.");
			return false;
        }
	    return true;
	}
	
	public static class TagEI extends AbstractControllerTag.TagEI {
		   public VariableInfo[] getVariableInfo(TagData data) {
		        return new VariableInfo[] {
			            new VariableInfo(data.getAttributeString("result"),
                            "com.freshdirect.framework.webapp.ActionResult",
                            true,
                            VariableInfo.NESTED),
                        };
		   }
	}

}
