/*
 * Created on Dec 8, 2004
 */
package com.freshdirect.cms.application.service.xml;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Utility class to translate content nodes into an XML Document.
 * 
 * @see com.freshdirect.cms.application.service.xml.FlexContentHandler
 */
public class ContentNodeSerializer {

	private final static Category LOGGER = LoggerFactory.getInstance(ContentNodeSerializer.class);

	public Document visitNodes(List contentNodes) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("Content");
		for (Iterator i = contentNodes.iterator(); i.hasNext();) {
			ContentNodeI node = (ContentNodeI) i.next();
			visitNode(root, node);
		}
		return doc;
	}

	public void visitNode(Element parent, ContentNodeI node) {
		ContentTypeDefI typeDef = node.getDefinition();
		Element eNode = parent.addElement(typeDef.getName());
		eNode.addAttribute("id", node.getKey().getId());
		for (Iterator i = node.getAttributes().entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			AttributeDefI attrDef = typeDef.getAttributeDef(name);
			if (attrDef == null) {
				LOGGER.warn("No definition for " + node + " " + name);
				continue;
			}
			if (!filter(typeDef, name)) {
			    continue;
			}
			if (attrDef instanceof RelationshipDefI) {

				// FIXME remove workaround for broken content
				if (e.getValue() instanceof String) {
					LOGGER.warn("Invalid type for " + node + " " + name);
					continue;
				}

				visitRelationship(eNode, (RelationshipDefI) attrDef, (RelationshipI) e.getValue());
			} else {
				// FIXME remove workaround for broken content
				if (e.getValue() == null) {
					LOGGER.warn("Null value for " + node + " " + name);
					continue;
				}

				visitScalar(eNode, attrDef, e.getValue());
			}
		}
	}

    protected boolean filter(ContentTypeDefI typeDef, String name) {
        return true;
    }

    private void visitScalar(Element parent, AttributeDefI attrDef, Object value) {
		AttributeI attrib = (AttributeI) value;
		if (attrib.getValue() != null) {
			parent.addElement(attrDef.getName()).addText(ContentTypeUtil.attributeToString(attrib));
		}
	}

	private void visitRelationship(Element parent, RelationshipDefI relDef, RelationshipI destination) {
		//values can either be null, a key, or a list of keys
		Object val = destination.getValue();
		if (val != null) {
			Element e = parent.addElement(relDef.getName());
			if (val instanceof List) {
				for (Iterator i = ((List) val).iterator(); i.hasNext();) {
					ContentKey key = (ContentKey) i.next();
					if (filterRelationTo(key)) {
					    visitContentKey(e, key);
					}
				}
			} else {
			    ContentKey key = (ContentKey) val;
                            if (filterRelationTo(key)) {
				visitContentKey(e, key);
                            }
			}
		}

	}
	
	protected boolean filterRelationTo(ContentKey key) {
	    return true;
	}

	private void visitContentKey(Element parent, ContentKey key) {
		parent.addElement(key.getType().getName()).addAttribute("ref", key.getId());
	}

}