/**
 * 
 */
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
		// TODO Auto-generated constructor stub
	}
	
	public String getQuestion(){
		return getAttribute("question", "");
	}
	
	public String getAnswer(){
		return getAttribute("answer", "");
	}
	
	public String getDescription() {
		return getFullName();
	}
	

}
