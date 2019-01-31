package com.freshdirect.dataloader.payment.notification;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.fdstore.ecomm.gateway.PayPalReconciliationService;

/**
 * This Job is to notify AppSupport with a list of dates that paypal settlement
 * is not executed
 * 
 * @author kumarramachandran
 * 
 */
public class PayPalPreSettlementNotification {


	public static void main(String[] args) {

		try {
			Map<String, String> settlementNotExecutedDates = null;
			settlementNotExecutedDates = PayPalReconciliationService.getInstance().getPPSettlementNotProcessed();
			
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
