package com.freshdirect.dataloader.payment.reconciliation.paymentech;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.dataloader.payment.reconciliation.SettlementBuilderI;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParserClient;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.ejb.ReconciliationSB;

public class PaymentechPDEParserClient extends SettlementParserClient {
	
	private static final Category LOGGER = LoggerFactory.getInstance(PaymentechPDEParserClient.class);
	private static final int FIRST_ECP_RETURN = 1;
	
	public PaymentechPDEParserClient(SettlementBuilderI builder, ReconciliationSB reconciliationSB) {
		super(builder, reconciliationSB);
	}
	
	public void process(DFRStart start) {
		LOGGER.info(start.toString());
	}
	
	public void process(PaymentechHeader header) {
		LOGGER.info(header.toString());
	}
	
	public void process(PDE0017SRecord trans) {
		LOGGER.info(trans.toString());
	}
	
	public void process(PDE0017DRecord trans) throws ErpTransactionException, RemoteException {
		EnumPaymentechCategory cat = trans.getCategory();
		
		if(!EnumPaymentechCategory.NEW_CC_ECP_CHARGEBACKS.equals(cat) && !EnumPaymentechCategory.ITEMS_REPRESENTED.equals(cat)) {
			return;
		}
		
		ErpChargebackModel cb = EnumPaymentechCategory.ITEMS_REPRESENTED.equals(cat) ? new ErpChargebackReversalModel() : new ErpChargebackModel();

		cb.setAmount(trans.getAmount());
		cb.setBatchDate(trans.getTransactionDate());
		cb.setCbkControlNumber(trans.getSequenceNumber());
		cb.setCbkReasonCode(trans.getReasonCode());
		cb.setCbkReferenceNumber(trans.getSequenceNumber());
		cb.setTransactionSource(EnumTransactionSource.SYSTEM);

		cb.setCbkRespondDate(trans.getActivityDate());
		cb.setCbkWorkDate(trans.getChargebackDate());
		cb.setOriginalTxAmount(trans.getIssuerCBKAmount());
		cb.setOriginalTxDate(trans.getTransactionDate());
		cb.setDescription(trans.getCategory().getName());
		cb.setMerchantReferenceNumber(trans.getMerchantReferenceNumber());
		ErpAffiliate aff = ErpAffiliate.getAffiliateByTxDivision(trans.getEntityNumber());
		cb.setAffiliate(aff);

		ErpSettlementInfo info = null; 
		if( cb instanceof ErpChargebackReversalModel ){
			info = this.reconciliationSB.addChargebackReversal( (ErpChargebackReversalModel) cb );
			this.builder.addChargebackReversal( info, cb.getAmount() );
		}else{
			info = this.reconciliationSB.addChargeback(cb);
			this.builder.addChargeback( info, cb.getAmount() );
		}				
	}
	
	public void process(PDE0018SRecord trans) {
		LOGGER.info(trans.toString());
	}
		
	public void process(PDE0018DRecord trans) throws ErpTransactionException, RemoteException {
		
		if(EnumPaymentechCategory.NEW_CC_ECP_CHARGEBACKS.equals(trans.getCategory())) {
			
			String saleId = trans.getMerchantReferenceNumber();
			String accountNumber = trans.getAccountNumber();
			double amount = trans.getEcpReturnAmount();
			String sequenceNumber = trans.getSequenceNumber();
			EnumPaymentResponse paymentResponse = EnumPaymentResponse.getEnum(trans.getReasonCode());
			int usageCode = trans.getUsageCode();
			String description = (trans.getCategory() != null) ? trans.getCategory().getName() : "";
			
			ErpAffiliate aff = ErpAffiliate.getAffiliateByTxDivision(trans.getEntityNumber());
		
			ErpSettlementInfo info = reconciliationSB.processECPReturn(saleId, aff, accountNumber, amount, sequenceNumber, paymentResponse, description, usageCode);
			
			if (trans.getUsageCode() == FIRST_ECP_RETURN && 
				(EnumPaymentResponse.INSUFFIENT_FUNDS_D.equals(paymentResponse) || 
				EnumPaymentResponse.INSUFFIENT_FUNDS_R.equals(paymentResponse) || 
				EnumPaymentResponse.UNCOLLECTED_FUNDS_D.equals(paymentResponse) || 
				EnumPaymentResponse.UNCOLLECTED_FUNDS_R.equals(paymentResponse))  ) {
				
				return;
			}
			
			this.builder.addFailedSettlement(info, amount, EnumCardType.ECP);
		}
		
	}

	public void process(PDE0020DataRecord trans) {
		LOGGER.info(trans.toString());
	}
	
	public void process(PDE0022DRecord trans) throws ErpTransactionException, RemoteException {
		process((PDE0018DRecord)trans);  //exactly the same as PDE0018
	}

	public void process(DFREnd end) throws RemoteException {
		LOGGER.info(end.toString());
	}

}
