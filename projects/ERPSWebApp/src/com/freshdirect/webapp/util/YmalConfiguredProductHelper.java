package com.freshdirect.webapp.util;

import java.util.Iterator;
import java.util.Map;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;

/**
 *  A class helping the display of products as YMALs.
 */
public class YmalConfiguredProductHelper extends YmalProductHelper
                                         implements YmalHelper {
	/**
	 *  Constructor.
	 *  
	 *  @param product the product that needs to be displayed as YMAL.
	 */
	YmalConfiguredProductHelper(ConfiguredProduct product) {
		super(product, true);
	}
	
	/**
	 *  Return the SKU code of the product related to this helper.
	 *  This will be the only SKU code if the product only has one
	 *  (like configured products), or the first SKU code.
	 *  
	 *  @return the SKU code for the product related to this helper.
	 */
	public String getSkuCode() {
		return getConfiguredProduct().getSkuCode();
	}

	/**
	 *  Return the configuration of the product related to this helper,
	 *  if applicable.
	 *  
	 *  @return the configuration of the product if this is a configured
	 *          or auto-configurable product. null otherwise.
	 */
	public FDConfigurableI getConfiguration() {
		return getConfiguredProduct().getConfiguration();
	}

	/**
	 *  Get the real product (i.e. not configured product,
	 *  configured product group, etc.) related to this helper.
	 *  
	 *  @return the real product related to this helper.
	 */
	public ProductModel getRealProduct() {
		return getConfiguredProduct().getProduct();
	}

	/**
	 *  Helper to get the wrapped configured product in a type-safe manner.
	 *  
	 *  @return the wrapped configured product. 
	 */
	protected ConfiguredProduct getConfiguredProduct() {
		return (ConfiguredProduct) getProduct();
	}
	
	/**
	 *  Return the category id for the product.
	 *  
	 *  @return the category id for the product.
	 */
	public String getCatId() {
		return getConfiguredProduct().getProduct().getParentNode().toString();
	}

	/**
	 *  Generate URL GET parameters from a Map.
	 *  
	 *  @param map the map to generate from
	 *  @return URL GET parameters, based on the map, so that the map keys
	 *          are parameter names, and their values are the parameter values.
	 */
	protected String mapToUrlParamList(Map map) {
		StringBuffer strBuf = new StringBuffer();
		
		for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry entry = (Map.Entry) it.next();
			
			strBuf.append(entry.getKey());
			strBuf.append("=");
			strBuf.append(entry.getValue());
			
			if (it.hasNext()) {
				strBuf.append("&");
			}
		}
		
		return strBuf.toString();
	}
	
	/* (non-Javadoc)
	 * @see com.freshdirect.webapp.util.YmalHelper#getLink()
	 */
	public String getLink() {
		ConfiguredProduct confProduct   = getConfiguredProduct();
		
		return "/product.jsp?catId="
	         + getCatId()
	         + "&productId="
	         + confProduct.getProduct()
	         + "&trk=conf";
	}
	
	/* (non-Javadoc)
	 * @see com.freshdirect.webapp.util.YmalHelper#getSalesDescription()
	 */
	public String getSalesDescription() throws FDResourceException {
		StringBuffer			salesDesc     = new StringBuffer();
		ConfiguredProduct       confProduct   = getConfiguredProduct();
		FDConfigurableI         configuration = confProduct.getConfiguration(); 
		FDProductSelectionI	    productSel;
				
		final String prdSalesDesc = super.getSalesDescription();
		salesDesc.append(prdSalesDesc);
		
		try {
			productSel = new FDProductSelection(confProduct.getFDProduct(),
                								confProduct.getProductRef(),
                								configuration);

			OrderLineUtil.describe(productSel);

			if (!"&nbsp;".equals(prdSalesDesc)) {
				// break line if product sales description is NOT empty (!= '&nbsp;')
				salesDesc.append("<br/>");
			}
			salesDesc.append(productSel.getConfigurationDesc());
		} catch (FDInvalidConfigurationException e) {
			// well, if no configuration, then just leaven de description as is
		}

		return salesDesc.toString();
	}
	
	/**
	 *  Tell if the product is sold by sales unit, or by quantity.
	 *  
	 *  @true if the product is sold by sales unit, false if by quantity.
	 */
	public boolean isSoldBySalesUnits() {
		ConfiguredProduct       confProduct   = getConfiguredProduct();		
		return confProduct.isSoldBySalesUnits();
	}
	
	/**
	 *  Tell if the product is sold by the pound.
	 *  
	 *  @return true if the product is sold by the pound.
	 */
	public boolean isSoldByLb() {
		ConfiguredProduct       confProduct   = getConfiguredProduct();
		FDConfigurableI         configuration = confProduct.getConfiguration(); 
		FDProductSelectionI	    productSel;

		productSel = new FDProductSelection(confProduct.getFDProduct(),
            								confProduct.getProduct().getProductRef(),
            								configuration);

		return productSel.isSoldByLb();
	}
	
	/**
	 *  Tell if the product related to this helper is a configured,
	 *  which is always true for conrigured products.
	 *  
	 *  @return true always
	 */
	public boolean isTransactional() {
		return true;
	}

}
