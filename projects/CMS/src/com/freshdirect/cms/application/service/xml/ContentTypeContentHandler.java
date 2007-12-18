/*
 * Created on Nov 17, 2004
 */
package com.freshdirect.cms.application.service.xml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.meta.AttributeDef;
import com.freshdirect.cms.meta.ContentTypeDef;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.meta.EnumDef;
import com.freshdirect.cms.meta.RelationshipDef;

/**
 * SAX event handler to parse a content type definition XML.
 * 
 * The format of the XML is as follows.
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="ISO-8859-1"?&gt;
 * &lt;CMSDef xmlns="http://www.freshdirect.com/xsd/CMS"&gt;
 *   &lt;ContentTypeDef name="<em>contentType</em>" generateId="<em><b>false</b>|true</em>"&gt;
 *     &lt;AttributeDefs&gt;
 *       &lt;AttributeDef name="<em>attributeName</em>" type="<em>B|I|D|S</em>"/&gt;
 *       ...
 *       &lt;EnumDef name="<em>attributeName</em>" type="<em>B|I|D|S</em>"&gt;
 *       	&lt;EnumValue value="<em>value</em>" label="<em>label</em>"/&gt;
 *       	...
 *       &lt;/EnumDef&gt;
 *     &lt;/AttributeDefs&gt;
 *     &lt;RelationshipDefs&gt;
 *       &lt;RelationshipDef name="<em>attributeName</em>" [navigable="<em><b>false</b>|true</em>"] [cardinality="<em><b>One</b>|Many</em>"]&gt;
 *         &lt;DestinationDef contentType="<em>contentType</em>"/&gt;
 *       &lt;/RelationshipDef&gt;
 *       ...
 *     &lt;/RelationshipDefs&gt;
 *   &lt;/ContentTypeDef&gt;
 * &lt;/CMSDef&gt;
 * </pre>
 * 
 * @see com.freshdirect.cms.ContentTypeDefI
 */
public class ContentTypeContentHandler extends DefaultHandler {

	private final ContentTypeServiceI typeService;

	/** Map of ContentType -> ContentTypeDef */
	private Map types = new HashMap();

	private Stack stack = new Stack();
	
	public ContentTypeContentHandler(ContentTypeServiceI typeService) {
		this.typeService = typeService;
	}

	/**
	 * @return Map of String (type name) -> ContentTypeDefI
	 */
	public Map getContentTypes() {
		return types;
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		Object obj = null;
		if ("ContentTypeDef".equals(localName)) {
			String name = atts.getValue("name");
			String label = atts.getValue("label");
			ContentTypeDef def = new ContentTypeDef(typeService, ContentType.get(name), label);

			String generateIdStr = atts.getValue("generateId");
			if (generateIdStr != null && "true".equals(generateIdStr)) {
				def.setIdGenerated(true);
			} else {
				def.setIdGenerated(false);
			}
			
			obj = def;
		} else if ("AttributeDef".equals(localName) || "RelationshipDef".equals(localName) || "EnumDef".equals(localName)) {
			String name = atts.getValue("name");
			String label = atts.getValue("label");
			boolean required = Boolean.valueOf(atts.getValue("required")).booleanValue();
			boolean inheritable = Boolean.valueOf(atts.getValue("inheritable")).booleanValue();
			boolean readOnly = Boolean.valueOf(atts.getValue("readOnly")).booleanValue();
			EnumCardinality cardinality = EnumCardinality.getEnum(atts.getValue("cardinality"));
			if (cardinality == null) {
				cardinality = EnumCardinality.ONE;
			}

			//Object lastObj = stack.peek();
			//ContentTypeDef typeDef = (ContentTypeDef) lastObj;

			if ("RelationshipDef".equals(localName)) {
				boolean navigable = Boolean.valueOf(atts.getValue("navigable")).booleanValue();
				obj = new RelationshipDef(name, label, required, inheritable, navigable, readOnly, cardinality);
			} else if ("EnumDef".equals(localName)) {
				EnumAttributeType valueType = EnumAttributeType.getEnum(atts.getValue("type"));
				Map values = new LinkedHashMap();
				stack.add(values);
				obj = new EnumDef(name, label, required, inheritable, readOnly, valueType, values);
			} else {
				EnumAttributeType atrType = EnumAttributeType.getEnum(atts.getValue("type"));
				obj = new AttributeDef(atrType, name, label, required, inheritable, readOnly, cardinality);
			}

		} else if ("DestinationDef".equals(localName)) {
			String contentType = atts.getValue("contentType");
			obj = ContentType.get(contentType);
		} else if ("EnumValue".equals(localName)) {
			EnumDef enumDef = (EnumDef) stack.peek();
			Map m = (Map)stack.get(stack.size()-2);
		
			String valueStr = atts.getValue("value");		
			String label = atts.getValue("label");
			Object value = ContentTypeUtil.coerce(enumDef.getValueType(), valueStr);
			m.put(value, label);
		}
		if (obj != null) {
			stack.add(obj);
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		// nothing required...
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		if ("ContentTypeDef".equals(localName)) {
			ContentTypeDefI typeDef = (ContentTypeDefI) stack.pop();

			if (types.containsKey(typeDef.getType())) {
				throw new CmsRuntimeException("Duplicate content type definition " + typeDef.getName());
			}

			types.put(typeDef.getType(), typeDef);

		} else if ("AttributeDef".equals(localName) || "RelationshipDef".equals(localName) || "EnumDef".equals(localName)) {
			AttributeDef def = (AttributeDef) stack.pop();
			if ("EnumDef".equals(localName)) {
				// pop values Map
				stack.pop();
			}
			ContentTypeDef type = (ContentTypeDef) stack.peek();
			if (type.getSelfAttributeDef(def.getName()) != null) {
				throw new CmsRuntimeException("Duplicate attribute definition " + type.getName() + " / " + def.getName());
			}

			type.addAttributeDef(def);
			

		} else if ("DestinationDef".equals(localName)) {
			ContentType dest = (ContentType) stack.pop();
			RelationshipDef rel = (RelationshipDef) stack.peek();
			rel.addContentType(dest);
		}
	}

}