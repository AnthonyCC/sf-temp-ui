package com.freshdirect.mobileapi.controller.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.util.UnitPriceUtil;
import com.freshdirect.mobileapi.controller.data.Product.ProductWarningMessage;
import com.freshdirect.mobileapi.controller.data.Product.ProductWarningMessage.ProductWarningMessageType;
import com.freshdirect.mobileapi.model.Product.ImageType;
import com.freshdirect.webapp.ajax.product.data.ProductData;

public class ProductSearchResult {

    private boolean inCart;

    private String formattedUnitPriceLabel;

    private Integer highestDealPercentage;

    private List<SalesUnit> salesUnits = new ArrayList<SalesUnit>();

    private String id;

    private String categoryId;
    
    private String departmentId;

    private String fullName;

    private Sku sku;

    private boolean available;

    private Image rollOverImage = new Image();

    private Image image = new Image();

    private Image thumbnail = new Image();
    
    private Image detailImage = new Image();
    
    private Image zoomImage = new Image();

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

    private boolean soldByWeight;

    private boolean pricedByWeight;
    
    private SortedSet<String> tags;
    
    //DOOR3 FD-iPad FDIP-644
    private String sashType = "";
    
    //Unit Pricing Fields
    private String utPrice;
	private String utSalesUnit;
	
	private ProductData productData;

    public String getFormattedUnitPriceLabel() {

        if ((null == formattedUnitPriceLabel) && (null != sku)) {
            StringBuilder currentPrice = new StringBuilder("$").append(String.format("%01.2f", sku.getPrice())).append("/").append(
                    sku.getPriceUnit().toLowerCase());
            if (sku.isHasWasPrice()) {
                formattedUnitPriceLabel = currentPrice.append(" (").append("$").append(String.format("%01.2f", sku.getBasePrice())).append(
                        ")").toString();
            } else {
                formattedUnitPriceLabel = currentPrice.toString();
            }
        }
        return formattedUnitPriceLabel;
    }

    public void setFormattedUnitPriceLabel(String formattedUnitPriceLabel) {
        this.formattedUnitPriceLabel = formattedUnitPriceLabel;
    }


    public boolean isPlatter() {
        return platter;
    }

    private Map<String, String> options = new HashMap<String, String>();

    public Integer getHighestDealPercentage() {
        return highestDealPercentage;
    }

    public String getConfigurationDescription() {
        return configurationDescription;
    }

    public ProductSearchResult() {
    }

    private List<ProductWarningMessage> productWarningMessages = new ArrayList<ProductWarningMessage>();

    public static ProductSearchResult wrap(com.freshdirect.mobileapi.model.Product product) {
        return new ProductSearchResult(product);
    }

