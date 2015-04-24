package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

public class Stopwords extends ContentNodeModelImpl {
	private static final long serialVersionUID = 1L;

	public Stopwords(ContentKey key) {
		super(key);
		// TODO Auto-generated constructor stub
	}
	
	public String getWord(){
		return getAttribute("word", "");
	}
}
