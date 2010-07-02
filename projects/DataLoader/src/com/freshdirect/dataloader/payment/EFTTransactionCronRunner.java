/*
 * Created on Mar 29, 2005
 */
package com.freshdirect.dataloader.payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EFTTransaction;
import com.freshdirect.payment.ejb.ReconciliationHome;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.fraud.EnumRestrictionReason;

/**
 * @author jng
 *
 */

public class EFTTransactionCronRunner {
	
	private final static Category LOGGER = LoggerFactory.getInstance(EFTTransactionCronRunner.class);

	private final static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	private final static SimpleDateFormat sdtf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
	
	public static void main(String[] args) {
		Date startDate = DateUtil.truncate(new Date());  // today starting at midnight
		Date endDate = null;
		
		if (args.length >= 1) {
			for (String arg : args) {
				try { 
					if (arg.startsWith("startDate=")) {
							startDate = sdf.parse(arg.substring("startDate=".length())); 
					} else if (arg.startsWith("startDateTime=")) {
							startDate = sdtf.parse(arg.substring("startDateTime=".length())); 
					} else if (arg.startsWith("endDate=")) {
							endDate = sdf.parse(arg.substring("endDate=".length())); 
					} else if (arg.startsWith("endDateTime=")) {
							endDate = sdtf.parse(arg.substring("endDateTime=".length())); 
					}
				} catch (ParseException pe) {
					System.err.println("Usage: java com.freshdirect.dataloader.payment.EFTTransactionConRunner [startDate=MM-DD-YYYY -OR- startTimeDate=MM-DD-YYYY HH:MM] [endDate=MM-DD-YYYY -OR- endTimeDate=MM-DD-YYYY HH:MM] ");
					System.exit(-1);
				}
			}
		}
		
		Context ctx = null;
		try {
			LOGGER.info("echeckCron started");
			ctx = getInitialContext();
			//(ErpSaleHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpSale", ErpSaleHome.class);
			ErpSaleHome saleHome=(ErpSaleHome)ctx.lookup("freshdirect.erp.Sale");
			
			ReconciliationHome reconciliationCronHome = (ReconciliationHome) ctx.lookup("freshdirect.payment.Reconciliation");
			ReconciliationSB reconciliationSB = reconciliationCronHome.create();
			//ErpSaleEB erpSaleEB = this.getErpSaleHome().findByPrimaryKey(new PrimaryKey(saleId));
			// process bad transactions
			List badTxnList = reconciliationSB.loadBadTransactions(startDate, endDate);
			processBadTransactions(badTxnList, reconciliationSB,saleHome);
			LOGGER.info("echeckCron finished");

		} catch (Exception e) {
			LOGGER.error(e);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				//could not do the cleanup
			}
		}
	}

	static private void processBadTransactions(List txnList, ReconciliationSB reconciliationSB,ErpSaleHome saleHome) {

		EFTTransaction paymentTransaction = new EFTTransaction(); 
		if (txnList != null && txnList.size() > 0) {
			Iterator iter = txnList.iterator();
			while (iter.hasNext()) {
				try {
					paymentTransaction = (EFTTransaction) iter.next(); 
					String orderNumber = paymentTransaction.getOrderNumber();
					int index = orderNumber.indexOf("X");
					String saleId = (index > -1) ? orderNumber.substring(0, index) : orderNumber;
					//String accountNumber = paymentTransaction.getBankAccountNumber();
					ErpSaleEB erpSaleEB = saleHome.findByPrimaryKey(new PrimaryKey(saleId));
					String accountNumber =erpSaleEB.getCurrentOrder().getPaymentMethod().getAccountNumber();
					double amount = paymentTransaction.getAmount();
					String sequenceNumber = paymentTransaction.getSequenceNumber();
					EnumPaymentResponse paymentResponse = getPaymentResponse(paymentTransaction);
					String description = paymentResponse.getDescription();
					int usageCode = 2;
					ErpAffiliate aff = ErpAffiliate.getAffiliateByMerchant(EnumCardType.ECP, paymentTransaction.getMerchantId());
					reconciliationSB.processECPReturn(saleId, aff, accountNumber, amount, sequenceNumber, paymentResponse, description, usageCode);
				} catch (Exception e) {
					LOGGER.error("EFTTransactionCronRunner.processFailedTransactions: Account Number = " + StringUtil.maskCreditCard(paymentTransaction.getBankAccountNumber()) + "-"+ e.getMessage());						
				}
			}
		}
			
	}
			
	static private EnumPaymentResponse getPaymentResponse(EFTTransaction paymentTransaction) {
		
		EnumRestrictionReason restrictedReason = EnumRestrictionReason.GENERAL_ACCOUNT_PROBLEM;		
		EnumPaymentResponse paymentResponse = null;
		
		try {
			if (paymentTransaction.getBadFieldCode() != null && !"".equals(paymentTransaction.getBadFieldCode())) {
				paymentResponse = EnumPaymentResponse.getEnum(paymentTransaction.getBadFieldCode());
			} else if (paymentTransaction.getProcResponseCode() != null && !"".equals(paymentTransaction.getProcResponseCode())) {
				paymentResponse = EnumPaymentResponse.getEnum(paymentTransaction.getProcResponseCode());
			}
			return paymentResponse;		
		} catch (Exception e) {
			LOGGER.error("EFTTransactionCronRunner.getPaymentResponse: " + e.getMessage());
		}
		return null;
	}

	static public Context getInitialContext() throws NamingException {
		Hashtable<String, String> h = new Hashtable<String, String>();
		h.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		h.put(Context.PROVIDER_URL, ErpServicesProperties.getProviderURL());
		return new InitialContext(h);
	}

}
