package com.freshdirect.dataloader.ecoupon;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ecoupon.FDCouponManagerHome;
import com.freshdirect.fdstore.ecoupon.FDCouponManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;

public class FDCouponSaleCronRunner {

	private static Category LOGGER = LoggerFactory.getInstance(FDCouponSaleCronRunner.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 1. For any cancelled orders, cancel the coupons with YT.
        //	  For any full return orders, cancel the coupons with YT.
		//	  For any full short shipped orders, cancel the coupons with YT.
		// 2. For any New/Modified Orders, submit the coupons with YT.
		// 3. For any delivery confirmed orders, confirm/commit the coupons with YT.

		LOGGER.info("FDCouponSaleCronRunner started.");
		Context ctx = null;
		
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDCouponManagerSB)) {
				IECommerceService fdECommerceService = FDECommerceService.getInstance();
				// Cancel Pending coupon transactions
				fdECommerceService.postCancelPendingCouponTransactions();

				// Submit Pending coupon transactions
				List<String> submitSales = fdECommerceService.getSubmitPendingCouponSales();
				if (null != submitSales) {
					for (Iterator<String> iterator = submitSales.iterator(); iterator.hasNext();) {
						String saleId = iterator.next();
						try {
							fdECommerceService.postSubmitPendingCouponTransactions(saleId);
						} catch (Exception e) {
							LOGGER.info(e);
						}
					}
				}

				// Confirm Pending coupon transactions
				List<String> confirmSales = fdECommerceService.getConfirmPendingCouponSales();
				if (null != confirmSales) {
					for (Iterator<String> iterator = confirmSales.iterator(); iterator.hasNext();) {
						String saleId = iterator.next();
						try {
							fdECommerceService.postConfirmPendingCouponTransactions(saleId);
						} catch (Exception e) {
							LOGGER.info(e);
						}
					}
				}
				LOGGER.info("FDCouponSaleCronRunner stopped.");
			}else{
			ctx = getInitialContext();
			FDCouponManagerHome home = (FDCouponManagerHome) ctx.lookup(FDStoreProperties.getFDCouponManagerHome());
			FDCouponManagerSB sb = home.create();
			//Cancel Pending coupon transactions
			sb.postCancelPendingCouponTransactions();
			
			//Submit Pending coupon transactions
			List<String> submitSales = sb.getSubmitPendingCouponSales();	
			if(null !=submitSales){
				for (Iterator<String> iterator = submitSales.iterator(); iterator.hasNext();) {
					String saleId =iterator.next();
					try {
						sb.postSubmitPendingCouponTransactions(saleId);
					} catch (Exception e) {
						LOGGER.info(e);
					}
				}
			}
			
			//Confirm Pending coupon transactions
			List<String> confirmSales = sb.getConfirmPendingCouponSales();
			if(null !=confirmSales){
				for (Iterator<String> iterator = confirmSales.iterator(); iterator.hasNext();) {
					String saleId =iterator.next();
					try {
						sb.postConfirmPendingCouponTransactions(saleId);
					} catch (Exception e) {
						LOGGER.info(e);
					}
				}
			}
			LOGGER.info("FDCouponSaleCronRunner stopped.");
			}
		} catch (Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String _msg=sw.getBuffer().toString();
			LOGGER.info(new StringBuilder("FDCouponSaleCronRunner failed with Exception...").append(_msg).toString());
			LOGGER.error(_msg);
			if(_msg!=null && _msg.indexOf("timed out while waiting to get an instance from the free pool")==-1)
				email(Calendar.getInstance().getTime(), _msg);
		}finally {
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

	
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	
	private static void email(Date processDate, String exceptionMsg) {
		// TODO Auto-generated method stub
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="FDCouponSaleCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " ");

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
