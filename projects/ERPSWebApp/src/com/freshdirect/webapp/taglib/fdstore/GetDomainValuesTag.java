package com.freshdirect.webapp.taglib.fdstore;

/**
 * 
 * @author knadeem
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.webapp.taglib.AbstractGetterTag;


public class GetDomainValuesTag extends AbstractGetterTag {
	
	String domainID = null;
	
	public void setDomainID(String domainID) {
		this.domainID = domainID;
	}
	
	protected Object getResult() throws FDResourceException {
		List values=ContentFactory.getInstance().getDomainById(domainID).getDomainValues();
		List domainValues=new ArrayList(values.size());
		DomainValue dValue=null;
		for(int i=0;i<values.size();i++) {
			dValue=(DomainValue)values.get(i);
			domainValues.add(dValue.getValue());
		}
		return domainValues;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}

