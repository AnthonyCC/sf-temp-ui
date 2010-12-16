package com.freshdirect.fdstore.content;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.NVL;

public interface ProductModel extends ContentNodeModel, AvailabilityI, YmalSource, HasRedirectUrl, HasTemplateType {

	/** Orders products by department & full name */
	public final static Comparator<ProductModel> DEPTFULL_COMPARATOR = new Comparator<ProductModel>() {

		public int compare(ProductModel p1, ProductModel p2) {
			String p1Dept = "";
			String p2Dept = "";
			
			//check that getDepartment != null (fix for new prods in cmsTest)
			p1Dept = (p1.getDepartment() != null) ? NVL.apply(p1.getDepartment().getFullName(), "") : "";
			p2Dept = (p2.getDepartment() != null) ? NVL.apply(p2.getDepartment().getFullName(), "") : "";
			
			int ret = p1Dept.compareTo(p2Dept);

			if (ret == 0) {
				ret = p1.getFullName().compareTo(p2.getFullName());
			}
			
			return ret;
		}
	};

	public static class PriceComparator implements Comparator<SkuModel> {

		private int flips;

		public int compare(SkuModel sku1, SkuModel sku2) {
			try {
				String zoneId1 = sku1.getPricingContext().getZoneId();
				String zoneId2 = sku2.getPricingContext().getZoneId();
				FDProductInfo pi1 = FDCachedFactory.getProductInfo(sku1.getSkuCode());
				FDProductInfo pi2 = FDCachedFactory.getProductInfo(sku2.getSkuCode());
				if (pi1.getZonePriceInfo(zoneId1).getDefaultPrice() > 
								pi2.getZonePriceInfo(zoneId2).getDefaultPrice()) {
					flips++;
					return 1;
				} else if (pi1.getZonePriceInfo(zoneId1).getDefaultPrice() < pi2.getZonePriceInfo(zoneId2).getDefaultPrice()) {
					flips++;
					return -1;
				} else {
					return 0;
				}
			} catch (FDResourceException fdre) {
				// rethrow as a runtime exception
				throw new RuntimeException(fdre.getMessage());
			} catch (FDSkuNotFoundException fdsnfe) {
				// rethrow as a runtime exception
				throw new RuntimeException(fdsnfe.getMessage());
			}
		}

		public void reset() {
			flips = 0;
		}

		public boolean allTheSame() {
			return (flips == 0);
		}

	}
	
	
	public static class ZonePriceComparator implements Comparator<SkuModel> {
	    String zoneId;
	    
        public ZonePriceComparator(String zoneId) {
            this.zoneId = zoneId;
        }

