/*
 * Created on Aug 8, 2005
 */
package com.freshdirect.dataloader.payment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.reconciliation.SapFileBuilder;
import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.ecomm.gateway.ReconciliationService;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.reconciliation.detail.CCDetailOne;

/**
 * @author jng
 */
public class ECPSettlementCronRunner {

	private final static int NUM_DAYS_BACK_DEFAULT = 3;  // default to 3 days back 
	private final static Category LOGGER = LoggerFactory.getInstance(ECPSettlementCronRunner.class);

	private static final SimpleDateFormat SF = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
	
	public static void main(String[] args) {
		
		Date startDate = DateUtil.truncate(new Date());
		int numDaysBack = NUM_DAYS_BACK_DEFAULT;
		int maxNumSales = -1; // defaul to all
		boolean sendToSap = true;
		String inputFileName = null;
		
		if (args.length >= 1) {
			for (String arg : args) {
				try { 
					if (arg.startsWith("numDaysBack=")) {
							numDaysBack = Integer.parseInt(arg.substring("numDaysBack=".length()));
					} else if (arg.startsWith("maxNumSales=")) {
						maxNumSales = Integer.parseInt(arg.substring("maxNumSales=".length()));
					} else if (arg.startsWith("sendToSap=")) {
						
						sendToSap = Boolean.valueOf(arg.substring("sendToSap=".length())).booleanValue(); 
					} else if (arg.startsWith("inputFileName=")) {
						inputFileName = arg.substring("inputFileName=".length()); 
					}
				} catch (Exception e) {
					System.err.println("Usage: java com.freshdirect.dataloader.payment.ECPSettlementCronRunner [numDaysBack={int value}] [maxNumSales={int value}] [sendToSap={true | false}] [ignorePaymentRecharge={true | false}] [inputFileName={filename of sale ids(one per line)}] ");
					System.exit(-1);
				}
			}
		}
		
		LOGGER.warn("ECPSettlementCronRunner.main: " + " numDaysBack=" + numDaysBack + ", maxNumSales=" + maxNumSales + ", sendToSap=" +sendToSap +
				", inputFileName=" + inputFileName);						
		
		
		try {
			List txnList = null;
			
			ReconciliationSB reconciliationSB = SettlementLoaderUtil.lookupReconciliationHome().create();
			
			// process settlement transactions
			if (inputFileName != null) {
				LOGGER.debug("ECPSettlementCronRunner.main:  Loading sale ids in file " + inputFileName);						
				List<String> saleIds = getSaleIdsFromFile(inputFileName);
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ReconciliationSB)){
					txnList = ReconciliationService.getInstance().loadReadyToSettleECPSales(saleIds);
					}else{
					txnList = reconciliationSB.loadReadyToSettleECPSales(saleIds);
					}
			} else {
				startDate = calcWeekDaysBack(startDate, numDaysBack);			
				LOGGER.debug("ECPSettlementCronRunner.main:  Loading Ready to Settle ECP Sales with capture date <= " + startDate);						
				if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ReconciliationSB)){
				txnList = ReconciliationService.getInstance().loadReadyToSettleECPSales(startDate, maxNumSales);
				}else{
				txnList = reconciliationSB.loadReadyToSettleECPSales(startDate, maxNumSales);
				}
			}
			LOGGER.debug("ECPSettlementCronRunner.main:  Settling " + txnList.size() + " ECP Sales");						
			String fileName = settleECPSales(txnList, reconciliationSB);
			if (fileName != null && sendToSap) {
				LOGGER.debug("ECPSettlementCronRunner.main:  Uploading file " + fileName + " to SAP");						
				SettlementLoaderUtil.uploadFileToSap(fileName);
				LOGGER.debug("ECPSettlementCronRunner.main:  making the BAPI call");
				SettlementLoaderUtil.callSettlementBapi(fileName);
			}
		} catch (Exception e) {
			LOGGER.fatal("Failed to load ECPSettlement", e);
			SettlementLoaderUtil.sendSettlementFailureEmail(e);
		}
	}

	private static Date calcWeekDaysBack(Date startDate, int numDaysBack) {
		
		Date date = new Date(startDate.getTime());
		int curDay = 0;
		while (curDay < numDaysBack) {			
			Calendar cal = DateUtil.toCalendar(date);
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				curDay++;
			}
			date = DateUtil.addDays(date, -1); // go back a day and retry
		}
		return date;
	}
	
	private static String settleECPSales(List txnList, ReconciliationSB reconciliationSB) throws IOException {

		String fileName = null;
		if (txnList != null && txnList.size() > 0) {
			SapFileBuilder builder = new SapFileBuilder();
			
			EnumCardType ccType = EnumCardType.ECP;
			Iterator iter = txnList.iterator();
			CCDetailOne txn = null;
			while (iter.hasNext()) {
				try {
					txn = (CCDetailOne) iter.next(); 
					String authId = txn.getAuthCode();
					String saleId = txn.getMerchantReferenceNumber();		
					String accountNumber = txn.getAccountNumber();
					double chargeAmount = txn.getTransactionAmount();
					String sequenceNumber = txn.getFDMSReferenceNumber();
					
					//We are just getting the primary affiliate because this CRON will be run for 3 days to 
					//handle pre-BC split ECP tranaction which would have been againg FD anyway
					//This will be turned off between 3 days - week.
					ErpAffiliate aff = ErpAffiliate.getPrimaryAffiliate();
					ErpSettlementInfo info;
					if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ReconciliationSB)){
					 info = ReconciliationService.getInstance().processSettlement(saleId, aff, authId, accountNumber, chargeAmount, sequenceNumber, ccType, false);;
							}else{
					info = reconciliationSB.processSettlement(saleId, aff, authId, accountNumber, chargeAmount, sequenceNumber, ccType, false);
							}
					boolean isChargeSettlement;
					if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ReconciliationSB)){
					isChargeSettlement = 	ReconciliationService.getInstance().isChargeSettlement(saleId, chargeAmount);
							}else{
					 isChargeSettlement = reconciliationSB.isChargeSettlement(saleId, chargeAmount);
							}
					
					boolean isSettlementFailedAfterSettled;
					if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ReconciliationSB)){
					isSettlementFailedAfterSettled = ReconciliationService.getInstance().isSettlementFailedAfterSettled(saleId);
							}else{
					isSettlementFailedAfterSettled = reconciliationSB.isSettlementFailedAfterSettled(saleId);
							}
					if (!isChargeSettlement && !isSettlementFailedAfterSettled) {
						builder.addChargeDetail(info, false, Math.abs(chargeAmount), ccType);
					} else if (isChargeSettlement) {
						builder.addBounceCheckCharge(info, ccType, Math.abs(chargeAmount));			
					} else if (isSettlementFailedAfterSettled) {
						builder.addPaymentRecharge(info, ccType, Math.abs(chargeAmount));
					}
					LOGGER.debug("ECPSettlementCronRunner.settleECPSales:  order number "+ ((txn != null) ? txn.getMerchantReferenceNumber(): " Unknown "));						
				} catch (Exception e) {
					LOGGER.error("ECPSettlementCronRunner.settleECPSales:  order number "+ ((txn != null) ? txn.getMerchantReferenceNumber(): " Unknown ") + " " + e.getMessage());						
				}
			}
			fileName = DataLoaderProperties.getSapFileNamePrefix() + "_ECPSettlement_" + SF.format(new Date()) + ".txt";

			File f = new File(DataLoaderProperties.getWorkingDir() + fileName);
			builder.writeTo(f);
			
		}

		return fileName;
			
	}

	private static List<String> getSaleIdsFromFile(String inputFileName) throws FileNotFoundException, IOException {

		List<String> saleIds = new ArrayList<String>();
		File file = new File(inputFileName);
		FileInputStream ifs = new FileInputStream(file);
		BufferedReader bfr = new BufferedReader(new InputStreamReader(ifs));
		
		String saleId = null;
		while ((saleId = bfr.readLine()) != null) {
			saleIds.add(saleId.trim());
		}
		
		bfr.close();
		
		return saleIds;
	}

}
