package com.freshdirect.fdstore.content;

import java.text.MessageFormat;

import com.freshdirect.cms.ContentKey;

public class BookRetailer extends ContentNodeModelImpl {

	public BookRetailer(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}
	
	public String getIsbnLink() {
		return getAttribute("isbnLink", "");
	}

	public String getNotes() {
		return getAttribute("notes", "");
	}

	public Image getLogo() {
		return (Image) getAttribute("logo", (Image) null);
	}
	
	public String createIsbnLink(String isbn) {
		return MessageFormat.format(getIsbnLink(), new Object[] { isbn });
	}

}
