package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;


public class ProductModelImpl extends AbstractProductModelImpl {
	
	private static final long serialVersionUID = 2103318183933323914L;

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getInstance( ProductModelImpl.class ); 
	
	private static ThreadLocal<Map<String, YmalSet>> activeYmalSets = new ThreadLocal<Map<String, YmalSet>>() {
		protected Map<String, YmalSet> initialValue() {
			return new HashMap<String, YmalSet>();
		}
	};
	
	private static YmalSet getCurrentActiveYmalSet(String productId) {
		return activeYmalSets.get().get(productId);
	}
	
	private static void setCurrentActiveYmalSet(String productId, YmalSet set) {
		activeYmalSets.get().put(productId, set);
	}

	private static void resetActiveYmalSets() {
		activeYmalSets.get().clear();
	}

	
	private List<SkuModel> skuModels = new ArrayList<SkuModel>();
	
	private final List<BrandModel> brandModels = new ArrayList<BrandModel>();
	
	private final List weRecommendText = new ArrayList();

	private final List weRecommendImage = new ArrayList();

	private final List<Recipe> relatedRecipes = new ArrayList<Recipe>();

	private final List<ProductModel> productBundle = new ArrayList<ProductModel>();

	private final List<CategoryModel> howtocookitFolders = new ArrayList<CategoryModel>();

	private final List<DomainValue> rating = new ArrayList<DomainValue>();

	private final List<Domain> usageList = new ArrayList<Domain>();

	private final List<Domain> variationMatrix = new ArrayList<Domain>();

	private final List<Domain> variationOptions = new ArrayList<Domain>();
	
	private final List<ComponentGroupModel> componentGroups = new ArrayList<ComponentGroupModel>();
	
	private final List<ContentNodeModel> recommendedAlternatives = new ArrayList<ContentNodeModel>();
	
	// new wine store related changes
	
	private final List<DomainValue> wineNewTypes = new ArrayList<DomainValue>();
	
	private final List<DomainValue> wineVintages = new ArrayList<DomainValue>();
	
	private final List<DomainValue> wineRegions = new ArrayList<DomainValue>();
	
	private final List<DomainValue> wineRatings1 = new ArrayList<DomainValue>();
	
	private final List<DomainValue> wineRatings2 = new ArrayList<DomainValue>();
	
	private final List<DomainValue> wineRatings3 = new ArrayList<DomainValue>();
	
	private final List<DomainValue> wineVarietals = new ArrayList<DomainValue>();
	
	/**
	 *  The list of YMAL products related to this product.
	 */
	private final List<ContentNodeModel> ymals = new ArrayList<ContentNodeModel>();
	
	/**
	 * Gift Card changes
	 * */
	private final List giftcardTypes=new ArrayList();
	
	public ProductModelImpl(ContentKey cKey) {
		super(cKey);
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
		return (CategoryModel) this.getParentNode();
	}


	public String getAka() {
		return getAttribute("AKA", "");
	}

	public float getQuantityMinimum() {
		return (float) getAttribute("QUANTITY_MINIMUM", 1.0);
	}

	public float getQuantityMaximum() {
		return (float) getAttribute("QUANTITY_MAXIMUM", 1.0);
	}

	public float getQuantityIncrement() {
		return (float) getAttribute("QUANTITY_INCREMENT", 1.0);
	}

	public boolean enforceQuantityMax() {
		return getAttribute("INCREMENT_MAX_ENFORCE", false);
	}

	public boolean isInvisible() {
		return getAttribute("INVISIBLE", false);
	}

	/** Indexed getter for property skus.
	 * @param index Index of the property.
	 * @return Value of the property at <CODE>index</CODE>.
	 */
	public SkuModel getSku(int idx) {
		return getSkus().get(idx);
	}

	public SkuModel getSku(String skuCode) {
		List<SkuModel> skus = getSkus();
		for ( SkuModel s : skus ) {
			if (s.getSkuCode().equalsIgnoreCase(skuCode)) {
				return s;
			}
		}
		return null;
	}

	/** Getter for property skus.
	 * @return Value of property skus.
	 */
	public List<SkuModel> getSkus() {
		ContentNodeModelUtil.refreshModels(this, "skus", skuModels, true);
		return new ArrayList<SkuModel>(skuModels);
	}

	public List<String> getSkuCodes() {
		List<SkuModel> skus = getPrimarySkus();
		List<String> skuCodeList = new ArrayList<String>(skus.size());
		for (SkuModel i : skus) {
			skuCodeList.add(i.getSkuCode());
		}
		return skuCodeList;
	}

	public Object clone() {
		ProductModelImpl pm = (ProductModelImpl) super.clone();

		// must cheat here by directly accessing protected/private variables.
		List<SkuModel> skus = pm.skuModels;
		List<SkuModel> newList = new ArrayList<SkuModel>();
		//clone the skus also, since we try to get parents from  the sku level
		for ( SkuModel next : skus ) {
			SkuModel sm = (SkuModel)next.clone();
			sm.setParentNode(pm);
			newList.add(sm);
		}
		pm.skuModels = newList;
		return pm;
	}
	
	/**
	 * Returns itself.
	 * @return itself
	 */
	public ProductModel getSourceProduct() {
		return this;
	}

	/**
	 *  Tell if this product can be autoconfigured.
	 *  This is equivalent to saying it has only one SKU,
	 *  and only one possible configuration,
	 *  with regards to variation options and sales units.
	 *  [segabor]: condition extended, see APPREQ-26
	 *  
	 *  @return true if the product can be auto-configured,
	 *          false otherwise.
	 */
	public boolean isAutoconfigurable() {
		return getAutoconfiguration() != null;
	}

	/**
	 *  Return the auto-configuration for the product, if applicable.
	 *  
	 *  @return the configuration describing the auto-configuration of the
	 *          product, or null if the product can not be auto-configured.
	 *   this.getProductPricingAdapter(PricingContext pricingContext).getDefaultSku()        
	 */
	public FDConfigurableI getAutoconfiguration() {
		FDConfigurableI ret = null;
		SkuModel sku = getDefaultSku();
		if (sku != null) {
			try {
				boolean soldBySalesUnits = isSoldBySalesUnits();
				FDProduct fdProduct = sku.getProduct();
                                if (fdProduct.isAutoconfigurable(soldBySalesUnits)) {
					ret = fdProduct.getAutoconfiguration(soldBySalesUnits, getQuantityMinimum());
				}
			} catch(Exception exc) {}
		}
		return ret;
	}
	
	/** Getter for property brands.
	 * @return List of BrandModels that are referenced by the  property brands.
	 */
	public List<BrandModel> getBrands() {
		ContentNodeModelUtil.refreshModels(this, "brands", brandModels, false);

		List<BrandModel> newList = new ArrayList<BrandModel>();
		for (int i = 0; i < brandModels.size(); i++) {
			BrandModel b = brandModels.get(i);
			String str = b.getFullName();
			if ((str != null) && !str.equals("")) {
				newList.add(b);
			}
		}
		return newList;
	}

