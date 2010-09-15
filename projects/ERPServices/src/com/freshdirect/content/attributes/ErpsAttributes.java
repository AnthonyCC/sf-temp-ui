package com.freshdirect.content.attributes;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDVersion;

public class ErpsAttributes implements Serializable, FDVersion<Date>, Cloneable {
	private static final long serialVersionUID = -1674926332717768971L;

	ErpsAttributesKey key;

	private String description;
	private Boolean optional;
	private Integer priority;
	private String displayFormat;
	private Boolean labelValue;
	private Boolean defaultValue;
	private String underLabel;
	private Boolean selected;
	private String productCode;
	private String displayGroup;
	private Boolean taxable;
	private Boolean custPromo;
	private String skucode;
	private String labelName;
	private String pricingUnitDescription;
	private Integer depositAmount;
	private Boolean kosherProduction;
	private String restrictions;
	private String specialProduct;
    private Boolean advanceOrderFlag;
    private String newProdDate;
    private String backInStock;

	private Date lastModified;

    public ErpsAttributes(ErpsAttributesKey key) {
    	this.key = key;
    }
	
    public ErpsAttributes(ErpsAttributesKey key, Date lastModified) {
    	this.key = key;
    	this.lastModified = lastModified;
    }
    
    public ErpsAttributes(String rootId, String childId, String grandChildId, Date lastModified) {
    	this(new ErpsAttributesKey(rootId, childId, grandChildId), lastModified); 
    }

	public Object getAttributeValue(EnumAttributeName attr) {
		switch (attr) {
		case DESCRIPTION:
			return description;
		case OPTIONAL:
			return optional;
		case PRIORITY:
			return priority;
		case DISPLAY_FORMAT:
			return displayFormat;
		case LABEL_VALUE:
			return labelValue;
		case DEFAULT:
			return defaultValue;
		case UNDER_LABEL:
			return underLabel;
		case SELECTED:
			return selected;
		case PRODUCT_CODE:
			return productCode;
		case DISPLAY_GROUP:
			return displayGroup;
		case TAXABLE:
			return taxable;
		case CUST_PROMO:
			return custPromo;
		case SKUCODE:
			return skucode;
		case LABEL_NAME:
			return labelName;
		case PRICING_UNIT_DESCRIPTION:
			return pricingUnitDescription;
		case DEPOSIT_AMOUNT:
			return depositAmount;
		case KOSHER_PRODUCTION:
			return kosherProduction;
		case RESTRICTIONS:
			return restrictions;
		case SPECIALPRODUCT:
			return specialProduct;
		case ADVANCE_ORDER_FLAG:
			return advanceOrderFlag;
		case NEW_PRODUCT_DATE:
			return newProdDate;
		case BACK_IN_STOCK_DATE:
			return backInStock;
		}
		return null;
	}
	
	public Map<EnumAttributeName, Object> getUpdatedAttributes() {
		Map<EnumAttributeName, Object> attrs = new HashMap<EnumAttributeName, Object>(EnumAttributeName.values().length);
		for (EnumAttributeName attr : EnumAttributeName.values()) {
			Object value = getAttributeValue(attr);
			if (value != null)
				attrs.put(attr, value);
		}
		return attrs;
	}
    
