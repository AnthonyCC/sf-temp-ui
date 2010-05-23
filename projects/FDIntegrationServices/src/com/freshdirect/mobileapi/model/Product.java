package com.freshdirect.mobileapi.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.common.pricing.ConfiguredPrice;
import com.freshdirect.common.pricing.Pricing;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.PricingEngine;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.common.pricing.SalesUnitRatio;
import com.freshdirect.content.nutrition.EnumAllergenValue;
import com.freshdirect.content.nutrition.ErpNutritionInfoType;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ComponentGroupModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.view.ProductRating;
import com.freshdirect.fdstore.content.view.WebProductRating;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.ProductLabeling;
import com.freshdirect.fdstore.util.RatingUtil;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.xml.XMLSerializer;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.comparator.DomainValueComparator;
import com.freshdirect.mobileapi.model.comparator.VariationComparator;
import com.freshdirect.mobileapi.model.tagwrapper.GetDlvRestrictionsTagWrapper;
import com.freshdirect.mobileapi.service.ServiceException;
import com.freshdirect.mobileapi.util.ProductUtil;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.webapp.util.CCFormatter;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.RestrictionUtil;

/**
 * Wrapper class for ProductModel and ProductImpression classe. The Idea is to
 * consolidate both classes in just one.
 * 
 * @author fgarcia
 * 
 */
public class Product {
    private static final Logger LOG = Logger.getLogger(Product.class);

    // 5minutes
    private static final int REFRESH_PERIOD = 300;

    public enum ImageType {
        ALTERNATE, CATEGORY, CONFIRM, DESCRIPTIVE, DETAIL, FEATURE, PRODUCT, RATING_RELATED, ROLLOVER, THUMBNAIL, ZOOM, LARGE_BURST, THUMB_BURST, WINE_ALT
    }

    public enum ProductLayout {
        PERISHABLE, COMPONENTGROUPMEAL, WINE
    }

    @Deprecated
    public static String SHORT_TERM_UNAVAILABILITY_MULTIPLE_SKU_MESSAGE = "* Some Items Unavailable Tomorrow";

    @Deprecated
    public static String SHORT_TERM_UNAVAILABILITY_SINGLE_SKU_MESSAGE = "Earliest delivery-";

    public static String ESTIMATED_LABEL = "Estimated";

    public static String ESTIMATED_PRICE_LABEL = "Estimated Price";

    public static String PRICE_LABEL = "Price";

    protected ProductImpression product;

    protected PricingContext pricingContext;

    protected FDProduct defaultProduct = null;

    protected Sku defaultSku = null;

    protected PriceCalculator defaultPriceCalculator;

    protected Map<String, Date> shortTermUnavailable = new HashMap<String, Date>();

    protected Map<String, String> kosherRestrictions;

    protected List<Sku> skus = new ArrayList<Sku>();

    protected List<Brand> brands = new ArrayList<Brand>();

    protected List<Brand> brandWithLogos = new ArrayList<Brand>();

    protected List<ProductDomain> productDomains = new ArrayList<ProductDomain>();

    protected List<ProductRating> ratings = new ArrayList<ProductRating>();

    protected List<SalesUnit> salesUnit = new ArrayList<SalesUnit>();

    protected List<Variation> variations = new ArrayList<Variation>();

    protected String ratingLabel = "";

    @SuppressWarnings("unchecked")
    protected List domains;

    // business logic flags

    protected boolean isPricedByLB;

    protected boolean isSoldByLB;

    protected boolean displaySalesUnitsOnly;

    protected boolean displayEstimatedQuantity;

    protected boolean displayShortTermUnavailability;

    protected boolean salesUnitFirst;

    protected boolean hasVariationMatrix;

    protected boolean hasSingleSku;

    protected boolean hasSingleSalesUnit;

    protected boolean salesUnitsMatch;

    protected boolean salesUnitDescrsMatch;

    protected boolean isWineLayout;

    protected boolean hideForMobile;

    /**
     * Flag that indicates how the product is sold. 
     * 
     * Possible values: 
     * QUANTITY = sold by quantity only 
     * SALES_UNIT = sold by sales unit only
     * BOTH = sold by quantity and sales unit.
     */
    protected String sellBySalesUnit;

    protected boolean selectBySalesUnitOnly;

    protected boolean selectBySalesUnitAndQuantity;

    protected List<ComponentGroup> componentGroups = new ArrayList<ComponentGroup>();

    public Product(ProductModel productModel, FDUserI user) throws ModelException {
        this(productModel, user, null);
    }

    private FDUserI user; //Used for product burst labeling

    private Variant variant; //Used for product burst labeling

    //private List<String> warningMessages = new ArrayList<String>();

