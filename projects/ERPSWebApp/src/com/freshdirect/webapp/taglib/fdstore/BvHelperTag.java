package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;

public class BvHelperTag extends SimpleTagSupport{
	
	private ProductModel prod;
	
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext)getJspContext(); 
		
		List<String> bvFreeDepts = new ArrayList<String>();
		bvFreeDepts.add("veg");
		bvFreeDepts.add("fru");
		bvFreeDepts.add("sea");
		bvFreeDepts.add("mea");
		bvFreeDepts.add("usq");

		boolean showReviews = true;
		for(ContentKey key : prod.getParentKeys()){
			
			ProductModel nodeByKey = ContentFactory.getInstance().getProductByName(key.getId(), prod.getContentKey().getId());
			
			if(bvFreeDepts.contains(nodeByKey.getDepartment().getContentKey().getId())){
				showReviews = false;
				break;
			}
		}
		pageContext.setAttribute("showReviews", showReviews);
	}

	public ProductModel getProd() {
		return prod;
	}

	public void setProd(ProductModel prod) {
		this.prod = prod;
	}

}
