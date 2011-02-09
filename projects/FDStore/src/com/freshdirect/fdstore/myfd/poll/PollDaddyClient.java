package com.freshdirect.fdstore.myfd.poll;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.MyFD;

public class PollDaddyClient {
	private static DateFormat POLL_DADDY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) throws IOException, PollDaddyProtocolException, ParseException {
		PollDaddyClient service = new PollDaddyClient();
		System.out.println(service.getPolls());
	}

	private HttpClient client;
	private String userCode;

	public PollDaddyClient() {
		client = new DefaultHttpClient();
		userCode = null;
	}

	public String getApiKey() {
		String apiKey = FDStoreProperties.getMyFdPollDaddyApiKey();
		if (apiKey != null)
			return apiKey.trim();
		MyFD myfd = MyFD.getMyFDInstance();
		if (myfd != null)
			apiKey = myfd.getPollDaddyApiKey();
		if (apiKey == null || apiKey.length() == 0)
			apiKey = "*illegal_key*";
		return apiKey;
	}

	public List<Poll> getPolls() throws IOException, PollDaddyProtocolException, ParseException {
		List<Poll> polls = new ArrayList<Poll>();
		JSONArray array = getPollsInternal(1, 12);
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			String id = obj.getString("id");
			String question = obj.getString("content");
			String isoDate = obj.getString("created");
			Date created = POLL_DADDY_DATE_FORMAT.parse(isoDate);
			Poll poll = new Poll(id, question, created);
			int closed = obj.getInt("closed");
			if (closed > 0)
				poll.setClosed(true);
			polls.add(poll);
		}
		return polls;
	}

	private HttpPost createPost() {
		HttpPost post = new HttpPost("https://api.polldaddy.com/");
		post.setHeader("Content-Type", "application/json");
		return post;
	}

	private String getUserCode() throws IOException, PollDaddyProtocolException {
		HttpPost post = createPost();
		String requestString = constructGetUserCode().toString();
		post.setEntity(new StringEntity(requestString));
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String contentBody = EntityUtils.toString(entity);
			JSONTokener tokener = new JSONTokener(contentBody);
			try {
				Object o = tokener.nextValue();
				if (o instanceof JSONObject) {
					JSONObject obj = (JSONObject) o;
					obj = obj.getJSONObject("pdResponse");
					if (obj != null) {
						String userCode = obj.getString("userCode");
						if (userCode != null)
							return userCode;
					}
				}
				throw new PollDaddyProtocolException("response returned malformed content");
			} catch (ParseException e) {
				throw new PollDaddyProtocolException("response returned malformed JSON stream");
			}
		} else
			throw new PollDaddyProtocolException("response returned no entity");
	}

	private JSONArray getPollsInternal(int start, int end) throws IOException, PollDaddyProtocolException {
		if (userCode == null)
			userCode = getUserCode();
		HttpPost post = createPost();
		String requestString = constructGetPolls(userCode, start, end).toString();
		post.setEntity(new StringEntity(requestString));
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String contentBody = EntityUtils.toString(entity);
			JSONTokener tokener = new JSONTokener(contentBody);
			try {
				Object o = tokener.nextValue();
				if (o instanceof JSONObject) {
					JSONObject obj = (JSONObject) o;
					obj = obj.getJSONObject("pdResponse");
					if (obj != null) {
						obj = obj.getJSONObject("demands");
						if (obj != null) {
							JSONArray array = obj.getJSONArray("demand");
							if (array != null) {
								obj = array.getJSONObject(0); // we expect only one object
								if (obj != null) {
									obj = obj.getJSONObject("polls");
									if (obj != null) {
										array = obj.getJSONArray("poll");
										if (array != null)
											return array;
									}
								}
							}
						}
					}
				}
				throw new PollDaddyProtocolException("response returned malformed content");
			} catch (ParseException e) {
				throw new PollDaddyProtocolException("response returned malformed JSON stream");
			}
		} else
			throw new PollDaddyProtocolException("response returned no entity");
	}

	@SuppressWarnings("unused")
	private JSONObject getPoll(String pollId) throws IOException, PollDaddyProtocolException {
		if (userCode == null)
			userCode = getUserCode();
		HttpPost post = createPost();
		String requestString = constructGetPoll(userCode, pollId).toString();
		post.setEntity(new StringEntity(requestString));
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String contentBody = EntityUtils.toString(entity);
			JSONTokener tokener = new JSONTokener(contentBody);
			try {
				Object o = tokener.nextValue();
				if (o instanceof JSONObject) {
					JSONObject obj = (JSONObject) o;
					obj = obj.getJSONObject("pdResponse");
					if (obj != null) {
						obj = obj.getJSONObject("demands");
						if (obj != null) {
							JSONArray array = obj.getJSONArray("demand");
							if (array != null) {
								obj = array.getJSONObject(0); // we expect only one object
								if (obj != null) {
									obj = obj.getJSONObject("poll");
									if (obj != null) {
										return obj;
									}
								}
							}
						}
					}
				}
				throw new PollDaddyProtocolException("response returned malformed content");
			} catch (ParseException e) {
				throw new PollDaddyProtocolException("response returned malformed JSON stream");
			}
		} else
			throw new PollDaddyProtocolException("response returned no entity");
	}

	@SuppressWarnings("unused")
	private JSONArray getPollResults(String pollId) throws IOException, PollDaddyProtocolException {
		if (userCode == null)
			userCode = getUserCode();
		HttpPost post = createPost();
		String requestString = constructGetPollResults(userCode, pollId).toString();
		post.setEntity(new StringEntity(requestString));
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String contentBody = EntityUtils.toString(entity);
			JSONTokener tokener = new JSONTokener(contentBody);
			try {
				Object o = tokener.nextValue();
				if (o instanceof JSONObject) {
					JSONObject obj = (JSONObject) o;
					obj = obj.getJSONObject("pdResponse");
					if (obj != null) {
						obj = obj.getJSONObject("demands");
						if (obj != null) {
							JSONArray array = obj.getJSONArray("demand");
							if (array != null) {
								obj = array.getJSONObject(0); // we expect only one object
								if (obj != null) {
									obj = obj.getJSONObject("result");
									if (obj != null) {
										obj = obj.getJSONObject("answers");
										if (obj != null) {
											array = obj.getJSONArray("answer");
											if (array != null)
												return array;
										}
									}
								}
							}
						}
					}
				}
				throw new PollDaddyProtocolException("response returned malformed content");
			} catch (ParseException e) {
				throw new PollDaddyProtocolException("response returned malformed JSON stream");
			}
		} else
			throw new PollDaddyProtocolException("response returned no entity");
	}

	private JSONObject constructGetUserCode() {
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		obj.put("pdAccess", obj1);
		obj1.put("partnerGUID", getApiKey());
		obj1.put("partnerUserID", "0");
		JSONObject obj2 = new JSONObject();
		obj1.put("demands", obj2);
		obj1 = new JSONObject();
		obj2.put("demand", obj1);
		obj1.put("id", "GetUserCode");
		return obj;
	}

	private JSONObject constructGetPolls(String userCode, int start, int end) {
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		obj.put("pdRequest", obj1);
		obj1.put("partnerGUID", getApiKey());
		obj1.put("userCode", userCode);
		JSONObject obj2 = new JSONObject();
		obj1.put("demands", obj2);
		obj1 = new JSONObject();
		obj2.put("demand", obj1);
		obj1.put("id", "GetPolls");
		if (start >= 0 && end >= 0) {
			obj2 = new JSONObject();
			obj1.put("list", obj2);
			obj2.put("start", Integer.toString(start));
			obj2.put("end", Integer.toString(end));
		}
		return obj;
	}

	private JSONObject constructGetPoll(String userCode, String pollId) {
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		obj.put("pdRequest", obj1);
		obj1.put("partnerGUID", getApiKey());
		obj1.put("userCode", userCode);
		JSONObject obj2 = new JSONObject();
		obj1.put("demands", obj2);
		obj1 = new JSONObject();
		obj2.put("demand", obj1);
		obj2 = new JSONObject();
		obj1.put("poll", obj2);
		obj2.put("id", pollId);
		obj1.put("id", "GetPoll");
		return obj;
	}

	private JSONObject constructGetPollResults(String userCode, String pollId) {
		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();
		obj.put("pdRequest", obj1);
		obj1.put("partnerGUID", getApiKey());
		obj1.put("userCode", userCode);
		JSONObject obj2 = new JSONObject();
		obj1.put("demands", obj2);
		obj1 = new JSONObject();
		obj2.put("demand", obj1);
		obj2 = new JSONObject();
		obj1.put("poll", obj2);
		obj2.put("id", pollId);
		obj1.put("id", "GetPollResults");
		return obj;
	}
}
