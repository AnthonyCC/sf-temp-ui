package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Category;

public class CheckSocialLoginTag extends AbstractGetterTag implements SessionName  {

	private static final long serialVersionUID = 7205669659879185264L;
	
	private final static Category LOGGER = LoggerFactory.getInstance(CheckSocialLoginTag.class);

	protected Object getResult() throws Exception {
		
		HttpSession session = pageContext.getSession();
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		
		String connectionToken  = (String) request.getParameter("connection_token");
		
		// Your Site Settings
		String site_subdomain = "fd-test";
		String site_public_key ="997ddd3a-9965-489c-96df-0a6585e66d6b";
		String site_private_key ="026599dc-10c3-4e69-bcf6-2db43c66dd37";
		 
		// API Access Domain
		String site_domain = site_subdomain + ".api.oneall.com";
		 
		//Connection Resource
	    //http://docs.oneall.com/api/resources/connections/read-connection-details/
		String  resource_uri = "https://"+ site_domain+ "/connections/"+connectionToken+ ".json";
		
		// Result Container
		String result_json = "";
		
		try
		{
		  // Forge authentication string username:password
		  String site_authentication = site_public_key + ":" + site_private_key;
		  String encoded_site_authentication = new String(Base64.encodeBase64(site_authentication.getBytes())).replaceAll("[\n\r]", "");
		        
		  // Setup connection
		  URL url = new URL (resource_uri);
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
		  
		  		 
		} 
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}

		
		if(result_json.length() > 0)
			return getUserProperties(result_json);
		
		
		return null;
	}
	
	
	
	
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.HashMap";
		}

	}
	
	private HashMap<String,String> getUserProperties(String resultJson)
	{
		HashMap<String,String> userProperties = new HashMap<String,String>();
		
		//read json String to bytes
		byte[] jsonData = resultJson.getBytes();
		 
		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		 
		//read JSON like DOM Parser
		JsonNode rootNode;
		String userToken="",identityToken="",provider="",displayName="",preferredUsername="",domain="",userid="",username="",email="",emailVerified="";
		try {
			rootNode = objectMapper.readTree(jsonData);
			
			JsonNode responseNode = (JsonNode) rootNode.path("response");
			
			JsonNode resultNode = (JsonNode) responseNode.get("result");
			
			JsonNode dataNode = (JsonNode) resultNode.get("data");
			
			JsonNode userNode = (JsonNode) dataNode.get("user");
			
			userToken = (String) userNode.path("user_token").asText();
			
			JsonNode identityNode = (JsonNode) userNode.path("identity");
			
			identityToken = (String)identityNode.path("identity_token").asText();
			provider = (String)identityNode.path("provider").asText();
			displayName = (String)identityNode.path("displayName").asText();
			preferredUsername = (String)identityNode.path("preferredUsername").asText();
			
			
			JsonNode emailsNode = identityNode.path("emails");
			Iterator<JsonNode> emailElements = emailsNode.elements();
			int j=0;
			while(emailElements.hasNext()){
				if(j > 0)
					break;
				JsonNode emailNode = emailElements.next();
			    email = emailNode.path("value").asText();
			    emailVerified = emailNode.path("is_verified").asText();
			    
			    if(emailVerified.equals("true"))
			    	emailVerified="Y";
			    else
			    	emailVerified="N";
			    
			    
			    j++;
			}
			
			
			
			JsonNode accountsNode = identityNode.path("accounts");
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
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		userProperties.put("userToken",userToken);
		userProperties.put("identityToken",identityToken);
		userProperties.put("provider",provider);
		userProperties.put("displayName",displayName);
		userProperties.put("preferredUsername",preferredUsername);
		userProperties.put("domain",domain);
		userProperties.put("userid",userid);
		userProperties.put("email",email);
		userProperties.put("emailVerified",emailVerified);
		
		this.pageContext.getSession().setAttribute("userProp",userProperties);
		
		return userProperties;
	}


}
