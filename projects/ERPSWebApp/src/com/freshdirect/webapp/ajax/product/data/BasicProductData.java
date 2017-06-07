package com.freshdirect.webapp.ajax.product.data;

import java.io.Serializable;
import java.util.Map;

import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;

public abstract class BasicProductData implements Serializable {

    private static final long serialVersionUID = -3918949741384207730L;

    // ============================ Product data ============================

    /**
     * Product content-ID
     */
    protected String productId;

    /**
     * Product full cms content key
     */
    protected String CMSKey;

    /**
     * Descriptive name of product (CMS: FULL_NAME + ProductModelImpl.getFullName() mocking!)
     */
    protected String productName;

    /**
     * Descriptive name of product (CMS: FULL_NAME + ProductModelImpl.getFullName() mocking! - brandName)
     */
    protected String productNameNoBrand;

    /**
     * Descriptive name of primary brand (CMS: brands[0].FULL_NAME)
     */
    protected String brandName;

    /**
     * Product image url (CMS: PROD_IMAGE)
     */
    protected String productImage;

    /**
     * Product detail image url (CMS: PROD_IMAGE_DETAIL)
     */
    protected String productDetailImage;

    /**
     * Product zoom image url (CMS: PROD_IMAGE_ZOOM)
     */
    protected String productZoomImage;

    /**
     * Product jumbo image url (CMS: PROD_IMAGE_JUMBO)
     */
    protected String productJumboImage;

    /**
     * Product alternate image url (CMS: ALTERNATE_IMAGE)
     */
    private String productAlternateImage;

    /**
     * Product package image url (CMS: PROD_IMAGE_PACKAGE)
     */
    private String productImagePackage;

    /**
     * Link to product page (FDURLUtil.getProductURI)
     */
    protected String productPageUrl;

    /**
     * Is alcoholic content? Requires alcohol warning popup?
     */
    protected boolean alcoholic = false;

    /**
     * Is USQ product? Needs extra USQ burst?
     */
    protected boolean usq = false;

    /**
     * Is product bazaar-voice enabled?
     */
    protected boolean bazaarVoice = false;

    /**
     * Alternative 'AKA' name (CMS: AKA)
     */
    protected String akaName;

    /**
     * Quantity description (CMS: QUANTITY_TEXT)
     */
    protected String quantityText;

    /**
     * Package description (CMS: PACKAGE_DESCRIPTION)
     */
    protected String packageDescription;

    /**
     * Is sold by sales unit? (CMS: SELL_BY_SALESUNIT == SALES_UNIT)
     */
    protected boolean soldBySalesUnit;

    /**
     * Savings (deals) description (generated)
     */
    protected String savingString;

    /**
     * Is mix'n'match (group scale pricing)
     */
    protected boolean mixNMatch;

    /**
     * Group scale pricing deal description - formatted, short (from ProductSavingTag.getGroupPrice, as on search page for example)
     */
    protected String dealInfo;

    /**
     * short description for group scale pricing
     */
    protected String grpShortDesc;

    /**
     * long description for group scale pricing
     */
    protected String grpLongDesc;

    /**
     * link for group scale pricing page
     */
    protected String grpLink;

    /**
     * group scale pricing - formatted price
     */
    protected String grpPrice;

    /**
     * identify for group
     */
    protected String grpId;

    /**
     * version for group
     */
    protected Integer grpVersion;

    // ============================ Transient data - postprocess may be needed! ============================

    /**
     * Amount of this product in current cart
     */
    protected double inCartAmount;

    /**
     * e-coupon object
     */
    protected FDCustomerCoupon coupon;

    /**
	 * 
	 */
    protected boolean couponDisplay;

    /**
     * Is coupon clipped?
     */
    protected boolean couponClipped;

    /**
	 * 
	 */
    protected String couponStatusText;

    /*
     * product has terms & conditions
     */
    protected boolean hasTerms;
    /*
     * standing order data
     */
    protected Map<String, Object> soData;

    public Map<String, Object> getSoData() {
        return soData;
    }

