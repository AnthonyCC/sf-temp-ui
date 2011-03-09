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
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.EnumSustainabilityRating;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.QuickDateFormat;

public class Sku {
    private static final Logger LOG = Logger.getLogger(Sku.class);

    @SuppressWarnings("unchecked")
    private List domains = Collections.EMPTY_LIST;

    private final SkuModel skuModel;
    
    private final PriceCalculator priceCalc;

    private String domainLabel;

    private String salesUnitDescription;

    private FDProductInfo productInfo;

    private final List<DomainValue> variationMatrix;

    private final List<DomainValue> variationOptions;

    private String rating;

    private String ratingDescription;

    private boolean wineLayout;

    private int secondaryDomainValuePriority;

    private int primaryDomainValuePriority;

    private ProductDomain domain;

    private String fdContentType;
    
    private String sustainabilityRating="";

    private String sustainabilityRatingDescription;

    private Sku(PriceCalculator priceCalc, SkuModel skuModel) throws FDResourceException, FDSkuNotFoundException {
        this.skuModel = skuModel;
        this.variationMatrix = skuModel.getVariationMatrix();
        this.variationOptions = skuModel.getVariationOptions();
        this.priceCalc = priceCalc;
        this.productInfo = priceCalc.getProductInfo();


        // BEGIN i_product_skus_rating
        boolean matchFound = false; //default to false

        //this is taking the place of a skuCode
        String deptIdCheck = this.productInfo.getSkuCode().toString().substring(0, 3);

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
            if (this.productInfo.getRating() != null && this.productInfo.getRating().trim().length() > 0) {
                EnumOrderLineRating enumRating = EnumOrderLineRating.getEnumByStatusCode(this.productInfo.getRating());
                if (enumRating != null && enumRating.isEligibleToDisplay()) {
                    this.rating = enumRating.getStatusCodeInDisplayFormat();
                    this.ratingDescription = enumRating.getShortDescription();
                }
            }
            if (this.productInfo.getSustainabilityRating() != null && this.productInfo.getSustainabilityRating().trim().length() > 0) {
                EnumSustainabilityRating enumRating = EnumSustainabilityRating.getEnumByStatusCode(this.productInfo.getSustainabilityRating());
                if (enumRating != null && enumRating.isEligibleToDisplay()) {
                    this.sustainabilityRating = enumRating.getStatusCodeInDisplayFormat();
                    this.sustainabilityRatingDescription = enumRating.getShortDescription();
                }
            }
        }
        // END i_product_skus_rating

    }
    
    public static Sku wrap(PriceCalculator priceCalc,SkuModel skuModel) {
        try {
            return new Sku(priceCalc, skuModel);
        } catch (FDResourceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FDSkuNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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
        return priceCalc.getPrice(0);
    }

    public String getFormattedPrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(getPrice());
    }

    public String getPriceUnit() {
        return productInfo.getDefaultPriceUnit();
    }

    public double getBasePrice() {
        try {
            //return priceCalc.getZonePriceInfoModel().getDefaultPrice();
            return priceCalc.getZonePriceInfoModel().getSellingPrice();
        } catch (FDResourceException e) {
            throw new RuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasWasPrice() {
        try {
            return priceCalc.getZonePriceInfoModel().isItemOnSale();
        } catch (FDResourceException e) {
            throw new RuntimeException(e);
        } catch (FDSkuNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFormattedBasePrice() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        return formatter.format(getBasePrice());
    }

    public String getBasePriceUnit() {
        return priceCalc.getDefaultUnitOnly();
    }

    public boolean hasVariationMatrix() {
        return (variationMatrix != null) && (variationMatrix.size() > 0);
    }

    public boolean hasVariationOptions() {
        return (variationOptions != null) && (variationOptions.size() > 0);
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
        String dvId = null;
        if((variationOptions != null) && (variationOptions.size() > 0)) {
            variationOptions.get(0).getContentName();        	
        }
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
        	if(getGroupPrice() > 0.0) return scales; //return empty array. If group scale is present do not return regular scales.
            if (wineLayout) {
                scales = priceCalc.getZonePriceModel().getWineScaleDisplay(true);;
            } else {
                scales = priceCalc.getZonePriceModel().getScaleDisplay();
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
            DomainValue secDomainValue = variationMatrix.get(1);
            DomainValue optDomainValue = null;

            if (this.hasVariationOptions() && (this.domain.getSkus().size() > 1) && !this.domain.isAllSameOption()) {
                optDomainValue = skuModel.getVariationOptions().get(0);
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
            DomainValue dv1 = variationMatrix.get(1);
            result = dv1.getPriority();
        }
        return result;
    }

    public int getPrimaryDomainValuePriority() {
        int result = 0;
        if (hasPrimaryDomain()) {
            DomainValue dv1 = variationMatrix.get(0);
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
        return priceCalc.getAboutPriceFormatted(0); 
    }

    public List<DomainValue> getVariationMatrix() {
        return this.variationMatrix;
    }
    
    //Added for Group Scale
    public FDGroup getFDGroup(){
    	if(getGroupPrice() > 0.0)
    		return productInfo.getGroup();
    	else 
    		return null;
   }
   
   public double getGroupPrice() {
	   return priceCalc.getGroupPrice();
   }
   public double getGroupQuantity() {
	   return priceCalc.getGroupQuantity();
   }

   public String getGroupScaleUnit() {
	   return priceCalc.getGroupScaleUnit();
   }
   
   public String getGroupPricingUnit() {
	   return priceCalc.getGroupPricingUnit();
   }
   
   public String getGroupShortDescription() {
   	try{
   		if(getFDGroup() == null) return null;
   		GroupScalePricing pricing = GroupScaleUtil.lookupGroupPricing(getFDGroup());
   		if(pricing != null)
   			return pricing.getShortDesc();
   		else
   			return null;

	    } catch (FDResourceException e) {
	        throw new FDRuntimeException(e);
	    }    	
   }

   public String getGroupLongDescription() {
   	try{
   		if(getFDGroup() == null) return null;
   		GroupScalePricing pricing = GroupScaleUtil.lookupGroupPricing(getFDGroup());
   		if(pricing != null)
   			return pricing.getLongDesc();
   		else
   			return null;
	    } catch (FDResourceException e) {
	        throw new FDRuntimeException(e);
	    }    	
   }
   
   public String getGroupLongOfferDescription() {
	   return priceCalc.getGroupLongOfferDescription();
   }
   
   public String getGroupShortOfferDescription() {
	   return priceCalc.getGroupShortOfferDescription();
   }

   public String getSustainabilityRating() {
       return sustainabilityRating;
   }

   public String getSustainabilityRatingDescription() {
       return sustainabilityRatingDescription;
   }
}
