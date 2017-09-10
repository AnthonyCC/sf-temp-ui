package com.freshdirect.fdstore.silverpopup.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.SilverPopupDetails;

/*
 * @Author Vamsi Krishna
 */
public class FDIBMPushNotification {
	
	private static final Logger LOGGER = Logger.getLogger(FDIBMPushNotification.class);
	
	public boolean execute(SilverPopupDetails details) {
		final String accessUrl = FDStoreProperties.getIBMAccessTokenURL();
		final String url = FDStoreProperties.getIBMPushNotificationURL();
		try {
			String clientId = FDStoreProperties.getIBMClientID(); 				
			String clientSecret = FDStoreProperties.getIBMClientSecret(); 		
			String refreshToken = FDStoreProperties.getIBMRefreshToken();	
			String request = "PUSH-"+URLEncoder.encode(details.getQualifier(), "UTF-8") + "/" + URLEncoder.encode(details.getDestination(), "UTF-8");
			OAuthAuthenticationRestServiceImpl service = new OAuthAuthenticationRestServiceImpl(accessUrl);
			String accessToken = service.retrieveToken(clientId, clientSecret, refreshToken);
			LOGGER.info("Post method for retriving AccessToken fro IBM SilverPopup Push Notification has been Requested");
			
			
			final PutMethod putMethod = new PutMethod(url+request);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			putMethod.addRequestHeader("Authorization","Bearer "+accessToken);
			putMethod.addRequestHeader("Content-Type", "application/json");
			mapper.setSerializationInclusion(Include.NON_NULL);  //Tells the serializer to only include those parameters that are not null
			final HttpClient client = new HttpClient();
			client.setTimeout(6000);
			try{
				if(request != null){
					LOGGER.info("Full Query Request to IBM is "+url+request);
					Identity ident = new Identity();
					ident.setName("customer_ID");
					ident.setValue(details.getCustomerId());
					IBMRequest ibmrequest = new IBMRequest();
					ibmrequest.setIdentity(ident);
					String body = mapper.writeValueAsString(ibmrequest);
					LOGGER.info("body for request to IBM : "+body);
					putMethod.setRequestBody(body);			
					client.executeMethod(putMethod);					
					LOGGER.info("PUT Method Statuc Code for IBM silverPopup Push notification is " +putMethod.getStatusCode());					
					if(putMethod.getStatusCode()==200){
						return true;
					}
				}
			} catch (JsonProcessingException e1) {
				LOGGER.error(e1);
			} catch(IOException e){
				LOGGER.error(e);
			}
			finally{
				putMethod.releaseConnection();
			}			

		} catch (Exception e) {
			LOGGER.error(e);
		}
		return false;

	}
	

	
}