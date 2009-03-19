package com.freshdirect.webapp.util;

import java.text.NumberFormat;

import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

public interface YmalHelper {

	/**
	 *  Tell if the product related to this helper is a configured.
	 *  That is, it is either a configured product, configured product group,
	 *  or an auto-configured product.
	 *  
	 *  @return true if the product related to this helper is configured,
	 *          and can be displayed in a transactional manner.
	 *          false otherwise.
	 */
	public boolean isTransactional();

	/**
	 *  Get the real product (i.e. not configured product,
	 *  configured product group, etc.) related to this helper.
	 *  
	 *  @return the real product related to this helper.
	 */
	public ProductModel getRealProduct();

	/**
	 *  Return the SKU model of the product related to this helper.
	 *  This will be the only SKU model if the product only has one
	 *  (like configured products), otherwise the first SKU model
	 *  for the product.
	 *  
	 *  @return the SKU model for the product related to this helper.
	 */
	public SkuModel getSku();

	/**
	 *  Return the SKU code of the product related to this helper.
	 *  This will be the only SKU code if the product only has one
	 *  (like configured products), otherwise the first SKU code
	 *  for the product.
	 *  
	 *  @return the SKU code for the product related to this helper.
	 */
	public String getSkuCode();

	/**
	 *  Return the configuration of the product related to this helper,
	 *  if applicable.
	 *  
	 *  @return the configuration of the product if this is a configured
	 *          or auto-configurable product. null otherwise.
	 */
	public FDConfigurableI getConfiguration();
	
	/**
	 *  Get the related FDProduct object.
	 *  
	 *  @return the related FDProduct object.
	 *  @throws FDSkuNotFoundException if the SKU for the product can not be found 
	 *  @throws FDResourceException if some resource can not be found
	 */
	public FDProduct getFDProduct() throws FDResourceException, FDSkuNotFoundException;

	/**
	 *  Return the link that leads to the product page itself.
	 *  
	 *  @return a link that leads to a page displaying the product in detail.
	 */
	public String getLink();

	/**
	 *  Return the full name of the product, including branding, etc.
	 *  
	 *  @return the full name of the product, suitable for display.
	 */
	public String getFullName();

	/**
	 *  Return the category id for the product.
	 *  
	 *  @return the category id for the product.
	 */
	public String getCatId();

	/**
	 *  Get the image for the product's category.
	 *  
	 *  @return the image for the product's category.
	 */
	public Image getImage();

	/**
	 *  Get the sales description.
	 *  
	 *  @return the sales description of the product.
	 *  @throws FDResourceException if some resources not found.
	 */
	public String getSalesDescription() throws FDResourceException;

	/**
	 *  Get the price of the product, as a string.
	 *  
	 *  @return the price of the product, suitable for display.
	 *  @throws FDResourceException if a resource could not be found
	 *  @throws FDSkuNotFoundException if the product's SKU could not be found
	 */
	public String getPrice()
										throws FDResourceException,
                                               FDSkuNotFoundException;

	/**
	 *  Tell if the product is sold by sales unit, or by quantity.
	 *  
	 *  @true if the product is sold by sales unit, false if by quantity.
	 */
	public boolean isSoldBySalesUnits();

	/**
	 *  Tell if the product is sold by the pound.
	 *  
	 *  @return true if the product is sold by the pound.
	 */
	public boolean isSoldByLb();

}