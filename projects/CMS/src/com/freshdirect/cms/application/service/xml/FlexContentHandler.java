package com.freshdirect.cms.application.service.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Category;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.cms.publish.PublishXmlTask;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * SAX event handler to parse a content-node XML.
 * 
 * The format of the XML is as follows.
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="ISO-8859-1"?&gt;
 * &lt;Content&gt;
 *   &lt;<b>ContentType</b> <i>[id="xxx"]</i>&gt;
 *     &lt;<b>attribute</b>&gt;value&lt;/<b>attribute</b>&gt;
 *     &lt;<b>attribute</b>&gt;value&lt;/<b>attribute</b>&gt;
 *     &lt;<b>relationship</b>&gt;
 *       &lt;<b>ContentType</b> <i>[id="xxx"|ref="xxx"]</i>&gt;
 *       ...
 *     &lt;/<b>relationship</b>&gt;
 *   &lt;/<b>ContentType</b>&gt;
 * &lt;/Content&gt;
 * </pre>
 */
public class FlexContentHandler extends CmsNodeHandler {

	private final static Category LOGGER = LoggerFactory.getInstance(FlexContentHandler.class);

	private static long ID_GENERATOR = 0;
	
	private final static Object IGNORE = new Object();

	private final Map nodes = new HashMap();
	private final Stack stack = new Stack();

	public FlexContentHandler() {
	}

	/**
	 * @return Map of ContentKey -> ContentNodeI
	 */
	public Map getContentNodes() {
		return nodes;
	}

	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (stack.isEmpty()) {
			stack.push(null);

			return;
		}

		Object last = stack.peek();

		//LOGGER.debug(StringUtils.repeat(" ", stack.size() - 1) + "(" + localName);

