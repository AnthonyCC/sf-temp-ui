package com.freshdirect.fdstore.silverpopup.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Scanner;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.metaparadigm.jsonrpc.JSONSerializer;

/**
 * @author Vamsi Krishna
 */
public class OAuthAuthenticationRestServiceImpl {

	private static final Logger LOGGER = Logger.getLogger(OAuthAuthenticationRestServiceImpl.class);

	public static final String PARAM_CLIENT_ID = "client_id";
	public static final String PARAM_CLIENT_SECRET = "client_secret";
	public static final String PARAM_REFRESH_TOKEN = "refresh_token";
	public static final String PARAM_GRANT_TYPE = "grant_type";
	public static final String GRANT_TYPE = "refresh_token";
	private String url;
	private HttpClient httpClient;
	private String responseText;

	public OAuthAuthenticationRestServiceImpl(String url) {
		this(url, new HttpClient());
	}

	OAuthAuthenticationRestServiceImpl(String url, HttpClient httpClient) {
		this.url = url;
		this.httpClient = httpClient;
	}

	public String retrieveToken(String clientId, String clientSecret, String refereshToken) {
		PostMethod post = createPost(clientId, clientSecret, refereshToken);
		try {
			httpClient.executeMethod(post);
			responseText = getResponseText(post);
			return getTokenFromResponse();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private PostMethod createPost(String clientId, String clientSecret, String refereshToken) {
		PostMethod post = new PostMethod(url);
		post.setParameter(PARAM_GRANT_TYPE, GRANT_TYPE);
		post.setParameter(PARAM_CLIENT_ID, clientId);
		post.setParameter(PARAM_CLIENT_SECRET, clientSecret);
		post.setParameter(PARAM_REFRESH_TOKEN, refereshToken);
		return post;
	}

	private String getResponseText(PostMethod post) throws IOException {
		InputStream is = post.getResponseBodyAsStream();
		Scanner scanner = new Scanner(is).useDelimiter("/,");
		return scanner != null ? scanner.next():"";
	}

	private String getTokenFromResponse() throws IOException {
		JSONObject json;
		String accessToken = "";
		try {
			json = new JSONObject (responseText);
			accessToken = json.getString("access_token");
			LOGGER.info("Access Token response for IBM POST call is : "+accessToken);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return accessToken;
	}
}