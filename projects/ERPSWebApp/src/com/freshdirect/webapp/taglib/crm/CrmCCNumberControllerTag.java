package com.freshdirect.webapp.taglib.crm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import weblogic.security.SimpleCallbackHandler;
import weblogic.security.services.Authentication;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.ActivityLog;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpCreditCardModel;
import com.freshdirect.customer.ErpECheckModel;
import com.freshdirect.customer.ejb.ErpCreateCaseCommand;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.mail.CrmSecurityCCCheckEmailVO;
import com.freshdirect.fdstore.mail.CrmSecurityCCCheckInfo;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.AbstractControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SystemMessageList;

public class CrmCCNumberControllerTag extends AbstractControllerTag {
	
	private String orderId;
	private String id;
	private final static ServiceLocator LOCATOR = new ServiceLocator();
	
	public void setOrderId(String orderId){
		this.orderId = orderId;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	protected boolean performAction(HttpServletRequest request, ActionResult actionResult) throws JspException {
		HttpSession session = pageContext.getSession();
		CrmAgentModel agent = CrmSession.getCurrentAgent(session);
		/*String accessCode = NVL.apply(request.getParameter("accesskey")," ");
		String hashedAccessCode = MD5Hasher.hash(accessCode);
		if(hashedAccessCode == null || !hashedAccessCode.equals(FDStoreProperties.getCrmCCDetailsAccessKey())) {
			actionResult.addError(true, "authentication", "Please enter the correct access key.");
			return true;
		}*/
		/*if(null != agent && !agent.isAuthorizedToSeeAuthAndCCInfo()){
			actionResult.addError(true, "authentication", "You are not authorized to see this info.");
			return true;
		}*/
		String password = NVL.apply(request.getParameter("password"), "");
		if("".equals(password)){
			actionResult.addError(true, "password", SystemMessageList.MSG_REQUIRED);
			return true;
		}
		try {
//			CrmManager.getInstance().loginAgent(agent.getUserId(), password);
			Subject subject = Authentication.login(new SimpleCallbackHandler(agent.getUserId(), password));
			FDOrderI order = FDCustomerManager.getOrder(this.orderId);
			List ccList = new ArrayList();
			ccList.add(order.getPaymentMethod());
			pageContext.setAttribute(this.id, ccList);
//			CrmManager.getInstance().logViewAccount(agentId, order.getCustomerId());
			if(order.getPaymentMethod()!= null /*&& (order.getPaymentMethod() instanceof ErpECheckModel || order.getPaymentMethod() instanceof ErpCreditCardModel )*/){
				
				if(order.getPaymentMethod() instanceof ErpECheckModel){
					CrmManager.getInstance().logViewAccount(agent, order.getCustomerId(),EnumAccountActivityType.VIEW_ECHECK,order.getPaymentMethod().getMaskedAccountNumber());
				}
				else if(order.getPaymentMethod() instanceof ErpCreditCardModel){
					CrmManager.getInstance().logViewAccount(agent, order.getCustomerId(),EnumAccountActivityType.VIEW_CC,order.getPaymentMethod().getMaskedAccountNumber());					
				}
				checkCCViewsLimit();
			}else{
				CrmManager.getInstance().logViewAccount(agent, order.getCustomerId());
			}
			
 		} catch (FDResourceException e) {
			actionResult.addError(true, "technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR);
		} catch (LoginException e) {
			actionResult.addError(true, "authentication", "Password is wrong");
		}
		return true;
	}

	private void checkCCViewsLimit() throws FDResourceException {
		Date date= new Date();
		Integer ccLimit = FDStoreProperties.getCrmCCDetailsLookupLimit();
		ErpActivityRecord template = new ErpActivityRecord();
		template.setActivityType(EnumAccountActivityType.VIEW_CC);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		template.setFromDateStr(dateFormat.format(date));
		Collection<ErpActivityRecord> list = ActivityLog.getInstance().getCCActivitiesByTemplate(template);
		if(null!= list && !list.isEmpty() && list.size()>= FDStoreProperties.getCrmCCDetailsLookupLimit()){
			//Getting the dummy customer id.
			PrimaryKey pk = FDCustomerManager.getCustomerId(ErpServicesProperties.getFDEmailAddress());
			CrmSystemCaseInfo info =new CrmSystemCaseInfo(pk,CrmCaseSubject.getEnum(CrmCaseSubject.CODE_SECURITY_INTERNAL),ccLimit +" or more full CC views logged.");			
			ErpCreateCaseCommand caseCmd=new ErpCreateCaseCommand(LOCATOR, info);						
			caseCmd.execute();
			
			//send email notification to the security group.
			if(FDStoreProperties.isCrmCCSecurityNotificationEnabled()){
//				List<CrmSecurityCCCheckInfo> infoList = new ArrayList<CrmSecurityCCCheckInfo>();
				List<CrmSecurityCCCheckInfo> ccInfoList = new ArrayList<CrmSecurityCCCheckInfo>();
				List<CrmSecurityCCCheckInfo> eCheckInfoList = new ArrayList<CrmSecurityCCCheckInfo>();
				Integer ccCount =0;
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {
					ErpActivityRecord erpActivityRecord = (ErpActivityRecord) iterator.next();
					CrmSecurityCCCheckInfo ccInfo = new CrmSecurityCCCheckInfo();
					ccInfo.setAgentId(erpActivityRecord.getInitiator());
					ccInfo.setActivityType(erpActivityRecord.getActivityType());
					if(EnumAccountActivityType.VIEW_CC.equals(ccInfo.getActivityType())){
						ccCount++;
						ccInfoList.add(ccInfo);
					}else{
						eCheckInfoList.add(ccInfo);
					}
					ccInfo.setCustomerId(erpActivityRecord.getCustomerId());
					ccInfo.setCustomerFirstName(erpActivityRecord.getCustFirstName());
					ccInfo.setCustomerLastName(erpActivityRecord.getCustLastName());
//					infoList.add(ccInfo);					
				}
				CrmSecurityCCCheckEmailVO emailVO = new CrmSecurityCCCheckEmailVO();
//				emailVO.setCcCheckInfo(infoList);
				emailVO.setCcInfo(ccInfoList);
				emailVO.setECheckInfo(eCheckInfoList);
				emailVO.setForDate(dateFormat.format(date));
				SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("h:mm a");
				emailVO.setAsOfTime(TIME_FORMATTER.format(date));
				emailVO.setCcInfoSize(ccInfoList.size());
				emailVO.setECheckInfoSize(eCheckInfoList.size());
				FDCustomerManager.sendCrmCCSecurityEmail(emailVO);
			}
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
					"java.util.List",
					true,
					VariableInfo.NESTED )
					
			};
		}
	}
}
