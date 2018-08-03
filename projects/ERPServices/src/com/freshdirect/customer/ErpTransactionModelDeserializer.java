package com.freshdirect.customer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.giftcard.ErpGiftCardBalanceTransferModel;
import com.freshdirect.giftcard.ErpGiftCardDlvConfirmModel;
import com.freshdirect.giftcard.ErpPostAuthGiftCardModel;
import com.freshdirect.giftcard.ErpPreAuthGiftCardModel;
import com.freshdirect.giftcard.ErpRegisterGiftCardModel;
import com.freshdirect.giftcard.ErpReverseAuthGiftCardModel;

public class ErpTransactionModelDeserializer extends JsonDeserializer<ErpTransactionModel> {

	@Override
	public ErpTransactionModel deserialize(JsonParser jp, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		ObjectNode root = (ObjectNode) mapper.readTree(jp);
		JsonNode transactionType = root.get("transactionType");
		if (transactionType != null) {
			String transactionTypeString = transactionType.asText();
			EnumTransactionType transactionTypeEnum = EnumTransactionType.getTransactionType(transactionTypeString);
			if (transactionTypeEnum == EnumTransactionType.ADJUSTMENT) {
				return mapper.convertValue(root, ErpAdjustmentModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.AUTHORIZATION) {
				return mapper.convertValue(root, ErpAuthorizationModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.BALANCETRANSFER_GIFTCARD) {
				return mapper.convertValue(root, ErpGiftCardBalanceTransferModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.CANCEL_ORDER) {
				return mapper.convertValue(root, ErpCancelOrderModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.CAPTURE) {
				return mapper.convertValue(root, ErpCaptureModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.CASHBACK) {
				return mapper.convertValue(root, ErpCashbackModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.CHARGE) {
				return mapper.convertValue(root, ErpChargeModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.CHARGEBACK) {
				return mapper.convertValue(root, ErpChargebackModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.CHARGEBACK_REVERSAL) {
				return mapper.convertValue(root, ErpChargebackReversalModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.CREATE_ORDER) {
				return mapper.convertValue(root, ErpCreateOrderModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.DELIVERY_CONFIRM) {
				return mapper.convertValue(root, ErpDeliveryConfirmModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.FUNDS_REDEPOSIT) {
				return mapper.convertValue(root, ErpFundsRedepositModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.GIFTCARD_DLV_CONFIRM) {
				return mapper.convertValue(root, ErpGiftCardDlvConfirmModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.INVOICE) {
				return mapper.convertValue(root, ErpInvoiceModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.INVOICE_CHARGE) {
				return mapper.convertValue(root, ErpChargeInvoiceModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.MODIFY_ORDER) {
				return mapper.convertValue(root, ErpModifyOrderModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.POSTAUTH_GIFTCARD) {
				return mapper.convertValue(root, ErpPostAuthGiftCardModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.PREAUTH_GIFTCARD) {
				return mapper.convertValue(root, ErpPreAuthGiftCardModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.REDELIVERY) {
				return mapper.convertValue(root, ErpRedeliveryModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.REGISTER_GIFTCARD) {
				return mapper.convertValue(root, ErpRegisterGiftCardModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.RESUBMIT_PAYMENT) {
				return mapper.convertValue(root, ErpResubmitPaymentModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.RETURN_ORDER) {
				return mapper.convertValue(root, ErpReturnOrderModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.REVERSAL) {
				return mapper.convertValue(root, ErpReversalModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.REVERSEAUTH_GIFTCARD) {
				return mapper.convertValue(root, ErpReverseAuthGiftCardModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.SETTLEMENT) {
				return mapper.convertValue(root, ErpSettlementModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.SETTLEMENT_CHARGE) {
				return mapper.convertValue(root, ErpChargeSettlementModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.SETTLEMENT_CHARGE_FAILED) {
				return mapper.convertValue(root, ErpFailedChargeSettlementModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.SETTLEMENT_FAILED) {
				return mapper.convertValue(root, ErpFailedSettlementModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.SUBMIT_FAILED) {
				return mapper.convertValue(root, ErpSubmitFailedModel.class);
			}
			if (transactionTypeEnum == EnumTransactionType.VOID_CAPTURE) {
				return mapper.convertValue(root, ErpVoidCaptureModel.class);
			}

		}
		return mapper.convertValue(root, ErpPaymentModel.class);

	}

}
