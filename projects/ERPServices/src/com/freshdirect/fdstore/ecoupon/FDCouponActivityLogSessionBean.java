package com.freshdirect.fdstore.ecoupon;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.ERPSessionBeanSupport;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpCouponDiscountLineModel;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionDetailModel;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.FDCouponEligibleInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponWallet;
import com.freshdirect.fdstore.ecoupon.service.CouponServiceException;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDCouponActivityLogSessionBean extends GatewaySessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getInstance(FDCouponActivityLogSessionBean.class);
	
	public void logCouponActivity(FDCouponActivityLogModel log) throws FDResourceException{
		/*Connection conn = null;
		try {
			conn = getConnection();
			FDCouponActivityLogDAO.logCouponActivity(log, conn);
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} */
		enqueue(log);
	}
	
	private void enqueue(FDCouponActivityLogModel log) {
		try {
			ObjectMessage logMsg = this.qsession.createObjectMessage();
			logMsg.setStringProperty("MessageType","LOG_COUPON_ACTIVITY" );
			logMsg.setObject(log);
			this.qsender.send(logMsg);
		} catch (Exception ex) {
			LOGGER.warn("Error enqueueing command", ex);
		}
	}
}
