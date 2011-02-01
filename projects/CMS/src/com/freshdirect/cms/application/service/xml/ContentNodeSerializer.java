/*
 * Created on Dec 8, 2004
 */
package com.freshdirect.cms.application.service.xml;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.meta.ContentTypeUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Utility class to translate content nodes into an XML Document.
 * 
 * @see com.freshdirect.cms.application.service.xml.FlexContentHandler
 */
public class ContentNodeSerializer {

	private final static Category LOGGER = LoggerFactory.getInstance(ContentNodeSerializer.class);

	public Document visitNodes(List<ContentNodeI> contentNodes) {
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("Content");
		for (ContentNodeI node : contentNodes) {
			visitNode(root, node);
		}
		return doc;
	}

	public void visitNode(Element parent, ContentNodeI node) {
		ContentTypeDefI typeDef = node.getDefinition();
		Element eNode = parent.addElement(typeDef.getName());
		eNode.addAttribute("id", node.getKey().getId());
		for (String name : typeDef.getAttributeNames()) {
			AttributeDefI attrDef = typeDef.getAttributeDef(name);
			if (attrDef == null) {
				LOGGER.warn("No definition for " + node + " " + name);
				continue;
			}
			if (!filter(typeDef, name)) {
			    continue;
			}
			if (attrDef instanceof RelationshipDefI) {
				visitRelationship(eNode, (RelationshipDefI) attrDef, node.getAttributeValue(name));
			} else {
				visitScalar(eNode, attrDef, node.getAttributeValue(name));
			}
		}
	}

    protected boolean filter(ContentTypeDefI typeDef, String name) {
        return true;
    }

    private void visitScalar(Element parent, AttributeDefI attrDef, Object value) {
		if (value != null) {
			parent.addElement(attrDef.getName()).addText(ContentTypeUtil.attributeToString(attrDef, value));
		}
	}

    /**
     * 
     * @param parent
     * @param relDef
     * @param val can either be null, a key, or a list of keys
     */
	private void visitRelationship(Element parent, RelationshipDefI relDef, Object val) {
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