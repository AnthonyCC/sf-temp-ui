package com.freshdirect.payment.ejb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.payment.GiveXRequest;
import com.freshdirect.payment.GivexResponseModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseServerGateway {

	public static final String gateway_endpoint = FDStoreProperties.getGiveXGatewayEndPoint();
	
	
	public static HttpURLConnection getConnection(String api) throws IOException{
		URL url = new URL(/*FDStoreProperties.getGiveXGatewayEndPoint()*/"http://localhost:8080/logisticsapi/giftcard/" + api);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
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
	
	protected static GivexResponseModel getResponse(HttpURLConnection conn, GiveXRequest request) throws IOException{
		
		Gson gson = getGson();
		String input = gson.toJson(request);
		
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		StringBuilder sb = new StringBuilder();
		while ((output = br.readLine()) != null) {

			System.out.println(output);
			sb.append(output);
		}

		GivexResponseModel response = gson.fromJson(sb.toString(), GivexResponseModel.class);
		System.err.println(response.toString());
		
		return response;

	}
	
}
