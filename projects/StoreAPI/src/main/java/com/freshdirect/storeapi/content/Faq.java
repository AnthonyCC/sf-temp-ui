package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;

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

	@Override
    public String getKeywords() {
		return getAttribute("KEYWORDS", "");
	}

	public String getFaqPriorityList() {
		return getAttribute("PRIORITY_LIST", "");
	}

	public String getDescription() {
		return getFullName();
	}

	public Integer calculatePriority(String word){
		int p = -1;

		String tagSource = getFaqPriorityList();
		for(String tag : tagSource.split(",")){

			tag = tag.trim();
			String[] elements = tag.split(":");

			if(elements.length!=2){
				continue;
			}

			elements[0] = elements[0].trim();
			elements[1] = elements[1].trim();

			if(elements[0].equals(word)){
				return Integer.parseInt(elements[1]);
			}
		}

		return p;
	}


}
