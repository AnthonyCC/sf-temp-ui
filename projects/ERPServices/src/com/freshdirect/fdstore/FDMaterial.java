package com.freshdirect.fdstore;

import com.freshdirect.content.attributes.AttributesI;
import com.freshdirect.erp.EnumATPRule;
import com.freshdirect.erp.EnumAlcoholicContent;
import com.freshdirect.framework.util.DayOfWeekSet;

/**
 * FD class representing an ERP material.
 *
 * @version $Revision$
 * @author $Author$
 */
public class FDMaterial extends FDAttributeProxy {

	private static final long	serialVersionUID	= -3900022254504839075L;
	
	private final String materialNumber;
	private final EnumATPRule atpRule;
	private final String salesUnitCharacteristic;
	private final String quantityCharacteristic;
	private final EnumAlcoholicContent alcoholicContent;
	private final boolean taxable;
	private final boolean kosherProduction;
	private final boolean platter;
	private final DayOfWeekSet blockedDays;

	public FDMaterial(
		AttributesI attributes,
		String materialNumber,
		EnumATPRule atpRule,
		String salesUnitCharacteristic,
		String quantityCharacteristic,
		EnumAlcoholicContent alcoholicContent,
		boolean taxable,
		boolean kosherProduction,
		boolean platter,
		DayOfWeekSet blockedDays) {
		super(attributes);
		this.materialNumber = materialNumber;
		this.atpRule = atpRule;
		this.salesUnitCharacteristic = salesUnitCharacteristic;
		this.quantityCharacteristic = quantityCharacteristic;
		this.alcoholicContent = alcoholicContent;
		this.taxable = taxable;
		this.kosherProduction = kosherProduction;
		this.platter = platter;
		this.blockedDays = blockedDays;
	}

	public String getMaterialNumber() {
		return this.materialNumber;
	}

	public EnumATPRule getAtpRule() {
		return this.atpRule;
	}

	/**
	 * Get the name of the characteristic to put the selected sales unit in.
	 *
	 * @return null or empty String if none
	 */
	public String getSalesUnitCharacteristic() {
		return this.salesUnitCharacteristic;
	}

	/**
	 * Get the name of the characteristic to put the ordered quantity in.
	 *
	 * @return null or empty String if none
	 */
	public String getQuantityCharacteristic() {
		return this.quantityCharacteristic;
	}

	public EnumAlcoholicContent getAlcoholicContent() {
		return this.alcoholicContent;
	}

	public boolean isTaxable() {
		return this.taxable;
	}

	public boolean isKosherProduction() {
		return this.kosherProduction;
	}

	public boolean isPlatter() {
		return this.platter;
	}

	public DayOfWeekSet getBlockedDays() {
		return this.blockedDays == null ? DayOfWeekSet.EMPTY : this.blockedDays;
	}

}