    @SuppressWarnings("unchecked")
    public Product(ProductModel productModel, FDUserI user, Variant variant) throws ModelException {
        this.product = new ProductImpression(productModel);
        this.pricingContext = user != null ? user.getPricingContext() : null;
        if (pricingContext == null) {
            pricingContext = PricingContext.DEFAULT;
        }
        this.user = user;
        this.variant = variant;

        //Skus init    
        ListIterator li = productModel.getSkus().listIterator();

        if (!productModel.isUnavailable()) {
            //Filter skus
            while (li.hasNext()) {
                SkuModel skuModel = (SkuModel) li.next();
                if (skuModel != null && !skuModel.isUnavailable()) {
                    Sku sku = Sku.wrap(new PriceCalculator(pricingContext, productModel, skuModel), skuModel);
                    this.skus.add(sku);
                }
            }

            this.hasSingleSku = this.skus.size() == 1;

            this.defaultPriceCalculator = new PriceCalculator(user.getPricingContext(), productModel);

            if (this.hasSingleSku) {
                this.defaultSku = this.skus.get(0);
            } else {
                if (this.defaultSku == null) {
                    this.defaultSku = Sku.wrap(defaultPriceCalculator, defaultPriceCalculator.getSkuModel());
                }
            }

            try {
                this.defaultProduct = defaultPriceCalculator.getProduct();

                //It appears that category groupings are also coming through.  Bypassing those.
                if (product.getProductModel().getParentNode() instanceof CategoryModel) {
                    /*
                    * DUP: /shared/includes/product/i_product_methods.jspf
                    * DATE: 9/25/2009   
                    * WHY: The following logic was duplicate because it was specified in a JSP file.
                    * WHAT: The duplicate code determines the product ratings
                    */
                    WebProductRating webProductRating = RatingUtil.getRatings(productModel);
                    if (webProductRating != null) {
                        this.ratingLabel = webProductRating.getRatingLabel();
                        this.ratings = webProductRating.getRatings();
                    }
                }
            } catch (FDResourceException e) {
                throw new ModelException("Error getting product info for default sku. default sku was:" + this.defaultSku.getSkuCode(), e);
            } catch (FDSkuNotFoundException e) {
                throw new ModelException("Error getting product info for default sku. default sku was:" + this.defaultSku.getSkuCode(), e);
            }

            // Flags initialization as in i_product.jspf
            this.isWineLayout = EnumProductLayout.WINE.equals(productModel.getProductLayout());
            this.hasSingleSalesUnit = (1 == this.defaultProduct.getSalesUnits().length);
            this.salesUnitsMatch = false;
            this.salesUnitDescrsMatch = false;

            // Flasg required for quantity;
            this.sellBySalesUnit = productModel.getSellBySalesunit();

            if (this.sellBySalesUnit != null && (this.sellBySalesUnit.equals("QUANTITY") || this.sellBySalesUnit.equals(""))) {
                this.sellBySalesUnit = null;
            }

            this.selectBySalesUnitOnly = "SALES_UNIT".equalsIgnoreCase(this.sellBySalesUnit) || (this.sellBySalesUnit == null);
            this.selectBySalesUnitAndQuantity = "BOTH".equalsIgnoreCase(this.sellBySalesUnit);

            String defaultSalesUnit = "";
            if (this.hasSingleSalesUnit) {
                FDSalesUnit defSU = this.defaultProduct.getSalesUnits()[0];
                defaultSalesUnit = defSU.getName();
                this.salesUnitsMatch = true;
                this.salesUnitDescrsMatch = true;

                Iterator i = productModel.getSkus().iterator();
                while (i.hasNext()) {
                    FDProduct fdp = null;
                    String skuCode = null;
                    try {
                        skuCode = ((SkuModel) i.next()).getSkuCode();
                        fdp = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
                    } catch (FDResourceException e) {
                        LOG.error("Error getting product for sku=" + skuCode, e);
                        continue;
                    } catch (FDSkuNotFoundException e) {
                        LOG.error("Error getting product for sku=" + skuCode, e);
                        continue;
                    }
                    this.salesUnitsMatch &= defaultSalesUnit.equals(fdp.getSalesUnits()[0].getName());
                    this.salesUnitDescrsMatch &= defSU.getDescription().equals(fdp.getSalesUnits()[0].getDescription());
                }

            } else if (defaultSalesUnit == "" && this.defaultProduct.getDefaultSalesUnit() != null) {
                defaultSalesUnit = this.defaultProduct.getDefaultSalesUnit().getName();
            }

            //
            // !!! need to look at isPricedByLB again with scaled pricing in effect
            //

            //this.isPricedByLB = ("LB".equalsIgnoreCase((this.defaultProduct.getPricing().getMaterialPrices()[0]).getPricingUnit()));
            try {
                this.isPricedByLB = "LB".equalsIgnoreCase(this.defaultPriceCalculator.getZonePriceModel().getMaterialPrices()[0]
                        .getPricingUnit());
            } catch (FDResourceException e1) {
                // it will never happens, because only FDProduct construction can throw exception 
            } catch (FDSkuNotFoundException e1) {
                // it will never happens, because only FDProduct construction can throw exception 
            }
            this.isSoldByLB = this.isPricedByLB && ("LB".equalsIgnoreCase((this.defaultProduct.getSalesUnits()[0]).getName()));

            // display sales unit dropdown only (qty is always one)
            // null and "QUANTITY"
            this.displaySalesUnitsOnly = (sellBySalesUnit != null) || (!this.hasSingleSalesUnit && this.isSoldByLB && this.isPricedByLB);
            // display estimated qty if there's no salesUnitDropdown, it's priced by the pound and not sold by the pound
            this.displayEstimatedQuantity = !this.displaySalesUnitsOnly && this.isPricedByLB && !this.isSoldByLB;
            this.salesUnitFirst = (sellBySalesUnit == null) && !this.hasSingleSalesUnit && this.isPricedByLB && !this.isSoldByLB;
            this.hasVariationMatrix = this.defaultSku.hasVariationMatrix();
            this.displayShortTermUnavailability = this.defaultProduct.getMaterial().getBlockedDays().isEmpty();
            if (productModel.getVariationMatrix() == null) {
                this.domains = Collections.EMPTY_LIST;
            } else {
                this.domains = productModel.getVariationMatrix();
            }

            // END Flag initialization

            int MAX_BRANDS_TO_SHOW = 2;

            if (this.isWineLayout) {
                MAX_BRANDS_TO_SHOW = 1;
            }

            // i_product_initialize.jspf
            List prodBrands = productModel.getBrands();
            Iterator it = prodBrands.iterator();
            while (it.hasNext()) {
                BrandModel brandModel = (BrandModel) it.next();
                Brand brand = Brand.wrap(brandModel);
                this.brands.add(brand);

            }
            prodBrands = productModel.getDisplayableBrands(MAX_BRANDS_TO_SHOW);
            it = prodBrands.iterator();
            while (it.hasNext()) {
                BrandModel brandModel = (BrandModel) it.next();
                Brand brand = Brand.wrap(brandModel);
                this.brandWithLogos.add(brand);

            }

            // Logic for grouping the skus
            DomainValueComparator domainValueComp = new DomainValueComparator();
            Map<DomainValue, ProductDomain> domainSkuMatrix = new TreeMap<DomainValue, ProductDomain>(domainValueComp);

            Iterator<Sku> skuIt = this.skus.iterator();
            while (skuIt.hasNext()) {
                Sku sku = skuIt.next();
                sku.setDomains(this.domains);

                FDProduct fdProduct;
                try {
                    fdProduct = sku.getOriginalSku().getProduct();
                } catch (FDResourceException e) {
                    throw new ModelException("Error getting product from default sku", e);
                } catch (FDSkuNotFoundException e) {
                    throw new ModelException("Error getting product from default sku", e);
                }
                String suDescr = fdProduct.getSalesUnits()[0].getDescription();
                if (this.hasSingleSku) {
                    /**
                     * DUP: FDWebSite/docroot/shared/includes/product/i_product_single_sku_box.jspf
                     * DATE: 9/25/2009   
                     * WHY: The following logic was duplicate because it was specified in a JSP file.
                     * WHAT: The duplicated code determines if sales unit description for the given sku should be displayed 
                     */
                    if (this.hasSingleSalesUnit && !this.hasVariationMatrix) {
                        if (!"nm".equalsIgnoreCase(suDescr) && !"ea".equalsIgnoreCase(suDescr) && !"".equalsIgnoreCase(suDescr)) {
                            sku.setSalesUnitDescription(suDescr);
                        }
                    }
                } else {
                    /**
                     * DUP: FDWebSite/docroot/shared/includes/product/i_product_multiple_sku_box.jspf
                     * DATE: 9/25/2009   
                     * WHY: The following logic was duplicate because it was specified in a JSP file.
                     * WHAT: The duplicated code determines if sales unit description for the given sku should be displayed 
                     */
                    if (this.hasSingleSalesUnit && !this.isSoldByLB && (!this.salesUnitsMatch || !this.salesUnitDescrsMatch)) {
                        if (!"nm".equalsIgnoreCase(suDescr)) {
                            sku.setSalesUnitDescription(suDescr);
                        }
                    }
                }

                if (this.getFdDefSource() != null) {
                    sku.setFdContentType("fdSource");
                } else if (this.getFdDefGrade() != null) {
                    sku.setFdContentType("fdGrade");
                } else if (this.getFdDefFrenching() != null) {
                    sku.setFdContentType("fdFrenching");
                } else if (this.getFdDefRipeness() != null) {
                    sku.setFdContentType("fdRipeness");
                }

                //*******************************************************************************
                // i_product_multiple_skus.jspf
                //
                // Matrix for ordering skus by domain value. 

                List<DomainValue> skuMultAttr = sku.getOriginalSku().getVariationMatrix();

                if (skuMultAttr != null && skuMultAttr.size() > 0) {
                    DomainValue key = (DomainValue) skuMultAttr.get(0);
                    if (domainSkuMatrix.containsKey(key)) {
                        ProductDomain productDomain = domainSkuMatrix.get(key);
                        sku.setDomain(productDomain);
                        productDomain.addSku(sku);
                    } else {
                        ProductDomain productDomain = new ProductDomain(this, key);
                        sku.setDomain(productDomain);
                        productDomain.addSku(sku);
                        domainSkuMatrix.put(key, productDomain);
                        //                        this.productDomains.add(productDomain);
                    }
                }

                // END Matrix for ordering skus by domain value. i_product_multiple_skus.jspf
                //*******************************************************************************

                Date earliestDate = sku.getOriginalSku().getEarliestAvailability();
                Calendar testDate = new GregorianCalendar();
                testDate.add(Calendar.DATE, 1);
                // cheat: if no availability indication, show the horizon as the earliest availability
                if (earliestDate == null) {
                    earliestDate = DateUtil.addDays(DateUtil.truncate(new Date()), ErpServicesProperties.getHorizonDays());
                }
                if (QuickDateFormat.SHORT_DATE_FORMATTER.format(testDate.getTime()).compareTo(
                        QuickDateFormat.SHORT_DATE_FORMATTER.format(earliestDate)) < 0) {

                    List<DomainValue> domains = sku.getVariationMatrix() == null ? Collections.EMPTY_LIST : sku.getVariationMatrix();
                    StringBuffer key = new StringBuffer();
                    key.append("*");

                    for (Iterator<DomainValue> i = domains.iterator(); i.hasNext();) {
                        DomainValue domainValue = i.next();
                        key.append(domainValue.getLabel());
                        key.append(", ");
                        key.deleteCharAt(key.length() - 2);
                    }
                    key.append(" avail");

                    this.shortTermUnavailable.put(key.toString(), earliestDate);
                }

            }
            //end skus init            
            // Getting the product domains in order.
            for (ProductDomain productDomain : domainSkuMatrix.values()) {
                this.productDomains.add(productDomain);
            }
        }

        // SALES UNIT
        boolean salesUnitSelected = false;
        if (isAvailable()) {
            FDSalesUnit dsu = defaultProduct.getDefaultSalesUnit();

            FDSalesUnit[] salesUnits = this.defaultProduct.getSalesUnits();
            for (FDSalesUnit salesUnit : salesUnits) {
                SalesUnit su = SalesUnit.wrap(salesUnit);
                if (salesUnit.equals(dsu)) {
                    su.setDefault(true);
                    salesUnitSelected = true;
                }
                this.salesUnit.add(su);
            }
            /*
             * i_product 
             */
            if (!salesUnitSelected && displaySalesUnitsOnly) {
                salesUnit.get(0).setDefault(true);
            }
        }

        // VARIATIONS
        if (isAvailable()) {

            List<FDVariation> variations = Arrays.asList(defaultProduct.getVariations());
            Collections.sort(variations, new VariationComparator());

            for (FDVariation variation : variations) {
                LOG.debug("Variation added: " + variation.getName());
                this.variations.add(Variation.wrap(variation, this));
            }
        }

        if (ProductLayout.COMPONENTGROUPMEAL.name().equalsIgnoreCase(getLayout())) {
            List<ComponentGroupModel> componentGroups = (List) product.getProductModel().getComponentGroups();
            for (ComponentGroupModel componentGroup : componentGroups) {
                ComponentGroup cgp;
                try {
                    cgp = new ComponentGroup(componentGroup, this, user);
                    this.componentGroups.add(cgp);
                } catch (FDException e) {
                    throw new ModelException("Unable to get ComponentGroup", e);
                }
            }

            for (Variation variation : this.variations) {
                variation.removeUnavailableOptions();
            }
        }

        this.hideForMobile = productModel.isHideIphone();

        // Getting any notice message that neeed to be displayed on screen:
        //rsung
        //        if (getFilteredEarliestAvailabilityDate() != null && !hasSingleSku()) {
        //            warningMessages.add("* Earliest delivery-" + CCFormatter.formatAvailabilityDate(getFilteredEarliestAvailabilityDate()));
        //        } else if (isPlatter()) {
        //            warningMessages.add(getCancellationNote());
        //        } else if (!displayShortTermUnavailability) {
        //            warningMessages.add(getDayOfWeekNotice());
        //            warningMessages.add(getDeliveryNote());
        //        }

    }

