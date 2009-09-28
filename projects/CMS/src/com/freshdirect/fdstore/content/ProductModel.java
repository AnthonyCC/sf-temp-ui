package com.freshdirect.fdstore.content;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.NVL;

public interface ProductModel extends ContentNodeModel, AvailabilityI, YmalSource {

	/** Orders products by department & full name */
	public final static Comparator DEPTFULL_COMPARATOR = new Comparator() {

		public int compare(Object o1, Object o2) {

			ProductModel p1 = (ProductModel) o1;
			ProductModel p2 = (ProductModel) o2;

			int ret = NVL.apply(p1.getDepartment().getFullName(), "").compareTo(NVL.apply(p2.getDepartment().getFullName(), ""));

			if (ret == 0) {
				ret = p1.getFullName().compareTo(p2.getFullName());
			}

			return ret;
		}
	};

	public static class PriceComparator implements Comparator {

		private int flips;

		public int compare(Object obj1, Object obj2) {
			try {
				FDProductInfo pi1 = FDCachedFactory.getProductInfo(((SkuModel) obj1).getSkuCode());
				FDProductInfo pi2 = FDCachedFactory.getProductInfo(((SkuModel) obj2).getSkuCode());
				if (pi1.getDefaultPrice() > pi2.getDefaultPrice()) {
					flips++;
					return 1;
				} else if (pi1.getDefaultPrice() < pi2.getDefaultPrice()) {
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
	
	public static class ProductModelPriceComparator implements Comparator {

	    final boolean inverse;
	    private ProductModelPriceComparator(boolean inverse) {
	        this.inverse = inverse;
	    }
	    
            public int compare(Object o1, Object o2) {
                double price1 = 0;
                double price2 = 0;
                ProductModel model1 = (ProductModel) o1;
                SkuModel sku1 = model1.getDefaultSku();
                if (sku1 != null) {
                    try {
                        price1 = sku1.getProductInfo().getDefaultPrice();
                    } catch (FDResourceException e) {
                        e.printStackTrace();
                    } catch (FDSkuNotFoundException e) {
                        e.printStackTrace();
                    }
                }
    
                ProductModel model2 = (ProductModel) o2;
                SkuModel sku2 = model2.getDefaultSku();
                if (sku2 != null) {
                    try {
                        price2 = sku2.getProductInfo().getDefaultPrice();
                    } catch (FDResourceException e) {
                        e.printStackTrace();
                    } catch (FDSkuNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                
                if (!model1.isDisplayable() && model2.isDisplayable()) {
                	return 1;
                }
                
                if (model1.isDisplayable() && !model2.isDisplayable()) {
                	return -1;
                }
                
                int result = Double.compare(price1, price2);
                if (inverse) {
                    result = -result;
                }
                return result;
            }
        }

	
	
	public static class RatingComparator implements Comparator {

		private int flips;

		public int compare(Object obj1, Object obj2) {
			try {
				FDProductInfo pi1 = FDCachedFactory.getProductInfo(((SkuModel) obj1).getSkuCode());
				FDProductInfo pi2 = FDCachedFactory.getProductInfo(((SkuModel) obj2).getSkuCode());
				
				EnumOrderLineRating oli1=EnumOrderLineRating.getEnumByStatusCode(pi1.getRating());
				EnumOrderLineRating oli2=EnumOrderLineRating.getEnumByStatusCode(pi2.getRating());
				
				
				if(oli1==null && oli2==null) return 0;
				
				if(oli1!=null && oli2==null) return 1;
				
				if(oli1==null && oli2!=null) return -1;
				
				if (oli1.getId()>oli2.getId()) {
					flips++;
					return 1;
				} if (oli1.getId()<oli2.getId()) {
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
	

	/** Don't use allTheSame/reset, that's not thread-safe */
	public final static Comparator PRICE_COMPARATOR = new ProductModel.PriceComparator();

        public final static Comparator PRODUCT_MODEL_PRICE_COMPARATOR = new ProductModel.ProductModelPriceComparator(false);
        public final static Comparator PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE = new ProductModel.ProductModelPriceComparator(true);
	
	
	/** Don't use allTheSame/reset, that's not thread-safe */
	public final static Comparator RATING_COMPARATOR = new ProductModel.RatingComparator();

	
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
	
	public EnumLayoutType getLayout();
	
	public EnumTemplateType getTemplateType();
	
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
	
        public boolean isDisplayable();

        public boolean isDisplayableBasedOnCms();
	
	public boolean isShowSalesUnitImage();
	
	public boolean isNutritionMultiple();
	
	public boolean isNotSearchable();
	
	public double getContainerWeightHalfPint();
	
	public double getContainerWeightPint();
	
	public double getContainerWeightQuart();
	
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
	public List getSkus();

	public List getSkuCodes();
	
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
	public List getBrands();

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
	public List getYmals();

	/**
	 *  Return a list of YMAL products.
	 *  
	 *  @return a list of ProductModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List getYmalProducts();
	
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
	public List getYmalProducts(Set removeSkus);
	
	/**
	 *  Return a list of YMAL categories.
	 *  
	 *  @return a list of CategoryModel objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List getYmalCategories();
	
	/**
	 *  Return a list of YMAL recipes.
	 *  
	 *  @return a list of Recipe objects, which are contained in
	 *          the YMALs for this product.
	 *  @see #getYmals()
	 */
	public List getYmalRecipes();
	
	/** 
	 * Return a list of recommended alternatives.
	 * @return a list of ProductModel objects 
	 */
	public List getRecommendedAlternatives();

	public List getWeRecommendText();
	
	public List getWeRecommendImage();
	
	/**
	 * @return List of Recipe
	 */
	public List getRelatedRecipes();
	
	public List getProductBundle();

	
	public ProductModel getAlsoSoldAs(int idx);

	/** Getter for property skus.
	 * @return Value of property skus.
	 */
	public List getAlsoSoldAs();

	
	public List getHowtoCookitFolders();
	
	public CategoryModel getPrimaryHome();
	
	public SkuModel getPreferredSku();
	
	public List getRating();
	
	public List getUsageList();
	
	public DomainValue getUnitOfMeasure();
	
	public List getVariationMatrix();
	
	public List getVariationOptions();
	
	public DomainValue getWineCountry();
	
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
	
	public Html getDonenessGuide();
	
	public Html getFddefFrenching();
	
	public Html getFddefGrade();
	
	public Html getFddefRipeness();
	
	public Html getSalesUnitDescription();
	
	public Html getPartallyFrozen();
	
	
	public boolean hasComponentGroups();
	
	public List getComponentGroups();
	
	
	public Html getProductTermsMedia();
	

	
	// other helpers
	
	
	public ProductRef getProductRef();

	public DepartmentModel getDepartment();

	public Image getCategoryImage();

	public Image getThumbnailImage();

	public boolean enforceQuantityMax();

	public List getAlsoSoldAsRefs();

	public Object clone();

	public boolean isQualifiedForPromotions() throws FDResourceException;

	/**
	 * @return true if any of its skus is a platter
	 */
	public boolean isPlatter();

	public DayOfWeekSet getBlockedDays();

	public SkuModel getDefaultSku();

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
	
	public List getNewWineType();
	
	public List getWineVarietal();
	
	public List getWineVintage();
	
	public List getNewWineRegion();
	
	public List getWineRating1();
	
	public List getWineRating2();
	
	public List getWineRating3();
	
	public Html getWineReview1();
	
	public Html getWineReview2();
	
	public Html getWineReview3();
	
	public Html getProductBottomMedia();
	
	public CategoryModel getPerfectPair();		
	
	public List getWineClassifications();
	//Added for Produce rating.
	public String getProductRating()throws FDResourceException; 
	
	public boolean isNew();
	
	public int getDealPercentage();

	public int getDealPercentage(String skuCode);
	
	public int getTieredDealPercentage();

	public int getTieredDealPercentage(String skuCode);

	public int getHighestDealPercentage();

	public int getHighestDealPercentage(String skuCode);
	
	public int getExpertWeight();
	
	public List getCountryOfOrigin() throws FDResourceException;
	
	//Gift Card changes
	public List getGiftcardType();
}
