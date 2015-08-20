package com.freshdirect.webapp.taglib.fdstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SocialProviderOneAll implements SocialProvider {
	
	private final static Category LOGGER = LoggerFactory.getInstance(SocialProviderOneAll.class);
	
	//Settings
	String site_subdomain = "fd-test";
	String site_public_key = "997ddd3a-9965-489c-96df-0a6585e66d6b";
	String site_private_key = "026599dc-10c3-4e69-bcf6-2db43c66dd37";
	

	// API Access Domain
	String site_domain = site_subdomain + ".api.oneall.com";

	// Connection Resource
	// http://docs.oneall.com/api/resources/connections/read-connection-details/
	//String resource_uri = "http://" + site_domain + "/connections/";
	
	String site_authentication ="";
	
	String encoded_site_authentication ="";
	
	public SocialProviderOneAll()
	{
				
		// Forge authentication string username:password
		site_authentication = site_public_key + ":" + site_private_key;
		
		encoded_site_authentication = new String(Base64.encodeBase64(site_authentication.getBytes())).replaceAll("[\n\r]", "");
	}
	
	public HashMap<String,String> getSocialUserProfile(String connectionToken)
	{
		if(connectionToken == null || connectionToken.length() ==0)
			throw new IllegalArgumentException("Invalid Connection Token");

		String resource_uri = "http://" + site_domain + "/connections/" + connectionToken + ".json"; 

		String resultJson = apiCall(resource_uri);
		
		if(resultJson != null  && resultJson.length() > 0)
			
			return getSocialUserProperties(resultJson);
			
		return null;
	}

	public HashMap<String,String> getSocialUserProfileByUserToken(String userToken,String providerName)
	{
		if(userToken == null || userToken.length() ==0)
			throw new IllegalArgumentException("Invalid User Token");
		
		if(providerName == null || providerName.length() ==0)
			throw new IllegalArgumentException("Invalid Provider Name");

		String resource_uri = "http://" + site_domain + "/users/" + userToken + ".json"; 

		String resultJson = apiCall(resource_uri);
		
		if(resultJson != null  && resultJson.length() > 0)
			
			return getSocialUserPropertiesForUserToekn(resultJson,providerName);
			
		return null;
	}

	
	
	
	private String apiCall(String resourceUri) {
			if(resourceUri == null || resourceUri.length()==0)
				throw new IllegalArgumentException("Invalid Resource URI");
		
		    // Result Container
			String result_json = "";
			
			try
			{
			  
	    	  // Setup connection
			  URL url = new URL (resourceUri);
			  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			   
			  // Connect using basic auth
			  connection.setRequestMethod("GET");   
			  connection.setRequestProperty("Authorization", "Basic " +  encoded_site_authentication);
			  connection.setDoOutput(true);
			  connection.setReadTimeout(10000);
			  connection.connect();
			  connection.getInputStream();
			   
			  StringBuilder sb = new StringBuilder();
			  String line = null;
			 
			  // Read result
			  BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			  while ((line = rd.readLine()) != null) {
			    sb.append(line);
			  }
			  result_json = sb.toString();
			  LOGGER.info("Profile :"+ result_json);
			  		 
			} 
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
			}

			return result_json;

	 }
	
	private HashMap<String,String> getSocialUserProperties(String resultJson)
	{
		
		if(resultJson == null || resultJson.length()==0)
				throw null;
		
		HashMap<String,String> socialUser = new HashMap<String,String>();
		
		//read json String to bytes
		byte[] jsonData = resultJson.getBytes();
		
		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		 
		//read JSON like DOM Parser
		JsonNode rootNode,responseNode,resultNode,dataNode,userNode,identityNode,emailsNode,accountsNode;
		String userToken="",identityToken="",provider="",displayName="",preferredUsername="",domain="",userid="",username="",email="",emailVerified="N";
		
		try {
			rootNode = objectMapper.readTree(jsonData);
			
			responseNode = rootNode.isNull() ? null : (JsonNode) rootNode.path("response");
			
			resultNode = responseNode.isNull() ? null : (JsonNode) responseNode.get("result");
			
			dataNode = resultNode.isNull() ? null : (JsonNode) resultNode.get("data");
			
			userNode = dataNode.isNull() ? null : (JsonNode) dataNode.get("user");
			
			
			if(userNode != null)
			{
				userToken = (String) userNode.path("user_token").asText();
				identityNode = (JsonNode) userNode.path("identity");
				
				if(identityNode != null)
				{
					identityToken = identityNode.path("identity_token").isNull() ? "" : (String)identityNode.path("identity_token").asText();
					provider = identityNode.path("provider").isNull() ? "" :  (String)identityNode.path("provider").asText();
					displayName = identityNode.path("displayName").isNull() ? "" : (String)identityNode.path("displayName").asText();
					preferredUsername = identityNode.path("preferredUsername").isNull() ? "" : (String)identityNode.path("preferredUsername").asText();
					accountsNode = identityNode.path("accounts");
					
					emailsNode = identityNode.path("emails");
					
					if(emailsNode != null)
					{
						Iterator<JsonNode> emailElements = emailsNode.elements();
						int j=0;
						while(emailElements.hasNext()){
							if(j > 0)
								break;
							JsonNode emailNode = emailElements.next();
							email = emailNode.path("value").asText();
							emailVerified = emailNode.path("is_verified").isNull()? "" : emailNode.path("is_verified").asText();
							emailVerified =  emailVerified.equalsIgnoreCase("true") ? "Y" : "N";
							j++;
						}
						
					}
					
					if(accountsNode != null)
					{
						Iterator<JsonNode> accountElements = accountsNode.elements();
						int i=0;
						while(accountElements.hasNext()){
						    if(i > 0)
						    	break;
							JsonNode accountNode = accountElements.next();
						    domain = accountNode.path("domain").asText();
						    userid = accountNode.path("userid").asText();
						    
						i++;
						}
					
					}
				}
				
			}
			
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		
		socialUser.put("userToken",userToken);
		socialUser.put("identityToken",identityToken);
		socialUser.put("provider",provider);
		socialUser.put("displayName",displayName);
		socialUser.put("preferredUsername",preferredUsername);
		socialUser.put("domain",domain);
		socialUser.put("userid",userid);
		socialUser.put("email",email);
		socialUser.put("emailVerified",emailVerified);
		
		return socialUser;
	}
	
	private HashMap<String,String> getSocialUserPropertiesForUserToekn(String resultJson,String providerName)
	{
		
		if(resultJson == null || resultJson.length()==0)
				throw null;
		
		HashMap<String,String> socialUser = new HashMap<String,String>();
		
		//read json String to bytes
		byte[] jsonData = resultJson.getBytes();
		
		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		 
		//read JSON like DOM Parser
		JsonNode rootNode,responseNode,resultNode,dataNode,userNode, identitiesNode, emailsNode,accountsNode;
		String userToken="",identityToken="",provider="",displayName="",preferredUsername="",domain="",userid="",username="",email="",emailVerified="N";
		
		try {
			rootNode = objectMapper.readTree(jsonData);
			
			responseNode = rootNode.isNull() ? null : (JsonNode) rootNode.path("response");
			
			resultNode = responseNode.isNull() ? null : (JsonNode) responseNode.get("result");
			
			dataNode = resultNode.isNull() ? null : (JsonNode) resultNode.get("data");
			
			userNode = dataNode.isNull() ? null : (JsonNode) dataNode.get("user");
			
			
			if(userNode != null)
			{
				userToken = (String) userNode.path("user_token").asText();
				identitiesNode = (JsonNode) userNode.path("identities");
				
				
				if(identitiesNode != null)
				{
					Iterator<JsonNode> identityElements = identitiesNode.elements();
					while(identityElements.hasNext()){
						JsonNode identityNode = identityElements.next();
						provider = identityNode.path("provider").isNull() ? "" :  (String)identityNode.path("provider").asText();
						
						if(provider.equalsIgnoreCase(providerName))
						{
							
							identityToken = identityNode.path("identity_token").isNull() ? "" : (String)identityNode.path("identity_token").asText();
							displayName = identityNode.path("displayName").isNull() ? "" : (String)identityNode.path("displayName").asText();
							emailsNode = identityNode.path("emails");
							
							if(emailsNode != null)
							{
								Iterator<JsonNode> emailElements = emailsNode.elements();
								int j=0;
								while(emailElements.hasNext()){
									if(j > 0)
										break;
									JsonNode emailNode = emailElements.next();
									email = emailNode.path("value").asText();
									emailVerified = emailNode.path("is_verified").isNull()? "" : emailNode.path("is_verified").asText();
									emailVerified =  emailVerified.equalsIgnoreCase("true") ? "Y" : "N";
									j++;
								}
								
							}
						

							socialUser.put("userToken",userToken);
							socialUser.put("identityToken",identityToken);
							socialUser.put("provider",provider);
							socialUser.put("displayName",displayName);
							socialUser.put("preferredUsername",preferredUsername);
							socialUser.put("domain",domain);
							socialUser.put("userid",userid);
							socialUser.put("email",email);
							socialUser.put("emailVerified",emailVerified);
					   
						
						
						
						}
						
						
						
						
					}
					
				}
				
			}
			
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		
		
		return socialUser;
	}
	
	public static void main(String args[])
	{
		//String connectionToken ="cf41a0fd-52a8-4f4b-9ca8-26526e09f4dd";
		String userToken = "2d066b2b-e5aa-4fec-82e5-3cfab7591fe7";
		SocialProviderOneAll rpxOneAll = new SocialProviderOneAll();
		
		HashMap<String,String> hm = rpxOneAll.getSocialUserProfileByUserToken(userToken,"google");
	   
		if(hm != null)
	    {
	    	for (Iterator it = hm.entrySet().iterator(); it.hasNext();) {
	           
	            try {
	                Map.Entry e = (Map.Entry)it.next();
	                System.out.println(e.getKey()+": "+e.getValue());
	            
	            } catch (Exception e) {
	                throw new RuntimeException("Unexpected encoding error", e);
	            }
	        }
	    }
	    
	}

}
