package com.freshdirect.webapp.taglib.fdstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Category;
import org.apache.oltu.oauth2.common.OAuth.HttpMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SocialProviderOneAll implements SocialProvider {
	
	private final static Category LOGGER = LoggerFactory.getInstance(SocialProviderOneAll.class);
	
	//Settings
    private String site_subdomain = FDStoreProperties.getSocialOneAllSubdomain();
    private String site_public_key = FDStoreProperties.getSocialOneAllPublicKey();
    private String site_private_key = FDStoreProperties.getSocialOneAllPrivateKey();
    private String site_post_url = FDStoreProperties.getSocialOneAllPostUrl();

	// API Access Domain
    private String site_domain = site_subdomain + site_post_url;

	// Connection Resource
	// http://docs.oneall.com/api/resources/connections/read-connection-details/
	//String resource_uri = "http://" + site_domain + "/connections/";
	
    private String site_authentication = "";
    private String encoded_site_authentication = "";
	
	public SocialProviderOneAll()
	{
		// Forge authentication string username:password
		site_authentication = site_public_key + ":" + site_private_key;
		
		encoded_site_authentication = new String(Base64.encodeBase64(site_authentication.getBytes())).replaceAll("[\n\r]", "");
	}
	
	@Override
    public HashMap<String,String> getSocialUserProfile(String connectionToken)
	{
		if(connectionToken == null || connectionToken.length() ==0)
			throw new IllegalArgumentException("Invalid Connection Token");

		String resource_uri = "http://" + site_domain + "/connections/" + connectionToken + ".json"; 

        String resultJson = apiCall(resource_uri, HttpMethod.GET);
		
		if(resultJson != null  && resultJson.length() > 0)
			
			return getSocialUserProperties(resultJson);
			
		return null;
	}

	@Override
    public HashMap<String,String> getSocialUserProfileByUserToken(String userToken,String providerName)
	{
		if(userToken == null || userToken.length() ==0)
			throw new IllegalArgumentException("Invalid User Token");
		
		if(providerName == null || providerName.length() ==0)
			throw new IllegalArgumentException("Invalid Provider Name");

		String resource_uri = "http://" + site_domain + "/users/" + userToken + ".json"; 

        String resultJson = apiCall(resource_uri, HttpMethod.GET);
		
		if(resultJson != null  && resultJson.length() > 0)
			
			return getSocialUserPropertiesForUserToekn(resultJson,providerName);
			
		return null;
	}

    private String apiCall(String resourceUri, String method) {
			if(resourceUri == null || resourceUri.length()==0)
				throw new IllegalArgumentException("Invalid Resource URI");
		
		    // Result Container
			String result_json = "";
			
			BufferedReader rd = null;
			try
			{
			  
	    	  // Setup connection
			  URL url = new URL (resourceUri);
			  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			   
			  // Connect using basic auth
            connection.setRequestMethod(method);
			  connection.setRequestProperty("Authorization", "Basic " +  encoded_site_authentication);
			  connection.setDoOutput(true);
			  connection.setReadTimeout(10000);
			  connection.connect();
			  connection.getInputStream();
			   
			  StringBuilder sb = new StringBuilder();
			  String line = null;
			 
			  // Read result
			  rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			  while ((line = rd.readLine()) != null) {
			    sb.append(line);
			  }
			  result_json = sb.toString();
			  LOGGER.info("Profile :"+ result_json);
			  		 
			} 
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
			} finally {
				if (rd != null) {
					try {
						rd.close();
					} catch (IOException ioe) {
						// intentionally do nothing here
					}
				}
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
		JsonNode rootNode,responseNode,resultNode,dataNode,userNode,identityNode,emailsNode,accountsNode, namesNode;
		String userToken="",identityToken="",provider="",displayName="",preferredUsername="",domain="",userid="",username="",email="",emailVerified="N", firstName="", lastName="";
		
		try {
			rootNode = objectMapper.readTree(jsonData);
			
			responseNode = rootNode.isNull() ? null : (JsonNode) rootNode.path("response");
			
			resultNode = responseNode.isNull() ? null : (JsonNode) responseNode.get("result");
			
			dataNode = resultNode.isNull() ? null : (JsonNode) resultNode.get("data");
			
			userNode = dataNode.isNull() ? null : (JsonNode) dataNode.get("user");
			
			
			if(userNode != null)
			{
				userToken = userNode.path("user_token").asText();
				identityNode = userNode.path("identity");
				
				if(identityNode != null)
				{
					identityToken = identityNode.path("identity_token").isNull() ? "" : (String)identityNode.path("identity_token").asText();
					provider = identityNode.path("provider").isNull() ? "" :  (String)identityNode.path("provider").asText();
					displayName = identityNode.path("displayName").isNull() ? "" : (String)identityNode.path("displayName").asText();
					preferredUsername = identityNode.path("preferredUsername").isNull() ? "" : (String)identityNode.path("preferredUsername").asText();
					accountsNode = identityNode.path("accounts");
					namesNode = identityNode.path("name");
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
					
					if(namesNode != null)
					{
						firstName = namesNode.path("givenName").asText();
						lastName = namesNode.path("familyName").asText();
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
		socialUser.put("firstName",firstName);
		socialUser.put("lastName",lastName);
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
				userToken = userNode.path("user_token").asText();
				identitiesNode = userNode.path("identities");
				
				
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
	
	@Override
    public HashMap<String, String> getSocialUserProfileByAccessToken(
			String accessToken, String providerName) {

		if(accessToken == null || accessToken.length() ==0)
			throw new IllegalArgumentException("Invalid User Token");
		
		if(providerName == null || providerName.length() ==0)
			throw new IllegalArgumentException("Invalid Provider Name");

		String resource_uri = "http://" + site_domain + "/users.json"; 

		String resultJson = apiCallUsingAccessToken(resource_uri, accessToken, providerName);
		
		if(resultJson != null  && resultJson.length() > 0)
			
			return getSocialUserPropertiesForAccessToken(resultJson,providerName);
			
		return null;
	
	}

    @Override
    public boolean deleteSocialIdentity(String identityToken) {
        if (identityToken == null || identityToken.length() == 0) {
            throw new IllegalArgumentException("Invalid Identity Token");
        }

        String resource_uri = "http://" + site_domain + "/identities/" + identityToken + ".json?confirm_deletion=true";

        String resultJson = apiCall(resource_uri, HttpMethod.DELETE);

        return isCallSuccess(resultJson);
    }

    private boolean isCallSuccess(String resultJson) {
        boolean success = false;
        if (resultJson != null && resultJson.length() != 0) {
            byte[] jsonData = resultJson.getBytes();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode, responseNode, requestNode, statusNode;
            try {
                rootNode = objectMapper.readTree(jsonData);
                responseNode = rootNode.isNull() ? null : (JsonNode) rootNode.path("response");
                requestNode = responseNode.isNull() ? null : (JsonNode) responseNode.path("request");
                statusNode = requestNode.isNull() ? null : (JsonNode) requestNode.get("status");
                success = "success".equals(statusNode.path("flag").asText());
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage());
                success = false;
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
                success = false;
            }
        }
        return success;
    }

	private HashMap<String, String> getSocialUserPropertiesForAccessToken(
			String resultJson, String providerName) {
		if (resultJson.toString() == null
				|| resultJson.toString().length() == 0)
			throw null;

		HashMap<String, String> socialUser = new HashMap<String, String>();

		// read json String to bytes
		byte[] jsonData = resultJson.toString().getBytes();

		// create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();

		// read JSON like DOM Parser
		JsonNode rootNode, responseNode, resultNode, dataNode, userNode, emailsNode, identityNode;
		String userToken = "", identityToken = "", provider = "", displayName = "", preferredUsername = "", domain = "", userid = "", email = "", emailVerified = "N";
		try {
			rootNode = objectMapper.readTree(jsonData);

			responseNode = rootNode.isNull() ? null : (JsonNode) rootNode
					.path("response");

			resultNode = responseNode.isNull() ? null : (JsonNode) responseNode
					.get("result");

			dataNode = resultNode.isNull() ? null : (JsonNode) resultNode
					.get("data");

			userNode = dataNode.isNull() ? null : (JsonNode) dataNode
					.get("user");

			if (userNode != null) {
				userToken = userNode.path("user_token").asText();
				identityNode = userNode.path("identity");

				if (identityNode != null) {
					provider = identityNode.path("provider").asText();
					if (provider.equalsIgnoreCase(providerName)) {
						displayName = identityNode.path("displayName").asText();
						provider = identityNode.path("provider").asText();
						identityToken = identityNode.path("identity_token")
								.isNull() ? "" : (String) identityNode.path(
								"identity_token").asText();
						emailsNode = identityNode.path("emails");
						if (emailsNode != null) {
							Iterator<JsonNode> emailElements = emailsNode
									.elements();
							int j = 0;
							while (emailElements.hasNext()) {
								if (j > 0)
									break;
								JsonNode emailNode = emailElements.next();
								email = emailNode.path("value").asText();
								emailVerified = emailNode.path("is_verified")
										.isNull() ? "" : emailNode.path(
										"is_verified").asText();
								emailVerified = emailVerified
										.equalsIgnoreCase("true") ? "Y" : "N";
								j++;
							}

						}

						socialUser.put("userToken", userToken);
						socialUser.put("identityToken", identityToken);
						socialUser.put("provider", provider);
						socialUser.put("displayName", displayName);
						socialUser.put("preferredUsername", preferredUsername);
						socialUser.put("domain", domain);
						socialUser.put("userid", userid);
						socialUser.put("email", email);
						socialUser.put("emailVerified", emailVerified);
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

	private String apiCallUsingAccessToken(String resource_uri,
			String accessToken, String providerName) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		String result_json = "";

		BufferedReader rd = null;
		try {
			URL url = new URL(resource_uri);
			String payload = "{\"request\":{\"user\":{\"action\":\"import_from_access_token\",\"identity\":{\"source\":{\"key\":\""
					+ providerName
					+ "\",\"access_token\":{\"key\":\""
					+ accessToken + "\"}}}}}}";
			byte[] data = payload.getBytes("UTF-8");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty(
					"Authorization",
					"Basic "
							+ new String(Base64
									.encodeBase64(site_authentication
											.getBytes())).replaceAll("[\n\r]",
									""));
			connection.setRequestProperty("Content-Type",
					"application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestMethod("PUT");

			OutputStream osw = connection.getOutputStream();
			osw.write(data);
			osw.flush();
			osw.close();

			rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			result_json = sb.toString();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException ioe) {
					// intentionally do nothing here
				}
			}
		}
		return result_json;
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
