package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.EnumAvailabilityStatus;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public abstract class AbstractProductModelImpl extends ContentNodeModelImpl implements ProductModel {
    private static final long serialVersionUID = -5952286320303107200L;

    private List<ProductModel> alsoSoldAs = new ArrayList<ProductModel>();
	private List<ProductModel> alsoSoldAsList = new ArrayList<ProductModel>();
	private List<ProductModel> alsoSoldAsRefs = new ArrayList<ProductModel>();
	private List<TagModel> tags = new ArrayList<TagModel>();

    public AbstractProductModelImpl(ContentKey key) {
        super(key);
	}

	//
	// common implementation between Product and ConfiguredProduct
	//

	/*

	 FULL_NAME
	 GLANCE_NAME
	 NAV_NAME
	 BLURB
	 PROD_DESCR
	 PROD_IMAGE
	 PROD_IMAGE_CONFIRM
	 PROD_IMAGE_DETAIL
	 PROD_IMAGE_FEATURE
	 PROD_IMAGE_ZOOM
	 PROD_DESCRIPTION_NOTE
	 ALTERNATE_IMAGE
	 PRODUCT_QUALITY_NOTE
	 RELATED_PRODUCTS
	 ALSO_SOLD_AS

	 */

	private Image getImage(String key) {
            return FDAttributeFactory.constructImage(this, key, Image.BLANK_IMAGE);
	}

	@Override
    public Image getCategoryImage() {
		return getImage("PROD_IMAGE");
	}

	@Override
    public Image getConfirmImage() {
		return getImage("PROD_IMAGE_CONFIRM");
	}

	@Override
    public Image getDetailImage() {
		return getImage("PROD_IMAGE_DETAIL");
	}

	@Override
    public Image getThumbnailImage() {
		return getImage("PROD_IMAGE_FEATURE");
	}

	@Override
    public Image getZoomImage() {
		return getImage("PROD_IMAGE_ZOOM");
	}

	@Override
    public Image getPackageImage() {
		return getImage("PROD_IMAGE_PACKAGE");
	}

	@Override
    public int getTemplateType(int defaultValue) {
	    Integer i = (Integer) getCmsAttributeValue("TEMPLATE_TYPE");
	    return i != null ? i.intValue() : defaultValue;
	}

	@Override
    public ProductModel getAlsoSoldAs(int idx) {
		return getAlsoSoldAs().get(idx);
	}

	/**
	 * Can the product be recommended.
	 * @return if the EXCLUDED_RECOMMENDATION flag is set to true (default false)
	 */
	@Override
    public boolean isExcludedRecommendation() {
		return getAttribute("EXCLUDED_RECOMMENDATION", false);
	}

	/** Getter for property skus.
	 * @return Value of property skus.
	 */
	@Override
    public List<ProductModel> getAlsoSoldAs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAs, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAs);
		}

		ArrayList<ProductModel> refs = new ArrayList<ProductModel>();
		refs.addAll( alsoSoldAs );
		return refs;
	}

	@Override
    public List<ProductModel> getAlsoSoldAsRefs() {
		boolean bRefreshed = ContentNodeModelUtil.refreshModels(this, "ALSO_SOLD_AS", alsoSoldAsList, false);

		if (bRefreshed) {
			ContentNodeModelUtil.setNearestParentForProducts(getParentNode(), alsoSoldAsList);
			alsoSoldAsRefs.clear();
			alsoSoldAsRefs.addAll( alsoSoldAsList );
		}
		return alsoSoldAsRefs;
	}


	@Override
    public Html getProductDescription() {
	    return FDAttributeFactory.constructHtml(this, "PROD_DESCR");
	}

	@Override
    public boolean hasTerms() {
	    Object a = getCmsAttributeValue("PRODUCT_TERMS_MEDIA");
	    return a == null ? false : true;
	}

	@Override
    public Html getProductTerms() {
		return FDAttributeFactory.constructHtml(this, "PRODUCT_TERMS_MEDIA");
	}

	@Override
    public boolean isShowTopTenImage() {
		return getAttribute("SHOW_TOP_TEN_IMAGE", false);
	}

	@Override
    public PriceCalculator getPriceCalculator() {
	    return new PriceCalculator(getUserContext().getPricingContext(), this, this.getDefaultSku(getUserContext().getPricingContext()));
	}

    @Override
    public PriceCalculator getPriceCalculator(String skuCode) {
        return new PriceCalculator(getUserContext().getPricingContext(), this, this.getValidSkuCode(getUserContext().getPricingContext(), skuCode));
    }

    @Override
    public PriceCalculator getPriceCalculator(SkuModel sku) {
        return new PriceCalculator(getUserContext().getPricingContext(), this, sku);
    }

	@Override
	public PriceCalculator getPriceCalculator(PricingContext pricingContext) {
	    return new PriceCalculator(pricingContext, this, this.getDefaultSku(pricingContext));
	}

	@Override
	public PriceCalculator getPriceCalculator(String skuCode, PricingContext pricingContext) {
        return new PriceCalculator(pricingContext, this, this.getValidSkuCode(pricingContext, skuCode));
	}

	@Override
	public PriceCalculator getPriceCalculator(SkuModel sku, PricingContext pricingContext) {
        return new PriceCalculator(pricingContext, this, sku);
	}

    /**
     * @param skuCode
     * @return the default sku code, if sku code is not specified or the sku is
     *         not in the products sku.
     */
    @Override
    public SkuModel getValidSkuCode(PricingContext ctx, String skuCode) {
        if (skuCode == null) {
            return getDefaultSku(ctx);
        }
		for (SkuModel s : getSkus()) {
		    if (skuCode.equals(s.getSkuCode())) {
		        return s;
		    }
		}
		return getDefaultSku(ctx);
    }


    @Override
    public boolean isFullyAvailable() {
        return !(isHidden() || isUnavailable() || isOrphan() || isInvisible());
    }

	/**
	 * Better name would be : 'IsAvailable'.
	 */
	public boolean isDisplayable() {
		return isFullyAvailable();
	}

	/**
	 * temporary unavailable = unavailable but not discontinued and not hidden and not discontinued and not orphan and not invisible
	 * @return
	 */
	public boolean isTemporaryUnavailable() {
		return isUnavailable() && !isDiscontinued() && !isHidden() && !isOrphan() && !isInvisible() && !isOutOfSeason();
	}

	/**
	 * The product is temporary unavailable or available.
	 * @return
	 */
	@Override
    public boolean isTemporaryUnavailableOrAvailable() {
	    return !(isHidden() || isOrphan() || isInvisible() || isDiscontinued() || isOutOfSeason());
	}


    @Override
    public boolean isDisplayableBasedOnCms() {
        return !(isHidden() ||  isOrphan() || isInvisible());
    }

	@Override
    public boolean isNew() {
        Map<ContentKey, Map<String, Date>> newProducts = ContentFactory.getInstance().getNewProducts();
        if (newProducts.containsKey(getContentKey())) {
            return newProducts.get(getContentKey()).containsKey(getProductNewnessKey());
		} else {
			return false;
		}
	}

	@Override
	public double getNewAge() {
		return ContentFactory.getInstance().getNewProductAge(this);
	}

	@Override
	public Date getNewDate() {
        Map<ContentKey, Map<String, Date>> newProducts = ContentFactory.getInstance().getNewProducts();
        if (newProducts.containsKey(getContentKey())) {
            return newProducts.get(getContentKey()).get(getProductNewnessKey());
		} else {
			return null;
		}
	}

	private String getProductNewnessKey() {
		String key="";
		ZoneInfo zone=this!=null&&this.getUserContext()!=null&&this.getUserContext().getPricingContext()!=null?this.getUserContext().getPricingContext().getZoneInfo():null;
		if(zone!=null) {
			key=new StringBuilder(5).append(zone.getSalesOrg()).append(zone.getDistributionChanel()).toString();
		}
		return key;
	}
	@Override
	public boolean isBackInStock() {
		return ContentFactory.getInstance().getBackInStockProducts().containsKey(this);
	}

	@Override
	public double getBackInStockAge() {
		return ContentFactory.getInstance().getBackInStockProductAge(this);
	}

	@Override
	public Date getBackInStockDate() {
        Map<ContentKey, Map<String, Date>> newProducts = ContentFactory.getInstance().getBackInStockProducts();
        if (newProducts.containsKey(getContentKey())) {
            return newProducts.get(getContentKey()).get(getProductNewnessKey());
		} else {
			return null;
		}
	}

    @Override
    public boolean isGoingOutOfStock() {
    	boolean isToBeDiscontinuedSoon;
        try {
            FDProductInfo productInfo = FDCachedFactory.getProductInfo(getDefaultSkuCode());
            ZoneInfo zone = getUserContext().getPricingContext().getZoneInfo();
            EnumAvailabilityStatus availabilityStatus = productInfo.getAvailabilityStatus(zone.getSalesOrg(), zone.getDistributionChanel());
            isToBeDiscontinuedSoon = EnumAvailabilityStatus.TO_BE_DISCONTINUED_SOON == availabilityStatus;
        } catch (FDSkuNotFoundException e) {
            isToBeDiscontinuedSoon = false;
        } catch (FDResourceException e) {
            isToBeDiscontinuedSoon = false;
        }
        return isToBeDiscontinuedSoon;
    }

	@Override
	public double getAge() {
		if(this.isNew()){
			return this.getNewAge();
		}else if(this.isBackInStock()){
			return this.getBackInStockAge();
		}else {
			return -1;
		}
	}

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public int getDealPercentage() {
        return getDealPercentage(null);
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public int getTieredDealPercentage() {
        return getTieredDealPercentage(null);
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public int getHighestDealPercentage() {
        return getHighestDealPercentage(null);
    }

    /* price calculator calls */

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public String getDefaultPrice() {
        return getPriceCalculator().getDefaultPrice();
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    public String getDefaultPriceOnly() {
        return getPriceCalculator().getDefaultPriceOnly();
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    public String getDefaultUnitOnly() {
        return getPriceCalculator().getDefaultUnitOnly();
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
	public int getDealPercentage(String skuCode) {
	    return getPriceCalculator(skuCode).getDealPercentage();
	}

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
	public int getTieredDealPercentage(String skuCode) {
	    return getPriceCalculator(skuCode).getTieredDealPercentage();
	}

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
	public int getHighestDealPercentage(String skuCode) {
	    return getPriceCalculator(skuCode).getHighestDealPercentage();
	}

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public String getTieredPrice(double savingsPercentage) {
        return getPriceCalculator().getTieredPrice(savingsPercentage);
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
	public double getPrice(double savingsPercentage) {
	    return getPriceCalculator().getPrice(savingsPercentage);
	}

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public String getPriceFormatted(double savingsPercentage) {
        return getPriceCalculator().getPriceFormatted(savingsPercentage);
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public String getPriceFormatted(double savingsPercentage, String skuCode) {
        return getPriceCalculator(skuCode).getPriceFormatted(savingsPercentage);
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public String getWasPriceFormatted(double savingsPercentage) {
        return getPriceCalculator().getWasPriceFormatted(savingsPercentage);
    }

    /**
     * use priceCalculator which can be cached for a request.
     * @return
     */
    @Deprecated
    @Override
    public String getAboutPriceFormatted(double savingsPercentage) {
        return getPriceCalculator().getAboutPriceFormatted(savingsPercentage);
    }

    /* end of the price calculator calls */

	@Override
    public String getYmalHeader() {
		final YmalSet activeYmalSet = getActiveYmalSet();
		if (activeYmalSet != null && activeYmalSet.getProductsHeader() != null)
			return activeYmalSet.getProductsHeader();

		return getAttribute("RELATED_PRODUCTS_HEADER", null);
	}

	private String getPlantID() {
		return ContentFactory.getInstance().getCurrentUserContext().getFulfillmentContext().getPlantId();
	}
	@Override
    public List<String> getCountryOfOrigin() throws FDResourceException {
		List<String> coolInfo = new ArrayList<String>();
		List<SkuModel> skus = getPrimarySkus();
		// remove the unavailable sku's
		for ( ListIterator<SkuModel> li = skus.listIterator(); li.hasNext(); ) {
			SkuModel sku = li.next();
			if ( sku.isUnavailable() ) {
				li.remove();
			}
		}
		if ( skus.size() == 0 )
			return coolInfo; // skip this item..it has no skus. Hmmm?
		if ( skus.size() == 1 ) {
			SkuModel sku = skus.get( 0 ); // we only need one sku
			FDProductInfo productInfo;
			try {
				productInfo = sku.getProductInfo();
				List<String> countries = productInfo.getCountryOfOrigin(getPlantID());
				String text = getCOOLText( countries );
				if ( !"".equals( text ) )
					coolInfo.add( text );
			} catch ( FDSkuNotFoundException ignore ) {
			}
		} else {
			/*
			 * int MAX_COOL_COUNT=5; List countries=new ArrayList(MAX_COOL_COUNT); FDProductInfo productInfo; int
			 * index=0; while(index<MAX_COOL_COUNT && countries.size()<MAX_COOL_COUNT ) { for(Iterator
			 * it=skus.iterator();it.hasNext();) { sku = (SkuModel)it.next(); try { if(countries.size()<MAX_COOL_COUNT)
			 * { productInfo = FDCachedFactory.getProductInfo( sku.getSkuCode()); List
			 * _countries=productInfo.getCOOLInfo(); if(_countries.size()>index) countries.add(_countries.get(index)); }
			 * else { break; }
			 *
			 * } catch (FDSkuNotFoundException ignore) { } } index++; } text=getCOOLText(countries);
			 */
			Map<String, String> coolInfoMap = new HashMap<String, String>();
			FDProductInfo productInfo = null;
			List<String> countries = null;
			String text = "";
			for ( SkuModel sku : skus ) {
				if ( sku.getVariationMatrix() != null && sku.getVariationMatrix().size() > 0 ) {
					String domainValue = sku.getVariationMatrix().get( 0 ).getValue();
					try {
						productInfo = sku.getProductInfo();
						countries = productInfo.getCountryOfOrigin(getPlantID());
						text = getCOOLText( countries );
					} catch ( FDSkuNotFoundException ignore ) {
						text = "";
					}

					if ( !coolInfoMap.containsKey( domainValue ) && !"".equals( text ) )
						coolInfoMap.put( domainValue, text );
				}
			}

			StringBuffer temp = new StringBuffer( 100 );
			for ( String domainValue : coolInfoMap.keySet() ) {
				coolInfo.add( temp.append( domainValue ).append( ": " ).append( coolInfoMap.get( domainValue ).toString() ).toString() );
				temp = new StringBuffer( 100 );
			}
			Collections.sort( coolInfo );

		}
		return coolInfo;
	}

	public static String getCOOLText(List<String> countries) {
		if(countries==null)
			return "";

		StringBuffer temp=new StringBuffer(50);
		for(int i=0;i<countries.size();i++) {
			if(i<(countries.size()-2)){
				temp.append(countries.get(i).toString()).append(", ");
			}else if(countries.size()>1 && i==(countries.size()-1)){
				temp.append(" and ").append(countries.get(i).toString());
			} else {
				temp.append(countries.get(i));
			}
		}
		return temp.toString();
	}

	@Override
	public String getDefaultSkuCode() {
	    SkuModel sku = getDefaultSku();
	    return sku != null ? sku.getSkuCode() : null;
	}

	/*
	 * iPhone related
	 */
	@Override
	public Image getAlternateProductImage() {
	    return FDAttributeFactory.constructImage(this, "ALTERNATE_PROD_IMAGE");
	}

	@Override
	public boolean isHideIphone() {
	    return getAttribute("HIDE_IPHONE", false);
	}

	@Override
	public Set<DomainValue> getWineDomainValues() {
		Set<DomainValue> values = new HashSet<DomainValue>();
		values.add(getWineCountry());
		values.addAll(getNewWineRegion());
		values.addAll(getNewWineType());
		values.addAll(getWineVarietal());
		return values;
	}

	@Override
	public boolean isHideWineRatingPricing() {
		CategoryModel home = getPrimaryHome();
		if (home != null)
			return home.isHideWineRatingPricing();

		return false;
	}



    // =================================
    // YmalSetSource interface methods :
    // =================================

	/**
	 *  The list of YmalSet objects related to this product.
	 */
	private final List<YmalSet> ymalSets = new ArrayList<YmalSet>();


	@Override
	public List<YmalSet> getYmalSets() {
		return YmalSetSourceUtil.getYmalSets( this, ymalSets );
	}

	@Override
	public boolean hasActiveYmalSets() {
		return YmalSetSourceUtil.hasActiveYmalSets( this, ymalSets );
	}

	@Override
	public YmalSetSource getParentYmalSetSource() {
		return YmalSetSourceUtil.getParentYmalSetSource( this );
	}

	/**
	 * Returns the FDGroup that is associated with PRODUCT (any sku)
	 *    was : default sku in that Product.
	 * @return
	 */
	@Override
    public FDGroup getFDGroup() throws FDResourceException {
		FDGroup group = null;
		List<SkuModel> skus = getPrimarySkus();

		for ( SkuModel sku : skus ) {
			if(sku != null){
				try {
					FDProductInfo pInfo = sku.getProductInfo();
					group = pInfo.getGroup(getPriceCalculator().getPricingContext().getZoneInfo());
					if (group != null) {
						break;
					}
				} catch (FDSkuNotFoundException e) {
					//ignore
				}
			}
		}
		return group;
	}

	@Override
	public boolean isRetainOriginalSkuOrder() {
		return getAttribute("retainOriginalSkuOrder", false);
	}

	@Override
	public 	boolean showDefaultSustainabilityRating() {
		return getAttribute("DEFAULT_SUSTAINABILITY_RATING", false);
	}

	@Override
    public boolean isExcludedForEBTPayment(){
		return getAttribute("EXCLUDED_EBT_PAYMENT", false);
	}

	@Override
    public List<TagModel> getTags() {
		ContentNodeModelUtil.refreshModels(this, "tags", tags, false);
		return Collections.unmodifiableList(tags);
	}

	@Override
	public String getPageTitle(){
		return getAttribute("PAGE_TITLE", "FreshDirect");
	}

    @Override
    public String getFdxPageTitle(){
        return getAttribute("PAGE_TITLE_FDX", "Foodkick");
    }

	@Override
	public String getSEOMetaDescription(){
		return getAttribute("SEO_META_DESC", "Online grocer providing high quality fresh foods and popular grocery and household items at incredible prices delivered to the New York area.");
	}

	@Override
	public String getFdxSEOMetaDescription(){
	    return getAttribute("SEO_META_DESC_FDX", "FoodKick has just what you need for today and tomorrow. Order now for same-day delivery!");
	}
}
