package com.freshdirect.cms.application.service.db;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ListIterator;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;

/**
 * Utility methods for converting String representations of attributes
 * to the appropriate type.
 */
class DbContentUtil {

	private static final DateFormat	dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private DbContentUtil() {
	}

	/**
	 * Converts string representations to the appropriate attribute type.
	 * 
	 * <b>Note:</b> Lists are converted <em>in place</em> and returned for convenience.
	 * 
	 * @param atrDef attribute definition, never null
	 * @param values List of String, never null
	 * @return attribute value, never null
	 */
	public static Object convertAttributeValues(AttributeDefI atrDef, List values) {
		EnumAttributeType valueType = atrDef.getAttributeType();
		if (EnumAttributeType.ENUM.equals(valueType)) {
			valueType = ((EnumDefI) atrDef).getValueType();
		}

		if (EnumCardinality.ONE.equals(atrDef.getCardinality())) {
			return convertAttributeValue(valueType, (String) values.get(0));
		}
		// MANY
		for (ListIterator i = values.listIterator(); i.hasNext();) {
			String v = (String) i.next();
			i.set(convertAttributeValue(valueType, v));
		}
		return values;
	}

	/**
	 * Coerce a string representation of a value to the appropriate
	 * attribute type.
	 * 
	 * @param type attribute type to coerce the value to
	 * @param value value, never null
	 * @return the attribute coerced to the appropriate type
	 */
	public static Object convertAttributeValue(EnumAttributeType type, String value) {
		if (EnumAttributeType.BOOLEAN.equals(type)) {
			return Boolean.valueOf(value);
		} else if (EnumAttributeType.INTEGER.equals(type)) {
			return Integer.valueOf(value);
		} else if (EnumAttributeType.DOUBLE.equals(type)) {
			return Double.valueOf(value);
		} else if (EnumAttributeType.RELATIONSHIP.equals(type)) {
			return ContentKey.decode(value);
		} else if (EnumAttributeType.DATE.equals(type)) {
			try {
				return dateFormat.parse(value);
			} catch (ParseException e) {
				throw new CmsRuntimeException(e);
			}
		}

		return value;
	}

}