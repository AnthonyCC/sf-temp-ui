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


public class CartConfirmPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( CartConfirmPotatoTag.class );
		
	private String name;
	private String cartlineId;
	
	public String getName() {
		return name;
	}	
	public void setName( String name ) {
		this.name = name;
	}
	
	public String getCartlineId() {
		return cartlineId;
	}	
	public void setCartlineId( String cartlineId ) {
		this.cartlineId = cartlineId;
	}

	@Override
	public void doTag() throws JspException {
		
		LOGGER.info( "Creating data potato: " + name );
		
		FDUserI user = (FDUserI)((PageContext)getJspContext()).getSession().getAttribute( SessionName.USER );
		
		Map<String,?> dataMap = DataPotatoField.digCartConfirm( user, cartlineId );

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