    /**
     * @return
     * @throws FDResourceException
     */
    public String[] getPlatterCutoffMessage() {
        TimeOfDay cutoffTime = null;
        ;
        StringBuilder title = new StringBuilder();
        StringBuilder message = new StringBuilder();
        try {
            if (isPlatter() && (cutoffTime = RestrictionUtil.getPlatterRestrictionStartTime()) != null) {

                String headerTime;
                String bodyTime;
                if (new TimeOfDay("12:00 PM").equals(cutoffTime)) {
                    headerTime = "12 NOON";
                    bodyTime = "12 Noon";
                } else {
                    SimpleDateFormat headerTimeFormat = new SimpleDateFormat("h:mm a");
                    SimpleDateFormat bodyTimeFormat = new SimpleDateFormat("ha");

                    headerTime = headerTimeFormat.format(cutoffTime.getAsDate());
                    bodyTime = bodyTimeFormat.format(cutoffTime.getAsDate());
                }
                title.append("PLEASE ORDER BY ").append(headerTime).append(" FOR DELIVERY TOMORROW");
                message.append("To assure the highest quality, our chefs prepare this item to order. You must complete checkout by ")
                        .append(bodyTime).append(" to order this item for delivery tomorrow.");
            }
        } catch (FDResourceException e) {
            LOG.warn("FDResourceException while trying to get platter cutoff. no need to throw exception. log and go forward.", e);
        }
        return new String[] { title.toString(), message.toString() };
    }

