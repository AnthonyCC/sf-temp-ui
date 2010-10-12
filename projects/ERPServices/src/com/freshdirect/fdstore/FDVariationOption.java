/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.content.attributes.EnumAttributeName;

/**
 * Variation Option - equivalent of ErpCharacteristicValue.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDVariationOption extends FDAttributeProxy {

	/** SAP characteristic value name */
	private String name;

	/** Chararcteristic value description */
	private String description;

	public FDVariationOption(AttributesI attributes, String name, String description) {
		super(attributes);
		this.name=name;
		this.description = description;
	}

	/**
	 * Get the SAP characteristic value name (machine readable) of this variation option.
	 *
	 * @return SAP name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get sales unit description (human readable).
	 * This is from the batch feeds, but can be overriden by the DESCRIPTION attribute.
	 *
	 * @return description
	 */
	public String getDescription() {
		return this.getAttribute(EnumAttributeName.DESCRIPTION.getName(), this.description);
	}

	/**
	 * @deprecated Use isSelected() instead
	 */
	public boolean isDefault() {
		return this.isSelected();
	}
	
	/**
	 *
	 *
	 */
	public boolean isSelected() {
		return this.getAttributeBoolean( EnumAttributeName.SELECTED );
	}

	/**
	 *
	 *
	 */
	public boolean isLabelValue() {
		return this.getAttributeBoolean( EnumAttributeName.LABEL_VALUE );
	}

	public boolean equals(Object o) {
		if (o instanceof FDVariationOption) {
			return this.name.equals( ((FDVariationOption)o).name );
		}	
		return false;
	}

	public String toString() {
		return "FDVariationOption["+this.name+"]";
	}

	public String getSkuCode() {
		return getAttributes().getSkucode();
	}
}
