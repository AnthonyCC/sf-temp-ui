/*
 * Created on Aug 3, 2005
 */
package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.fdstore.FDContentTypes;
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
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 */
public class ProductModelImpl extends AbstractProductModelImpl {
	private static final long serialVersionUID = 2103318183933323914L;

	private static final Logger LOG = LoggerFactory.getInstance( ProductModelImpl.class ); 

	private List skuModels = new ArrayList();
	
	private final List brandModels = new ArrayList();
	
	private final List weRecommendText = new ArrayList();

	private final List weRecommendImage = new ArrayList();

	private final List relatedRecipes = new ArrayList();

	private final List productBundle = new ArrayList();

	private final List howtocookitFolders = new ArrayList();

	private final List rating = new ArrayList();

	private final List usageList = new ArrayList();

	private final List variationMatrix = new ArrayList();

	private final List variationOptions = new ArrayList();
	
	private final List componentGroups = new ArrayList();
	
	private final List recommendedAlternatives = new ArrayList();
	
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
	
	public ProductModelImpl(ContentKey cKey) {
		super(cKey);
	}

	public ProductRef getProductRef() {
		return new ProductRef(this.getParentNode().getContentName(), this.getContentName());
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
	public List getSkus() {
		ContentNodeModelUtil.refreshModels(this, "skus", skuModels, true);

		return new ArrayList(skuModels);
	}

	public List getSkuCodes() {
		List skus = getSkus();
		List skuCodeList = new ArrayList(skus.size());
		for (Iterator i = skus.iterator(); i.hasNext();) {
			skuCodeList.add(((SkuModel) i.next()).getSkuCode());
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
	 */
	public FDConfigurableI getAutoconfiguration() {
		FDConfigurableI ret = null;
		SkuModel sku = getDefaultSku();
		if (sku != null) {
			try {
				boolean soldBySalesUnits = isSoldBySalesUnits();
				if (sku.getProduct().isAutoconfigurable(soldBySalesUnits)) {
					ret = sku.getProduct().getAutoconfiguration(soldBySalesUnits, getQuantityMinimum());
				}
			} catch(Exception exc) {}
		}
		return ret;
	}
	
	/** Getter for property brands.
	 * @return List of BrandModels that are referenced by the  property brands.
	 */
	public List getBrands() {
		ContentNodeModelUtil.refreshModels(this, "brands", brandModels, true);

		List newList = new ArrayList();
		for (int i = 0; i < brandModels.size(); i++) {
			BrandModel b = (BrandModel) brandModels.get(i);
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
		return "SALES_UNIT".equals(getAttribute("SELL_BY_SALESUNIT", ""));
	}

	public boolean isQualifiedForPromotions() throws FDResourceException {
		List skus = getSkus();
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
		List skus = getSkus();
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
		List skus = getSkus();
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
		List skus = getSkus();
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
		List skus = getSkus();
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
		List skus = getSkus();
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
		List skus = getSkus();
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
		List skus = getSkus();
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
		List skus = getSkus();
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
	 *  Returns the preferred SKU for a product if defined.
	 *  Otherwise returns the minimum price SKU among the available SKUs for
	 *  the product. If the product only has one available SKU, that will be
	 *  returned.
	 *  
	 *  @return the preferred SKU for the product, or the minimally priced
	 *          SKU that is available. if no SKUs are available, returns null.
	 */
	public SkuModel getDefaultSku() {
		List skus = this.getSkus();

		AttributeI a = getCmsAttribute("PREFERRED_SKU");
		ContentKey preferredSku = a == null ? null : (ContentKey) a.getValue();

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
		return (SkuModel) Collections.min(skus, PRICE_COMPARATOR);
	}

	/**
	 * @param type a multivalued ErpNutritionInfoType
	 * @return common Set of nutrition information of all available SKUs 
	 */
	public Set getCommonNutritionInfo(ErpNutritionInfoType type) throws FDResourceException {
		List skus = getSkus();
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
			MediaI brandLogoSmall = (MediaI)b.getAttribute("BRAND_LOGO_SMALL",(Object)null);
			if (brandLogoSmall!=null) {
				displayableBrands.add(prodBrands.get(0));
				numberOfBrands--;
			}
		}
		
		// now get the remaining brands from the sku
		List skus = getSkus();
		for (Iterator skuItr = skus.iterator();skuItr.hasNext() && numberOfBrands >0;) {
			SkuModel sku = (SkuModel)skuItr.next();
			List skuBrands = sku.getBrands();
			boolean gotOne = false;
			for (Iterator skbItr=skuBrands.iterator();skbItr.hasNext() && !gotOne; ) {
				BrandModel b = (BrandModel) skbItr.next();
				MediaI brandLogoSmall = (MediaI)b.getAttribute("BRAND_LOGO_SMALL",(Object)null);
				if (brandLogoSmall!=null && !displayableBrands.contains(b) && !sku.isUnavailable()) {
					numberOfBrands--;
					displayableBrands.add(b);
					gotOne=true;
				}
			}
		}
		return displayableBrands;
	}
	
	
	public String getSizeDescription() throws FDResourceException {
		try {
			SkuModel sku = getDefaultSku();
			if ( sku == null )
				return null;
			
			FDProduct pr = sku.getProduct();
			FDSalesUnit[] sus = pr.getSalesUnits();
			if (sus.length == 1) {
				String salesUnitDescr = sus[0].getDescription();

				// clean sales unit description
				if (salesUnitDescr != null) {
					if (salesUnitDescr.indexOf("(") > -1) {
						salesUnitDescr = salesUnitDescr.substring(0, salesUnitDescr.indexOf("("));
					}
					salesUnitDescr = salesUnitDescr.trim();
					// empty descriptions, "nm" and "ea" should be ignored
					if ((!"".equals(salesUnitDescr))
						&& (!"nm".equalsIgnoreCase(salesUnitDescr))
						&& (!"ea".equalsIgnoreCase(salesUnitDescr))) {
						if (!this.getAttribute("SELL_BY_SALESUNIT", "").equals("SALES_UNIT")) {
							return salesUnitDescr;
						}
					}
				}

			}
		} catch (FDSkuNotFoundException ex) {
		}
		return null;
	}

	public String getKosherSymbol() throws FDResourceException {
		try {
			SkuModel sku = getDefaultSku();
			if ( sku == null )
				return "";
			
			FDProduct pr = sku.getProduct();
			FDKosherInfo ki = pr.getKosherInfo();
			if (ki.hasKosherSymbol() && ki.getKosherSymbol().display()) {
				return ki.getKosherSymbol().getCode();
			} else {
				return "";
			}
		} catch (FDSkuNotFoundException fdsnfe) {
		}
		return "";
	}

	public String getKosherType() throws FDResourceException {
		try {
			SkuModel sku = getDefaultSku();
			if ( sku == null )
				return "";
			
			FDProduct pr = sku.getProduct();
			FDKosherInfo ki = pr.getKosherInfo();
			if (ki.hasKosherType() && ki.getKosherType().display()) {
				return ki.getKosherType().getName();
			} else {
				return "";
			}
		} catch (FDSkuNotFoundException fdsnfe) {
		}
		return "";
	}

	public boolean isKosherProductionItem() throws FDResourceException {
		try {
			SkuModel sku = getDefaultSku();
			if ( sku == null )
				return false;
			
			FDProduct pr = sku.getProduct();
			FDKosherInfo ki = pr.getKosherInfo();
			return ki.isKosherProduction();
		} catch (FDSkuNotFoundException fdsnfe) {
		}
		return false;
	}

	public int getKosherPriority() throws FDResourceException {
		try {
			SkuModel sku = getDefaultSku();
			if ( sku == null )
				return 999;
			
			FDProduct pr = sku.getProduct();
			FDKosherInfo ki = pr.getKosherInfo();
			return ki.getPriority();
		} catch (FDSkuNotFoundException fdsnfe) {
		}
		return 999;
	}

	public boolean hasComponentGroups() {
		return !((List) getAttribute("COMPONENT_GROUPS", Collections.EMPTY_LIST)).isEmpty();
	}

	public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config) {
		List compositeGroups = (List) getAttribute("COMPONENT_GROUPS", Collections.EMPTY_LIST);
		try {
			for (Iterator cgItr = compositeGroups.iterator(); cgItr.hasNext();) {
				ComponentGroupModel cgm = (ComponentGroupModel) cgItr.next();
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
		return EnumProductLayout.getLayoutType(this.getAttribute("PRODUCT_LAYOUT", EnumProductLayout.PERISHABLE.getId()));
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
		return getAttribute("REDIRECT_URL", "");
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
		return getAttribute("QUANTITY_TEXT_SECONDARY", "");
	}

	public String getServingSuggestion() {
		return getAttribute("SERVING_SUGGESTION", "");
	}

	public String getPackageDescription() {
		return getAttribute("PACKAGE_DESCRIPTION", "");
	}

	public String getSeasonText() {
		return getAttribute("SEASON_TEXT", "");
	}

	public String getWineFyi() {
		return getAttribute("WINE_FYI", "");
	}

	public String getWineRegion() {
		return getAttribute("WINE_REGION", "");
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

	public double getContainerWeightHalfPint() {
		return new Double(getAttribute("CONTAINER_WEIGHT_HALF_PINT", 0.0)).doubleValue();
	}

	public double getContainerWeightPint() {
		return new Double(getAttribute("CONTAINER_WEIGHT_PINT", 0.0)).doubleValue();
	}

	public double getContainerWeightQuart() {
		return new Double(getAttribute("CONTAINER_WEIGHT_QUART", 0.0)).doubleValue();
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
		List ymalSets = getYmalSets();
		for (Iterator it = ymalSets.iterator(); it.hasNext(); ) {
			YmalSet     ymalSet = (YmalSet) it.next();
			
			if (ymalSet.isActive()) {
				return ymalSet;
			}
		}
	
		return null;
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
	public List getRecommendedAlternatives() {
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

	public List getWeRecommendText() {
		ContentNodeModelUtil.refreshModels(this, "WE_RECOMMEND_TEXT", weRecommendText, false);

		return new ArrayList(weRecommendText);
	}

	public List getWeRecommendImage() {
		ContentNodeModelUtil.refreshModels(this, "WE_RECOMMEND_IMAGE", weRecommendImage, false);

		return new ArrayList(weRecommendImage);
	}


	public List getRelatedRecipes() {
		ContentNodeModelUtil.refreshModels(this, "RELATED_RECIPES", relatedRecipes, false);
		return Collections.unmodifiableList(relatedRecipes);
	}

	public List getProductBundle() {
		ContentNodeModelUtil.refreshModels(this, "PRODUCT_BUNDLE", productBundle, false);

		return new ArrayList(productBundle);
	}

	public List getHowtoCookitFolders() {
		ContentNodeModelUtil.refreshModels(this, "HOWTOCOOKIT_FOLDERS", howtocookitFolders, false);

		return new ArrayList(howtocookitFolders);
	}

	public CategoryModel getPrimaryHome() {
            AttributeI attribute = getCmsAttribute("PRIMARY_HOME");
            if (attribute==null) {
                return null;
            }
            ContentKey key = (ContentKey) attribute.getValue();
    
            return key == null ? null : (CategoryModel) ContentFactory.getInstance().getContentNode(key.getId());
        }

	public SkuModel getPreferredSku() {
		AttributeI att = getCmsAttribute("PREFERRED_SKU");
		if (att == null) return null;
		ContentKey key = (ContentKey) att.getValue();
		
		return key == null
  	         ? null
             : (SkuModel) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public List getRating() {
		ContentNodeModelUtil.refreshModels(this, "RATING", rating, false);

		return new ArrayList(rating);
	}

	public List getUsageList() {
		ContentNodeModelUtil.refreshModels(this, "USAGE_LIST", usageList, false);

		return new ArrayList(usageList);
	}

	public DomainValue getUnitOfMeasure() {
		ContentKey key = (ContentKey) getCmsAttribute("UNIT_OF_MEASURE").getValue();
		
		return key == null
  	         ? null
             : (DomainValue) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public List getVariationMatrix() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_MATRIX", variationMatrix, false);

		return new ArrayList(variationMatrix);
	}

	public List getVariationOptions() {
		ContentNodeModelUtil.refreshModels(this, "VARIATION_OPTIONS", variationOptions, false);

		return new ArrayList(variationOptions);
	}

	public DomainValue getWineCountry() {		
		Attribute a = getAttribute("WINE_COUNTRY");
		return a == null ? null : ((DomainValueRef) a.getValue()).getDomainValue();		
	}
			

	public Image getProdImage() {
		return (Image) getAttribute("PROD_IMAGE", (Image) null); 
	}

	public Image getFeatureImage() {
		ContentKey key = (ContentKey) getCmsAttribute("PROD_IMAGE_FEATURE").getValue();
		
		return key == null
  	         ? null
             : (Image) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Image getRatingRelatedImage() {
		ContentKey key = (ContentKey) getCmsAttribute("RATING_RELATED_IMAGE").getValue();
		
		return key == null
  	         ? null
             : (Image) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Image getAlternateImage() {
		return (Image) getAttribute("ALTERNATE_IMAGE", (Image) null); 
	}

	public Image getDescriptiveImage() {
		return (Image) getAttribute("DESCRIPTIVE_IMAGE", (Image) null); 
	}

	public Image getRolloverImage() {

		AttributeI attr = getCmsAttribute("PROD_IMAGE_ROLLOVER");
		if ( attr == null )
			return null;
		
		Attribute attr2 = FDAttributeFactory.getAttribute( attr );
		if ( attr2 == null )
			return null;
		
		return (Image) attr2.getValue();
	}

	public Html getProductAbout() {		
		Attribute a = getAttribute("PRODUCT_ABOUT");
		return a == null ? null : (Html) a.getValue();		
	}

	public Html getRecommendTable() {
		ContentKey key = (ContentKey) getCmsAttribute("RECOMMEND_TABLE").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getProductQualityNote() {
		ContentKey key = (ContentKey) getCmsAttribute("PRODUCT_QUALITY_NOTE").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getProductDescriptionNote() {		
		Attribute a = getAttribute("PROD_DESCRIPTION_NOTE");
		return a == null ? null : (Html) a.getValue();			
	}

	public Html getFreshTips() {
		ContentKey key = (ContentKey) getCmsAttribute("FRESH_TIPS").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getDonenessGuide() {
		ContentKey key = (ContentKey) getCmsAttribute("DONENESS_GUIDE").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getFddefFrenching() {
		ContentKey key = (ContentKey) getCmsAttribute("FDDEF_FRENCHING").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getFddefGrade() {
		ContentKey key = (ContentKey) getCmsAttribute("FDDEF_GRADE").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getFddefRipeness() {
		ContentKey key = (ContentKey) getCmsAttribute("FDDEF_RIPENESS").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getSalesUnitDescription() {
		ContentKey key = (ContentKey) getCmsAttribute("SALES_UNIT_DESCRIPTION").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public Html getPartallyFrozen() {
		ContentKey key = (ContentKey) getCmsAttribute("PARTIALLY_FROZEN").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
	}

	public List getComponentGroups() {
		ContentNodeModelUtil.refreshModels(this, "COMPONENT_GROUPS", componentGroups, false);

		return new ArrayList(componentGroups);
	}

	public Html getProductTermsMedia() {
		ContentKey key = (ContentKey) getCmsAttribute("PRODUCT_TERMS_MEDIA").getValue();
		
		return key == null
  	         ? null
             : (Html) ContentFactory.getInstance().getContentNode(key.getId());
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
		return getAttribute("WINE_TYPE", "");
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
		Attribute a = getAttribute("WINE_REVIEW1");
		return a == null ? null : (Html) a.getValue();			
	}
	
	public int getExpertWeight() {
		Attribute a = getAttribute("SS_EXPERT_WEIGHTING");
		return a == null ? 0 : ((Number)a.getValue()).intValue();
	}

	public Html getWineReview2() {
		Attribute a = getAttribute("WINE_REVIEW2");
		return a == null ? null : (Html) a.getValue();			
	}

	public Html getWineReview3() {
		Attribute a = getAttribute("WINE_REVIEW3");
		return a == null ? null : (Html) a.getValue();					
	}

	public Html getProductBottomMedia() {
		Attribute a = getAttribute("PRODUCT_BOTTOM_MEDIA");
		return a == null ? null : (Html) a.getValue();		
	}

	public CategoryModel getPerfectPair() {
		AttributeI a = getCmsAttribute("PERFECT_PAIR");
		if(a==null)
		   return null;
		ContentKey key = (ContentKey) a.getValue();
		
		return key == null
  	         ? null
             : (CategoryModel) ContentFactory.getInstance().getContentNode(key.getId());

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
	public String getProductRating() throws FDResourceException {
 	   String rating="";
	   
	   List skus = getSkus(); 
       SkuModel sku = null;
       //remove the unavailable sku's
       for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
           sku = (SkuModel)li.next();
           if ( sku.isUnavailable() ) {
              li.remove();
           }
       }

       FDProductInfo productInfo = null;
       if (skus.size()==0) return rating;  // skip this item..it has no skus.  Hmmm?
       if (skus.size()==1) {
           sku = (SkuModel)skus.get(0);  // we only need one sku
       }
       else {
           sku = (SkuModel) Collections.max(skus, RATING_COMPARATOR);
       }
       if (sku!=null && sku.getSkuCode() != null) {
           //
           // get the FDProductInfo from the FDCachedFactory
           //
           try {
				/*
				 * grab property to determine which sku prefixes to display ratings for
				 * 
				 * assuming some prefixes are set, loop, checking to see if the current product's
				 * sku prefix matches an allowed prefix.
				 * 
				 * exit loop on a match.
				 */
        	   
				/*
				 * There is a similar setup in the GetPeakProduceTag.java file
				 * and in ProductModelImpl.java
				 */
//				LOG.debug("===== in getProductRating in ProductModelImpl :"+sku.getSkuCode());
        		  
				// grab sku prefixes that should show ratings
				String _skuPrefixes=FDStoreProperties.getRatingsSkuPrefixes();
//				LOG.debug("* getRatingsSkuPrefixes :"+_skuPrefixes);
    	   
				//if we have prefixes then check them
				if (_skuPrefixes!=null && !"".equals(_skuPrefixes)) {
					StringTokenizer st=new StringTokenizer(_skuPrefixes, ","); //setup for splitting property
					String curPrefix = ""; //holds prefix to check against
					String spacer="* "; //spacing for sysOut calls
					boolean matchFound = false;
					
					//loop and check each prefix
					while(st.hasMoreElements()) {
						
						curPrefix=st.nextToken();
//						LOG.debug(spacer+"Rating _skuPrefixes checking :"+curPrefix);
						
						//if prefix matches get product info
						if(sku.getSkuCode().startsWith(curPrefix)) {
                            productInfo = FDCachedFactory.getProductInfo( sku.getSkuCode());
//							LOG.debug(" Rating productInfo :"+productInfo);
							String tmpRating = productInfo.getRating();
   	
                   if(tmpRating!=null && tmpRating.trim().length()>0){
                	   EnumOrderLineRating enumRating=EnumOrderLineRating.getEnumByStatusCode(tmpRating);
//								LOG.debug(" enumRating :"+enumRating);
								
                	   if(enumRating!=null && enumRating.isEligibleToDisplay()){
                		   rating=enumRating.getStatusCodeInDisplayFormat();
//									LOG.debug(" rating in display format  :"+rating);
                	   }
                   }
							matchFound=true;
        	   } 
						//exit on matched sku prefix
//						LOG.debug(spacer+"Rating matchFound :"+matchFound);
						if (matchFound) { break; }
						spacer=spacer+"   ";
					}
				}

	        } catch (FDSkuNotFoundException ignore) {

           }
       }
      
       return rating;

	}
}