	public boolean isPerishable() {
		return getAttribute("PERISHABLE", false);
	}

	public boolean isFrozen() {
		return getAttribute("FROZEN", false);
	}

	public boolean isGrocery() {
		return getAttribute("GROCERY", false);
	}
	
	public boolean isSoldBySalesUnits() {
		return "SALES_UNIT".equals(getSellBySalesunit());
	}

	public boolean isQualifiedForPromotions() throws FDResourceException {
		List<SkuModel> skus = getPrimarySkus();
		for (Iterator<SkuModel> i = skus.iterator(); i.hasNext();) {
			try {
				FDProduct fdp = i.next().getProduct();
				if (fdp.isQualifiedForPromotions()) {
					return true;
				}
			} catch (FDSkuNotFoundException e) {
				// ignorance is bliss
			}
		}

		return false;
	}

	/**
	 * a product is discontinued only if all of its skus are discontinued
	 * @return  */
	public boolean isDiscontinued() {
		List<SkuModel> skus = getPrimarySkus();
		for ( SkuModel sku  : skus ) {
			if (!sku.isDiscontinued())
				return false;
		}
		return true;
	}

	/**
	 * a product is out of season only if all of its skus are out of season
	 * @return  */
	public boolean isOutOfSeason() {
		List<SkuModel> skus = getPrimarySkus();
		for ( SkuModel sku  : skus ) {
			if (!sku.isOutOfSeason())
				return false;
		}
		return true;
	}

	/**
	 * a product is unavailable only if all of its skus are unavailable
	 * @return  */
	public boolean isUnavailable() {
		List<SkuModel> skus = getPrimarySkus();
		for ( SkuModel sku  : skus ) {
			if (!sku.isUnavailable() && isCharacteristicsComponentsAvailable(null))
				return false;
		}

		return true;
	}

	/**
	 * a product is temp. unavailable only if all of its skus are temp. unavailable
	 * @return  */
	public boolean isTempUnavailable() {
		List<SkuModel> skus = getPrimarySkus();
		for ( SkuModel sku  : skus ) {
			if (!sku.isTempUnavailable())
				return false;
		}
		return true;
	}

