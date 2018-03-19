package com.freshdirect.cms.contentio.xml;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.converter.ScalarValueConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;

/**
 * Service class which creates an XML Document object from a Map of ContentKey -> Map of Attribute -> Value
 *
 * @see FlexContentHandler
 */
@Service
public class ContentToXmlDocumentConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentToXmlDocumentConverter.class);

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    public Document convert(Map<ContentKey, Map<Attribute, Object>> contentNodes) {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("Content");
        for (ContentKey key : contentNodes.keySet()) {
            visitNode(root, key, contentNodes.get(key));
        }
        return doc;
    }

    private void visitNode(Element parent, ContentKey contentKey, Map<Attribute, Object> attributes) {
        if (contentKey != null) {
            ContentType type = contentKey.type;
            Element eNode = parent.addElement(type.name());
            eNode.addAttribute("id", contentKey.id);
            Set<Attribute> attributesOfType = contentTypeInfoService.selectAttributes(type);
            for (Attribute attribute : attributesOfType) {
                if (attribute instanceof Relationship) {
                    visitRelationship(eNode, (Relationship) attribute, attributes.get(attribute));
                } else {
                    visitScalar(eNode, (Scalar) attribute, attributes.get(attribute));
                }
            }
        }
    }

    private void visitScalar(Element parent, Scalar attribute, Object value) {
        if (value != null) {
            parent.addElement(attribute.getName()).addText(ScalarValueConverter.serializeToString(attribute, value));
        }
    }

    @SuppressWarnings("unchecked")
    private void visitRelationship(Element parent, Relationship relationship, Object value) {
        if (value != null) {
            Element e = parent.addElement(relationship.getName());
            if (RelationshipCardinality.MANY == relationship.getCardinality()) {
                List<ContentKey> childKeys = (List<ContentKey>) value;
                if (childKeys.isEmpty()) {
                    visitNullReferenceElement(e);
                } else {
                    // Do the normal way
                    List<ContentKey> relationshipdestinations = childKeys;
                    for (ContentKey key : relationshipdestinations) {
                        visitContentKey(e, key);
                    }
                }

            } else {
                ContentKey key = (ContentKey) value;
                visitContentKey(e, key);
            }
        } else {
            // LOGGER.warn("Skip serializing " + relationship.getName() + " having 'null' value");
        }
    }

    private void visitNullReferenceElement(Element parent) {
        parent.addElement("Null").addAttribute("ref", "null");
    }

    private void visitContentKey(Element parent, ContentKey key) {
        parent.addElement(key.type.name()).addAttribute("ref", key.id);
    }
}
