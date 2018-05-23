package com.freshdirect.fdstore;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class FDConfiguration implements FDConfigurableI {

	private static final long	serialVersionUID	= -6923366786443915310L;

	/** quantity in sales unit */
	private final double quantity;

	/** Selected sales unit */
	private final String salesUnit;

	/** Selected options (characteristic name / char.value name pairs) */
	private final Map<String,String> options;

	public FDConfiguration(double quantity, String salesUnit) {
		this(quantity, salesUnit, Collections.<String,String>emptyMap());
	}

	/**
	 * @param quantity
	 *            quantity in sales unit
	 * @param salesUnit
	 *            Selected sales unit
	 * @param options
	 *            Map of selected options (characteristic name / char.value name
	 *            pairs)
	 */
	@JsonCreator
	public FDConfiguration(@JsonProperty("quantity") double quantity, @JsonProperty("salesUnit") String salesUnit,
			@JsonProperty("options") Map<String, String> options) {
		this.quantity = quantity;
		this.salesUnit = salesUnit;
		this.options = options;
	}

	public FDConfiguration(FDConfigurableI config) {
		this.quantity = config.getQuantity();
		this.salesUnit = config.getSalesUnit();
		this.options = config.getOptions();
	}

	public double getQuantity() {
		return this.quantity;
	}

	public String getSalesUnit() {
		return this.salesUnit;
	}

	public Map<String,String> getOptions() {
		return Collections.unmodifiableMap(this.options);
	}

	public String toString() {
		return "FDConfiguration[" + quantity + ", " + salesUnit + ", " + options + "]";
	}

	public int hashCode() {
		return (int) quantity ^ salesUnit.hashCode() ^ options.hashCode();
	}

	public boolean equals(Object o) {
		if (o instanceof FDConfiguration) {
			FDConfiguration conf = (FDConfiguration) o;
			if (this.salesUnit.equals(conf.getSalesUnit())
				&& this.quantity == conf.getQuantity()
				&& this.options.equals(conf.getOptions())) {
				return true;
			}
		}
		return false;
	}
}