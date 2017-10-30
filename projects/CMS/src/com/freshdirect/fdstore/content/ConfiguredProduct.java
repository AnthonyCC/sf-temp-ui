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
import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ejb.ErpOrderLineUtil;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.util.DateUtil;

public class ConfiguredProduct extends ProxyProduct implements YmalSetSource {

    private static final long serialVersionUID = 7188118020157672951L;

    private final List<ProductModel> alsoSoldAs = new ArrayList<ProductModel>();

	private final List<ProductModel> relatedProducts = new ArrayList<ProductModel>();
	
	// List of ProductModel or SkuModel type objects
	private final List<ContentNodeModel> recommendedAlternatives = new ArrayList<ContentNodeModel>();

	/**
	 *  [APPDEV-3179] list of merchant selected products for upsell recommender
	 */
	private List<ProductModel> upSellProducts = new ArrayList<ProductModel>();

	/**
	 *  [APPDEV-3179] list of merchant selected products for cross-sell recommender
	 */
	private List<ProductModel> crossSellProducts = new ArrayList<ProductModel>();


	public ConfiguredProduct(ContentKey key) {
		super(key);
	}

	@Override
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

	@Override
    public String getFullName() {
		return getAttribute("FULL_NAME", "");
	}
	
	public String getUnavailabilityMessage() {
		return "";
	}

	@Override
    public String getGlanceName() {
		return getAttribute("GLANCE_NAME", "");
	}

	@Override
    public String getNavName() {
		return getAttribute("NAV_NAME", "");
	}

	@Override
    public String getBlurb() {
		return getAttribute("BLURB", "");
	}

	@Override
    public String getAlsoSoldAsName() {
		return getAttribute("ALSO_SOLD_AS_NAME", "");
	}

	@Override
    public String getSubtitle() {
		return getAttribute("SUBTITLE", "");
	}

	public String getInvisible() {
		return getAttribute("INVISIBLE", "");
	}

	@Override
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
	@Override
    public SkuModel getDefaultSku() {
		return getSku();
	}

	@Override
    public Html getProductDescription() {
	    return FDAttributeFactory.constructHtml(this, "PROD_DESCR");
	}

	@Override
    public Html getProductDescriptionNote() {
            return FDAttributeFactory.constructHtml(this, "PROD_DESCRIPTION_NOTE");
	}

	@Override
    public Html getProductQualityNote() {
            return FDAttributeFactory.constructHtml(this, "PRODUCT_QUALITY_NOTE");
	}

	@Override
    public Image getCategoryImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE", Image.BLANK_IMAGE);
	}

	@Override
    public Image getConfirmImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_CONFIRM", Image.BLANK_IMAGE);
	}

	@Override
    public Image getDetailImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_DETAIL", Image.BLANK_IMAGE);
	}

	@Override
    public Image getFeatureImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_FEATURE", Image.BLANK_IMAGE);
	}

	@Override
    public Image getZoomImage() {
            return FDAttributeFactory.constructImage(this, "PROD_IMAGE_ZOOM", Image.BLANK_IMAGE);
	}

	@Override
    public Image getAlternateImage() {
            return FDAttributeFactory.constructImage(this, "ALTERNATE_IMAGE");
	}

	@Override
    public Image getDescriptiveImage() {
	    return FDAttributeFactory.constructImage(this, "DESCRIPTIVE_IMAGE");
	}

	@Override
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
	@Override
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
	@Override
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
	@Override
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

	@Override
    public boolean isPreconfigured() {
		return true;
	}

	@Override
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
	@Override
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
	@Override
    public Date getEarliestAvailability() {
		Date         earliestAvailability = null;
		
		FDProduct fdp = null;
		try {
			fdp = getFDProduct();
		} catch (FDRuntimeException e) {
			fdp = null;
		} catch (CmsRuntimeException e) {
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
	@Override
    public boolean isAutoconfigurable() {
		return false;
	}

	/**
	 *  Return the auto-configuration of the product.
	 *  Does not make sense for configured products.
	 *  
	 *  @return always null.
	 */
	@Override
    public FDConfigurableI getAutoconfiguration() {
		return null;
	}

	@Override
    public int getExpertWeight() {
		return getProduct().getExpertWeight();
	}
	
	@Override
	public UserContext getUserContext(){
		//return new PricingContext(ZonePriceListing.DEFAULT_ZONE_INFO);
		return getProduct().getUserContext();
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

	/**
	 * @see {@link ProductModel#getUpSellProducts()}
	 */
	@Override
	public List<ProductModel> getUpSellProducts() {
		ContentNodeModelUtil.refreshModels(this, "PDP_UPSELL", upSellProducts, false);
		return new ArrayList<ProductModel>(upSellProducts);
	}

	/**
	 * @see {@link ProductModel#getCrossSellProducts()}
	 */
	@Override
	public List<ProductModel> getCrossSellProducts() {
		ContentNodeModelUtil.refreshModels(this, "PDP_XSELL", crossSellProducts, false);
		return new ArrayList<ProductModel>(crossSellProducts);
	}
	
	@Override
	public String getBrowseRecommenderType(){
		return getProduct().getBrowseRecommenderType();
	}

	@Override
	public int getHeatRating() {
		return getProduct().getHeatRating();
	}

	/**
	 * @see {@link ProductModel#getJumboImage()}
	 */
	@Override
	public Image getJumboImage() {
        return FDAttributeFactory.constructImage(this, "PROD_IMAGE_JUMBO", Image.BLANK_IMAGE);
	}

	/**
	 * @see {@link ProductModel#getItemImage()}
	 */
	@Override
	public Image getItemImage() {
        return FDAttributeFactory.constructImage(this, "PROD_IMAGE_ITEM", Image.BLANK_IMAGE);
	}

	/**
	 * @see {@link ProductModel#getExtraImage()}
	 */
	@Override
	public Image getExtraImage() {
        return FDAttributeFactory.constructImage(this, "PROD_IMAGE_EXTRA", Image.BLANK_IMAGE);
	}
	
	@Override
	public boolean isDisableAtpFailureRecommendation(){
		return getProduct().isDisableAtpFailureRecommendation();
	}

	
	@Override
	public EnumProductLayout getSpecialLayout() {
		return getProduct().getSpecialLayout();
	}

	@Override
	public List<ProductModel> getCompleteTheMeal() {
		return getProduct().getCompleteTheMeal();
	}

    @Override
    public List<ProductModel> getIncludeProducts() {
        return getProduct().getIncludeProducts();
    }

	@Override
	public String getPageTitle() {
		return getProduct().getPageTitle();
	}

	@Override
	public String getSEOMetaDescription() {
		return getProduct().getSEOMetaDescription();
	}

	@Override
	public String getFdxSEOMetaDescription() {
	    return getProduct().getFdxSEOMetaDescription();
	}

	@Override
	public String getPairItHeading() {
		return getProduct().getPairItHeading();
	}

	@Override
	public String getPairItText() {
		return getProduct().getPairItText();
	}
	
	@Override
	public int getTimeToComplete() {
	    return getProduct().getTimeToComplete();
	}

	@Override
	public double getAvailabileQtyForDate(Date targetDate) {
		 return this.getProduct().getAvailabileQtyForDate(targetDate);
		
	}
}
