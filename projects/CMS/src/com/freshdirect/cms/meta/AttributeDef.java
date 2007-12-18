package com.freshdirect.cms.meta;

import java.util.ArrayList;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.framework.util.StringUtil;


/**
 * Trivial implementation of {@link com.freshdirect.cms.AttributeDefI}.
 */
public class AttributeDef implements AttributeDefI {

	private final EnumAttributeType attrType;
	private final String name;
	private final String label;
	private final boolean required;
	private final boolean inheritable;
	private final EnumCardinality cardinality;
	private final boolean readOnly;

	/**
	 * Full constructor.
	 * 
	 * @param attrType
	 * @param name
	 * @param label
	 * @param required
	 * @param inheritable
	 * @param readOnly
	 * @param cardinality
	 */
	public AttributeDef(
		EnumAttributeType attrType,
		String name,
		String label,
		boolean required,
		boolean inheritable,
		boolean readOnly,
		EnumCardinality cardinality) {

		this.attrType = attrType;
		this.name = name;
		this.label = label;
		this.required = required;
		this.inheritable = inheritable;
		this.cardinality = cardinality;
		this.readOnly = readOnly;
	}

	/**
	 * Simplified constructor for convenience. Defaults to a non-navigable,
	 * non-inheritable, mutable attribute with cardinality ONE.
	 * 
	 * @param attrType
	 * @param name
	 * @param label
	 */
	public AttributeDef(EnumAttributeType attrType, String name, String label) {
		this(attrType, name, label, false, false, false, EnumCardinality.ONE);
	}

	public EnumCardinality getCardinality() {
		return cardinality;
	}

	public boolean isCardinalityOne() {
		return EnumCardinality.ONE.equals(cardinality);
	}

	public boolean isInheritable() {
		return inheritable;
	}

	public String getName() {
		return name;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public EnumAttributeType getAttributeType() {
		return attrType;
	}


	public String getLabel() {
		if (label == null || "".equals(label)) {
			return StringUtil.smartCapitalize(name);
		}
		return label;
	}

	public Object getEmptyValue() {
		if (EnumCardinality.MANY.equals(this.cardinality)) {
			return new ArrayList();
		}
		return this.attrType.getEmptyValue();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("AttributeDef[");
		sb.append(this.name).append(", ");
		if (this.required)
			sb.append("required ");
		if (this.inheritable)
			sb.append("inheritable ");
		sb.append(cardinality.getName()).append(' ');
		sb.append(attrType.getName());
		sb.append("]");
		return sb.toString();
	}

}