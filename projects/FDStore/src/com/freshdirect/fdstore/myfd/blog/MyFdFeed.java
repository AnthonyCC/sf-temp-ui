package com.freshdirect.fdstore.myfd.blog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.MyFD;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class MyFdFeed implements Serializable, Iterable<MyFdPost> {
	private static final long serialVersionUID = -8648764873781055003L;

	private static long REFRESH_PERIOD = 1000 * 60 * 5; // 5 minutes

	private static BalkingExpiringReference<MyFdFeed> THE_FEED;

	public static final String FEED_URL = "/feed/";
	public static final String MYFD_FEED_URL = "/feed/fd/";

	private static class MyFdFeedReference extends BalkingExpiringReference<MyFdFeed> {
		private static final Logger LOG = LoggerFactory.getInstance(MyFdFeedReference.class);

		public MyFdFeedReference() {
			super(REFRESH_PERIOD);
			set(load());
		}

		@Override
		protected MyFdFeed load() {
			LOG.info("(re)loading feed started...");
			String blogURL;
			int blogEntryCount;
			StoreModel store = (StoreModel) ContentFactory.getInstance().getContentNode("Store", "FreshDirect");
			MyFD myfd = store != null ? store.getMyFD() : null;
			if (myfd != null) {
				blogURL = myfd.getBlogURL();
				blogEntryCount = myfd.getBlogEntryCount();
			} else {
				blogURL = null;
				blogEntryCount = 3;
			}
			MyFdFeed feed = new MyFdFeed(blogURL, blogEntryCount);
			LOG.info("(re)loading feed completed");
			return feed;
		}
	}

	public synchronized static MyFdFeed getInstance() {
		if (THE_FEED == null) {
			THE_FEED = new MyFdFeedReference();
		}
		return THE_FEED.get();
	}

	private String feedUrl;
	private ArrayList<MyFdPost> posts;
	private String blogUrl;
	private int blogEntryCount;
	private final Date lastUpdated;

	private MyFdFeed(String blogUrl, int blogEntryCount) {
		this.blogUrl = blogUrl;
		this.blogEntryCount = blogEntryCount;
		this.feedUrl = blogUrl + MYFD_FEED_URL;
		this.posts = new ArrayList<MyFdPost>();
		processFeedUrl();
		lastUpdated = new Date();
	}

	protected void processFeedUrl() throws RuntimeException {
		if (feedUrl == null)
			return;

		try {
			Document document = DOMUtils.urlToNode(feedUrl);
			NodeList postNodes = document.getElementsByTagName("item");
			for (int i = 0; i < Math.min(postNodes.getLength(), blogEntryCount); ++i) {
				try {
					posts.add(new MyFdPost((Element) postNodes.item(i)));
				} catch (RuntimeException e) {
				}
			}

		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public ArrayList<MyFdPost> getPosts() {
		return posts;
	}

	public String getBlogUrl() {
		return blogUrl;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	@Override
	public Iterator<MyFdPost> iterator() {
		return posts.iterator();
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
}
