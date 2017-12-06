package com.freshdirect.webapp.ajax.expresscheckout.receipt.service;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.expresscheckout.cart.data.CartSubTotalFieldData;
import com.freshdirect.webapp.ajax.expresscheckout.receipt.data.ReceiptData;

public class ReceiptService {

	private static final ReceiptService INSTANCE = new ReceiptService();

	private ReceiptBoxService receiptBoxService;

	private ReceiptService() {
		receiptBoxService = ReceiptBoxService.defaultService();
	}

	public static ReceiptService defaultService() {
		return INSTANCE;
	}

	public ReceiptData populateReceiptData(final FDOrderI order, String requestURI, final FDUserI user) throws FDResourceException {
		final ReceiptData receiptData = new ReceiptData();
		receiptBoxService.populateOrderTotalToBox(receiptData, order);
		receiptBoxService.populateSaveAmountBox(receiptData, order);
		
		final List<CartSubTotalFieldData> receiptLines = new ArrayList<CartSubTotalFieldData>();
		receiptBoxService.populateSubTotalToBox(receiptLines, order);
		receiptBoxService.populateTaxToBox(receiptLines, order);
		//receiptBoxService.populateOrderTipToBox(receiptLines, order);
		receiptBoxService.populateDepositValueToBox(receiptLines, order);
		receiptBoxService.populateDeliveryChargeToBox(receiptLines, order, user);
		receiptBoxService.populateFuelSurchargeToBox(receiptLines, order);
		receiptBoxService.populateOrderTipToBox(receiptLines, order);
		receiptBoxService.populateDiscountsToBox(receiptLines, order);
		receiptBoxService.populateRedeemedSampleDescriptionToBox(receiptLines, order);
		receiptBoxService.populateExtendDeliveyPassDiscountToBox(receiptLines, order);
        receiptBoxService.populateGiftCardAmountToBeAppliedToBox(receiptLines, order, requestURI);
        receiptBoxService.populateRemainingGiftCardBalanceToBox(receiptLines, order, user);
        receiptBoxService.populateAmountToBeChargedYourBalanceToBox(receiptLines, order);
		receiptBoxService.populateCustomerCreditsToBox(receiptLines, order);
		receiptData.setReceiptLines(receiptLines);

		return receiptData;
	}
}
