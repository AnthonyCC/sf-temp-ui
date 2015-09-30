package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.webapp.taglib.crm.CrmSession;

public class AccountActivityUtil implements SessionName {

	public static FDActionInfo getActionInfo(HttpSession session) {
		return getActionInfo(session, "");
	}

	public static FDActionInfo getActionInfo(HttpSession session, String note) {
		EnumTransactionSource src;
		String initiator="SYSTEM";//default
		CrmAgentModel agent;
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

		String app = (String) session.getAttribute(SessionName.APPLICATION);
		if ((app != null && app.equalsIgnoreCase("CALLCENTER")) ||(user != null && user.getMasqueradeContext() != null)) {
			src = EnumTransactionSource.CUSTOMER_REP;
			agent = CrmSession.getCurrentAgent(session);
			if (agent != null) {
				initiator = agent.getUserId();
			} else {
				CallcenterUser ccUser = (CallcenterUser) session
						.getAttribute(SessionName.CUSTOMER_SERVICE_REP);
				initiator = ccUser.getId();
			}

		} else if (app != null
				&& app.equalsIgnoreCase(EnumTransactionSource.IPHONE_WEBSITE
						.getCode())) {
			src = EnumTransactionSource.IPHONE_WEBSITE;
			agent = null;
			initiator = "CUSTOMER";
		} else if (app != null
				&& app.equalsIgnoreCase(EnumTransactionSource.ANDROID_WEBSITE
						.getCode())) {
			src = EnumTransactionSource.ANDROID_WEBSITE;
			agent = null;
			initiator = "CUSTOMER";
		}else {
			src = EnumTransactionSource.WEBSITE;
			agent = null;
			initiator = "CUSTOMER";
		}
		
		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);

		FDIdentity identity = currentUser == null ? null : currentUser.getIdentity();
		
		FDActionInfo info = new FDActionInfo(src, identity, initiator, note, agent, (currentUser!=null)?currentUser.getPrimaryKey():null);

		return info;
	}
	
	public static FDCouponActivityContext getCouponActivityContext(HttpSession session){
		EnumTransactionSource src;
		String initiator="SYSTEM";//default
		CrmAgentModel agent;

		String app = (String) session.getAttribute(SessionName.APPLICATION);
		if (app != null && app.equalsIgnoreCase("CALLCENTER")) {
			src = EnumTransactionSource.CUSTOMER_REP;
			agent = CrmSession.getCurrentAgent(session);
			if (agent != null) {
				initiator = agent.getUserId();
			} else {
				CallcenterUser ccUser = (CallcenterUser) session
						.getAttribute(SessionName.CUSTOMER_SERVICE_REP);
				initiator = ccUser.getId();
			}

		} else if (app != null
				&& app.equalsIgnoreCase(EnumTransactionSource.IPHONE_WEBSITE
						.getCode())) {
			src = EnumTransactionSource.IPHONE_WEBSITE;
			agent = null;
			initiator = "CUSTOMER";
		} else if (app != null
				&& app.equalsIgnoreCase(EnumTransactionSource.ANDROID_WEBSITE
						.getCode())) {
			src = EnumTransactionSource.ANDROID_WEBSITE;
			agent = null;
			initiator = "CUSTOMER";
		}else {
			src = EnumTransactionSource.WEBSITE;
			agent = null;
			initiator = "CUSTOMER";
		}
		
		FDCouponActivityContext couponContext = new FDCouponActivityContext(src, initiator, agent);
		
		return couponContext;
		
	}

}