    protected ProductSearchResult(com.freshdirect.mobileapi.model.Product product) {
        this.soldBySalesUnits = product.isSoldBySalesUnits();
        this.autoConfigurableSalesUnit = product.getAutoConfiguredSalesUnit();
        this.deliveryPass = product.isDeliveryPass();
        if (product.isAvailable()) {
            this.sku = Sku.wrap(product.getDefaultSku());
        }
        this.fullName = product.getProductTitle();
        this.id = product.getProductId();
        this.categoryId = product.getCategoryId();
        this.departmentId=product.getDepartmentId();
        this.available = product.isAvailable();

        com.freshdirect.storeapi.content.Image prodImage = product.getImage(ImageType.PRODUCT);
        if (prodImage != null) {
            this.image.setSource(prodImage.getPath());
            this.image.setHeight(prodImage.getHeight());
            this.image.setWidth(prodImage.getWidth());
        }

        com.freshdirect.storeapi.content.Image rollImage = product.getImage(ImageType.ROLLOVER);
        if (rollImage != null) {
            this.rollOverImage.setSource(rollImage.getPath());
            this.rollOverImage.setHeight(rollImage.getHeight());
            this.rollOverImage.setWidth(rollImage.getWidth());
        }

        com.freshdirect.storeapi.content.Image thumbImage = product.getImage(ImageType.THUMBNAIL);
        if (thumbImage != null) {
            this.thumbnail.setSource(thumbImage.getPath());
            this.thumbnail.setHeight(thumbImage.getHeight());
            this.thumbnail.setWidth(thumbImage.getWidth());
        }
        

        com.freshdirect.storeapi.content.Image detailImage = product.getImage(ImageType.DETAIL);
        
        if (detailImage != null) {
	        this.detailImage.setHeight(detailImage.getHeight());
	        this.detailImage.setWidth(detailImage.getWidth());
	        this.detailImage.setSource(detailImage.getPath());
        }        
        
        com.freshdirect.storeapi.content.Image zoomImg = product.getImage(ImageType.ZOOM);
        if(zoomImg != null) {
        this.zoomImage.setHeight(zoomImg.getHeight());
        this.zoomImage.setWidth(zoomImg.getWidth());
        this.zoomImage.setSource(zoomImg.getPath());
        }
        
        setQuantityMaximum(product.getQuantityMaximum());
        setQuantityMinimum(product.getQuantityMinimum());
        setQuantityIncrement(product.getQuantityIncrement());
        setQuantityLabel(product.getQuantitText());
        setAutoConfigurable(product.isAutoConfigurable());
        setProductData(product.getProductData());

        for (com.freshdirect.mobileapi.model.SalesUnit salesUnit : product.getSalesUnit()) {
            this.salesUnits.add(new SalesUnit(salesUnit));
        }

        configurationDescription = product.getConfigurationDescription();

        if (product.getHighestDealPercentage() > 0) {
            this.highestDealPercentage = product.getHighestDealPercentage();
        }

        com.freshdirect.storeapi.content.Image thumbBurstImage = product.getImage(ImageType.THUMB_BURST);
        if (thumbBurstImage != null) {
            this.thumbBurst = new Image(thumbBurstImage.getPath(), thumbBurstImage.getHeight(), thumbBurstImage.getWidth());
        }

        com.freshdirect.storeapi.content.Image largeBurstImage = product.getImage(ImageType.LARGE_BURST);
        if (largeBurstImage != null) {
            this.largeBurst = new Image(largeBurstImage.getPath(), largeBurstImage.getHeight(), largeBurstImage.getWidth());
        }

        //Set "in cart" flag...
        //This is based on sku level comparison
        inCart = product.isInProductInCart();

        this.platter = product.isPlatter();

        this.soldByWeight = product.isSoldByLB();
        this.pricedByWeight = product.isPricedByLB();

        if (product.isPlatter()) {
            String[] message = product.getPlatterCutoffMessage();
            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.PLATTER_CUTOFF_NOTICE, message[0], message[1]));
            //We dont need to send cancellation message
            //            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.PLATTER_CANCELLATION_NOTE, null, product
            //                    .getCancellationNote()));
        }
        if (!product.getDayOfWeekNotice().isEmpty()) {
            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.DAY_OF_THE_WEEK_NOTICE, null, product
                    .getDayOfWeekNotice()));
        }
        if (!product.getDeliveryNote().isEmpty()) {
            addProductWarningMessage(new ProductWarningMessage(ProductWarningMessageType.DELIVERY_NOTE, null, product.getDeliveryNote()));
        }
        
        // Code duplication FTW!
        // Seriously, guys, that's the third class that represents Product in this one module!
        this.setTags(product.getTags());
        
        //DOOR3 FD-iPad FDIP-644
        String prodsashType = product.getSashType();
        if( prodsashType != null )
        	this.setSashType( prodsashType );
        
        if(FDStoreProperties.isUnitPriceDisplayEnabled() && product.getDefaultProduct() != null){
        	FDSalesUnit su = product.getDefaultProduct().getDefaultSalesUnit();
        	if (su != null) {
        		String unitPrice = UnitPriceUtil.getUnitPrice(su, product.getDefaultPrice());
        		if(unitPrice != null) {
        			this.setUtPrice( unitPrice );
        			this.setUtSalesUnit( su.getUnitPriceUOM() );
        		}
        	}
        }
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
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

    public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
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
    
    public Image getDetailImage() {
        return detailImage;
    }

    public void setDetailImage(Image detailImage) {
        this.detailImage = detailImage;
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
    
    private boolean deliveryPass;

    private boolean soldBySalesUnits;

    private String autoConfigurableSalesUnit;

    public String getAutoConfigurableSalesUnit() {
        return this.autoConfigurableSalesUnit;
    }

    public boolean isSoldBySalesUnits() {
        return this.soldBySalesUnits;
    }

    public List<SalesUnit> getSalesUnits() {
        return salesUnits;
    }

    public void addOption(String key, String value) {
        this.options.put(key, value);
    }

    public List<ProductWarningMessage> getProductWarningMessages() {
        return productWarningMessages;
    }

    public void addProductWarningMessage(ProductWarningMessage productWarningMessage) {
        this.productWarningMessages.add(productWarningMessage);
    }

    public boolean isSoldByWeight() {
        return soldByWeight;
    }

    public boolean isPricedByWeight() {
        return pricedByWeight;
    }

	public SortedSet<String> getTags() {
		return tags;
	}

	public void setTags(SortedSet<String> tags) {
		this.tags = tags;
	}

	public String getSashType() {
		return sashType;
	}

	public void setSashType(String sashType) {
		this.sashType = sashType;
	}

	public String getUtPrice() {
		return utPrice;
	}

	public void setUtPrice(String utPrice) {
		this.utPrice = utPrice;
	}

	public String getUtSalesUnit() {
		return utSalesUnit;
	}

	public void setUtSalesUnit(String utSalesUnit) {
		this.utSalesUnit = utSalesUnit;
	}

	public Image getZoomImage() {
		return zoomImage;
	}

	public void setZoomImage(Image zoomImage) {
		this.zoomImage = zoomImage;
	}

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

	public boolean isDeliveryPass() {
		return deliveryPass;
	}

}