	/**
	 * @return true if any of its skus is a platter
	 */
	public boolean isPlatter() {
		List<SkuModel> skus = getPrimarySkus();
		for ( SkuModel sku  : skus ) {
			try {
				FDProduct product = sku.getProduct();
				if (product.isPlatter())
					return true;
			} catch (FDSkuNotFoundException ignore) {
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
		return false;
	}

	public DayOfWeekSet getBlockedDays() {
		List<SkuModel> skus = getPrimarySkus();
		DayOfWeekSet allBlockedDays = DayOfWeekSet.EMPTY;
		for ( SkuModel sku  : skus ) {
			try {
				FDProduct product = sku.getProduct();
				allBlockedDays = allBlockedDays.union(product.getMaterial().getBlockedDays());
			} catch (FDSkuNotFoundException ignore) {
			} catch (FDResourceException ex) {
				throw new FDRuntimeException(ex);
			}
		}
		return allBlockedDays;
	}

	/**
	 * a product is available with X days if any of its skus are available within X days
	 * @param days
	 * @return  */
	public boolean isAvailableWithin(int days) {
		List<SkuModel> skus = getPrimarySkus();
		for ( SkuModel sku  : skus ) {
			if (sku.isAvailableWithin(days))
				return true;
		}
		return false;
	}

	/**
	 * a product's earliest availability is the earliest of it's sku's earliest availability
	 * @return  */
	public Date getEarliestAvailability() {
		List<SkuModel> skus = getPrimarySkus();
		Date ea = null;
		for ( SkuModel sku  : skus ) {
			Date     earliestAvailability = sku.getEarliestAvailability();
			if ((ea == null) && (earliestAvailability != null)) {
				ea = sku.getEarliestAvailability();
			} else if ((ea != null) && (earliestAvailability != null)
				    && (earliestAvailability.before(ea))) {
				ea = sku.getEarliestAvailability();
			}
		}
		return ea;
	}

	/**
	 * 
	 *  Returns the preferred SKU for a product if defined.
	 *  Otherwise returns the minimum price SKU among the available SKUs for
	 *  the product. If the product only has one available SKU, that will be
	 *  returned.
	 *  
	 *  @return the preferred SKU for the product, or the minimally priced
	 *          SKU that is available. if no SKUs are available, returns null.
	 */
	public SkuModel getDefaultSku() {
	    return getDefaultSku(getPricingContext());
	}

	/**
	 * Return a plain SkuModel, not just a wrapper around a SkuModel.
	 */
	public SkuModel getDefaultSku(PricingContext context) {
	    if (context == null) {
	        context = PricingContext.DEFAULT;
	    }
        List<SkuModel> skus = this.getSkus();

        ContentKey preferredSku = (ContentKey) getCmsAttributeValue("PREFERRED_SKU");

        for (ListIterator<SkuModel> li = skus.listIterator(); li.hasNext();) {
            SkuModel sku = li.next();
            if (sku.isUnavailable()) {
                li.remove();
            } else if (sku.getContentKey().equals(preferredSku)) {
                return sku;
            }
        }
        if (skus.size() == 0)
            return null;
        
        return Collections.min(skus, new ZonePriceComparator(context.getZoneId()));
	}
	
	/**
	 * @param type a multivalued ErpNutritionInfoType
	 * @return common Set of nutrition information of all available SKUs 
	 */
	public Set getCommonNutritionInfo(ErpNutritionInfoType type) throws FDResourceException {
		List<SkuModel> skus = getPrimarySkus();
		if (!type.isMultiValued()) {
			throw new IllegalArgumentException(type + " is not multivalued");
		}
		Set common = null;
		boolean first = true;
		for ( SkuModel sku : skus ) {
			if (sku.isUnavailable()) {
				continue;
			}

			try {
				List values = sku.getProduct().getNutritionInfoList(type);
				if (first) {
					common = new HashSet();
				}
				if (values != null) {
					if (first) {
						common.addAll(values);
					} else {
						common.retainAll(values);
					}
				}
				first = false;
			} catch (FDSkuNotFoundException ignore) {
			}
		}

		return common == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(common);
	}

	/**
	 * @return the brand that begins with the full name, empty string otherwise
	 */
	public String getPrimaryBrandName() {
		String fullName = super.getFullName();
		return fullName != null ? getPrimaryBrandName(fullName) : "";
	}



	protected static final String DOMAIN_ORGANIC_NAME = "source_Organic";
	protected static final String DOMAIN_LOCAL_NAME = "source_Local";

	
	@Override
	public String getFullName() {
		String theFullName = null;
		final String origFullName = super.getFullName() != null ? super.getFullName() : "";

		
		// does it have 'source' domain which can have either local or organic values?
		boolean hasSourceDomain = false;
		for (Domain d : getVariationMatrix()) {
			if ("source".equals(d.getContentName())) {
				hasSourceDomain = true;
				break;
			}
		}


		if (hasSourceDomain) {
			int availableSkus = 0;
			SkuModel firstAvailableSku = null;
			for (SkuModel sku : getSkus()) {
				if (!sku.isUnavailable()) {
					availableSkus++;
					if (firstAvailableSku == null)
						firstAvailableSku = sku;
					else
						break;
				}
			}


			// only one available sku required
			if (availableSkus == 1) {
				final String[] wordList = origFullName.split("\\s+");
				boolean hasLocalInName = false;
				boolean hasOrgInName = false;
				for (String w : wordList) {
					if ("local".equalsIgnoreCase(w)) {
						hasLocalInName = true;
					} else if ("organic".equalsIgnoreCase(w)) {
						hasOrgInName = true;
					}
				}


				// local ...
				if (!hasLocalInName) {
					for (DomainValue dv : firstAvailableSku.getVariationMatrix()) {
						if (DOMAIN_LOCAL_NAME.equals(dv.getContentName())) {
							theFullName = origFullName + ", Local";
							break;
						}
					}
				}

				// organic ...
				if (theFullName == null && !hasOrgInName) {
					for (DomainValue dv : firstAvailableSku.getVariationMatrix()) {
						if (DOMAIN_ORGANIC_NAME.equals(dv.getContentName())) {
							theFullName = origFullName + ", Organic";
							break;
						}
					}
				}
			}
		}
		
		// fall back to original name
		if (theFullName == null) {
			theFullName = origFullName;
		}
		
		return theFullName;
	}

	/**
	 * @return the brand that begins with the specified name, empty string otherwise
	 */
	public String getPrimaryBrandName(String productName) {
		// get the first brand name, if any.
		List<BrandModel> myBrands = this.getBrands();

		String prodNameLower = productName.toLowerCase();

		if (myBrands != null && myBrands.size() > 0) {

			// find the brand that begins with the full name
			for (int bx = 0; bx < myBrands.size(); bx++) {
				BrandModel bm = myBrands.get(bx);
				if (bm.getFullName() != null) {
					if (prodNameLower.startsWith(bm.getFullName().toLowerCase())) {
						return bm.getFullName();
					}
				}
			}

		}
		return "";
	}
	
	/**
	 * @return list of brands that can be displayed on the product page
	 *  (did not want to use the word primaryBrands since the notion of primary brand is a brand name 
	 * that is part of the full name of the product )
	 */
	public List<BrandModel> getDisplayableBrands() {
		return this.getDisplayableBrands(1);
	}
	/**
	 * @return list of brands that can be displayed on the product page
	 */
	public List<BrandModel> getDisplayableBrands(int numberOfBrands) {
		List<BrandModel> prodBrands = this.getBrands();
		List<BrandModel> displayableBrands = new ArrayList<BrandModel>();
		
		if (prodBrands.size() > 0) {
			BrandModel b = prodBrands.get(0);
			Image brandLogoSmall = b.getLogoSmall();
			if (brandLogoSmall!=null) {
				displayableBrands.add(prodBrands.get(0));
				numberOfBrands--;
			}
		}
		
		// now get the remaining brands from the sku
		List<SkuModel> skus = getPrimarySkus();
		for (Iterator<SkuModel> skuItr = skus.iterator();skuItr.hasNext() && numberOfBrands >0;) {
			SkuModel sku = skuItr.next();
			List<BrandModel> skuBrands = sku.getBrands();
			boolean gotOne = false;
			for (Iterator<BrandModel> skbItr=skuBrands.iterator();skbItr.hasNext() && !gotOne; ) {
				BrandModel b = skbItr.next();
				Image brandLogoSmall = b.getLogoSmall();
				if (brandLogoSmall!=null && !displayableBrands.contains(b) && !sku.isUnavailable()) {
					numberOfBrands--;
					displayableBrands.add(b);
					gotOne=true;
				}
			}
		}
		return displayableBrands;
	}
	
	/* price calculator calls */ 
	
	public String getSizeDescription() throws FDResourceException {
	    return getPriceCalculator().getSizeDescription();
	}
	
	public String getKosherSymbol() throws FDResourceException {
	    return getPriceCalculator().getKosherSymbol();
	}
	
	public String getKosherType() throws FDResourceException {
        return getPriceCalculator().getKosherType();
	}

	
	public boolean isKosherProductionItem() throws FDResourceException {
        return getPriceCalculator().isKosherProductionItem();
	}

	public int getKosherPriority() throws FDResourceException {
	    return getPriceCalculator().getKosherPriority();
	}

        /* price calculator call end */ 
	
	public boolean hasComponentGroups() {
		return !getComponentGroups().isEmpty();
	}

	public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config) {
		List<ComponentGroupModel> compositeGroups = getComponentGroups();
		try {
			for (ComponentGroupModel cgm : compositeGroups) {
			    if (cgm.hasCharacteristics() && cgm.isUnavailable(config)) {
				return false;
			    }
			}
			return true;
		} catch (FDResourceException ex) {
			throw new FDRuntimeException(ex);
		}
	}

	public boolean isPreconfigured() {
		return false;
	}

	public EnumProductLayout getProductLayout() {
		return getProductLayout(EnumProductLayout.PERISHABLE);
	}
	
	public EnumProductLayout getProductLayout(EnumProductLayout defValue) {
        return EnumProductLayout.getLayoutType(this.getAttribute("PRODUCT_LAYOUT", defValue.getId()));
	}

	public String getSubtitle() {
		return getAttribute("SUBTITLE", "");
	}

	public String getAltText() {
		return getAttribute("ALT_TEXT","");
	}

	public EnumLayoutType getLayout() {
		return EnumLayoutType.getLayoutType(this.getAttribute("LAYOUT", EnumLayoutType.GENERIC.getId()));
	}

	public EnumTemplateType getTemplateType() {
		return EnumTemplateType.getTemplateType(this.getAttribute("TEMPLATE_TYPE", EnumTemplateType.GENERIC.getId()));
	}

	public String getAlsoSoldAsName() {
		return getAttribute("ALSO_SOLD_AS_NAME", "");
	}

	public String getRedirectUrl() {
        return (String) getCmsAttributeValue("REDIRECT_URL");
	}

	public String getSellBySalesunit() {
		return getAttribute("SELL_BY_SALESUNIT", "");
	}

	public String getSalesUnitLabel() {
		return getAttribute("SALES_UNIT_LABEL", "");
	}

	public String getQuantityText() {
		return getAttribute("QUANTITY_TEXT", "");
	}

	public String getQuantityTextSecondary() {
		return getAttribute("QUANTITY_TEXT_SECONDARY", (String) null);
	}

	public String getServingSuggestion() {
		return (String) getCmsAttributeValue("SERVING_SUGGESTION");
	}

	public String getPackageDescription() {
		return getAttribute("PACKAGE_DESCRIPTION", "");
	}

	public String getSeasonText() {
		return (String) getCmsAttributeValue("SEASON_TEXT");
	}

	public String getWineFyi() {
	    return (String) getCmsAttributeValue("WINE_FYI");
	}

	public String getWineRegion() {
		return (String) getCmsAttributeValue("WINE_REGION");
	}

	public String getSeafoodOrigin() {
		return getAttribute("SEAFOOD_ORIGIN", "");
	}

	public boolean isShowSalesUnitImage() {
		return getAttribute("SHOW_SALES_UNIT_IMAGE", false);
	}

	public boolean isNutritionMultiple() {
		return getAttribute("NUTRITION_MULTIPLE", false);
	}

	public boolean isNotSearchable() {
		return getAttribute("NOT_SEARCHABLE", false);
	}

	public Double getContainerWeightHalfPint() {
		return (Double) getCmsAttributeValue("CONTAINER_WEIGHT_HALF_PINT");
	}

	public Double getContainerWeightPint() {
		return (Double) getCmsAttributeValue("CONTAINER_WEIGHT_PINT");
	}

	public Double getContainerWeightQuart() {
		return (Double) getCmsAttributeValue("CONTAINER_WEIGHT_QUART");
	}

	public boolean isIncrementMaxEnforce() {
		return getAttribute("INCREMENT_MAX_ENFORCE", false);
	}

	public String getProdPageRatings() {
		return getAttribute("PROD_PAGE_RATINGS", "");
	}

	public String getProdPageTextRatings() {
		return getAttribute("PROD_PAGE_TEXT_RATINGS", "");
	}

	public String getRatingProdName() {
		return getAttribute("RATING_PROD_NAME", "");
	}

	
	public List<ProductModel> getRelatedProducts() {
		return getYmals(FDContentTypes.PRODUCT);
	}

	/**
	 *  Return all the YMAL products, of all types, that are related to this
	 *  product. The returned list will not contain any items from YMAL sets
	 *  related to this product.
	 *  
	 *  @return a list of content nodes that are YMALs to this product.
	 */
	public List<ContentNodeModel> getYmals() {
		ContentNodeModelUtil.refreshModels(this, "RELATED_PRODUCTS", ymals, false, true);
		return Collections.unmodifiableList(ymals);
	}

	/**
	 *  Return a list of YMAL products, but only of a specified type.
	 *  
	 *  @param type the type of YMAL products to return.
	 *  @return a list of objects of the specified content type, which are
	 *          YMALs of this product.
	 *  @see #getYmals()
	 */
	@SuppressWarnings( "unchecked" )
	private <T extends ContentNodeModel> List<T> getYmals(ContentType type) {
		List<ContentNodeModel> values = getYmals();
		List<T> l = new ArrayList<T>( values.size() );
		
		for (Iterator<ContentNodeModel> i = values.iterator(); i.hasNext(); ) {
			ContentNodeModel node = i.next();
			ContentKey k = node.getContentKey();
			if (type.equals(k.getType())) {
				l.add((T)node);
			}
		}
		
		return l;
	}

	/**
	 *  Return the active YmalSet, if any.
	 *  
	 *  @return the active YmalSet, or null if there's no active Ymal Set.
	 *  @see YmalSet
	 */
	public YmalSet getActiveYmalSet() {
		YmalSet current = getCurrentActiveYmalSet(this.getContentKey().getId());
		if (current == null) {
			YmalSet newSet = YmalSetSourceUtil.findActiveYmalSet(this);
			if (newSet != null) {
				setCurrentActiveYmalSet(this.getContentKey().getId(), newSet);
			}
			return newSet;
		}
		return current;
	}
	
	public void resetActiveYmalSetSession() {
		resetActiveYmalSets();
	}

	/**
	 *  Remove discontinued and unavailable products from a product set.
	 *
	 *  @param products a set of ProductModel objects.
	 *         this set may be changed by this function call.
	 */
	private void removeDiscUnavProducts(Set<ProductModel> products) {
		for (Iterator<ProductModel> it = products.iterator(); it.hasNext(); ) {
			ProductModel product = it.next();
			
			if (product.isUnavailable() || product.isDiscontinued()) {
				it.remove();
			}
		}
	}

	/**
	 *  Remove this very product of a product set, if contained within.
	 *
	 *  @param products a set of ProductModel objects.
	 *         this set may be changed by this function call.
	 */
	private void removeSelf(Set<? extends ContentNodeModel> products) {
		if (products.contains(this)) {
			products.remove(this);
		}
	}

	/**
	 *  Remove unavailable recipes from a recipe set.
	 *
	 *  @param recipes a set of Recipe objects.
	 *         this set may be changed by this function call.
	 */
	private void removeUnavRecipes(Set<Recipe> recipes) {
		for (Iterator<Recipe> it = recipes.iterator(); it.hasNext(); ) {
			Recipe recipe = it.next();
			
			if (!recipe.isAvailable()) {
				it.remove();
			}
		}
	}

	/**
	 *  Remove products from the YMAL set that correspond to the supplied set
	 *  of SKUs.
	 *
	 *  @param products a collection of Product objects.
	 *         this set may be changed by this function call.
	 *  @param removeSkus a set of FDSku object, for which correspodning
	 *         products will be removed.
	 */
	private void removeSkus(Collection<ProductModel> products, Set<FDSku> removeSkus) {
		if (removeSkus == null) 
			return;
		for (Iterator<ProductModel> it = products.iterator(); it.hasNext(); ) {
			ProductModel product = it.next();			
inner:
			for ( SkuModel sku : product.getSkus() ) {
				for ( FDSku fdSku : removeSkus ) {
					if (sku.getSkuCode().equals(fdSku.getSkuCode())) {
						it.remove();
						break inner;
					}
				}
			}
		}
	}

	/**
	 *  Return a list of YMAL products.
	 *  A helper function, for a complete description, {@link #getYmalProducts(Set)}
	 *  Calls getYmalProducts(null)
	 *  
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmalProducts(Set)
	 */
	public List<ProductModel> getYmalProducts() {
		return getYmalProducts(null);
	}
	
	/**
	 * Return a list of the recommended alternatives of this product.
	 * 
	 * @return list of recommended alternatives
	 */
	public List<ContentNodeModel> getRecommendedAlternatives() {
		ContentNodeModelUtil.refreshModels(this, "RECOMMENDED_ALTERNATIVES", recommendedAlternatives, false, true);
		return Collections.unmodifiableList(recommendedAlternatives);
	}

	/**
	 *  Return a list of YMAL products.
	 *  
	 *  YMALs are gathered in the following way:
	 *  - nodes from the RELATED_PRODUCTS attribute are gathered
	 *  - the active YmalSet is retrieved
	 *  - items from these two lists are put into a new list, with RELATED_PRODUCTS
	 *  - duplicate items from this list are removed
	 *  - only YMALs of the PRODUCT are retained
	 *    in the beginning, following items from the active YmalSet
	 *  - discontinued and unavailable products are removed from the list
	 *  - the product shown on the page is removed from the list
	 *  - the list is cut to display at most 6 items
	 *
	 *  TODO:
	 *  - auto-configure products which can be auto-configured if the active
	 *    YMAL set has the 'transactional' property set
	 *  - generate auto cross-sell if otherwise no YMAL items would be displayed
	 *
	 *  @param removeSkus a set of FDSku objects, for which correspoding
	 *         products need to be removed from the final list of YMALs.
	 *         this might be null, in which case it has no effect. 
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 */
	public List<ProductModel> getYmalProducts(Set<FDSku> removeSkus) {
		List<ProductModel> l = getYmals( FDContentTypes.PRODUCT );
		LinkedHashSet<ProductModel> ymals = new LinkedHashSet<ProductModel>( l );
		YmalSet ymalSet = getActiveYmalSet();
		ArrayList<ProductModel> finalList;
		int size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalProducts());
		}
		
