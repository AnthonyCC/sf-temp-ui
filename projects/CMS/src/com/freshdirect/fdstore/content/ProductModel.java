package com.freshdirect.fdstore.content;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.util.ProductInfoUtil;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.NVL;

public interface ProductModel extends AvailabilityI, YmalSource, YmalSetSource, HasRedirectUrl, HasTemplateType {

	/** Orders products by department & full name */
	public final static Comparator<ProductModel> DEPTFULL_COMPARATOR = new Comparator<ProductModel>() {

		@Override
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

	public final static Comparator<ProductModel> FULL_NAME_PRODUCT_COMPARATOR = new Comparator<ProductModel>() {
		@Override
        public int compare(ProductModel p1, ProductModel p2) {
			String name1 = NVL.apply(p1.getFullName().toLowerCase(), "");
			String name2 = NVL.apply(p2.getFullName().toLowerCase(), "");

			int d = name1.compareTo(name2);
			if (d != 0)
				return d;
			else
				return p1.getContentKey().getId().compareTo(p2.getContentKey().getId());
		}
	};
	
	public static abstract class SkuInfoComparator implements Comparator<SkuModel> {
				
		@Override
        public int compare(SkuModel sku1, SkuModel sku2) {
			
			if(sku1==null && sku2==null) return 0;
			
			if(sku1!=null && sku2==null) return 1;
			
			if(sku1==null && sku2!=null) return -1;

			FDProductInfo pi1 = null;
			FDProductInfo pi2 = null;
			try {
				pi1 = FDCachedFactory.getProductInfo(sku1.getSkuCode());
			} catch (FDResourceException fdre) {
				handle(fdre);				
			} catch (FDSkuNotFoundException fdsnfe) {
				handle(fdsnfe);				
			} 			

			try {
				pi2 = FDCachedFactory.getProductInfo(sku2.getSkuCode());
			} catch (FDResourceException fdre) {
				handle(fdre);				
			} catch (FDSkuNotFoundException fdsnfe) {
				handle(fdsnfe);				
			} 			

			if(FDStoreProperties.isDeveloperDisableAvailabilityLookup()) {
				if(pi1==null && pi2==null) return 0;
				
				if(pi1!=null && pi2==null) return 1;
				
				if(pi1==null && pi2!=null) return -1;				
			}
			
			return doCompare(sku1, sku2, pi1, pi2);							
		}

		private void handle(Exception e) {
			if(! FDStoreProperties.isDeveloperDisableAvailabilityLookup()) {
				// rethrow as a runtime exception
				throw new RuntimeException(e.getMessage());
			}			
		}
		
		protected abstract int doCompare(SkuModel sku1, SkuModel sku2, FDProductInfo pi1, FDProductInfo pi2);
	}
	
	public static class PriceComparator extends SkuInfoComparator {

		private int flips;

		@Override
		public int doCompare(SkuModel sku1, SkuModel sku2, FDProductInfo pi1, FDProductInfo pi2) {
				ZoneInfo zoneId1 = sku1.getPricingContext().getZoneInfo();
				ZoneInfo zoneId2 = sku2.getPricingContext().getZoneInfo();
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
		}

		public void reset() {
			flips = 0;
		}

		public boolean allTheSame() {
			return (flips == 0);
		}

	}
	
	
	public static class ZonePriceComparator extends SkuInfoComparator {
		ZoneInfo zoneInfo;
	    
        public ZonePriceComparator(ZoneInfo zoneInfo) {
            this.zoneInfo = zoneInfo;
        }

        @Override
        public int doCompare(SkuModel sku1, SkuModel sku2, FDProductInfo pi1, FDProductInfo pi2) {
                if (pi1.getZonePriceInfo(zoneInfo).getDefaultPrice() > pi2.getZonePriceInfo(zoneInfo).getDefaultPrice()) {
                    return 1;
                } else if (pi1.getZonePriceInfo(zoneInfo).getDefaultPrice() < pi2.getZonePriceInfo(zoneInfo).getDefaultPrice()) {
                    return -1;
                } else {
                    return 0;
                }
        }
	}
	
	public final static Comparator<ProductModel> AGE_COMPARATOR = new Comparator<ProductModel>() {

		@Override
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
	    
        @Override
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
            	ZoneInfo zoneId1 = sku1.getPricingContext().getZoneInfo();
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
                ZoneInfo zoneId2 = sku2.getPricingContext().getZoneInfo();
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

	
	
	public static class RatingComparator extends SkuInfoComparator {

		private int flips;

		@Override
		public int doCompare(SkuModel obj1, SkuModel obj2, FDProductInfo pi1, FDProductInfo pi2) {
				String plantID1=ProductInfoUtil.getPickingPlantId(pi1);
				String plantID2=ProductInfoUtil.getPickingPlantId(pi2);
				EnumOrderLineRating oli1=pi1.getRating(plantID1);
				EnumOrderLineRating oli2=pi2.getRating(plantID2);
				
				
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
		}

		public void reset() {
			flips = 0;
		}

		public boolean allTheSame() {
			return (flips == 0);
		}

	}

	public static class SustainabilityComparator extends SkuInfoComparator {

		private int flips;

		@Override
        public int doCompare(SkuModel obj1, SkuModel obj2, FDProductInfo pi1, FDProductInfo pi2) {

				String plantID1=ProductInfoUtil.getPickingPlantId(pi1);
				String plantID2=ProductInfoUtil.getPickingPlantId(pi2);
				EnumSustainabilityRating oli1=pi1.getSustainabilityRating(plantID1);
				EnumSustainabilityRating oli2=pi2.getSustainabilityRating(plantID2);
				
				
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
	
	/** Don't use allTheSame/reset, that's not thread-safe */
	public final static Comparator<SkuModel> SUSTAINABILITY_COMPARATOR = new ProductModel.SustainabilityComparator();
	
	public static final Comparator<ProductModel> GENERIC_PRICE_COMPARATOR = new Comparator<ProductModel>() {
		@Override
		public int compare(ProductModel p1, ProductModel p2) {
			double pv1 = p1.getPriceCalculator().getDefaultPriceValue();
			double pv2 = p2.getPriceCalculator().getDefaultPriceValue();
			return Double.compare(pv1, pv2);
		}
	};
	
	public static final Comparator<ProductModel> GENERIC_RATING_COMPARATOR = new Comparator<ProductModel>() {
		@Override
		public int compare(ProductModel p1, ProductModel p2) {
			try {
				return p2.getProductRatingEnum().getValue() - p1.getProductRatingEnum().getValue();
			} catch (FDResourceException e) {
				throw new FDRuntimeException(e);
			}
		}
	};

	public static NumberFormat CURRENCY_FORMAT = java.text.NumberFormat.getCurrencyInstance(Locale.US);

	
	// data model accessors
	
	@Override
    public String getFullName();
	
	@Override
    public String getGlanceName();
	
	@Override
    public String getNavName();
	
	@Override
    public String getAltText();
	
	@Override
    public String getKeywords();
	
	public String getAka();
	
	@Override
    public String getBlurb();
	
	public String getSubtitle();

	@Override
    public String getHideUrl();
	
	public EnumProductLayout getProductLayout();
	
	public EnumProductLayout getProductLayout(EnumProductLayout defValue);
	
	public EnumLayoutType getLayout();
	
	public EnumTemplateType getTemplateType();
	
	@Override
    public int getTemplateType(int defaultValue);
	
	public String getAlsoSoldAsName();
	
	@Override
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
	@Override
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
	@Override
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
	@Override
    public List<ProductModel> getYmalProducts(Set<FDSku> removeSkus);
	
	/**
	 *  Return a list of YMAL categories.
	 *  
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	@Override
    public List<CategoryModel> getYmalCategories();
	
	/**
	 *  Return a list of YMAL recipes.
	 *  
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	@Override
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

	/** accessor for attribute PROD_IMAGE_PACKAGE */
	public Image getPackageImage();

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
	
	public SkuModel getDefaultTemporaryUnavSku();
	
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

	public boolean isCharacteristicsComponentsAvailable(FDConfigurableI config);
	
	public boolean isPreconfigured();
	
	public List getDisplayableBrands();

	public List getDisplayableBrands(int numberOfBrands);
	
	public boolean hasTerms();
	
	public Html getProductTerms() ;
	
	public boolean isShowTopTenImage();

        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
	@Deprecated
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
	
	public List<DomainValue> getWineClassifications();

	public String getProductRating() throws FDResourceException; 
	
	public String getProductRating(String skuCode) throws FDResourceException; 

	public EnumOrderLineRating getProductRatingEnum() throws FDResourceException;
	
	public EnumOrderLineRating getProductRatingEnum(String skuCode) throws FDResourceException;
	
	public String getFreshnessGuaranteed() throws FDResourceException; 

	public boolean isNew();
	
	public Date getNewDate();
	
	public double getNewAge();
	
	public boolean isBackInStock();
	
	public Date getBackInStockDate();
	
	public double getBackInStockAge();
	
	public double getAge();
	
	/**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public int getDealPercentage();

        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public int getDealPercentage(String skuCode);
	
        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public int getTieredDealPercentage();

        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public int getTieredDealPercentage(String skuCode);

        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public int getHighestDealPercentage();

        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public int getHighestDealPercentage(String skuCode);
	
        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public String getTieredPrice(double savingsPercentage);
	
        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public double getPrice(double savingsPercentage);

        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public String getPriceFormatted(double savingsPercentage);
	
        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public String getPriceFormatted(double savingsPercentage, String skuCode);
	
        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public String getWasPriceFormatted(double savingsPercentage);
	
        /**
         * use priceCalculator which can be cached for a request. 
         * @return
         */
        @Deprecated
	public String getAboutPriceFormatted(double savingsPercentage);
	
	public int getExpertWeight();
	
	public List<String> getCountryOfOrigin() throws FDResourceException;
	
	//Gift Card changes
	public List getGiftcardType();
	
	public UserContext getUserContext();

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
	 * @return a price calculator which encapsulates the specified sku, if the given skuCode is invalid, otherwise the sku
	 *         which is specified, and the pricing context.
	 */
	public PriceCalculator getPriceCalculator( String skuCode );

	
    /**
     * 
     * @return a price calculator which encapsulates the specified sku.
     */
    public PriceCalculator getPriceCalculator(SkuModel sku);

	public PriceCalculator getPriceCalculator(PricingContext pricingContext);

	public PriceCalculator getPriceCalculator(String skuCode, PricingContext pricingContext);

    public PriceCalculator getPriceCalculator(SkuModel sku, PricingContext pricingContext);
	
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
	
	public EnumSustainabilityRating getSustainabilityRatingEnum() throws FDResourceException;
	
	public String getSustainabilityRating() throws FDResourceException; 
	
	public String getSustainabilityRating(String skuCode) throws FDResourceException;
	
	public boolean showDefaultSustainabilityRating();
	
	public FDGroup getFDGroup() throws FDResourceException;
	
	public boolean isExcludedForEBTPayment();
	
	public boolean isDisabledRecommendations();
	
	/**
	 * If this is set to <code>true</code> order of SKUs on the storefront will be the same as in CMS.
	 * If it's <code>false</code>, SKU order will be determined by domain values.
	 * Default value is <code>false</code>
	 * This attribute is inheritable.
	 * @return Value of <code>retainOriginalSkuOrder</code> flag.
	 */
	public boolean isRetainOriginalSkuOrder();
	
	/**
	 * Return tags directly assigned to product
	 * 
	 * @return
	 */
	public List<TagModel> getTags();
	
	/**
	 * Return all tags including parent tags
	 * @return
	 */
	public Set<TagModel> getAllTags();
	
	public Set<DomainValue> getAllDomainValues();

	
	// ** APPDEV-3179 **
	// Upsell and Cross-Sell attributes for PDP feature

	/**
	 * Return upsell products
	 * 
	 * @return list of {@link ProductModel} objects
	 */
	public List<ProductModel> getUpSellProducts();

	/**
	 * Return cross-sell products
	 * 
	 * @return list of {@link ProductModel} objects
	 */
	public List<ProductModel> getCrossSellProducts();
	
	public String getBrowseRecommenderType();

	/**
	 * Return heat rating factor
	 * @return integer between 0 and 5
	 */
	public int getHeatRating();

	/* PROD_IMAGE_JUMBO */
	public Image getJumboImage();

	/* PROD_IMAGE_ITEM */
	public Image getItemImage();

	/* PROD_IMAGE_EXTRA */
	public Image getExtraImage();
	
	public boolean isDisableAtpFailureRecommendation();

	
	public EnumProductLayout getSpecialLayout();

	public List<ProductModel> getCompleteTheMeal();
	
    public List<ProductModel> getIncludeProducts();

	public String getPageTitle();

	public String getFdxPageTitle();

	public String getSEOMetaDescription();

	public String getFdxSEOMetaDescription();

	public String getPairItHeading();

	public String getPairItText();

	public void setParentNode(ContentNodeModel parentNode);

	/* Time to Complete value for MealKit items */
	public int getTimeToComplete();
}
