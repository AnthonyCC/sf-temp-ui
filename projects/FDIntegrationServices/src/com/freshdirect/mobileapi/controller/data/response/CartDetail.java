package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.ProductConfiguration;

public class CartDetail {
    private List<AffiliateCartDetail> affiliates = new ArrayList<AffiliateCartDetail>();

    private String platterCutoffTime;

    public String getPlatterCutoffTime() {
        return platterCutoffTime;
    }

    public void setPlatterCutoffTime(String platterCutoffTime) {
        this.platterCutoffTime = platterCutoffTime;
    }

    public List<AffiliateCartDetail> getAffiliates() {
        return affiliates;
    }

    public void setAffiliates(List<AffiliateCartDetail> affiliates) {
        this.affiliates = affiliates;
    }

    public void addAffiliates(AffiliateCartDetail affiliate) {
        this.affiliates.add(affiliate);
    }

    public float getCreditCharge() {
        return creditCharge;
    }

    public void setCreditCharge(float creditCharge) {
        this.creditCharge = creditCharge;
    }

    private float creditCharge = 0f;

    private double estimatedTotal;

    public double getEstimatedTotal() {
        return estimatedTotal;
    }

    public void setEstimatedTotal(double estimatedTotal) {
        this.estimatedTotal = estimatedTotal;
    }

    private double subtotal;

    private double tax;

    private double giftcardBalance;

    public double getGiftcardBalance() {
        return giftcardBalance;
    }