    public void setSoData(Map<String, Object> soData) {
        this.soData = soData;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameNoBrand() {
        return productNameNoBrand;
    }

    public void setProductNameNoBrand(String productNameNoBrand) {
        this.productNameNoBrand = productNameNoBrand;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductDetailImage() {
        return productDetailImage;
    }

    public void setProductDetailImage(String productDetailImage) {
        this.productDetailImage = productDetailImage;
    }

    public String getProductZoomImage() {
        return productZoomImage;
    }

    public void setProductZoomImage(String productZoomImage) {
        this.productZoomImage = productZoomImage;
    }

    public String getProductJumboImage() {
        return productJumboImage;
    }

    public void setProductJumboImage(String productJumboImage) {
        this.productJumboImage = productJumboImage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getInCartAmount() {
        return inCartAmount;
    }

    public void setInCartAmount(double inCartAmount) {
        this.inCartAmount = inCartAmount;
    }

    public String getDealInfo() {
        return dealInfo;
    }

    public void setDealInfo(String dealInfo) {
        this.dealInfo = dealInfo;
    }

    public boolean isSoldBySalesUnit() {
        return soldBySalesUnit;
    }

    public void setSoldBySalesUnit(boolean soldBySalesUnit) {
        this.soldBySalesUnit = soldBySalesUnit;
    }

    public String getSavingString() {
        return savingString;
    }

    public void setSavingString(String savingString) {
        this.savingString = savingString;
    }

    public FDCustomerCoupon getCoupon() {
        return coupon;
    }

    public void setCoupon(FDCustomerCoupon coupon) {
        this.coupon = coupon;
    }

    public boolean isCouponDisplay() {
        return couponDisplay;
    }

    public void setCouponDisplay(boolean couponDisplay) {
        this.couponDisplay = couponDisplay;
    }

    public boolean isCouponClipped() {
        return couponClipped;
    }

    public void setCouponClipped(boolean couponClipped) {
        this.couponClipped = couponClipped;
    }

    public String getCouponStatusText() {
        return couponStatusText;
    }

    public void setCouponStatusText(String couponStatusText) {
        this.couponStatusText = couponStatusText;
    }

    public boolean isMixNMatch() {
        return mixNMatch;
    }

    public void setMixNMatch(boolean mixNMatch) {
        this.mixNMatch = mixNMatch;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    public boolean isUsq() {
        return usq;
    }

    public void setUsq(boolean usq) {
        this.usq = usq;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public void setProductPageUrl(String productPageUrl) {
        this.productPageUrl = productPageUrl;
    }

    public String getAkaName() {
        return akaName;
    }

    public void setAkaName(String akaName) {
        this.akaName = akaName;
    }

    public String getQuantityText() {
        return quantityText;
    }

    public void setQuantityText(String quantityText) {
        this.quantityText = quantityText;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getGrpShortDesc() {
        return grpShortDesc;
    }

    public void setGrpShortDesc(String grpShortDesc) {
        this.grpShortDesc = grpShortDesc;
    }

    public String getGrpLongDesc() {
        return grpLongDesc;
    }

    public void setGrpLongDesc(String grpLongDesc) {
        this.grpLongDesc = grpLongDesc;
    }

    public String getGrpLink() {
        return grpLink;
    }

    public void setGrpLink(String grpLink) {
        this.grpLink = grpLink;
    }

    public String getGrpPrice() {
        return grpPrice;
    }

    public void setGrpPrice(String grpPrice) {
        this.grpPrice = grpPrice;
    }

    public String getGrpId() {
        return grpId;
    }

    public void setGrpId(String grpId) {
        this.grpId = grpId;
    }

    public Integer getGrpVersion() {
        return grpVersion;
    }

    public void setGrpVersion(Integer grpVersion) {
        this.grpVersion = grpVersion;
    }

    public boolean isBazaarVoice() {
        return bazaarVoice;
    }

    public void setBazaarVoice(boolean bazaarVoice) {
        this.bazaarVoice = bazaarVoice;
    }

    public void setCMSKey(String string) {
        this.CMSKey = string;
    }

    public String getCMSKey() {
        return CMSKey;
    }

    public void setHasTerms(boolean hasTerms) {
        this.hasTerms = hasTerms;
    }

    public boolean getHasTerms() {
        return hasTerms;
    }

    public String getProductAlternateImage() {
        return productAlternateImage;
    }

    public void setProductAlternateImage(String productAlternateImage) {
        this.productAlternateImage = productAlternateImage;
    }

    public String getProductImagePackage() {
        return productImagePackage;
    }

    public void setProductImagePackage(String productImagePackage) {
        this.productImagePackage = productImagePackage;
    }
}
