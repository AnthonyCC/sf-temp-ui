package com.freshdirect.dataloader.payment.reconciliation.paypal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.log4j.Category;

import com.freshdirect.dataloader.DataLoaderProperties;
import com.freshdirect.dataloader.payment.reconciliation.SettlementBuilderI;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParserClient;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.PaymentechFINParserClient;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.EnumPaymentMethodType;
import com.freshdirect.payment.ejb.PayPalSettlementTransactionCodes;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.gateway.ewallet.impl.PayPalReconciliationSB;
import com.freshdirect.payment.model.EnumSummaryDetailType;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSettlementTransactionModel;
import com.freshdirect.payment.model.ErpSummaryDetailModel;

public class PayPalSettlementParserClient extends SettlementParserClient {
	
	private static final Category LOGGER = LoggerFactory.getInstance(PayPalSettlementParserClient.class);
	
	private Date batchDate;
	private String batchNumber;

	private List<ErpSettlementTransactionModel> settlements = new ArrayList<ErpSettlementTransactionModel>();
	
	private ErpSettlementSummaryModel[] settlementSummarys;
	private List<ErpSummaryDetailModel> settlementDetails = null;
	private int currSection = 0;
	private int currSectionRowCnt = 0;
	private int currFileRowCnt = 0;
	private int currReportRowCnt = 0;
	private String currAccountId = null;
	private int currFile = 0;
	
	private long currSectionTotalTransactionCredits = 0;
	private long currSectionTotalTransactionDebits = 0;
	private long currSectionTotalFeeCredits = 0;
	private long currSectionTotalFeeDebits = 0;
	private List<String> settlementIds = null;
	
	private boolean ignoreLock = false;
		
	public PayPalSettlementParserClient(SettlementBuilderI builder,ReconciliationSB reconSB,
			PayPalReconciliationSB ppReconSB, List<String> settlementIds) {
		super(builder, reconSB, ppReconSB);
		settlementSummarys = new ErpSettlementSummaryModel[2];
		settlementDetails = new ArrayList<ErpSummaryDetailModel>();
		this.settlementIds = settlementIds;
	}

	public void process(ReportHeaderDataRecord record) {
		this.batchDate = record.getReportGenerationDate();
		this.batchNumber = "" + batchDate.getTime();
	}
	
	public void process(FileHeaderDataRecord record) {
		//File number may not be required
		currFile = record.getFileNo();
		LOGGER.debug("File that is being processed " + currFile);
	}
	
	public void process(SectionHeaderDataRecord record) {
		ErpSettlementSummaryModel model = new ErpSettlementSummaryModel();
		model.setBatchDate(batchDate);
		model.setBatchNumber(batchNumber);
		model.setProcessPeriodStart(record.getReportPeriodStartDate());
		model.setProcessPeriodEnd(record.getReportPeriodEndDate());
		model.setAffiliateAccountId(record.getAccountId());
		model.setProcessDate(record.getReportPeriodEndDate());
		model.setDepositDate(record.getReportPeriodEndDate());
		model.setSettlementFileDate(record.getReportPeriodEndDate());
		model.setSettlementSource(EnumPaymentMethodType.PAYPAL.getName());
		
		settlementSummarys[currSection] = model;
		currAccountId = record.getAccountId();
	}
	
	public void process(ColumnHeaderDataRecord record) {
		//Intentionally ignored. 
	}
	
