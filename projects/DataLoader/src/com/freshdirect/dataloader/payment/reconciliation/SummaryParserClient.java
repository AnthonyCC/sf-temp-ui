package com.freshdirect.dataloader.payment.reconciliation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.CreateException;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackResponse;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.model.EnumSummaryDetailType;
import com.freshdirect.payment.model.ErpSettlementInvoiceModel;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSummaryDetailModel;
import com.freshdirect.payment.reconciliation.summary.Adjustment;
import com.freshdirect.payment.reconciliation.summary.AdjustmentAddendum;
import com.freshdirect.payment.reconciliation.summary.AmexJCBSummary;
import com.freshdirect.payment.reconciliation.summary.AmexJCBUnbundled;
import com.freshdirect.payment.reconciliation.summary.Chargeback;
import com.freshdirect.payment.reconciliation.summary.ChargebackAddendum;
import com.freshdirect.payment.reconciliation.summary.DinersClubCarteBlancheSummary;
import com.freshdirect.payment.reconciliation.summary.DinersClubCarteBlancheUnbundled;
import com.freshdirect.payment.reconciliation.summary.FileHeader;
import com.freshdirect.payment.reconciliation.summary.FileTrailer;
import com.freshdirect.payment.reconciliation.summary.Invoice;
import com.freshdirect.payment.reconciliation.summary.NovusSummary;
import com.freshdirect.payment.reconciliation.summary.NovusUnbundled;
import com.freshdirect.payment.reconciliation.summary.SummaryHeader;
import com.freshdirect.payment.reconciliation.summary.VisaMCSummary;
import com.freshdirect.payment.reconciliation.summary.VisaMCUnbundled;


class SummaryParserClient extends SettlementParserClient {
	
	private final static String CHARGEBACK_REVERSAL_CODE = "RV";

	public SummaryParserClient(SettlementBuilderI builder, ReconciliationSB reconSB) {
		super(builder, reconSB);
	}

	private Date batchDate = null;
	private String batchNumber = "";

	private ErpSettlementSummaryModel settlementSummary = null;
	private ErpSummaryDetailModel visaModel = null;
	private ErpSummaryDetailModel mcModel = null;
	private ErpSummaryDetailModel amexModel = null;
	private ErpSummaryDetailModel jcbModel = null;
	private ErpSummaryDetailModel dcModel = null;
	private ErpSummaryDetailModel cbModel = null;
	private ErpSummaryDetailModel novusModel = null;

	private Chargeback chgBackTx;

	private List invoices = new ArrayList();
	private ErpAdjustmentModel adjustmentModel = null;

	public void process(SummaryHeader trans) {
		System.out.println(trans.toString());
		this.settlementSummary.setBatchDate(trans.getBatchDate());
		this.settlementSummary.setBatchNumber(trans.getBatchNumber());
		this.settlementSummary.setProcessDate(trans.getProcessDate());
		this.settlementSummary.setDepositDate(trans.getDepositDate());
		this.settlementSummary.setNetSaleAmount(trans.getNetSalesAmount());
		this.settlementSummary.setNumberOfAdjustments(trans.getNumberOfAdjustments());
		this.settlementSummary.setAdjustmentAmount(trans.getAdjustmentAmount());
		this.settlementSummary.setSettlementFileDate(trans.getSettlementFileCreationDate());

		this.batchDate = trans.getBatchDate();
		this.batchNumber = trans.getBatchNumber();
	}

	public void process(VisaMCSummary trans) {
		System.out.println(trans.toString());
		this.visaModel = new ErpSummaryDetailModel(EnumSummaryDetailType.VISA);
		this.visaModel.setNumberOfItems(trans.getNumberOfVisaItems());
		this.visaModel.setNetAmount(trans.getVisaNetSalesAmount());

		this.builder.addCreditFee(EnumCardType.VISA, trans.getVisaDiscountAmount());

		this.mcModel = new ErpSummaryDetailModel(EnumSummaryDetailType.MASTERCARD);
		this.mcModel.setNumberOfItems(trans.getNumberOfMCItems());
		this.mcModel.setNetAmount(trans.getMCNetSalesAmount());

		this.builder.addCreditFee(EnumCardType.MC, trans.getMCDiscountAmount());
	}

