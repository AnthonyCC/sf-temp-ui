/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import com.freshdirect.erp.model.ErpCharacteristicValueModel;


/**
 * Variation Option - equivalent of ErpCharacteristicValue.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDVariationOption extends FDAttributesProxy {
	private static final long serialVersionUID = 4970965586298269545L;

	/** SAP characteristic value name */
	private String name;

	/** Chararcteristic value description */
	private String description;

	public FDVariationOption(ErpCharacteristicValueModel model) {
		super(model);
		this.name=model.getName();
		this.description = model.getDescription();
	}
	
	public FDVariationOption(String name, String description) {
	    this.name = name;
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
		return getAttributes().getDescription(description);
	}

	/**
	 * @deprecated Use isSelected() instead
	 */
	@Deprecated
	public boolean isDefault() {
		return this.isSelected();
	}
	
	public boolean isSelected() {
		return getAttributes().isSelected();
	}

	public boolean isLabelValue() {
		return getAttributes().isLabelValue();
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