	public void process(SectionBodyDataRecord record) {
		ErpSettlementTransactionModel model = new ErpSettlementTransactionModel();

		String txEventCode = record.getTransactionEventCode();
		if (null == PayPalSettlementTransactionCodes.EnumPPIgnoreableEventCode.getEnum(txEventCode)) {
			model.setTransactionId(record.getTransactionId());
			model.setGatewayOrderId(record.getInvoiceId());
			model.setPaypalReferenceId(record.getPaypalReferenceId());
			model.setPaypalReferenceIdType(record.getPaypalReferenceIdType());
			model.setTransactionEventCode(record.getTransactionEventCode());
			model.setTransactionInitiationDate(record.getTransactionInitiationDate());
			model.setTransactionCompletionDate(record.getTransactionCompletionDate());
			model.setTransactionDebitOrCredit(record.getTransactionDebitOrCredit());
			model.setGrossTransactionAmount(record.getGrossTransactionAmount());
			model.setGrossTransactionCurrency(record.getGrossTransactionCurrency());
			model.setFeeDebitOrCredit(record.getFeeDebitOrCredit());
			model.setFeeAmount(record.getFeeAmount());
			model.setFeeCurrency(record.getFeeCurrency());
			model.setConsumerId(record.getConsumerId());
			model.setPaymentTrackingId(record.getPaymentTrackingId());
			model.setCustomField(record.getCustomField());
			model.setStoreId(record.getStoreId());
			model.setCreditTransactionalFee(record.getCreditTransactionalFee());
			model.setCreditPromotionalFee(record.getCreditPromotionalFee());
			model.setCreditTerm(record.getCreditTerm());
			settlements.add(model);
		}
		if (record.getTransactionDebitOrCredit().equals("CR")) {
			currSectionTotalTransactionCredits += record.getGrossTransactionAmount();
		}
		else if (record.getTransactionDebitOrCredit().equals("DR")) {
			currSectionTotalTransactionDebits += record.getGrossTransactionAmount();
		}

		if (record.getFeeDebitOrCredit().equals("CR")) {
			currSectionTotalFeeCredits += record.getFeeAmount();
		} else if (record.getFeeDebitOrCredit().equals("DR")) {
			currSectionTotalFeeDebits += record.getFeeAmount();
		}
		
		if (null == PayPalSettlementTransactionCodes.EnumPPIgnoreableEventCode.getEnum(txEventCode)) {
			if (record.getInvoiceId() == null || StringUtil.isEmpty(record.getInvoiceId())) {				
				throw new RuntimeException("Unrecognized order id " + record.getInvoiceId());
			} else {
				String orderId = record.getInvoiceId().substring(0, record.getInvoiceId().indexOf("X"));
				ErpSummaryDetailModel detailModel = new ErpSummaryDetailModel(EnumSummaryDetailType.PAYPAL);
				detailModel.setNetAmount(new Money(record.getGrossTransactionAmount()).getDollar());
				detailModel.setTransactionFees(new Money(record.getFeeAmount()).getDollar());
				detailModel.setPmType(EnumPaymentMethodType.PAYPAL);
				settlementDetails.add(detailModel);
			}
		}

		currSectionRowCnt++;
		currFileRowCnt++;
		currReportRowCnt++;
	}
	
	public void process(SectionFooterDataRecord record) {
		if (currSectionRowCnt == 0) {
			LOGGER.info("Atleast 1 of the sections is empty)");
			//settlementSummarys[currSection] = null;
			currSectionTotalTransactionCredits = 0;
			currSectionTotalTransactionDebits = 0;
			currSectionTotalFeeCredits = 0;
			currSectionTotalFeeDebits = 0;
			return;
		}
		//payable, available and total balances are being ignored.
		long totalCredits = record.getTotalGrossAmountCredits();
		long totalDebits = record.getTotalGrossAmountDebits();
		Money netSales = new Money(totalCredits - totalDebits);
		
		long trxnFeeCredits = record.getTotalTransactionFeeCredits();
		long trxnFeeDebits = record.getTotalTransactionFeeDebits();
		settlementSummarys[currSection].setTotalGrossCredit(totalCredits);
		settlementSummarys[currSection].setTotalGrossDebit(totalDebits);
		settlementSummarys[currSection].setNetSalesAmount(netSales.getDollar());
		settlementSummarys[currSection].setTotalTransactionFeeCredit(trxnFeeCredits);
		settlementSummarys[currSection].setTotalTransactionFeeDebit(trxnFeeDebits);
		settlementSummarys[currSection].setSettlementSource(EnumPaymentMethodType.PAYPAL.getName());

		settlementSummarys[currSection].setSettlementTrxns(settlements);
		settlementSummarys[currSection].setSummaryDetails(settlementDetails);
		settlements = new ArrayList<ErpSettlementTransactionModel>();
		settlementDetails = new ArrayList<ErpSummaryDetailModel>();
		if (totalCredits != currSectionTotalTransactionCredits ||
				totalDebits != currSectionTotalTransactionDebits ||
				record.getTotalTransactionFeeCredits() != currSectionTotalFeeCredits){ 
				// APPDEV-5531 || record.getTotalTransactionFeeDebits() != currSectionTotalFeeDebits) {
			StringBuffer buffer = new StringBuffer(300);
			buffer.append("PayPal :: Amounts of Section with Account Id ");
			buffer.append(currAccountId );
			buffer.append("\n");
			buffer.append(" Total Current section Trxn Credits = ");
			buffer.append(currSectionTotalTransactionCredits);
			buffer.append("\n");
			buffer.append(" Section Footer Trxn Credits = ");
			buffer.append(totalCredits);
			buffer.append("\n");
			buffer.append(" Total Current section Fee Credits = ");
			buffer.append(currSectionTotalFeeCredits);
			buffer.append("\n");
			buffer.append(" Section Footer Fee Credits = ");
			buffer.append(currSectionTotalTransactionCredits);
			buffer.append("\n");
			buffer.append(" Total Current section Trxn Debits = ");
			buffer.append(currSectionTotalTransactionDebits);
			buffer.append("\n");
			buffer.append(" Section Footer Trxn Debits = " + totalDebits);
			buffer.append("\n");
			buffer.append(" Total Current section Fee Debits = ");
			buffer.append(currSectionTotalFeeDebits);
			buffer.append("\n");
			buffer.append(" Section Footer Fee Debits = ");
			buffer.append(currSectionTotalTransactionDebits);
			buffer.append("\n");
			buffer.append("do not match with Section Footer amounts. "); 
			buffer.append(" Settlement File of " + batchDate + " may be corrupted");
			String msg = buffer.toString();

			LOGGER.error(msg);
			//throw new FDRuntimeException(msg); //To fix temporarily paypal issue
		}
		
		currSectionTotalTransactionCredits = 0;
		currSectionTotalTransactionDebits = 0;
		currSectionTotalFeeCredits = 0;
		currSectionTotalFeeDebits = 0;
	}
	