    public Sku getDefaultSku() {
        return defaultSku;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public List<Brand> getBrandWithLogos() {
        return brandWithLogos;
    }

    public List<ProductDomain> getProductDomains() {
        return productDomains;
    }

    /**
     * Product category Id
     * @return
     */
    public String getCategoryId() {
        return product.getProductModel().getParentNode().getContentName();
    }

    /**
     * Prodcut id
     * @return
     */
    public String getProductId() {
        return product.getProductModel().getContentName();
    }

    public Department getDepartment() {
        return Department.wrap(product.getProductModel().getDepartment());
    }

    public Image getImage(ImageType type) {
        ProductModel productModel = product.getProductModel();
        Image result = null;
        switch (type) {
        case ALTERNATE:
            result = productModel.getAlternateImage();
            break;
        case DETAIL:
            result = productModel.getDetailImage();
            break;
        case CATEGORY:
            result = productModel.getCategoryImage();
            break;
        case CONFIRM:
            result = productModel.getConfirmImage();
            break;
        case DESCRIPTIVE:
            result = productModel.getDescriptiveImage();
            break;
        case FEATURE:
            result = productModel.getFeatureImage();
            break;
        case PRODUCT:
            result = productModel.getProdImage();
            break;
        case RATING_RELATED:
            result = productModel.getRatingRelatedImage();
            break;
        case ROLLOVER:
            result = productModel.getRolloverImage();
            break;
        case THUMBNAIL:
            result = productModel.getThumbnailImage();
            break;
        case ZOOM:
            result = productModel.getZoomImage();
            break;
        case LARGE_BURST:
            result = getLargeBurstImage();
            break;
        case THUMB_BURST:
            result = getThumbBurstImage();
            break;
        case WINE_ALT:
            result = productModel.getAlternateProductImage();
            break;
        }

        return result;
    }

    /**
    * DUP: /shared/includes/product/i_product.jspf
    * DATE: 9/25/2009   
    * WHY: The following logic was duplicate because it was specified in a JSP file.
    * WHAT: The duplicated code determines the product title. For wine, the wine vintage domain label is appended
    * @return
    */
    @SuppressWarnings("unchecked")
    public String getProductTitle() {
        String result = "";

        result = product.getProductModel().getFullName();

        if (EnumProductLayout.WINE.equals(product.getProductModel().getProductLayout())) {
            List wineVintage = product.getProductModel().getWineVintage();
            if (wineVintage != null && wineVintage.size() > 0) {
                DomainValue dValue = (DomainValue) wineVintage.get(0);
                result = new StringBuffer(100).append(result).append(" ").append(dValue.getLabel()).toString();
            }
        }

        return result;
    }

    /**
     * Retrieves product "acknowledge as" name
     * @return
     */
    public String getAkaName() {
        return product.getProductModel().getAka();
    }

    /**
     * Product description is stored in a file with HTML content. This file is read and returned as string (with HTML tegs)
     * @return
     */
    public String getProductDescription() {
        String result = "";

        Html productDescription = product.getProductModel().getProductDescription();
        if (productDescription != null) {
            result = ProductUtil.readHtml(productDescription);
        }
        return result;
    }

    public String getPackageDescription() {
        return product.getProductModel().getPackageDescription();
    }

    /**
     * DUP: /shared/includes/product/i_product_quality_note.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicated code determines the product quality note
     * @return String
     */
    public String getQualityNote() {
        String result = "";
        Html prodQuality = product.getProductModel().getProductQualityNote();
        if (prodQuality != null && !product.getProductModel().isUnavailable()) {
            result = ProductUtil.readHtml(prodQuality);
        }
        return result;
    }

    public boolean hasTerms() {
        return product.getProductModel().hasTerms();
    }

    public String getProductTerms() {
        String result = "";
        Html productTerms = product.getProductModel().getProductTerms();
        if (productTerms != null) {
            result = ProductUtil.readHtml(productTerms);
        }
        return result;
    }

    /**
     * DUP: /includes/product/cutoff_notice.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Retrieves the time restriction to order a platter product. Only applies for platter type products.
     * @return
     */
    public Date getCutOffTime() throws ModelException {
        Date result = null;
        try {
            if (isPlatter() && (RestrictionUtil.getPlatterRestrictionStartTime()) != null) {
                TimeOfDay cutoffTime = null;
                cutoffTime = RestrictionUtil.getPlatterRestrictionStartTime();
                result = cutoffTime.getAsDate();
            }
        } catch (FDResourceException e) {
            throw new ModelException("Unable to get CutOffTime", e);
        }
        return result;
    }

    /**
     * Products with alcohol must show an health warning before display any product information. If the user
     * has seen the alert and is aware, the warning screen can be skipped. 
     * @param isHealthWarningAcknowledged
     * @return
     */
    public boolean showAlcoholAlert(boolean isHealthWarningAcknowledged) {
        return ((CategoryModel) product.getProductModel().getParentNode()).isHavingBeer() && isHealthWarningAcknowledged;
    }

    /**
     * DUP: /shared/includes/product/i_product.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Check if the short term unavailability message should be displayed. There is a specific message related if the product is single or multi sku  
     * @return
     */
    public boolean displayShortTermUnavailability() {
        return displayShortTermUnavailability;
    }

    /**
     * DUP: /shared/includes/product/i_product.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Returns the earliest date of availability
     * @return
     */
    private Date getEarliestAvailabilityDate() {
        Date earliestDate = defaultSku.getEarliestAvailability();
        // if no availability indication, show the horizon as the
        // earliest availability
        if (earliestDate == null) {
            earliestDate = DateUtil.addDays(DateUtil.truncate(new Date()), ErpServicesProperties.getHorizonDays());
        }
        return earliestDate;
    }

    /**
     * DUP: /shared/includes/product/i_product.jspf
     * DATE: 10/23/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Returns the earliest date of availability if flag to display it is set to true. otherwise returns null.
     * @return
     */
    public Date getFilteredEarliestAvailabilityDate() {
        Date earliestAvailabilityDate = null;
        if (displayShortTermUnavailability && shortTermUnavailable.size() > 0) {
            earliestAvailabilityDate = getEarliestAvailabilityDate();
        }
        return earliestAvailabilityDate;
    }

    /**
     * DUP: /shared/includes/product/i_product.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Returns the message for short term unavailability
     * @deprecated Please use the displayShortTermUnavailability() method and hasSingleSku flag to display the message 
     * @return
     */
    @Deprecated
    public String getShortTermUnavailabilityMessage() {
        String result = "";
        if (displayShortTermUnavailability()) {
            if (skus.size() > 1) {
                result = SHORT_TERM_UNAVAILABILITY_MULTIPLE_SKU_MESSAGE;
            } else {
                result = SHORT_TERM_UNAVAILABILITY_SINGLE_SKU_MESSAGE + CCFormatter.formatAvailabilityDate(getEarliestAvailabilityDate());
            }
        }
        return result;
    }

    /**
     * DUP: /shared/includes/product/i_dayofweek_notice.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicate code determines the following logic:
     * Logic from 
     * if the Number of days available is 3 or less, then 
     *   show message; This item is ONLY available for delivery on ...
     * Else 
     *  show message:This item is Not available for delivery on ....
     *  
     *  Days orders is from Monday-Sunday, and must be in the plural form  
     */
    public String getDayOfWeekNotice() {
        String result = "";
        DayOfWeekSet blockedDays = product.getProductModel().getBlockedDays();
        if (!blockedDays.isEmpty()) {
            int numOfDays = 0;
            StringBuffer daysStringBuffer = null;
            boolean isInverted = true;

            if (blockedDays.size() > 3) {
                numOfDays = (7 - blockedDays.size());
                daysStringBuffer = new StringBuffer(blockedDays.inverted().format(true));
            } else {
                isInverted = false;
                daysStringBuffer = new StringBuffer(blockedDays.format(true));
                numOfDays = blockedDays.size();
            }

            if (numOfDays > 1) {
                //** make sundays the last day, if more than one in the list 
                if (daysStringBuffer.indexOf("Sundays, ") != -1) {
                    daysStringBuffer.delete(0, 9);
                    daysStringBuffer.append(" ,Sundays");
                }

                //replace final comma with "and" or "or"
                int li = daysStringBuffer.lastIndexOf(",");
                daysStringBuffer.replace(li, li + 1, (isInverted ? " and " : " or "));
            }

            StringBuilder label = new StringBuilder("This item is ");
            label.append(isInverted ? "only" : "not").append(" available for delivery on ").append(daysStringBuffer.toString());
            result = label.toString();
        }
        return result;
    }

    /**
     * DUP: /shared/includes/product/i_delivery_note.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicate code determines delivery note based on blocked days
     */
    public String getDeliveryNote() {
        String result = "";
        DayOfWeekSet blockedDays = product.getProductModel().getBlockedDays();

        if (!blockedDays.isEmpty()) {
            result = "* Only available for delivery on " + blockedDays.inverted().format(true) + ".";
        }

        return result;
    }

    /**
     * Returns the content of FDDEF_SOURCE file
     * @return
     */
    public String getFdDefSource() throws ModelException {
        String result = "";
        Html fddefSource = product.getProductModel().getFddefSource();
        if (fddefSource != null) {
            try {
                result = ProductUtil.readContent(ProductUtil.resolve(fddefSource.getPath()));
            } catch (IOException e) {
                LOG.warn("Error reading FDDEF_SOURCE file " + fddefSource.getPath(), e);
            }
        }
        return result;
    }

    /**
     * Returns the content of FDDEF_GRADE file
     * @return
     */
    public String getFdDefGrade() {
        String result = "";
        Html content = product.getProductModel().getFddefGrade();
        if (content != null) {
            result = ProductUtil.readHtml(content);
        }
        return result;
    }

    /**
     * Returns the content of FDDEF_FRENCHING file
     * @return
     */
    public String getFdDefFrenching() {
        String result = "";
        Html content = product.getProductModel().getFddefFrenching();
        if (content != null) {
            result = ProductUtil.readHtml(content);
        }
        return result;
    }

    /**
     * Returns the content of FDDEF_RIPENESS file
     * @return
     */
    public String getFdDefRipeness() {
        String result = "";
        Html content = product.getProductModel().getFddefRipeness();
        if (content != null) {
            result = ProductUtil.readHtml(content);
        }
        return result;
    }

    public boolean hasSecondaryDomain() {
        boolean result = false;

        if (domains != null && domains.size() == 2) {
            result = true;
        }
        return result;
    }

    /**
     * List of sales unit. i_product.jspf
     * @return
     */
    public List<SalesUnit> getSalesUnit() {

        return salesUnit;
    }

    /**
     * Returns product variations
     * @return
     */
    public List<Variation> getVariations() {
        return variations;
    }

    public FDProduct getDefaultProduct() {
        return this.defaultProduct;
    }

    public FDProductInfo getDefaultProductInfo() {
        return this.product.getProductInfo();
    }

    /**
    * DUP: /shared/includes/product/i_product.jspf
    * DATE: 9/25/2009   
    * WHY: The following logic was duplicate because it was specified in a JSP file.
    * WHAT: The duplicate code determines the text for the quantity text field
    */
    public String getQuantitText() {
        String result = product.getProductModel().getQuantityText();

        return result;
    }

    public float getQuantityMinimum() {
        return product.getProductModel().getQuantityMinimum();
    }

    public float getQuantityMaximum() {
        return product.getProductModel().getQuantityMaximum();
    }

    public float getQuantityIncrement() {
        return product.getProductModel().getQuantityIncrement();
    }

    public String getSalesUnitLabel() {
        String result = product.getProductModel().getSalesUnitLabel();
        if (displaySalesUnitsOnly) {
            if (selectBySalesUnitOnly) {
                result = product.getProductModel().getQuantityText();
            } else if (selectBySalesUnitAndQuantity()) {
                result = product.getProductModel().getSalesUnitLabel();
            }
        }
        return result;
    }

    public boolean hasHalfPint() {
        boolean value = true;
        try {
            product.getProductModel().getContainerWeightHalfPint();
        } catch (NullPointerException e) {
            value = false;
        }
        return value;
    }

    public boolean hasPint() {
        boolean value = true;
        try {
            product.getProductModel().getContainerWeightPint();
        } catch (NullPointerException e) {
            value = false;
        }
        return value;
    }

    public boolean hasQuart() {
        boolean value = true;
        try {
            product.getProductModel().getContainerWeightQuart();
        } catch (NullPointerException e) {
            value = false;
        }
        return value;
    }

    public List<ProductRating> getProductRatings() {
        return ratings;
    }

    public String getRatingLabel() {
        return ratingLabel;
    }

    public List<String> getOrigin() {
        List<String> result = new ArrayList<String>();
        try {
            result = product.getProductModel().getCountryOfOrigin();
            if (result.size() == 0) {
                String seafoodOrigin = product.getProductModel().getSeafoodOrigin();
                if (seafoodOrigin != null && seafoodOrigin.length() > 0) {
                    result.add(seafoodOrigin);
                }
            }
        } catch (FDResourceException e) {
            LOG.warn("Unable to get the origin list", e);
        }
        return result;
    }

    public String getHowToBuyIt() {
        return product.getProductModel().getServingSuggestion();
    }

    public boolean isQualifiedForPromotions() {
        boolean result = false;
        try {
            result = product.getProductModel().isQualifiedForPromotions();
        } catch (FDResourceException e) {
            LOG.warn("Unable to get if the product is qualified for promotions", e);
        }
        return result;
    }

    public List<String> getAllergens() {
        List<String> result = new ArrayList<String>();
        Set common = Collections.EMPTY_SET;

        try {
            common = product.getProductModel().getCommonNutritionInfo(ErpNutritionInfoType.ALLERGEN);
        } catch (FDResourceException e) {
            LOG.warn("Unable to get allergens list", e);
        }

        for (Iterator ic = common.iterator(); ic.hasNext();) {
            EnumAllergenValue allergen = (EnumAllergenValue) ic.next();
            if (!EnumAllergenValue.getValueForCode("NONE").equals(allergen)) {
                result.add(allergen.toString());
            }
        }

        return result;
    }

    public String getIngredients() {
        String result = "";
        if (isAvailable()) {
            result = defaultProduct.getIngredients();
        }
        return result;
    }

    public String getSkuIngredients(Sku sku) throws FDResourceException, FDSkuNotFoundException {
        String result = null;
        if (!sku.getOriginalSku().isUnavailable()) {

            result = sku.getOriginalSku().getProduct().getIngredients();
        }
        return result;
    }

    public String getSeasonText() {
        return product.getProductModel().getSeasonText();
    }

    /**
     * DUP: /shared/includes/product/i_product_descriptions.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic is used different in a JSP and has been changed to help display the contnet for other clients
     * WHAT: 
     * Return heat rating. The heat rating is contained in a HTML file. A search is done on that content
     * looking for the text that represents the heat level of the product and assign a numeric value
     * from 1 to 5, where 1 is the lowest. 
     * @return
     */
    public int getHeatRating() throws ModelException {
        int result = 0;
        String content = getDescriptionNote();
        if (StringUtils.isNotEmpty(content)) {
            if (content.indexOf("MINOR HEAT") >= 0) {
                result = 1;
            } else if (content.indexOf("SUBTLE HEAT") >= 0) {
                result = 2;
            } else if (content.indexOf("MEDIUM HEAT") >= 0) {
                result = 3;
            } else if (content.indexOf("STRONG HEAT") >= 0) {
                result = 4;
            }
        }
        return result;
    }

    /**
     * Returns the description note. Usually goes after "about" section.
     * @return
     */
    public String getDescriptionNote() {
        String result = "";
        Html content = product.getProductModel().getProductDescriptionNote();
        if (content != null) {
            result = ProductUtil.readHtml(content);
        }
        return result;
    }

    /**
     * DUP: /shared/includes/product/i_product_descriptions.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: If product has product rating, and has a sku, show the daily product rating image
     * 
     * @return
     */
    public boolean showProductRating() {
        boolean result = false;

        try {
            if (defaultSku != null) {
                result = StringUtils.isNotEmpty(product.getProductModel().getProductRating());
            }
        } catch (FDResourceException e) {
            LOG.warn("Unable to determine if product rating should be shown", e);
        }

        return result;

    }

    /**
     * DUP: /shared/includes/product/i_cancellation_note.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: A specific message for platter products.
     * @deprecated User isPlatter() method
     */
    @Deprecated
    public String getCancellationNote() {
        String result = "";
        if (isPlatter()) {
            result = "* Orders for this item cancelled later than 3PM on the day before delivery (or 12 Noon on Fri., Sat. and Sun. and certain holidays) will be subject to a 50% fee.";
        }
        return result;
    }

    public boolean isPlatter() {
        return product.getProductModel().isPlatter();
    }

    public boolean isDisplaySalesUnitsOnly() {
        return displaySalesUnitsOnly;
    }

    public boolean selectBySalesUnitOnly() {
        return selectBySalesUnitOnly;
    }

    public boolean selectBySalesUnitAndQuantity() {
        return selectBySalesUnitAndQuantity;
    }

    public boolean hasSingleSku() {
        return hasSingleSku;
    }

    public String getLayout() {
        String result = "perishable";
        if (EnumProductLayout.WINE.equals(product.getProductModel().getProductLayout())) {
            result = "wine";
        } else if (EnumProductLayout.NEW_WINE_PRODUCT.equals(product.getProductModel().getProductLayout())) {
            result = "wine";
        } else if (EnumProductLayout.COMPONENTGROUP_MEAL.equals(product.getProductModel().getProductLayout())) {
            result = "componentGroupMeal";
        }
        return result;
    }

    /**
     * DUP: /shared/includes/product/i_also_sold_as.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Get a list of products associated to this product that are sold similarly 
     */
    public List<Product> getAlsoSoldAs() throws ModelException {
        List<Product> result = new ArrayList<Product>();
        List<ProductModel> alsoSoldAsRefs = product.getProductModel().getAlsoSoldAsRefs();
        if (alsoSoldAsRefs != null) {
            Iterator<ProductModel> it = alsoSoldAsRefs.iterator();
            while (it.hasNext()) {
                ProductModel asaProd = it.next();
                if (!asaProd.getParentNode().isHidden() && !asaProd.isUnavailable()) {
                    try {
                        result.add(new Product(asaProd, this.user, this.variant));
                    } catch (ModelException e) {
                        LOG.warn("ModelException encountered while trying to wrap 'AlsoSoldAs' product.", e);
                    }
                }
            }
        }
        return result;
    }

    public String getAlsoSoldAsName() {
        return product.getProductModel().getAlsoSoldAsName();
    }

    public boolean isAlcoholProduct() {
        boolean result = false;
        CategoryModel cat = (CategoryModel) product.getProductModel().getParentNode();
        if (cat != null) {
            result = cat.isHavingBeer();
        }
        return result;
    }

    public boolean isAvailable() {
        return !product.getProductModel().isUnavailable();
    }

    /**
     * DUP: FDWebSite/docroot/shared/includes/product/i_product.jspf
     * DATE: 9/58/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicated code displays the days when the produc is available, based on kosher restrictions 
     * 
     * @return A map keyed by kosher restriction name, and the available date for the product as value
     * @throws FDException
     */
    public Map<String, String> getKosherRestrictions() throws FDException {
        kosherRestrictions = new HashMap<String, String>();
        if (isAvailable()) {
            FDProduct defaultSku = this.defaultSku.getOriginalSku().getProduct();

            if (defaultSku.getKosherInfo().isKosherProduction()) {
                GetDlvRestrictionsTagWrapper tagWrapper = new GetDlvRestrictionsTagWrapper(user);
                List<RestrictionI> restrictions = tagWrapper.getRestrictions(EnumDlvRestrictionReason.KOSHER);
                for (RestrictionI restriction : restrictions) {
                    kosherRestrictions.put(restriction.getName(), restriction.getDisplayDate());
                }
            }
        }
        return kosherRestrictions;
    }

    /**
     * DUP: FDWebSite/docroot/shared/includes/product/i_product.jspf
     * DATE: 9/28/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicated code determines the label for price field 
     * Returns the label for price field
     * @return
     */
    public String getPriceLabel() {
        String result = PRICE_LABEL;
        if (!isWineLayout) {
            if (displayEstimatedQuantity) {
                result = ESTIMATED_LABEL;
            } else if (isPricedByLB) {
                result = ESTIMATED_PRICE_LABEL;
            }
        }
        return result;
    }

    /**
     * DUP: FDWebSite/docroot/shared/includes/product/i_product_about.jspf
     *      Class IncludeMediaTag
     * DATE: 9/28/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicated code "prints" the product description, reading the Html object called productDesc
     * 
     * @return the product description (HTML content)
     */
    public String getDescription() {
        String result = "";
        Html desc = product.getProductModel().getProductDescription();
        if (desc != null) {
            result = ProductUtil.readHtml(desc);
        }
        return result;
    }

    /**
     * DUP: FDWebSite/docroot/shared/includes/i_nutrition_sheet.jspf
     *      Class IncludeMediaTag
     * DATE: 9/28/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicated code "prints" the product description, reading the Html object called productDesc
     * 
     * @return the product description (HTML content)
     */
    public String getHeatingInstructions() {
        String result = "";
        if (isAvailable()) {
            result = product.getFDProduct().getNutritionInfoString(ErpNutritionInfoType.HEATING);
        }
        return result;
    }

    /**
     * DUP: /shared/includes/i_nutrition_sheet.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Generates the Nutrition Facts layout for the product.
     * TODO: Look for caching if we find any performance issue.
     * @return
     */
    public String getNutrition() {
        String result = "";
        if (isAvailable()) {
            try {
                InputStream xslStream = Product.class.getResourceAsStream(("nutrition_label.xsl"));
                Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslStream));
                org.dom4j.Document doc = new XMLSerializer().serializeDocument("nutrition", getDefaultProduct().getNutrition());
                StringWriter st = new StringWriter();
                transformer.transform(new org.dom4j.io.DocumentSource(doc), new StreamResult(st));
                result = st.toString();
            } catch (TransformerConfigurationException e) {
                LOG.error(e.getMessage(), e);
            } catch (TransformerFactoryConfigurationError e) {
                LOG.error(e.getMessage(), e);
            } catch (TransformerException e) {
                LOG.error(e.getMessage(), e);
            }

        }
        return result;
    }

    public String getSkuNutrition(Sku sku) throws FDResourceException, FDSkuNotFoundException {
        String result = "";
        FDProduct fdProduct = sku.getOriginalSku().getProduct();
        if (isAvailable()) {
            try {
                InputStream xslStream = Product.class.getResourceAsStream(("nutrition_label.xsl"));
                Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(xslStream));
                org.dom4j.Document doc = new XMLSerializer().serializeDocument("nutrition", fdProduct.getNutrition());
                StringWriter st = new StringWriter();
                transformer.transform(new org.dom4j.io.DocumentSource(doc), new StreamResult(st));
                result = st.toString();
            } catch (TransformerConfigurationException e) {
                LOG.error(e.getMessage(), e);
            } catch (TransformerFactoryConfigurationError e) {
                LOG.error(e.getMessage(), e);
            } catch (TransformerException e) {
                LOG.error(e.getMessage(), e);
            }

        }
        return result;

    }

    public boolean isInProductInCart() {
        boolean inProductInCart = false;
        boolean coaseMatch = true; //assume coarse match by sku

        if (coaseMatch) {
            inProductInCart = isCoarseMatch();
        }

        return inProductInCart;
    }

    private boolean isCoarseMatch() {
        boolean inProductInCart = false;

        if (user != null && user.getShoppingCart() != null) {
            Cart cart = Cart.wrap(user.getShoppingCart());
            inProductInCart = cart.isProductInCart(this);
        }

        return inProductInCart;
    }

    public static Product wrap(ProductModel productModel, FDUserI user, Variant variant) throws ModelException {
        Product result = null;
        if (EnumProductLayout.WINE.equals(productModel.getProductLayout())) {
            result = new Wine(productModel, user, variant);
        } else if (EnumProductLayout.NEW_WINE_PRODUCT.equals(productModel.getProductLayout())) {
            result = new Wine(productModel, user, variant);
        } else {
            result = new Product(productModel, user, variant);
        }
        return result;
    }

    public static Product wrap(ProductModel productModel, FDUserI user) throws ModelException {
        return wrap(productModel, user, null);
    }

    public static Product wrap(ProductModel productModel) throws ModelException {
        return wrap(productModel, null, null);
    }

    public boolean isAutoConfigurable() {
        return product.getProductModel().isAutoconfigurable();
    }

    /**
     * If applicable, returns autoconfigured sales unit.
     * @return
     */
    public String getAutoConfiguredSalesUnit() {
        String autoConfiguredSalesUnit = null;
        if (!product.getProductModel().isUnavailable()) {
            FDConfigurableI configuration = product.getFDProduct().getAutoconfiguration(product.getProductModel().isSoldBySalesUnits(), 1);
            if (configuration != null) {
                autoConfiguredSalesUnit = configuration.getSalesUnit();
            }
        }
        return autoConfiguredSalesUnit;
    }

    public double getPrice(Sku sku, SalesUnit salesUnit, double quantity, Map<String, String> options) throws PricingException {
        double price = 0.0;
        //Pricing pricing = this.product.getFDProduct().getPricing();
        Pricing pricing = getFDProduct(sku.getSkuCode()).getPricing();
        FDConfiguration configuration = new FDConfiguration(quantity, salesUnit.getName(), options);

        if (sku != null && salesUnit != null && quantity > 0.0) {
            ConfiguredPrice configuredPrice = PricingEngine.getConfiguredPrice(pricing, configuration, pricingContext);
            price = configuredPrice.getPrice().getPrice();
        }
        return price;
    }

    public double getEstimatedQuantity(Sku sku, SalesUnit salesUnit, double quantity) throws PricingException {
        double estimatedQuantity = 0.0;

        Pricing pricing = getFDProduct(sku.getSkuCode()).getPricing();
        SalesUnitRatio sur = pricing.findSalesUnitRatio(salesUnit.getName());

        estimatedQuantity = sur.getRatio() * quantity;
        return estimatedQuantity;
    }

    public Sku getSkyByCode(String skuCode) {
        Sku result = null;
        for (Sku sku : skus) {
            if (sku.getSkuCode().equals(skuCode)) {
                result = sku;
            }
        }
        return result;
    }

    public SalesUnit getSalesUnitByName(String name) {
        SalesUnit result = null;
        for (SalesUnit su : this.getSalesUnit()) {
            if (su.getName().equals(name)) {
                result = su;
            }
        }
        return result;
    }

    private FDProduct getFDProduct(String skuCode) {

        try {
            FDProductInfo productInfo = FDCachedFactory.getProductInfo(skuCode);
            return FDCachedFactory.getProduct(productInfo);
        } catch (FDResourceException e) {
            throw new FDRuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new FDRuntimeException(e);
        }

    }

    public String getSellBySalesUnit() {
        return sellBySalesUnit;

    }

    public void setSellBySalesUnit(String sellBySalesUnit) {
        this.sellBySalesUnit = sellBySalesUnit;

    }

    public boolean isSoldBySalesUnits() {
        return product.getProductModel().isSoldBySalesUnits();

    }

    public int getHighestDealPercentage() {
        int result = 0;
        if (!product.getProductModel().isUnavailable()) {
            //result = product.getProductInfo().getHighestDealPercentage();
            result = defaultPriceCalculator.getHighestDealPercentage();
        }
        return result;
    }

    /**
     * Method to get a product by id and category id. If product is not get, 
     * 
     * @param id
     * @param categoryId
     * @return
     * @throws ServiceException 
     * @throws ModelException
     */
    public static Product getProduct(String id, String categoryId, SessionUser user) throws ServiceException {
        ProductModel productModel = null;
        Product result = null;

        productModel = ContentFactory.getInstance().getProduct(categoryId, id);

        if (productModel == null) {
            LOG.info("Unable to get product, trying with content node key");
            productModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(ContentKey.decode("Product:" + id));
        }
        try {
            result = Product.wrap(productModel, user.getFDSessionUser().getUser());
        } catch (ModelException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return result;
    }

    public String getConfigurationDescription() {
        return product.getConfigurationDescription();
    }

    public boolean isFrozen() {
        return product.getProductModel().isFrozen();
    }

    @SuppressWarnings("deprecation")
    public String getPartiallyFrozen() {
        String result = "";
        Html partiallyFrozen = product.getProductModel().getPartallyFrozen();
        if (partiallyFrozen != null) {
            try {
                result = ProductUtil.readContent(ProductUtil.resolve(partiallyFrozen.getPath()));
            } catch (IOException e) {
                LOG.warn("Error reading PARTIALLY_FROZEN file " + partiallyFrozen.getPath(), e);
            }
        }

        return result;
    }

    public boolean isMultipleNutrition() {
        return product.getProductModel().isNutritionMultiple();
    }

    public List<ComponentGroup> getComponentGroups() {
        return componentGroups;
    }

    public boolean isDisplayEstimatedQuantity() {
        return displayEstimatedQuantity;
    }

    /**
     * @return
     */
    public Image getLargeBurstImage() {
        /*
         * DUP: FDWebSite/docroot/shared/includes/product/i_product_image.jspf
         * LAST UPDATED ON: 11/13/2009
         * LAST UPDATED WITH SVN#: 5951
         * WHY: The following logic was duplicate because it was specified in a JSP file.
         * WHAT: Only deal bursts are displayed for large product image
         */

        Image image = null;
        ProductLabeling productLabeling = new ProductLabeling(this.user, this.product.getProductModel(), (variant == null ? null : variant
                .getHideBursts()));
        if (productLabeling.isDisplayDeal()) {
            image = new Image("/media_stat/images/deals/brst_lg_" + getHighestDealPercentage() + ".png", 55, 55);
        }
        return image;
    }

    public Image getThumbBurstImage() {
        Image image = null;

        ProductLabeling productLabeling = new ProductLabeling(this.user, this.product.getProductModel(), (variant == null ? null : variant
                .getHideBursts()));

        if ( productLabeling.isDisplayDeal()) {                   	
            image = new Image("/media_stat/images/deals/brst_sm_" + getHighestDealPercentage() + ".png", 35, 35);
        } else if (productLabeling.isDisplayFave()) {
            image = new Image("/media_stat/images/bursts/brst_sm_fave.png", 35, 35);
        } else if (productLabeling.isDisplayNew()) {
            image = new Image("/media_stat/images/bursts/brst_sm_new.png", 35, 35);
        }
        return image;

    }

    public boolean containsSku(String skuCode) {
        boolean containsSku = false;
        for (Sku sku : getSkus()) {
            if (sku.getSkuCode().equals(skuCode)) {
                containsSku = true;
                break;
            }
        }
        return containsSku;
    }

    /**
     * DUP: FDWebSite/docroot/shared/includes/product/kosher.jspf
     * LAST UPDATED ON: 11/13/2009
     * LAST UPDATED WITH SVN#: 1
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: Retrieves the kosher symbol to display the correct logo on product description
     * 
     * @return
     * @throws FDResourceException
     */
    public Image getKosherSymbol() throws FDResourceException {
        Image result = null;
        String kosherType = product.getProductModel().getKosherType();
        String kosherSymbol = product.getProductModel().getKosherSymbol();
        if ((!"".equalsIgnoreCase(kosherSymbol) && kosherSymbol != null) || (!"".equalsIgnoreCase(kosherType) && kosherType != null)) {
            result = new Image();
            result.setPath("/media/editorial/kosher/symbols/" + kosherSymbol.toLowerCase() + "_s.gif");
        }
        return result;

    }

    public String getKosherType() throws FDResourceException {
        String kosherType = product.getProductModel().getKosherType();
        return kosherType;
    }

    public boolean isSoldByLB() {
        return isSoldByLB;
    }

    public boolean isPricedByLB() {
        return isPricedByLB;
    }
    //
    //    public List<String> getWarningMessages() {
    //        return warningMessages;
    //    }

}
