package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpPaymentMethodException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;

public class CustomerGiftCardService extends AbstractEcommService implements CustomerGiftCardServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerGiftCardService.class);

	private static final String GET_RECIPIENT_DLV_INFO = "/customerGiftCard/getRecipientDlvInfo";
	private static final String ADD_PAYMENT_METHOD = "/customerPayment/paymentMethod/add";
	private static final String UPDATE_PAYMENT_METHOD = "/customerPayment/paymentMethod/update";
	private static final String REMOVE_PAYMENT_METHOD = "/customerPayment/paymentMethod/remove";
	private static final String GET_DEFAULT_PAYMENT_METHOD_PK = "/customerPayment/defaultPaymentMethodPK";
	private static final String SET_DEFAULT_PAYMENT_METHOD_PK = "/customerPayment/defaultPaymentMethod";
	private static final String RESET_DEFAULT_PAYMENT_METHOD_TYPE = "/customerPayment/defaultPaymentMethodType/reset";
	private static CustomerGiftCardServiceI INSTANCE;

	public static CustomerGiftCardServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerGiftCardService();

		return INSTANCE;
	}

	@Override
	public ErpGCDlvInformationHolder getRecipientDlvInfo(FDIdentity identity, String saleId, String certificationNum)
			throws FDResourceException, RemoteException {
		Response<ErpGCDlvInformationHolder> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.put("saleId", saleId);
			rootNode.put("certificationNum", certificationNum);
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_RECIPIENT_DLV_INFO),
					new TypeReference<Response<ErpGCDlvInformationHolder>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

}
