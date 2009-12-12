package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.controller.data.ContainerSize.Size;
import com.freshdirect.mobileapi.controller.data.Image.ImageSizeType;
import com.freshdirect.mobileapi.controller.data.Product.ProductWarningMessage.ProductWarningMessageType;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.model.Brand;
import com.freshdirect.mobileapi.model.ComponentGroup;
import com.freshdirect.mobileapi.model.ProductDomain;
import com.freshdirect.mobileapi.model.Product.ImageType;

public class Product extends Message {

    public static class ProductWarningMessage {

        public ProductWarningMessage() {

        }

        public ProductWarningMessage(ProductWarningMessageType type, String title, String description) {
            this.type = type;
            this.title = title;
            this.description = description;
        }

        public enum ProductWarningMessageType {
            PLATTER_CUTOFF_NOTICE, DELIVERY_NOTE, DAY_OF_THE_WEEK_NOTICE
        };

        private String title;

        private ProductWarningMessageType type;

        private String description;

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public ProductWarningMessageType getType() {
            return type;
        }
    }

    private List<ProductWarningMessage> productWarningMessages = new ArrayList<ProductWarningMessage>();

    private static final Category LOGGER = LoggerFactory.getInstance(Product.class);

    public List<ProductWarningMessage> getProductWarningMessages() {
        return productWarningMessages;
    }

    public void setProductWarningMessages(List<ProductWarningMessage> productWarningMessages) {
        this.productWarningMessages = productWarningMessages;
    }

    public void addProductWarningMessage(ProductWarningMessage productWarningMessage) {
        this.productWarningMessages.add(productWarningMessage);
    }

    /**
     * Product layout handled for iphone 
     * @author fgarcia
     *
     */
    public enum LayoutType {
        PERISHABLE("perishable"), WINE("wine"), COMPONENT_GROUP_MEAL("componentGroupMeal");

        private String value;

        private LayoutType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    private String id;

    private String categoryId;

    private String productTerms;

    private String blockedDaysMessage;

    public String getBlockedDaysMessage() {
        return blockedDaysMessage;
    }

    public void setProductTerms(String productTerms) {
        this.productTerms = productTerms;
    }

    public String getProductTerms() {
        return productTerms;
    }

    /**
     * Default sku (if multi sku), or in case is single sku
     */
    private Sku sku;

    private List<BrandData> brandWithLogos = new ArrayList<BrandData>();

    private List<BrandData> brands = new ArrayList<BrandData>();

    private Image thumbBurst = null;

    private Image largeBurst = null;

    private List<Image> images = new ArrayList<Image>();

    private List<ContainerSize> deliBuyingGuide = new ArrayList<ContainerSize>();

    private List<ProductRating> productRatings = new ArrayList<ProductRating>();

    private List<SalesUnit> salesUnits = new ArrayList<SalesUnit>();

    private List<Variation> variations = new ArrayList<Variation>();

    private List<String> allegerns = new ArrayList<String>();

    private List<AlsoSoldAs> alsoSoldAs = new ArrayList<AlsoSoldAs>();

    private List<Domain> domains = new ArrayList<Domain>();

    private List<KosherRestriction> kosherRestrictions;

    private String ratingLabel;

    private ProductQuantity quantity;

    private boolean available;

    private boolean displayFrozenLogo;

    private LayoutType layoutType;

    private String fullName;

    private String akaName;

    private String ingredients;

    private boolean showDailyQualityRating;

    private String seasonText;

    private int heatRating;

    private Date cutoffTime;

    private boolean qualifiedForPromotions;

    private List<String> origin;

    private String howToBuyIt;

    private boolean oceanFriendly;

    private boolean prideOfNy;

    private float quantityMinimum;

    private float quantityMaximum;

    private float quantityIncrement;

    private String quantityText;

    private String priceLabel;

    private String winePromotion;

    private boolean healthWarning;

    private boolean autoConfigurable;

    //    private Date earliestAvailabilityDate;
    //  private String dayOfTheWeekNotice;
    //  private String cancellationNote;

    private String salesUnitLabel;

    private String autoConfiguredSalesUnit;

    private Integer highestDealPercentage;

    private boolean soldBySalesUnits;

    private boolean taxable;

    private boolean depositRequired;

    private boolean displayEstimatedQuantity;

    //private String platterCutoffMessage;

    private boolean soldByWeight;

    private boolean pricedByWeight;

    private String deliveryNote;

    public Product() {

    }

    //    public String getPlatterCutoffMessage() {
    //        return platterCutoffMessage;
    //    }

    public Integer getHighestDealPercentage() {
        return highestDealPercentage;
    }

    //    public Date getEarliestAvailabilityDate() {
    //        return earliestAvailabilityDate;
    //    }

