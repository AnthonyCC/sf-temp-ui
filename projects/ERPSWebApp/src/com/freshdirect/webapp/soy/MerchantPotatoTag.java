package com.freshdirect.webapp.soy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.content.PopulatorUtil;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.webapp.ajax.DataPotatoField;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.ProductRecommenderUtil;

/**
 * A potato tag that serves product recommendations for a given product
 * Currently, upsell and cross-sell options are available.
 * 
 * TBD: configure {@link SessionInput} for Cross-Sell recommender
 * 
 * @author segabor
 *
 */
public class MerchantPotatoTag extends SimpleTagSupport {
	private static final Logger LOGGER = LoggerFactory.getInstance( MerchantPotatoTag.class );

	/**
	 * Optional parameter
	 */
	protected String upSellName;

	/**
	 * Optional parameter
	 */
	protected String crossSellName;

	protected String productId;

	protected String categoryId;

	public void setUpSellName(String upSellName) {
		this.upSellName = upSellName;
	}
	
	public void setCrossSellName(String crossSellName) {
		this.crossSellName = crossSellName;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public void doTag() throws JspException, IOException {
		if (upSellName == null && crossSellName == null) {
			LOGGER.warn("Neither upsell nor cross-sell param was given, skip tag");
		}
		
		// Get the user
		final FDUserI user = (FDUserI) ((PageContext) getJspContext()).getSession().getAttribute(SessionName.USER);
		if (user == null) {
			LOGGER.error("User is null! Skipping tag.");
			return;
		}
		
		// Get the ProductModel
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );		
		if (product == null) {
			LOGGER.error("Product with id " + productId + " not found");
			return;
		}
		
		try {
			SkuModel skuModel = PopulatorUtil.getDefSku(product);
			if (skuModel != null && skuModel.getProduct() != null && skuModel.getProduct().isDeliveryPass()) {
				//Ensuring that recommenders will be empty as per BRD
				((PageContext)getJspContext()).setAttribute( upSellName, new HashMap<String, Object>() );
				((PageContext)getJspContext()).setAttribute( crossSellName, new HashMap<String, Object>() );
				return;
			}
		} catch (FDException e) {
			LOGGER.error("Error during determining DeliveryPass attribute value...");
		}

		// produce upsell list
		if (upSellName != null) {
			Map<String, ?> basicPotatoList = extractUpsellPotato(user, product);
			((PageContext)getJspContext()).setAttribute( upSellName, basicPotatoList );
		}

		// produce cross-sell list
		if (crossSellName != null) {
			Map<String,?> basicPotatoList = extractCrossSellPotato(user, product);
			((PageContext)getJspContext()).setAttribute( crossSellName, basicPotatoList );
		}
	}


	/**
	 * @param user 
	 * @param product 
	 * @return
	 */
	private Map<String, ?> extractCrossSellPotato(FDUserI user, ProductModel product) {
		return DataPotatoField.digProductListFromData(user, ProductRecommenderUtil.getCrossSellProducts(product, user));
	}


	/**
	 * @param user 
	 * @param product 
	 * @return
	 */
	private Map<String, ?> extractUpsellPotato(FDUserI user, ProductModel product) {
		return DataPotatoField.digProductListFromModels(user, ProductRecommenderUtil.getUpsellProducts(product));
	}


	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
		@Override
	    public VariableInfo[] getVariableInfo(TagData data) {

	        return new VariableInfo[] {
	            new VariableInfo(
	            		data.getAttributeString( "upSellName" ),
	            		"java.util.Map<String,?>",
	            		true, 
	            		VariableInfo.AT_BEGIN ),
	            new VariableInfo(
	            		data.getAttributeString( "crossSellName" ),
	            		"java.util.Map<String,?>",
	            		true, 
	            		VariableInfo.AT_BEGIN )
	        };
	    }
	}
}