	public void setAttributeValue(EnumAttributeName attr, Object value, Date date) {
		switch (attr) {
		case DESCRIPTION:
			description = (String) value;
			updateVersion(date);
			return;
		case OPTIONAL:
			optional = (Boolean) value;
			updateVersion(date);
			return;
		case PRIORITY:
			priority = (Integer) value;
			updateVersion(date);
			return;
		case DISPLAY_FORMAT:
			displayFormat = (String) value;
			updateVersion(date);
			return;
		case LABEL_VALUE:
			labelValue = (Boolean) value;
			updateVersion(date);
			return;
		case DEFAULT:
			defaultValue = (Boolean) value;
			updateVersion(date);
			return;
		case UNDER_LABEL:
			underLabel = (String) value;
			updateVersion(date);
			return;
		case SELECTED:
			selected = (Boolean) value;
			updateVersion(date);
			return;
		case PRODUCT_CODE:
			productCode = (String) value;
			updateVersion(date);
			return;
		case DISPLAY_GROUP:
			displayGroup = (String) value;
			updateVersion(date);
			return;
		case TAXABLE:
			taxable = (Boolean) value;
			updateVersion(date);
			return;
		case CUST_PROMO:
			custPromo = (Boolean) value;
			updateVersion(date);
			return;
		case SKUCODE:
			skucode = (String) value;
			updateVersion(date);
			return;
		case LABEL_NAME:
			labelName = (String) value;
			updateVersion(date);
			return;
		case PRICING_UNIT_DESCRIPTION:
			pricingUnitDescription = (String) value;
			updateVersion(date);
			return;
		case DEPOSIT_AMOUNT:
			depositAmount = (Integer) value;
			updateVersion(date);
			return;
		case KOSHER_PRODUCTION:
			kosherProduction = (Boolean) value;
			updateVersion(date);
			return;
		case RESTRICTIONS:
			restrictions = (String) value;
			updateVersion(date);
			return;
		case SPECIALPRODUCT:
			specialProduct = (String) value;
			updateVersion(date);
			return;
		case ADVANCE_ORDER_FLAG:
			advanceOrderFlag = (Boolean) value;
			updateVersion(date);
			return;
		case NEW_PRODUCT_DATE:
			newProdDate = (String) value;
			updateVersion(date);
			return;
		case BACK_IN_STOCK_DATE:
			backInStock = (String) value;
			updateVersion(date);
			return;
		case RESET_TO_DEFAULT :
		        updateVersion(date);
                        return;
		}
	}

    /**
     * @return the attributes key
     */
    public ErpsAttributesKey getKey() {
		return key;
	}

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @return the description
     */
    public String getDescription(String defaultValue) {
        return description != null ? description : defaultValue;
    }
    
    public void setDescription(String description) {
		this.description = isNull(description) ? null : description;
	}

    /**
     * @return the optional
     */
    public boolean isOptional() {
        return optional != null ? optional : false;
    }
    
    public void setOptional(boolean optional) {
		this.optional = optional ? optional : null;
	}
    
    /**
     * @return the priority
     */
    public int getPriority() {
        return priority != null ? priority : 0;
    }
    
    public int getPriority(int defValue) {
        return priority != null ? priority : defValue;
    }

    public void setPriority(int priority) {
		this.priority = priority == 0 ? null : priority;
	}

    /**
     * @return the displayFormat
     */
    public String getDisplayFormat() {
        return displayFormat == null ? "dropdown" : displayFormat;
    }
    
    public void setDisplayFormat(String displayFormat) {
		this.displayFormat = "dropdown".equals(displayFormat) ? null : displayFormat;
	}

    /**
     * @return the labelValue
     */
    public boolean isLabelValue() {
        return labelValue != null ? labelValue : false;
    }
    
    public void setLabelValue(boolean labelValue) {
		this.labelValue = labelValue ? labelValue : null;
	}

    /**
     * @return the defaultValue
     */
    public boolean isDefaultValue() {
        return defaultValue != null ? defaultValue : false;
    }
    
    public void setDefaultValue(boolean defaultValue) {
		this.defaultValue = defaultValue ? defaultValue : null;
	}

    /**
     * @return the underLabel
     */
    public String getUnderLabel() {
        return underLabel;
    }
    
    public String getUnderLabel(String defValue) {
        return underLabel != null ? underLabel : defValue;
    }
    
