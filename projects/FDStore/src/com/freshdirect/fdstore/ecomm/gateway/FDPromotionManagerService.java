package com.freshdirect.fdstore.ecomm.gateway;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.promotion.management.DowLimit;
import com.freshdirect.ecommerce.data.promotion.management.FDPromoChangeData;
import com.freshdirect.ecommerce.data.promotion.management.FDPromotionNewData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.fdstore.ecomm.gateway.customconverters.promotion.PromotionDTOConverter;
import com.freshdirect.fdstore.promotion.EnumPromotionStatus;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.management.FDPromoChangeModel;
import com.freshdirect.fdstore.promotion.management.FDPromotionNewModel;
import com.freshdirect.fdstore.util.json.FDPromotionJSONSerializer;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.metaparadigm.jsonrpc.JSONSerializer;
import com.metaparadigm.jsonrpc.UnmarshallException;

public class FDPromotionManagerService extends AbstractEcommService implements FDPromotionManagerServiceI {
	private final static Category LOGGER = LoggerFactory.getInstance(FDPromotionManagerService.class);

	private static FDPromotionManagerService INSTANCE = null;
	// this dateformat matches the one in
	// com.freshdirect.ecommerce.web.api.rest.promotion.FDPromotionManagerController
	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private final String SLASH = "/";

	/**
	 * @param args
	 */

	public static FDPromotionManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDPromotionManagerService();

