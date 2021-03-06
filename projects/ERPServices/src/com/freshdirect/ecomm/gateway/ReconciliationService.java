/*
 * @author tbalumuri
 */
package com.freshdirect.ecomm.gateway;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Category;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.common.CustomMapper;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.DlvSaleInfo;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.ErpAdjustmentModel;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpDeliveryInfoModel;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpSettlementInfo;
import com.freshdirect.customer.ErpSettlementModel;
import com.freshdirect.deliverypass.DlvPassUsageInfo;
import com.freshdirect.deliverypass.DlvPassUsageLine;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.complaint.ErpComplaintReasonData;
import com.freshdirect.ecommerce.data.dlvpass.DlvPassUsageLineData;
import com.freshdirect.ecommerce.data.order.DlvSaleInfoData;
import com.freshdirect.ecommerce.data.order.ErpSaleInfoData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.giftcard.ErpGCSettlementInfo;
import com.freshdirect.payment.EFTTransaction;
import com.freshdirect.payment.model.ErpSettlementSummaryModel;
import com.freshdirect.payment.reconciliation.detail.CCDetailOne;
import com.freshdirect.sap.ejb.SapException;

public class ReconciliationService extends AbstractEcommService implements ReconciliationServiceI {
	
	private static ReconciliationService INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(ReconciliationService.class);

	private static final String ADDSETTLEMENT = "reconsiliation/addSettlement";
	private static final String CHARGEBACK = "reconsiliation/chargeBack";
	private static final String CHARGEBACK_REVERSAL ="reconsiliation/chargeBackReversal";
	private static final String SETTLEMENT_SUMMARY ="reconsiliation/addSettlementSummary";
	private static final String ADD_ADJUSTMENTS = "reconsiliation/addAdjustments";
	private static final String IS_CHAGE_SETTLEMENT =  "reconsiliation/isChargeSettlement";
	private static final String PROCESS_ECPRETURNS = "reconsiliation/processECPReturn";
	private static final String PROCESS_SETTLEMENTS = "reconsiliation/processSettlement";
	private static final String LOAD_BAD_TRANSACTION =  "reconsiliation/loadBadTransaction";
	private static final String LOAD_READY_SETTLEMENT = "reconsiliation/loadReadyToSettleECPSales";
	private static final String LOAD_READY_SETTLEMENT_BY_SALESIDS = "reconsiliation/loadReadyToSettleByECPSaleIds";
	private static final String SETTLEMENT_FAILED_AFTER_SETTLED = "reconsiliation/isSettlementFailedAfterSettled/";
	private static final String PROCESS_GC_SETTLEMENT = "reconsiliation/processGcSettlement/";
	private static final String PROCESS_SETTLEMENT_PENDING_ORDER = "reconsiliation/proSettlePenOrder";
	private static final String SEND_SETTLEMENT_RECON_SAP = "reconsiliation/sendSettleToReconSap";
	private static final String UPLOAD_SAP_FILE = "reconsiliation/uploadSapFile";
	private static final String UPLOAD_SAP_FILE_TO_BAPI = "reconsiliation/uploadSapFileToBapi";
		
	
	
	public static ReconciliationServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ReconciliationService();

