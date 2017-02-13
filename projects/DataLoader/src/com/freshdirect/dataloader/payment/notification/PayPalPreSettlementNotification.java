package com.freshdirect.dataloader.payment.notification;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;


import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.payment.gateway.ewallet.impl.PayPalReconciliationSB;

/**
 * This Job is to notify AppSupport with a list of dates that paypal settlement
 * is not executed
 * 
 * @author kumarramachandran
 * 
 */
public class PayPalPreSettlementNotification {

	/**
	 * @param args
	 */
	static PayPalReconciliationSB ppReconSB = null;

	public static void main(String[] args) {

		try {
			if (null == ppReconSB) {
				ppReconSB = SettlementLoaderUtil.lookupPPReconciliationHome().create();
			}
			Map<String, String> settlementNotExecutedDates = ppReconSB
					.getPPSettlementNotProcessed();

			if (settlementNotExecutedDates.size() != 7) {
				String body = getEmailContent(settlementNotExecutedDates);
				SettlementLoaderUtil.sendEmailNotification(" PayPal Settlement Not Processed ",
						body, null);
			}

		} catch (RemoteException e) {
			e.printStackTrace();
			SettlementLoaderUtil
					.sendEmailNotification(
							" PayPal Settlement Not Processed ",
							"Exception occured while excuting PayPal Settlement Not Processed Notification",
							e);
		} catch (EJBException e) {
			SettlementLoaderUtil
					.sendEmailNotification(
							" PayPal Settlement Not Processed ",
							"Exception occured while excuting PayPal Settlement Not Processed Notification",
							e);
		} catch (CreateException e) {
			SettlementLoaderUtil.sendEmailNotification(" PayPal Settlement Not Processed ",
					"Exception occured while excuting PayPal Settlement Not ProcessedNotification",
					e);
		}

	}

	private static DateFormat sd = new SimpleDateFormat("EEE, MMM d, yyyy");

	private static String getEmailContent(Map<String, String> settlementNotExecutedDates) {

		StringBuffer buffer = new StringBuffer();

		buffer.append("PayPal Settlement Not Processed \n \n");

		Calendar cal = null;
		// Verify details for last seven days
		for (int dateCnt = 0; dateCnt < 7; dateCnt++) {

			cal = Calendar.getInstance();
			cal.set(Calendar.DATE, cal.get(Calendar.DATE) - dateCnt);
			String actualDate = sd.format(cal.getTime());

			if (null == settlementNotExecutedDates.get(actualDate)) {
				buffer.append("PayPal Settlement File").append(" ")
						.append(actualDate).append("\n \n ");
			}

		}
		return buffer.toString();
	}

}