        @Override
        public int compare(SkuModel sku1, SkuModel sku2) {
            try {
                FDProductInfo pi1 = FDCachedFactory.getProductInfo(sku1.getSkuCode());
                FDProductInfo pi2 = FDCachedFactory.getProductInfo(sku2.getSkuCode());
                if (pi1.getZonePriceInfo(zoneId).getDefaultPrice() > pi2.getZonePriceInfo(zoneId).getDefaultPrice()) {
                    return 1;
                } else if (pi1.getZonePriceInfo(zoneId).getDefaultPrice() < pi2.getZonePriceInfo(zoneId).getDefaultPrice()) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (FDResourceException e) {
                throw new RuntimeException(e);
            } catch (FDSkuNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
	}
	
	public final static Comparator<ProductModel> AGE_COMPARATOR = new Comparator<ProductModel>() {

		public int compare(ProductModel p1, ProductModel p2) {
			double age1;
			double age2;
			if(p1 != null)
				age1 = p1.getAge();
			else
				age1 = -1;
			
			if(p2 != null)
				age2 = p2.getAge();
			else
				age2 = -1;

			return new Double(age1).compareTo(new Double(age2));
		}
	};

	
	public static class ProductModelPriceComparator implements Comparator<ProductModel> {

	    final boolean inverse;
	    public ProductModelPriceComparator(boolean inverse) {
	        this.inverse = inverse;
	    }
	    
        public int compare(ProductModel model1, ProductModel model2) {
        	
            if (!model1.isFullyAvailable() && model2.isFullyAvailable()) {
            	return 1;
            }
            
            if (model1.isFullyAvailable() && !model2.isFullyAvailable()) {
            	return -1;
            }
            
            double price1 = 0;
            double price2 = 0;
            
            SkuModel sku1 = model1.getDefaultSku();
            if (sku1 != null) {
				String zoneId1 = sku1.getPricingContext().getZoneId();
                try {
                    price1 = sku1.getProductInfo().getZonePriceInfo(zoneId1).getDefaultPrice();
                } catch (FDResourceException e) {
                    e.printStackTrace();
                } catch (FDSkuNotFoundException e) {
                    e.printStackTrace();
                }
            }

            SkuModel sku2 = model2.getDefaultSku();
            if (sku2 != null) {
                String zoneId2 = sku2.getPricingContext().getZoneId();
                try {
                    price2 = sku2.getProductInfo().getZonePriceInfo(zoneId2).getDefaultPrice();
                } catch (FDResourceException e) {
                    e.printStackTrace();
                } catch (FDSkuNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            int result = Double.compare(price1, price2);
            if (inverse) {
                result = -result;
            }
            return result;
        }
    }

	
	
	public static class RatingComparator implements Comparator<SkuModel> {

		private int flips;

		public int compare(SkuModel obj1, SkuModel obj2) {
			try {
				FDProductInfo pi1 = FDCachedFactory.getProductInfo(obj1.getSkuCode());
				FDProductInfo pi2 = FDCachedFactory.getProductInfo(obj2.getSkuCode());
				
				EnumOrderLineRating oli1=EnumOrderLineRating.getEnumByStatusCode(pi1.getRating());
				EnumOrderLineRating oli2=EnumOrderLineRating.getEnumByStatusCode(pi2.getRating());
				
				
				if(oli1==null && oli2==null) return 0;
				
				if(oli1!=null && oli2==null) return 1;
				
				if(oli1==null && oli2!=null) return -1;
				
				if (oli1.getId()>oli2.getId()) {
					flips++;
					return 1;
				} 
				if (oli1.getId()<oli2.getId()) {
					flips++;
					return -1;
				}
				return 0;
			} catch (FDResourceException fdre) {
				// rethrow as a runtime exception
				throw new RuntimeException(fdre.getMessage());
			} catch (FDSkuNotFoundException fdsnfe) {
				// rethrow as a runtime exception
				throw new RuntimeException(fdsnfe.getMessage());
			}
		}

		public void reset() {
			flips = 0;
		}

		public boolean allTheSame() {
			return (flips == 0);
		}

	}

	/** Don't use allTheSame/reset, that's not thread-safe */
	public final static Comparator<SkuModel> PRICE_COMPARATOR = new ProductModel.PriceComparator();

    public final static Comparator<ProductModel> PRODUCT_MODEL_PRICE_COMPARATOR = new ProductModel.ProductModelPriceComparator(false);
    public final static Comparator<ProductModel> PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE = new ProductModel.ProductModelPriceComparator(true);
	
	
	/** Don't use allTheSame/reset, that's not thread-safe */
	public final static Comparator<SkuModel> RATING_COMPARATOR = new ProductModel.RatingComparator();

	public static NumberFormat CURRENCY_FORMAT = java.text.NumberFormat.getCurrencyInstance(Locale.US);

	
	// data model accessors
	
	public String getFullName();
	
	public String getGlanceName();
	
	public String getNavName();
	
	public String getAltText();
	
	public String getKeywords();
	
	public String getAka();
	
	public String getBlurb();
	
	public String getSubtitle();

	public String getHideUrl();
	
	public EnumProductLayout getProductLayout();
	
	public EnumProductLayout getProductLayout(EnumProductLayout defValue);
	
	public EnumLayoutType getLayout();
	
	public EnumTemplateType getTemplateType();
	
	public int getTemplateType(int defaultValue);
	
	public String getAlsoSoldAsName();
	
	public String getRedirectUrl();
	
	public String getSellBySalesunit();
	
	public String getSalesUnitLabel();
	
	public String getQuantityText();
	
	public String getQuantityTextSecondary();
	
	public String getServingSuggestion();
	
	public String getPackageDescription();
	
	public String getSeasonText();
	
	public String getWineFyi();
	
	public String getWineRegion();
	
	public String getSeafoodOrigin();
	
    public boolean isFullyAvailable();
    
    /**
     * The product is temporary unavailable or available.
     * @return
     */
    public boolean isTemporaryUnavailableOrAvailable();

    public boolean isDisplayableBasedOnCms();
	
	public boolean isShowSalesUnitImage();
	
	public boolean isNutritionMultiple();
	
	public boolean isNotSearchable();
	
	public Double getContainerWeightHalfPint();
	
	public Double getContainerWeightPint();
	
	public Double getContainerWeightQuart();
	
	public boolean isIncrementMaxEnforce();
	
	public float getQuantityMinimum();

	public float getQuantityMaximum();

	public float getQuantityIncrement();
	
	public boolean isInvisible();

	public boolean isPerishable();

	public boolean isFrozen();

	public boolean isGrocery();

	public String getProdPageRatings();
	
	public String getProdPageTextRatings();
	
	public String getRatingProdName();
	
	/*
	 * iPhone related
	 */
	public Image getAlternateProductImage();
	
	public boolean isHideIphone();
	
	
	/**
	 * Can the product be recommended?
	 * 
	 * @return whether the product is excluded from SmartStore recommendations
	 * @author istvan
	 */
	public boolean isExcludedRecommendation();

	
	/** Indexed getter for property skus.
	 * @param index Index of the property.
	 * @return Value of the property at <CODE>index</CODE>.
	 */
	public SkuModel getSku(int idx);

	public SkuModel getSku(String skuCode);

	/** Getter for property skus.
	 * @return a list of SkuModel objects.
	 */
	public List<SkuModel> getSkus();

	public List<String> getSkuCodes();
	
	
	/**
	 * 
	 * @return decides that this product model is in his primary home.
	 */
    public boolean isInPrimaryHome();
    
    /**
     * 
     * @return the product model, which is in his primary home
     */
    public ProductModel getPrimaryProductModel();
    
    /**
     * 
     * 
     * @return the list of sku models of the primary product. Primary product is the product which is in his primary home.
     */
    public List<SkuModel> getPrimarySkus();

	
	/**
	 * Get the source product.
	 * In case of a proxy product, the returned value is the (recursivelly :)) original
	 * product that was wrapped. For a "simple" product, it is itself.
	 * @return source product
	 */
	public ProductModel getSourceProduct();

	
	/** Getter for property brands.
	 * @return List of BrandModels that are referenced by the  property brands.
	 */
	public List<BrandModel> getBrands();

	/**
	 *  Tell if this product can be autoconfigured.
	 *  This is equivalent to saying it has only one SKU,
	 *  and only one possible configuration,
	 *  with regards to variation options and sales units.
	 *  
	 *  @return true if the product can be auto-configured,
	 *          false otherwise.
	 */
	public boolean isAutoconfigurable();

	/**
	 *  Return the auto-configuration for the product, if applicable.
	 *  
	 *  @return the configuration describing the auto-configuration of the
	 *          product, or null if the product can not be auto-configured.
	 */
	public FDConfigurableI getAutoconfiguration();

	/**
	 *  Return the active YmalSet, if any.
	 *  
	 *  @return the active YmalSet, or null if there's no active Ymal Set.
	 *  @see YmalSet
	 */
	public YmalSet getActiveYmalSet();

	/**
	 *  Return a list of products to display as cross-sell items.
	 *  
	 *  @return a list containing ProductModel, Recipe and CategoryModel
	 *          objects.
	 */
	public List<ContentNodeModel> getYmals();

	/**
	 *  Return a list of YMAL products.
	 *  
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List<ProductModel> getYmalProducts();
	
	/**
	 *  Return a list of YMAL products.
	 *  
	 *  @param removeSkus a set of FDSku objects, for which correspoding
	 *         products need to be removed from the final list of YMALs.
	 *         this might be null, in which case it has no effect. 
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List<ProductModel> getYmalProducts(Set<FDSku> removeSkus);
	
	/**
	 *  Return a list of YMAL categories.
	 *  
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List<CategoryModel> getYmalCategories();
	
	/**
	 *  Return a list of YMAL recipes.
	 *  
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List<Recipe> getYmalRecipes();
	
	/** 
	 * Return a list of recommended alternatives.
	 * @return a list of ProductModel objects 
	 */
	public List<ContentNodeModel> getRecommendedAlternatives();

	public List<ProductModel> getWeRecommendText();
	
	public List<ProductModel> getWeRecommendImage();
	
	/**
	 * @return List of Recipe
	 */
	public List<Recipe> getRelatedRecipes();
	
	public List<ProductModel> getProductBundle();

	
	public ProductModel getAlsoSoldAs(int idx);

	/** Getter for property skus.
	 * @return Value of property skus.
	 */
	public List<ProductModel> getAlsoSoldAs();

	
	public List<CategoryModel> getHowtoCookitFolders();
	
	public CategoryModel getPrimaryHome();
	
	public SkuModel getPreferredSku();
	
	public List<DomainValue> getRating();
	
	public List<Domain> getUsageList();
	
	public DomainValue getUnitOfMeasure();
	
	public List<Domain> getVariationMatrix();
	
	public List<Domain> getVariationOptions();
	
	public DomainValue getWineCountry();
	
	public ContentKey getWineCountryKey();
	
	public Image getProdImage();
	
	/** accessor for attribute PROD_IMAGE_CONFIRM */
	public Image getConfirmImage();

	/** accessor for attribute PROD_IMAGE_DETAIL */
	public Image getDetailImage();

	/** accessor for attribute PROD_IMAGE_FEATURE */
	public Image getFeatureImage();
	
	/** accessor for attribute PROD_IMAGE_ZOOM */
	public Image getZoomImage();

	public Image getRatingRelatedImage();
	
	public Image getAlternateImage();
	
	public Image getDescriptiveImage();
	
	/** accessor for attribute PROD_IMAGE_ROLLOVER */
	public Image getRolloverImage();

	public Html getProductAbout();
	
	public Html getProductDescription();

	public Html getRecommendTable();
	
	public Html getProductQualityNote();
	
	public Html getProductDescriptionNote();
	
	public Html getFreshTips();
	
	public List<Html> getDonenessGuide();
	
	public Html getFddefFrenching();
	
	public Html getFddefGrade();
	
	public Html getFddefSource();
	
	public Html getFddefRipeness();
	
	public boolean isHasSalesUnitDescription();
	
	public Html getSalesUnitDescription();
	
	public Html getPartallyFrozen();
	
	public boolean isHasPartiallyFrozen();
	
	
	public boolean hasComponentGroups();
	
	public List<ComponentGroupModel> getComponentGroups();
	
	
	public Html getProductTermsMedia();
	

	
	// other helpers


	public DepartmentModel getDepartment();

	public CategoryModel getCategory();
	
	public Image getCategoryImage();

	public Image getThumbnailImage();

	public boolean enforceQuantityMax();

	public List<ProductModel> getAlsoSoldAsRefs();

	public Object clone();

	public boolean isQualifiedForPromotions() throws FDResourceException;

	/**
	 * @return true if any of its skus is a platter
	 */
	public boolean isPlatter();

	public DayOfWeekSet getBlockedDays();

	public SkuModel getDefaultSku();
	
	public String getDefaultSkuCode();

	public SkuModel getDefaultSku(PricingContext context);
	
	
	/**
	 * @param type a multivalued ErpNutritionInfoType
	 * @return common Set of nutrition information of all available SKUs 
	 */
	public Set getCommonNutritionInfo(ErpNutritionInfoType type) throws FDResourceException;

	/**
	 * @return the brand that begins with the full name, empty string otherwise
	 */
	public String getPrimaryBrandName();

	/**
	 * @return the brand that begins with the specified name, empty string otherwise
	 */
	public String getPrimaryBrandName(String productName);

	public String getSizeDescription() throws FDResourceException;

	public String getKosherSymbol() throws FDResourceException;

	public String getKosherType() throws FDResourceException;

	public boolean isKosherProductionItem() throws FDResourceException;

	public int getKosherPriority() throws FDResourceException;

	public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config);
	
	public boolean isPreconfigured();
	
	public List getDisplayableBrands();

	public List getDisplayableBrands(int numberOfBrands);
	
	public boolean hasTerms();
	
	public Html getProductTerms() ;
	
	public boolean isShowTopTenImage();

	String getDefaultPrice();

	public boolean isSoldBySalesUnits();
	
	// for new wine store
	
	public String getWineClassification();
	
	public String getWineImporter();
	
	public String getWineAlchoholContent();
	
	public String getWineAging();
	
	public String getWineCity();
	
	public String getWineType();
	
	public List<DomainValue> getNewWineType();
	
	public List<DomainValue> getWineVarietal();
	
	public List<DomainValue> getWineVintage();
	
	public List<DomainValue> getNewWineRegion();
	
	public List<DomainValue> getWineRating1();
	
	public List<DomainValue> getWineRating2();
	
	public List<DomainValue> getWineRating3();

	public DomainValue getWineRatingValue1();
	
	public DomainValue getWineRatingValue2();
	
	public DomainValue getWineRatingValue3();
	
	public boolean hasWineOtherRatings();
	
	public Html getWineReview1();
	
	public Html getWineReview2();
	
	public Html getWineReview3();
	
	public Html getProductBottomMedia();
	
	public CategoryModel getPerfectPair();		
	
	public List getWineClassifications();

	public String getProductRating() throws FDResourceException; 
	
	public String getProductRating(String skuCode) throws FDResourceException; 

	public EnumOrderLineRating getProductRatingEnum() throws FDResourceException; 
	
	public String getFreshnessGuaranteed() throws FDResourceException; 

	public boolean isNew();
	
	public Date getNewDate();
	
	public double getNewAge();
	
	public boolean isBackInStock();
	
	public Date getBackInStockDate();
	
	public double getBackInStockAge();
	
	public double getAge();
	
	public int getDealPercentage();

	public int getDealPercentage(String skuCode);
	
	public int getTieredDealPercentage();

	public int getTieredDealPercentage(String skuCode);

	public int getHighestDealPercentage();

	public int getHighestDealPercentage(String skuCode);
	
	public String getTieredPrice(double savingsPercentage);
	
	public double getPrice(double savingsPercentage);

	public String getPriceFormatted(double savingsPercentage);
	
	public String getPriceFormatted(double savingsPercentage, String skuCode);
	
	public String getWasPriceFormatted(double savingsPercentage);
	
	public String getAboutPriceFormatted(double savingsPercentage);
	
	public int getExpertWeight();
	
	public List<String> getCountryOfOrigin() throws FDResourceException;
	
	//Gift Card changes
	public List getGiftcardType();
	
	public PricingContext getPricingContext();

	/**
	 * @param skuCode
	 * @return the default sku code, if sku code is not specified or the sku is not in the products sku, otherwise the
	 *         specified SkuModel.
	 */
	public SkuModel getValidSkuCode( PricingContext ctx, String skuCode );

	/**
	 * 
	 * @return a price calculator which encapsulates the default sku, and the pricing context. If there is no default
	 *         sku, it returns a pricing calculator which returns sensible defaults.
	 */
	public PriceCalculator getPriceCalculator();

	/**
	 * 
	 * @return a price calculator which encapsulates the default sku, if the given skuCode is invalid, otherwise the sku
	 *         which is specified, and the pricing context.
	 */
	public PriceCalculator getPriceCalculator( String skuCode );

	/**
	 * This is used for getting a media attribute, if the usage of the normal getters are not feasible. For example,
	 * when the name of the attribute comes from the client side. It's not a very fortunate situation.
	 * 
	 * @param name
	 * @return
	 */
	public MediaI getMedia( String name );

	public Set<DomainValue> getWineDomainValues();

	public boolean isHideWineRatingPricing();
	
	/**
	 * Should show wine ratings
	 * @return
	 */
	public boolean isShowWineRatings();
}
