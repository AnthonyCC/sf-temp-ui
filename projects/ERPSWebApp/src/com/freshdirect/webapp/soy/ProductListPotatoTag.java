package com.freshdirect.webapp.soy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/* Pass in a List of product models, and convert them to ProductDatas
 * optionally pass in perLine to add that attribute to the returned data */

public class ProductListPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( ProductListPotatoTag.class );
		
	private String name;
	List<ProductModel> productList = new ArrayList<ProductModel>();
	private int perLine = 0;
		
	public List<ProductModel> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductModel> productList) {
		this.productList = productList;
	}
	public String getName() {
		return name;
	}	
	public void setName( String name ) {
		this.name = name;
	}
	public int getPerLine() {
		return perLine;
	}	
	public void setPerLine( int perLine ) {
		this.perLine = perLine;
	}
	
	@Override
	public void doTag() throws JspException {
		
		LOGGER.info( "Creating data potato: " + name );
		
		HttpSession session = ((PageContext)getJspContext()).getSession();
		FDUserI user = (FDUserI)session.getAttribute( SessionName.USER );
		
		if (this.perLine == 0) {
			this.setPerLine(productList.size());
		}

		HashMap<String,Object> dataMap = new HashMap<String,Object>();
		Map<String,?> productListMap = null;
		
		productListMap = DataPotatoField.digProductListFromModels( user, productList );
		
		dataMap.put("itemPerLine", this.getPerLine());
		dataMap.put("productList", productListMap.get("productList"));

		((PageContext)getJspContext()).setAttribute( name, dataMap );
		
	}
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "name" ),
	            		"java.util.Map<String,?>",
	            		true, 
	            		VariableInfo.AT_BEGIN )
	        };
	    }
	}
}
