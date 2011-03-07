package com.freshdirect.mobileapi.controller.data;

import java.text.SimpleDateFormat;

import com.freshdirect.fdstore.FDGroup;

public class Sku {
    public enum ContentType {
        FD_SOURCE("fdSource"), FD_GRADE("fdGrade"), FD_FRENCHING("fdFrenching"), FD_RIPENESS("fdRipeness");

        String value;

        private ContentType(String value) {
            this.value = value;
        }

        public static ContentType getContentType(String value) {
            ContentType result = null;
            ContentType[] types = ContentType.values();
            for (ContentType type : types) {
                if (type.value.equals(value)) {
                    result = type;
                }
            }
            return result;
        }
    }

    private ContentType fdContentType;

    private String domainLabel;

    private String salesUnitDescription;

    private String displayAboutPrice;

    private double price;

    private String priceUnit;

    private double basePrice;

    private String basePriceUnit;

    private String skuRating;

    private String scaledPrices;

    private boolean hasWasPrice;

    private boolean defaultSku;

    private String earliestAvailability;

    private String code;
    
    private String groupId;
    
    private String groupVersion;
    
    private double groupPrice;
    
    private double groupQuantity;
    
    private String groupScaleUnit;
    
    private String groupPricingUnit;
    
    private String groupShortDesc;
    
    private String groupLongDesc;
    
    private String skuSustainabilityRating;

    public Sku() {
    }

    public String getEarliestAvailability() {
        return earliestAvailability;
    }

    public static Sku wrap(com.freshdirect.mobileapi.model.Sku sku) {
        Sku result = new Sku();
        result.price = sku.getPrice();
        result.priceUnit = sku.getPriceUnit();
        result.basePrice = sku.getBasePrice();
        result.basePriceUnit = sku.getBasePriceUnit();
        result.skuRating = sku.getRating();
        result.skuSustainabilityRating=sku.getSustainabilityRating();
        result.hasWasPrice = sku.hasWasPrice();
        result.domainLabel = sku.getDomainLabel();
        result.salesUnitDescription = sku.getSalesUnitDescription();
        result.displayAboutPrice = sku.getDisplayAboutPrice();

        if (sku.getFilteredEarliestAvailability() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(DateFormat.STANDARDIZED_DATE_FORMAT);
            result.earliestAvailability = formatter.format(sku.getFilteredEarliestAvailability());
        }

        result.setFdContentType(ContentType.getContentType(sku.getFdContentType()));
        StringBuffer buff = new StringBuffer();
        for (String scaledPrice : sku.getScaledPrices()) {
            buff.append(scaledPrice);
            buff.append(" ");
        }
        result.scaledPrices = buff.toString();
        result.setCode(sku.getSkuCode());
        FDGroup grp = sku.getFDGroup();
        result.setGroupId(grp != null ? grp.getGroupId():"");
        result.setGroupVersion(grp != null ? String.valueOf(grp.getVersion()):"");
        result.setGroupPrice(sku.getGroupPrice());
        result.setGroupQuantity(sku.getGroupQuantity());
        String grpScaleUnit = sku.getGroupScaleUnit();
        result.setGroupScaleUnit(grpScaleUnit != null ? grpScaleUnit : "");
        String grpPricingUnit = sku.getGroupPricingUnit();
        result.setGroupPricingUnit(grpPricingUnit != null ? grpPricingUnit : "");
        String shortDesc = sku.getGroupShortDescription();
        result.setGroupShortDesc(shortDesc != null ? shortDesc : "");
        String longDesc = sku.getGroupLongDescription();
        result.setGroupLongDesc(longDesc != null ? longDesc : "");
        return result;
    }

    public String getDomainLabel() {
        return domainLabel;
    }

    public void setDomainLabel(String domainLabel) {
        this.domainLabel = domainLabel;
    }

    public String getSalesUnitDescription() {
        return salesUnitDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String getSkuRating() {
        return skuRating;
    }

    public void setSkuRating(String skuRating) {
        this.skuRating = skuRating;
    }

    public String getScaledPrices() {
        return scaledPrices;
    }

    public void setScaledPrices(String scaledPrices) {
        this.scaledPrices = scaledPrices;
    }

    public String getBasePriceUnit() {
        return basePriceUnit;
    }

    public void setBasePriceUnit(String basePriceUnit) {
        this.basePriceUnit = basePriceUnit;
    }

    public ContentType getFdContentType() {
        return fdContentType;
    }

    public void setFdContentType(ContentType fdContentType) {
        this.fdContentType = fdContentType;
    }

    public boolean isHasWasPrice() {
        return hasWasPrice;
    }

    public void setHasWasPrice(boolean hasWasPrice) {
        this.hasWasPrice = hasWasPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDefaultSku() {
        return defaultSku;
    }

    public void setDefaultSku(boolean defaultSku) {
        this.defaultSku = defaultSku;
    }

    public String getDisplayAboutPrice() {
        return displayAboutPrice;
    }

    public void setDisplayAboutPrice(String displayAboutPrice) {
        this.displayAboutPrice = displayAboutPrice;
    }

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupVersion() {
		return groupVersion;
	}

	public void setGroupVersion(String groupVersion) {
		this.groupVersion = groupVersion;
	}

	public double getGroupPrice() {
		return groupPrice;
	}

	public void setGroupPrice(double groupPrice) {
		this.groupPrice = groupPrice;
	}

	public double getGroupQuantity() {
		return groupQuantity;
	}

	public void setGroupQuantity(double groupQuantity) {
		this.groupQuantity = groupQuantity;
	}

	public String getGroupScaleUnit() {
		return groupScaleUnit;
	}

	public void setGroupScaleUnit(String groupScaleUnit) {
		this.groupScaleUnit = groupScaleUnit;
	}

	public String getGroupPricingUnit() {
		return groupPricingUnit;
	}

	public void setGroupPricingUnit(String groupPricingUnit) {
		this.groupPricingUnit = groupPricingUnit;
	}

	public String getGroupShortDesc() {
		return groupShortDesc;
	}

	public void setGroupShortDesc(String groupShortDesc) {
		this.groupShortDesc = groupShortDesc;
	}

	public String getGroupLongDesc() {
		return groupLongDesc;
	}

	public void setGroupLongDesc(String groupLongDesc) {
		this.groupLongDesc = groupLongDesc;
	}

}
