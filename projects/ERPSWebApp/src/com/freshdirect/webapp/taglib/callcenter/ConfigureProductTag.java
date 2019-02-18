/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ConfiguredProduct;
import com.freshdirect.storeapi.content.PriceCalculator;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ConfigureProductTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance( ConfigureProductTag.class );

	private FDConfiguration configuration;
    private String configDesc;
    private String configProduct;
    private String sku;
    private ProductModel product;

    private FDCartLineModel configProductValue = null;


    public void setConfigDesc(String s) {
        this.configDesc = s;
    }

    public void setConfigProduct(String s) {
        this.configProduct = s;
    }

    public void setSku(String s) {
        this.sku = s;
    }

	public void setProduct(ProductModel p) {
		this.product = p;
	}

    public int doStartTag() throws JspException {

		boolean isSuccess = true;

		try {
			//
			// Get sku for this ProductModel
			//
			FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
			PriceCalculator priceCalculator = new PriceCalculator(user.getPricingContext(), product);
			SkuModel defaultSku = priceCalculator.getSkuModel();
			if (defaultSku==null) {
				return SKIP_BODY;
			}
			String skuCode = defaultSku.getSkuCode();
			FDProductInfo productInfo = priceCalculator.getProductInfo();
			FDProduct fdProd = FDCachedFactory.getProduct( skuCode, productInfo.getVersion() );


			//
			// do this initial set up of the config product here
			//

			// FIXME: variant ID is null here (last param). Is it correct?
			String configDescValue = this.buildConfiguration(fdProd);
			this.configProductValue = new FDCartLineModel(new FDSku(fdProd),
					this.product, this.configuration, null, getUserContext());

			//
			// Set variables in PageContext
			//
			pageContext.setAttribute(configDesc, configDescValue);
			pageContext.setAttribute(configProduct, configProductValue);
			pageContext.setAttribute(sku, skuCode);

		} catch (FDSkuNotFoundException ex) {
			LOGGER.debug("no sku for : " + product.getFullName());
			isSuccess = false;
		}	catch (FDResourceException ex) {
			LOGGER.debug("resource exception for : " + product.getFullName());
			isSuccess = false;
		}
		return isSuccess ? EVAL_BODY_BUFFERED: SKIP_BODY;

    }


	/** @return config description */
	private String buildConfiguration(FDProduct fdproduct) throws FDResourceException {

		FDVariation[] variations = fdproduct.getVariations();

		FDSalesUnit[] units = fdproduct.getSalesUnits();
		String salesUnit = units[0].getName();
		StringBuffer configBuffer = new StringBuffer();

		// construct a default set of options for the product
		Map optionMap = new HashMap();
		if (this.product.isPreconfigured()) {
			FDConfigurableI config = ((ConfiguredProduct)this.product).getConfiguration();
			optionMap = config.getOptions();
			salesUnit = config.getSalesUnit();

			//bahhhhh the option map values are strings, not options....so we got to get the real options
			Map fdOptionsMap = new HashMap();
			for (int i=0; i< variations.length; i++) {
				FDVariationOption[] fdVarOpts = variations[i].getVariationOptions();
				int numOfOptions = fdVarOpts.length;
				for(int j=0; j< numOfOptions; j++) {
					FDVariationOption fdOpt = fdVarOpts[j];
					fdOptionsMap.put(fdOpt.getName(),fdOpt.getDescription());
				}
			}
			for (Iterator optItr = optionMap.values().iterator(); optItr.hasNext();) {
				String configOptionName = (String)optItr.next();
				String optionDesc = (String)fdOptionsMap.get(configOptionName);
				if (optionDesc==null) continue;
				if (configBuffer.length() > 0) configBuffer.append(", ");
				configBuffer.append(optionDesc);
			}
			
		} else {
			for (int i=0; i<variations.length; i++) {
				FDVariation variation = variations[i];
				boolean isOptional = variation.isOptional();
				FDVariationOption[] options = variation.getVariationOptions();
				FDVariationOption option = null;
				if (isOptional) {
					for (int j = 0; j < options.length; j++) {
						if ( options[j].isSelected() ) {
							option = options[j];
							break;
						}
					}
					// no default selection for optional variation -> just pick the first
					option = options[0];
				} else {
					option = options[0];
				}
				optionMap.put(variation.getName(), option.getName());
				if (!option.getDescription().equalsIgnoreCase("None")) {
					if ((i != 0) && (configBuffer.length() > 0)) configBuffer.append(", ");
					configBuffer.append(option.getDescription());
				}
			}
		}
		//
		// make the configured product for this sales unit & these options
		//
		this.configuration = new FDConfiguration(0, salesUnit, optionMap);
		//this.configProductValue = new FDConfiguredProduct(fdproduct, salesUnit.getName(), optionMap);
		// set the configuration description
		return configBuffer.toString();

	}

    private UserContext getUserContext() {
        FDUserI user = FDSessionUser.getFDSessionUser(pageContext.getSession());
        if (user == null) {
            throw new FDRuntimeException("User object is Null");
        }
        return user.getUserContext();
    }


}
