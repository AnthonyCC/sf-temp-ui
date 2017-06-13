package com.freshdirect.fdstore.brandads.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.freshdirect.fdstore.brandads.HLOrderFeedDataModel;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.framework.util.log.LoggerFactory;

public class HLBrandProductAdServiceProvider implements BrandProductAdService {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(HLBrandProductAdServiceProvider.class);
	public static final String charSet = "UTF-8";
	private static final String HOOKLOGIC_APIKEY = "apiKey";
	private static final String HOOKLOGIC_PUSERID = "puserid";
	private static final String HOOKLOGIC_KEYWORD = "keyword";
	private static final String HOOKLOGIC_TAXONOMY = "taxonomy";
	private static final String HOOKLOGIC_PLATFORM="platform";
	private static final String HOOKLOGIC_IC="ic";
	private static final String HOOKLOGIC_CULTURE="culture";
	private static final String HOOKLOGIC_MEDIASOURCE="mediasource";
	private static final String HOOKLOGIC_HLPT="hlpt";
	private static final String HOOKLOGIC_STRATEGY="strategy";
	private static final String HOOKLOGIC_PRICE="price";
	private static final String HOOKLOGIC_TOTAL="total";
	private static final String HOOKLOGIC_PRODUCTID="productid";
	private static final String  HOOKLOGIC_ORDERID="orderid";
	private static final String HOOKLOGIC_QUANTITY="quantity";
	private static final String HOOKLOGIC_CUSERID="cuserid";
	private static final String HOOKLOGIC_CREATIVE="creative";
	private static final String HOOKLOGIC_ALLOWMARKETPLACE="allowMarketplace";
	private static final String HOOKLOGIC_MINMES="minmes";
	private static final String HOOKLOGIC_MAXMES="maxmes";
	private static final String HOOKLOGIC_PGN="pgn";
	
	
	private BrandProductAdConfigProvider hlAdConfigProvider = HLBrandProductAdConfigProvider.getInstance();
	
	
	public HLBrandProductAdResponse getSearchbykeyword(HLBrandProductAdRequest hLRequestData) throws BrandProductAdServiceException {
		
		
		LOGGER.info("while making a call to Hook Logic search word: "+hLRequestData.getSearchKeyWord());
		
		StringBuilder urlToCallStr=new StringBuilder(hlAdConfigProvider.getBrandProductAdProviderURL());
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();

		/*if we want to test the Hook logic API just pass the puserId and KeyWord below comment code and need to be commented below HLBrandProductAdRequest.java properties(getUserId,getSearchKeyWord). 
			urlParameters.put(HOOKLOGIC_PUSERID, "1234");
			urlParameters.put(HOOKLOGIC_KEYWORD, "soda");*/
		
		urlParameters.put(HOOKLOGIC_APIKEY, hlAdConfigProvider.getBrandProductAdProviderAPIKey());
		urlParameters.put(HOOKLOGIC_PUSERID, hLRequestData.getUserId());
		urlParameters.put(HOOKLOGIC_KEYWORD, hLRequestData.getSearchKeyWord());
		urlParameters.put(HOOKLOGIC_TAXONOMY, "");
		urlParameters.put(HOOKLOGIC_PLATFORM, hLRequestData.getPlatformSource());
		//urlParameters.put(HOOKLOGIC_PLATFORM, hlAdConfigProvider.getBrandProductAdProviderPlatform());
		urlParameters.put(HOOKLOGIC_IC, hlAdConfigProvider.getBrandProductAdProviderIc());
		urlParameters.put(HOOKLOGIC_CULTURE, hlAdConfigProvider.getBrandProductAdProviderCulture());
		urlParameters.put(HOOKLOGIC_MEDIASOURCE, hlAdConfigProvider.getBrandProductAdProviderMediaSource());
		urlParameters.put(HOOKLOGIC_HLPT, hlAdConfigProvider.getBrandProductAdProviderHlpt());
		urlParameters.put(HOOKLOGIC_STRATEGY, hlAdConfigProvider.getBrandProductAdProviderStrategy());
		StringBuilder urlToCall = getBaseUrl(urlToCallStr, urlParameters);
		String jsonResponse = sendGetRequest(urlToCall);
		HLBrandProductAdResponse response = parseResponse(jsonResponse, HLBrandProductAdResponse.class);
		return response;
		
	}
	
public HLBrandProductAdResponse getCategoryProducts(HLBrandProductAdRequest hLRequestData) throws BrandProductAdServiceException {
		
		LOGGER.info("while making a call to Hook Logic Category Productslist from Hook Loogic: "+hLRequestData.getCategoryId());
		
		StringBuilder urlToCallStr=new StringBuilder(hlAdConfigProvider.getBrandProductAdProviderCategoryURL());
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		urlParameters.put(HOOKLOGIC_APIKEY, hlAdConfigProvider.getBrandProductAdProviderAPIKey());
		urlParameters.put(HOOKLOGIC_PUSERID, hLRequestData.getUserId());
		urlParameters.put(HOOKLOGIC_CUSERID, hLRequestData.getCustomerId()==null?"":hLRequestData.getCustomerId());
		urlParameters.put(HOOKLOGIC_TAXONOMY, hLRequestData.getCategoryId());
		urlParameters.put(HOOKLOGIC_PLATFORM, hLRequestData.getPlatformSource());
		//urlParameters.put(HOOKLOGIC_PLATFORM, hlAdConfigProvider.getBrandProductAdProviderPlatform());
		urlParameters.put(HOOKLOGIC_ALLOWMARKETPLACE, hlAdConfigProvider.getBrandProductAdProviderAallowMarketplace());
		urlParameters.put(HOOKLOGIC_MINMES, hlAdConfigProvider.getBrandProductAdProviderMinmes());
		urlParameters.put(HOOKLOGIC_MAXMES, hlAdConfigProvider.getBrandProductAdProviderMaxmes());
		urlParameters.put(HOOKLOGIC_PGN, hlAdConfigProvider.getBrandProductAdProviderPgn());
		urlParameters.put(HOOKLOGIC_HLPT, hlAdConfigProvider.getBrandProductAdProviderCategoryHlpt());
		urlParameters.put(HOOKLOGIC_CREATIVE, hlAdConfigProvider.getBrandProductAdProviderCreative());
		StringBuilder urlToCall = getBaseUrl(urlToCallStr, urlParameters);
		String jsonResponse = sendGetRequest(urlToCall);
		HLBrandProductAdResponse response = parseResponse(jsonResponse, HLBrandProductAdResponse.class);
		return response;
		
	}

	
	public void submittedOrderdDetailsToHL(HLOrderFeedDataModel hLOrderFeedDataModel) throws BrandProductAdServiceException {
		
		
		LOGGER.info("submitted Orderd details to HL: while making a call to Hook logic API Starting..");
		StringBuilder urlToCallStr=new StringBuilder(hlAdConfigProvider.getHLBrandProductAdvertiseConfirmationURL());
		TreeMap<String,String> urlParameters = new TreeMap<String, String>();
		urlParameters.put(HOOKLOGIC_APIKEY, hlAdConfigProvider.getBrandProductAdProviderAPIKey());
		urlParameters.put(HOOKLOGIC_PUSERID, hLOrderFeedDataModel.getpUserId());
		urlParameters.put(HOOKLOGIC_PLATFORM, hlAdConfigProvider.getBrandProductAdProviderPlatform());
		urlParameters.put(HOOKLOGIC_HLPT, hlAdConfigProvider.getBrandProductAdProviderConformationHlpt());
		urlParameters.put(HOOKLOGIC_PRICE, hLOrderFeedDataModel.getPrice() );
		urlParameters.put(HOOKLOGIC_TOTAL, hLOrderFeedDataModel.getOrderTotal());
		urlParameters.put(HOOKLOGIC_PRODUCTID, hLOrderFeedDataModel.getSku());
		urlParameters.put(HOOKLOGIC_ORDERID, hLOrderFeedDataModel.getOrderId());
		urlParameters.put(HOOKLOGIC_QUANTITY, hLOrderFeedDataModel.getQuantity());
		urlParameters.put(HOOKLOGIC_CUSERID, hLOrderFeedDataModel.getcUserId());
		
		StringBuilder urlToCall = getBaseUrlOfOrderFeed(urlToCallStr, urlParameters);
		String jsonResponse = sendGetRequest(urlToCall);
		HLBrandProductAdResponse pageBeanconResponse = parseResponse(jsonResponse, HLBrandProductAdResponse.class);
		StringBuilder pageBeancon = new StringBuilder();
		if(null!=pageBeanconResponse && pageBeanconResponse.getPageBeacon()!=null)
			sendGetRequest(pageBeancon.append("http:"+pageBeanconResponse.getPageBeacon()));
		
		LOGGER.info("Succesfully submitted Orderd details to HL");	
	}
	