		return INSTANCE;
	}

	private static JSONSerializer ser = new JSONSerializer();
	static {
		try {
			ser.registerDefaultSerializers();
			ser.registerSerializer(FDPromotionJSONSerializer.getInstance());
		} catch (Exception e) {
			
		}
	}
	@Override
	public FDPromotionNewData getPromotionNewDataByPK(String pK) throws FDResourceException {

		Response<FDPromotionNewData> response;
		try {
			// looks like this:
			// promotionmanagement/getpromodtobypk/10297467312
			// promodtobypk/10297467312
			LOGGER.debug("getPromotionnewDataByPK: " + getFdCommerceEndPoint("promotionmanagement/promodtobypk/" + pK));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/promodtobypk/" + pK),
					new TypeReference<Response<FDPromotionNewData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error(response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			FDPromotionNewData data = new FDPromotionNewData();
			// data =
			// ModelConverter.buildFDProductPromotionInfo(response.getData());
			data = response.getData();
			return data;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public FDPromotionNewModel getPromotionByPK(String pK) throws FDResourceException {

		Response<FDPromotionNewData> response;
		try {
			// looks like this:
			// promotionmanagement/promodtobypk/10297467312
			// promodtobypk/10297467312
			LOGGER.debug("getPromotionnewDataByPK: " + getFdCommerceEndPoint("promotionmanagement/promodtobypk/" + pK));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/promodtobypk/" + pK),
					new TypeReference<Response<FDPromotionNewData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error(response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			FDPromotionNewData data = response.getData();
			PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
			FDPromotionNewModel model = dtoConverter.convert(data);

			return model;

			 
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	// getPromotions()
	@Override
	public List<FDPromotionNewModel> getPromotions() throws FDResourceException {

		Response<List<FDPromotionNewData>> response;
		try {

			LOGGER.debug("getAllPromotions: " + getFdCommerceEndPoint("promotionmanagement/allpromotions"));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/allpromotions"),
					new TypeReference<Response<List<FDPromotionNewData>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error(response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			List<FDPromotionNewData> dtoLst = new ArrayList<FDPromotionNewData>();
			// data =
			// ModelConverter.buildFDProductPromotionInfo(response.getData());
			dtoLst = response.getData();

			// List<FDPromotionNewModel> models =
			// FacadeFactoryPromotion.getInstance()
			// .getCustomMapperForPromoNewDataToModel().mapAsList(dtoLst,
			// FDPromotionNewModel.class);

			PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
			List<FDPromotionNewModel> models = dtoConverter.converttoModel(dtoLst);
			return models;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public List<FDPromotionNewModel> getBatchPromotions(String batchId) throws FDResourceException {

		Response<List<FDPromotionNewData>> response;
		try {

			LOGGER.debug("getAllPromotions: " + getFdCommerceEndPoint("promotionmanagement/allpromotions"));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/batchpromotions/" + batchId),
					new TypeReference<Response<List<FDPromotionNewData>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error(response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			List<FDPromotionNewData> dtoLst = new ArrayList<FDPromotionNewData>();
			// data =
			// ModelConverter.buildFDProductPromotionInfo(response.getData());
			dtoLst = response.getData();

			// List<FDPromotionNewModel> models =
			// FacadeFactoryPromotion.getInstance()
			// .getCustomMapperForPromoNewDataToModel().mapAsList(dtoLst,
			// FDPromotionNewModel.class);
			PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
			List<FDPromotionNewModel> models = dtoConverter.converttoModel(dtoLst);
			return models;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "getBatchPromotions Unable to process the request." + batchId);
		}
	}

	@Override
	public List<FDPromotionNewModel> getModifiedOnlyPromotions(Date modifiedDate) throws FDResourceException {

		Response<List<FDPromotionNewData>> response;
		String dateStr = dateFormat.format(modifiedDate);

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/modifiedpromotionsbydate/yyyy-MM-dd'T'HH:mm:ss.SSSZ
		try {

			LOGGER.debug("modifiedpromotionsbydate: " + getFdCommerceEndPoint("modifiedpromotionsbydate/" + dateStr));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("modifiedpromotionsbydate/" + dateStr),
					new TypeReference<Response<List<FDPromotionNewData>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error(
						"coult not getModifiedOnlyPromotions: " + modifiedDate + ",  error: " + response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			List<FDPromotionNewData> dtoLst = new ArrayList<FDPromotionNewData>();
			dtoLst = response.getData();

			// List<FDPromotionNewModel> models =
			// FacadeFactoryPromotion.getInstance()
			// .getCustomMapperForPromoNewDataToModel().mapAsList(dtoLst,
			// FDPromotionNewModel.class);
			PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
			List<FDPromotionNewModel> models = dtoConverter.converttoModel(dtoLst);

			return models;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public String findpromotionIDbyPromoCode(String promoCode) throws FDResourceException {

		Response<String> response;

		LOGGER.debug("findpromotionbypromocode "
				+ getFdCommerceEndPoint("promotionmanagement/findbypromocode/" + promoCode));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/findbypromocode/" + promoCode),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to findpromotionIDbyPromoCode: "
					+ response.getMessage() + " for promoCode: " + promoCode);
		}

		String retval = "";
		// data =
		// ModelConverter.buildFDProductPromotionInfo(response.getData());
		retval = response.getData();
		return retval;

	}

	@Override
	public String deletePromotionByPromoID(String promoID) throws FDResourceException {

		Response<String> response;

		// looks like this:
		// promotionmanagement/getpromodtobypk/10297467312
		/// deletepromotionbyid/{promotionid}
		LOGGER.debug("findpromotionbypromocode "
				+ getFdCommerceEndPoint("promotionmanagement/deletepromotionbyid/" + promoID));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/deletepromotionbyid/" + promoID),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to deletePromotionByPromoID: "
					+ response.getMessage() + " for deletePromotionByPromoID: " + promoID);
		}

		String retval = "";
		// data =
		// ModelConverter.buildFDProductPromotionInfo(response.getData());
		retval = response.getData();
		return retval;

	}

	@Override
	public Boolean isPromotionCodeUsed(String promoCode) throws FDResourceException {

		Response<Boolean> response;

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/ispromotioncodeused/CS_GC_50_49

		LOGGER.debug(
				"isPromotionCodeUsed " + getFdCommerceEndPoint("promotionmanagement/promotioncodeused/" + promoCode));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/promotioncodeused/" + promoCode),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to check if  isPromotionCodeUsed: "
					+ response.getMessage() + " for promocode: " + promoCode);
		}

		// data =
		// ModelConverter.buildFDProductPromotionInfo(response.getData());
		Boolean retval = response.getData();
		return retval;

	}
	// promotionnameused/{name}/

	@Override
	public Boolean isPromotionNameUsed(String promoName) throws FDResourceException {

		Response<Boolean> response;

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/promotionnameused/DAVID

		LOGGER.debug(
				"PromotionNameUsed " + getFdCommerceEndPoint("promotionmanagement/promotionnameused/" + promoName));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/promotionnameused/" + promoName),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to check if  isPromotionNameUsed: "
					+ response.getMessage() + " for promocode: " + promoName);
		}

		Boolean retval = response.getData();
		return retval;

	}

	@Override
	public int getRedemptionCount(String promoID, Date date) throws FDResourceException {

		Response<Integer> response;
		String dateStr = dateFormat.format(date);

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/redemptioncount/10297467312/yyyy-MM-dd'T'HH:mm:ss.SSSZ

		LOGGER.debug("getRedemptionCount "
				+ getFdCommerceEndPoint("promotionmanagement/redemptioncount/" + promoID + SLASH + dateStr));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/redemptioncount/" + promoID + SLASH + dateStr),
				new TypeReference<Response<Integer>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to getRedemptionCount: " + response.getMessage()
					+ " for promoID: " + promoID);
		}

		int retval = response.getData();
		return retval;

	}

	/// redemptionpromotionid/{rc}

	@Override
	public String getRedemptionPromotionId(String redemptionCode) throws FDResourceException {

		Response<String> response;

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/redemptioncount/10297467312/yyyy-MM-dd'T'HH:mm:ss.SSSZ

		LOGGER.debug("getRedemptionPromotionId "
				+ getFdCommerceEndPoint("promotionmanagement/redemptionpromotionid/" + redemptionCode));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/redemptionpromotionid/" + redemptionCode),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to getRedemptionPromotionId: "
					+ response.getMessage() + " for redemptionCode: " + redemptionCode);
		}

		return response.getData();

	}
	/// redemptioncodeexists/{rc}

	@Override
	public boolean isRedemptionCodeExists(String redemptionCode) throws FDResourceException {

		Response<Boolean> response;

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/redemptioncount/10297467312/yyyy-MM-dd'T'HH:mm:ss.SSSZ

		LOGGER.debug("getRedemptionPromotionId "
				+ getFdCommerceEndPoint("promotionmanagement/redemptionpromotionid/" + redemptionCode));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/redemptioncodeexists/" + redemptionCode),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to getRedemptionPromotionId: "
					+ response.getMessage() + " for redemptionCode: " + redemptionCode);
		}

		return response.getData();

	}

	/// redemptioncodeexists/{rc}/{promoid}
	@Override
	public boolean isRedemptionCodeExists(String redemptionCode, String promoId) throws FDResourceException {

		Response<Boolean> response;

		LOGGER.debug("getRedemptionPromotionId " + getFdCommerceEndPoint(
				"promotionmanagement/redemptioncodeexists/" + redemptionCode + SLASH + promoId));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/redemptioncodeexists/" + redemptionCode),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(" failure isRedemptionCodePromoIdExists " + response.getMessage() + " for redemptionCode: "
					+ redemptionCode + " and promo id " + promoId);
			throw new FDResourceException("Webservice Failed, unable to getRedemptionPromotionId: "
					+ response.getMessage() + " for redemptionCode: " + redemptionCode + " and promo id " + promoId);
		}

		return response.getData();

	}

	// raf code instead of promoCode

	@Override
	public boolean isRafPromoCodeExists(String redemptionCode) throws FDResourceException {

		Response<Boolean> response;

		LOGGER.debug("getRedemptionPromotionId "
				+ getFdCommerceEndPoint("promotionmanagement/rafpromocodeexists/" + redemptionCode));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/redemptioncodeexists/" + redemptionCode),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to getRedemptionPromotionId: "
					+ response.getMessage() + " for redemptionCode: " + redemptionCode);
		}

		return response.getData();

	}

	@Override
	public boolean isRafPromoCodeExists(String redemptionCode, String promoId) throws FDResourceException {

		Response<Boolean> response;

		LOGGER.debug("getRedemptionPromotionId "
				+ getFdCommerceEndPoint("promotionmanagement/rafpromocodeexists/" + redemptionCode + SLASH + promoId));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/redemptioncodeexists/" + redemptionCode),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(" failure getRedemptionPromotionId " + response.getMessage() + " for redemptionCode: "
					+ redemptionCode + " and promo id " + promoId);
			throw new FDResourceException("Webservice Failed, unable to getRedemptionPromotionId: "
					+ response.getMessage() + " for redemptionCode: " + redemptionCode + " and promo id " + promoId);
		}

		return response.getData();

	}

	@Override
	public Boolean fixPromotionStatusAfterPublish(Collection<String> promoCodes) throws FDResourceException {
		try {
			Request<Collection<String>> request = new Request<Collection<String>>();
			request.setData(promoCodes);
			String inputJson = buildRequest(request);
			@SuppressWarnings("unchecked")
			Response<Boolean> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/fixpromotion"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("fixpromotionstatusafterpublish:  " + response.getMessage());
				throw new FDResourceException("fixpromotionstatusafterpublish:  " + response.getMessage());
			}
			return response.getData();
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, " fixpromotionstatusafterpublish: Unable to process the request.");
		}
	}
	//

	@Override
	public List<String> loadAssignedCustomerUserIds(String promoId) throws FDResourceException {

		Response<List<String>> response;

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/ispromotioncodeused/CS_GC_50_49

		LOGGER.debug("isPromotionCodeUsed "
				+ getFdCommerceEndPoint("promotionmanagement/loadassignedcustomeruserids/" + promoId));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/loadassignedcustomeruserids/" + promoId),
				new TypeReference<Response<List<String>>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to check loadassignedcustomeruserids: "
					+ response.getMessage() + " for promoId: " + promoId);
		}

		return response.getData();

	}

	// lookuppromotion

	@Override
	public boolean lookupPromotion(String promoCode) throws FDResourceException {

		Response<Boolean> response;

		LOGGER.debug("lookupPromotion " + getFdCommerceEndPoint("promotionmanagement/lookuppromotion/" + promoCode));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/lookuppromotion/" + promoCode),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to getRedemptionPromotionId: "
					+ response.getMessage() + " for promoCode: " + promoCode);
		}

		return response.getData();

	}

	@Override
	public boolean isCustomerInAssignedList(String userId, String promotionId) throws FDResourceException {

		Response<Boolean> response;

		LOGGER.debug("getRedemptionPromotionId "
				+ getFdCommerceEndPoint("promotionmanagement/rustomerinassignedlist/" + userId + SLASH + promotionId));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/rustomerinassignedlist/" + userId + SLASH + promotionId),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(" failure getRedemptionPromotionId " + response.getMessage() + " for userId: " + userId
					+ " and promo id " + promotionId);
			throw new FDResourceException("Webservice Failed, unable to getRedemptionPromotionId: "
					+ response.getMessage() + " for userId: " + userId + " and promo id " + promotionId);
		}

		return response.getData();

	}

	@Override
	public String setDOWLimit(int dayofweek, double limit) throws FDResourceException {

		Response<String> response;

		LOGGER.debug(
				"setDOWLimit " + getFdCommerceEndPoint("promotionmanagement/dowlimit/" + dayofweek + SLASH + limit));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/dowlimit/" + dayofweek + SLASH + limit),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to setDOWLimit: " + response.getMessage()
					+ " for dayofweek: " + dayofweek + " AND limit: " + limit);
		}

		String retval = "";
		// data =
		// ModelConverter.buildFDProductPromotionInfo(response.getData());
		retval = response.getData();
		return retval;

	}
	// getdowlimits
	// hold off on dow limits until you can import the dowLimitDTO class

	@Override
	public Map<Integer, Double> getDOWLimits() throws FDResourceException {

		Response<List<DowLimit>> response;

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/dowlimits

		LOGGER.debug("getDOWLimit " + getFdCommerceEndPoint("promotionmanagement/dowlimits"));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/dowlimits"),
				new TypeReference<Response<List<DowLimit>>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException(
					"Webservice Failed, unable to check loadassignedcustomeruserids: " + response.getMessage());
		}
		Map<Integer, Double> dowLimitMap = new HashMap<Integer, Double>();
		List<DowLimit> dowlimitlst = response.getData();
		for (DowLimit limit : dowlimitlst) {
			dowLimitMap.put(limit.getDayofweek(), limit.getLimit());
		}

		return dowLimitMap;

	}

	@Override
	public Boolean isTSAPromoCodeExists(String tsaCode) throws FDResourceException {

		Response<Boolean> response;

		LOGGER.debug(
				"isPromotionCodeUsed " + getFdCommerceEndPoint("promotionmanagement/tsapromocodeexists/" + tsaCode));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/tsapromocodeexists/" + tsaCode),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to check if  tsapromocodeexists: "
					+ response.getMessage() + " for tsaCode: " + tsaCode);
		}

		// data =
		// ModelConverter.buildFDProductPromotionInfo(response.getData());
		Boolean retval = response.getData();
		return retval;
	}

	@Override
	public Boolean isTSAPromoCodeExists(String tsaCode, String promotionId) throws FDResourceException {

		Response<Boolean> response;

		LOGGER.debug(
				"isPromotionCodeUsed " + getFdCommerceEndPoint("promotionmanagement/tsapromocodeexists/" + tsaCode));
		response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/tsapromocodeexists/" + tsaCode + SLASH + promotionId),
				new TypeReference<Response<Boolean>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to check if  tsapromocodeexists: "
					+ response.getMessage() + " for tsaCode: " + tsaCode + "AND PROMOID: " + promotionId);
		}

		// data =
		// ModelConverter.buildFDProductPromotionInfo(response.getData());
		Boolean retval = response.getData();
		return retval;
	}

	@Override
	public String getRedemptionCode(String tsaCode) throws FDResourceException {

		Response<String> response;

		LOGGER.debug(
				"findpromotionbypromocode " + getFdCommerceEndPoint("promotionmanagement/redemptioncode/" + tsaCode));
		response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/redemptioncode/" + tsaCode),
				new TypeReference<Response<String>>() {
				});
		if (!response.getResponseCode().equals("OK")) {
			LOGGER.error(response.getMessage());
			throw new FDResourceException("Webservice Failed, unable to getRedemptionCodebytsaCode: "
					+ response.getMessage() + " for tsacode: " + tsaCode);
		}

		String retval = "";
		// data =
		// ModelConverter.buildFDProductPromotionInfo(response.getData());
		retval = response.getData();
		return retval;

	}

	@Override
	public PrimaryKey createPromotion(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/createpromotion"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("createPromotion:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"createPromotion failed as unable to build the request json from the input:  " + data.toString());
		}
		return new PrimaryKey(primaryKeyId);

	}

	@Override
	public boolean publishPromotion(FDPromotionNewModel promotion) throws FDResourceException {
		Boolean answer;

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
//		FDPromotionNewData data = FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper()
//				.map(promotion, FDPromotionNewData.class);
		
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<Boolean> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/publishpromotion"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("publishpromotion:  " + response.getMessage());
				throw new FDResourceException("publishpromotion:  " + response.getMessage());
			}
			answer = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"publishpromotion failed as unable to build the request json from the input:  " + data.toString());
		}
		return answer;

	}

	@Override
	public PrimaryKey createPromotionBatch(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/createpromotionbatch"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("createpromotionbatch:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"createPromotion failed as unable to build the request json from the input:  " + data.toString());
		}
		return new PrimaryKey(primaryKeyId);

	}

	@Override
	public void createPromotions(List<FDPromotionNewModel> promotions) throws FDResourceException {

		Request<List<FDPromotionNewData>> request = new Request<List<FDPromotionNewData>>();
		// List<FDPromotionNewData> datas =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().mapAsList(promotions,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		List<FDPromotionNewData> datas = dtoConverter.converttoDTO(promotions);
		request.setData(datas);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/createpromotions"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("createPromotion:  " + response.getMessage());
			}
			// the response contains "OK", ignore it.
			// primaryKeyId= response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"createPromotion failed as unable to build the request json from the input:  " + datas.toString());
		}
		return;

	}

	@Override
	public PrimaryKey createPromotionBasic(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/createpromotionbasic"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("createPromotionBasic:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"createPromotionBasic failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return new PrimaryKey(primaryKeyId);

	}

	@Override
	public PrimaryKey storePromotionBasic(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotionbasic"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("storepromotionbasic:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storepromotionbasic failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return new PrimaryKey(primaryKeyId);

	}

	@Override
	public PrimaryKey storePromotionOfferInfo(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotionofferinfo"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("storePromotionOfferInfo:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storePromotionOfferInfo failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return new PrimaryKey(primaryKeyId);

	}

	@Override
	public void storePromotionCartInfo(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotioncartinfo"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException(
						"storePromotiostorepromotioncartinfonOfferInfo:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storepromotioncartinfo failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return;

	}

	@Override
	public PrimaryKey storePromotion(FDPromotionNewModel promotion, boolean savelog) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storePromotion/" + savelog), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("failure with storePromotion:  " + promotion.getId() + ", " + response.getMessage());
				throw new FDResourceException(
						" failure with storePromotion:  " + promotion.toString() + ", " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e, "failure with storePromotion:  " + data.toString());
		}
		return new PrimaryKey(primaryKeyId);

	}

	@Override
	public List<FDPromotionNewModel> getPromotionsByYear(int year) throws FDResourceException {

		Response<List<FDPromotionNewData>> response;
		try {

			LOGGER.debug(
					"getPromotionsByYear: " + getFdCommerceEndPoint("promotionmanagement/promotionsbyyear/" + year));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionsbyyear/" + year),
					new TypeReference<Response<List<FDPromotionNewData>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("coult not get promotions by year: " + year + ",  error: " + response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			List<FDPromotionNewData> dtoLst = new ArrayList<FDPromotionNewData>();
			// data =
			// ModelConverter.buildFDProductPromotionInfo(response.getData());
			dtoLst = response.getData();

			// List<FDPromotionNewModel> models =
			// FacadeFactoryPromotion.getInstance()
			// .getCustomMapperForPromoNewDataToModel().mapAsList(dtoLst,
			// FDPromotionNewModel.class);
			PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
			List<FDPromotionNewModel> models = dtoConverter.converttoModel(dtoLst);
			return models;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public List<FDPromotionNewModel> getPublishablePromos() throws FDResourceException {

		Response<List<FDPromotionNewData>> response;
		try {

			LOGGER.debug("getPublishablePromos: " + getFdCommerceEndPoint("promotionmanagement/publishablepromos"));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("publishablepromos/"),
					new TypeReference<Response<List<FDPromotionNewData>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("could not getPublishablePromos: " + ",  error: " + response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			List<FDPromotionNewData> dtoLst = new ArrayList<FDPromotionNewData>();

			dtoLst = response.getData();

			// List<FDPromotionNewModel> models =
			// FacadeFactoryPromotion.getInstance()
			// .getCustomMapperForPromoNewDataToModel().mapAsList(dtoLst,
			// FDPromotionNewModel.class);
			PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
			List<FDPromotionNewModel> models = dtoConverter.converttoModel(dtoLst);

			return models;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}

	@Override
	public boolean cancelPromotion(FDPromotionNewModel promotion) throws FDResourceException {
		Boolean isCancelled;

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<Boolean> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/cancelpromotion"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("cancelPromotion:  " + response.getMessage());
				throw new FDResourceException("cancelPromotion:  " + response.getMessage());
			}
			isCancelled = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"cancelPromotion failed as unable to build the request json from the input:  " + data.toString());
		}
		return isCancelled;

	}

	@Override
	public void storePromotionPaymentInfo(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotionpaymentinfo"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("storePromotionPaymentInfo:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storePromotionPaymentInfo failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return;

	}

	@Override
	public void storePromotionDlvZoneInfo(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotiondlvzoneinfo"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("storepromotiondlvzoneinfo:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storepromotiondlvzoneinfo failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return;

	}

	@Override
	public void storePromotionCustReqInfo(FDPromotionNewModel promotion) throws FDResourceException {
		String primaryKeyId = "";

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotioncustreqinfo"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("storepromotioncustreqinfo:  " + response.getMessage());
			}
			primaryKeyId = response.getData();
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storepromotioncustreqinfo failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return;

	}

	@Override
	public void storePromotionStatus(FDPromotionNewModel promotion, EnumPromotionStatus status)
			throws FDResourceException {

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotionstatus/" + status.getName()),
					Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("createPromotion:  " + response.getMessage());
				throw new FDResourceException("storepromotionstatus:  " + response.getMessage());
			}

		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storepromotionstatus failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return;

	}

	//
	@Override
	public void storeAssignedCustomers(FDPromotionNewModel promotion, String assignedCustomerUserIds)
			throws FDResourceException {

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storeassignedcustomers/" + assignedCustomerUserIds),
					Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("storeAssignedCustomers:  " + response.getMessage());
				throw new FDResourceException("storeAssignedCustomers:  " + response.getMessage());
			}

		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storeAssignedCustomers failed as unable to build the request json from the input:  "
							+ data.toString() + " customers:" + assignedCustomerUserIds);
		}
		return;

	}

	@Override
	public void storePromotionHoldStatus(FDPromotionNewModel promotion) throws FDResourceException {

		Request<FDPromotionNewData> request = new Request<FDPromotionNewData>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		FDPromotionNewData data = dtoConverter.convert(promotion);
		request.setData(data);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storepromotionholdstatus"), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("storePromotionHoldStatus:  " + response.getMessage());
				throw new FDResourceException("storepromotionholdstatus:  " + response.getMessage());
			}

		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e,
					"storepromotionholdstatus failed as unable to build the request json from the input:  "
							+ data.toString());
		}
		return;

	}

	//

	@Override
	public List<FDPromoChangeModel> loadPromoAuditChanges(String promotionId) throws FDResourceException {

		Response<List<FDPromoChangeData>> response;
		try {

			LOGGER.debug("loadPromoAuditChanges: "
					+ getFdCommerceEndPoint("promotionmanagement/promoauditchanges/" + promotionId));
			response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/promoauditchanges/" + promotionId),
					new TypeReference<Response<List<FDPromoChangeData>>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error(response.getMessage());
				throw new FDResourceException(response.getMessage());
			}

			List<FDPromoChangeData> dtoLst = new ArrayList<FDPromoChangeData>();

			dtoLst = response.getData();

			PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
			List<FDPromoChangeModel> models = dtoConverter.convertFDPromoChangeDataLst(dtoLst);
			return models;
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new FDResourceException(e, "Unable to process the request.");
		}
	}
	//

	@Override

	public void storeChangeLogEntries(String promoPk, List<FDPromoChangeModel> changes) throws FDResourceException {

		Request<List<FDPromoChangeData>> request = new Request<List<FDPromoChangeData>>();
		// FDPromotionNewData data =
		// FacadeFactoryPromotionDTO.getInstance().getPromotionNewModelToDataMapper().map(promotion,FDPromotionNewData.class
		// );
		PromotionDTOConverter dtoConverter = new PromotionDTOConverter();
		List<FDPromoChangeData> datalst = dtoConverter.convertFDPromoChangeModelLst(changes);

		request.setData(datalst);
		String inputJson;
		try {
			inputJson = buildRequest(request);

			@SuppressWarnings("unchecked")
			Response<String> response = this.postData(inputJson,
					getFdCommerceEndPoint("promotionmanagement/storechangelogentries/" + promoPk), Response.class);
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("failure with storeChangeLogEntries:  " + promoPk + ", " + response.getMessage());
				throw new FDResourceException(
						" failure with storeChangeLogEntries:  " + promoPk + ", " + response.getMessage());
			}
			String responsemsg = response.getData();
			LOGGER.debug(this.getClass().getName() + " recieved: " + responsemsg);
		} catch (FDEcommServiceException e) {
			throw new FDResourceException(e, "failure with storeChangeLogEntries:  ");
		}

		return;

	}
	
	public List<PromotionI> getAllAutomaticPromotions() throws FDResourceException{		
		try {
			Response<String> response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/allautomaticpromotions/" ), new TypeReference<Response<String>>(){});		
			List<PromotionI> promotions =(List<PromotionI>)ser.fromJSON(response.getData());
			return promotions;
		} catch (UnmarshallException e) {
			throw new FDResourceException(e, "failure with getAllAutomaticPromotions:  ");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
