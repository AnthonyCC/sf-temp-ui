package com.freshdirect.payment.ewallet.ejb;

import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ewallet.gateway.ejb.EwalletActivityLogModel;
/**
 * 
 * @author garooru
 *
 */
@SuppressWarnings("serial")
public class EwalletActivityLogSessionBean extends GatewaySessionBeanSupport {


	private static Category LOGGER = LoggerFactory
			.getInstance(EwalletActivityLogSessionBean.class);

	public void logActivity(EwalletActivityLogModel eWalletLogModel) {

		try {
			ObjectMessage logMsg = this.qsession.createObjectMessage();
			logMsg.setStringProperty("MessageType", "LOG_EWALLETAUDIT_ACTIVITY");
			logMsg.setObject(eWalletLogModel);
			this.qsender.send(logMsg);
		} catch (Exception ex) {
			LOGGER.warn("Exception during sending message to queue.", ex);
		}
	}
}
