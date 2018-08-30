package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SaleCronService extends AbstractEcommService implements SaleCronServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(SaleCronService.class);

	private static final String AUTHORIIZE_SALES = "saleCron/authorize";

	private static final String AUTHORIIZE_SUBSCRIPTIONS = "saleCron/subscription/authorize?timeout=";

	private static final String CUT_OFF_SALES = "saleCron/cutoff";

	private static final String CAPTURE_SALES = "saleCron/capture";

	private static final String CANCEL_AUTHORIZATION_FAILED = "saleCron/failedAuthorization/cancel";

	private static final String REGISTER_GIFT_CARDS = "saleCron/giftCard/register?timeout=";

	private static final String PREAUTHORIZE_SALES = "saleCron/preAuthorize?timeout=";

	private static final String REVERSE_AUTHORIZE_SALES = "saleCron/reverseAuthorize?timeout=";

	private static final String POST_AUTH_SALES = "saleCron/postAuth?timeout=";

	private static final String CUTT_OFF_REPORT_DELIVERY_DATES = "saleCron/dlvDates/cutOff";

	private static final String POST_AUTH_EBT_SALES = "saleCron/EBT/postAuth?timeout=";

	private static final String CAPTURE_EBT_SALES = "saleCron/capture/EBT";

	private static final String SETTLE_EBT_SALES = "saleCron/EBT/settle";

	private static SaleCronService INSTANCE;

	public static SaleCronServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SaleCronService();

		return INSTANCE;
	}

	@Override
	public void authorizeSales(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(AUTHORIIZE_SALES + "?timeout=" + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void authorizeSubscriptions(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(AUTHORIIZE_SUBSCRIPTIONS + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public int cutoffSales() throws RemoteException {
		Response<Integer> response = new Response<Integer>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CUT_OFF_SALES),
					new TypeReference<Response<Integer>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public void captureSales(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CAPTURE_SALES + "?timeout=" + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void cancelAuthorizationFailed() throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CANCEL_AUTHORIZATION_FAILED),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void registerGiftCards(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(REGISTER_GIFT_CARDS + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new RemoteException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void preAuthorizeSales(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(PREAUTHORIZE_SALES + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void reverseAuthorizeSales(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(REVERSE_AUTHORIZE_SALES + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void postAuthSales(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(POST_AUTH_SALES + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<Date> queryCutoffReportDeliveryDates() throws SQLException, RemoteException {
		Response<List<Long>> response = new Response<List<Long>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CUTT_OFF_REPORT_DELIVERY_DATES),
					new TypeReference<Response<List<Long>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}

		List<Long> dates = response.getData();
		List<Date> deliveryDates = new ArrayList<Date>();
		for (Long date : dates) {
			deliveryDates.add(new Date(date));
		}
		return deliveryDates;
	}

	@Override
	public void postAuthEBTSales(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(POST_AUTH_EBT_SALES + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void captureEBTSales(long timeout) throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CAPTURE_EBT_SALES + "?timeout=" + timeout),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void settleEBTSales() throws RemoteException {
		Response<Void> response = new Response<Void>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SETTLE_EBT_SALES),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in SaleCronService: response=" + response);
				throw new RemoteException(response.getMessage());
			}
		} catch (FDRuntimeException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error("Error in SaleCronService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

}
