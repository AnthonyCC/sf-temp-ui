package com.freshdirect.mail.service.silverpop;

import java.io.IOException;
import java.io.InputStream;

import java.util.Scanner;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author dHeller
 */
public class OAuthAuthentication {

	private static final Logger LOGGER = Logger.getLogger(OAuthAuthentication.class);

	public static final String PARAM_CLIENT_ID = "client_id";
	public static final String PARAM_CLIENT_SECRET = "client_secret";
	public static final String PARAM_REFRESH_TOKEN = "refresh_token";
	public static final String PARAM_GRANT_TYPE = "grant_type";
	public static final String GRANT_TYPE = "refresh_token";
	public static final String RESPONSE_TOKEN_KEY = "access_token";
	private String url;
	private HttpClient httpClient;
	private String responseText;

	public OAuthAuthentication() {

		this(FDStoreProperties.getIBMAccessTokenURL());
	}

	public OAuthAuthentication(String url) {
		this(url, new HttpClient());
	}

	OAuthAuthentication(String url, HttpClient httpClient) {
		this.url = url;
		this.httpClient = httpClient;
	}

	/**
	 * @param clientId
	 *            The client id as supplied from ibm silverpop
	 * @param clientSecret
	 *            The clientSecret id as supplied from ibm silverpop
	 * @param refereshToken
	 *            The refereshToken id as supplied from ibm silverpop
	 * 
	 * @return the value of the token to be used; blank if something goes amiss.
	 * @throws OAuthenticationException
	 */
	public String retrieveToken(String clientId, String clientSecret, String refereshToken)
			throws OAuthenticationException {
		PostMethod post = createPost(clientId, clientSecret, refereshToken);
		String token = "";
		try {
			int responseCode = httpClient.executeMethod(post);
			if (responseCode == 200) {
				responseText = getResponseJson(post);
				// System.out.println(" ********response token "+ responseText);
				token = getJsonElementFromJsonString(responseText, RESPONSE_TOKEN_KEY);

			} else {
				responseText = getResponseJson(post);
				// System.out.println ("error response: "+ responseText );
				throw new OAuthenticationException("ibm silverpop authentication exception; recieved: " + responseCode);
			}

			// return getTokenFromResponse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				post.releaseConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return token;
	}

	private PostMethod createPost(String clientId, String clientSecret, String refereshToken) {
		PostMethod post = new PostMethod(url);
		post.setParameter(PARAM_GRANT_TYPE, GRANT_TYPE);
		post.setParameter(PARAM_CLIENT_ID, clientId);
		post.setParameter(PARAM_CLIENT_SECRET, clientSecret);
		post.setParameter(PARAM_REFRESH_TOKEN, refereshToken);
		return post;
	}

	private String getResponseJson(PostMethod post) throws IOException {
		InputStream is = post.getResponseBodyAsStream();
		Scanner scanner = new Scanner(is).useDelimiter("/,");
		return scanner != null ? scanner.next() : "";
	}

	/**
	 * 
	 * @param jsonLine
	 *            the json string in quesion.
	 * @param searchElement
	 *            the name of the element or key that you which to have the
	 *            value returned.
	 * @return the value of the key or element name, blank if not found.
	 */
	public String getJsonElementFromJsonString(String jsonLine, String searchElement) {
		JsonElement jelement = new JsonParser().parse(jsonLine);
		JsonObject jobject = jelement.getAsJsonObject();

		jelement = jobject.get(searchElement);
		String token = jelement != null ? jelement.getAsString() : "";

		return token;
	}

	public String getIBMCampaignAccessToken() throws OAuthenticationException {

		// System.out.println(this.getClass().getName() +"
		// getIBMCampaignAccessToken()" + "accessUrl: "+ url);
		String clientId = FDStoreProperties.getIBMCampaignClientID();
		String clientSecret = FDStoreProperties.getIBMCampaignClientSecret();
		String refreshToken = FDStoreProperties.getIBMCampaignRefreshToken();
		// String request = "PUSH-"+URLEncoder.encode(details.getQualifier(),
		// "UTF-8") + "/" + URLEncoder.encode(details.getDestination(),
		// "UTF-8");
		// OAuthAuthentication service = new OAuthAuthentication(accessUrl);
		String accessToken = this.retrieveToken(clientId, clientSecret, refreshToken);

		return accessToken;
	}
}