package com.freshdirect.fdstore.content;

import java.text.MessageFormat;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

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
	    return FDAttributeFactory.constructImage(this, "logo");
	}
	
	public String createIsbnLink(String isbn) {
		return MessageFormat.format(getIsbnLink(), new Object[] { isbn });
	}

}
