package com.freshdirect.dataloader.invoice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpShippingInfo;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.delivery.model.ShippingDetail;
import com.freshdirect.mail.ErpMailSender;

/**
 * As per the JIIRA TICKET APPDEV-5319 SAP is not going to send
 * freeze,alcohol,regular,truck number, stop sequence Get the list of sales
 * order with carton details from store front Get the list of sales order with
 * truck number and stop sequence from Logistic update the wave info into sale
 * entity .
 * 
 * @author kumarramachandran
 * 
 */
public class FDShippingInfoCronRunner {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDShippingInfoCronRunner.class);

	public static void main(String[] args) {
		Context ctx = null;
		List<ShippingDetail> trucks = new ArrayList<ShippingDetail>();

		try {

			LOGGER.info("START ******* CRON SHIPPING INFO UPDATE *********** ");
			// get the list of sales id to update truck and route number

			List<String> salesIds = FDCustomerManager.getShippingInfoSalesId();

			
             //Testing sample data  
			// populateTestingData(trucks,salesIds);

			if (null != salesIds && !salesIds.isEmpty()) {
				// get the carton details for the list of sales id's .
				Map<String, Map<String, Integer>> cartonInfos = FDCustomerManager
						.getShippingInfoCartonDetails(salesIds);

				// Logistics call returns list of sales id with truck

				trucks = FDDeliveryManager.getInstance().getTruckDetails();
				
				Map<String, ErpShippingInfo> erpShippingInfoMap = mapShippingDetails(cartonInfos,trucks);
				
				if (!erpShippingInfoMap.isEmpty()) {
					ctx = ErpServicesProperties.getInitialContext();
					ErpCustomerManagerSB sb = lookupErpCustomer(ctx);

					sb.updateSalesShippingInfo(erpShippingInfoMap);
				}

			}
		 LOGGER.info("END ******* CRON SHIPPING INFO UPDATE IS SUCCESS *********** ");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg = sw.getBuffer().toString();
			LOGGER.error(new StringBuilder("FDShippingInfoCronRunner - failed with Exception...")
					.append(_msg).toString());

			if (_msg != null)
				email(Calendar.getInstance().getTime(), _msg);
		} finally {
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
				}
			}
		}
	}

	/**
	 * @param ctx
	 * @return
	 * @throws NamingException
	 * @throws CreateException
	 * @throws RemoteException
	 */
	private static ErpCustomerManagerSB lookupErpCustomer(Context ctx) throws NamingException,
			CreateException, RemoteException {

		ErpCustomerManagerHome mgr = (ErpCustomerManagerHome) ctx
				.lookup("freshdirect.erp.CustomerManager");
		ErpCustomerManagerSB sb = mgr.create();

		return sb;
	}

	private static Map<String, ErpShippingInfo> mapShippingDetails(
			Map<String, Map<String, Integer>> cartonInfos, List<ShippingDetail> trucks) {
		Map<String, Integer> cartons = null;
		Map<String, ErpShippingInfo> erpShippingMap = new HashMap<String, ErpShippingInfo>();
		ErpShippingInfo shippingInfo = null;

		for (ShippingDetail truck : trucks) {

			cartons = cartonInfos.get(truck.getOrderId());
			if (cartons != null) {
				shippingInfo = new ErpShippingInfo(truck.getTruckNumber(), truck.getStopSquence(),
						null != cartons.get("Regular") ? cartons.get("Regular").intValue() : 0,
						null != cartons.get("Freezer") ? cartons.get("Freezer").intValue() : 0,
						null != cartons.get("Beer") ? cartons.get("Beer").intValue() : 0);

				erpShippingMap.put(truck.getOrderId(), shippingInfo);
			}
		}
		return erpShippingMap;
	}

	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject = "FDShippingInfoCronRunner:	"
					+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");

			if (exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),
					ErpServicesProperties.getCronFailureMailCC(), subject, buff.toString(), true,
					"");

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending FDShippingInfoCronRunner report email: ", e);
		}

	}

	/**
	 * @param trucks
	 * @param salesIds
	 */
	private static void populateTestingData(List<ShippingDetail> trucks, List<String> salesIds) {

		salesIds.add("10188565795");
		salesIds.add("2149066148");
		salesIds.add("2149066165");

		ShippingDetail sh = new ShippingDetail();
		sh.setOrderId("10188565795");
		sh.setTruckNumber("1234");
		sh.setStopSquence("012");
		trucks.add(sh);

		sh = new ShippingDetail();
		sh.setOrderId("2149066148");
		sh.setTruckNumber("1234");
		sh.setStopSquence("012");
		trucks.add(sh);

		sh = new ShippingDetail();
		sh.setOrderId("2149066165");
		sh.setTruckNumber("1234");
		sh.setStopSquence("12");
		trucks.add(sh);
	}

}
