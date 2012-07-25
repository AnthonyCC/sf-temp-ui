package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.ejb.ErpOrderLineUtil;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.util.DateUtil;

public class ConfiguredProduct extends ProxyProduct implements YmalSetSource {

	private final List<ProductModel> alsoSoldAs = new ArrayList<ProductModel>();

	private final List<ProductModel> relatedProducts = new ArrayList<ProductModel>();
	
	// List of ProductModel or SkuModel type objects
	private final List<ContentNodeModel> recommendedAlternatives = new ArrayList<ContentNodeModel>();

	public ConfiguredProduct(ContentKey key) {
		super(key);
	}

	public DepartmentModel getDepartment() {
		ContentNodeModel start = this;
		while (!(start instanceof DepartmentModel) && (start != null)) {
			start = start.getParentNode();
		}
		return (DepartmentModel) start;
	}

	@Override
	public CategoryModel getCategory() {
		return this.getProduct().getCategory();
	}

	public boolean isRequired() {
		return getAttribute("REQUIRED", false);
	}

	public String getFullName() {
		return getAttribute("FULL_NAME", "");
	}
	
	public String getUnavailabilityMessage() {
		return "";
	}

	public String getGlanceName() {
		return getAttribute("GLANCE_NAME", "");
	}

	public String getNavName() {
		return getAttribute("NAV_NAME", "");
	}

	public String getBlurb() {
		return getAttribute("BLURB", "");
	}

	public String getAlsoSoldAsName() {
		return getAttribute("ALSO_SOLD_AS_NAME", "");
	}

	public String getSubtitle() {
		return getAttribute("SUBTITLE", "");
	}

	public String getInvisible() {
		return getAttribute("INVISIBLE", "");
	}

	public String getPackageDescription() {
		return getAttribute("PACKAGE_DESCRIPTION", "");
	}

	/**
	 * @return the SKU code associated with this configuration
	 */
	public String getSkuCode() {
		Object attr   = super.getCmsAttributeValue("SKU");
		if (attr == null) {
			return null;
		}
		ContentKey skuKey = (ContentKey) attr;
		return skuKey == null ? null : skuKey.getId();
	}

	/**
	 * @return the SKU associated with this configuration
	 */
	public SkuModel getSku() {
		ProductModel product = getProduct();
		if (product == null) {
			return null;
		}

		return product.getSku(getSkuCode());
	}
	
	// TODO check impact
	public SkuModel getDefaultSku() {
		return getSku();
	}

	public Html getProductDescription() {
	    return FDAttributeFactory.constructHtml(this, "PROD_DESCR");
	}

	public Html getProductDescriptionNote() {
            return FDAttributeFactory.constructHtml(this, "PROD_DESCRIPTION_NOTE");
	}

	public Html getProductQualityNote() {
            return FDAttributeFactory.constructHtml(this, "PRODUCT_QUALITY_NOTE");
	}

