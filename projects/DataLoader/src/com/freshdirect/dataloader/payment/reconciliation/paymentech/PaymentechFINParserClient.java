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

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.payment.reconciliation.SettlementBuilderI;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParserClient;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGCSettlementInfo;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.model.EnumSummaryDetailType;
import com.freshdirect.payment.model.ErpSettlementInvoiceModel;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSummaryDetailModel;
import com.freshdirect.payment.reconciliation.detail.CCDetailOne;

public class PaymentechFINParserClient extends SettlementParserClient {
	
	private static final String RETURN_TRANSACTION = "06";
	private static final String PAYMENTECH_RETURN_TX_CC = "R";
	private static final String PAYMENTECH_RETURN_TX_EC = "N";
	
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechFINParserClient.class);
	
	private Date batchDate;
	private String batchNumber;
	private double netSales;
	private double netDeductions;
	private int adjustmentCount;
	private double adjustmentAmount;
	//GC net sales.
	private double gcNetSales;
	//Failed GC settlements
	private List failedGCSettlements = new ArrayList<ErpGCSettlementInfo>();
	private Set<String> processedSaleIds = new HashSet<String>();
	
	private ErpSettlementSummaryModel settlementSummary;
	
	public PaymentechFINParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB) {
		super(builder, reconciliationSB);
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
		boolean refund = PAYMENTECH_RETURN_TX_CC.equals(txCode) || PAYMENTECH_RETURN_TX_EC.equals(txCode) || RETURN_TRANSACTION.equals(txCode);
		ErpAffiliate aff = ErpAffiliate.getAffiliateByTxDivision(trans.getMerchantNumber());
		
		ErpSettlementInfo info = this.reconciliationSB.processSettlement(saleId, aff, authId, accountNumber, Math.abs(chargeAmount), sequenceNumber, ccType, refund);
		
		if (info.isChargeSettlement()) {
			this.builder.addBounceCheckCharge(info, ccType, Math.abs(chargeAmount));			
		} else if (info.isSettlementFailedAfterSettled()) {
			builder.addPaymentRecharge(info, ccType, Math.abs(chargeAmount));			
		}else{
			this.builder.addChargeDetail(info, refund, Math.abs(chargeAmount), ccType);
		} 
		if(!processedSaleIds.contains(saleId)){
			//Process Gift card settlements only if that is sale is not processed and is not a refund.
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

	private void appendFailedGCSettlements(List gcSettlementInfos) {
		if(gcSettlementInfos.size() > 0){
			for(Iterator iter = gcSettlementInfos.iterator() ; iter.hasNext();){
				ErpGCSettlementInfo gcInfo = (ErpGCSettlementInfo) iter.next();
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

}
