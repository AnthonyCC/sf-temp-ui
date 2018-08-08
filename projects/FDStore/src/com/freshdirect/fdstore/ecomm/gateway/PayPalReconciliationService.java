package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;

public class PayPalReconciliationService extends AbstractEcommService implements PayPalReconciliationServiceI {

	private final static Category LOGGER = LoggerFactory.getInstance(PayPalReconciliationService.class);

	private static final String GET_PAYPAL_NOT_PROCESSED_SETTLEMENT = "paypalReconciliation/getPPSettlementNotProcessed";
	private static final String ACQUIRE_PAYPAL_LOCK = "paypalReconciliation/lock/acquire";
	private static final String RELEASE_PAYPAL_LOCK = "paypalReconciliation/lock/release";
	private static final String CREATE_SETTLEMENT_RECORD = "paypalReconciliation/settlementRecord/create";
	private static final String ADD_PAYPAL_SETTLEMENT_SUMMARY = "paypalReconciliation/settlementSummary/create";
	private static final String UPDATE_PAYPAL_STATUS = "paypalReconciliation/status/update";
	private static final String UPDATE_PAYPAL_SETTLEMENT_STATUS = "paypalReconciliation/settlementStatus/update";
	private static final String GET_PAYPAL_TRANSACTIONS = "paypalReconciliation/tx";

	private static PayPalReconciliationServiceI INSTANCE;

	public static PayPalReconciliationServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PayPalReconciliationService();

		return INSTANCE;
	}

	@Override
	public void insertNewSettlementRecord(Date date) throws RemoteException {
		Request<Date> request = new Request<Date>();
		request.setData(date);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<Void> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CREATE_SETTLEMENT_RECORD),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService: date=" + date, e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List<String> addPPSettlementSummary(ErpSettlementSummaryModel[] models) throws RemoteException {
		Request<ErpSettlementSummaryModel[]> request = new Request<ErpSettlementSummaryModel[]>();
		request.setData(models);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<List<String>> response = this.postDataTypeMap(inputJson,
					getFdCommerceEndPoint(ADD_PAYPAL_SETTLEMENT_SUMMARY), new TypeReference<Response<List<String>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService: models=" + models, e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void updatePayPalStatus(List<String> settlementIds) throws RemoteException {
		Request<List<String>> request = new Request<List<String>>();
		request.setData(settlementIds);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<Void> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_PAYPAL_STATUS),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService: settlementIds=" + settlementIds, e);
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public List<ErpSettlementSummaryModel> getPPTrxns(List<String> ppStlmntIds) throws RemoteException {
		Request<List<String>> request = new Request<List<String>>();
		request.setData(ppStlmntIds);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<List<ErpSettlementSummaryModel>> response = this.postDataTypeMap(inputJson,
					getFdCommerceEndPoint(GET_PAYPAL_TRANSACTIONS),
					new TypeReference<Response<List<ErpSettlementSummaryModel>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService: ppStlmntIds=" + ppStlmntIds, e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void updatePPSettlementTransStatus(String settlementTransId) throws RemoteException {
		Request<String> request = new Request<String>();
		request.setData(settlementTransId);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<Void> response = this.postDataTypeMap(inputJson,
					getFdCommerceEndPoint(UPDATE_PAYPAL_SETTLEMENT_STATUS), new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService: settlementTransId=" + settlementTransId, e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public Map<String, String> getPPSettlementNotProcessed() throws RemoteException {
		try {
			Response<Map<String, String>> response = this.httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_PAYPAL_NOT_PROCESSED_SETTLEMENT),
					new TypeReference<Response<Map<String, String>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService");
				throw new RemoteException(response.getMessage());
			}
			return response.getData();
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> acquirePPLock(Date date) throws RemoteException {
		Request<Date> request = new Request<Date>();
		request.setData(date);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<Map<String, Object>> response = this.postDataTypeMap(inputJson,
					getFdCommerceEndPoint(ACQUIRE_PAYPAL_LOCK), new TypeReference<Response<Map<String, Object>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
			return response.getData();
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService: date=" + date, e);
			throw new RemoteException(e.getMessage());
		}

	}

	@Override
	public void releasePPLock(List<String> settlementIds) throws RemoteException {
		Request<List<String>> request = new Request<List<String>>();
		request.setData(settlementIds);
		String inputJson;
		try {
			inputJson = buildRequest(request);
			Response<Void> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(RELEASE_PAYPAL_LOCK),
					new TypeReference<Response<Void>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in PayPalReconciliationService: inputJson=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("Error in PayPalReconciliationService: settlementIds=" + settlementIds, e);
			throw new RemoteException(e.getMessage());
		}
	}
}