	public void process(SectionRecordCntDataRecord record) {
		StringBuffer buffer = new StringBuffer(300);
		buffer.append("PayPal :: Rows of Section ");
		buffer.append(currAccountId);
		buffer.append(" do not match. Settlement File of ");
		buffer.append(batchDate);
		buffer.append(" Section ");
		buffer.append(currSection);
		buffer.append(" may be corrupted.\n");
		String msg = buffer.toString();
		if (!(currSectionRowCnt == record.getRowCount())) {
			LOGGER.error(msg);
			//throw new FDRuntimeException(msg); //To fix temporarily paypal issue
		}
		currSectionRowCnt = 0;
		currSection++;
	}
	
	public void process(ReportFooterDataRecord record) {
		long totalSectionCredits = 0;
		long totalSectionDebits = 0;
		long totalSectionFeeCredits = 0;
		long totalSectionFeeDebits = 0;
		for (ErpSettlementSummaryModel summary : settlementSummarys) {
			if (summary == null)
				continue;
			totalSectionCredits += summary.getTotalGrossCredit();
			totalSectionDebits += summary.getTotalGrossDebit();
			totalSectionFeeCredits += summary.getTotalTransactionFeeCredit();
			totalSectionFeeDebits += summary.getTotalTransactionFeeDebit();
		}
		
		if (totalSectionCredits != record.getTotalGrossAmountCredits() ||
				totalSectionDebits != record.getTotalGrossAmountDebits() ||
				totalSectionFeeCredits != record.getTotalTransactionFeeCredits() ||
				totalSectionFeeDebits != record.getTotalTransactionFeeDebits()) {
			StringBuffer buffer = new StringBuffer(300);
			buffer.append("PayPal :: Amounts of Sections ");
			buffer.append(" do not match with Report total. Settlement File of ");
			buffer.append(batchDate);
			buffer.append(" may be corrupted");
			String msg = buffer.toString();
			LOGGER.error(msg);
			//throw new FDRuntimeException(msg); //To fix temporarily paypal issue
		}
	}
	
	public void process(ReportRecordCntDataRecord record) {
	
	 // START APPDEV-5531 
		if (record.getRowCount() > 0){
    	 
			if (!(currReportRowCnt == record.getRowCount())) {
				LOGGER.warn("Rows of Report " + currAccountId +
						" do not match with Sections. Settlement File of " + batchDate + " " + "may be corrupted");
			}
			
			try {
				settlementIds = this.ppReconSB.addPPSettlementSummary(settlementSummarys);
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
				
			// END APPDEV-5531 
		} else if (record.getRowCount() == 0) {
			
			StringBuilder msg= new StringBuilder(300);
			msg.append("PayPal Reconciliation file does not have any data at this time.\n");
			msg.append("Please verify that the settlement batch was submitted for the previous day.");
			msg.append("If settlement batch was submitted the previous day, and it contained orders,\n");
			msg.append("Please re-run the settlement loader job ");
			LOGGER.error(msg);
			throw new FDRuntimeException(msg.toString());
		}
	}
	
	public void process(FileFooterDataRecord record) {
		if (!(currFileRowCnt == record.getRowCount())) {
			StringBuilder msg= new StringBuilder(300);
			msg.append("PayPal :: Rows of File ");
			msg.append(currFile);
			msg.append(" do not match with Sections. Settlement File of ");
			msg.append("batchDate");
			msg.append(" may be corrupted.");
			LOGGER.error(msg);
			//throw new FDRuntimeException(msg); //To fix temporarily paypal issue
		}
		currFileRowCnt = 0;
	}
}
