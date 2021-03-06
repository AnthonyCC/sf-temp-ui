/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.fdstore.attributes.cms.DomainValueBuilder;
import com.freshdirect.fdstore.attributes.cms.GenericContentNodeBuilder;
import com.freshdirect.fdstore.attributes.cms.GenericNodeBuilder;
import com.freshdirect.fdstore.attributes.cms.HtmlBuilder;
import com.freshdirect.fdstore.attributes.cms.ImageBuilder;
import com.freshdirect.fdstore.attributes.cms.PrimitiveBuilder;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author mrose
 *
 */
public class FDAttributeFactory {

	private final static Logger LOGGER = LoggerFactory.getInstance(FDAttributeFactory.class);

	/**
	 * Construct the backward compatible wrapper objects for a node, without constructing the deprecated
	 * (Multi)Attribute objects.
	 * 
	 * @param node
	 * @param attribName
	 * @return
	 */
	public static Object constructWrapperValue(ContentNodeModel node, String attribName) {
	    Object value = node.getCmsAttributeValue(attribName);
	    if (value != null) {
	        AttributeDefI attributeDef = node.getAttributeDef(attribName);
	        FDAttributeBuilderI builder = getBuilder(attributeDef, value);
	        return builder.constructValue(attributeDef, value);
	    }
	    return null;
	}
	
	/**
	 * Construct an image object.
	 * @param node
	 * @param attribName
	 * @return
	 */
	public static Image constructImage(ContentNodeModel node, String attribName) {
	    return (Image) constructWrapperValue(node, attribName);
	}

	public static Image constructImage( ContentNodeModel node, String attribName, Image defValue ) {
		Image i = (Image)constructWrapperValue( node, attribName );
		return i != null ? i : defValue;
	}

    /**
     * Construct an Html object.
     * @param node
     * @param attribName
     * @return
     */
	public static Html constructHtml( ContentNodeModel node, String attribName ) {
		return (Html)constructWrapperValue( node, attribName );
	}
	
	/**
	 * Construct the backward compatible wrapper objects for a node, without constructing the deprecated
	 * (Multi)Attribute objects.
	 * 
	 * @param node
	 * @param attribName
	 * @return
	 */
	public static List constructWrapperList( ContentNodeModel node, String attribName ) {
		Object value = node.getCmsAttributeValue( attribName );
		if ( value != null ) {
			AttributeDefI attributeDef = node.getAttributeDef( attribName );
			FDAttributeBuilderI builder = getBuilder( attributeDef, value );
			return (List)builder.constructValue( attributeDef, value );
		}
		return Collections.EMPTY_LIST;
	}
	
	@SuppressWarnings("unchecked")
	public static <X> X lookup( ContentNodeModel node, String attribName, X defValue ) {
		Object value = node.getCmsAttributeValue( attribName );
		if ( value instanceof ContentKey ) {
			return (X)ContentFactory.getInstance().getContentNodeByKey( (ContentKey)value );
		}
		return defValue;
	}

	private static FDAttributeBuilderI getBuilder(AttributeDefI attrDef, Object value) {

		String builderKey;

		if (attrDef instanceof RelationshipDefI) {
			//RelationshipDefI relDef = (RelationshipDefI) attrDef;
			if (!EnumCardinality.MANY.equals(attrDef.getCardinality())) {
				// cardinality ONE
				ContentKey cKey = (ContentKey) value;
				builderKey = cKey.getType().getName();
			} else {
				List<ContentKey> valueList = (List<ContentKey>) value;
				Set<String> types = new HashSet<String>();
				for ( ContentKey cKey : valueList ) {
					types.add(cKey.getType().getName());
				}
				if (types.isEmpty())
					return EMPTY_LIST_BUILDER;
				if (types.size() > 1) {
					// FIXME this scenario should really throw an exception
					// for now, we'll just log the offending caller
					LOGGER.warn(
							"Unable to select an FDAttributeBuilder for a heterogeneous relationship "
									+ attrDef + ", types: " + types,
							new Exception().fillInStackTrace());
					builderKey = types.iterator().next();
					//throw new CmsRuntimeException("Unable to select an FDAttributeBuilder for a heterogeneous relationship");
				} else {
					builderKey = types.iterator().next();
				}				
			}
			FDAttributeBuilderI builder = typeMap.get(builderKey);
			if (builder == null) {
				builder = GENERIC_NODE_BUILDER;
			}
			return builder;
		} else if (attrDef instanceof EnumDefI) {
			EnumDefI enumDef = (EnumDefI) attrDef;
			builderKey = enumDef.getValueType().getLabel();
		} else {
			builderKey = attrDef.getAttributeType().getLabel();
		}

		return typeMap.get(builderKey);

	}

	private final static FDAttributeBuilderI GENERIC_NODE_BUILDER = new GenericNodeBuilder();
	
	private final static Map<String,FDAttributeBuilderI> typeMap = new HashMap<String,FDAttributeBuilderI>();
	static {
		typeMap.put("Department", new GenericContentNodeBuilder());
		typeMap.put("Category", new GenericContentNodeBuilder());
		typeMap.put("Product", new GenericContentNodeBuilder());
		typeMap.put("Sku", new GenericContentNodeBuilder());
		typeMap.put("Domain", new GenericContentNodeBuilder());
		typeMap.put("Image", new ImageBuilder());
		typeMap.put("Html", new HtmlBuilder());
		typeMap.put("DomainValue", new DomainValueBuilder());
		typeMap.put("Integer", new PrimitiveBuilder());
		typeMap.put("Double", new PrimitiveBuilder());
		typeMap.put("Boolean", new PrimitiveBuilder());
		typeMap.put("String", new PrimitiveBuilder());
	}

	private final static FDAttributeBuilderI EMPTY_LIST_BUILDER = new FDAttributeBuilderI() {
		@Override
		public Object constructValue(AttributeDefI cmsAttrDef, Object value) {
			return Collections.EMPTY_LIST;
		}
	};

}
