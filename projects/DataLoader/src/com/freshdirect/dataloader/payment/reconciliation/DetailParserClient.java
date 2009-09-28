package com.freshdirect.dataloader.payment.reconciliation;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.giftcard.ejb.GiftCardManagerSB;
import com.freshdirect.payment.ejb.ReconciliationSB;
import com.freshdirect.payment.reconciliation.detail.CCDetailOne;
import com.freshdirect.payment.reconciliation.detail.CCDetailTwo;
import com.freshdirect.payment.reconciliation.detail.FileHeader;
import com.freshdirect.payment.reconciliation.detail.FileTrailer;


public class DetailParserClient extends SettlementParserClient {

	public DetailParserClient(SettlementBuilderI builder, ReconciliationSB reconSB) {
		super(builder, reconSB);
	}

	public void process(CCDetailOne trans) throws RemoteException, CreateException {
		System.out.println(trans.toString());

		EnumCardType ccType = EnumCardType.getBySettlementCode(trans.getProductCode());

		if (ccType==null) {
			// !!!
			return;
		}
		
		ErpSettlementModel model = new ErpSettlementModel();
		model.setAmount(trans.getTransactionAmount());
		model.setSequenceNumber(trans.getFDMSReferenceNumber());
		model.setTransactionSource(EnumTransactionSource.SYSTEM);
		String authId = trans.getAuthCode();
		String saleId = trans.getMerchantReferenceNumber();
		//TODO This is old Chase reconciliation code did not update for BC split for lack of time.
		//String invoiceNumber = invoiceNumber = this.reconciliationSB.addSettlement(model, saleId, authId);
		//this.builder.addChargeDetail(invoiceNumber, trans.getTransactionCode(), model.getAmount(), ccType);
	}

	public void process(CCDetailTwo trans) {
		System.out.println(trans.toString());
	}

	public void process(FileHeader trans) {
		System.out.println(trans.toString());
	}

	public void process(FileTrailer trans) {
		System.out.println(trans.toString());
	}

}