		return INSTANCE;
	}
	
	@Override
	public ErpSettlementInfo addSettlement(ErpSettlementModel model, String saleId, ErpAffiliate affiliate, boolean refund)
			throws RemoteException {
		Response<ErpSettlementInfo> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("model", getMapper().convertValue(model, JsonNode.class));
			rootNode.set("affiliate", getMapper().convertValue(affiliate, JsonNode.class));
			rootNode.set("refund", getMapper().convertValue(refund, JsonNode.class));
			rootNode.put("saleId", saleId);

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADDSETTLEMENT),
					new TypeReference<Response<ErpSettlementInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in AddSettlement: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		
		return response.getData();
	}


	@Override
	public ErpSettlementInfo addChargeback(ErpChargebackModel chargebackModel)
			throws RemoteException {
		Response<ErpSettlementInfo> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("chargebackModel", getMapper().convertValue(chargebackModel, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CHARGEBACK),
					new TypeReference<Response<ErpSettlementInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in addChargeback: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		
		return response.getData();
	}

	@Override
	public ErpSettlementInfo addChargebackReversal(
			ErpChargebackReversalModel chargebackReversalModel)
			throws RemoteException {
		Response<ErpSettlementInfo> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("chargebackReversalModel", getMapper().convertValue(chargebackReversalModel, JsonNode.class));
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CHARGEBACK_REVERSAL),
					new TypeReference<Response<ErpSettlementInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in addChargebackReversal: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		
		return response.getData();
	}

	@Override
	public void addSettlementSummary(ErpSettlementSummaryModel settlementSummary)
			throws RemoteException {
		Response<String> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("settlementSummary", getMapper().convertValue(settlementSummary, JsonNode.class));
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SETTLEMENT_SUMMARY),
					new TypeReference<Response<String>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in addSettlementSummary: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public void addAdjustment(ErpAdjustmentModel adjustmentModel)
			throws RemoteException {
		Response<String> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("adjustmentModel", getMapper().convertValue(adjustmentModel, JsonNode.class));
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ADD_ADJUSTMENTS),
					new TypeReference<Response<String>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in addAdjustment: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public boolean isChargeSettlement(String saleId, double chargeAmount)
			throws RemoteException {
		Response<Boolean> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("saleId", saleId);
			rootNode.put("chargeAmount", chargeAmount);
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(IS_CHAGE_SETTLEMENT),
					new TypeReference<Response<Boolean>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in isChargeSettlement: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData().booleanValue();
	}

	@Override
	public ErpSettlementInfo processSettlement(String saleId, ErpAffiliate aff,
			String authId, String accountNumber, double chargeAmount,
			String sequenceNumber, EnumCardType ccType, boolean cbr)
			throws RemoteException {
		Response<ErpSettlementInfo> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("saleId", saleId);
			rootNode.set("erpAffiliate", getMapper().convertValue(aff, JsonNode.class));
			rootNode.put("authId", authId);
			rootNode.put("accountNumber", accountNumber);
			rootNode.put("chargeAmount", chargeAmount);
			rootNode.put("sequenceNumber", sequenceNumber);
			rootNode.set("ccType", getMapper().convertValue(ccType, JsonNode.class));
			rootNode.put("cbr", cbr);
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(PROCESS_SETTLEMENTS),
					new TypeReference<Response<ErpSettlementInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in processSettlement: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public ErpSettlementInfo processECPReturn(String saleId, ErpAffiliate aff,
			String accountNumber, double amount, String sequenceNumber,
			EnumPaymentResponse paymentResponse, String description,
			int usageCode) throws RemoteException {
		Response<ErpSettlementInfo> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("saleId", saleId);
			rootNode.set("erpAffiliate", getMapper().convertValue(aff, JsonNode.class));
			rootNode.set("paymentResponse", getMapper().convertValue(paymentResponse, JsonNode.class));
			rootNode.put("accountNumber", accountNumber);
			rootNode.put("amount", amount);
			rootNode.put("sequenceNumber", sequenceNumber);
			rootNode.put("description", description);
			rootNode.put("usageCode", usageCode);
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(PROCESS_ECPRETURNS),
					new TypeReference<Response<ErpSettlementInfo>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in processECPReturn: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List loadBadTransactions(Date startDate, Date endDate)
			throws RemoteException {
		Response<List<EFTTransaction>> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("startDate", startDate.getTime());
			rootNode.put("endDate", endDate.getTime());
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOAD_BAD_TRANSACTION),
					new TypeReference<Response<List<EFTTransaction>>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in loadBadTransactions: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List loadReadyToSettleECPSales(Date startDate, int maxNumSales)
			throws RemoteException {
		Response<List<CCDetailOne>> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.put("startDate", startDate.getTime());
			rootNode.put("maxNumSales", maxNumSales);
			
			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOAD_READY_SETTLEMENT),
					new TypeReference<Response<List<CCDetailOne>>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in loadReadyToSettleECPSales: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List loadReadyToSettleECPSales(List<String> saleIds)
			throws RemoteException {
		Response<List<CCDetailOne>> response = null;
		try {
			Request<ObjectNode> request = new Request<ObjectNode>();
			ObjectNode rootNode = getMapper().createObjectNode();
			rootNode.set("saleIds", getMapper().convertValue(saleIds, JsonNode.class));

			request.setData(rootNode);
			String inputJson = buildRequest(request);

			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(LOAD_READY_SETTLEMENT_BY_SALESIDS),
					new TypeReference<Response<List<CCDetailOne>>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in loadReadyToSettleECPSales: data=" + inputJson);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean isSettlementFailedAfterSettled(String saleId)
			throws RemoteException {
		Response<Boolean> response = null;
		try {

			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SETTLEMENT_FAILED_AFTER_SETTLED+saleId),	new TypeReference<Response<Boolean>>() {});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in isSettlementFailedAfterSettled: " );
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List processGCSettlement(String saleId) throws RemoteException {
		Response<List<ErpGCSettlementInfo>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(PROCESS_GC_SETTLEMENT+saleId),	new TypeReference<Response<List<ErpGCSettlementInfo>>>() {});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in processGCSettlement: ");
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List processSettlementPendingOrders() throws RemoteException {
		Response<List<ErpGCSettlementInfo>> response = null;
		try {

			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(PROCESS_SETTLEMENT_PENDING_ORDER),	new TypeReference<Response<List<ErpGCSettlementInfo>>>() {});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in processSettlementPendingOrders: ");
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}



	@Override
	public void sendSettlementReconToSap(InputStream is , String fileName, String workingFolder)
			throws SapException, RemoteException {
		Response<String> response = null;
		MultiValueMap<String, Object> body  = new LinkedMultiValueMap<String, Object>();
			body.add("file", new FileSystemResource(new File(workingFolder+fileName))); 
			body.add("fileName", fileName); 
		try{	
			response = this.postMFormDataTypeMap(body, getFdCommerceEndPoint(UPLOAD_SAP_FILE_TO_BAPI),
					new TypeReference<Response<String>>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in Uploading Sap File to Ecom Env: data=" + body);
				throw new FDResourceException(response.getMessage());
			}

		} catch (FDResourceException e) {
			LOGGER.error("Error in ReconciliationService: ", e);
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void sendFile(InputStream is, final String fileName)
			throws RemoteException {
		MultiValueMap<String, Object> body  = new LinkedMultiValueMap<String, Object>();
		body.add("file", new FileSystemResource(new File(fileName))); 
		body.add("fileName", fileName); 
		Response<String> response = null;
		try {
	

			response = this.postMFormDataTypeMap(body, getFdCommerceEndPoint(UPLOAD_SAP_FILE),
					new TypeReference<String>() {
					});

			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in Uploading Sap File to Ecom Env: data=" + body);
				throw new FDResourceException(response.getMessage());
			}
		}catch (FDResourceException e) {
			LOGGER.error("Error in in Uploading Sap File to Ecom: ", e);
			throw new RemoteException(e.getMessage());
		}
		       
	}
	
	
}