    public Product(com.freshdirect.mobileapi.model.Product product) throws ModelException {
        setId(product.getProductId());
        setCategoryId(product.getCategoryId());
        setAkaName(product.getAkaName());
        setFullName(product.getProductTitle());
        setAvailable(product.isAvailable());
        if ("perishable".equals(product.getLayout())) {
            setLayoutType(LayoutType.PERISHABLE);
        }
        if ("wine".equals(product.getLayout())) {
            setLayoutType(LayoutType.WINE);
        }
        if ("componentGroupMeal".equals(product.getLayout())) {
            setLayoutType(LayoutType.COMPONENT_GROUP_MEAL);
        }
        setQuantityText(product.getQuantitText());
        setPriceLabel(product.getPriceLabel());

        if (product.getProductDomains().size() > 0) {
            for (ProductDomain productDomain : product.getProductDomains()) {
                Domain domain = new Domain(productDomain);
                domains.add(domain);
            }

        }
        if (product.isAvailable()) {
            setSku(Sku.wrap(product.getDefaultSku()));
        }
        for (Brand brand : product.getBrands()) {
            BrandData brandData = new BrandData(brand);
            brands.add(brandData);
        }
        for (Brand brand : product.getBrandWithLogos()) {
            BrandData brandData = new BrandData(brand);
            brandWithLogos.add(brandData);
        }

        for (com.freshdirect.mobileapi.model.Product asaProduct : product.getAlsoSoldAs()) {
            AlsoSoldAs asa = new AlsoSoldAs();
            asa.setCategoryId(asaProduct.getCategoryId());
            asa.setProductId(asaProduct.getProductId());
            asa.setProductName(asaProduct.getAlsoSoldAsName());
            alsoSoldAs.add(asa);
        }

        Image detailImage = new Image();
        com.freshdirect.fdstore.content.Image detailImg = null;
        if (LayoutType.WINE.equals(this.layoutType)) {
            detailImg = product.getImage(ImageType.WINE_ALT);
            if (detailImg == null) {
                LOGGER.warn("AlternateProductImag for wine was null. sku=" + getSku().getCode());
                detailImg = product.getImage(ImageType.DETAIL);
            }
        } else {
            detailImg = product.getImage(ImageType.DETAIL);
        }

        detailImage.setHeight(detailImg.getHeight());
        detailImage.setWidth(detailImg.getWidth());
        detailImage.setSource(detailImg.getPath());
        detailImage.setType(ImageSizeType.MEDIUM);

        Image productImage = new Image();
        com.freshdirect.fdstore.content.Image prodImg = product.getImage(ImageType.PRODUCT);
        productImage.setHeight(prodImg.getHeight());
        productImage.setWidth(prodImg.getWidth());
        productImage.setSource(prodImg.getPath());
        productImage.setType(ImageSizeType.THUMB);

        images.add(detailImage);
        images.add(productImage);

        com.freshdirect.fdstore.content.Image thumbBurstImage = product.getThumbBurstImage();
        if (thumbBurstImage != null) {
            this.thumbBurst = new Image(thumbBurstImage.getPath(), thumbBurstImage.getHeight(), thumbBurstImage.getWidth());
        }

        com.freshdirect.fdstore.content.Image largeBurstImage = product.getImage(ImageType.LARGE_BURST);
        if (largeBurstImage != null) {
            this.largeBurst = new Image(largeBurstImage.getPath(), largeBurstImage.getHeight(), largeBurstImage.getWidth());
        }

        if (product.hasHalfPint()) {
            ContainerSize container = new ContainerSize();
            container.setSize(Size.HALF_PINT);
            deliBuyingGuide.add(container);
        }
        if (product.hasPint()) {
            ContainerSize container = new ContainerSize();
            container.setSize(Size.PINT);
            deliBuyingGuide.add(container);
        }
        if (product.hasQuart()) {
            ContainerSize container = new ContainerSize();
            container.setSize(Size.QUART);
            deliBuyingGuide.add(container);
        }

        for (com.freshdirect.fdstore.content.view.ProductRating prodRating : product.getProductRatings()) {
            ProductRating productRating = new ProductRating();
            productRating.setName(prodRating.getRatingName());
            productRating.setRating(Integer.valueOf(prodRating.getRating()));
            this.productRatings.add(productRating);
        }

        setRatingLabel(product.getRatingLabel());

        for (com.freshdirect.mobileapi.model.SalesUnit su : product.getSalesUnit()) {
            SalesUnit salesUnit = new SalesUnit(su);
            this.salesUnits.add(salesUnit);
        }

        if (LayoutType.COMPONENT_GROUP_MEAL.equals(getLayoutType())) {
            List<ComponentGroup> componentGroups = product.getComponentGroups();
            for (ComponentGroup cgp : componentGroups) {
                for (com.freshdirect.mobileapi.model.Variation var : cgp.getVariations()) {
                    this.variations.add(new Variation(var));
                }
            }
        } else {
            for (com.freshdirect.mobileapi.model.Variation variation : product.getVariations()) {
                Variation var = new Variation(variation);
                this.variations.add(var);
            }
        }
        this.setAllegerns(product.getAllergens());
        this.setIngredients(product.getIngredients());
        this.setShowDailyQualityRating(product.showProductRating());
        this.setSeasonText(product.getSeasonText());
        this.setHeatRating(product.getHeatRating());
        this.setCutoffTime(product.getCutOffTime());

        this.setQualifiedForPromotions(product.isQualifiedForPromotions());

        this.setOrigin(product.getOrigin());
        this.setHowToBuyIt(product.getHowToBuyIt());
        this.setHealthWarning(product.isAlcoholProduct());

        this.setQuantityIncrement(product.getQuantityIncrement());
        this.setQuantityMaximum(product.getQuantityMaximum());
        this.setQuantityMinimum(product.getQuantityMinimum());
        this.setAutoConfigurable(product.isAutoConfigurable());
        try {
            Map<String, String> productKosherRestrictions = product.getKosherRestrictions();
            Iterator<String> names = productKosherRestrictions.keySet().iterator();
            while (names.hasNext()) {
                KosherRestriction restriction = new KosherRestriction();
                String name = names.next();
                String displayDate = productKosherRestrictions.get(name);
                restriction.setName(name);
                restriction.setDisplayDate(displayDate);
            }
        } catch (FDException e) {
            addErrorMessage("Error getting kosherRestrictions. Cause : " + e.getMessage());
            e.printStackTrace();
        }

        //this.earliestAvailabilityDate = product.getFilteredEarliestAvailabilityDate();
        this.salesUnitLabel = product.getSalesUnitLabel();
        this.autoConfiguredSalesUnit = product.getAutoConfiguredSalesUnit();
        this.soldBySalesUnits = product.isSoldBySalesUnits();

        if (product.getHighestDealPercentage() > 0) {
            this.highestDealPercentage = product.getHighestDealPercentage();
        }
        this.taxable = product.getDefaultProduct().isTaxable();
        this.depositRequired = product.getDefaultProduct().hasDeposit();

        if (product.hasTerms()) {
            this.productTerms = product.getProductTerms();
        }

        this.displayEstimatedQuantity = product.isDisplayEstimatedQuantity();
        if (product.isPlatter()) {
            String[] message = product.getPlatterCutoffMessage();
            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.PLATTER_CUTOFF_NOTICE, message[0], message[1]));
            //We dont need to send cancellation message
            //            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.PLATTER_CANCELLATION_NOTE, null, product
            //                    .getCancellationNote()));
        }

