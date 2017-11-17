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
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ProductPotatoTag extends SimpleTagSupport {

	private static final Logger LOGGER = LoggerFactory.getInstance( ProductPotatoTag.class );

	protected String name;
	/**
	 * Optional parameter
	 */
	protected String extraName;
	protected String productId;
	protected String categoryId;
	/** optional */
	protected String variantId;
	protected String grpId;
	protected String version;

	public String getName() {
		return name;
	}	
	public void setName( String name ) {
		this.name = name;
	}
	public void setExtraName(String extraName) {
		this.extraName = extraName;
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
	
	public void setVariantId(String variantId) {
		this.variantId = variantId;
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
	
    @Override
	public void doTag() throws JspException {
		LOGGER.info( "Creating data potato: " + name );

		// Get the user
		final FDUserI user = (FDUserI) ((PageContext) getJspContext()).getSession().getAttribute(SessionName.USER);
		if (user == null) {
			LOGGER.error("User is null! Skipping tag.");
			return;
		}
		
		// Get the ProductModel
		final ProductModel product = PopulatorUtil.getProduct( productId, categoryId );		
		if (product == null) {
			LOGGER.error("Product with id " + productId + " not found");
			return;
		}

		
		/*
		 * This part checks if product is considered 'incomplete'
		 * that is product and sku is already created in CMS
		 * but data on ERPS side is missing. If so, potatoes must be
		 * populated regarding this fact.
		 */
		boolean incomplete = false;
		try {
            incomplete = PopulatorUtil.isProductIncomplete(product) && !PopulatorUtil.isNodeArchived(product);
		} catch (Exception e) {
			throw new JspException(e);
		}


        if (incomplete) {
            // minimal potato population
            LOGGER.info("Product " + productId + " is considered 'incomplete', produce light potatos");
            final Map<String, ?> dataMap = DataPotatoField.digProductLight(user, product);
            ((PageContext) getJspContext()).setAttribute(name, dataMap);

            if (extraName != null) {
                LOGGER.info("Creating light extra potato: " + extraName);
                final Map<String, ?> extraMap = DataPotatoField.digProductLightExtraData(user, product);
                ((PageContext) getJspContext()).setAttribute(extraName, extraMap);
            }
        } else {
            // normal population
            LOGGER.info("Product " + productId + " is produced potatos.");
            final Map<String, ?> dataMap = DataPotatoField.digProduct(user, product, variantId);
            ((PageContext) getJspContext()).setAttribute(name, dataMap);

            if (extraName != null) {
                LOGGER.info("Creating extra potato: " + extraName);
                final Map<String, ?> extraMap = DataPotatoField.digProductExtraData(user, product, ((PageContext) getJspContext()).getServletContext(), grpId, version);
                ((PageContext) getJspContext()).setAttribute(extraName, extraMap);
            }
        }
	}
	
	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "name" )!=null?data.getAttributeString( "name" ):"dummy_name",
	            		"java.util.Map<String,?>",
	            		true, 
	            		VariableInfo.AT_BEGIN ),
	            new VariableInfo(
	            		data.getAttributeString( "extraName" )!=null?data.getAttributeString( "extraName" ):"dummy_extra_name",
	            		"java.util.Map<String,?>",
	            		true, 
	            		VariableInfo.AT_BEGIN )
	        };
	    }
	}
}
