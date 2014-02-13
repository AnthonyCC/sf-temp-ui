package com.freshdirect.webapp.soy;

import java.io.IOException;
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

public class ProductAnnotationsPotatoTag extends SimpleTagSupport {
	private static final Logger LOGGER = LoggerFactory.getInstance( ProductAnnotationsPotatoTag.class );

	protected String annotations;

	protected String productId;
	protected String categoryId;

	
	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}


	private Map<String, ?> extractPotato() {
		FDUserI user = (FDUserI) ((PageContext) getJspContext()).getSession()
				.getAttribute(SessionName.USER);

		return DataPotatoField.digProductAnnotations(user, categoryId, productId);
	}

	
	@Override
	public void doTag() throws JspException, IOException {
		LOGGER.info( "Creating product annotations potato: " + annotations );

		final Map<String,?> dataMap = extractPotato();

		((PageContext)getJspContext()).setAttribute( annotations, dataMap );
	}

	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "annotations" ),
	            		"java.util.Map<String,?>",
	            		true, 
	            		VariableInfo.AT_BEGIN )
	        };
	    }
	}
}
