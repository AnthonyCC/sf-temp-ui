/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.fdstore.attributes.cms.BooleanBuilder;
import com.freshdirect.fdstore.attributes.cms.BrandRefBuilder;
import com.freshdirect.fdstore.attributes.cms.CategoryRefBuilder;
import com.freshdirect.fdstore.attributes.cms.DepartmentRefBuilder;
import com.freshdirect.fdstore.attributes.cms.DomainRefBuilder;
import com.freshdirect.fdstore.attributes.cms.DomainValueRefBuilder;
import com.freshdirect.fdstore.attributes.cms.DoubleBuilder;
import com.freshdirect.fdstore.attributes.cms.GenericNodeBuilder;
import com.freshdirect.fdstore.attributes.cms.HtmlBuilder;
import com.freshdirect.fdstore.attributes.cms.ImageBuilder;
import com.freshdirect.fdstore.attributes.cms.IntegerBuilder;
import com.freshdirect.fdstore.attributes.cms.ProductRefBuilder;
import com.freshdirect.fdstore.attributes.cms.SkuRefBuilder;
import com.freshdirect.fdstore.attributes.cms.StringBuilder;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author mrose
 *
 */
public class FDAttributeFactory {
	
	private final static Category LOGGER = LoggerFactory.getInstance(FDAttributeFactory.class);

	public static Attribute getAttribute(AttributeI attr) {
		if (attr != null) {
			Object value = attr.getValue();
			if (value == null) {
				return null;
			}

			AttributeDefI attrDef = attr.getDefinition();
			FDAttributeBuilderI builder = getBuilder(attrDef, value);

			if (builder != null) {
				return builder.build(attrDef, value);
			} else {
				LOGGER.warn("Couldn't find builder for " + attrDef.getName() + " : " + value.getClass().getName());
			}
		}
		return null;

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
				List valueList = (List) value;
				Set types = new HashSet();
				for (Iterator i = valueList.iterator(); i.hasNext();) {
					ContentKey cKey = (ContentKey) i.next();
					types.add(cKey.lookupContentNode().getKey().getType().getName());
				}
				if (types.isEmpty())
					return null; // no 
				if (types.size() > 1) {
					// FIXME this scenario should really throw an exception
					// for now, we'll just log the offending caller
					LOGGER.warn(
							"Unable to select an FDAttributeBuilder for a heterogeneous relationship "
									+ attrDef + ", types: " + types,
							new Exception().fillInStackTrace());
					builderKey = (String) types.iterator().next();
					//throw new CmsRuntimeException("Unable to select an FDAttributeBuilder for a heterogeneous relationship");
				} else {
					builderKey = (String) types.iterator().next();
				}				
			}
			FDAttributeBuilderI builder = (FDAttributeBuilderI) typeMap.get(builderKey);
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

		return (FDAttributeBuilderI) typeMap.get(builderKey);

	}

	private final static FDAttributeBuilderI GENERIC_NODE_BUILDER = new GenericNodeBuilder();
	
	private final static Map typeMap = new HashMap();
	static {
		typeMap.put("Department", new DepartmentRefBuilder());
		typeMap.put("Category", new CategoryRefBuilder());
		typeMap.put("Product", new ProductRefBuilder());
		typeMap.put("Sku", new SkuRefBuilder());
		typeMap.put("Brand", new BrandRefBuilder());
		typeMap.put("Image", new ImageBuilder());
		typeMap.put("Html", new HtmlBuilder());
		typeMap.put("Domain", new DomainRefBuilder());
		typeMap.put("DomainValue", new DomainValueRefBuilder());
		typeMap.put("Integer", new IntegerBuilder());
		typeMap.put("Double", new DoubleBuilder());
		typeMap.put("Boolean", new BooleanBuilder());
		typeMap.put("String", new StringBuilder());
	}

}