    public void setUnderLabel(String underLabel) {
		this.underLabel = isNull(underLabel) ? null : underLabel;
	}

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected != null ? selected : false;
    }
    
    public void setSelected(boolean selected) {
		this.selected = selected ? selected : null;
	}

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
		this.productCode = isNull(productCode) ? null : productCode;
	}

    /**
     * @return the displayGroup
     */
    public String getDisplayGroup() {
        return displayGroup;
    }
    
    public void setDisplayGroup(String displayGroup) {
		this.displayGroup = isNull(displayGroup) ? null : displayGroup;
	}

    /**
     * @return the taxable
     */
    public boolean isTaxable() {
        return taxable;
    }
    
    public void setTaxable(boolean taxable) {
		this.taxable = taxable ? taxable : null;
	}

    /**
     * @return the custPromo
     */
    public boolean isCustPromo() {
        return custPromo != null ? custPromo : false;
    }
    
    public void setCustPromo(boolean custPromo) {
		this.custPromo = custPromo ? custPromo : null;
	}

    /**
     * @return the skucode
     */
    public String getSkucode() {
        return skucode;
    }
    
    public void setSkucode(String skucode) {
		this.skucode = isNull(skucode) ? null : skucode;
	}

    /**
     * @return the labelName
     */
    public String getLabelName() {
        return labelName;
    }
    
    public void setLabelName(String labelName) {
		this.labelName = isNull(labelName) ? null : labelName;
	}

    /**
     * @return the pricingUnitDescription
     */
    public String getPricingUnitDescription() {
        return pricingUnitDescription;
    }

    public String getPricingUnitDescription(String defaultValue) {
        return pricingUnitDescription != null ? pricingUnitDescription : defaultValue;
    }
    
    public void setPricingUnitDescription(String pricingUnitDescription) {
		this.pricingUnitDescription = isNull(pricingUnitDescription) ? null : pricingUnitDescription;
	}
    
    /**
     * @return the depositAmount
     */
    public int getDepositAmount() {
        return depositAmount != null ? depositAmount : 0;
    }
    
    public void setDepositAmount(int depositAmount) {
		this.depositAmount = depositAmount == 0 ? null : depositAmount;
	}

    /**
     * @return the kosherProduction
     */
    public boolean isKosherProduction() {
        return kosherProduction != null ? kosherProduction : false;
    }
    
    public void setKosherProduction(boolean kosherProduction) {
		this.kosherProduction = kosherProduction ? kosherProduction : null;
	}

    /**
     * @return the restrictions
     */
    public String getRestrictions() {
        return restrictions;
    }
    
    public void setRestrictions(String restrictions) {
		this.restrictions = isNull(restrictions) ? null : restrictions;
	}

    /**
     * @return the specialProduct
     */
    public String getSpecialProduct() {
        return specialProduct;
    }
    
    public void setSpecialProduct(String specialProduct) {
		this.specialProduct = isNull(specialProduct) ? null : specialProduct;
	}

    /**
     * @return the advanceOrderFlag
     */
    public boolean isAdvanceOrderFlag() {
        return advanceOrderFlag != null ? advanceOrderFlag : false;
    }
    
    public void setAdvanceOrderFlag(boolean advanceOrderFlag) {
		this.advanceOrderFlag = advanceOrderFlag ? advanceOrderFlag : null;
	}

    /**
     * @return the newProdDate
     */
    public String getNewProdDate() {
        return newProdDate;
    }
    
    public void setNewProdDate(String newProdDate) {
		this.newProdDate = isNull(newProdDate) ? null : newProdDate;
	}

    /**
     * @return the backInStock
     */
    public String getBackInStock() {
        return backInStock;
    }
    
    public void setBackInStock(String backInStock) {
		this.backInStock = isNull(backInStock) ? null : backInStock;
	}
    
	@Override
	public Date getVersion() {
		return lastModified;
	}

    protected void updateVersion(Date date) {
        if (lastModified == null || date.after(lastModified)) {
            lastModified = date;
        }
    }

	@Override
	public ErpsAttributes clone() {
		try {
			return (ErpsAttributes) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new FDRuntimeException(e, "cloning failed");
		}
	}
	
	private boolean isNull(String value) {
		return value == null || value.length() == 0;
	}
}
