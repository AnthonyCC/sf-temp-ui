package com.freshdirect.webapp.taglib.fdstore;

//Java Helper Class for Janrain Engage
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import org.xml.sax.SAXException;
import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SocialProviderJanRain implements SocialProvider {
  
  private static String baseUrl="https://fdsite.rpxnow.com/";
  private static String apiKey="bc08e9273d6bb2f7e0934116f0acdbb36a6918ae";
  private static String methodName="auth_info";
  private static String format="json";
  private static String resourceURI="";
  Map<String, String> query = null;
  String connectionToken = "";
  
  private final static Category LOGGER = LoggerFactory.getInstance(SocialProviderJanRain.class);
  
  public SocialProviderJanRain() {
      
	  while(baseUrl.endsWith("/"))
         baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
	  
	  resourceURI = baseUrl + "/api/v2/" + methodName;
	  query = new HashMap<String, String>();
	  query.put("format", format);
	  query.put("apiKey", apiKey);
	  query.put("token", connectionToken);
      
  }
 
  
  public HashMap<String,String> getSocialUserProfile(String connectionToken)
	{
		
	  if(connectionToken == null || connectionToken.length() ==0)
			throw new IllegalArgumentException("Invalid Connection Token");

	  if(query == null)
      {
    	  query = new HashMap<String, String>();
    	  query.put("format", format);
    	  query.put("apiKey", apiKey);
    	  query.put("token",connectionToken);
      }
	  
	  resourceURI = baseUrl + "/api/v2/" + methodName;
	  
	  String resultJson = apiCall(resourceURI);
		
		if(resultJson != null  && resultJson.length() > 0)
			
			return getSocialUserProperties(resultJson);
			
		return null;
	}
  
  
  
  
  private String apiCall(String resourceURI) {
      
	  if(resourceURI == null || resourceURI.length()==0)
			throw new IllegalArgumentException("Invalid Resource URI");
	  
	  StringBuffer sb = new StringBuffer();
      for (Iterator it = query.entrySet().iterator(); it.hasNext();) {
          if (sb.length() > 0) sb.append('&');
          try {
              Map.Entry e = (Map.Entry)it.next();
              sb.append(URLEncoder.encode(e.getKey().toString(), "UTF-8"));
              sb.append('=');
              sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
          } catch (UnsupportedEncodingException e) {
              throw new RuntimeException("Unexpected encoding error", e);
          }
      }
      String data = sb.toString();
      
      BufferedReader rd = null;
      try {
      	  URL url = new URL(resourceURI);
          HttpURLConnection conn = (HttpURLConnection)url.openConnection();
          conn.setRequestMethod("POST");
          conn.setDoOutput(true);
          conn.connect();
          OutputStreamWriter osw = new OutputStreamWriter(
              conn.getOutputStream(), "UTF-8");
          osw.write(data);
          osw.close();
         
          StringBuilder result = new StringBuilder();
		  String line = null;
		 
		  // Read result
		  rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  while ((line = rd.readLine()) != null) {
		    result.append(line);
		  }
		  
          return result.toString();
          
      } catch (MalformedURLException e) {
          throw new RuntimeException("Unexpected URL error", e);
          
      } catch (IOException e) {
          throw new RuntimeException("Unexpected IO error", e);
    	  
      } finally  {
          if (rd != null) {
              try {
	          rd.close();
              } catch (IOException ioe) {
                  // intentionally do nothing here
              }
          }
      }
	
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
		JsonNode rootNode,statNode,profileNode,dataNode,userNode,identityNode,emailNode,emailVerifiedNode,accountsNode;
		String userToken="",identityToken="",provider="",displayName="",preferredUsername="",domain="",userid="",username="",email="",emailVerified="N";
		
		try {
			rootNode = objectMapper.readTree(jsonData);
			
			statNode = rootNode.isNull() ? null : (JsonNode) rootNode.path("stat");
			
			profileNode = rootNode.isNull() ? null : (JsonNode) rootNode.get("profile");
			
			if(profileNode != null)
			{
				identityToken = profileNode.path("identifier").isNull() ? "" : (String)profileNode.path("identifier").asText();
				provider = profileNode.path("providerName").isNull() ? "" :  (String)profileNode.path("providerName").asText();
				displayName = profileNode.path("displayName").isNull() ? "" : (String)profileNode.path("displayName").asText();
				preferredUsername = profileNode.path("preferredUsername").isNull() ? "" : (String)profileNode.path("preferredUsername").asText();
				email = profileNode.path("email").isNull() ? "" : profileNode.path("email").asText();
				emailVerified = profileNode.path("verifiedEmail").isNull() ? "N" : (profileNode.path("verifiedEmail").asText().length() > 0 ? "Y":"N");
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

  
  	  public static void main(String [] args) {
	  	
  		String connectionToken = "a7684319f136d3bf639e43f6f460df5863ed4f86";
	  	SocialProviderJanRain rpx = new SocialProviderJanRain();
	
	  	HashMap<String,String> hm = rpx.getSocialUserProfile(connectionToken);
	  	
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


	@Override
	public HashMap<String, String> getSocialUserProfileByUserToken(
			String userToken, String providerName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HashMap<String, String> getSocialUserProfileByAccessToken(
			String accessToken, String providerName) {
		// TODO Auto-generated method stub
		return null;
	}
}