	public void process(VisaMCUnbundled trans) {
		System.out.println(trans.toString());
		this.visaModel.setInterchangeFees(trans.getVisaInterchangeFee());
		this.visaModel.setAssessmentFees(trans.getVisaAssessmentFee());
		this.visaModel.setTransactionFees(trans.getVisaTransactionFee());
		this.settlementSummary.addSummaryDetail(visaModel);

		this.mcModel.setInterchangeFees(trans.getMCInterchangeFee());
		this.mcModel.setAssessmentFees(trans.getMCAssessmentFee());
		this.mcModel.setTransactionFees(trans.getMCTransactionFee());
		this.settlementSummary.addSummaryDetail(mcModel);
	}

	public void process(Invoice invoiceTx) {
		System.out.println(invoiceTx.toString());
		ErpSettlementInvoiceModel invoice = new ErpSettlementInvoiceModel();
		invoice.setInvoiceDate(invoiceTx.getInvoiceDate());
		invoice.setInvoiceAmount(invoiceTx.getInvoiceAmount());
		invoice.setInvoiceNumber(invoiceTx.getInvoiceNumber());
		invoice.setDescription(invoiceTx.getInvoiceDescription());
		this.invoices.add(invoice);

		this.builder.addInvoice(invoice.getInvoiceAmount(), invoice.getDescription());
	}

	public void process(Chargeback chgBackTx) {
		System.out.println(chgBackTx.toString());
		this.chgBackTx = chgBackTx;
	}

	public void process(ChargebackAddendum chgBackAddTx) throws CreateException, ErpTransactionException, RemoteException {
		System.out.println(chgBackAddTx.toString());

		String reason = chgBackTx.getChargebackReasonCode();
		
		ErpChargebackModel cb = CHARGEBACK_REVERSAL_CODE.equals(reason) ? new ErpChargebackReversalModel() : new ErpChargebackModel();

		cb.setAmount(chgBackTx.getChargebackAmount());
		cb.setBatchDate(chgBackTx.getBatchDate());
		cb.setBatchNumber(chgBackTx.getBatchNumber());
		cb.setCbkControlNumber(chgBackTx.getChargebackControlNumber());
		cb.setCbkDiscount(chgBackTx.getChargebackDiscount());
		cb.setCbkReasonCode(chgBackTx.getChargebackReasonCode());
		cb.setCbkReferenceNumber(chgBackTx.getChargebackReferenceNumber());
		cb.setTransactionSource(EnumTransactionSource.SYSTEM);
		
		this.chgBackTx = null;

		cb.setCbkRespondDate(chgBackAddTx.getChargebackRespondDate());
		cb.setCbkWorkDate(chgBackAddTx.getChargebackWorkDate());
		cb.setOriginalTxAmount(chgBackAddTx.getOriginalTransactionAmount());
		cb.setOriginalTxDate(chgBackAddTx.getOriginalTransactionDate());
		cb.setDescription(chgBackAddTx.getChargebackDescription());
		cb.setMerchantReferenceNumber(chgBackAddTx.getMerchantReferenceNumber());

		ErpChargebackResponse response = null; 
		
//		TODO This is old Chase reconciliation code did not update for BC split for lack of time.
		/*if( cb instanceof ErpChargebackReversalModel ){
			response = this.reconciliationSB.addChargebackReversal( (ErpChargebackReversalModel) cb );
			this.builder.addChargebackReversal( response.getInvoiceNumber(), response.getCardType(), cb.getAmount() );
		}else{
			response = this.reconciliationSB.addChargeback(cb);
			this.builder.addChargeback( response.getInvoiceNumber(), response.getCardType(), cb.getAmount() );
		}*/				
	}

	public void process(AmexJCBSummary trans) {
		System.out.println(trans.toString());
		this.amexModel = new ErpSummaryDetailModel(EnumSummaryDetailType.AMERICAN_EXPRESS);
		this.amexModel.setNumberOfItems(trans.getNumberOfAmexItems());
		this.amexModel.setNetAmount(trans.getAmexNetSalesAmount());

		this.jcbModel = new ErpSummaryDetailModel(EnumSummaryDetailType.JCB);
		this.jcbModel.setNumberOfItems(trans.getNumberOfJCBItems());
		this.jcbModel.setNetAmount(trans.getJCBNetSalesAmount());
	}

	public void process(AmexJCBUnbundled trans) {
		System.out.println(trans.toString());
		this.amexModel.setInterchangeFees(trans.getAmexInterchangeFee());
		this.amexModel.setAssessmentFees(trans.getAmexAssessmentFee());
		this.amexModel.setTransactionFees(trans.getAmexTransactionFee());
		this.settlementSummary.addSummaryDetail(this.amexModel);

		this.builder.addCreditFee(EnumCardType.AMEX, trans.getAmexTransactionFee());

		this.jcbModel.setInterchangeFees(trans.getJCBInterchangeFee());
		this.jcbModel.setAssessmentFees(trans.getJCBAssessmentFee());
		this.jcbModel.setTransactionFees(trans.getJCBTransactionFee());
		this.settlementSummary.addSummaryDetail(this.jcbModel);
	}

