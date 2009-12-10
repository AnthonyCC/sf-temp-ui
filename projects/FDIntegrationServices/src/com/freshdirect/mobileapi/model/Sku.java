package com.freshdirect.mobileapi.model;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.attributes.MultiAttribute;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.DomainValueRef;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.QuickDateFormat;
import com.freshdirect.webapp.util.JspMethods;

public class Sku {
    private static final Logger LOG = Logger.getLogger(Sku.class);

    @SuppressWarnings("unchecked")
    private List domains = Collections.EMPTY_LIST;

    private SkuModel skuModel;

    private String domainLabel;

    private String salesUnitDescription;

    private FDProductInfo productInfo;

    private Attribute variationMatrix;

    private MultiAttribute variationOptions;

    private String rating;

    private String ratingDescription;

    private boolean wineLayout;

    private int secondaryDomainValuePriority;

    private int primaryDomainValuePriority;

    private ProductDomain domain;

    private String fdContentType;

    public static Sku wrap(SkuModel skuModel) {
        Sku sku = new Sku();
        try {
            sku.productInfo = FDCachedFactory.getProductInfo(skuModel.getSkuCode());
        } catch (FDResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FDSkuNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sku.skuModel = skuModel;
        sku.variationMatrix = skuModel.getAttribute("VARIATION_MATRIX");
        sku.variationOptions = (MultiAttribute) skuModel.getAttribute("VARIATION_OPTIONS");

        // BEGIN i_product_skus_rating
        boolean matchFound = false; //default to false

        //this is taking the place of a skuCode
        String deptIdCheck = sku.productInfo.getSkuCode().toString().substring(0, 3);

        if (deptIdCheck != null && !"".equals(deptIdCheck)) {
            deptIdCheck = deptIdCheck.toUpperCase();

            // grab sku prefixes that should show ratings
            String _skuPrefixes = FDStoreProperties.getRatingsSkuPrefixes();

            //if we have prefixes then check them
            if (_skuPrefixes != null && !"".equals(_skuPrefixes)) {
                StringTokenizer st = new StringTokenizer(_skuPrefixes, ","); //setup for splitting property
                String curPrefix = ""; //holds prefix to check against
                String spacer = "* "; //spacing for sysOut calls

                //loop and check each prefix
                while (st.hasMoreElements()) {
                    curPrefix = st.nextToken();

                    //if prefix matches get product info
                    if (deptIdCheck.startsWith(curPrefix)) {
                        matchFound = true;
                    }
                    //exit on matched sku prefix
                    if (matchFound) {
                        break;
                    }
                    spacer = spacer + "   ";
                }
            }
        }

        if (matchFound) {
            if (sku.productInfo.getRating() != null && sku.productInfo.getRating().trim().length() > 0) {
                EnumOrderLineRating enumRating = EnumOrderLineRating.getEnumByStatusCode(sku.productInfo.getRating());
                if (enumRating != null && enumRating.isEligibleToDisplay()) {
                    sku.rating = enumRating.getStatusCodeInDisplayFormat();
                    sku.ratingDescription = enumRating.getShortDescription();
                }
            }
        }
        // END i_product_skus_rating

        return sku;

    }

    public String getSkuCode() {
        return skuModel.getSkuCode();
    }

    public Date getEarliestAvailability() {
        return skuModel.getEarliestAvailability();
    }
    
    public Date getFilteredEarliestAvailability() {
        /*
       Date earliestDate = sku.getEarliestAvailability();
       Calendar testDate = new GregorianCalendar();
       testDate.add(Calendar.DATE, 1);
       // cheat: if no availability indication, show the horizon as the
       //        earliest availability
       if (earliestDate == null) {
           earliestDate = DateUtil.addDays( DateUtil.truncate( new Date() ), ErpServicesProperties.getHorizonDays() );
       }
       if (QuickDateFormat.SHORT_DATE_FORMATTER.format(testDate.getTime()).compareTo(QuickDateFormat.SHORT_DATE_FORMATTER.format(earliestDate)) < 0) {
           %><font class="text11rbold">*</font><%
       } %>
       */
        Date earliestDate = getEarliestAvailability();
        Calendar testDate = new GregorianCalendar();
        testDate.add(Calendar.DATE, 1);
        if (earliestDate == null) {
            earliestDate = DateUtil.addDays( DateUtil.truncate( new Date() ), ErpServicesProperties.getHorizonDays() );
        }
        if (!(QuickDateFormat.SHORT_DATE_FORMATTER.format(testDate.getTime()).compareTo(QuickDateFormat.SHORT_DATE_FORMATTER.format(earliestDate)) < 0)) {
            earliestDate = null;
        }
        return earliestDate;
    }

    public double getPrice() {
        return productInfo.getDefaultPrice();
    }

    public String getFormattedPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(getPrice());
    }

    public String getPriceUnit() {
        return productInfo.getDefaultPriceUnit();
    }

    public double getBasePrice() {
        return productInfo.getBasePrice();
    }

    public boolean hasWasPrice() {
        return productInfo.hasWasPrice();
    }

