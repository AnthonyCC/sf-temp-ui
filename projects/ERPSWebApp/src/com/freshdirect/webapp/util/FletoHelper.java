package com.freshdirect.webapp.util;

import java.text.NumberFormat;

import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;


/**
 * This class is meant to be phased out ASAP (as this class' nominee).
 * 
 * 
 * This class only keeps the YMAL helpers' rendering pieces, plus wraps some
 * functions into the same interface.
 * 
 * @author istvan
 *
 */
public class FletoHelper implements YmalHelper {
	
	private ProductImpression productImpression;
	
	private ProductModel getProductModel() {
		return productImpression.getProductModel();

	}
	
	/**
	 * Wrapper for a product impression.
	 * @param productImpression
	 */
	public FletoHelper(ProductImpression productImpression) {
		this.productImpression = productImpression;
	}
	
	/**
	 * Get config.
	 * @return Transactional conf, or null.
	 */
	public FDConfigurableI getConfiguration() {
		if (isTransactional()) {
			return ((TransactionalProductImpression)productImpression).getConfiguration();
		} else {
			return null;
		}
	}
	
	/**
	 * Wrapper for instanceof
	 * @return
	 */
	public boolean isTransactional() {
		return productImpression.isTransactional();
	}
	
	/**
	 * Get selected sku for transactional, otherwise default sku.
	 */
	public SkuModel getSku() {
		return productImpression instanceof TransactionalProductImpression ? 
			((TransactionalProductImpression)productImpression).getSku() :
			productImpression.getProductModel().getDefaultSku();
	}
	
	// Wrapper
	public String getSkuCode() {
		return getSku().getSkuCode();
	}
	
	// Wrapper
	public boolean isSoldBySalesUnits() {
		return getProductModel().isSoldBySalesUnits();
	}
	
	// Wrapper
	public boolean isSoldByLb() {
		return getFDProduct().isSoldByLb();
	}
	
	// GABOR
	public String getLink() {
		return FDURLUtil.getProductURI(productImpression.getProductModel(), "conf");
	}
	
	
	// AKOS
	public Image getImage() {
        return getRealProduct().getCategoryImage();
	}
	
	// CHECK!!! CatId from the "real" product, NOT THE SAME for Proxies
	public String getCatId() {
		return getRealProduct().getParentNode().toString();
	}
	
	// Wrapper
	public ProductModel getRealProduct() {
		return productImpression.getProductModel().getSourceProduct();
	}
	
	
	// Wrapper
	public FDProduct getFDProduct() {
		return productImpression.getFDProduct();
	}
	
	// AKOS
	public String getFullName() {
		String fullName = getProductModel().getFullName();

        // modify name if brand available
        String brandName = getProductModel().getPrimaryBrandName();
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
	
	// AKOS
	private String getPlainSalesDesctiption() {
		String productDesc = null;
        String sizeDesc = null;
		try {
			sizeDesc = getRealProduct().getSizeDescription();
		} catch (FDResourceException e) {
			throw new FDRuntimeException(e);
		}

        if (sizeDesc != null) {
            productDesc = "&nbsp;(" + sizeDesc + ")&nbsp;";
        } else {
            productDesc = "&nbsp;";
        }

        return productDesc;
	}
	
	// AKOS
	private String getConfiguredSalesDescription() {
		StringBuffer salesDesc = new StringBuffer();
		ConfiguredProduct confProduct = (ConfiguredProduct)getRealProduct();
		FDConfigurableI configuration = confProduct.getConfiguration(); 
		FDProductSelectionI	productSel;
				
		final String prdSalesDesc = getPlainSalesDesctiption();
		salesDesc.append(prdSalesDesc);
		
		try {
			productSel = new FDProductSelection(confProduct.getFDProduct(),
                								confProduct.getProductRef(),
                								configuration);

			try {
				OrderLineUtil.describe(productSel);
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}

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
	
	// AKOS
	public String getSalesDescription() {
		return getRealProduct() instanceof ConfiguredProduct ?
			getConfiguredSalesDescription() :
			getPlainSalesDesctiption();
	}
	
	// AKOS
	public String getPrice() 
	throws FDResourceException, FDSkuNotFoundException {
		if (getProductModel().isUnavailable()) {
			return "Not Avail.";
		}
		
		String productPrice = null;
		SkuModel sku = getSku();
		
		if (sku != null) {
			FDProductInfo productInfo = FDCachedFactory.getProductInfo(sku.getSkuCode());
	        productPrice = "<b>"
	        	         + JspMethods.currencyFormatter.format(productInfo.getDefaultPrice())
	        	         + "/"
	        	         + productInfo.getDisplayableDefaultPriceUnit().toLowerCase()
	        	         + "</b>";
		}
		
		if (productPrice == null ) {
		    productPrice = "&nbsp;";
		}
		
		return productPrice;
	}
}
