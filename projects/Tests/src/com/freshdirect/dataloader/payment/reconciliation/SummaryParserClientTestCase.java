/*
 * Created on Apr 10, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.dataloader.payment.reconciliation;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.freshdirect.ReflectionParamComparator;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackResponse;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.model.EnumSummaryDetailType;
import com.freshdirect.payment.model.ErpSettlementInvoiceModel;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.model.ErpSummaryDetailModel;
import com.freshdirect.payment.reconciliation.summary.AmexJCBSummary;
import com.freshdirect.payment.reconciliation.summary.AmexJCBUnbundled;
import com.freshdirect.payment.reconciliation.summary.Chargeback;
import com.freshdirect.payment.reconciliation.summary.ChargebackAddendum;
import com.freshdirect.payment.reconciliation.summary.FileHeader;
import com.freshdirect.payment.reconciliation.summary.FileTrailer;
import com.freshdirect.payment.reconciliation.summary.Invoice;
import com.freshdirect.payment.reconciliation.summary.NovusSummary;
import com.freshdirect.payment.reconciliation.summary.NovusUnbundled;
import com.freshdirect.payment.reconciliation.summary.SummaryHeader;
import com.freshdirect.payment.reconciliation.summary.VisaMCSummary;
import com.freshdirect.payment.reconciliation.summary.VisaMCUnbundled;

/**
 * @author knadeem
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SummaryParserClientTestCase extends TestCase {

	private MockControl builderCtrl;
	private MockControl sbCtrl;
	private SettlementBuilderI mockBuilder;
	private ReconciliationSB mockSB;
	private SummaryParserClient client;
	private Date mockDate = new Date(0);

	public SummaryParserClientTestCase(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		builderCtrl = MockControl.createStrictControl(SettlementBuilderI.class);
		mockBuilder = (SettlementBuilderI) builderCtrl.getMock();

		sbCtrl = MockControl.createStrictControl(ReconciliationSB.class);
		mockSB = (ReconciliationSB) sbCtrl.getMock();

		client = new SummaryParserClient(mockBuilder, mockSB);

	}

	public void testProcessHeader() throws CreateException, ErpTransactionException, RemoteException {

		mockBuilder.addCreditFee(EnumCardType.VISA, 50.46);
		mockBuilder.addCreditFee(EnumCardType.MC, 23.67);
		mockBuilder.addCreditFee(EnumCardType.AMEX, 0);
		mockBuilder.addCreditFee(EnumCardType.DISC, 0);
		mockBuilder.addInvoice(100, "MONTHLY AUTH CHARGES");
		//mockBuilder.addChargeback("12345", EnumCardType.VISA, -10);
		//mockBuilder.addChargebackReversal("12345", EnumCardType.VISA, 10);
		mockBuilder.addHeader(mockDate, "41111", 1000.12);

		ErpSettlementSummaryModel summary = new ErpSettlementSummaryModel();
		summary.setNetSaleAmount(1200.56);
		summary.setProcessPeriodStart(mockDate);
		summary.setProcessPeriodEnd(mockDate);
		summary.setProcessDate(mockDate);
		summary.setDepositDate(mockDate);
		summary.setSettlementFileDate(mockDate);
		summary.setBatchDate(mockDate);
		summary.setBatchNumber("41111");
		summary.setAdjustmentAmount(0.0);

		ErpSummaryDetailModel detail = new ErpSummaryDetailModel(EnumSummaryDetailType.VISA);
		detail.setNumberOfItems(10);
		detail.setNetAmount(1200);
		detail.setAssessmentFees(2.00);
		detail.setInterchangeFees(10.0);
		detail.setTransactionFees(38.46);

		summary.addSummaryDetail(detail);

		detail = new ErpSummaryDetailModel(EnumSummaryDetailType.MASTERCARD);
		detail.setNumberOfItems(5);
		detail.setNetAmount(500.50);
		detail.setAssessmentFees(3.67);
		detail.setInterchangeFees(5.00);
		detail.setTransactionFees(15.00);

		summary.addSummaryDetail(detail);

		detail = new ErpSummaryDetailModel(EnumSummaryDetailType.AMERICAN_EXPRESS);
		detail.setAssessmentFees(0);
		detail.setInterchangeFees(0);
		detail.setNetAmount(100);
		detail.setNumberOfItems(2);
		detail.setTransactionFees(0);

		summary.addSummaryDetail(detail);

		detail = new ErpSummaryDetailModel(EnumSummaryDetailType.JCB);
		detail.setAssessmentFees(0);
		detail.setInterchangeFees(0);
		detail.setNetAmount(0);
		detail.setNumberOfItems(0);
		detail.setTransactionFees(0);

		summary.addSummaryDetail(detail);

		detail = new ErpSummaryDetailModel(EnumSummaryDetailType.NOVUS);
		detail.setAssessmentFees(0);
		detail.setInterchangeFees(0);
		detail.setNetAmount(20);
		detail.setNumberOfItems(1);
		detail.setTransactionFees(0);

		summary.addSummaryDetail(detail);

		ErpSettlementInvoiceModel invoice = new ErpSettlementInvoiceModel();
		invoice.setDescription("MONTHLY AUTH CHARGES");
		invoice.setInvoiceAmount(100);
		invoice.setInvoiceDate(mockDate);
		invoice.setInvoiceNumber("00005");

		summary.addInvoice(invoice);

		mockSB.addSettlementSummary(summary);
		sbCtrl.setComparator(new ReflectionParamComparator());
		sbCtrl.setVoidCallable();

		ErpChargebackModel chargeback = new ErpChargebackModel();
		chargeback.setAmount(10);
		chargeback.setBatchDate(mockDate);
		chargeback.setBatchNumber("00005");
		chargeback.setCbkControlNumber("1234567");
		chargeback.setCbkDiscount(0);
		chargeback.setCbkReasonCode("08");
		chargeback.setCbkReferenceNumber("98765");
		chargeback.setCbkRespondDate(mockDate);
		chargeback.setCbkWorkDate(mockDate);
		chargeback.setDescription("NO AUTHORIZATION");
		chargeback.setMerchantReferenceNumber("12345");
		chargeback.setOriginalTxAmount(10);
		chargeback.setOriginalTxDate(mockDate);
		chargeback.setTax(0);
		chargeback.setTransactionDate(mockDate);
		chargeback.setTransactionSource(EnumTransactionSource.SYSTEM);

		mockSB.addChargeback(chargeback);
		sbCtrl.setComparator(new ReflectionParamComparator());
		//sbCtrl.setDefaultReturnValue((Object) new ErpChargebackResponse("12345", EnumCardType.VISA));

		ErpChargebackReversalModel cbkReversal = new ErpChargebackReversalModel();
		cbkReversal.setAmount(10);
		cbkReversal.setBatchDate(mockDate);
		cbkReversal.setBatchNumber("00005");
		cbkReversal.setCbkControlNumber("1234567");
		cbkReversal.setCbkDiscount(0);
		cbkReversal.setCbkReasonCode("RV");
		cbkReversal.setCbkReferenceNumber("98765");
		cbkReversal.setCbkRespondDate(mockDate);
		cbkReversal.setCbkWorkDate(mockDate);
		cbkReversal.setDescription("REVERSING PREVIOUS CHARGEBACK");
		cbkReversal.setMerchantReferenceNumber("12345");
		cbkReversal.setOriginalTxAmount(10);
		cbkReversal.setOriginalTxDate(mockDate);
		cbkReversal.setTax(0);
		cbkReversal.setTransactionDate(mockDate);
		cbkReversal.setTransactionSource(EnumTransactionSource.SYSTEM);

		mockSB.addChargebackReversal(cbkReversal);
		sbCtrl.setComparator(new ReflectionParamComparator());
		//sbCtrl.setDefaultReturnValue(new ErpChargebackResponse("12345", EnumCardType.VISA));

		builderCtrl.replay();
		sbCtrl.replay();

		FileHeader fhdr = new FileHeader();
		fhdr.setFileID("CR44");
		fhdr.setProcessingPeriodStartDate(mockDate);
		fhdr.setProcessingPeriodEndDate(mockDate);

		client.process(fhdr);

		SummaryHeader shdr = new SummaryHeader();
		shdr.setMerchantNumber("123456");
		shdr.setBatchDate(mockDate);
		shdr.setBatchNumber("41111");
		shdr.setProcessDate(mockDate);
		shdr.setDepositDate(mockDate);
		shdr.setNetSalesAmount(1200.56);
		shdr.setNumberOfAdjustments(0);
		shdr.setAdjustmentAmount(0.0);
		shdr.setSettlementFileCreationDate(mockDate);

		client.process(shdr);

		processVisaMCRecord();

		processAmexJCBRecord();

		processNovusRecord();

		processInvoiceRecord();

		processChargebackRecord();

		processChargebackReversal();

		FileTrailer trl = new FileTrailer();
		trl.setFileNetAmount(1001.12);
		trl.setFileNetDeposit(1002.12);
		trl.setFilePostedAmount(1000.12);
		trl.setFileRecordCount(0);
		client.process(trl);

		builderCtrl.verify();
		sbCtrl.verify();

	}

	public void processVisaMCRecord() {

		VisaMCSummary vmSummary = new VisaMCSummary();
		vmSummary.setNumberOfVisaItems(10);
		vmSummary.setNumberOfMCItems(5);
		vmSummary.setVisaNetSalesAmount(1200.00);
		vmSummary.setMCNetSalesAmount(500.50);
		vmSummary.setVisaDiscountAmount(50.46);
		vmSummary.setMCDiscountAmount(23.67);

		client.process(vmSummary);

		VisaMCUnbundled vmUnbundled = new VisaMCUnbundled();
		vmUnbundled.setUnbundledOption("7");
		vmUnbundled.setVisaInterchangeFee(10.00);
		vmUnbundled.setVisaAssessmentFee(2.00);
		vmUnbundled.setVisaTransactionFee(38.46);
		vmUnbundled.setMCAssessmentFee(3.67);
		vmUnbundled.setMCInterchangeFee(5.00);
		vmUnbundled.setMCTransactionFee(15.00);

		client.process(vmUnbundled);
	}

	public void processInvoiceRecord() {

		Invoice invoice = new Invoice();
		invoice.setMerchantNumber("12345");
		invoice.setInvoiceAmount(100);
		invoice.setInvoiceDate(mockDate);
		invoice.setInvoiceNumber("00005");
		invoice.setInvoiceDescription("MONTHLY AUTH CHARGES");

		client.process(invoice);
	}

	public void processChargebackRecord() throws ErpTransactionException, RemoteException, CreateException {

		Chargeback chargeback = new Chargeback();
		chargeback.setMerchantNumber("12345");
		chargeback.setBatchDate(mockDate);
		chargeback.setBatchNumber("00005");
		chargeback.setChargebackAmount(-10.0);
		chargeback.setChargebackControlNumber("1234567");
		chargeback.setChargebackDiscount(0);
		chargeback.setChargebackReasonCode("08");
		chargeback.setCardholderNumber("4111111111111111");
		chargeback.setChargebackReferenceNumber("98765");

		client.process(chargeback);

		ChargebackAddendum chargebackA = new ChargebackAddendum();
		chargebackA.setMerchantReferenceNumber("12345");
		chargebackA.setChargebackDescription("NO AUTHORIZATION");
		chargebackA.setChargebackWorkDate(mockDate);
		chargebackA.setChargebackRespondDate(mockDate);
		chargebackA.setOriginalTransactionAmount(10);
		chargebackA.setOriginalTransactionDate(mockDate);

		client.process(chargebackA);
	}

	public void processChargebackReversal() throws ErpTransactionException, RemoteException, CreateException {
		Chargeback chargeback = new Chargeback();
		chargeback.setMerchantNumber("12345");
		chargeback.setBatchDate(mockDate);
		chargeback.setBatchNumber("00005");
		chargeback.setChargebackAmount(10.0);
		chargeback.setChargebackControlNumber("1234567");
		chargeback.setChargebackDiscount(0);
		chargeback.setChargebackReasonCode("RV");
		chargeback.setCardholderNumber("4111111111111111");
		chargeback.setChargebackReferenceNumber("98765");

		client.process(chargeback);

		ChargebackAddendum chargebackA = new ChargebackAddendum();
		chargebackA.setMerchantReferenceNumber("12345");
		chargebackA.setChargebackDescription("REVERSING PREVIOUS CHARGEBACK");
		chargebackA.setChargebackWorkDate(mockDate);
		chargebackA.setChargebackRespondDate(mockDate);
		chargebackA.setOriginalTransactionAmount(10);
		chargebackA.setOriginalTransactionDate(mockDate);

		client.process(chargebackA);
	}

	public void processAmexJCBRecord() {

		AmexJCBSummary ajSummary = new AmexJCBSummary();
		ajSummary.setNumberOfAmexItems(2);
		ajSummary.setAmexNetSalesAmount(100);
		ajSummary.setAmexFDMSDiscountAmount(0);
		ajSummary.setAmexIssuerDiscountAmount(0);
		ajSummary.setNumberOfJCBItems(0);
		ajSummary.setJCBFDMSDiscountAmount(0);
		ajSummary.setJCBIssuerDiscountAmount(0);
		ajSummary.setJCBNetSalesAmount(0);

		client.process(ajSummary);

		AmexJCBUnbundled ajUnbundled = new AmexJCBUnbundled();
		ajUnbundled.setUnbundledOption("7");
		ajUnbundled.setAmexAssessmentFee(0);
		ajUnbundled.setAmexInterchangeFee(0);
		ajUnbundled.setAmexTransactionFee(0);
		ajUnbundled.setAmexTransactionFee(0);
		ajUnbundled.setJCBAssessmentFee(0);
		ajUnbundled.setJCBInterchangeFee(0);
		ajUnbundled.setJCBTransactionFee(0);

		client.process(ajUnbundled);

	}

	public void processDinersClubCarteBlancheRecord() {
	}

	public void processNovusRecord() {

		NovusSummary nSummary = new NovusSummary();
		nSummary.setNumberOfNovusItems(1);
		nSummary.setNovusNetSalesAmount(20);
		nSummary.setNovusFDMSDiscountAmount(0);
		nSummary.setNovusIssuerDiscountAmount(0);

		client.process(nSummary);

		NovusUnbundled nUnbundled = new NovusUnbundled();
		nUnbundled.setUnbundledOption("7");
		nUnbundled.setNovusAssessmentFee(0);
		nUnbundled.setNovusInterchangeFee(0);
		nUnbundled.setNovusTransactionFee(0);

		client.process(nUnbundled);
	}

	public void processAdjustmentRecord() {
	}

}
