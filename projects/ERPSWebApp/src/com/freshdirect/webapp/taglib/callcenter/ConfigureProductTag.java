/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.callcenter;

import java.util.*;
import javax.servlet.jsp.JspException;

import org.apache.log4j.*;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.customer.*;

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
		        String skuCode = product.getDefaultSkuCode();
			if (skuCode==null) {
				return SKIP_BODY;
			}

			FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
			FDProduct fdProd = FDCachedFactory.getProduct( skuCode, productInfo.getVersion() );


			//
			// do this initial set up of the config product here
			//

			// FIXME: variant ID is null here (last param). Is it correct?
			String configDescValue = this.buildConfiguration(fdProd);
			this.configProductValue = new FDCartLineModel(new FDSku(fdProd),
					this.product, this.configuration, null);

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
System.out.println(" Config  Desc: "+configBuffer.toString());
		//this.configProductValue = new FDConfiguredProduct(fdproduct, salesUnit.getName(), optionMap);
		// set the configuration description
		return configBuffer.toString();

	}



}