		if (last == IGNORE) {
			stack.push(IGNORE);
			return;
		} else if (last == null) {
			// expect a node or meta
			// process meta-data
			if (PublishXmlTask.NS_DC.equals(namespaceURI)) {
				stack.push(new Metadata(localName));
				return;
			}
			ContentNodeI n = parseNode(localName, atts);
			if (n == null) {
				LOGGER.warn("startElement ignoring tag " + localName + ". Stack: " + stack);
				stack.push(IGNORE);
			} else {
				stack.push(n);
			}
		} else if (last instanceof ContentNodeI) {
			// expect an attribute
			Slot s = parseSlot(localName);
			if (s == null) {
				LOGGER.warn("startElement ignoring tag " + localName + ". Stack: " + stack);
				stack.push(IGNORE);
			} else {
				stack.push(s);
			}

		} else if (last instanceof Relationship) {
			// expect a ref
			ContentType type = parseType(localName);
			String ref = atts.getValue("ref");
			if (ref != null) {
				ContentKey key = new ContentKey(type, ref);
				stack.push(key);
			} else {
				ContentNodeI node = parseNode(localName, atts);
				if (node == null) {
					LOGGER.warn("startElement ignoring tag " + localName + ". Stack: " + stack);
					stack.push(IGNORE);
				} else {
					stack.push(node.getKey());
					stack.push(node);
				}
			}
		} else {
			throw new SAXException("Unexpected element " + localName);
		}

	}

	private ContentType parseType(String localName) {
		ContentType type = ContentType.get(localName);
		/*
		 if (getTypeService().getContentTypeDefinition(type) == null) {
		 throw new SAXException("Unknown content type " + type);
		 }
		 */
		return type;
	}

	private ContentNodeI parseNode(String localName, Attributes atts) {
		ContentType type = parseType(localName);
		String id = atts.getValue("id");
		if (id == null) {
			id = "flex_" + ID_GENERATOR++;
		}
		ContentKey key = new ContentKey(type, id);
		return createNode(key);
	}

	private Slot parseSlot(String localName) {
		ContentNodeI node = (ContentNodeI) stack.peek();
		AttributeDefI attributeDef = node.getDefinition().getAttributeDef(localName);
		if (attributeDef == null) {
			return null;
		}
		Slot slot;
		if (attributeDef instanceof RelationshipDefI) {
			slot = new Relationship(attributeDef.getCardinality());
		} else {
			slot = new Attribute();
		}

		slot.name = localName;
		slot.node = (ContentNodeI) stack.peek();
		return slot;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String chars = new String(ch, start, length);
		Object last = stack.peek();
		if (last == IGNORE) {
			return;
		} else if (last instanceof Attribute) {
			Attribute attr = (Attribute) stack.peek();
			if (attr.value == null) {
				attr.value = chars;
			} else {
				attr.value += chars;
			}

		} else if (last instanceof Metadata) {
			Metadata meta = (Metadata) last;
			meta.value += chars;

		} else if (chars.trim().length() != 0) {
			throw new SAXException("No text allowed here: '" + chars + "'");
		}
	}

	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		Object last = stack.pop();
		/*
		 LOGGER.debug(StringUtils.repeat(" ", stack.size())
		 + (last == null ? "" : last.getClass().getName())
		 + ": "
		 + localName
		 + ")");
		 */

		if (last == IGNORE) {
			return;

		} else if (last instanceof Metadata) {
			Metadata meta = (Metadata) last;
			if (getResourceInfoService() != null) {
				getResourceInfoService().addEvent(meta.key + " -> " + meta.value);
			}

		} else if (last instanceof ContentNodeI) {
			ContentNodeI node = (ContentNodeI) last;
			nodes.put(node.getKey(), node);
			if (stack.peek() instanceof ContentKey) {
				ContentKey key = (ContentKey) stack.pop();
				Relationship rel = (Relationship) stack.peek();
				if (rel.card.equals(EnumCardinality.ONE)) {
					rel.value = key;
				} else {
					if (rel.value == null) {
						rel.value = new ArrayList();
					}
					((List) rel.value).add(key);
				}
			}
		} else if (last instanceof Relationship) {
			Relationship rel = (Relationship) last;

			ContentTypeDefI typeDef = rel.node.getDefinition();
			RelationshipDefI relDef = (RelationshipDefI) typeDef.getAttributeDef(rel.name);
			if (relDef == null) {
				LOGGER.warn("No definition for relationship " + rel.name + " on node " + rel.node);
			} else {
				rel.node.getAttribute(rel.name).setValue(rel.value);
			}
		} else if (last instanceof Attribute) {
			Attribute attr = (Attribute) last;

			AttributeI a = attr.node.getAttribute(attr.name);
			if (a == null) {
				LOGGER.warn("No defintiion for attribute " + attr.name + " on node " + attr.node);
			} else {
				a.setValue(attr.parseValue());
			}
		} else if (last instanceof ContentKey) {
			ContentKey key = (ContentKey) last;
			Relationship rel = (Relationship) stack.peek();
			if (rel.card.equals(EnumCardinality.ONE)) {
				rel.value = key;
			} else {
				if (rel.value == null) {
					rel.value = new ArrayList();
				}
				((List) rel.value).add(key);
			}
		}
	}

	private class Metadata {
		final String key;
		String value = "";

		public Metadata(String key) {
			this.key = key;
		}
	}

	private class Slot {
		ContentNodeI node;
		String name;
		
		public String toString() {
			return node.toString() + "." + name;
		}
	}

	private class Relationship extends Slot {
		Object value = null;
		EnumCardinality card;

		public Relationship(EnumCardinality card) {
			this.card = card;
		}
	}

	private class Attribute extends Slot {
		String value = new String();

		public Object parseValue() {
			String s = value.toString();

			AttributeDefI def = node.getDefinition().getAttributeDef(name);
			if (def == null) {
				LOGGER.warn("No definition for attribute " + name + " on node " + node);
				return null;
			}

			return ContentTypeUtil.coerce(
					(def instanceof EnumDefI) ? ((EnumDefI) def).getValueType()
							: def.getAttributeType(), s);
		}
	}
}