	public Image getCategoryImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE", IMAGE_BLANK);
	}

	public Image getConfirmImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_CONFIRM", IMAGE_BLANK);
	}

	public Image getDetailImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_DETAIL", IMAGE_BLANK);
	}

	public Image getFeatureImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_FEATURE", IMAGE_BLANK);
	}

	public Image getZoomImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_ZOOM", IMAGE_BLANK);
	}

	public Image getAlternateImage() {
            return FDAttributeFactory.constructImage(this, "ALTERNATE_IMAGE");
	}

	public Image getDescriptiveImage() {
	    return FDAttributeFactory.constructImage(this, "DESCRIPTIVE_IMAGE");
	}

	public List<ProductModel> getAlsoSoldAs() {
		ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAs, false);
		return Collections.unmodifiableList(alsoSoldAs);
	}

	@Override
	public List<ProductModel> getRelatedProducts() {
		ContentNodeModelUtil.refreshModels(this, "RELATED_PRODUCTS", relatedProducts, false);
		return Collections.unmodifiableList(relatedProducts);
	}
	
	/** Get the list of recommended alternatives.
	 * @return list of recommended alternatives
	 */
	public List<ContentNodeModel> getRecommendedAlternatives() {
		ContentNodeModelUtil.refreshModels(this, "RECOMMENDED_ALTERNATIVES", recommendedAlternatives, false);
		return Collections.unmodifiableList(recommendedAlternatives);
	}

	//
	// configured product-specific methods  
	//

	/**
	 * @return the ProductModel associated with this configuration
	 */
	public ProductModel getProduct() {
		try {
			ProductModel product = ContentFactory.getInstance().getProduct(getSkuCode());
			return product;
		} catch (FDSkuNotFoundException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	/**
	 * Returns the original product this configuration wraps.
	 * @return {@link #getProduct()}
	 */
	public ProductModel getSourceProduct() {
		return getProduct();
	}

	/**
	 * @return configuration to sell product in
	 */
	public FDConfigurableI getConfiguration() {
		return new FDConfiguration(getQuantity(), getSalesUnit(), getOptions());
	}
	
	public double getQuantity() {
		double q = super.getAttribute("QUANTITY", 0.0);
		return q;	//return q == 0.0 ? getQuantityMinimum() : q;
	}
	
	public String getSalesUnit() {
		Object suAttr = super.getCmsAttributeValue("SALES_UNIT");
		return suAttr == null ? "EA" : (String) suAttr;
	}
	
	public Map<String,String> getOptions() {
		Object optAttr = super.getCmsAttributeValue("OPTIONS");
		Map<String,String> options = optAttr == null ? Collections.EMPTY_MAP
				: ErpOrderLineUtil.convertStringToHashMap((String) optAttr);
		if (!options.isEmpty()) {
			// perform a bit of cleanup (remove extra options)
			FDProduct fdp = getFDProduct();
			if (fdp != null) {
				FDVariation[] fdv = fdp.getVariations();
				if (fdv.length == 0) {
					options.clear();
				} else {
					Set<String> s = new HashSet<String>(fdv.length);
					for (int i = 0; i < fdv.length; i++) {
						s.add(fdv[i].getName());
					}
					options.keySet().retainAll(s);
				}
			}
		}
		return options;
	}

	public FDProduct getFDProduct() {
		SkuModel sku = getSku();
		if (sku == null) {
			return null;
		}
		try {
			return sku == null ? null : sku.getProduct();
		} catch (FDSkuNotFoundException e) {
			return null;
		} catch (FDResourceException e) {
			throw new CmsRuntimeException(e);
		}
	}
	
        @Override
	public EnumProductLayout getProductLayout() {
		return EnumProductLayout.CONFIGURED_PRODUCT;
	}
	
	@Override
	public EnumProductLayout getProductLayout(EnumProductLayout defValue) {
	    return EnumProductLayout.CONFIGURED_PRODUCT;
	}

	public boolean isPreconfigured() {
		return true;
	}

	public boolean isUnavailable() {
		return getEarliestAvailability() == null;
	}

	/**
	 *  Tell if the product is available within a specific timeframe.
	 *  
	 *  @param days the number of days from today to check for availability,
	 *         1 means today, 2 means by tomorrow, etc.
	 *  @return true of the configured product, with its specific SKU is available
	 *          within the specified number of days.
	 */
	public boolean isAvailableWithin(int days) {
		Date earliest  = getEarliestAvailability();
		Date targetDay = DateUtil.addDays(new Date(), days - 1);
		
		if (earliest == null) {
			return false;
		}
		
		int diff = DateUtil.getDiffInDays(earliest, targetDay);
		
		return diff <= 0;
	}
	
	/**
	 *  Return the earliest availability for this ConfiguredProduct.
	 *  This is the earliest availability of the SKU this ConfiguredProduct
	 *  points to.
	 *  
	 *  @return the earliest availability of the configured product, or
	 *          null if not available at all.
	 *          If the product is not available within the horizon, this also
	 *          return null.
	 */
	public Date getEarliestAvailability() {
		Date         earliestAvailability = null;
		
		FDProduct fdp = null;
		try {
			fdp = getFDProduct();
		} catch (FDRuntimeException e) {
			fdp = null;
		}
		if (fdp == null) {
			return null;
		}
				
		SkuModel         sku = (SkuModel) ContentFactory.getInstance().getContentNode(getSkuCode());
		earliestAvailability = sku.getEarliestAvailability();
		if (earliestAvailability == null) {
			return null;
		}
		
		if (fdp.getVariations().length == 0) {
			return earliestAvailability;
		}
		Map optionsMap = getOptions();

		// check to see that each configured option is available
		for (Iterator optItr = optionsMap.entrySet().iterator(); optItr.hasNext();) {
			Map.Entry entry = (Map.Entry) optItr.next();
			String erpCharName = (String) entry.getKey();
			String optionValue = (String) entry.getValue();

			FDVariation var = fdp.getVariation(erpCharName);
			if (var == null) {
				// invalid configuration
				return null;
			}

			FDVariationOption varOpt = var.getVariationOption(optionValue);
			if (varOpt == null) {
				// invalid configuration
				return null;
			}

			// see if there's a sku code linked to this variation option
			String optSkuCode = varOpt.getSkuCode();
			if (optSkuCode == null || "".equals(optSkuCode)) {
				continue;
			}

			// see if corresponding sku is available also
			SkuModel optSku = (SkuModel) ContentFactory.getInstance().getContentNode(optSkuCode);
			if (optSku == null) {
				return null;
			}
			
			Date ea = optSku.getEarliestAvailability();
			if (ea == null) {
				return null;
			}
			if (ea.before(earliestAvailability)) {
				earliestAvailability = ea;
			}
		}

		return earliestAvailability;
	}

	/**
	 *  Tell if this product is auto-configurable.
	 *  Always false for configured products.
	 *  
	 *  @return false.
	 */
	public boolean isAutoconfigurable() {
		return false;
	}

	/**
	 *  Return the auto-configuration of the product.
	 *  Does not make sense for configured products.
	 *  
	 *  @return always null.
	 */
	public FDConfigurableI getAutoconfiguration() {
		return null;
	}

	public int getExpertWeight() {
		return getProduct().getExpertWeight();
	}
	
	@Override
	public PricingContext getPricingContext(){
		return new PricingContext(ZonePriceListing.MASTER_DEFAULT_ZONE);
	}

    @Override
    public ProductModel getPrimaryProductModel() {
        return this;
    }

    @Override
    public List<SkuModel> getPrimarySkus() {
        return getSkus();
    }

    @Override
    public boolean isInPrimaryHome() {
        return true;
    }

	@Override
	public YmalSetSource getParentYmalSetSource() {
		return getProduct().getParentYmalSetSource();
	}

	@Override
	public List<YmalSet> getYmalSets() {
		return getProduct().getYmalSets();
	}

	@Override
	public boolean hasActiveYmalSets() {
		return getProduct().hasActiveYmalSets();
	}

	@Override
	public boolean isDisabledRecommendations() {
		return getAttribute("DISABLED_RECOMMENDATION", false);
	}

	@Override
	public SkuModel getDefaultTemporaryUnavSku() {
		return getSku();
	}
}
