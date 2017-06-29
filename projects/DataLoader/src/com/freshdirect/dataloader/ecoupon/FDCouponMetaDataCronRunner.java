package com.freshdirect.dataloader.ecoupon;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ecoupon.FDCouponManagerHome;
import com.freshdirect.fdstore.ecoupon.FDCouponManagerSB;
import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityContext;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.service.FDECommerceService;

public class FDCouponMetaDataCronRunner {

	private final static Category LOGGER = LoggerFactory.getInstance(FDCouponMetaDataCronRunner.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Context ctx = null;
		try {
			LOGGER.info("FDCouponMetaDataCron Started.");
//			FDCouponManager.loadAndSaveCoupons(null);
			FDCouponActivityContext activityContext = new FDCouponActivityContext(EnumTransactionSource.SYSTEM, "SYSTEM", null);
			if (FDStoreProperties.isSF2_0_AndServiceEnabled("fdstore.ecoupon.FDCouponManagerSB")) {
				FDECommerceService.getInstance().loadAndSaveCoupons(activityContext);
			} else {
				ctx = getInitialContext();
				FDCouponManagerHome managerHome = (FDCouponManagerHome) ctx.lookup(FDStoreProperties.getFDCouponManagerHome());
				FDCouponManagerSB sb = managerHome.create();
				sb.loadAndSaveCoupons(activityContext);
				LOGGER.info("FDCouponMetaDataCron Stopped.");
			}
		} catch (Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg=sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("FDCouponMetaDataCron failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			if(_msg!=null && _msg.indexOf("timed out while waiting to get an instance from the free pool")==-1)
				email(Calendar.getInstance().getTime(), _msg);		
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
					ctx = null;
				}
			} catch (NamingException ne) {
				//could not do the cleanup
				email(Calendar.getInstance().getTime(), ne.toString());
			}
		}
	}
	
	public static Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="FDCouponMetaDataCron:	"+ (processDate != null ? dateFormatter.format(processDate) : " ");

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
			LOGGER.warn("Error Sending FDCouponMetaDataCron report email: ", e);
		}
		
	}

}
