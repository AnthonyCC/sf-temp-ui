package com.freshdirect.sms.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.TreeMap;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sms.model.st.STSmsResponse;

public class STSmsProvider implements SmsNotificationService {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(STSmsProvider.class);
	
	public static final String charSet = "UTF-8";
	public static final String contentType = "application/json";
	public static final String httpConnUnauthorized = "401";
		
	@SuppressWarnings("deprecation")
	private static DefaultHttpClient createHttpClient()	throws SocketTimeoutException {

		org.apache.http.params.HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(params, SmsConfigProvider.getSTConnectionTimeoutPeriod() * 1000);
		HttpConnectionParams.setSoTimeout(params, SmsConfigProvider.getSTReadTimeoutPeriod() * 1000);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		ThreadSafeClientConnManager multiThreadedConnectionManager = new ThreadSafeClientConnManager(params, registry);

		DefaultHttpClient httpclient = new DefaultHttpClient(multiThreadedConnectionManager, params);

		return httpclient;
	}
	
	private HttpResponse sendPOSTRequest(StringBuilder urlToCall, boolean includeAcceptHeader, Header authHeader, StringEntity postRequestData)  throws SmsServiceException  {
				
		DefaultHttpClient httpClient = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		try {
			httpClient = createHttpClient();
			LOGGER.info("URL: "+ urlToCall.toString());
			httpPost = new HttpPost(urlToCall.toString());
			LOGGER.info("Requesting: "+ httpPost.getURI());
			if(includeAcceptHeader) {				
				httpPost.addHeader(authHeader);								
				httpPost.setEntity(postRequestData);
				httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");								
			}
			
			response = httpClient.execute(httpPost);
			
		} catch (MalformedURLException mal) {
			LOGGER.error("Exception while making a call to Single Touch: "+mal);
			throw new SmsServiceException(mal);
		} catch (UnsupportedEncodingException unsup) {
			LOGGER.error("Exception while making a call to Single Touch: "+unsup);
			throw new SmsServiceException(unsup);
		} catch (IOException io) {
			LOGGER.error("Exception while making a call to Single Touch: "+io);
			throw new SmsServiceException(io);
		}

		return response;
	}
	
	private StringBuilder getBaseUrl(TreeMap<String,String> urlParameters) throws SmsServiceException {
		        
		StringBuilder urlToCall = new StringBuilder();
		urlToCall.append(SmsConfigProvider.getSTUrl());

		if (urlParameters != null) {
			urlToCall.append("?");
			boolean first = true;
			for (String key : urlParameters.keySet()) {
				if (first) {
					first = false;
				} else {
					urlToCall.append("&");
				}
				urlToCall.append(key).append("=").append(urlParameters.get(key));
			}
		}
        
        return urlToCall;
	}

	@SuppressWarnings("unchecked")
	private static STSmsResponse parseResponse(String responseStr, Class _class){
		STSmsResponse response = null;
		try {
			if (null != responseStr) {
				Object obj = getMapper().readValue(responseStr, _class);
				if (null != obj && obj instanceof STSmsResponse) {
					response = (STSmsResponse) obj;
				}
			}
		} catch (JsonMappingException e) {
			LOGGER.info("Exception while parsing sms response from Single Touch: "+e);
		} catch(JsonParseException e){
			LOGGER.info("Exception while parsing sms response from Single Touch: "+e);
		} catch (IOException e) {
			LOGGER.info("Exception while parsing sms response from Single Touch: "+e);
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
		
	private static String retainDigits(String string) {
		StringBuffer clean = new StringBuffer();
		if (string == null)
			return "";
		for (int i = 0; i < string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				clean.append(string.charAt(i));
			}
		}
		return clean.toString();
	}
	
	private String getHttpResponseAsString(HttpResponse httpResponse)
			throws IOException, UnsupportedEncodingException {
		String returnString="";
		InputStream response = httpResponse.getEntity().getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(response));
		StringBuilder responseString = new StringBuilder();			
		for ( String oneLine; (oneLine = reader.readLine()) != null; ) {
			responseString.append(oneLine);
		}
		returnString = responseString.toString();
		return returnString;
	}

	@SuppressWarnings("deprecation")
	@Override
	public STSmsResponse sendSMSRequest(String mobileNumber, String message, String eStoreId)
			throws com.freshdirect.sms.service.SmsServiceException {
		
		STSmsResponse response = null;
		TreeMap<String, String> urlParameters = new TreeMap<String, String>();
		try {			
			// A org.apache.http.impl.auth.DigestScheme instance is what will process the challenge from the web-server
			final DigestScheme md5Auth = new DigestScheme();

			// This should return an HTTP 401 Unauthorized with a challenge to solve.
			final HttpResponse authResponse = sendPOSTRequest(getBaseUrl(urlParameters), false, null, null);

			if (authResponse.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
				if (authResponse.containsHeader("WWW-Authenticate")) {
					// Get the challenge.
					final Header challenge = authResponse.getHeaders("WWW-Authenticate")[0];
					md5Auth.processChallenge(challenge);
					// Generate a solution Authentication header using your user name and password.
					Header authHeader;
						if("FDX".equalsIgnoreCase(eStoreId)){
							authHeader = md5Auth.authenticate(new UsernamePasswordCredentials(SmsConfigProvider.getSTFdxUserName(), SmsConfigProvider.getSTFdxPassword())
							, new BasicHttpRequest(HttpPost.METHOD_NAME, new URL(getBaseUrl(urlParameters).toString()).getPath()));
						}
						else{
							authHeader = md5Auth.authenticate(new UsernamePasswordCredentials(SmsConfigProvider.getSTUserName(), SmsConfigProvider.getSTPassword())
							, new BasicHttpRequest(HttpPost.METHOD_NAME, new URL(getBaseUrl(urlParameters).toString()).getPath()));
						}
					
					StringEntity postRequestData = new StringEntity("{\"sms_to\":" + retainDigits(mobileNumber) + ",\"sms_msg\":\"" + message + "\"}");
					final HttpResponse finalResponse = sendPOSTRequest(getBaseUrl(urlParameters), true, authHeader, postRequestData);
									
					response = parseResponse(getHttpResponseAsString(finalResponse), STSmsResponse.class);

				} else {
					throw new SmsServiceException(
							"SMS service provider responded with Http 401, but didn't send us a usable WWW-Authenticate header.");
				}
			} else {
				throw new SmsServiceException(
						"SMS service provider response isn't an Http status 401 we were expecting.");
			}

		} catch (AuthenticationException e) {			
			LOGGER.error("AuthenticationException to Single Touch: "+e);
			throw new SmsServiceException(e);
		} catch (MalformedURLException e) {			
			LOGGER.error("MalformedURLException while making a call to Single Touch: "+e);
			throw new SmsServiceException(e);
		} catch (UnsupportedEncodingException e) {			
			LOGGER.error("UnsupportedEncodingException while making a call to Single Touch: "+e);
			throw new SmsServiceException(e);
		} catch (IOException e) {
			LOGGER.error("IOException while making a call to Single Touch: "+e);
			throw new SmsServiceException(e);
		} catch (MalformedChallengeException e) {			
			LOGGER.error("MalformedChallengeException while making a call to Single Touch: "+e);
			throw new SmsServiceException(e);
		}
		
		return response;
	}
	
	public static void main(String[] args) throws SmsServiceException {
		
		STSmsProvider provider = new STSmsProvider();
		provider.sendSMSRequest("3472634065", "HeyKris", "Freshdirect" );
		
	}
	
}
