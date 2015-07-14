package com.freshdirect.analytics;

import java.sql.Connection;
import java.sql.SQLException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.ecoupon.FDCouponActivityLogDAO;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.framework.core.MessageDrivenBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
/**
 * 
 * @author ksriram
 *
 */
public class CouponActivityLogListener extends MessageDrivenBeanSupport {

	private static final long serialVersionUID = -5755200116155367468L;
	
	private static Category LOGGER = LoggerFactory.getInstance(CouponActivityLogListener.class);
	@Override
	public void onMessage(Message message) {

		ObjectMessage objMsg = (ObjectMessage) message;
		try {
			Object obj = objMsg.getObject();
//			LOGGER.debug("Received message is:"+obj);	
			if(obj instanceof FDCouponActivityLogModel)	{
				logCouponActivity((FDCouponActivityLogModel)obj);
//				LOGGER.debug("Completed logging the coupon activity.");
			}
		} catch (JMSException e) {
			LOGGER.error("JMSException in CouponActivityLogListener:"+e);
		}	
		
	}

	
	public void logCouponActivity(FDCouponActivityLogModel log){
		Connection conn = null;
		try {
			conn = getConnection();
			FDCouponActivityLogDAO.logCouponActivity(log, conn);
		} catch (SQLException e) {
			LOGGER.error("SQLException in logCouponActivity:", e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing connection:", e);
			}
		}
	}
}
