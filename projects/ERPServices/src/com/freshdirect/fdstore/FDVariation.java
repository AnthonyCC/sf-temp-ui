/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore;

import com.freshdirect.erp.model.ErpCharacteristicModel;

/**
 * Variaton - equivalent of ErpCharacteristic.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDVariation extends FDAttributesProxy {
	private static final long serialVersionUID = -1771593842781044141L;

	/** SAP characteristic name */
	private String name;

	/**
	 * @link aggregationByValue
	 */
	private FDVariationOption[] variationOptions;

	public FDVariation(ErpCharacteristicModel model, FDVariationOption[] variationOptions) {
		super(model);
		name = model.getName();
		this.variationOptions = variationOptions;
	}
	
	public FDVariation(String name, FDVariationOption[] variationOptions) {
	    super();
	    this.name = name;
	    this.variationOptions = variationOptions;
	}

	/**
	 * Get the SAP characteristic name (machine readable) of this variation.
	 *
	 * @return SAP name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the variation options.
	 *
	 * @return array of FDVariationOption objects
	 */
	public FDVariationOption[] getVariationOptions() {
		return this.variationOptions;
	}
	
	public FDVariationOption getVariationOption(String name) {
		for (int i = 0; i < variationOptions.length; i++) {
			if (name.equals(variationOptions[i].getName())) {
				return variationOptions[i];
			}
		}
		return null;
	}

	/**
	 * Get sales unit description (human readable).
	 * This returns the DESCRIPTION attribute.
	 * If no attribute is present, it returns the SAP characteristic name.
	 *
	 * @return description
	 */
	public String getDescription() {
		return getAttributes().getDescription(name);
	}

	/**
	 * Tells how to display this variation (eg. "dropdown" or "checkbox").
	 * Returns the DISPLAY_FORMAT attribute, defaults to "dropdown".
	 *
	 * @return display format string
	 */
	public String getDisplayFormat() {
		return getAttributes().getDisplayFormat();
	}

	/**
	 * Tells whether this variation is optional to select.
	 * Returns the OPTIONAL attribute, defaults to false.
	 * 
	 * @return true if this variation is optional
	 */
	public boolean isOptional() {
		return getAttributes().isOptional();
	}

	public boolean equals(Object o) {
		if (o instanceof FDVariation) {
			return this.name.equals( ((FDVariation)o).name );
		}	
		return false;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("FDVariation[");
		buf.append(this.name);
		for (int i=0; i<this.variationOptions.length; i++) {
			buf.append("\n\t\t").append( this.variationOptions[i].toString() );
		}
		buf.append("\n\t]");
		return buf.toString();
	}
	
	public String getUnderLabel() {
		return getAttributes().getUnderLabel();
	}
	
        public int getPriority() {
            return getAttributes().getPriority();
        }

        public int getPriority(int defValue) {
            return getAttributes().getPriority(defValue);
        }

        public String getDescription(String defValue) {
            return getAttributes().getDescription(defValue);
        }
        
}
