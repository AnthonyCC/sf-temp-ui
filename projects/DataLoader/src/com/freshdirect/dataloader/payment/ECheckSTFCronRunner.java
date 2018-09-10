/*
 * Created on Mar 29, 2005
 */
package com.freshdirect.dataloader.payment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpCaptureModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.customer.ejb.ErpSaleHome;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.ecomm.gateway.ReconciliationService;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.ejb.ReconciliationHome;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.fraud.EnumRestrictionReason;


/**
 * @author jng
 *
 */

public class ECheckSTFCronRunner {
	
	private final static Category LOGGER = LoggerFactory.getInstance(ECheckSTFCronRunner.class);

	private final static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
	private final static SimpleDateFormat sdtf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
	
	
private static FileContext getFileContext(String[] args) {
		
		
		FileContext ctx=new FileContext();
		ctx.setFileType(PaymentFileType.SETTLEMENT_FAILURE);
		ctx.setDownloadFiles(true);
		if (args.length >= 1) {
			for (String arg : args) {
				try { 
					if (arg.startsWith("remoteURL=")) {								
						ctx.setRemoteHost(arg.substring("remoteURL=".length())); 
					}  else if(arg.startsWith("remoteUser=")) {
						ctx.setUserName(arg.substring("remoteUser=".length()));  							
					} else if(arg.startsWith("remotePassword=")) {
						ctx.setPassword(arg.substring("remotePassword=".length()));  							
					} else if(arg.startsWith("privateKey=")) {
						ctx.setOpenSSHPrivateKey(arg.substring("privateKey=".length()));  							
					}else if(arg.startsWith("fetchFiles=")) {
						ctx.setDownloadFiles(Boolean.valueOf(arg.substring("fetchFiles=".length())).booleanValue());  							
					}else if(arg.startsWith("processFile=")) {
						ctx.setFileToProcess(arg.substring("processFile=".length()))	;						
					}
				} catch (Exception e) {
					System.err.println("Usage: java com.freshdirect.dataloader.payment.bin.ECheckSTFCronRunner  [fetchFiles={true | false}] [remoteURL=Value] [remoteUser=Value] [remotePassword=Value]  [privateKey=Value] [processFile=Value]");
					System.exit(-1);
				}
			}
		}
		
		ctx.setLocalHost(DataLoaderProperties.getWorkingDir());
		if(StringUtils.isEmpty(ctx.getOpenSSHPrivateKey()))
			ctx.setOpenSSHPrivateKey(DataLoaderProperties.getWorkingDir()+DataLoaderProperties.getPaymentSFTPKey());
		if(StringUtils.isEmpty(ctx.getRemoteHost()))
			ctx.setRemoteHost(DataLoaderProperties.getPaymentStfSFTPHost());
		if(StringUtils.isEmpty(ctx.getUserName()))
			ctx.setUserName(DataLoaderProperties.getPaymentStfSFTPUser());
		if(StringUtils.isEmpty(ctx.getPassword()))
			ctx.setPassword(DataLoaderProperties.getPaymentStfSFTPPassword());
		LOGGER.info( "FileContext: "+ ctx );
		return ctx;
		
	}
	public static void main(String[] args) {
		LOGGER.info("ECheckSTFCronRunner started");
		FileContext filectx=getFileContext(args);
		
		Context ctx = null;
		List<String> files=new ArrayList<String>(10);
		try {
			if(filectx.downloadFiles()) {
				SFTPFileProcessor fp=new SFTPFileProcessor(filectx);
				files=fp.getFiles();
			} else {
				if(StringUtil.isEmpty(filectx.getFileToProcess())) {
					System.err.println("Must pass the file name to process when fetchFiles=true");
					System.err.println("Usage: java com.freshdirect.dataloader.payment.bin.ECheckSTFCronRunner  [fetchFiles={true | false}] [remoteURL=Value] [remoteUser=Value] [remotePassword=Value]  [privateKey=Value] [processFile=Value]");
					System.exit(-1);
				} else {
					files.add(filectx.getFileToProcess());
				}
			}
			
			if(files!=null ) {
				if(files.isEmpty())
					LOGGER.info("No ECheck Settlement failure files to process ");
				else {
					LOGGER.info("Processing "+files.size()+" ECheck Settlement failure files ");
					LOGGER.info(" ECheck Settlement failure -- files for processing are ..");
					for(String fileName: files) {
						LOGGER.info(fileName);
					}
				}
				
			} else  {
				LOGGER.info("List containing ECheck Settlement failure file names is NULL or EMPTY ");
			}
			
			
			ctx = getInitialContext();
			//(ErpSaleHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpSale", ErpSaleHome.class);
			ErpSaleHome saleHome=(ErpSaleHome)ctx.lookup("freshdirect.erp.Sale");
			
			ReconciliationHome reconciliationCronHome = (ReconciliationHome) ctx.lookup("freshdirect.payment.Reconciliation");
			ReconciliationSB reconciliationSB = reconciliationCronHome.create();
			// process bad transactions
			for(String fileName: files) {
				LOGGER.info(" ECheck Settlement failure -- file being processed is .."+fileName);
				List<ECheckRejectedTxDetail> badTxnList = new RejectedTxParser().parseFile(DataLoaderProperties.getWorkingDir() +fileName);
				processBadTransactions(badTxnList, reconciliationSB,saleHome);
				LOGGER.info(" ECheck Settlement failure -- finished processing file .."+fileName);
			}
			LOGGER.info("ECheckSTFCronRunner finished");

		} catch (Exception e) {
			LOGGER.error(e);
			LOGGER.info(new StringBuilder("ECheckSTFCronRunner failed with Exception...").append(e.toString()).toString());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			email(Calendar.getInstance().getTime(), sw.getBuffer().toString());
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

	static private void processBadTransactions(List<ECheckRejectedTxDetail> txnList, ReconciliationSB reconciliationSB,ErpSaleHome saleHome) {

		ECheckRejectedTxDetail paymentTransaction = null; 
		ErpCaptureModel captureModel=null;
		Set<String> orders=new HashSet<String>();
		if (txnList != null && txnList.size() > 0) {
			Iterator<ECheckRejectedTxDetail> iter = txnList.iterator();
			while (iter.hasNext()) {
				try {
					paymentTransaction = (ECheckRejectedTxDetail) iter.next(); 
					String orderNumber = paymentTransaction.getOrderID();
					int index = orderNumber.indexOf("X");
					String saleId = (index > -1) ? orderNumber.substring(0, index) : orderNumber;
					//String accountNumber = paymentTransaction.getBankAccountNumber();
					ErpSaleEB erpSaleEB = saleHome.findByPrimaryKey(new PrimaryKey(saleId));
					captureModel=getMatchingCapture(paymentTransaction,erpSaleEB.getCaptures());
					String profileID =captureModel.getProfileID();
					double amount = captureModel.getAmount();
					String sequenceNumber = captureModel.getSequenceNumber();
					EnumPaymentResponse paymentResponse = getPaymentResponse(paymentTransaction);
					String description = paymentResponse.getDescription();
					int usageCode = 2;
					ErpAffiliate aff = ErpAffiliate.getAffiliateByMerchant(EnumCardType.ECP, captureModel.getMerchantId());
					if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ReconciliationSB)){
					ReconciliationService.getInstance().addSettlement(getSettlementModel(amount,captureModel.getCcNumLast4(),aff,sequenceNumber,paymentResponse,description), 
							saleId, 
							aff, 
							false);
					}else{
					reconciliationSB.addSettlement( getSettlementModel(amount,captureModel.getCcNumLast4(),aff,sequenceNumber,paymentResponse,description), 
													saleId, 
													aff, 
													false);
					}
					if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ReconciliationSB)){
					ReconciliationService.getInstance().processECPReturn(saleId, aff, captureModel.getCcNumLast4(), amount, sequenceNumber, paymentResponse, description, usageCode);
					}else{
					reconciliationSB.processECPReturn(saleId, aff, captureModel.getCcNumLast4(), amount, sequenceNumber, paymentResponse, description, usageCode);
					}
					FDCustomerManager.sendSettlementFailedEmail(saleId);
				} catch (Exception e) {
					LOGGER.error("ECheckSTFCronRunner.processFailedTransactions: Order ID = " +paymentTransaction.getOrderID()  + "-"+ e.getMessage());						
				}
			}
		}
			
	}
	
	private static ErpCaptureModel getMatchingCapture(ECheckRejectedTxDetail tx, List<ErpCaptureModel> captures){
		ErpCaptureModel matchedCapture=null;
		for(ErpCaptureModel capture:captures) {
			if(capture.getGatewayOrderID().equals(tx.getOrderID())) {
				matchedCapture=capture;
			}
		}
		return matchedCapture;
		
	}
	private static ErpSettlementModel getSettlementModel( double amount,String accountNumber, ErpAffiliate aff,String sequenceNumber,EnumPaymentResponse paymentResponse,String description) {
		ErpSettlementModel model=new ErpSettlementModel();
		model.setPaymentMethodType(EnumPaymentMethodType.ECHECK);
		model.setCardType(EnumCardType.ECP);
		model.setAmount(amount);
		model.setSequenceNumber(sequenceNumber);
		model.setResponseCode(paymentResponse);
		model.setTransactionSource(EnumTransactionSource.SYSTEM);			
		model.setDescription(description);
		model.setCcNumLast4(accountNumber.substring(accountNumber.length()-4));
		model.setAffiliate(aff);
		return model;
	}
	static private EnumPaymentResponse getPaymentResponse(ECheckRejectedTxDetail paymentTransaction) {
		
		EnumRestrictionReason restrictedReason = EnumRestrictionReason.GENERAL_ACCOUNT_PROBLEM;		
		EnumPaymentResponse paymentResponse = null;
		
		try {
			 if (!StringUtils.isEmpty(paymentTransaction.getRejectResponse())) {
				paymentResponse = EnumPaymentResponse.getEnum(paymentTransaction.getRejectResponse());
			}
			return paymentResponse;		
		} catch (Exception e) {
			LOGGER.error("ECheckSTFCronRunner.getPaymentResponse: " + e.getMessage());
		}
		return null;
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
			String subject="ECheckSTFCronRunner:	"+ (processDate != null ? dateFormatter.format(processDate) : " date error");

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("<body>");			
			
			if(exceptionMsg != null) {
				buff.append("Exception is :").append("\n");
				buff.append(exceptionMsg);
			}
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getCronFailureMailFrom(),
					ErpServicesProperties.getCronFailureMailTo(),ErpServicesProperties.getCronFailureMailCC(),
					subject, buff.toString(), true, "");
			
		}catch (MessagingException e) {
			LOGGER.warn("Error Sending ECheckSTFCronRunner report email: ", e);
		}
		
	}

}
