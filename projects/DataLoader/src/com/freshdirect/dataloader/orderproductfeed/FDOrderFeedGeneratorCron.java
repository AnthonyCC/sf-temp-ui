package com.freshdirect.dataloader.orderproductfeed;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManagerHome;
import com.freshdirect.fdstore.brandads.FDBrandProductsAdManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.service.FDECommerceService;

public class FDOrderFeedGeneratorCron {

	private static final Logger LOGGER = LoggerFactory.getInstance(FDOrderFeedGeneratorCron.class);
	
	public static void main(String[] args) throws Exception{

		Date orderFeedDateFrom=null;
		Context ctx = null;
		List<String> ordersList = null;
		if(!FDStoreProperties.isHookLogicBlackHoleEnabled()){
			try {
				LOGGER.info("FDOrderFeedGeneratorCron Started.");
				ctx = getInitialContext();
				FDBrandProductsAdManagerHome managerHome = (FDBrandProductsAdManagerHome) ctx
						.lookup(FDStoreProperties.getFDBrandProductsAdManagerHome());
				FDBrandProductsAdManagerSB sb = managerHome.create();
				if (args.length >= 1) {
					for (String arg : args) {
						if (arg.startsWith("minutes=")) {
							String noOfMins = arg.substring("minutes=".length());
							if (null != noOfMins
									&& !noOfMins.trim().equalsIgnoreCase("")) {
								orderFeedDateFrom = getDate(noOfMins);
								if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
								FDECommerceService.getInstance().submittedOrderdDetailsToHL(orderFeedDateFrom);
								}else{
								sb.submittedOrderdDetailsToHL(orderFeedDateFrom);
								}
							}
						} else if (arg.startsWith("orders=")) {
							String orders = arg.substring("orders=".length());
							String[] order = orders.split(",");
							ordersList = new ArrayList<String>(Arrays.asList(order));
							if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
								FDECommerceService.getInstance().submittedOrderdDetailsToHL(ordersList);
								}else{
									sb.submittedOrderdDetailsToHL(ordersList);
								}
	
						}
						break;
					}
				} else {
					try {
						orderFeedDateFrom = sb.getLastSentFeedOrderTime();
					} catch (Exception e) {
						LOGGER.warn("Exception while getting lastSentFeedOrderTime: "
								+ e);
					}
					if (null == orderFeedDateFrom) {
						// 15 minute back from system time.
						Integer noOfMins = FDStoreProperties.getHlOrderFeedMins();
						orderFeedDateFrom = getDate(noOfMins.toString());
					}
					LOGGER.info("FDOrderFeedGeneratorCron - sending orders from: "+orderFeedDateFrom);
					if(FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.brandads.FDBrandProductsAdManagerSB")){
						FDECommerceService.getInstance().submittedOrderdDetailsToHL(orderFeedDateFrom);
						}else{
					sb.submittedOrderdDetailsToHL(orderFeedDateFrom);
						}
				}
	
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				String _msg = sw.getBuffer().toString();
				LOGGER.info(new StringBuilder(
						"FDOrderFeedGeneratorCron failed with Exception...")
						.append(_msg).toString());
				LOGGER.error(_msg);
				if (_msg != null
						&& _msg.indexOf("timed out while waiting to get an instance from the free pool") == -1) {
					email(Calendar.getInstance().getTime(), _msg);
				}
			} 
		}
		LOGGER.info("FDOrderFeedGeneratorCron Stopped.");


}
	private static void email(Date processDate, String exceptionMsg) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="FDOrderFeedGeneratorCron for HookLogic: "+ (processDate != null ? dateFormatter.format(processDate) : " ");
			StringBuffer buff = new StringBuffer();
			buff.append("<html>").append("<body>");			
			if(exceptionMsg != null) {
				buff.append("<b>").append(exceptionMsg).append("</b>");
			}
			buff.append("</body>").append("</html>");
	
			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
			}catch (MessagingException e) {
			LOGGER.warn("Error Sending FDOrderFeedGeneratorCron report email: ", e);
			}
	
	}

public static Context getInitialContext() throws NamingException {
	Hashtable<String, String> h = new Hashtable<String, String>();
	h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
	return new InitialContext(h);
}

private static Date getDate(String noOfMins) throws ParseException{
	int minute = Integer.parseInt(noOfMins);
	DateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
	
	Calendar cal = Calendar.getInstance();
	//cal.setTime(currentDate);
	cal.add(Calendar.MINUTE, -minute);
	return sdfDate.parse(sdfDate.format(cal.getTime()));
	   
	
	}
}
