package com.freshdirect.fdstore.ecoupon.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.certicom.security.cert.internal.x509.Base64;
import com.freshdirect.customer.ErpOrderLineModel;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.fdstore.ecoupon.model.CouponCart;
import com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponCartResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponMetaDataResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCouponResponse;
import com.freshdirect.fdstore.ecoupon.model.yt.YTCustomerCouponResponse;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class YTCouponProvider implements CouponService {
	
	private static final String YT_PARAM_SITE_ID = "site_id";	
	private static final String YT_PARAM_RETAILER_FAMILY_ID = "retailer_family_id";
	private static final String YT_PARAM_RETAILER_NAME = "retailer_name";
	private static final String YT_PARAM_RULE = "rule";
	private static final String YT_PARAM_TEMP_CARD_NUMBER = "temp_card_number";
	private static final String YT_PARAM_CARD_NUMBER = "card_number";
	private static final String YT_PARAM_TRANSACTION_ID = "transaction_id";
	private static final String YT_PARAM_ITEM_LIST = "item_list";;
	private static final String YT_PARAM_COUPON_ID_LIST = "coupon_id_list";
	
	private static final Logger LOGGER = LoggerFactory.getInstance(YTCouponProvider.class);
	public static final String charSet = "UTF-8";
	
	
	public void doClipCoupon(FDCouponCustomer couponCustomer, List couponIds)
		throws CouponServiceException {
	}
	
	public YTCouponMetaDataResponse getCoupons() throws CouponServiceException {
		
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		String urlToCallStr = CouponConfigProvider.getYTCouponsMetaURL();		
		urlParameters.put(YT_PARAM_RETAILER_FAMILY_ID, CouponConfigProvider.getRetailerId());
		StringBuilder urlToCall = getBaseUrl(urlToCallStr, urlParameters);
		String jsonResponse = sendGetRequest(urlToCall);
		YTCouponResponse response = parseResponse(jsonResponse, YTCouponMetaDataResponse.class);
		return (YTCouponMetaDataResponse)response;
		
	}

	public YTCustomerCouponResponse getCouponsForUser(FDCouponCustomer couponCustomer) throws CouponServiceException {
		
		
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		String urlToCallStr = CouponConfigProvider.getYTCustomerCouponsURL();
		addRetailerName(urlParameters);
//		LOGGER.debug("getCouponsForUser>"+couponCustomer.getCouponUserId()+" || "+couponCustomer.getCouponCustomerId());
		if(couponCustomer.getCouponUserId() == null && couponCustomer.getCouponCustomerId() == null){
			throw new CouponServiceException();
		}else{
			if(couponCustomer.getCouponCustomerId() != null){
				urlParameters.put(YT_PARAM_CARD_NUMBER, couponCustomer.getCouponCustomerId());
			}
			if(couponCustomer.getCouponUserId() != null){
				urlParameters.put(YT_PARAM_TEMP_CARD_NUMBER, couponCustomer.getCouponUserId());
			}
			if(couponCustomer.getZipCode() != null){
				urlParameters.put(YT_PARAM_RULE, "ZIP|"+couponCustomer.getZipCode());
			}
		}
		
		StringBuilder urlToCall = getBaseUrl(urlToCallStr, urlParameters);
		String jsonResponse = sendGetRequest(urlToCall);
		YTCouponResponse response = parseResponse(jsonResponse,YTCustomerCouponResponse.class);
		return (YTCustomerCouponResponse)response;
	}
	
	private String sendGetRequest(StringBuilder urlToCall)  throws CouponServiceException  {
		String returnString=null;
		HttpURLConnection connection = null;		
		try {
			connection = (HttpURLConnection) new URL(urlToCall.toString()).openConnection();
			connection.setRequestProperty("Accept-Charset",charSet);
			connection.setConnectTimeout(CouponConfigProvider.getYTConnectionTimeoutPeriod()*1000);
			connection.setReadTimeout(CouponConfigProvider.getYTReadTimeoutPeriod()*1000);
			returnString = sendRequest(connection);
		} catch (MalformedURLException mal) {
			LOGGER.error("Exception while making a call to YT: "+mal);
			throw new CouponServiceException(mal);
		} catch (UnsupportedEncodingException unsup) {
			LOGGER.error("Exception while making a call to YT: "+unsup);
			throw new CouponServiceException(unsup);
		} catch (IOException io) {
			LOGGER.error("Exception while making a call to YT: "+io);
			throw new CouponServiceException(io);
		} finally {
			try {
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				LOGGER.warn("Exception while disconnecting the httpconnection: ", e);
			}
		}
//		LOGGER.debug("callYT/Response = "+returnString);
		return returnString;
	}
	
        private String sendRequest(HttpURLConnection connection)
                        throws IOException, UnsupportedEncodingException {
                String returnString="";
                InputStream response = null;
                try {
                        response = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response,charSet));
                        StringBuilder responseString = new StringBuilder();
                        for ( String oneLine; (oneLine = reader.readLine()) != null; ) {
                                responseString.append(oneLine);
                        }
                        returnString = responseString.toString();
                } catch (IOException e) {
                        // handle an exception
                } finally { //  finally blocks are guaranteed to be executed
                        try {
                                if (response != null) {
                                        response.close();
                                }
                        } catch (IOException e) {
                                // do nothing - intentionally empty
                        }
                }
                return returnString;
        }
	
	private StringBuilder getBaseUrl(String urlToCallStr, TreeMap<String,String> urlParameters) throws CouponServiceException {
		
        urlParameters.put(YT_PARAM_SITE_ID, CouponConfigProvider.getSiteId());
//        urlParameters.put("retailer_family_id", retailerId);
                
        StringBuilder urlToCall = new StringBuilder();
        urlToCall.append(CouponConfigProvider.getUrl());
        urlToCall.append(CouponConfigProvider.getVersion());
        urlToCall.append(urlToCallStr);
        
        try {
            String mac = createMacLocally(urlParameters, CouponConfigProvider.getSignature());
            if ( mac != null ) {
                boolean first = true;
                for ( String key : urlParameters.keySet() ) {
                    if ( first) {
                        first = false;
                    } else {
                        urlToCall.append("&");
                    }
                    urlToCall.append(key).append("=").append(URLEncoder.encode(urlParameters.get(key),charSet));
                }
                urlToCall.append("&mac=").append(mac);
            }
        }catch(UnsupportedEncodingException unsup){
        	throw new CouponServiceException(unsup);
        }catch(NoSuchAlgorithmException nsuch){
        	throw new CouponServiceException(nsuch);
        }
        return urlToCall;
	}

	private static String createMacLocally(TreeMap<String,String> parameters,String signature)
            throws NoSuchAlgorithmException,UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (String key : parameters.keySet()) {
            sb.append(parameters.get(key));
        }
        sb.append(signature);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        byte[] macBytes = messageDigest.digest(sb.toString().getBytes());
        return java.net.URLEncoder.encode(Base64.encode(macBytes), charSet);
    }

	private static YTCouponResponse parseResponse(String responseStr, Class _class){
		YTCouponResponse response=null;
		try {
			if(null !=responseStr){
				Object obj =getMapper().readValue(responseStr, _class);
				if(null != obj && obj instanceof YTCouponResponse){
					response=(YTCouponResponse)obj;
				}
			}
		} catch (JsonMappingException e) {
			LOGGER.info("Exception while parsing coupon response from YT: "+e);
		} catch(JsonParseException e){
			LOGGER.info("Exception while parsing coupon response from YT: "+e);
		} catch (IOException e) {
			LOGGER.info("Exception while parsing coupon response from YT: "+e);
        }
		return response;
	}
	
	protected static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES , false);
        return mapper;
    }
	

	public void close() {
		// TODO Auto-generated method stub		
	}

	/* (non-Javadoc)
	 * @see com.freshdirect.fdstore.ecoupon.service.CouponService#doClipCoupon(java.lang.String, com.freshdirect.fdstore.ecoupon.model.FDCouponCustomer)
	 */
	@Override
	public  YTCouponResponse doClipCoupon(String couponId, FDCouponCustomer couponCustomer) throws CouponServiceException {
		String urlToCallStr = CouponConfigProvider.getYTClipCouponsURL();
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		addRetailerName(urlParameters);
		if((couponCustomer.getCouponUserId() == null && couponCustomer.getCouponCustomerId() == null) || (couponId==null ||"".equals(couponId))){
			throw new CouponServiceException();
		}else{
			if(couponCustomer.getCouponCustomerId() != null){
				urlParameters.put(YT_PARAM_CARD_NUMBER,couponCustomer.getCouponCustomerId());
			}else if(couponCustomer.getCouponUserId() != null){
				urlParameters.put(YT_PARAM_CARD_NUMBER,couponCustomer.getCouponUserId());
			}
		}
		urlParameters.put(YT_PARAM_COUPON_ID_LIST,couponId);		
		StringBuilder urlToCall = getBaseUrl(urlToCallStr,urlParameters);
//		LOGGER.debug("doClipCoupon()->Request URL: "+urlToCall);
		String jsonResponse = sendGetRequest(urlToCall);
		YTCouponResponse response = parseResponse(jsonResponse,YTCouponResponse.class);
		return response;
	}

	
	public YTCouponCartResponse submitOrder(CouponCart couponCart) throws CouponServiceException{
		String urlToCallStr = CouponConfigProvider.getYTSubmitCreateOrderURL();
		if(EnumCouponTransactionType.MODIFY_ORDER.equals(couponCart.getTranType())){
			 urlToCallStr = CouponConfigProvider.getYTSubmitModifyOrderURL();
		}
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		addRetailerName(urlParameters);
		if( null == couponCart || null==couponCart.getCouponCustomer()||couponCart.getOrderId()==null||
				(couponCart.getCouponCustomer().getCouponUserId() == null && couponCart.getCouponCustomer().getCouponCustomerId() == null)){
			throw new CouponServiceException();
		}
		List<ErpOrderLineModel> orderLines =couponCart.getOrderLines();
		if(null != orderLines && !orderLines.isEmpty()){
			StringBuffer itemList = new StringBuffer();
			int i=0;
			String ean13 = null;
			for (Iterator iterator = orderLines.iterator(); iterator.hasNext();) {
				ErpOrderLineModel erpOrderLineModel = (ErpOrderLineModel) iterator.next();
//				LOGGER.debug("Orderline UPC->"+erpOrderLineModel.getUpc());
				if(null !=erpOrderLineModel.getUpc() && erpOrderLineModel.getUpc().length() == 12) { // We will be sending only 12 digit upc order line UPC-A
					ean13 = StringUtil.convertToEan13(erpOrderLineModel.getUpc());//0007940003864
//					LOGGER.debug("EAN13 Orderline UPC->"+ean13);
					if(StringUtil.isNumeric(ean13)){
						if(i!=0){
							itemList.append(",");
						}
						itemList.append(ean13+"|"+(erpOrderLineModel.getPriceWithoutCoupons()/(int)erpOrderLineModel.getQuantity())+"|"+(int)erpOrderLineModel.getQuantity());
						i++;
					}
				}				
			}
			urlParameters.put(YT_PARAM_ITEM_LIST,itemList.toString());
			if(couponCart.getCouponCustomer().getCouponCustomerId() != null){
				urlParameters.put(YT_PARAM_CARD_NUMBER, couponCart.getCouponCustomer().getCouponCustomerId());
			}
			/*if(null!=couponCart.getCouponCustomer().getCouponUserId()){
				urlParameters.put(YT_PARAM_TEMP_CARD_NUMBER,couponCart.getCouponCustomer().getCouponUserId());
			}*/
			
			urlParameters.put(YT_PARAM_TRANSACTION_ID, couponCart.getOrderId());
		}
		StringBuilder urlToCall = getBaseUrl(urlToCallStr,urlParameters);
//		appendMac(urlParameters);	
//		LOGGER.debug("submitOrder()->Request URL: "+urlToCall.toString());
		String jsonResponse = sendGetRequest(urlToCall);
//		LOGGER.debug("Resonse:"+jsonResponse);
		YTCouponResponse response = parseResponse(jsonResponse,YTCouponCartResponse.class);
		return (YTCouponCartResponse)response;
	}

	@Override
	public YTCouponCartResponse evaluateCartAndCoupons(CouponCart couponCart) throws CouponServiceException {
		String urlToCallStr = CouponConfigProvider.getYTPreviewCartCouponsURL();
		if(EnumCouponTransactionType.PREVIEW_MODIFY_ORDER.equals(couponCart.getTranType())){
			 urlToCallStr = CouponConfigProvider.getYTPreviewModifyCartCouponsURL();
		}
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		addRetailerName(urlParameters);
		if( (null == couponCart || null== couponCart.getCouponCustomer())||
				(couponCart.getCouponCustomer().getCouponUserId() == null && couponCart.getCouponCustomer().getCouponCustomerId() == null)){
			throw new CouponServiceException();
		}
		List<ErpOrderLineModel> orderLines =couponCart.getOrderLines();
		if(null != orderLines && !orderLines.isEmpty()){
			StringBuffer itemList = new StringBuffer();
			int i=0;
			String ean13 = null;
			for (Iterator iterator = orderLines.iterator(); iterator.hasNext();) {					
					ErpOrderLineModel erpOrderLineModel = (ErpOrderLineModel) iterator.next();
//					LOGGER.debug("Orderline UPC->"+erpOrderLineModel.getUpc());
					if(null !=erpOrderLineModel.getUpc() && erpOrderLineModel.getUpc().length() == 12) { // We will be sending only 12 digit upc order line UPC-A
						ean13 = StringUtil.convertToEan13(erpOrderLineModel.getUpc());//0007940003864
						if(StringUtil.isNumeric(ean13)){
//							LOGGER.debug("EAN13 Orderline UPC->"+ean13);
//							if(ean13.indexOf("FD")<=-1){
								if(i!=0){
									itemList.append(",");
								}
								itemList.append(ean13+"|"+(erpOrderLineModel.getPrice()/(int)erpOrderLineModel.getQuantity())+"|"+(int)erpOrderLineModel.getQuantity());
								i++;
//							}
						}
					}
			}
			urlParameters.put(YT_PARAM_ITEM_LIST,itemList.toString());
			
			if(couponCart.getCouponCustomer().getCouponCustomerId() != null){
				urlParameters.put(YT_PARAM_CARD_NUMBER, couponCart.getCouponCustomer().getCouponCustomerId());
			}
			if(EnumCouponTransactionType.PREVIEW_MODIFY_ORDER.equals(couponCart.getTranType())){
				urlParameters.put(YT_PARAM_TRANSACTION_ID, couponCart.getOrderId());
			}else if(null!=couponCart.getCouponCustomer().getCouponUserId()){
				urlParameters.put(YT_PARAM_TEMP_CARD_NUMBER,couponCart.getCouponCustomer().getCouponUserId());
			}
		}
		
		StringBuilder urlToCall = getBaseUrl(urlToCallStr,urlParameters);		
//		appendMac(urlParameters);	
//		LOGGER.debug("evaluateCartAndCoupons()->Request URL: "+urlToCall.toString());
		String jsonResponse = sendGetRequest(urlToCall);
//		LOGGER.debug("Response: "+jsonResponse);
		YTCouponResponse response = parseResponse(jsonResponse,YTCouponCartResponse.class);
		return (YTCouponCartResponse)response;
	}

	private void appendMac(TreeMap<String, String> urlParameters)
			throws CouponServiceException {
		try {
			String mac = createMacLocally(urlParameters, CouponConfigProvider.getSignature());
			if(null !=mac){
				urlParameters.put("mac", mac);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new CouponServiceException(e);
		} catch (UnsupportedEncodingException e) {
			throw new CouponServiceException(e);
		}
	}

	@Override
	public YTCouponResponse cancelOrder(String orderId)
			throws CouponServiceException {
		String urlToCallStr = CouponConfigProvider.getYTCancelOrderURL();
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		addRetailerName(urlParameters);
		if( orderId==null){
			throw new CouponServiceException();
		}
		
		urlParameters.put(YT_PARAM_TRANSACTION_ID, orderId);
		
		StringBuilder urlToCall = getBaseUrl(urlToCallStr,urlParameters);
//		LOGGER.debug("cancelOrder()->Request URL: "+urlToCall);
		String jsonResponse = sendGetRequest(urlToCall);
		YTCouponResponse response = parseResponse(jsonResponse,YTCouponResponse.class);
		return response;
	}

	@Override
	public YTCouponResponse confirmOrder(String orderId,Set<String> coupons)
			throws CouponServiceException {
		String urlToCallStr = CouponConfigProvider.getYTConfirmOrderURL();
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		addRetailerName(urlParameters);
		if( orderId==null){
			throw new CouponServiceException();
		}
		urlParameters.put(YT_PARAM_TRANSACTION_ID, orderId);
		if(null != coupons && !coupons.isEmpty()){
			String couponsStr = coupons.toString().replace("[", "").replace("]", "");
			urlParameters.put(YT_PARAM_COUPON_ID_LIST , couponsStr);
		}
		StringBuilder urlToCall = getBaseUrl(urlToCallStr,urlParameters);
//		LOGGER.debug("confirmOrder()->Request URL: "+urlToCall);
		String jsonResponse = sendGetRequest(urlToCall);
		YTCouponResponse response = parseResponse(jsonResponse,YTCouponResponse.class);
		return response;
	}

	private void addRetailerName(TreeMap<String, String> urlParameters) {
		urlParameters.put(YT_PARAM_RETAILER_NAME, CouponConfigProvider.getRetailerName());
	}

}
