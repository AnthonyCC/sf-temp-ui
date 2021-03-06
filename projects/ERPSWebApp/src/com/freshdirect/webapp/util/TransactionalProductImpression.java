package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.storeapi.content.ProductModel;

/**
 * A product with configuration and selected sku.
 * 
 * @author istvan
 *
 */
public class TransactionalProductImpression extends ProductSkuImpression {
	private FDConfigurableI configuration;
	
	/**
	 * Constructor.
	 * @param productModel
	 * @param configuration
	 * @param specific configuration
	 */
	public TransactionalProductImpression(
		ProductModel productModel, String selectedSkuCode, FDConfigurableI configuration) {
		super(productModel, selectedSkuCode);
		this.configuration = configuration;
	}
	
	/**
	 * Whether this impression is transactional.
	 * @return true
	 */
	public boolean isTransactional() {
		return true;
	}
	
	/** Get configuration.
	 * 
	 * @return configuration
	 */
	public FDConfigurableI getConfiguration() {
		return configuration;
	}
	

	/**
	 * Get if product is sold by sales units.
	 * @return whether product is soled by sales units
	 * @see #isSoldByQuantity()
	 */
	public boolean isSoldBySalesUnits() {
		return getProductModel().isSoldBySalesUnits();
	}
	
	/**
	 * Get the product's sales units.
	 * @return sales units
	 */
	public FDSalesUnit[] getSalesUnits() {
		return getFDProduct().getSalesUnits();
	}
	
	/** Get if product is sold by the pound.
	 * 
	 * @return if product is sold by the pound
	 * @see #isSoldByQuantity()
	 */
	public boolean isSoldByLB() {
		return getFDProduct().isSoldByLb();
	}
	
	/** Get if product is sold by quantity.
	 * 
	 * A product is sold by quantity if it is not sold
	 * by sales units and not sold by the pound.
	 * 
	 * Exactly one of {@link #isSoldByLB()}, {@link #isSoldBySalesUnits()}
	 * or {@link #isSoldByQuantity()} will return true.
	 * 
	 * @see #isSoldByLB()
	 * @see #isSoldBySalesUnits()
	 * @return if product is sold by quantity
	 */
	public boolean isSoldByQuantity() {
		return !isSoldByLB() && !isSoldBySalesUnits();
	}
	

	@Override
	public String getConfigurationDescription() {
		String desc = ConfigurationUtil.getConfigurationDescription(this);
		return desc != null ? desc : super.getConfigurationDescription();
	}

	

	@Deprecated
	public boolean validate() {
		boolean isValid = super.validate();
		
		// perform transactional specific validations
		if (isValid) {
			try {
				FDConfigurableI c = getConfiguration();
				isValid = !(
					productModel.getParentNode() == null ||
					c == null
				);


				if (isValid && productModel.isSoldBySalesUnits()) {
					FDSalesUnit     salesUnits[] = getFDProduct().getSalesUnits();
                    for (int ii = 0; (isValid && ii < salesUnits.length); ++ii) {
                        FDSalesUnit salesUnit      = salesUnits[ii];
                    	isValid = (salesUnit != null && salesUnit.getName() != null && salesUnit.getDescription() != null);
                    }
				}
			} catch (Exception exc) {
				isValid = false;
			}
		}
		return isValid;
	}
}