        this.soldByWeight = product.isSoldByLB();
        this.pricedByWeight = product.isPricedByLB();
        if (!product.getDayOfWeekNotice().isEmpty()) {
            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.DAY_OF_THE_WEEK_NOTICE, null, product
                    .getDayOfWeekNotice()));
        }
        if (!product.getDeliveryNote().isEmpty()) {
            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.DELIVERY_NOTE, null, product.getDeliveryNote()));
        }
        //RSUNG: this.deliveryNote = product.getDayOfWeekNotice();
        //RSUNG: this.dayOfTheWeekNotice = product.getDeliveryNote();
        //RSUNG: this.setCancellationNote(product.getCancellationNote());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public List<BrandData> getBrands() {
        return brands;
    }

    public List<BrandData> getBrandWithLogos() {
        return brandWithLogos;
    }

    public void setBrands(List<BrandData> brands) {
        this.brands = brands;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<ContainerSize> getDeliBuyingGuide() {
        return deliBuyingGuide;
    }

    public void setDeliBuyingGuide(List<ContainerSize> deliBuyingGuide) {
        this.deliBuyingGuide = deliBuyingGuide;
    }

    public List<ProductRating> getProductRatings() {
        return productRatings;
    }

    public void setProductRatings(List<ProductRating> productRatings) {
        this.productRatings = productRatings;
    }

    public List<SalesUnit> getSalesUnits() {
        return salesUnits;
    }

    public void setSalesUnits(List<SalesUnit> salesUnits) {
        this.salesUnits = salesUnits;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    public void setVariations(List<Variation> variations) {
        this.variations = variations;
    }

    public List<String> getAllegerns() {
        return allegerns;
    }

    public void setAllegerns(List<String> allegerns) {
        this.allegerns = allegerns;
    }

    public List<AlsoSoldAs> getAlsoSoldAs() {
        return alsoSoldAs;
    }

    public void setAlsoSoldAs(List<AlsoSoldAs> alsoSoldAs) {
        this.alsoSoldAs = alsoSoldAs;
    }

    public ProductQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(ProductQuantity quantity) {
        this.quantity = quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isDisplayFrozenLogo() {
        return displayFrozenLogo;
    }

    public void setDisplayFrozenLogo(boolean displayFrozenLogo) {
        this.displayFrozenLogo = displayFrozenLogo;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAkaName() {
        return akaName;
    }

    public void setAkaName(String akaName) {
        this.akaName = akaName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isShowDailyQualityRating() {
        return showDailyQualityRating;
    }

    public void setShowDailyQualityRating(boolean showDailyQualityRating) {
        this.showDailyQualityRating = showDailyQualityRating;
    }

    public String getSeasonText() {
        return seasonText;
    }

    public void setSeasonText(String seasonText) {
        this.seasonText = seasonText;
    }

    public int getHeatRating() {
        return heatRating;
    }

    public void setHeatRating(int heatRating) {
        this.heatRating = heatRating;
    }

    public Date getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(Date cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    //    public String getCancellationNote() {
    //        return cancellationNote;
    //    }
    //
    //    public void setCancellationNote(String cancellationNote) {
    //        this.cancellationNote = cancellationNote;
    //    }

    public boolean isQualifiedForPromotions() {
        return qualifiedForPromotions;
    }

    public void setQualifiedForPromotions(boolean qualifiedForPromotions) {
        this.qualifiedForPromotions = qualifiedForPromotions;
    }

    public List<String> getOrigin() {
        return origin;
    }

    public void setOrigin(List<String> origin) {
        this.origin = origin;
    }

    public String getHowToBuyIt() {
        return howToBuyIt;
    }

    public void setHowToBuyIt(String howToBuyIt) {
        this.howToBuyIt = howToBuyIt;
    }

    public boolean isOceanFriendly() {
        return oceanFriendly;
    }

    public void setOceanFriendly(boolean oceanFriendly) {
        this.oceanFriendly = oceanFriendly;
    }

    public boolean isPrideOfNy() {
        return prideOfNy;
    }

    public void setPrideOfNy(boolean prideOfNy) {
        this.prideOfNy = prideOfNy;
    }

    public float getQuantityMinimum() {
        return quantityMinimum;
    }

    public void setQuantityMinimum(float quantityMinimum) {
        this.quantityMinimum = quantityMinimum;
    }

    public float getQuantityMaximum() {
        return quantityMaximum;
    }

    public void setQuantityMaximum(float quantityMaximum) {
        this.quantityMaximum = quantityMaximum;
    }

    public float getQuantityIncrement() {
        return quantityIncrement;
    }

    public void setQuantityIncrement(float quantityIncrement) {
        this.quantityIncrement = quantityIncrement;
    }

    public String getWinePromotion() {
        return winePromotion;
    }

    public void setWinePromotion(String winePromotion) {
        this.winePromotion = winePromotion;
    }

    public boolean isHealthWarning() {
        return healthWarning;
    }

    public void setHealthWarning(boolean healthWarning) {
        this.healthWarning = healthWarning;
    }

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    public List<KosherRestriction> getKosherRestrictions() {
        return kosherRestrictions;
    }

    public void setKosherRestrictions(List<KosherRestriction> kosherRestrictions) {
        this.kosherRestrictions = kosherRestrictions;
    }

    public String getQuantityText() {
        return quantityText;
    }

    public void setQuantityText(String quantityText) {
        this.quantityText = quantityText;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }

    public String getRatingLabel() {
        return ratingLabel;
    }

    public void setRatingLabel(String ratingLabel) {
        this.ratingLabel = ratingLabel;
    }

    public boolean isAutoConfigurable() {
        return autoConfigurable;
    }

    public void setAutoConfigurable(boolean autoConfigurable) {
        this.autoConfigurable = autoConfigurable;
    }

    public String getSalesUnitLabel() {
        return salesUnitLabel;
    }

    /**
     * We're adding this methods so that iphone doesn't need to be updated with correct name.
     * @return
     */
    public String getSalesUnitsDescription() {
        return salesUnitLabel;
    }

    public String getAutoConfiguredSalesUnit() {
        return autoConfiguredSalesUnit;
    }

    public boolean isSoldBySalesUnits() {
        return soldBySalesUnits;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public boolean isDepositRequired() {
        return depositRequired;
    }

    public Image getThumbBurst() {
        return thumbBurst;
    }

    public Image getLargeBurst() {
        return largeBurst;
    }

    public boolean isDisplayEstimatedQuantity() {
        return displayEstimatedQuantity;
    }

    public boolean isSoldByWeight() {
        return soldByWeight;
    }

    public boolean isPricedByWeight() {
        return pricedByWeight;
    }

    //    public String getDayOfTheWeekNotice() {
    //        return dayOfTheWeekNotice;
    //    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

}
