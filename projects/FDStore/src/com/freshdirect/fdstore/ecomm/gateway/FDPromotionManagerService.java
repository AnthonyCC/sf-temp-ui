package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
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
import com.freshdirect.fdstore.EnumEStoreId;
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
	final DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	final DateFormat dateTimeFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
	public int getRedemptionCount(String promoID, Date date) throws FDResourceException {

		Response<Integer> response;
		String dateStr = (null !=date? dateFormat.format(date): null);

		// looks like this:
		// fdcommerceapi/fd/v1/promotionmanagement/redemptioncount/10297467312/yyyy-MM-dd'T'HH:mm:ss.SSSZ

		LOGGER.debug("getRedemptionCount "
				+ getFdCommerceEndPoint("promotionmanagement/redemptioncount/" + promoID + SLASH + dateStr));
		if(null !=dateStr){
			response = httpGetDataTypeMap(
				getFdCommerceEndPoint("promotionmanagement/redemptioncount/" + promoID + SLASH + dateStr),
				new TypeReference<Response<Integer>>() {
				});
		}else{
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint("promotionmanagement/redemptioncountbypromocode/" + promoID ),
					new TypeReference<Response<Integer>>() {
					});
		}
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

	

	
	
	
	public List<PromotionI> getAllAutomaticPromotions() throws FDResourceException{		
		try {
			Response<String> response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/allautomaticpromotions/" ), new TypeReference<Response<String>>(){});		
			List<PromotionI> promotions =(List<PromotionI>)ser.fromJSON(response.getData());
			return promotions;
		} catch (UnmarshallException e) {
			throw new FDResourceException(e, "failure with getAllAutomaticPromotions:  ");
		}
	}
	
	public PromotionI getPromotionForRT(String promoCode) throws FDResourceException{
		try {
			Response<String> response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/promoforrt/"+promoCode ), new TypeReference<Response<String>>(){});		
			PromotionI promotion =(PromotionI)ser.fromJSON(response.getData());
			return promotion;
		} catch (UnmarshallException e) {
			throw new FDResourceException(e, "failure with getPromotionForRT:  ");
		}
	}

	public List<PromotionI> getModifiedOnlyPromos(Date lastModified) throws FDResourceException {
		try {
			String dateStr = dateTimeFormat.format(lastModified);
			Response<String> response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/modifiedpromosbydate/"+dateStr), new TypeReference<Response<String>>(){});		
			List<PromotionI> promotions =(List<PromotionI>)ser.fromJSON(response.getData());
			return promotions;
		} catch (UnmarshallException e) {
			throw new FDResourceException(e, "failure with getModifiedOnlyPromos:  ");
		}
	}
	
	public List<PromotionI> getReferralPromotions(String customerId, EnumEStoreId storeid) throws FDResourceException {
		
		try {
			Response<String> response = httpGetDataTypeMap(getFdCommerceEndPoint("promotionmanagement/referralpromotions/"+customerId+SLASH+(null!=storeid?storeid.getContentId():EnumEStoreId.FD.getContentId())), new TypeReference<Response<String>>(){});		
			List<PromotionI> promotions =(List<PromotionI>)ser.fromJSON(response.getData());
			return promotions;
		} catch (UnmarshallException e) {
			throw new FDResourceException(e, "failure with getReferralPromotions:  ");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
