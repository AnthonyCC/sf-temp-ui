package com.freshdirect.cms.meta;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.ContentTypeServiceI;

/**
 * Utility methods to operate on content type definitions.
 */
public class ContentTypeUtil {

	private static final DateFormat	dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private ContentTypeUtil() {
	}

	/**
	 * Get all navigable relationship definitions.
	 * 
	 * @return Collection of {@link RelationshipDefI} 
	 */
	public static Collection<RelationshipDefI> getNavigableRelationshipDefs(ContentTypeDefI definition) {
		if (definition==null) {
			return Collections.EMPTY_LIST;
		}
		List<RelationshipDefI> l = new ArrayList<RelationshipDefI>();		
		for ( AttributeDefI def : definition.getSelfAttributeDefs() ) {
			if (def instanceof RelationshipDefI && ((RelationshipDefI) def).isNavigable()) {
				l.add((RelationshipDefI) def);
			}
		}
		return l;
	}

	/**
	 * Recursively get navigable content types.
	 * 
	 * @return Set of ContentType
	 */
	public static Set<ContentType> getReachableContentTypes(ContentTypeServiceI typeService, ContentTypeDefI definition) {
		return collectReachableTypes(typeService, definition, new HashSet<ContentType>());
	}

	private static Set<ContentType> collectReachableTypes(ContentTypeServiceI typeService, ContentTypeDefI definition, Set<ContentType> typeRefs) {
		for (RelationshipDefI rel : getNavigableRelationshipDefs(definition)) {
			for (ContentType ctr : rel.getContentTypes()) {
				if (!typeRefs.contains(ctr)) {
					typeRefs.add(ctr);
					ContentTypeDef typeDef = (ContentTypeDef) typeService.getContentTypeDefinition(ctr);
					collectReachableTypes(typeService, typeDef, typeRefs);
				}
			}
		}
		return typeRefs;
	}

	/**
	 * Coerce an attribute value to the type specified by an {@link EnumAttributeType}.
	 * 
	 * @param type attribute type to coerce the value to
	 * @param value object value to coerce
	 * @return the attribute value coerced to the appropriate type
	 */
	public static Object coerce(EnumAttributeType type, Object value) {
		if (value == null) {
			return null;
		}
		
		// convert from a string representation
		if (value instanceof String) {
			String str = (String)value;
			if (EnumAttributeType.STRING.equals(type)) {
				return str;
			} else if (EnumAttributeType.BOOLEAN.equals(type)) {
				return Boolean.valueOf(str);
			} else if (EnumAttributeType.INTEGER.equals(type)) {
				return Integer.valueOf(str);
			} else if (EnumAttributeType.DOUBLE.equals(type)) {
				return Double.valueOf(str);
			} else if (EnumAttributeType.RELATIONSHIP.equals(type)) {
				return ContentKey.decode(str);
			} else if (EnumAttributeType.DATE.equals(type)) {
				try {
					return dateFormat.parse(str);
				} catch (ParseException e) {
					throw new IllegalArgumentException(e.toString());
				}
			}
			throw new IllegalArgumentException(
					"Unable to convert value value [" + value + "] ("
							+ value.getClass().getName() + ") to "
							+ type.getLabel());
		}
		
		// convert from other objects
		if (EnumAttributeType.DOUBLE.equals(type)) {
			if (value instanceof Double) {
				return value;
			} else if (value instanceof Number) {
				return new Double(((Number) value).doubleValue());
			}
		} else if (EnumAttributeType.INTEGER.equals(type)) {
			if (value instanceof Integer) {
				return value;
			} else if (value instanceof Number) {
				return new Integer(((Number) value).intValue());
			}
		} else if (EnumAttributeType.STRING.equals(type)) {
			return value.toString();
		} else if (EnumAttributeType.DATE.equals(type)) {
			if (value instanceof java.sql.Date) {
				return new Date(((java.sql.Date) value).getTime());
			} else if (value instanceof Date) {
				return value;
			}
		}
		
		// make a final attempt by making it a String
		return coerce(type, value.toString());
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
	public static Object convertAttributeValues(AttributeDefI atrDef, List<String> values) {
		EnumAttributeType valueType = atrDef.getAttributeType();
		if (EnumAttributeType.ENUM.equals(valueType)) {
			valueType = ((EnumDefI) atrDef).getValueType();
		}

		if (EnumCardinality.ONE.equals(atrDef.getCardinality())) {
			return coerce(valueType, (String) values.get(0));
		}
		// MANY
		for (ListIterator i = values.listIterator(); i.hasNext();) {
			String v = (String) i.next();
			i.set(coerce(valueType, v));
		}
		return values;
	}
	
	public static String attributeToString(AttributeI attr) {
		AttributeDefI atrDef = attr.getDefinition();
		Object value = attr.getValue();
		if (EnumAttributeType.DATE.equals(atrDef.getAttributeType())) {
			return dateFormat.format((Date) value);
		}
		return String.valueOf(value);
	}

}
