package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.crm.CrmSession;


public class AccountActivityUtil implements SessionName {

	private static final Category LOGGER = LoggerFactory.getInstance(AccountActivityUtil.class);

	public static FDActionInfo getActionInfo(HttpSession session) {
		return getActionInfo(session, "");
	}

	public static FDActionInfo getActionInfo(HttpSession session, FDUserI user) {
		FDActionInfo info = getActionInfo(session, "");
		if (info.getIdentity() == null && user != null && user.getIdentity() != null) {
			info.setIdentity(user.getIdentity());
		}
		return info;
	}
	
	public static FDActionInfo getActionInfo(HttpSession session, String note) {
		EnumTransactionSource src;
		String initiator="SYSTEM";//default
		CrmAgentModel agent;

		String app = (String) session.getAttribute(SessionName.APPLICATION);
		if ((app != null && app.equalsIgnoreCase("CALLCENTER")) ) {
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
	     } else if (app != null
	                && app.equalsIgnoreCase(EnumTransactionSource.FOODKICK_WEBSITE
	                        .getCode())) {
	            src = EnumTransactionSource.FOODKICK_WEBSITE;
	            agent = null;
	            initiator = "CUSTOMER";
		} else if (app != null
                && app.equalsIgnoreCase(EnumTransactionSource.FDX_IPHONE
                        .getCode())) {
            src = EnumTransactionSource.FDX_IPHONE;
            agent = null;
            initiator = "CUSTOMER";
		} else if (app != null
				&& app.equalsIgnoreCase(EnumTransactionSource.ANDROID_PHONE
						.getCode())) {
			src = EnumTransactionSource.ANDROID_PHONE;
			agent = null;
			initiator = "CUSTOMER";
		}  	else {
			src = EnumTransactionSource.WEBSITE;
			agent = null;
			
			initiator = "CUSTOMER";
		}
		
		FDSessionUser currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);

		FDIdentity identity = currentUser == null ? null : currentUser.getIdentity();
		EnumEStoreId eStore=EnumEStoreId.FD;
		if(currentUser!=null && currentUser.getUserContext().getStoreContext()!=null) {
			eStore=currentUser.getUserContext().getStoreContext().getEStoreId();
		
		}
		
		if(currentUser!=null && currentUser.getUser()!=null && currentUser.getUser().getMasqueradeContext()!=null ) {
            FDActionInfo.setMasqueradeAgentTL(currentUser.getUser().getMasqueradeContext().getAgentId());
        }
		
		FDActionInfo info = new FDActionInfo(eStore,src, identity, initiator, note, agent, (currentUser!=null)?currentUser.getPrimaryKey():null);
		info.setDebitCardSwitch(FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, currentUser));

		if(currentUser!=null) {
			info.setClientIp(currentUser.getClientIp());
		}
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
	    } else if (app != null
                  && app.equalsIgnoreCase(EnumTransactionSource.FOODKICK_WEBSITE
                          .getCode())) {
              src = EnumTransactionSource.FOODKICK_WEBSITE;
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