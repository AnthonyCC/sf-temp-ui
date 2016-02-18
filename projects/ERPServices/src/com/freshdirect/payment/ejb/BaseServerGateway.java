package com.freshdirect.payment.ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.GiveXRequest;
import com.freshdirect.payment.GivexResponseModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseServerGateway {
	
	private static final Category LOGGER = LoggerFactory.getInstance( BaseServerGateway.class );
	
	public static HttpURLConnection getConnection(String api) throws IOException{
		
		URL url = new URL(FDStoreProperties.getGiveXGatewayEndPoint() + api);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(true);
		conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		return conn;
	}
	
	protected static String buildRequest(Object object){
		Gson gson = getGson();
		return gson.toJson(object);
	}
	
	protected static Gson getGson(){
		GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mmZ");
		Gson gson = builder.create();
		return gson;
	}
	
	protected static GivexResponseModel getResponse(String api, GiveXRequest request) throws IOException{
		
		HttpURLConnection conn = getConnection(api);
	
		Gson gson = getGson();
		String input = gson.toJson(request);
		
		OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        osw.write(input);
        osw.flush();
        osw.close();
        
        /*
		os.write(input.getBytes());
		os.flush();*/

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		StringBuilder sb = new StringBuilder();
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}

		GivexResponseModel response = gson.fromJson(sb.toString(), GivexResponseModel.class);
		LOGGER.info(response.toString());
		
		return response;

	}
	
}
