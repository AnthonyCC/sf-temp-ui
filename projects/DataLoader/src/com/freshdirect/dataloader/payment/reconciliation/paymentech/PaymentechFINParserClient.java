package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.CreateException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.reconciliation.SettlementBuilderI;
import com.freshdirect.dataloader.payment.reconciliation.SettlementLoaderUtil;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParserClient;
import com.freshdirect.fdlogistics.services.impl.FDCommerceService;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ewallet.ErpPPSettlementInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGCSettlementInfo;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.Money;
import com.freshdirect.payment.ejb.PayPalSettlementTransactionCodes;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.gateway.ewallet.impl.PayPalReconciliationSB;
import com.freshdirect.payment.gateway.impl.ReconciliationConstants;
import com.freshdirect.payment.model.EnumSummaryDetailType;
import com.freshdirect.payment.model.ErpSettlementInvoiceModel;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSettlementTransactionModel;
import com.freshdirect.payment.model.ErpSummaryDetailModel;
import com.freshdirect.payment.reconciliation.detail.CCDetailOne;
import com.freshdirect.payment.service.FDECommerceService;

public class PaymentechFINParserClient extends SettlementParserClient {
	
	private static final String RETURN_TRANSACTION = "06";
	private static final String PAYMENTECH_RETURN_TX_CC = "R";
	private static final String PAYMENTECH_RETURN_TX_CC_NEW = "RF";//Orbital reconciliation file uses RF for Credit card cashbacks.
	private static final String PAYMENTECH_RETURN_TX_EC = "N";
	private static final String PAYMENTECH_RETURN_TX_EC_NEW = "ER";//Orbital reconciliation file uses ER for Echeck cashbacks.
	private static final String PAYPAL_TRANSACTION_STATUS="C";
	
