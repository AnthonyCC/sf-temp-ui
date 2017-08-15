package com.freshdirect.dataloader.ewallet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.ejb.CreateException;

import java.rmi.RemoteException;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ewallet.ejb.EwalletNotifyStatusHome;
import com.freshdirect.fdstore.ewallet.ejb.EwalletNotifyStatusSB;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.service.FDECommerceService;

public class EwalletNotifyStatusCron {
	
	private final static Category LOGGER = LoggerFactory.getInstance(EwalletNotifyStatusCron.class);
		
	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}
	
	public static void main(String[] args) {
		int maxDays = 0;
		printHelpMessage();
		if (args.length > 0) {
			try {
				maxDays = Integer.parseInt(args[0]);
			} catch (Exception e) {
				maxDays = 0;
			}
		}
		try {
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.EwalletNotifyStatusSB)){
				FDECommerceService.getInstance().loadTrxnsForPostBack(maxDays);
				FDECommerceService.getInstance().postTrxnsToEwallet();
			}else{
			Context ctx=getInitialContext();
			EwalletNotifyStatusHome ewalletHome=(EwalletNotifyStatusHome)ctx.lookup( "freshdirect.fdstore.EWalletNotify" ) ;
			EwalletNotifyStatusSB sb = ewalletHome.create();

			sb.loadTrxnsForPostBack(maxDays);
			sb.postTrxnsToEwallet();
			}

		} catch (RemoteException e) {
			LOGGER.error("Exception while Postback ", e.getCause());
			sendPostBackFailureEmail(e);
		} catch (NamingException e) {
			LOGGER.error("Exception while Postback ", e.getCause());
			sendPostBackFailureEmail(e);
		} catch (CreateException e) {
			LOGGER.error("Exception while Postback ", e.getCause());
			sendPostBackFailureEmail(e);
		}
	
	}
	
	public static void sendPostBackFailureEmail(Throwable e) {
		
		String ewalletNotifyEmailEnabledStr = ErpServicesProperties.geteWalletNotifyEnabled();
		boolean ewalletNotifyEmailEnabled = false;
		try {
			if (ewalletNotifyEmailEnabledStr != null && !ewalletNotifyEmailEnabledStr.trim().equals(""));
				ewalletNotifyEmailEnabled = Boolean.valueOf(ewalletNotifyEmailEnabledStr);
		} catch (Exception ex) {
			LOGGER.info("Ewallet Notify email sending is disbaled. Please use 'ewallet.notify.email.send' property to enable it ", ex);
			//Not significant default to false
		}
		if (!ewalletNotifyEmailEnabled) {
			return;
		}
		
		StringWriter sw = new StringWriter();
		
		sw.write("---------------------------------------------\n");
		sw.write("Ewallet Postback Failed (");
		sw.write(new SimpleDateFormat().format(new Date()));
		sw.write(")\n");
		sw.write("---------------------------------------------\n");
		
		sw.write(e.getMessage() + "\n");
		e.printStackTrace(new PrintWriter(sw));
		
		ErpMailSender mailer = new ErpMailSender();
		
		try {
			mailer.sendMail(ErpServicesProperties.geteWalletNotifyFrom(), ErpServicesProperties.geteWalletNotifyTo(), ErpServicesProperties.geteWalletNotifyCC(), "Ewallet Postback Failure", sw.getBuffer().toString());
		} catch (MessagingException me) {
			LOGGER.fatal("Could not send a email for Ewallet Postback Failure", me);
		}
		
	}
	
	private static void printHelpMessage() {
		LOGGER.info("USAGE: java com.freshdirect.dataloader.ewallet.EwalletNotifyStatusCron");
	}
}
