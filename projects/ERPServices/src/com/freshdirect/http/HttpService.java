package com.freshdirect.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.framework.util.log.LoggerFactory;

public class HttpService {

	private static HttpService instance = new HttpService();

	private static final Logger LOG = LoggerFactory.getInstance(HttpService.class);

	private HttpService() {
	}

	public static HttpService defaultService() {
		return instance;
	}

	public static void setHttpService(HttpService httpService) {
		instance = httpService;
	}

	public void deleteData(String uri) throws IOException {
		if (uri != null) {
			HttpClient client = new DefaultHttpClient();
			HttpDelete delete = null;
			try {
				delete = new HttpDelete(uri);
				client.execute(delete);
			} finally {
				try {
					if (delete != null) {
						delete.releaseConnection();
					}
				} catch (Exception e) {
					
				}
			}
		}
	}

	public void postData(String uri, String data) throws IOException {
		if (uri != null && data != null) {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = null;
			try {
				post = new HttpPost(uri);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("data", data));
				post.setEntity(new UrlEncodedFormEntity(params));
				client.execute(post);
                        } finally {
                                try {
									if (post != null) {
									        post.releaseConnection();
									}
								} catch (Exception e) {
								}
                        }
		}
	}

	public void postDataWithContentTypeJson(String uri, String data) throws IOException {
		if (uri != null) {
			HttpClient client = new DefaultHttpClient();
                        HttpPost post = null;
                        try {
                                post = new HttpPost(uri);
				StringEntity content = new StringEntity(data);
				content.setContentType("application/json");
				post.setEntity(content);
				HttpResponse response = client.execute(post);
				LOG.debug(response.getStatusLine());
                        } finally {
                                try {
									if (post != null) {
									        post.releaseConnection();
									}
								} catch (Exception e) {
									
								}
                        }
		}
	}

	public <T> void postList(final String uri, final Collection<T> collection) throws IOException {
		if (uri != null && collection != null) {
			// serialize collection of objects into string
			ObjectMapper mapper = new ObjectMapper();
			String jsonPayload = mapper.writeValueAsString(collection);

			HttpClient client = new DefaultHttpClient();
                        HttpPost post = null;
                        try {
                                post = new HttpPost(uri);

				StringEntity content = new StringEntity(jsonPayload);
				content.setContentType("application/json");
				post.setEntity(content);

				client.execute(post);
                        } finally {
                                try {
									if (post != null) {
									        post.releaseConnection();
									}
								} catch (Exception e) {
									
								}
                        }
		}
	}

	public <T> T getData(String uri, Class<T> type) throws IOException {
		T result = null;
		if (uri != null) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = null;
			InputStream payload = null;
			try {
				get = new HttpGet(uri);
				HttpResponse response = client.execute(get);
				payload = response.getEntity().getContent();
				String body = IOUtils.toString(payload, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				result = mapper.readValue(body, type);
			} finally {
				if (payload != null) {
					try {
						payload.close();
					} catch (IOException ioe) {
					}
				}
				try {
					if (get != null) {
						get.releaseConnection();
					}
				} catch (Exception e) {
				}
			}
		}
		return result;
	}

	public <T> List<T> getList(String uri, Class<T> type) throws IOException {
		List<T> result = null;
		if (uri != null) {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = null;
			InputStream payload = null;
			try {
				get = new HttpGet(uri);
				HttpResponse response = client.execute(get);
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				payload = response.getEntity().getContent();

				StringWriter writer = new StringWriter();
				IOUtils.copy(payload, writer, "UTF-8");

				String content = writer.toString();

				result = mapper.readValue(content, mapper.getTypeFactory().constructCollectionType(List.class, type));
			} finally {
                                if (payload != null) {
                                        try {
                                                payload.close();
                                        } catch (IOException ioe) {
                                        }
                                }
                                try {
									if (get != null) {
									        get.releaseConnection();
									}
								} catch (Exception e) {
								}
			}
		}
		return result;
	}

}