	private static final String PAYPAL_FEE_TX= "Fee";
	private List<String> settlementIds = null;
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechFINParserClient.class);
	
	private Date batchDate;
	private String batchNumber;
	private double netSales;
	private double netDeductions;
	private int adjustmentCount;
	private double adjustmentAmount;
	
	private Date ppDesiredDate;
	//GC net sales.
	private double gcNetSales;
	//Failed GC settlements
	private List<ErpGCSettlementInfo> failedGCSettlements = new ArrayList<ErpGCSettlementInfo>();
	private Set<String> processedSaleIds = new HashSet<String>();
	
	private ErpSettlementSummaryModel settlementSummary;
	
	public PaymentechFINParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB) {
		super(builder, reconciliationSB);
	}
	
	public PaymentechFINParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB, PayPalReconciliationSB ppReconcSB, List<String> settlementIds) {
		super(builder, reconciliationSB, ppReconcSB);
		this.settlementIds = settlementIds;
	}
	
	public PaymentechFINParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB, Date desiredDate) {
		super(builder, reconciliationSB);
		this.ppDesiredDate = desiredDate;
	}
	
	public PaymentechFINParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB, PayPalReconciliationSB ppReconcSB, Date desiredDate) {
		super(builder, reconciliationSB, ppReconcSB);
		this.ppDesiredDate = desiredDate;
	}
	
	public void process(DFRStart start) {
		this.settlementSummary = new ErpSettlementSummaryModel();
	}
	
	public void process(PaymentechHeader header) {
		if(EnumPaymentechRecordType.FIN0010_HEADER.equals(header.getRecordType())){
			this.batchDate = header.getGenerationDate();
			this.batchNumber = String.valueOf(header.getGenerationDate().getTime());
			this.settlementSummary.setBatchDate(header.getFromDate());
			this.settlementSummary.setProcessPeriodStart(header.getFromDate());
			this.settlementSummary.setProcessPeriodEnd(header.getToDate());
			this.settlementSummary.setProcessDate(header.getGenerationDate());
			this.settlementSummary.setDepositDate(header.getGenerationDate());
			this.settlementSummary.setSettlementFileDate(header.getGenerationDate());
		}
	}
	
	public void process(FIN0010DataRecord trans) {
		if(EnumPaymentechCategory.SALES.equals(trans.getCategory())){
			netSales += trans.getAmount();
		}else{
			netDeductions += trans.getAmount();
		}
	}
	
	public void process(ACT0033DataRecord trans){
		this.adjustmentCount += trans.getCount();
		this.adjustmentAmount += trans.getAmount();
		
	}
	
	public void process(LNK010ADataRecord trans) {
		System.out.println(trans);
	}
	
	public void process(FIN0011DataRecord trans) throws BadDataException {
		if(EnumPaymentechCategory.INTERCHANGE_ASSESSMENT_FEES.equals(trans.getCategory())){
			EnumCardType ccType = EnumCardType.getByPaymentechCode(trans.getMop());
			EnumSummaryDetailType sType = EnumSummaryDetailType.getByPaymentechCode(trans.getMop());
			if(ccType == null){
				throw new BadDataException("INTERCHANGED ASSESSMENT FEE received for unknown card type: "+trans.getMop());
			}
			
			ErpSummaryDetailModel m = new ErpSummaryDetailModel(sType);
			m.setNumberOfItems(trans.getCount());
			m.setNetAmount(trans.getAmount());
			
			this.settlementSummary.addSummaryDetail(m);
			this.builder.addCreditFee(ccType, trans.getTotalCharge());
			
		} else if(EnumPaymentechCategory.PAYMENTECH_FEES.equals(trans.getCategory())){
			ErpSettlementInvoiceModel invoice = new ErpSettlementInvoiceModel();
			invoice.setInvoiceDate(this.batchDate);
			invoice.setInvoiceAmount(trans.getTotalCharge());
			invoice.setInvoiceNumber(String.valueOf(trans.getFeeSchedule()));
			invoice.setDescription(trans.getFeeTypeDescription());
			
			this.settlementSummary.addInvoice(invoice);
			this.builder.addInvoice(invoice.getInvoiceAmount(), invoice.getDescription());
		} else {
			throw new BadDataException("Fee Recieved for Unexpected Category: "+trans.getCategory().getName());
		}
		
	}
	
	public void process(CCDetailOne trans) throws RemoteException, CreateException {
		
		EnumCardType ccType = EnumCardType.getByPaymentechCode(trans.getProductCode());

		if (ccType==null) {
			LOGGER.warn("Got a transaction for unsupported CC: "+trans.getProductCode());
			return;
		}
		
		String authId = trans.getAuthCode();
		String saleId = trans.getMerchantReferenceNumber();		
		String accountNumber = trans.getAccountNumber();
		double chargeAmount = trans.getTransactionAmount();
		String sequenceNumber = trans.getFDMSReferenceNumber();
		String txCode = trans.getTransactionCode();
		boolean refund = PAYMENTECH_RETURN_TX_CC.equals(txCode) || PAYMENTECH_RETURN_TX_EC.equals(txCode) || 
				RETURN_TRANSACTION.equals(txCode)||PAYMENTECH_RETURN_TX_CC_NEW.equals(txCode)|| PAYMENTECH_RETURN_TX_EC_NEW.equals(txCode);
		ErpAffiliate aff = ErpAffiliate.getAffiliateByTxDivision(trans.getMerchantNumber());
		
		ErpSettlementInfo info = this.reconciliationSB.processSettlement(saleId, aff, authId, accountNumber, Math.abs(chargeAmount), sequenceNumber, ccType, refund);
		
		if (info.isChargeSettlement()) {
			this.builder.addBounceCheckCharge(info, ccType, Math.abs(chargeAmount));			
		} else if (info.isSettlementFailedAfterSettled()) {
			builder.addPaymentRecharge(info, ccType, Math.abs(chargeAmount));			
		}else{
			this.builder.addChargeDetail(info, refund, Math.abs(chargeAmount), ccType);
		} 
		if(!processedSaleIds.contains(saleId) && !refund){
			//Process Gift card settlements only if that is sale is not processed and is not a refund(Fix for APPDEV-866).
			List gcSettlementInfos = this.reconciliationSB.processGCSettlement(saleId);
			appendGCSettlements(gcSettlementInfos);
			processedSaleIds.add(saleId);
		}
		
	}

	private void appendGCSettlements(List gcSettlementInfos) {
		if(gcSettlementInfos.size() > 0){
			for(Iterator iter = gcSettlementInfos.iterator() ; iter.hasNext();){
				ErpGCSettlementInfo gcInfo = (ErpGCSettlementInfo) iter.next();
				if(gcInfo.isPostAuthFailed())
					failedGCSettlements.add(gcInfo);
				else {
					addGCNetSales(gcInfo);
				}
				this.builder.addGCChargeDetail(gcInfo, EnumCardType.GCP);
			}
		}
	}

	private void appendPPSettlements(List ppSettlementInfos) {
		if(ppSettlementInfos.size() > 0){
			for(Iterator iter = ppSettlementInfos.iterator() ; iter.hasNext();){
				ErpPPSettlementInfo ppInfo = (ErpPPSettlementInfo) iter.next();
				if (ppInfo.getTxEventCode().equals(ReconciliationConstants.FEE_KEY)) {
					this.builder.addPPFeeDetail(ppInfo);
				} else if (ppInfo.getTxEventCode().equals(ReconciliationConstants.MISC_FEE_KEY)) {
					this.builder.addPPMiscFeeDetail(ppInfo);
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPSTLEventCode.getEnum(ppInfo.getTxEventCode())) {
					this.builder.addChargeDetail(ppInfo, false, ppInfo.getAmount(), EnumCardType.PAYPAL);
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPCBREventCode.getEnum(ppInfo.getTxEventCode())) {
					this.builder.addChargebackReversal(ppInfo, ppInfo.getAmount());
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPCBKEventCode.getEnum(ppInfo.getTxEventCode())) {
					this.builder.addChargeback(ppInfo, ppInfo.getAmount());
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPREFEventCode.getEnum(ppInfo.getTxEventCode())) {
					this.builder.addChargeDetail(ppInfo, true, ppInfo.getAmount(), EnumCardType.PAYPAL);
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPMiscFeeEventCode.getEnum(ppInfo.getTxEventCode())) {
					this.builder.addInvoice(ppInfo.getAmount(), getMiscFeeDescr(ppInfo.getTxEventCode()));
				} else {
					LOGGER.error("Unknown PayPal settlment event code identified " + ppInfo.getTxEventCode());
				}
			}
		}
	}
	
	private void appendFailedGCSettlements(List<ErpGCSettlementInfo> gcSettlementInfos) {
		if(gcSettlementInfos.size() > 0){
			for (ErpGCSettlementInfo gcInfo : gcSettlementInfos) {
				this.builder.addFailedGCSettlement(gcInfo, EnumCardType.GCP);
			}
		}
	}
	
	private void addGCNetSales(ErpGCSettlementInfo gcInfo) {
		if(!gcInfo.isPostAuthFailed())
			gcNetSales += gcInfo.getAmount();
	}
	
	public void process(DFREnd end) throws RemoteException {
		//Before adding header process settlement pending orders(Orders Paid with GC only).
		List gcSettlementInfos = this.reconciliationSB.processSettlementPendingOrders();
		appendGCSettlements(gcSettlementInfos);
		
		try {
			if (DataLoaderProperties.isPayPalSettlementEnabled()) {
				if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaypalReconciliationSB)) {
					List ppSettlementInfos = processPPSettlements(settlementIds);
					if (ppSettlementInfos != null)
						appendPPSettlements(ppSettlementInfos);
					else
						LOGGER.info("No PayPal records to be processed. Please check whether PayPalSettlementLoader is run");
					FDECommerceService.getInstance().updatePayPalStatus(settlementIds);
				} else if(this.ppReconSB!=null) {
					List ppSettlementInfos = processPPSettlements(settlementIds);
					if (ppSettlementInfos != null)
						appendPPSettlements(ppSettlementInfos);
					else
						LOGGER.info("No PayPal records to be processed. Please check whether PayPalSettlementLoader is run");
					ppReconSB.updatePayPalStatus(settlementIds);	
				}
				
			}
		} catch (RemoteException e) {
			LOGGER.fatal("Could not process PayPal transactions ", e);
			SettlementLoaderUtil.sendSettlementFailureEmail(e);
		} catch (CreateException e) {
			LOGGER.fatal("Could not process PayPal transactions ", e);
			SettlementLoaderUtil.sendSettlementFailureEmail(e);
		} catch (ErpTransactionException e) {
			LOGGER.fatal("Could not process PayPal transactions ", e);
			SettlementLoaderUtil.sendSettlementFailureEmail(e);
		}
		
		double netDeposit = this.netSales - Math.abs(this.netDeductions);
		this.settlementSummary.setBatchNumber(this.batchNumber);
		this.settlementSummary.setNetSaleAmount(netDeposit);
		this.settlementSummary.setNumberOfAdjustments(this.adjustmentCount);
		this.settlementSummary.setAdjustmentAmount(this.adjustmentAmount);
		this.builder.addHeader(this.batchDate, this.batchNumber, (Math.round(netDeposit * 100)) / 100.0);
		this.reconciliationSB.addSettlementSummary(this.settlementSummary);
		//After adding header append failed GC settlements.
		appendFailedGCSettlements(failedGCSettlements);
	}

	private String getMiscFeeDescr(String txEventCode) {
		//T0100, T0106, T0107, T1108
		if (txEventCode.equals("T0100")) {
			return "General non-payment fee of a type not belonging to the other T01xx categories";
		} else if (txEventCode.equals("T0106")) {
			return "Chargeback Processing Fee";
		} else if (txEventCode.equals("T0107")) {
			return "Payment Fee";
		} else if (txEventCode.equals("T1108")) {
			return "Fee Reversal";
		}
		return "NOT DEFINED";
	}
	
	/**
	 * This method settles trxns and also generates SettlementInfo for PayPal orders for SAP.
	 * @return
	 */
	public List<ErpPPSettlementInfo> processPPSettlements(List<String> ppStlmntIds) throws RemoteException, CreateException, ErpTransactionException {
	
		List<ErpSettlementSummaryModel> ppStlmntTrxns;
		List<ErpPPSettlementInfo> settlementInfos = new ArrayList<ErpPPSettlementInfo>();
		if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaypalReconciliationSB)) {
			ppStlmntTrxns = FDECommerceService.getInstance().getPPTrxns(ppStlmntIds);
		} else {
			ppStlmntTrxns = this.ppReconSB.getPPTrxns(ppStlmntIds);
		}
		if (ppStlmntTrxns == null) {
			return null;
		}
		
		StringBuffer missedTrasactionBufer=new StringBuffer();
		
		String gatewayOrderId = "";
		String saleId = "";
		int totalTrxns = processPPFee(ppStlmntTrxns, settlementInfos);
		if (totalTrxns == 0)
			return null;
		
		for (ErpSettlementSummaryModel summary : ppStlmntTrxns) {
			ErpAffiliate affiliate = getErpAffiliate(summary.getAffiliateAccountId());
			for (ErpSettlementTransactionModel trxn : summary.getSettlementTrxns()) {
				ErpSettlementModel model = new ErpSettlementModel();

				model.setAuthCode(null);
				model.setAmount(new Money(trxn.getGrossTransactionAmount()).getDollar());
				model.setCardType(EnumCardType.PAYPAL);
				model.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
				model.setTransactionSource(EnumTransactionSource.SYSTEM);
				model.setSequenceNumber(trxn.getPaypalReferenceId());
				gatewayOrderId = trxn.getGatewayOrderId();
				model.setGatewayOrderID(gatewayOrderId);

				if (gatewayOrderId != null) {
					saleId = gatewayOrderId.substring(0, gatewayOrderId.indexOf("X"));
				} else {
					throw new RuntimeException("PayPal settlement failed as Order Id is null ");
				}
				
				model.setProcessorTrxnId(trxn.getPaypalReferenceId());
				model.setAffiliate(affiliate);
				ErpSettlementInfo info = null;
				boolean isTrxnExecuted= false;
				if (null != PayPalSettlementTransactionCodes.EnumPPSTLEventCode.getEnum(trxn.getTransactionEventCode())){
					info = this.reconciliationSB.addSettlement(model, saleId, affiliate, false);
					isTrxnExecuted = true;
				}else if (null != PayPalSettlementTransactionCodes.EnumPPREFEventCode.getEnum(trxn.getTransactionEventCode())){
					info = this.reconciliationSB.addSettlement(model, saleId, affiliate, true);
					isTrxnExecuted = true;
				} else if (null != PayPalSettlementTransactionCodes.EnumPPCBKEventCode.getEnum(trxn.getTransactionEventCode())) {
					info = this.reconciliationSB.addChargeback(getChargebackModel(trxn, affiliate, trxn.getTransactionInitiationDate()));
					isTrxnExecuted = true;
				} else if (null != PayPalSettlementTransactionCodes.EnumPPCBREventCode.getEnum(trxn.getTransactionEventCode())) {
					info = this.reconciliationSB.addChargebackReversal(getChargebackReversalModel(trxn, affiliate, trxn.getTransactionInitiationDate()));
					isTrxnExecuted = true;
				} else {
					LOGGER.info("Transaction with event codes is not being update to FD DB" + trxn.getTransactionEventCode());
					missedTrasactionBufer.append(gatewayOrderId+",").append("\n");
				}
				
				if(isTrxnExecuted){
					if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.PaypalReconciliationSB)) {
						FDECommerceService.getInstance().updatePPSettlementTransStatus(trxn.getId());
					} else {
						this.ppReconSB.updatePPSettlementTransStatus(trxn.getId());
					}
				}
				
				try {
					ErpPPSettlementInfo ppInfo = new ErpPPSettlementInfo(info.getInvoiceNumber(), affiliate);
					if (info.getId() != null)
						ppInfo.setId(info.getId());
					ppInfo.setAmount(new Money(trxn.getGrossTransactionAmount()).getDollar());
					ppInfo.setChargeSettlement(info.isChargeSettlement());
					ppInfo.setSplitTransaction(info.hasSplitTransaction());
					ppInfo.setsettlementFailedAfterSettled(info.isSettlementFailedAfterSettled());
					ppInfo.setTransactionCount(info.getTransactionCount());
					ppInfo.setTxEventCode(trxn.getTransactionEventCode());
					ppInfo.setCardType(info.getCardType());
					settlementInfos.add(ppInfo);
				} catch (Exception e) {
					// Not expecting as of now
					missedTrasactionBufer.append(gatewayOrderId+",").append("\n");
					LOGGER.error("[PayPal Batch]", e);
				}
			}
		}

		// send email communication with list of paypal transaction was not processed  to APP support 
		
		if(!missedTrasactionBufer.toString().isEmpty()){
			SettlementLoaderUtil.sendEmailNotification("List of Not Processed PayPal Transaction settlement  GatewayOrderId", missedTrasactionBufer.toString(),null);

		}
		return settlementInfos;
	}
	
	private ErpChargebackModel getChargebackModel(ErpSettlementTransactionModel trxn, ErpAffiliate affiliate, Date date) {
		ErpChargebackModel cbModel = new ErpChargebackModel();
		populateChargeBack(cbModel, trxn, affiliate, date);
		return cbModel;
	}
	
	private ErpChargebackReversalModel getChargebackReversalModel(ErpSettlementTransactionModel trxn, ErpAffiliate affiliate, Date date) {
		ErpChargebackReversalModel cbModel = new ErpChargebackReversalModel();
		populateChargeBack(cbModel, trxn, affiliate, date);
		return cbModel;
	}
	
	private void populateChargeBack(ErpChargebackModel cbModel, ErpSettlementTransactionModel trxn, ErpAffiliate affiliate, Date date) {
		cbModel.setAffiliate(affiliate);
		cbModel.setAmount(new Money(trxn.getGrossTransactionAmount()).getDollar());
		cbModel.setCardType(EnumCardType.PAYPAL);
		cbModel.setBatchDate(date);
		String ppRefId = trxn.getPaypalReferenceId();
		String cbRefId = ppRefId != null ? ppRefId.substring(0, 14) : "";
		cbModel.setCbkReferenceNumber(cbRefId);
		cbModel.setCcNumLast4("1111");
		cbModel.setCbkRespondDate(date);
		cbModel.setCbkWorkDate(date);
		cbModel.setGatewayOrderID(trxn.getGatewayOrderId());
		cbModel.setMerchantReferenceNumber(trxn.getOrderId());
		cbModel.setPaymentMethodType(EnumPaymentMethodType.PAYPAL);
		cbModel.setTransactionSource(EnumTransactionSource.SYSTEM);
		cbModel.setOriginalTxDate(date);
		// FIN-21
		if(PAYPAL_TRANSACTION_STATUS.equalsIgnoreCase(trxn.getStatus())){
			cbModel.setTrxnComplete(true);

		}
	}
	
	private ErpAffiliate getErpAffiliate(String accountId) {
		if (ErpServicesProperties.getPPFDAccountIds().contains(accountId)) {
			return ErpAffiliate.getEnum(ErpAffiliate.CODE_FD);
		} else if (ErpServicesProperties.getPPFDWAccountIds().contains(accountId)) {
			return ErpAffiliate.getEnum(ErpAffiliate.CODE_FDW);
		} else {
			throw new RuntimeException("Unknown ErpAffiliate identified " + accountId);
		}
	}
	
	public int processPPFee(List<ErpSettlementSummaryModel> stlmntTrxns, List<ErpPPSettlementInfo> settlementInfos) {
		long txFee = 0;
		long miscFee = 0;
		int totalTrxns = 0;
		for (ErpSettlementSummaryModel summary : stlmntTrxns) {
			for (ErpSettlementTransactionModel tx : summary.getSettlementTrxns()) {
				totalTrxns++;
				if (null !=PayPalSettlementTransactionCodes.EnumPPSTLEventCode.getEnum(tx.getTransactionEventCode())){//ErpServicesProperties.getPPSTLEventCodes().contains(tx.getTransactionEventCode())) {
					txFee += tx.getFeeAmount();
				} else if (null !=PayPalSettlementTransactionCodes.EnumPPSTFEventCode.getEnum(tx.getTransactionEventCode()) ||
						null !=PayPalSettlementTransactionCodes.EnumPPCBKEventCode.getEnum(tx.getTransactionEventCode()) ||
								null !=PayPalSettlementTransactionCodes.EnumPPREFEventCode.getEnum(tx.getTransactionEventCode())) {
					miscFee += tx.getFeeAmount();
				}
			}
		}
		
		if (totalTrxns <= 0)
			return 0;
		ErpPPSettlementInfo txFeeInfo = new ErpPPSettlementInfo("FeeTrxnNOInvoice", ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
		//As part of APPDEV-5531  accumulate misc and transaction fee and send to SAP
		if(miscFee>0){
			txFee=txFee+miscFee;
		}
		txFeeInfo.setAmount(new Money(txFee).getDollar());
		txFeeInfo.setTxEventCode(ReconciliationConstants.FEE_KEY);
		if (txFee < 0)
			LOGGER.error("Unexpected Tx fee in settlement ");
		else
			settlementInfos.add(txFeeInfo);
		
		// As part of APPDEV-5531 : No need to send Misc fee to SAP 
		/*
		ErpPPSettlementInfo miscFeeInfo = new ErpPPSettlementInfo("FeeTrxnNOInvoice", ErpAffiliate.getEnum(ErpAffiliate.CODE_FD));
		miscFeeInfo.setAmount(new Money(miscFee).getDollar());
		miscFeeInfo.setTxEventCode(ReconciliationConstants.MISC_FEE_KEY);
		if (miscFee < 0)
			LOGGER.error("Unexpected Misc fee in settlement ");
		else if (miscFee > 0)
			settlementInfos.add(miscFeeInfo);
		*/
		
		return totalTrxns;
	}
}
