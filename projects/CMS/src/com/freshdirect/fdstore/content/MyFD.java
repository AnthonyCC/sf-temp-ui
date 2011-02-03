package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class MyFD extends ContentNodeModelImpl {
	private List<HolidayGreeting> holidayGreetings = new ArrayList<HolidayGreeting>();

	public MyFD(ContentKey key) {
		super(key);
	}

	@SuppressWarnings("unchecked")
	public List<Image> getHeader() {
		return FDAttributeFactory.constructWrapperList(this, "HEADER");
	}
	
	public Html getPollWidget() {
		return FDAttributeFactory.constructHtml(this, "POLL_WIDGET");
	}

	public String getBlogURL() {
		String url = this.getAttribute("BLOG_URL", "http://blog.freshdirect.com");
		while (url.endsWith("/"))
			url = url.substring(0, url.length() - 1);
		return url;
	}
	
	public int getBlogEntryCount() {
		return getAttribute("BLOG_ENTRY_COUNT", 3);
	}

	@SuppressWarnings("unchecked")
	public List<MediaModel> getEditorialMain() {
		return FDAttributeFactory.constructWrapperList(this, "EDITORIAL_MAIN");
	}

	@SuppressWarnings("unchecked")
	public List<MediaModel> getEditorialSide() {
		return FDAttributeFactory.constructWrapperList(this, "EDITORIAL_SIDE");
	}
	
	public List<HolidayGreeting> getHolidayGreetings() {
        ContentNodeModelUtil.refreshModels(this, "HOLIDAY_GREETINGS", holidayGreetings, true);
		return holidayGreetings;
	}
	
	public static MyFD getMyFDInstance() {
		StoreModel store = (StoreModel) ContentFactory.getInstance().getContentNode("Store", "FreshDirect");
		MyFD myfd = null;
		if (store != null) {
			myfd = store.getMyFD();
		}
		return myfd;
	}

	public String getPollDaddyApiKey() {
		return getAttribute("POLL_DADDY_API_KEY", "*illegal_key*");
	}
}
