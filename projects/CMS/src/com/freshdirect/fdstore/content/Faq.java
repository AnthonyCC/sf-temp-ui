package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

/**
 * @author ksriram
 *
 */
public class Faq extends ContentNodeModelImpl {

	private static final long serialVersionUID = 1L;

	public Faq(ContentKey key) {
		super(key);
	}
	
	public String getQuestion(){
		return getAttribute("QUESTION", "");
	}
	
	public String getAnswer(){
		return getAttribute("ANSWER", "");
	}
	
	public String getKeywords() {
		return getAttribute("KEYWORDS", "");
	}
	
	public String getDescription() {
		return getFullName();
	}
	

}
