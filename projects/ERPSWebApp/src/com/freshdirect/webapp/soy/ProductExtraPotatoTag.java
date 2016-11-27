package com.freshdirect.webapp.soy;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

@Deprecated
public class ProductExtraPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( ProductExtraPotatoTag.class );

	protected String name;
	protected String productId;
	protected String categoryId;
	protected String grpId;
	protected String version;

	public String getName() {
		return name;
	}	
	public void setName( String name ) {
		this.name = name;
	}
	public String getProductId() {
		return productId;
	}	
	public void setProductId( String productId ) {
		this.productId = productId;
	}	
	public String getCategoryId() {
		return categoryId;
	}	
	public void setCategoryId( String categoryId ) {
		this.categoryId = categoryId;
	}
	
	public String getGrpId() {
		return grpId;
	}
	
	public void setGrpId(String grpId) {
		this.grpId = grpId;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	

	private Map<String, ?> extractPotato() {
		FDUserI user = (FDUserI) ((PageContext) getJspContext()).getSession()
				.getAttribute(SessionName.USER);

		return DataPotatoField.digProductExtraData(user, categoryId, productId, ((PageContext) getJspContext()).getServletContext(), grpId, version );
	}

	@Override
	public void doTag() throws JspException {
		LOGGER.info( "Creating data potato: " + name );
		
		Map<String,?> dataMap = extractPotato();

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
