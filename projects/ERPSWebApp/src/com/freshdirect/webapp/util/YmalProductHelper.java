package com.freshdirect.webapp.util;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;

/**
 *  A class helping the display of products as YMALs.
 */
public class YmalProductHelper implements YmalHelper {
	/**
	 *  The product to display.
	 */
	private ProductModel product;
	
	/**
	 *  Flag to indicate if auto-configurable products whould be presented
	 *  in a transactional manner.
	 */
	private boolean transactional;
	
	/**
	 *  Constructor.
	 *  
	 *  @param product the product that needs to be displayed as YMAL.
	 *  @param tranasctional if auto-configurable products should be presented
	 *         in a transactional manner.
	 */
	YmalProductHelper(ProductModel product,
			          boolean      transactional) {
		this.product       = product;
		this.transactional = transactional;
	}

	/**
	 *  Get the product this helper is wrapped around.
	 *  Used in subclasses mostly.
	 *  
	 *  @return the product this helper is wrapped around.
	 */
	public ProductModel getProduct() {
		return product;
	}
	
	/**
	 *  Get the real product (i.e. not configured product,
	 *  configured product group, etc.) related to this helper.
	 *  
	 *  @return the real product related to this helper.
	 */
	public ProductModel getRealProduct() {
		return getProduct();
	}

	/**
	 *  Return the SKU code of the product related to this helper.
	 *  This will be the only SKU code if the product only has one
	 *  (like configured products), or the first SKU code.
	 *  
	 *  @return the SKU code for the product related to this helper.
	 */
	public String getSkuCode() {
		return (String) product.getSkuCodes().get(0);
	}

	/**
	 *  Return the configuration of the product related to this helper,
	 *  if applicable.
	 *  
	 *  @return the configuration of the product if this is a configured
	 *          or auto-configurable product. null otherwise.
	 */
	public FDConfigurableI getConfiguration() {
		if (isTransactional()) {
			return product.getAutoconfiguration();
		}
		
		return null;
	}

	/**
	 *  Return the category id for the product.
	 *  
	 *  @return the category id for the product.
	 */
	public String getCatId() {
		return product.getParentNode().toString();
	}
	
	/**
	 *  Return the link that leads to the product page itself.
	 *  
	 *  @return a link that leads to a page displaying the product in detail.
	 */
	public String getLink() {
		return FDURLUtil.getProductURI(this.product, "conf");
		/** return "/product.jsp?catId="
		     + getCatId()
		     + "&productId="
		     + product
		     + "&trk=conf";**/
	}
	
	/**
	 *  Return the full name of the product, including branding, etc.
	 *  
	 *  @return the full name of the product, suitable for display.
	 */
	public String getFullName() {
		String fullName = product.getFullName();

        // modify name if brand available
        String brandName = product.getPrimaryBrandName();
        if (brandName != null
         && brandName.length() > 0
         && (fullName.length() >= brandName.length())
         && fullName.substring(0, brandName.length()).equalsIgnoreCase(brandName)) {

            String shortenedProductName = fullName.substring(brandName.length()).trim();
            fullName = "<b>" + brandName + "</b> " + shortenedProductName;
        } else {
            fullName = "<b>" + fullName + "</b>";
        }

        return fullName;
	}
	
	/* (non-Javadoc)
	 * @see com.freshdirect.webapp.util.YmalHelper#getImage()
	 */
	public Image getImage() {
		// Not needed to look for alternate image as per creative.
		//Image image = product.getAlternateImage();
		
        Image image = product.getCategoryImage();
       
        return image;
	}

	/**
	 *  Get the SKU of the product.
	 *  
	 *  @return the SKU of the product.
	 */
	public SkuModel getSku() {
		List       skus         = product.getSkus();
		Comparator priceComp    = new ProductModel.PriceComparator();
		
		for (ListIterator li = skus.listIterator(); li.hasNext(); ) {
		    SkuModel sku = (SkuModel) li.next();
		    if (sku.isUnavailable()) {
		    	li.remove();
		    }
		}
		
		int      skuSize   = skus.size();
		SkuModel sku       = null;
		
		if (skuSize == 1) {
		    sku = (SkuModel) skus.get(0);  // we only need one sku
		} else if (skus.size() > 1) {
		    sku = (SkuModel) Collections.min(skus, priceComp);
		}

		return sku;
	}
	
	/**
	 *  Get the price of the product, as a string.
	 *  
	 *  @param currencyFormatter a formatter suitable for formatting
	 *         monetary values.
	 *  @return the price of the product, suitable for display.
	 *  @throws FDResourceException if a resource could not be found
	 *  @throws FDSkuNotFoundException if the product's SKU could not be found
	 */
	public String getPrice(NumberFormat currencyFormatter)
											throws FDResourceException,
	                                               FDSkuNotFoundException {
		if (product.isUnavailable()) {
			return "Not Avail.";
		}
		
		String     productPrice = null;
		SkuModel   sku          = getSku();
		
		if (sku != null) {
			FDProductInfo productInfo = FDCachedFactory.getProductInfo(sku.getSkuCode());
	        productPrice = "<b>"
	        	         + currencyFormatter.format(productInfo.getDefaultPrice())
	        	         + "/"
	        	         + productInfo.getDisplayableDefaultPriceUnit().toLowerCase()
	        	         + "</b>";
		}
		
		if (productPrice == null ) {
		    productPrice = "&nbsp;";
		}
		
		return productPrice;
	}
	
	/**
	 *  Get the related FDProduct object.
	 *  
	 *  @return the related FDProduct object.
	 *  @throws FDSkuNotFoundException if the SKU for the product can not be found 
	 *  @throws FDResourceException if some resource can not be found
	 */
	public FDProduct getFDProduct() throws FDResourceException, FDSkuNotFoundException {
		SkuModel      sku         = getSku();
		FDProductInfo productInfo = FDCachedFactory.getProductInfo(sku.getSkuCode());

		return FDCachedFactory.getProduct(productInfo);
	}
	
	/**
	 *  Get the sales description.
	 *  
	 *  @return the sales description of the product.
	 *  @throws FDResourceException if some resources not found.
	 */
	public String getSalesDescription() throws FDResourceException {
        String productDesc = null;
        String sizeDesc    = product.getSizeDescription();

        if (sizeDesc != null) {
            productDesc = "&nbsp;(" + sizeDesc + ")&nbsp;";
        } else {
            productDesc = "&nbsp;";
        }

        return productDesc;
	}
	
	/**
	 *  Tell if the product is sold by sales unit, or by quantity.
	 *  
	 *  @true if the product is sold by sales unit, false if by quantity.
	 */
	public boolean isSoldBySalesUnits() {
		return getProduct().isSoldBySalesUnits();
	}

	/**
	 *  Tell if the product is sold by the pound.
	 *  
	 *  @return true if the product is sold by the pound.
	 */
	public boolean isSoldByLb() {
		return false;
	}

	/**
	 *  Tell if the product related to this helper is a configured.
	 *  That is, the product is auto-configurable, and should be presented
	 *  in a transactional manner.
	 *  
	 *  @return true if the product related to this helper is configured,
	 *          and can be displayed in a transactional manner.
	 *          false otherwise.
	 */
	public boolean isTransactional() {
		return transactional && product.isAutoconfigurable();
	}

}
