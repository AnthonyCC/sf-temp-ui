package com.freshdirect.fdstore.myfd.blog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.MyFD;
import com.freshdirect.fdstore.content.StoreModel;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.RuntimeServiceUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class MyFdFeed implements Serializable, Iterable<MyFdPost> {
	private static final long serialVersionUID = -8648764873781055003L;

	private static final Logger LOGGER = LoggerFactory.getInstance(MyFdFeed.class);

	private static long REFRESH_PERIOD = 1000 * 60 * 5; // 5 minutes

	private static BalkingExpiringReference<MyFdFeed> THE_FEED;

	public static final String FEED_URL = "/feed/";
	public static final String MYFD_FEED_URL = "/feed/fd/";
	public static final String MYFD_FEED_SAVE_FILE = "fdfeed.rss";
	public static final String MYFD_FEED_SAVE_FILE_TEMP = "fdfeed_temp.rss";

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
			String propUrl = FDStoreProperties.getMyfdBlogUrl();
			if (propUrl != null && !propUrl.trim().isEmpty())
				blogURL = propUrl;
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

	private MyFdFeed(String blogUrl, int blogEntryCount) {
		this.blogUrl = blogUrl;
		this.blogEntryCount = blogEntryCount;
		this.feedUrl = blogUrl + MYFD_FEED_URL;
		this.posts = new ArrayList<MyFdPost>();

		processFeed();
	}

	private void processFeed() {

		String rootDirectory =  RuntimeServiceUtil.getInstance().getRootDirectory();

		if (rootDirectory == null){
			processFeedUrl(feedUrl);
			
		} else {
		
			String feedFilePath = rootDirectory + File.separator + MYFD_FEED_SAVE_FILE;
			String feedFileTempPath = rootDirectory + File.separator + MYFD_FEED_SAVE_FILE_TEMP;
			
			try {
				saveFeedToFile(feedFileTempPath);
				processFeedUrl(feedFileTempPath);

				//deleting old file and renaming temp file
				new File(feedFilePath).delete();
				new File(feedFileTempPath).renameTo(new File(feedFilePath));
				
			} catch (Exception e) {
				LOGGER.warn("Error in processing feed from blog URL, opening last saved version...", e);
				try{
					processFeedUrl(feedFilePath);
				} catch (Exception e2) {
					posts.clear();
					LOGGER.error("Error in opening last saved version...", e);
				}
			}
		}
	}

	
	private void processFeedUrl(String url) {
		posts.clear();
		Document document = DOMUtils.urlToNode(url);
		NodeList postNodes = document.getElementsByTagName("item");
		for (int i = 0; i < Math.min(postNodes.getLength(), blogEntryCount); ++i) {
			posts.add(new MyFdPost((Element) postNodes.item(i)));
		}
	}

	private void saveFeedToFile(String filePath) {
		
		BufferedInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(feedUrl).openStream());
			out = new FileOutputStream(filePath);
			byte[] buffer = new byte[1024];
			int len = in.read(buffer);
			while (len != -1) {
			    out.write(buffer, 0, len);
			    len = in.read(buffer);
			}
		} catch (MalformedURLException e) {
			LOGGER.error("Error in opening feed URL", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			LOGGER.error("Error in saving feed to file", e);
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
	            try {
					in.close();
				} catch (IOException e) {
					LOGGER.error("IO error closing feed URL stream", e);
				}
			}
			if (out != null) {
	            try {
	            	out.close();
				} catch (IOException e) {
					LOGGER.error("IO error closing feed file stream", e);
				}
			}
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
}
