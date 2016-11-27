package com.freshdirect.cms.meta;

import java.util.Collections;
import java.util.Map;

import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;

/**
 * Trivial implementation of {@link com.freshdirect.cms.EnumDefI}.
 */
public class EnumDef extends AttributeDef implements EnumDefI {

	private final EnumAttributeType valueType;
	private final Map<Object,String> values;

	/**
	 * 
	 * @param name
	 * @param label
	 * @param required
	 * @param inheritable
	 * @param readOnly
	 * @param valueType
	 * @param values Map of Object (value) -> String (label)
	 */
	public EnumDef(String name, String label, boolean required, boolean inheritable, boolean readOnly, EnumAttributeType valueType, Map<Object,String> values) {
		super(EnumAttributeType.ENUM, name, label, required, inheritable, readOnly, EnumCardinality.ONE);
		this.values = Collections.unmodifiableMap(values);
		this.valueType = valueType;
	}

	public Map<Object,String> getValues() {
		return this.values;
	}

	public EnumAttributeType getValueType() {
		return valueType;
	}

	public Object getEmptyValue() {
		for( Object o : values.keySet() ) {
			return o;
		}
		return null;
	}

}