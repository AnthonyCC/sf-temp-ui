package com.freshdirect.fdstore.ecoupon;

import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDCouponActivityLogSessionBean extends GatewaySessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getInstance(FDCouponActivityLogSessionBean.class);
	
	private static final String LOG_COUPON_ACTIVITY = "LOG_COUPON_ACTIVITY";
	private static final String MessageType = "MessageType";
	
	public void logCouponActivity(FDCouponActivityLogModel log) throws FDResourceException {		
		enqueue(log);
	}
	
	private void enqueue(FDCouponActivityLogModel log) {
		try {
			ObjectMessage logMsg = this.qsession.createObjectMessage();
			logMsg.setStringProperty(MessageType,LOG_COUPON_ACTIVITY );
			logMsg.setObject(log);
			this.qsender.send(logMsg);
		} catch (Exception ex) {
			LOGGER.warn("Error enqueueing command", ex);
		}
	}
}