    public String getFormattedBasePrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(getBasePrice());
    }

    public String getBasePriceUnit() {
        return productInfo.getBasePriceUnit();
    }

    public boolean hasVariationMatrix() {
        return variationMatrix != null;
    }

    public boolean hasVariationOptions() {
        return variationOptions != null;
    }

    public boolean hasPrimaryDomain() {
        return (domains.size() > 0);
    }

    public boolean hasSecondaryDomain() {
        return (domains.size() == 2);
    }

    @SuppressWarnings("unchecked")
    public void setDomains(List domains) {
        this.domains = domains;
    }

    public String getRating() {
        return rating;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public SkuModel getOriginalSku() {
        return skuModel;
    }

    public String getDomainValueId() {
        String dvId = ((DomainValueRef) variationOptions.getValue(0)).getDomainValue().getContentName();
        return dvId;
    }

    /**
     * Display scaled pricing for FDProduct product.
     * from  /shared/includes/product/i_scaled_prices_fixed.jspf
     * @return
     */
    public String[] getScaledPrices() {
        String[] scales = new String[] {};

        try {
            FDProduct defaultProduct = FDCachedFactory.getProduct(productInfo);
            if (wineLayout) {
                scales = defaultProduct.getPricing().getWineScaleDisplay(true);
            } else {
                scales = defaultProduct.getPricing().getScaleDisplay();
            }
        } catch (FDResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FDSkuNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return scales;
    }

    public void setWineLayout(boolean isWineLayout) {
        wineLayout = isWineLayout;
    }

    @SuppressWarnings("deprecation")
    public String getDomainLabel() {

        if (this.hasSecondaryDomain()) {
            String optionalDomainLabel = "";
            DomainValue secDomainValue = ((DomainValueRef) ((MultiAttribute) skuModel.getAttribute("VARIATION_MATRIX")).getValue(1))
                    .getDomainValue();
            DomainValue optDomainValue = null;

            if (this.hasVariationOptions() && (this.domain.getSkus().size() > 1) && !this.domain.isAllSameOption()) {
                optDomainValue = ((DomainValueRef) ((MultiAttribute) skuModel.getAttribute("VARIATION_OPTIONS")).getValue(0))
                        .getDomainValue();
                optionalDomainLabel = "(" + optDomainValue.getLabel() + ")";

            }
            if (secDomainValue != null) {
                this.domainLabel = secDomainValue.getLabel();
            }

            if (secDomainValue != null && optDomainValue != null) {
                this.domainLabel += optionalDomainLabel;
            } else if (secDomainValue != null && getSalesUnitDescription() != null) {
                this.domainLabel += " " + getSalesUnitDescription();
            }
        }


        return this.domainLabel;
    }

    public void setDomainLabel(String domainLabel) {
        this.domainLabel = domainLabel;
    }

    public int getSecondaryDomainValuePriority() {
        int result = 0;
        if (hasSecondaryDomain()) {
            DomainValue dv1 = ((DomainValueRef) ((MultiAttribute) skuModel.getAttribute("VARIATION_MATRIX")).getValue(1)).getDomainValue();
            result = dv1.getPriority();
        }
        return result;
    }

    public int getPrimaryDomainValuePriority() {
        int result = 0;
        if (hasPrimaryDomain()) {
            DomainValue dv1 = ((DomainValueRef) ((MultiAttribute) skuModel.getAttribute("VARIATION_MATRIX")).getValue(0)).getDomainValue();
            result = dv1.getPriority();
        }
        return result;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }

    public String getSalesUnitDescription() {
        return salesUnitDescription;
    }

    public void setSalesUnitDescription(String salesUnitDescription) {
        this.salesUnitDescription = salesUnitDescription;
    }

    public ProductDomain getDomain() {
        return domain;
    }

    public void setDomain(ProductDomain domain) {
        this.domain = domain;
    }

    public String getFdContentType() {
        return fdContentType;
    }

    public void setFdContentType(String fdContentType) {
        this.fdContentType = fdContentType;
    }

    /**
     * DUP: /shared/includes/product/i_product_display_about.jspf
     * DATE: 9/25/2009   
     * WHY: The following logic was duplicate because it was specified in a JSP file.
     * WHAT: The duplicate code determines the following logic:
     * TODO This must be moved to SKU
     * @return
     */
    public String getDisplayAboutPrice() {
        String displayPriceString = null;

        try {
            FDProduct defaultProduct = FDCachedFactory.getProduct(productInfo);

            if (null != defaultProduct.getDisplaySalesUnits() && defaultProduct.getDisplaySalesUnits().length > 0) {
                FDSalesUnit fdSalesUnit = defaultProduct.getDisplaySalesUnits()[0];
                double salesUnitRatio = (double) fdSalesUnit.getDenominator() / (double) fdSalesUnit.getNumerator();
                String baseUnit = fdSalesUnit.getName();
                String[] scalesPrice = defaultProduct.getPricing().getScaleDisplay();
                double displayPrice = 0;
                if (null != scalesPrice && scalesPrice.length > 0) {
                    displayPrice = defaultProduct.getPricing().getMinPrice() / salesUnitRatio;
                } else {
                    displayPrice = productInfo.getDefaultPrice() / salesUnitRatio;
                }

                if (displayPrice > 0) {
                    displayPriceString = "about " + JspMethods.formatDecimal(salesUnitRatio) + baseUnit.toLowerCase() + ", "
                            + JspMethods.currencyFormatter.format(displayPrice) + "/" + baseUnit.toLowerCase();
                }
            }
        } catch (FDResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FDSkuNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return displayPriceString;
    }

}
