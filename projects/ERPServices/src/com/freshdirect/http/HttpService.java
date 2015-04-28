package com.freshdirect.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class HttpService {

	private static final HttpService INSTANCE = new HttpService();

	private HttpService() {
	}

	public static HttpService defaultService() {
		return INSTANCE;
	}

	public void postData(String uri, String data) throws IOException {
		if (uri != null && data != null) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(uri);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("data", data));
			post.setEntity(new UrlEncodedFormEntity(params));
			client.execute(post);
		}
	}
}