	private String sendGetRequest(StringBuilder urlToCall)  throws BrandProductAdServiceException  {
		String returnString=null;
		HttpURLConnection connection = null;		
		try {
			connection = (HttpURLConnection) new URL(urlToCall.toString()).openConnection();
			connection.setRequestProperty("Accept-Charset",charSet);
			connection.setConnectTimeout(hlAdConfigProvider.getConnectionTimeoutPeriod()*1000);
			connection.setReadTimeout(hlAdConfigProvider.getReadTimeoutPeriod()*1000);
			returnString = sendRequest(connection);
		} catch (MalformedURLException mal) {
			LOGGER.error("Exception while making a FD Order confirmation call to Hook Logic API : "+mal);
			throw new BrandProductAdServiceException(mal);
		} catch (UnsupportedEncodingException unsup) {
			LOGGER.error("Exception while making a FD Order confirmation call to Hook Logic API : "+unsup);
			throw new BrandProductAdServiceException(unsup);
		} catch (IOException io) {
			LOGGER.error("Exception while making a FD Order confirmation call to Hook Logic API : "+io);
			throw new BrandProductAdServiceException(io);
		} finally {
			try {
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				LOGGER.warn("Exception while disconnecting the httpconnection: ", e);
			}
		}
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
	
	private StringBuilder getBaseUrl(StringBuilder urlToCallStr, TreeMap<String,String> urlParameters) throws BrandProductAdServiceException {
		
                
		  StringBuilder urlToCall = new StringBuilder();
	        
        try {
            if ( urlToCallStr != null ) {
                boolean first = true;
                for ( String key : urlParameters.keySet() ) {
                	if(null !=urlParameters.get(key)){
	                    if ( first) {
	                        first = false;
	                    } else {
	                        urlToCall.append("&");
	                    }
	                    urlToCall.append(key).append("=").append(URLEncoder.encode(urlParameters.get(key),charSet));
                	}
                }
                urlToCallStr.append(urlToCall);
            }
        }catch(UnsupportedEncodingException unsup){
        	throw new BrandProductAdServiceException(unsup);
        }
        return urlToCallStr;
        
	}
	
	
	private StringBuilder getBaseUrlOfOrderFeed(StringBuilder urlToCallStr, TreeMap<String,String> urlParameters) throws BrandProductAdServiceException {
		
        
		  StringBuilder urlToCall = new StringBuilder();
	        
    try {
        if ( urlToCallStr != null ) {
            boolean first = true;
            for ( String key : urlParameters.keySet() ) {
                if ( first) {
                    first = false;
                } else {
                    urlToCall.append("&");
                }
                urlToCall.append(key).append("=").append(urlParameters.get(key));
            }
            urlToCallStr.append(urlToCall);
        }
    }catch(Exception unsup){
    	throw new BrandProductAdServiceException(unsup);
    }
    return urlToCallStr;
    
	}

	private static  HLBrandProductAdResponse parseResponse(String responseStr, Class _class){
		
		HLBrandProductAdResponse response=null;
		try {
			if(null !=responseStr){
				Object obj =getMapper().readValue(responseStr, HLBrandProductAdResponse.class);
				if(null != obj && obj instanceof HLBrandProductAdResponse){
					response=(HLBrandProductAdResponse)obj;
				}
			}
		} catch (JsonMappingException e) {
			LOGGER.info("Exception while parsing BrandProduct response from hooklogic: "+e);
		} catch(JsonParseException e){
			LOGGER.info("Exception while parsing BrandProduct response from hooklogic: "+e);
		} catch (Exception e) {
			LOGGER.info("Exception while parsing BrandProduct response from hooklogic: "+e);
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



}