	public void process(DinersClubCarteBlancheSummary trans) {
		System.out.println(trans.toString());
		this.dcModel = new ErpSummaryDetailModel(EnumSummaryDetailType.DINERS_CLUB);
		this.dcModel.setNumberOfItems(trans.getNumberOfDinersClubItems());
		this.dcModel.setNetAmount(trans.getDinersClubNetSalesAmount());

		this.cbModel = new ErpSummaryDetailModel(EnumSummaryDetailType.CARTE_BLANCHE);
		this.cbModel.setNumberOfItems(trans.getNumberOfCarteBlancheItems());
		this.cbModel.setNetAmount(trans.getCarteBlancheNetSalesAmount());
	}

	public void process(DinersClubCarteBlancheUnbundled trans) {
		System.out.println(trans.toString());
		this.dcModel.setInterchangeFees(trans.getDinersClubInterchangeFee());
		this.dcModel.setAssessmentFees(trans.getDinersClubAssessmentFee());
		this.dcModel.setTransactionFees(trans.getDinersClubTransactionFee());
		this.settlementSummary.addSummaryDetail(this.dcModel);

		this.cbModel.setInterchangeFees(trans.getCarteBlancheInterchangeFee());
		this.cbModel.setAssessmentFees(trans.getCarteBlancheAssessmentFee());
		this.cbModel.setTransactionFees(trans.getCarteBlancheTransactionFee());
		this.settlementSummary.addSummaryDetail(this.cbModel);
	}

	public void process(NovusSummary trans) {
		System.out.println(trans.toString());
		this.novusModel = new ErpSummaryDetailModel(EnumSummaryDetailType.NOVUS);
		this.novusModel.setNumberOfItems(trans.getNumberOfNovusItems());
		this.novusModel.setNetAmount(trans.getNovusNetSalesAmount());
	}

	public void process(NovusUnbundled trans) {
		System.out.println(trans.toString());
		this.novusModel.setInterchangeFees(trans.getNovusInterchangeFee());
		this.novusModel.setAssessmentFees(trans.getNovusAssessmentFee());
		this.novusModel.setTransactionFees(trans.getNovusTransactionFee());
		this.settlementSummary.addSummaryDetail(this.novusModel);

		this.builder.addCreditFee(EnumCardType.DISC, trans.getNovusInterchangeFee());
	}

	public void process(Adjustment trans) {
		System.out.println(trans.toString());
		this.adjustmentModel = new ErpAdjustmentModel();
		this.adjustmentModel.setBatchDate(trans.getBatchDate());
		this.adjustmentModel.setBatchNumber(trans.getBatchNumber());
		this.adjustmentModel.setDepositDate(trans.getDepositDate());
		this.adjustmentModel.setProcessDate(trans.getProcessDate());
		this.adjustmentModel.setAdjustmentAmount(trans.getAdjustmentAmount());
		this.adjustmentModel.setAdjustmentDescription(trans.getAdjustmentDescription());
		this.adjustmentModel.setTransactionDate(trans.getTransactionDate());
		this.adjustmentModel.setTransactionSource(EnumTransactionSource.SYSTEM);
	}

	public void process(AdjustmentAddendum trans) throws RemoteException, CreateException {
		System.out.println(trans.toString());
		this.adjustmentModel.setReferenceNumber(trans.getReferenceNumber());
		this.adjustmentModel.setSequenceNumber(trans.getSequenceNumber());
		this.adjustmentModel.setNetSalesAmount(trans.getNetSalesAmount());

		this.reconciliationSB.addAdjustment(this.adjustmentModel);
	}

	public void process(FileHeader trans) {
		System.out.println(trans.toString());
		this.settlementSummary = new ErpSettlementSummaryModel();
		this.settlementSummary.setProcessPeriodStart(trans.getProcessingPeriodStartDate());
		this.settlementSummary.setProcessPeriodEnd(trans.getProcessingPeriodEndDate());
	}

	public void process(FileTrailer trans) throws RemoteException, CreateException {
		System.out.println(trans.toString());
		double netDeposit = trans.getFilePostedAmount();
		this.settlementSummary.setInvoices(this.invoices);

		this.builder.addHeader(this.batchDate, this.batchNumber, netDeposit);

		this.reconciliationSB.addSettlementSummary(this.settlementSummary);
	}
}