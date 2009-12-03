package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mobileapi.model.Product.ImageType;
import com.freshdirect.webapp.util.CCFormatter;

public class ProductSearchResult {

    private static Category LOGGER = LoggerFactory.getInstance(ProductSearchResult.class);

    private boolean inCart;

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    private Integer highestDealPercentage;

    private List<SalesUnit> salesUnits = new ArrayList<SalesUnit>();

    private String id;

    private String categoryId;

    private String fullName;

    private Sku sku;

    private boolean available;

    private Image rollOverImage = new Image();

    private Image image = new Image();

    private Image thumbnail = new Image();

    private Image thumbBurst = null;

    private Image largeBurst = null;

    private double quantityMinimum;

    private double quantityMaximum;

    private double quantityIncrement;

    private double quantity;

    private String quantityLabel;

    private String configurationDescription;

    private boolean platter;

    private boolean autoConfigurable;

    private String checkoutInformation;

    private boolean soldByWeight;

    private boolean pricedByWeight;

    public boolean isPlatter() {
        return platter;
    }

    protected com.freshdirect.mobileapi.model.Product product;

    private Map<String, String> options = new HashMap<String, String>();

    private String cancellationNote;

    public Integer getHighestDealPercentage() {
        return highestDealPercentage;
    }

    public String getConfigurationDescription() {
        return configurationDescription;
    }

    public ProductSearchResult() {
    }

    public static ProductSearchResult wrap(com.freshdirect.mobileapi.model.Product product) {
        return new ProductSearchResult(product);
    }

    protected ProductSearchResult(com.freshdirect.mobileapi.model.Product product) {
        //ProductSearchResult result = new ProductSearchResult();
        this.product = product;
        if (product.isAvailable()) {
            this.sku = Sku.wrap(product.getDefaultSku());
        }
        this.fullName = product.getProductTitle();
        this.id = product.getProductId();
        this.categoryId = product.getCategoryId();
        this.available = product.isAvailable();

        com.freshdirect.fdstore.content.Image prodImage = product.getImage(ImageType.PRODUCT);
        if (prodImage != null) {
            this.image.setSource(prodImage.getPath());
            this.image.setHeight(prodImage.getHeight());
            this.image.setWidth(prodImage.getWidth());
        }

        com.freshdirect.fdstore.content.Image rollImage = product.getImage(ImageType.ROLLOVER);
        if (rollImage != null) {
            this.rollOverImage.setSource(rollImage.getPath());
            this.rollOverImage.setHeight(rollImage.getHeight());
            this.rollOverImage.setWidth(rollImage.getWidth());
        }

        com.freshdirect.fdstore.content.Image thumbImage = product.getImage(ImageType.THUMBNAIL);
        if (thumbImage != null) {
            this.thumbnail.setSource(thumbImage.getPath());
            this.thumbnail.setHeight(thumbImage.getHeight());
            this.thumbnail.setWidth(thumbImage.getWidth());
        }
        setQuantityMaximum(product.getQuantityMaximum());
        setQuantityMinimum(product.getQuantityMinimum());
        setQuantityIncrement(product.getQuantityIncrement());
        setQuantityLabel(product.getQuantitText());
        setAutoConfigurable(product.isAutoConfigurable());

        for (com.freshdirect.mobileapi.model.SalesUnit salesUnit : product.getSalesUnit()) {
            this.salesUnits.add(new SalesUnit(salesUnit));
        }

        configurationDescription = product.getConfigurationDescription();

        if (product.getHighestDealPercentage() > 0) {
            this.highestDealPercentage = product.getHighestDealPercentage();
        }

        com.freshdirect.fdstore.content.Image thumbBurstImage = product.getImage(ImageType.THUMB_BURST);
        if (thumbBurstImage != null) {
            this.thumbBurst = new Image(thumbBurstImage.getPath(), thumbBurstImage.getHeight(), thumbBurstImage.getWidth());
        }

        com.freshdirect.fdstore.content.Image largeBurstImage = product.getImage(ImageType.LARGE_BURST);
        if (largeBurstImage != null) {
            this.largeBurst = new Image(largeBurstImage.getPath(), largeBurstImage.getHeight(), largeBurstImage.getWidth());
        }

        //Set "in cart" flag...
        //This is based on sku level comparison
        long startTime = System.currentTimeMillis();
        inCart = product.isInProductInCart();
        long endTime = System.currentTimeMillis() - startTime;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Calculating in cart took:" + endTime + "msec");
        }

        this.platter = product.isPlatter();
        this.cancellationNote = product.getCancellationNote();
        if (product.getWarningMessages().size() > 0) {
            this.checkoutInformation = product.getWarningMessages().get(0);
        }

        this.soldByWeight = product.isSoldByLB();
        this.pricedByWeight = product.isPricedByLB();

    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public Image getThumbBurst() {
        return thumbBurst;
    }

    public Image getLargeBurst() {
        return largeBurst;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Image getRollOverImage() {
        return rollOverImage;
    }

    public void setRollOverImage(Image rollOverImage) {
        this.rollOverImage = rollOverImage;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getQuantityMinimum() {
        return quantityMinimum;
    }

    public void setQuantityMinimum(double quantityMinimum) {
        this.quantityMinimum = quantityMinimum;
    }

    public double getQuantityMaximum() {
        return quantityMaximum;
    }

    public void setQuantityMaximum(double quantityMaximum) {
        this.quantityMaximum = quantityMaximum;
    }

    public double getQuantityIncrement() {
        return quantityIncrement;
    }

    public void setQuantityIncrement(double quantityIncrement) {
        this.quantityIncrement = quantityIncrement;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getQuantityLabel() {
        return quantityLabel;
    }

    public void setQuantityLabel(String quantityLabel) {
        this.quantityLabel = quantityLabel;
    }

    public boolean isAutoConfigurable() {
        return autoConfigurable;
    }

    public void setAutoConfigurable(boolean autoConfigurable) {
        this.autoConfigurable = autoConfigurable;
    }

    public String getAutoConfigurableSalesUnit() {
        return product.getAutoConfiguredSalesUnit();
    }

    public boolean isSoldBySalesUnits() {
        return product.isSoldBySalesUnits();
    }

    public List<SalesUnit> getSalesUnits() {
        return salesUnits;
    }

    public void addOption(String key, String value) {
        this.options.put(key, value);
    }

    public String getCheckoutInformation() {
        return checkoutInformation;
    }

    public void setCheckoutInformation(String checkoutInformation) {
        this.checkoutInformation = checkoutInformation;
    }

    public boolean isSoldByWeight() {
        return soldByWeight;
    }

    public boolean isPricedByWeight() {
        return pricedByWeight;
    }

}
