package com.freshdirect.webapp.taglib.crm;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class CrmSession {

	private final static Category LOGGER = LoggerFactory.getInstance(CrmSession.class);

	private final static long LAST_FIND_EXPIRATION = 1 * 60 * 1000;

	private final static String CRM_AGENT = "fd.crm.agent";
	private final static String CRM_LOCKED_CASE = "fd.crm.lockedCase";
	private final static String CRM_SEARCH_TEMPLATE = "fd.crm.searchTemplate";

	private final static String CRM_LAST_FIND_TIMESTAMP = "fd.crm.lastFind.timestamp";
	private final static String CRM_LAST_FIND_TEMPLATE = "fd.crm.lastFind.template";
	private final static String CRM_LAST_FIND_RESULTS = "fd.crm.lastFind.results";
	
	private final static String CRM_AGENT_STATUS = "fd.crm.agent.status";
	private final static String CRM_CURRENT_ORDER = "fd.crm.current.order";
	private final static String CRM_CURRENT_CUST_HEADER = "fd.crm.current.customer.header";
	
	public static CrmAgentModel getCurrentAgent(HttpSession session) {
		return (CrmAgentModel) session.getAttribute(CRM_AGENT);
	}
	
	public static FDOrderI getOrder(HttpSession session, String orderId) throws FDResourceException {
		return getOrder(session, orderId, false);
	}
	
	public static FDOrderI getOrder(HttpSession session, String orderId, boolean refresh) throws FDResourceException {
		FDOrderI order = (FDOrderI) session.getAttribute(CRM_CURRENT_ORDER);
		if(!refresh && order != null && order.getErpSalesId() != null && order.getErpSalesId().equals(orderId)){
			return order;
		}
		order = FDCustomerManager.getOrder(orderId);
		session.setAttribute(CRM_CURRENT_ORDER, order);
		
		return order;
	}
	
	public static void invalidateCachedOrder(HttpSession session) {
		session.removeAttribute(CRM_CURRENT_ORDER);
	}

	public static void setCurrentAgent(HttpSession session, CrmAgentModel agent) {
		session.setAttribute(CRM_AGENT, agent);
	}

	public static CrmCaseModel getLockedCase(HttpSession session) {
		LockedCaseWrapper wrapper = (LockedCaseWrapper) session.getAttribute(CRM_LOCKED_CASE);
		return wrapper == null ? null : wrapper.get();
	}

	public static void setLockedCase(HttpSession session, CrmCaseModel cm) throws FDResourceException {
		if (cm != null) {
			session.setAttribute(CRM_LOCKED_CASE, new LockedCaseWrapper(cm));
			CrmManager.getInstance().lockCase(getCurrentAgent(session).getPK(), cm.getPK());
			getSessionStatus(session).setCase(cm);
		} else {
			session.removeAttribute(CRM_LOCKED_CASE);
			getSessionStatus(session).clear(true);
		}
	}

	public static CrmCaseTemplate getSearchTemplate(HttpSession session) {
		CrmCaseTemplate t = (CrmCaseTemplate) session.getAttribute(CRM_SEARCH_TEMPLATE);
		return t == null ? new CrmCaseTemplate() : t;
	}

	public static void setSearchTemplate(HttpSession session, CrmCaseTemplate template) {
		session.setAttribute(CRM_SEARCH_TEMPLATE, template);
	}

	public static List findCases(HttpSession session, CrmCaseTemplate template) throws FDResourceException {
		if (template.isBlank()) {
			return Collections.EMPTY_LIST;
		}
		
		CrmCaseTemplate lastTemplate = (CrmCaseTemplate) session.getAttribute(CRM_LAST_FIND_TEMPLATE);

		long now = System.currentTimeMillis();
		Long lastCheck = (Long) session.getAttribute(CRM_LAST_FIND_TIMESTAMP);

		List results;
		if (template.equals(lastTemplate) && lastCheck != null && (now - lastCheck.longValue() < LAST_FIND_EXPIRATION)) {
			results = (List) session.getAttribute(CRM_LAST_FIND_RESULTS);
		} else {
			results = CrmManager.getInstance().findCases(template);
			session.setAttribute(CRM_LAST_FIND_TIMESTAMP, new Long(now));
			session.setAttribute(CRM_LAST_FIND_RESULTS, results);
			session.setAttribute(CRM_LAST_FIND_TEMPLATE, template.deepCopy());
		}
		return results;
	}

	public static void clearSearchCache(HttpSession session) {
		session.removeAttribute(CRM_LAST_FIND_RESULTS);
		session.removeAttribute(CRM_LAST_FIND_TEMPLATE);
	}

	public static boolean verifyCaseAttachment(HttpSession session) {
		CrmCaseModel currentCase = getLockedCase(session);
		PrimaryKey customerPk = currentCase != null ? currentCase.getCustomerPK() : null;
		if (currentCase == null
			|| customerPk == null
			|| !customerPk.getId().equals(getUser(session).getIdentity().getErpCustomerPK())) {
			return false;
		}
		return true;
	}

	public static boolean verifyCaseAttachment(HttpSession session, ActionResult actionResult) {
		if (!verifyCaseAttachment(session)) {
			actionResult.addError(true, "case_not_attached", "current case is not for this customer");
		}
		return actionResult.isSuccess();
	}

	public static FDUserI getUser(HttpSession session) {
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		return user;
	}
	
	public static String getFDCustomerId (HttpSession session, String erpCustomerId) throws FDResourceException {
		FDUserI u = getUser(session);
		if(u != null && u.getIdentity() != null && u.getIdentity().getErpCustomerPK().equals(erpCustomerId)){
			return u.getIdentity().getFDCustomerPK();
		} else {
			return FDCustomerFactory.getFDCustomerFromErpId(erpCustomerId).getPK().getId();
		}
	}

	public static boolean hasCustomerCase(HttpSession session) {
		CrmCaseModel currentCase = getLockedCase(session);
		if (currentCase == null) {
			return false;
		}
		PrimaryKey customerPk = currentCase.getCustomerPK();
		if (customerPk == null) {
			return false;
		}
		FDUserI user = getUser(session);
		return user != null && user.getIdentity() != null && customerPk.getId().equals(user.getIdentity().getErpCustomerPK());
	}
	
	public static void setSessionStatus(HttpSession session, CrmSessionStatus status){
		session.setAttribute(CRM_AGENT_STATUS, status);
	}
	
	public static CrmSessionStatus getSessionStatus(HttpSession session){
		return (CrmSessionStatus) session.getAttribute(CRM_AGENT_STATUS);
	}
	
	public static CrmCustomerHeaderInfo getCustomerHeaderInfo(HttpSession session, FDIdentity identity) throws FDResourceException {
		CrmCustomerHeaderInfo info = (CrmCustomerHeaderInfo) session.getAttribute(CRM_CURRENT_CUST_HEADER);
		if(info == null || !info.getId().equals(identity.getErpCustomerPK())){
			info = CrmManager.getInstance().getCustomerHeaderInfo(identity.getErpCustomerPK());
			session.setAttribute(CRM_CURRENT_CUST_HEADER, info);
		}
		return info;
	}

	private static class LockedCaseWrapper implements Serializable, HttpSessionBindingListener {

		private final CrmCaseModel cm;

		public LockedCaseWrapper(CrmCaseModel cm) {
			this.cm = cm;
		}

		public CrmCaseModel get() {
			return this.cm;
		}

		public void valueBound(HttpSessionBindingEvent evt) {
		}

		public void valueUnbound(HttpSessionBindingEvent evt) {
			try {
				LOGGER.debug("Unbound: unlocking case " + cm.getPK());
				CrmManager.getInstance().unlockCase(cm.getPK());
			} catch (FDResourceException e) {
				LOGGER.warn("Unable to unlock case " + cm.getPK(), e);
			}
			
		}

	}

}
