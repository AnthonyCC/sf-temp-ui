package com.freshdirect.customer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.payment.EnumPaymentMethodType;

public class ErpPaymentMethodDeserializer extends JsonDeserializer<ErpPaymentMethodI> {

	@Override
	public ErpPaymentMethodI deserialize(JsonParser jp, DeserializationContext context)
			throws IOException, JsonProcessingException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		ObjectNode root = (ObjectNode) mapper.readTree(jp);
		JsonNode paymentMethodNode = root.get("paymentMethodType");
		if (paymentMethodNode != null) {
			String paymentMethodTypeString = paymentMethodNode.get("name") == null ? paymentMethodNode.asText()
					: paymentMethodNode.get("name").asText();
			EnumPaymentMethodType paymentMethodType = EnumPaymentMethodType.getEnum(paymentMethodTypeString);
			if (paymentMethodType == EnumPaymentMethodType.PAYPAL) {
				return mapper.convertValue(root, ErpPayPalCardModel.class);
			}
			if (paymentMethodType == EnumPaymentMethodType.CREDITCARD) {
				return mapper.convertValue(root, ErpCreditCardModel.class);
			}
			if (paymentMethodType == EnumPaymentMethodType.ECHECK) {
				return mapper.convertValue(root, ErpECheckModel.class);
			}
			if (paymentMethodType == EnumPaymentMethodType.EBT) {
				return mapper.convertValue(root, ErpEbtCardModel.class);
			}
			if (paymentMethodType == EnumPaymentMethodType.GIFTCARD) {
				return mapper.convertValue(root, ErpGiftCardModel.class);
			}
		}
		return mapper.convertValue(root, ErpPaymentMethodModel.class);

	}

}
