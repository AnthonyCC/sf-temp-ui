package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.deliverypass.FDUserDlvPassInfo;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerDeliveryPassService extends AbstractEcommService implements CustomerDeliveryPassServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomerDeliveryPassService.class);

	private static final String GET_INFO = "customerDeliveryPass/getInfo";

	private static final String HAS_AUTO_RENEW_DP = "customerDeliveryPass/hasAutoRenewDP";

	private static CustomerDeliveryPassServiceI INSTANCE;

	public static CustomerDeliveryPassServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CustomerDeliveryPassService();

		return INSTANCE;
	}

	@Override
	public FDUserDlvPassInfo getDeliveryPassInfo(FDIdentity identity, EnumEStoreId estore)
			throws FDResourceException, RemoteException {
		Response<FDUserDlvPassInfo> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("identity", getMapper().convertValue(identity, JsonNode.class));
			rootNode.set("estore", getMapper().convertValue(estore, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(GET_INFO),
					new TypeReference<Response<FDUserDlvPassInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in CustomerDeliveryPassService: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in CustomerDeliveryPassService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String hasAutoRenewDP(String customerPK) throws FDResourceException, RemoteException {
		Response<String> response = this.httpGetDataTypeMap(getFdCommerceEndPoint(HAS_AUTO_RENEW_DP + "/" + customerPK),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			throw new FDResourceException(response.getMessage());
		}
		return response.getData();
	}

}
