package com.freshdirect.webapp.taglib.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.ContentNodeI;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;

public class WineFilterTag extends com.freshdirect.framework.webapp.BodyTagSupport {
       
    private String id;
    private List displayList;
    private String domainName;
    private String domainValue;
    
    public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getDomainValue() {
		return domainValue;
	}
	public void setDomainValue(String domainValue) {
		this.domainValue = domainValue;
	}
	public String getId() {
        return (this.id);
    }
    public void setId(String id) {
        this.id = id;
    }
   
    public List getDisplayList() {
        return (this.displayList);
    }
    public void setDisplayList(List dispList) {
        this.displayList = dispList;
    }
    
    public int doStartTag() throws JspException {
    	if(domainName == null || domainName.length() == 0 ||  domainValue == null || domainValue.length() == 0 || domainValue.equals("ALL")){
			 return SKIP_BODY;
    	}
    	List filteredList =  new ArrayList();
    	for(Iterator iter = displayList.iterator(); iter.hasNext();){
    		ContentNodeModel contentNode = (ContentNodeModel) iter.next();
    		if (contentNode.getContentType().equals(ContentNodeI.TYPE_PRODUCT)){
    			ProductModel product = (ProductModel) contentNode;
    			List domainValues = product.getWineClassifications();
    			for(Iterator domainIter = domainValues.iterator(); domainIter.hasNext();){
    				DomainValue domainValueObj =  (DomainValue) domainIter.next();
    				if(domainValueObj == null || domainValueObj.getDomain() == null)
    					////The Domain value is null or its Parent domain is null. Ignore it.
    					continue;
    				String prodDomainName = domainValueObj.getDomain().getName();
    				String prodDomainValue = domainValueObj.getContentName();
    				if(domainName.equals(prodDomainName) && domainValue.equals(prodDomainValue)){
    					//This product has domain name and value we are looking for. so add to the filtered list.
    					filteredList.add(product);
    				}
    			}
    		}
    	}
        pageContext.setAttribute(id, filteredList);
        return (EVAL_BODY_BUFFERED);
    }
   
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("id"),
					"java.util.List",
					true,
					VariableInfo.NESTED )
					
			};
		}
	}
}

