package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.payment.PaymentCommandData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.command.Capture;
import com.freshdirect.payment.command.DeliveryConfirmation;
import com.freshdirect.payment.command.PaymentCommandI;
import com.freshdirect.payment.command.Redelivery;
import com.freshdirect.payment.command.RefusedOrder;

public class PaymentGatewayService extends AbstractEcommService implements PaymentGatewayServiceI {
	
	private final static Category LOGGER = LoggerFactory
			.getInstance(PaymentGatewayService.class);
	
	private static PaymentGatewayService INSTANCE;
	
	public static PaymentGatewayServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PaymentGatewayService();

		return INSTANCE;
	}
	
	private static final String UPDATE_SALE_DLV_STATUS = "paymentgateway/saledlvstatus/update";
	
	@Override
	public void updateSaleDlvStatus(PaymentCommandI command)throws RemoteException {
		try {
			Request<PaymentCommandData> request = new Request<PaymentCommandData>();
			request.setData(buildPaymentCommandData(command));
			String inputJson = buildRequest(request);
			Response<String> response = this.postData(inputJson, getFdCommerceEndPoint(UPDATE_SALE_DLV_STATUS),Response.class);
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	private PaymentCommandData buildPaymentCommandData(PaymentCommandI command) {
		PaymentCommandData commandData = new PaymentCommandData();
		if(command instanceof RefusedOrder){
			RefusedOrder refusedOrder = (RefusedOrder) command;
			commandData.setAlcoholOnly(refusedOrder.isAlcoholOnly());
			commandData.setFullReturn(refusedOrder.isFullReturn());
			commandData.setSaleId(refusedOrder.getSaleId());
			commandData.setReturnType(RefusedOrder.class.getSimpleName());
			return commandData;
		}
		else if (command instanceof Capture){
			Capture capture = (Capture) command;
			commandData.setSaleId(capture.getSaleId());
			commandData.setReturnType(Capture.class.getSimpleName());
			return commandData;
		}
		else if(command instanceof DeliveryConfirmation){
			DeliveryConfirmation deliveryConfirmation = (DeliveryConfirmation) command;
			commandData.setSaleId(deliveryConfirmation.getSaleId());
			commandData.setReturnType(DeliveryConfirmation.class.getSimpleName());
			return commandData;
		}
		else if(command instanceof Redelivery){
			Redelivery redelivery = (Redelivery) command;
			commandData.setSaleId(redelivery.getSaleId());
			commandData.setReturnType(Redelivery.class.getSimpleName());
			return commandData;
		}
		return null;
	}

}
