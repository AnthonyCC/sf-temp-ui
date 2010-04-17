/*
 * Created on Aug 3, 2005
 */
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
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDKosherInfo;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 */
public class ProductModelImpl extends AbstractProductModelImpl {
	private static final long serialVersionUID = 2103318183933323914L;

	private static final Logger LOG = LoggerFactory.getInstance( ProductModelImpl.class ); 
	
	private static final Random rnd = new Random();
	
	private synchronized static final int nextInt(int n) {
		return rnd.nextInt(n);
	}

	private static ThreadLocal activeYmalSets = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
	};
	
	private static YmalSet getCurrentActiveYmalSet(String productId) {
		return (YmalSet) ((Map) activeYmalSets.get()).get(productId);
	}
	
	private static void setCurrentActiveYmalSet(String productId, YmalSet set) {
		((Map) activeYmalSets.get()).put(productId, set);
	}

	private static void resetActiveYmalSets() {
		((Map) activeYmalSets.get()).clear();
	}
	
	private List skuModels = new ArrayList();
	
	private final List<BrandModel> brandModels = new ArrayList<BrandModel>();
	
	private final List weRecommendText = new ArrayList();

	private final List weRecommendImage = new ArrayList();

	private final List relatedRecipes = new ArrayList();

	private final List productBundle = new ArrayList();

	private final List howtocookitFolders = new ArrayList();

	private final List rating = new ArrayList();

	private final List usageList = new ArrayList();

	private final List variationMatrix = new ArrayList();

	private final List variationOptions = new ArrayList();
	
	private final List<ComponentGroupModel> componentGroups = new ArrayList<ComponentGroupModel>();
	
	private final List<ContentNodeModel> recommendedAlternatives = new ArrayList<ContentNodeModel>();
	
	// new wine store related changes
	
	private final List wineNewTypes=new ArrayList();
	
	private final List wineVintages=new ArrayList();
	
	private final List wineRegions=new ArrayList();
	
	private final List wineRatings1=new ArrayList();
	
	private final List wineRatings2=new ArrayList();
	
	private final List wineRatings3=new ArrayList();
	
	private final List wineVarietals=new ArrayList();
	
	/**
	 *  The list of YMAL products related to this recipe.
	 */
	private final List ymals = new ArrayList();
	
	/**
	 *  The list of YmalSet objects related to this recipe.
	 */
	private final List ymalSets = new ArrayList();
	
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
		return (SkuModel) getSkus().get(idx);
	}

	public SkuModel getSku(String skuCode) {
		List skus = getSkus();
		for (Iterator sIter = skus.iterator(); sIter.hasNext();) {
			SkuModel s = (SkuModel) sIter.next();
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
		List skus = pm.skuModels;
		List newList = new ArrayList();
		//clone the skus also, since we try to get parents from  the sku level
		for (Iterator itrSku = skus.iterator(); itrSku.hasNext();) {
			SkuModel sm = (SkuModel) ((SkuModel) itrSku.next()).clone();
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
		List skus = getPrimarySkus();
		for (Iterator i = skus.iterator(); i.hasNext();) {
			try {
				FDProduct fdp = ((SkuModel) i.next()).getProduct();
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
		List skus = getPrimarySkus();
		Iterator skuIter = skus.iterator();
		while (skuIter.hasNext()) {
			SkuModel sku = (SkuModel) skuIter.next();
			if (!sku.isDiscontinued())
				return false;
		}
		return true;
	}

	/**
	 * a product is out of season only if all of its skus are out of season
	 * @return  */
	public boolean isOutOfSeason() {
		List skus = getPrimarySkus();
		Iterator skuIter = skus.iterator();
		while (skuIter.hasNext()) {
			SkuModel sku = (SkuModel) skuIter.next();
			if (!sku.isOutOfSeason())
				return false;
		}
		return true;
	}

	/**
	 * a product is unavailable only if all of its skus are unavailable
	 * @return  */
	public boolean isUnavailable() {
		List skus = getPrimarySkus();
		Iterator skuIter = skus.iterator();
		while (skuIter.hasNext()) {
			SkuModel sku = (SkuModel) skuIter.next();
			if (!sku.isUnavailable() && isCharacteristicsComponentsAvailable(null))
				return false;
		}

		return true;
	}

	/**
	 * a product is temp. unavailable only if all of its skus are temp. unavailable
	 * @return  */
	public boolean isTempUnavailable() {
		List skus = getPrimarySkus();
		Iterator skuIter = skus.iterator();
		while (skuIter.hasNext()) {
			SkuModel sku = (SkuModel) skuIter.next();
			if (!sku.isTempUnavailable())
				return false;
		}
		return true;
	}

	/**
	 * @return true if any of its skus is a platter
	 */
	public boolean isPlatter() {
		List skus = getPrimarySkus();
		Iterator skuIter = skus.iterator();
		while (skuIter.hasNext()) {
			SkuModel sku = (SkuModel) skuIter.next();
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
		List skus = getPrimarySkus();
		DayOfWeekSet allBlockedDays = DayOfWeekSet.EMPTY;
		for (Iterator i = skus.iterator(); i.hasNext();) {
			SkuModel sku = (SkuModel) i.next();
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
		List skus = getPrimarySkus();
		Iterator skuIter = skus.iterator();
		while (skuIter.hasNext()) {
			SkuModel sku = (SkuModel) skuIter.next();
			if (sku.isAvailableWithin(days))
				return true;
		}
		return false;
	}

	/**
	 * a product's earliest availability is the earliest of it's sku's earliest availability
	 * @return  */
	public Date getEarliestAvailability() {
		List skus = getPrimarySkus();
		Date ea = null;
		Iterator skuIter = skus.iterator();
		while (skuIter.hasNext()) {
			SkuModel sku                  = (SkuModel) skuIter.next();
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

            for (ListIterator li = skus.listIterator(); li.hasNext();) {
                    SkuModel sku = (SkuModel) li.next();
                    if (sku.isUnavailable()) {
                            li.remove();
                    } else if (sku.getContentKey().equals(preferredSku)) {
                            return sku;
                    }
            }
            if (skus.size() == 0)
                    return null;
            return (SkuModel) Collections.min(skus, new ZonePriceComparator(context.getZoneId()));
	}
	
	/**
	 * @param type a multivalued ErpNutritionInfoType
	 * @return common Set of nutrition information of all available SKUs 
	 */
	public Set getCommonNutritionInfo(ErpNutritionInfoType type) throws FDResourceException {
		List skus = getPrimarySkus();
		if (!type.isMultiValued()) {
			throw new IllegalArgumentException(type + " is not multivalued");
		}
		Set common = null;
		boolean first = true;
		for (Iterator i = skus.listIterator(); i.hasNext();) {
			SkuModel sku = (SkuModel) i.next();
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


	public String getFullName() {
		String fullName = super.getFullName();
		return fullName != null ? fullName : "";
	}

	/**
	 * @return the brand that begins with the specified name, empty string otherwise
	 */
	public String getPrimaryBrandName(String productName) {
		// get the first brand name, if any.
		List myBrands = this.getBrands();

		String prodNameLower = productName.toLowerCase();

		if (myBrands != null && myBrands.size() > 0) {

			// find the brand that begins with the full name
			for (int bx = 0; bx < myBrands.size(); bx++) {
				BrandModel bm = (BrandModel) myBrands.get(bx);
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
	public List getDisplayableBrands() {
		return this.getDisplayableBrands(1);
	}
	/**
	 * @return list of brands that can be displayed on the product page
	 */
	public List getDisplayableBrands(int numberOfBrands) {
		List prodBrands = this.getBrands();
		List displayableBrands = new ArrayList();
		
		if (prodBrands.size() > 0) {
			BrandModel b = (BrandModel) prodBrands.get(0);
			Image brandLogoSmall = b.getLogoSmall();
			if (brandLogoSmall!=null) {
				displayableBrands.add(prodBrands.get(0));
				numberOfBrands--;
			}
		}
		
		// now get the remaining brands from the sku
		List skus = getPrimarySkus();
		for (Iterator skuItr = skus.iterator();skuItr.hasNext() && numberOfBrands >0;) {
			SkuModel sku = (SkuModel)skuItr.next();
			List skuBrands = sku.getBrands();
			boolean gotOne = false;
			for (Iterator skbItr=skuBrands.iterator();skbItr.hasNext() && !gotOne; ) {
				BrandModel b = (BrandModel) skbItr.next();
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

	
	public List getRelatedProducts() {
		return getYmals(FDContentTypes.PRODUCT);
	}

	/**
	 *  Return all the YMAL products, of all types, that are related to this
	 *  product. The returned list will not contain any items from YMAL sets
	 *  related to this product.
	 *  
	 *  @return a list of content nodes that are YMALs to this product.
	 */
	public List getYmals() {
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
	private List getYmals(ContentType type) {
		List values = getYmals();
		List l      = new ArrayList(values.size());
		
		for (Iterator i = values.iterator(); i.hasNext(); ) {
			ContentNodeModel  node = (ContentNodeModel) i.next();
			ContentKey        k    = node.getContentKey();
			if (type.equals(k.getType())) {
				l.add(node);
			}
		}
		
		return l;
	}

	/**
	 *  Return all Ymal Sets related to this recipe.
	 *  
	 *  @return the list of all YmalSet objects related to this recipe.
	 */
	private List getYmalSets() {
		ContentNodeModelUtil.refreshModels(this, "ymalSets", ymalSets, false, true);
		return Collections.unmodifiableList(ymalSets);
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
			List ymalSets = getYmalSets();
			List activeSets = new ArrayList(ymalSets.size());
			for (Iterator it = ymalSets.iterator(); it.hasNext(); ) {
				YmalSet     ymalSet = (YmalSet) it.next();
				
				if (ymalSet.isActive()) {
					activeSets.add(ymalSet);
				}
			}
		
			if (activeSets.size() == 0)
				return null;
			else {
				YmalSet newSet = (YmalSet) activeSets.get(nextInt(activeSets.size()));
				setCurrentActiveYmalSet(this.getContentKey().getId(), newSet);
				return newSet;
			}
		} else
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
	private void removeDiscUnavProducts(Set products) {
		for (Iterator it = products.iterator(); it.hasNext(); ) {
			ProductModel product = (ProductModel) it.next();
			
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
	private void removeSelf(Set products) {
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
	private void removeUnavRecipes(Set recipes) {
		for (Iterator it = recipes.iterator(); it.hasNext(); ) {
			Recipe recipe = (Recipe) it.next();
			
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
	private void removeSkus(Collection products, Set removeSkus) {
		if (removeSkus == null) return;
		for (Iterator it = products.iterator(); it.hasNext(); ) {
			ProductModel product = (ProductModel) it.next();
			
inner:
			for (Iterator iit = product.getSkus().iterator(); iit.hasNext(); ) {
				SkuModel sku = (SkuModel) iit.next();

				for (Iterator iiit = removeSkus.iterator(); iiit.hasNext(); ) {
					FDSku fdSku = (FDSku) iiit.next();
					
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
	public List getYmalProducts() {
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
	public List getYmalProducts(Set removeSkus) {
		LinkedHashSet  ymals   = new LinkedHashSet(getYmals(FDContentTypes.PRODUCT));
		YmalSet        ymalSet = getActiveYmalSet();
		ArrayList      finalList;
		int            size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalProducts());
		}
		
		removeDiscUnavProducts(ymals);
		removeSelf(ymals);
		removeSkus(ymals, removeSkus);

		size      = Math.min(ymals.size(), 6);      
		finalList = new ArrayList(size);
		// cut the list to be at most 6 items in size
		for (Iterator it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
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
	public List getYmalCategories() {
		LinkedHashSet  ymals   = new LinkedHashSet(getYmals(FDContentTypes.CATEGORY));
		YmalSet        ymalSet = getActiveYmalSet();
		ArrayList      finalList;
		int            size;
		
		if (ymalSet != null) {
			ymals.addAll(ymalSet.getYmalCategories());
		}
		
		size      = Math.min(ymals.size(), 6);      
		finalList = new ArrayList(size);
		// cut the list to be at most 6 items in size
		for (Iterator it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
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
	public List getYmalRecipes() {
		LinkedHashSet  ymals   = new LinkedHashSet(getYmals(FDContentTypes.RECIPE));
		YmalSet        ymalSet = getActiveYmalSet();
		ArrayList      finalList;
		int            size;
		
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
				finalList = new ArrayList(ymals.size());
				finalList.addAll(ymals);
			} else {
				Vector  v      = new Vector(ymals);
				Random  random = new Random();
				finalList      = new ArrayList(5);
				
				while (finalList.size() < 5) {
					int		ix = random.nextInt(v.size());
					Recipe  r  = (Recipe) v.get(ix);
					finalList.add(r);
					v.remove(ix);
				}
			}
		} else {
			size      = Math.min(ymals.size(), 6);      
			finalList = new ArrayList(size);
			// cut the list to be at most 6 items in size
			for (Iterator it = ymals.iterator(); it.hasNext() && size-- > 0; ) {
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


	public List getRelatedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "RELATED_RECIPES", relatedRecipes, false);
		return Collections.unmodifiableList(relatedRecipes);
	}

	public List<ProductModel> getProductBundle() {
		ContentNodeModelUtil.refreshModels(this, "PRODUCT_BUNDLE", productBundle, false);

		return new ArrayList(productBundle);
	}

	public List<CategoryModel> getHowtoCookitFolders() {
		ContentNodeModelUtil.refreshModels(this, "HOWTOCOOKIT_FOLDERS", howtocookitFolders, false);

		return new ArrayList(howtocookitFolders);
	}

	public CategoryModel getPrimaryHome() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("PRIMARY_HOME");
    
            return key == null ? null : (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key);
        }

	public SkuModel getPreferredSku() {
	    ContentKey key = (ContentKey) getCmsAttributeValue("PREFERRED_SKU");

	    return key == null ? null
             : (SkuModel) ContentFactory.getInstance().getContentNodeByKey(key);
	}

	public List<DomainValue> getRating() {
		ContentNodeModelUtil.refreshModels(this, "RATING", rating, false);

		return new ArrayList(rating);
	}

	public List<Domain> getUsageList() {
		ContentNodeModelUtil.refreshModels(this, "USAGE_LIST", usageList, false);

		return new ArrayList(usageList);
	}

	public DomainValue getUnitOfMeasure() {
		ContentKey key = (ContentKey) getCmsAttributeValue("UNIT_OF_MEASURE");
		
		return key == null
  	         ? null
             : (DomainValue) ContentFactory.getInstance().getContentNodeByKey(key);
	}

	public List<Domain> getVariationMatrix() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_MATRIX", variationMatrix, false);

		return new ArrayList(variationMatrix);
	}

	public List<Domain> getVariationOptions() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_OPTIONS", variationOptions, false);

		return new ArrayList(variationOptions);
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
	    return FDAttributeFactory.constructImage(this, "PROD_IMAGE_ROLLOVER");
	}

	public Html getProductAbout() {		
	    return FDAttributeFactory.constructHtml(this, "PRODUCT_ABOUT");
	}

	public Html getRecommendTable() {
	    return FDAttributeFactory.constructHtml(this, "RECOMMEND_TABLE");
	}

	public Html getProductQualityNote() {
            return FDAttributeFactory.constructHtml(this, "PRODUCT_QUALITY_NOTE");
	}

	public Html getProductDescriptionNote() {		
            return FDAttributeFactory.constructHtml(this, "PROD_DESCRIPTION_NOTE");
	}

	public Html getFreshTips() {
            return FDAttributeFactory.constructHtml(this, "FRESH_TIPS");
	}

        public List<Html> getDonenessGuide() {
            return FDAttributeFactory.constructWrapperList(this, "DONENESS_GUIDE");
        }

	public Html getFddefFrenching() {
            return FDAttributeFactory.constructHtml(this, "FDDEF_FRENCHING");
	}

	public Html getFddefGrade () {
	    return FDAttributeFactory.constructHtml(this, "FDDEF_GRADE");
	}

        public Html getFddefSource () {
            return FDAttributeFactory.constructHtml(this, "FDDEF_SOURCE");
        }
	
	

	public Html getFddefRipeness() {
            return FDAttributeFactory.constructHtml(this, "FDDEF_RIPENESS");
	}
	
	public boolean isHasSalesUnitDescription() {
	    return getCmsAttributeValue("SALES_UNIT_DESCRIPTION") != null;
	}

	public Html getSalesUnitDescription() {
            return FDAttributeFactory.constructHtml(this, "SALES_UNIT_DESCRIPTION");
	}

	public boolean isHasPartiallyFrozen() {
	    return getCmsAttributeValue("PARTIALLY_FROZEN") != null;
	}
	
	public Html getPartallyFrozen() {
            return FDAttributeFactory.constructHtml(this, "PARTIALLY_FROZEN");
	}

	public List<ComponentGroupModel> getComponentGroups() {
		ContentNodeModelUtil.refreshModels(this, "COMPONENT_GROUPS", componentGroups, false);

		return new ArrayList<ComponentGroupModel>(componentGroups);
	}

	public Html getProductTermsMedia() {
            return FDAttributeFactory.constructHtml(this, "PRODUCT_TERMS_MEDIA");
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

	public List getNewWineType() {
		ContentNodeModelUtil.refreshModels(this, "WINE_NEW_TYPE", wineNewTypes, false);
		return new ArrayList(wineNewTypes);		
	}

	public List getWineVintage() {
		ContentNodeModelUtil.refreshModels(this, "WINE_VINTAGE", wineVintages, false);
		return new ArrayList(wineVintages);		

	}
	
	
	public List getWineVarietal() {
		ContentNodeModelUtil.refreshModels(this, "WINE_VARIETAL", wineVarietals, false);
		return new ArrayList(wineVarietals);		
	}

	public List getNewWineRegion() {
		ContentNodeModelUtil.refreshModels(this, "WINE_NEW_REGION", wineRegions, false);
		return new ArrayList(wineRegions);		

	}

	public List getWineRating1() {
		ContentNodeModelUtil.refreshModels(this, "WINE_RATING1", wineRatings1, false);
		return new ArrayList(wineRatings1);		

	}

	public List getWineRating2() {
		ContentNodeModelUtil.refreshModels(this, "WINE_RATING2", wineRatings2, false);
		return new ArrayList(wineRatings2);		

	}

	public List getWineRating3() {
		ContentNodeModelUtil.refreshModels(this, "WINE_RATING3", wineRatings3, false);
		return new ArrayList(wineRatings3);		

	}

	public Html getWineReview1() {
            return FDAttributeFactory.constructHtml(this, "WINE_REVIEW1");
	}
	
	public int getExpertWeight() {
            Object value = FDAttributeFactory.constructWrapperValue(this, "SS_EXPERT_WEIGHTING");
            return value instanceof Number ? ((Number)value).intValue() : 0;
	}

	public Html getWineReview2() {
            return FDAttributeFactory.constructHtml(this, "WINE_REVIEW2");
	}

	public Html getWineReview3() {
            return FDAttributeFactory.constructHtml(this, "WINE_REVIEW3");
	}

	public Html getProductBottomMedia() {
            return FDAttributeFactory.constructHtml(this, "PRODUCT_BOTTOM_MEDIA");
	}

	public CategoryModel getPerfectPair() {
		ContentKey key = (ContentKey) getCmsAttributeValue("PERFECT_PAIR");
		
		return key == null
  	         ? null
             : (CategoryModel) ContentFactory.getInstance().getContentNodeByKey(key);

	}

	public List getWineClassifications() {
		List classifications = new ArrayList();
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
        	List skus = getPrimarySkus();
            SkuModel sku = null;
            // remove the unavailable sku's
            for (ListIterator li = skus.listIterator(); li.hasNext();) {
                sku = (SkuModel) li.next();
                if (sku.isUnavailable()) {
                    li.remove();
                }
            }
	        if (skus.size() == 0)
	            return rating; // skip this item..it has no skus. Hmmm?
	        if (skus.size() == 1) {
	            sku = (SkuModel) skus.get(0); // we only need one sku
	        } else {
	            sku = (SkuModel) Collections.max(skus, RATING_COMPARATOR);
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
                    StringTokenizer st = new StringTokenizer(_skuPrefixes, ","); // setup
                                                                                 // for
                                                                                 // splitting
                                                                                 // property
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
    	if (rating == EnumOrderLineRating.NO_RATING)
    		return "";
    	else
    		return rating.getStatusCodeInDisplayFormat();
    }
    
    public String getFreshnessGuaranteed() throws FDResourceException {
    	
    	String freshness = null;
    	if(!FDStoreProperties.IsFreshnessGuaranteedEnabled()) {
    		return null;
    	}
    	List skus = getSkus();
        SkuModel sku = null;
        // remove the unavailable sku's
        for (ListIterator li = skus.listIterator(); li.hasNext();) {
            sku = (SkuModel) li.next();
            if (sku.isUnavailable()) {
                li.remove();
            }
        }

        FDProductInfo productInfo = null;
        if (skus.size() == 0)
            return freshness; // skip this item..it has no skus. Hmmm?
        if (skus.size() == 1) {
            sku = (SkuModel) skus.get(0); // we only need one sku
        } else {
            sku = (SkuModel) Collections.max(skus, RATING_COMPARATOR);
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
	 * 
	 * 
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
}