    public void setGiftcardBalance(double giftcardBalance) {
        this.giftcardBalance = giftcardBalance;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTax() {
        return tax;
    }

    private double depositValue;

    public void setDepositValue(double depositValue) {
        this.depositValue = depositValue;
    }

    public double getDepositValue() {
        return depositValue;
    }

    private boolean isDlvPassApplied = false;

    private double deliverySurcharge = 0.0;

    private boolean isDeliveryChargeWaived = false;

    private boolean isDeliveryChargeTaxable = false;

    private double miscellaneousCharge = 0.0;

    private String miscellaneousChargeLabel;

    private boolean isMiscellaneousChargeTaxable = false;

    private boolean isMiscellaneousChargeWaived = false;

    public boolean isDlvPassApplied() {
        return isDlvPassApplied;
    }

    public void setIsDlvPassApplied(boolean isDlvPassApplied) {
        this.isDlvPassApplied = isDlvPassApplied;
    }

    public double getDeliverySurcharge() {
        return deliverySurcharge;
    }

    public void setDeliverySurcharge(double deliverySurcharge) {
        this.deliverySurcharge = deliverySurcharge;
    }

    public boolean isDeliveryChargeWaived() {
        return isDeliveryChargeWaived;
    }

    public void setIsDeliveryChargeWaived(boolean isDeliveryChargeWaived) {
        this.isDeliveryChargeWaived = isDeliveryChargeWaived;
    }

    public boolean isDeliveryChargeTaxable() {
        return isDeliveryChargeTaxable;
    }

    public void setIsDeliveryChargeTaxable(boolean isDeliveryChargeTaxable) {
        this.isDeliveryChargeTaxable = isDeliveryChargeTaxable;
    }

    public double getMiscellaneousCharge() {
        return miscellaneousCharge;
    }

    public void setMiscellaneousCharge(double miscellaneousCharge) {
        this.miscellaneousCharge = miscellaneousCharge;
    }

    public boolean isMiscellaneousChargeTaxable() {
        return isMiscellaneousChargeTaxable;
    }

    public void setIsMiscellaneousChargeTaxable(boolean isMiscellaneousChargeTaxable) {
        this.isMiscellaneousChargeTaxable = isMiscellaneousChargeTaxable;
    }

    public boolean isMiscellaneousChargeWaived() {
        return isMiscellaneousChargeWaived;
    }

    public void setIsMiscellaneousChargeWaived(boolean isMiscellaneousChargeWaived) {
        this.isMiscellaneousChargeWaived = isMiscellaneousChargeWaived;
    }

    private List<Discount> discounts = new ArrayList<Discount>();

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public void addDiscount(Discount discount) {
        this.discounts.add(discount);
    }

    private List<RemptionPromotion> remptionPromotions = new ArrayList<RemptionPromotion>();

    public List<RemptionPromotion> getRemptionPromotions() {
        return remptionPromotions;
    }

    public void setRemptionPromotions(List<RemptionPromotion> remptionPromotions) {
        this.remptionPromotions = remptionPromotions;
    }

    public void addRemptionPromotion(RemptionPromotion remptionPromotion) {
        this.remptionPromotions.add(remptionPromotion);
    }

    private double customerCreditsValue = 0.0;

    public double getCustomerCreditsValue() {
        return customerCreditsValue;
    }

    public void setCustomerCreditsValue(double customerCreditsValue) {
        this.customerCreditsValue = customerCreditsValue;
    }

    private boolean isEstimatedPrice;

    public boolean isEstimatedPrice() {
        return isEstimatedPrice;
    }

    public void setIsEstimatedPrice(boolean isEstimatedPrice) {
        this.isEstimatedPrice = isEstimatedPrice;
    }

    /**
     * 
     * @author Rob
     *
     */
    public static class RemptionPromotion {

        public RemptionPromotion(String code, RemptionPromotionType type, String description, boolean automatic) {
            this.code = code;
            this.type = type;
            this.description = description;
            this.automatic = automatic;
        }

        public enum RemptionPromotionType {
            SAMPLE, WAIVE_CHARGE
        }

        public boolean isAutomatic() {
            return automatic;
        }

        private String description;

        private RemptionPromotionType type;

        private boolean automatic;

        private String code;

        private double amount = 0.0;

        public RemptionPromotionType getType() {
            return type;
        }

        public void setType(RemptionPromotionType type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

    }

    /**
     * Similiar to com.freshdirect.common.pricing.Discount. Presents various discounts that are added to the cart.
     * Discounts are applied either via redemption or automatically, and lowers the total prices in cart.
     * 
     * @author Rob
     */
    /*
     * DUP: com.freshdirect.common.pricing.Discount
     * LAST UPDATED ON: 10/5/2009
     * LAST UPDATED WITH SVN#: 5951
     * WHY: Even though discount class exists in common.pricing package. This class was created as it 
     *     better matches conditions that drives categorization in view. Also, didn't want to create an 
     *     unwanted dependencies between an DTO and FD common package.
     * WHAT: DTO encapsulating discount
     */
    public static class Discount {

        public Discount(String code, DiscountType type, double amount, boolean automatic, String description) {
            this(code, type, amount, automatic);
            this.description = description;
        }

        public Discount(String code, DiscountType type, double amount, boolean automatic) {
            this.code = code;
            this.type = type;
            this.amount = amount;
            this.automatic = automatic;
        }

        public enum DiscountType {
            PROMO, SIGNUP, AUTO_HEADER
        }

        private boolean automatic;
        
        public boolean isAutomatic() {
            return automatic;
        }

        private String description;

        private String code;

        private DiscountType type;

        private double amount;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public DiscountType getType() {
            return type;
        }

        public void setType(DiscountType type) {
            this.type = type;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

    }

    /**
     * Represents a affiliate grouping in cart.  e.g. USQ Wine is an affiliate as well as FD
     * 
     * @author Rob
     *
     */
    public static class AffiliateCartDetail {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private List<CartLineItem> lineItems = new ArrayList<CartLineItem>();

        public List<CartLineItem> getLineItems() {
            return lineItems;
        }

        public void setLineItems(List<CartLineItem> lineItems) {
            this.lineItems = lineItems;
        }

        public void addLineItems(CartLineItem lineItem) {
            this.lineItems.add(lineItem);
        }

        public double getTax() {
            return tax;
        }

        public void setTax(double tax) {
            this.tax = tax;
        }

        private double tax;

        private double subtotal;

        private boolean isEstimatedPrice;

        public void setIsEstimatedPrice(boolean estimatedPrice) {
            this.isEstimatedPrice = estimatedPrice;
        }

        public boolean isEstimatedPrice() {
            return isEstimatedPrice;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }

        private double depositValue;

        public void setDepositValue(double depositValue) {
            this.depositValue = depositValue;
        }

        public double getDepositValue() {
            return depositValue;
        }

    }

    public static class CartLineItem {

        protected CartLineItemType type;

        public enum CartLineItemType {
            DEPT, RECIPE, PRODUCT
        }

        public CartLineItemType getType() {
            return type;
        }

        public void setType(CartLineItemType type) {
            this.type = type;
        }

    }

    public static class Group extends CartLineItem {

        public Group(CartLineItemType type) {
            this.type = type;
        }

        private String id;

        private String name;

        private String imageUrl;

        private List<CartLineItem> lineItems = new ArrayList<CartLineItem>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public List<CartLineItem> getLineItems() {
            return lineItems;
        }

        public void setLineItems(List<CartLineItem> lineItems) {
            this.lineItems = lineItems;
        }

        public void addLineItem(CartLineItem lineItem) {
            this.lineItems.add(lineItem);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

    }

    /**
     * Represents DTO for line item in cart
     * 
     * @author Rob
     *
     */
    public static class ProductLineItem extends CartLineItem {

        public ProductLineItem() {
            type = CartLineItemType.PRODUCT;
        }

        private ProductConfiguration productConfiguration; //

        public ProductConfiguration getProductConfiguration() {
            return productConfiguration;
        }

        public void setProductConfiguration(ProductConfiguration productConfiguration) {
            this.productConfiguration = productConfiguration;
        }

        private Boolean newItem;

        private String cartLineId;

        //        private String name;
        //
        //        private String description;
        //
        //        private String quantityUnitLabel;
        //
        //        private String salesUnit;
        //
        //        private String quanity;
        //
        //        private String configurationDesc;

        private boolean hasKosherRestriction = false;

        private boolean isPlatter;

        public Boolean isNewItem() {
            return newItem;
        }

        public void setNewItem(Boolean newItem) {
            this.newItem = newItem;
        }

        public boolean isPlatter() {
            return isPlatter;
        }

        public void setPlatter(boolean isPlatter) {
            this.isPlatter = isPlatter;
        }

        public boolean isHasKosherRestriction() {
            return hasKosherRestriction;
        }

        public void setHasKosherRestriction(boolean hasKosherRestriction) {
            this.hasKosherRestriction = hasKosherRestriction;
        }

        //        public String getQuantityUnitLabel() {
        //            return quantityUnitLabel;
        //        }
        //
        //        public void setQuantityUnitLabel(String quantityUnitLabel) {
        //            this.quantityUnitLabel = quantityUnitLabel;
        //        }
        //
        //        public String getName() {
        //            return name;
        //        }
        //
        //        public void setName(String name) {
        //            this.name = name;
        //        }
        //
        //        public String getSalesUnit() {
        //            return salesUnit;
        //        }
        //
        //        public void setSalesUnit(String salesUnit) {
        //            this.salesUnit = salesUnit;
        //        }
        //
        //        public String getQuanity() {
        //            return quanity;
        //        }
        //
        //        public void setQuanity(String quanity) {
        //            this.quanity = quanity;
        //        }
        //
        //        public String getDescription() {
        //            return description;
        //        }
        //
        //        public void setDescription(String description) {
        //            this.description = description;
        //        }
        //
        //        public void setConfigurationDesc(String configurationDesc) {
        //            this.configurationDesc = configurationDesc;
        //        }
        //
        //        public String getConfigurationDesc() {
        //            return this.configurationDesc;
        //        }

        private boolean kosherProduction;

        public void setKosherProduction(boolean kosherProduction) {
            this.kosherProduction = kosherProduction;
        }

        public boolean getKosherProduction() {
            return this.kosherProduction;
        }

        private boolean modifiedLineItem;

        public boolean isModifiedLineItem() {
            return modifiedLineItem;
        }

        public void setModifiedLineItem(boolean modifiedLineItem) {
            this.modifiedLineItem = modifiedLineItem;
        }

        private boolean hasPlatterRestriction;

        public boolean hasPlatterRestriction() {
            return hasPlatterRestriction;
        }

        public void setHasPlatterRestriction(boolean hasPlatterRestriction) {
            this.hasPlatterRestriction = hasPlatterRestriction;
        }

        private String earliestAvailability;

        public String getEarliestAvailability() {
            return earliestAvailability;
        }

        public void setEarliestAvailability(String earliestAvailability) {
            this.earliestAvailability = earliestAvailability;
        }

        private double price;

        private String unitPrice;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        private boolean hasDepositValue;

        private boolean hasScaledPricing;

        private boolean isEstimatedPrice;

        private boolean hasTax;

        public boolean isHasDepositValue() {
            return hasDepositValue;
        }

        public void setHasDepositValue(boolean hasDepositValue) {
            this.hasDepositValue = hasDepositValue;
        }

        public boolean isHasScaledPricing() {
            return hasScaledPricing;
        }

        public void setHasScaledPricing(boolean hasScaledPricing) {
            this.hasScaledPricing = hasScaledPricing;
        }

        public boolean isEstimatedPrice() {
            return isEstimatedPrice;
        }

        public void setEstimatedPrice(boolean isEstimatedPrice) {
            this.isEstimatedPrice = isEstimatedPrice;
        }

        public boolean isHasTax() {
            return hasTax;
        }

        public void setHasTax(boolean hasTax) {
            this.hasTax = hasTax;
        }

        public String getCartLineId() {
            return cartLineId;
        }

        public void setCartLineId(String cartLineId) {
            this.cartLineId = cartLineId;
        }

    }

    public void setMiscellaneousChargeLabel(String miscChargeLabel) {
       this.miscellaneousChargeLabel = miscChargeLabel;        
    }

    public String getMiscellaneousChargeLabel() {
        return this.miscellaneousChargeLabel;
    }


}
