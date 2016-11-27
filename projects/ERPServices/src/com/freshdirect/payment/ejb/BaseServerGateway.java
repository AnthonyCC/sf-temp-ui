package com.freshdirect.payment.ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.GiveXRequest;
import com.freshdirect.payment.GivexException;
import com.freshdirect.payment.GivexResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseServerGateway {
	
	private static final Category LOGGER = LoggerFactory.getInstance( BaseServerGateway.class );
	
	protected String buildRequest(Object object){
		Gson gson = getGson();
		return gson.toJson(object);
	}
	
	protected Gson getGson(){
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mmZ");
		Gson gson = builder.create();
		return gson;
	}
	
	protected GivexResponse call(String api, GiveXRequest request) throws GivexException, IOException{
		
	HttpURLConnection conn = null;
	
	try{
		URL url = new URL(FDStoreProperties.getGiveXGatewayEndPoint() + api);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		//conn.setRequestProperty("Connection","Keep-Alive");
		//System.getProperties().put("http.keepAlive", true);
		//System.out.println(conn.getResponseCode());
		//conn.setUseCaches(false);
		//conn.setAllowUserInteraction(true);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		
		Gson gson = getGson();
		String input = gson.toJson(request);
		
		OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        osw.write(input);
        osw.flush();
        osw.close();
      
		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new GivexException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		StringBuilder sb = new StringBuilder();
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}

		GivexResponse response = gson.fromJson(sb.toString(), GivexResponse.class);
		
		return response;
		
	}finally{
		if(conn!=null)conn.disconnect();
	}
		

	}
	
}