		removeDiscUnavProducts(ymals);
		removeSelf(ymals);
		removeSkus(ymals, removeSkus);

		size      = Math.min(ymals.size(), 6);      
		finalList = new ArrayList<ProductModel>(size);
		// cut the list to be at most 6 items in size
		for (Iterator<ProductModel> it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
			finalList.add(it.next());
		}
		
		return finalList;
	}

	/**
	 *  Return a list of YMAL categories.
	 *  
	 *  YMALs are gathered in the following way:
	 *  - nodes from the RELATED_PRODUCTS attribute are gathered
	 *  - the active YmalSet is retrieved
	 *  - items from these two lists are put into a new list, with RELATED_PRODUCTS
	 *  - duplicate items from this list are removed
	 *  - only YMALs of the CATEGORY are retained
	 *    in the beginning, following items from the active YmalSet
	 *  - the product shown on the page is removed from the list
	 *  - the list is cut to display at most 6 items
	 *
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List<CategoryModel> getYmalCategories() {
		List<CategoryModel> l = getYmals( FDContentTypes.CATEGORY );
		LinkedHashSet<CategoryModel> ymals = new LinkedHashSet<CategoryModel>( l );
		YmalSet        ymalSet = getActiveYmalSet();
		ArrayList<CategoryModel> finalList;
		int            size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalCategories());
		}
		
		size      = Math.min(ymals.size(), 6);      
		finalList = new ArrayList<CategoryModel>(size);
		// cut the list to be at most 6 items in size
		for ( Iterator<CategoryModel> it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
			finalList.add(it.next());
		}
		
		return finalList;
	}
	
	/**
	 *  Return a list of YMAL recipes.
	 *  
	 *  YMALs are gathered in the following way:
	 *  - nodes from the RELATED_PRODUCTS attribute are gathered
	 *  - the active YmalSet is retrieved
	 *  - items from these two lists are put into a new list, with RELATED_PRODUCTS
	 *  - duplicate items from this list are removed
	 *  - only YMALs of type RECIPE are retained
	 *    in the beginning, following items from the active YmalSet
	 *  - unavailable recipes are removed from the list
	 *  - the list is cut to display at most 6 items
	 *
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List<Recipe> getYmalRecipes() {
		List<Recipe> l = getYmals( FDContentTypes.RECIPE );
		LinkedHashSet<Recipe> ymals = new LinkedHashSet<Recipe>( l );
		YmalSet ymalSet = getActiveYmalSet();
		ArrayList<Recipe> finalList;
		int size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalRecipes());
		}
		
		removeUnavRecipes(ymals);
		
		if (ymals.size() == 0) {
			// generate automated cross-sell if no recipes are specified as YMALs
			// otherwise. these will be randomized to at most 5 elements
			ymals.addAll(YmalSet.getAutoCrossSellRecipes(this));
			removeUnavRecipes(ymals);
			
			if (ymals.size() <= 5) {
				finalList = new ArrayList<Recipe>(ymals.size());
				finalList.addAll(ymals);
			} else {
				Vector<ContentNodeModel>  v      = new Vector<ContentNodeModel>(ymals);
				Random  random = new Random();
				finalList      = new ArrayList<Recipe>(5);
				
				while (finalList.size() < 5) {
					int		ix = random.nextInt(v.size());
					Recipe  r  = (Recipe) v.get(ix);
					finalList.add(r);
					v.remove(ix);
				}
			}
		} else {
			size      = Math.min(ymals.size(), 6);      
			finalList = new ArrayList<Recipe>(size);
			// cut the list to be at most 6 items in size
			for (Iterator<Recipe> it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
				finalList.add(it.next());
			}
		}
		
		return finalList;
	}

	public List<ProductModel> getWeRecommendText() {
		ContentNodeModelUtil.refreshModels(this, "WE_RECOMMEND_TEXT", weRecommendText, false);

		return new ArrayList<ProductModel>(weRecommendText);
	}

	public List<ProductModel> getWeRecommendImage() {
		ContentNodeModelUtil.refreshModels(this, "WE_RECOMMEND_IMAGE", weRecommendImage, false);

		return new ArrayList<ProductModel>(weRecommendImage);
	}


	public List<Recipe> getRelatedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "RELATED_RECIPES", relatedRecipes, false, true);
		return Collections.unmodifiableList(relatedRecipes);
	}

	public List<ProductModel> getProductBundle() {
		ContentNodeModelUtil.refreshModels(this, "PRODUCT_BUNDLE", productBundle, false);

		return new ArrayList<ProductModel>(productBundle);
	}

	public List<CategoryModel> getHowtoCookitFolders() {
		ContentNodeModelUtil.refreshModels(this, "HOWTOCOOKIT_FOLDERS", howtocookitFolders, false);

		return new ArrayList<CategoryModel>(howtocookitFolders);
	}

	public CategoryModel getPrimaryHome() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("PRIMARY_HOME"); 
	    
	    return key == null ? null : (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key);
    }

	public SkuModel getPreferredSku() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("PREFERRED_SKU");

	    return key == null ? null : (SkuModel) ContentFactory.getInstance().getContentNodeByKey(key);
	}

	public List<DomainValue> getRating() {
		ContentNodeModelUtil.refreshModels(this, "RATING", rating, false);

		return new ArrayList<DomainValue>(rating);
	}

	public List<Domain> getUsageList() {
		ContentNodeModelUtil.refreshModels(this, "USAGE_LIST", usageList, false);

		return new ArrayList<Domain>(usageList);
	}

	public DomainValue getUnitOfMeasure() {
		ContentKey key = (ContentKey) getCmsAttributeValue("UNIT_OF_MEASURE");
		
		return key == null
  	         ? null
             : (DomainValue) ContentFactory.getInstance().getContentNodeByKey(key);
	}

	public List<Domain> getVariationMatrix() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_MATRIX", variationMatrix, false);

		return new ArrayList<Domain>(variationMatrix);
	}

	public List<Domain> getVariationOptions() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_OPTIONS", variationOptions, false);

		return new ArrayList<Domain>(variationOptions);
	}

	public DomainValue getWineCountry() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("WINE_COUNTRY");
	    return key != null ? ContentFactory.getInstance().getDomainValueById(key.getId()) : null;
	}
	
	public ContentKey getWineCountryKey() {
	    return (ContentKey) getCmsAttributeValue("WINE_COUNTRY");
	}
			

	public Image getProdImage() {
        return FDAttributeFactory.constructImage(this, "PROD_IMAGE");
	}

	public Image getFeatureImage() {
        return FDAttributeFactory.constructImage(this, "PROD_IMAGE_FEATURE");
	}

	public Image getRatingRelatedImage() {
        return FDAttributeFactory.constructImage(this, "RATING_RELATED_IMAGE");
	}

	public Image getAlternateImage() {
        return FDAttributeFactory.constructImage(this, "ALTERNATE_IMAGE");
	}

	public Image getDescriptiveImage() {
        return FDAttributeFactory.constructImage(this, "DESCRIPTIVE_IMAGE");
	}

	public Image getRolloverImage() {
		return FDAttributeFactory.constructImage( this, "PROD_IMAGE_ROLLOVER" );
	}

	public Html getProductAbout() {
		return FDAttributeFactory.constructHtml( this, "PRODUCT_ABOUT" );
	}

	public Html getRecommendTable() {
		return FDAttributeFactory.constructHtml( this, "RECOMMEND_TABLE" );
	}

	public Html getProductQualityNote() {
		return FDAttributeFactory.constructHtml( this, "PRODUCT_QUALITY_NOTE" );
	}

	public Html getProductDescriptionNote() {
		return FDAttributeFactory.constructHtml( this, "PROD_DESCRIPTION_NOTE" );
	}

	public Html getFreshTips() {
		return FDAttributeFactory.constructHtml( this, "FRESH_TIPS" );
	}

	public List<Html> getDonenessGuide() {
		return FDAttributeFactory.constructWrapperList( this, "DONENESS_GUIDE" );
	}

	public Html getFddefFrenching() {
		return FDAttributeFactory.constructHtml( this, "FDDEF_FRENCHING" );
	}

	public Html getFddefGrade() {
		return FDAttributeFactory.constructHtml( this, "FDDEF_GRADE" );
	}

	public Html getFddefSource() {
		return FDAttributeFactory.constructHtml( this, "FDDEF_SOURCE" );
	}

	public Html getFddefRipeness() {
		return FDAttributeFactory.constructHtml( this, "FDDEF_RIPENESS" );
	}

	public boolean isHasSalesUnitDescription() {
		return getCmsAttributeValue( "SALES_UNIT_DESCRIPTION" ) != null;
	}

	public Html getSalesUnitDescription() {
		return FDAttributeFactory.constructHtml( this, "SALES_UNIT_DESCRIPTION" );
	}

	public boolean isHasPartiallyFrozen() {
		return getCmsAttributeValue( "PARTIALLY_FROZEN" ) != null;
	}

	public Html getPartallyFrozen() {
		return FDAttributeFactory.constructHtml( this, "PARTIALLY_FROZEN" );
	}

	public List<ComponentGroupModel> getComponentGroups() {
		ContentNodeModelUtil.refreshModels( this, "COMPONENT_GROUPS", componentGroups, false );
		return new ArrayList<ComponentGroupModel>( componentGroups );
	}

	public Html getProductTermsMedia() {
		return FDAttributeFactory.constructHtml( this, "PRODUCT_TERMS_MEDIA" );
	}

	// new Wine store changes
	
	
	public String getWineClassification() {
		return getAttribute("WINE_CLASSIFICATION", "");
	}

	public String getWineImporter() {
		return getAttribute("WINE_IMPORTER", "");
	}

	public String getWineAlchoholContent() {
		return getAttribute("WINE_ALCH_CONTENT", "");
	}

	public String getWineAging() {
		return getAttribute("WINE_AGING", "");
	}

	public String getWineCity() {
		return getAttribute("WINE_CITY", "");
	}
	
	public String getWineType() {
		return (String)getCmsAttributeValue("WINE_TYPE");
	}
	
	/**
	 * This is used for getting a media attribute, if the usage of the normal getters are not feasible. 
	 * For example, when the name of the attribute comes from the client side. It's not a very fortunate
	 * situation.
	 * 
	 * @param name
	 * @return
	 */
	public MediaI getMedia(String name) {
	    return (MediaI) FDAttributeFactory.constructWrapperValue(this, name);
	}

	public List<DomainValue> getNewWineType() {
		ContentNodeModelUtil.refreshModels(this, "WINE_NEW_TYPE", wineNewTypes, false);
		return new ArrayList<DomainValue>(wineNewTypes);		
	}

	public List<DomainValue> getWineVintage() {
		ContentNodeModelUtil.refreshModels(this, "WINE_VINTAGE", wineVintages, false);
		return new ArrayList<DomainValue>(wineVintages);		
	}
	
	
	public List<DomainValue> getWineVarietal() {
		ContentNodeModelUtil.refreshModels(this, "WINE_VARIETAL", wineVarietals, false);
		return new ArrayList<DomainValue>(wineVarietals);		
	}

	public List<DomainValue> getNewWineRegion() {
		ContentNodeModelUtil.refreshModels(this, "WINE_NEW_REGION", wineRegions, false);
		return new ArrayList<DomainValue>(wineRegions);		
	}

	public List<DomainValue> getWineRating1() {
		ContentNodeModelUtil.refreshModels(this, "WINE_RATING1", wineRatings1, false);
		return new ArrayList<DomainValue>(wineRatings1);	
	}

	public List<DomainValue> getWineRating2() {
		ContentNodeModelUtil.refreshModels(this, "WINE_RATING2", wineRatings2, false);
		return new ArrayList<DomainValue>(wineRatings2);	
	}

	public List<DomainValue> getWineRating3() {
		ContentNodeModelUtil.refreshModels(this, "WINE_RATING3", wineRatings3, false);
		return new ArrayList<DomainValue>(wineRatings3);		
	}
	
	@Override
	public DomainValue getWineRatingValue1() {
		List<DomainValue> values = getWineRating1();
		return values.size() > 0 ? values.get(0) : null;
	}
	
	@Override
	public DomainValue getWineRatingValue2() {
		List<DomainValue> values = getWineRating2();
		return values.size() > 0 ? values.get(0) : null;
	}
	
	@Override
	public DomainValue getWineRatingValue3() {
		List<DomainValue> values = getWineRating3();
		return values.size() > 0 ? values.get(0) : null;
	}
	
	@Override
	public boolean hasWineOtherRatings() {
		return getWineRating1().size() > 0 || getWineRating2().size() > 0 || getWineRating3().size() > 0;
	}

	public Html getWineReview1() {
		return FDAttributeFactory.constructHtml( this, "WINE_REVIEW1" );
	}

	public int getExpertWeight() {
		Object value = FDAttributeFactory.constructWrapperValue( this, "SS_EXPERT_WEIGHTING" );
		return value instanceof Number ? ( (Number)value ).intValue() : 0;
	}

	public Html getWineReview2() {
		return FDAttributeFactory.constructHtml( this, "WINE_REVIEW2" );
	}

	public Html getWineReview3() {
		return FDAttributeFactory.constructHtml( this, "WINE_REVIEW3" );
	}

	public Html getProductBottomMedia() {
		return FDAttributeFactory.constructHtml( this, "PRODUCT_BOTTOM_MEDIA" );
	}

	public CategoryModel getPerfectPair() {
		ContentKey key = (ContentKey)getCmsAttributeValue( "PERFECT_PAIR" );
		return key == null ? null : (CategoryModel)ContentFactory.getInstance().getContentNodeByKey( key );
	}

	public List<DomainValue> getWineClassifications() {
		List<DomainValue> classifications = new ArrayList<DomainValue>();
		//Add Country domain Value.
		classifications.add(getWineCountry());
		//Add Type domain Values.
		classifications.addAll(getNewWineType());
		//Add Region domain Values.
		classifications.addAll(getNewWineRegion());
		//Add Varietal domain Values.
		classifications.addAll(getWineVarietal());
		return classifications;
	}
	
	public EnumOrderLineRating getProductRatingEnum() throws FDResourceException {
		return getProductRatingEnum(null);
	}

    public EnumOrderLineRating getProductRatingEnum(String skuCode) throws FDResourceException {
        EnumOrderLineRating rating = EnumOrderLineRating.NO_RATING;

        FDProductInfo productInfo = null;
        if(skuCode == null || skuCode.trim().equals("")) {
        	List<SkuModel> skus = getPrimarySkus();
            SkuModel sku = null;
            // remove the unavailable sku's
            for (ListIterator<SkuModel> li = skus.listIterator(); li.hasNext();) {
                sku = li.next();
                if (sku.isUnavailable()) {
                    li.remove();
                }
            }
	        if (skus.size() == 0)
	            return rating; // skip this item..it has no skus. Hmmm?
	        if (skus.size() == 1) {
	            sku = skus.get(0); // we only need one sku
	        } else {
	            sku = Collections.max(skus, RATING_COMPARATOR);
	        }
	        if (sku != null && sku.getSkuCode() != null) {
	        	skuCode = sku.getSkuCode();
	        }
        }
        if (skuCode != null) {
            //
            // get the FDProductInfo from the FDCachedFactory
            //
            try {
                /*
                 * grab property to determine which sku prefixes to display
                 * ratings for
                 * 
                 * assuming some prefixes are set, loop, checking to see if the
                 * current product's sku prefix matches an allowed prefix.
                 * 
                 * exit loop on a match.
                 */

                /*
                 * There is a similar setup in the GetPeakProduceTag.java file
                 * and in ProductModelImpl.java
                 */
                // LOG.debug("===== in getProductRating in ProductModelImpl :"+sku.getSkuCode());

                // grab sku prefixes that should show ratings
                String _skuPrefixes = FDStoreProperties.getRatingsSkuPrefixes();
                // LOG.debug("* getRatingsSkuPrefixes :"+_skuPrefixes);

                // if we have prefixes then check them
                if (_skuPrefixes != null && !"".equals(_skuPrefixes)) {
                    StringTokenizer st = new StringTokenizer(_skuPrefixes, ","); // setup for splitting property
                    String curPrefix = ""; // holds prefix to check against
                    String spacer = "* "; // spacing for sysOut calls
                    boolean matchFound = false;

                    // loop and check each prefix
                    while (st.hasMoreElements()) {

                        curPrefix = st.nextToken();
                        // LOG.debug(spacer+"Rating _skuPrefixes checking :"+curPrefix);

                        // if prefix matches get product info
                        if (skuCode.startsWith(curPrefix)) {
                            productInfo = FDCachedFactory.getProductInfo(skuCode);
                            // LOG.debug(" Rating productInfo :"+productInfo);
                            String tmpRating = productInfo.getRating();

                            if (tmpRating != null && tmpRating.trim().length() > 0) {
                                EnumOrderLineRating enumRating = EnumOrderLineRating.getEnumByStatusCode(tmpRating);
                                // LOG.debug(" enumRating :"+enumRating);

                                if (enumRating != null && enumRating.isEligibleToDisplay()) {
                                    rating = enumRating;
                                    // LOG.debug(" rating in display format  :"+rating);
                                }
                            }
                            matchFound = true;
                        }
                        // exit on matched sku prefix
                        // LOG.debug(spacer+"Rating matchFound :"+matchFound);
                        if (matchFound) {
                            break;
                        }
                        spacer = spacer + "   ";
                    }
                }
            } catch (FDSkuNotFoundException ignore) {
            }
        }

        return rating;
    }
    
    public String getProductRating() throws FDResourceException {
    	return getProductRating(null);
    }
    
    public String getProductRating(String skuCode) throws FDResourceException {
    	EnumOrderLineRating rating = getProductRatingEnum(skuCode);
    	if (rating == EnumOrderLineRating.NO_RATING) {
    		return "";
    	}
	return rating.getStatusCodeInDisplayFormat();
    }
    
    public String getFreshnessGuaranteed() throws FDResourceException {
    	
    	String freshness = null;
    	if(!FDStoreProperties.IsFreshnessGuaranteedEnabled()) {
    		return null;
    	}
    	List<SkuModel> skus = getSkus();
        SkuModel sku = null;
        // remove the unavailable sku's
        for (ListIterator<SkuModel> li = skus.listIterator(); li.hasNext();) {
            sku = li.next();
            if (sku.isUnavailable()) {
                li.remove();
            }
        }

        FDProductInfo productInfo = null;
        if (skus.size() == 0)
            return freshness; // skip this item..it has no skus. Hmmm?
        if (skus.size() == 1) {
            sku = skus.get(0); // we only need one sku
        } else {
            sku = Collections.max(skus, RATING_COMPARATOR);
        }
        if (sku != null && sku.getSkuCode() != null) {
            
            try {
                // Freshness Guaranteed list of qualifying sku prefixees
                String skuPrefixes = FDStoreProperties.getFreshnessGuaranteedSkuPrefixes();
                
                // if we have prefixes then check them
                if (skuPrefixes != null && !"".equals(skuPrefixes)) {
                    StringTokenizer st = new StringTokenizer(skuPrefixes, ","); // split comma-delimited list
                    String curPrefix = ""; // holds prefix to check against
                    
                    while (st.hasMoreElements()) {
                        curPrefix = st.nextToken();
                        // if prefix matches get product info
                        if (sku.getSkuCode().startsWith(curPrefix)) {
                            productInfo = FDCachedFactory.getProductInfo(sku.getSkuCode());
                            freshness = productInfo.getFreshness();
                            if ((freshness != null && freshness.trim().length() > 0) 
                            		&& !"000".equalsIgnoreCase(freshness.trim()) 
                                	&& StringUtil.isNumeric(freshness) 
                                	&& Integer.parseInt(freshness) > 0)  {
                            	    	return freshness;
                            }   
                        }
                    }
                }
            } catch (FDSkuNotFoundException ignore) {}
        }
        return null;
    }

	public List getGiftcardType() {
		ContentNodeModelUtil.refreshModels(this, "GIFTCARD_TYPE", giftcardTypes, false);
		return new ArrayList(giftcardTypes);		
	}

	public PricingContext getPricingContext(){
		return PricingContext.DEFAULT;
	}

	public boolean isInPrimaryHome() {
	    ContentKey primaryHome = (ContentKey) getCmsAttributeValue("PRIMARY_HOME");
	    return primaryHome != null && primaryHome.equals(getParentNode().getContentKey());
	}
	
	public ProductModel getPrimaryProductModel() {
	    return isInPrimaryHome() ? this : (ProductModel) ContentFactory.getInstance().getContentNodeByKey(getContentKey());
	}
	
	/**
	 * @return the list of sku models of the primary product. Primary product is the product which is in his primary home.
	 */
	public List<SkuModel> getPrimarySkus() {
	    return getPrimaryProductModel().getSkus();
	}
	
	public String getReverseParentPath(boolean fullname) {
	    StringBuilder sb = new StringBuilder();
	    ContentNodeModel model = this;
	    while (model != null && !"Store".equals(model.getContentKey().getType().getName())) {
	        if (fullname) {
	            sb.append(model.getFullName());
	        }
	        if (model instanceof CategoryModel) {
	            sb.append("["+((CategoryModel)model).getPriority()+']');
	        }
	        sb.append('(').append(model.getContentKey()).append(") :: ");
	        model = model.getParentNode();
	    }
	    return sb.toString();
	}

	

	@Override
	public boolean isShowWineRatings() {
		// NOT wine
		if (!"usq".equalsIgnoreCase(getDepartment().getContentName()))
			return false;
		
		// NOT enabled
		if (!FDStoreProperties.isWineShowRatings())
			return false;
		
		// Rating is HIDDEN
		if (isHideWineRatingPricing())
			return false;
		
		return true;
	}

	@Override
	public EnumSustainabilityRating getSustainabilityRatingEnum()
			throws FDResourceException {
		return getSustainabilityRatingEnum(null);
	}

	@Override
	public String getSustainabilityRating() throws FDResourceException {
		return getSustainabilityRating(null);
	}

	@Override
	public String getSustainabilityRating(String skuCode)throws FDResourceException {
		
		EnumSustainabilityRating rating = getSustainabilityRatingEnum(skuCode);
    	if (rating == EnumSustainabilityRating.NO_RATING) {
    		return "";
    	}
    	return rating.getStatusCodeInDisplayFormat();
	}
	
	
	public EnumSustainabilityRating getSustainabilityRatingEnum(String skuCode) throws FDResourceException {
		EnumSustainabilityRating rating = EnumSustainabilityRating.NO_RATING;

        FDProductInfo productInfo = null;
        if(skuCode == null || skuCode.trim().equals("")) {
        	List<SkuModel> skus = getPrimarySkus();
            SkuModel sku = null;
            // remove the unavailable sku's
            for (ListIterator<SkuModel> li = skus.listIterator(); li.hasNext();) {
                sku = li.next();
                if (sku.isUnavailable()) {
                    li.remove();
                }
            }
	        if (skus.size() == 0)
	            return rating; // skip this item..it has no skus. Hmmm?
	        if (skus.size() == 1) {
	            sku = skus.get(0); // we only need one sku
	        } else {
	            sku = Collections.max(skus, SUSTAINABILITY_COMPARATOR);
	        }
	        if (sku != null && sku.getSkuCode() != null) {
	        	skuCode = sku.getSkuCode();
	        }
        }
        if (skuCode != null) {
            //
            // get the FDProductInfo from the FDCachedFactory
            //
            try {
                /*
                 * grab property to determine which sku prefixes to display
                 * ratings for
                 * 
                 * assuming some prefixes are set, loop, checking to see if the
                 * current product's sku prefix matches an allowed prefix.
                 * 
                 * exit loop on a match.
                 */

                /*
                 * There is a similar setup in the GetPeakProduceTag.java file
                 * and in ProductModelImpl.java
                 */
                // LOG.debug("===== in getProductRating in ProductModelImpl :"+sku.getSkuCode());

                // grab sku prefixes that should show ratings
                String _skuPrefixes = FDStoreProperties.getRatingsSkuPrefixes();
                // LOG.debug("* getRatingsSkuPrefixes :"+_skuPrefixes);

                // if we have prefixes then check them
                if (_skuPrefixes != null && !"".equals(_skuPrefixes)) {
                    StringTokenizer st = new StringTokenizer(_skuPrefixes, ","); // setup for splitting property
                    String curPrefix = ""; // holds prefix to check against
                    String spacer = "* "; // spacing for sysOut calls
                    boolean matchFound = false;

                    // loop and check each prefix
                    while (st.hasMoreElements()) {

                        curPrefix = st.nextToken();
                        // LOG.debug(spacer+"Rating _skuPrefixes checking :"+curPrefix);

                        // if prefix matches get product info
                        if (skuCode.startsWith(curPrefix)) {
                            productInfo = FDCachedFactory.getProductInfo(skuCode);
                            // LOG.debug(" Rating productInfo :"+productInfo);
                            String tmpRating = productInfo.getSustainabilityRating();

                            if (tmpRating != null && tmpRating.trim().length() > 0) {
                            	EnumSustainabilityRating enumRating = EnumSustainabilityRating.getEnumByStatusCode(tmpRating);
                                // LOG.debug(" enumRating :"+enumRating);

                                if (enumRating != null && enumRating.isEligibleToDisplay()) {
                                    rating = enumRating;
                                    // LOG.debug(" rating in display format  :"+rating);
                                }
                            }
                            matchFound = true;
                        }
                        // exit on matched sku prefix
                        // LOG.debug(spacer+"Rating matchFound :"+matchFound);
                        if (matchFound) {
                            break;
                        }
                        spacer = spacer + "   ";
                    }
                }
            } catch (FDSkuNotFoundException ignore) {
            }
        }

        return rating;
    }

}
