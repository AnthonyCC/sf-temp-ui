package com.freshdirect.cms.contentio.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.google.common.base.Optional;

/**
 * SAX event handler to parse a content-node XML.
 *
 * The format of the XML is as follows.
 *
 * <pre>
 * &lt;?xml version="1.0" encoding="ISO-8859-1"?&gt;
 * &lt;Content&gt;
 * &lt;<b>ContentType</b> <i>[id="xxx"]</i>&gt;
 * &lt;<b>attribute</b>&gt;value&lt;/<b>attribute</b>&gt;
 * &lt;<b>attribute</b>&gt;value&lt;/<b>attribute</b>&gt;
 * &lt;<b>relationship</b>&gt;
 * &lt;<b>ContentType</b> <i>[id="xxx"|ref="xxx"]</i>&gt;
 * ...
 * &lt;/<b>relationship</b>&gt;
 * &lt;/<b>ContentType</b>&gt;
 * &lt;/Content&gt;
 * </pre>
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlexContentHandler extends DefaultHandler {

    public static final String DUBLIN_CORE_METADATA_NAMESPACE = "http://purl.org/dc/elements/1.1";

    private static final Logger LOGGER = LoggerFactory.getLogger(FlexContentHandler.class);

    private static long generatedId = 0;

    private static final Object IGNORE = new Object();

    private static final ContentKey NULL_KEY = ContentKeyFactory.get(ContentType.Store, "##flexcontenthandler##nullkey##");

    private final Map<String, String> metadatas;
    private final Map<ContentKey, Map<Attribute, Object>> nodes;
    private final Stack<Object> stack;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    public FlexContentHandler() {
        nodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        stack = new Stack<Object>();
        metadatas = new HashMap<String, String>();
    }

    /**
     * This method is for getting the fetched XML content. The return value can be null, if no XML document is parsed before calling this method.
     *
     * @return Map of ContentKey -> {Map of Attribute -> Value}, can be null
     */

    public Object getContentNodeAttributeValue(ContentKey key, Attribute attribute) {
        Object value = null;
        if (nodes.containsKey(key) && nodes.get(key).containsKey(attribute)) {
            value = nodes.get(key).get(attribute);
        }
        return value;
    }

    public Set<ContentKey> getContentNodesKeys() {
        return Collections.unmodifiableSet(nodes.keySet());
    }

    public Map<ContentKey, Map<Attribute, Object>> getContentNodes() {
        return Collections.unmodifiableMap(nodes);
    }

    public Map<Attribute, Object> getContentNodeAttributes(ContentKey key) {
        if (containsKeyContentNodes(key)) {
            return Collections.unmodifiableMap(nodes.get(key));
        } else {
            return Collections.emptyMap();
        }
    }

    public boolean containsKeyContentNodes(ContentKey key) {
        return nodes.containsKey(key);
    }

    /**
     * This method is for getting the fetched metadatas, like the type, date and description. The description field contains the publishId
     *
     * @return map of metadata key -> value
     */
    public Map<String, String> getMetadatas() {
        return Collections.unmodifiableMap(metadatas);
    }

    /**
     * Inherited method from SAX DefaultHandler. When a START_ELEMENT event incoming, this method gets called. As a developer, you don't have to call this method manually, the SAX
     * parser calls this.
     */
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        if (stack.isEmpty()) {
            stack.push(null);
            return;
        }

        Object last = stack.peek();

        if (last == IGNORE) {
            stack.push(IGNORE);
            return;
        } else if (last == null) {
            if (DUBLIN_CORE_METADATA_NAMESPACE.equals(namespaceURI)) {
                stack.push(new Metadata(localName));
                return;
            }
            handleStartContentKey(localName, attributes);
        } else if (last instanceof ContentKey) {
            handleStartAttribute(localName);
        } else if (last instanceof AttributeWithValue && (((AttributeWithValue) last).attribute instanceof Relationship)) {
            handleStartRelationship(localName, attributes, last);
        } else {
            throw new SAXException("Unexpected element " + localName);
        }

    }

    private void handleStartContentKey(String localName, Attributes attributes) {
        ContentKey contentKey = parseContentKey(localName, attributes);
        if (contentKey == null) {
            LOGGER.warn("startElement ignoring tag " + localName + ". Stack: " + stack);
            stack.push(IGNORE);
        } else {
            stack.push(contentKey);
        }
    }

    private void handleStartAttribute(String localName) {
        final Attribute attribute = parseAttribute(localName);
        if (attribute == null) {
            LOGGER.warn("startElement ignoring tag (attribute) " + localName + ". Stack: " + stack);
            stack.push(IGNORE);
        } else {
            stack.push(new AttributeWithValue(attribute, null));
        }
    }

    @SuppressWarnings("unchecked")
    private void handleStartRelationship(String localName, Attributes attributes, Object last) {
        AttributeWithValue relationshipAttribute = (AttributeWithValue) last;
        Relationship relationship = (Relationship) relationshipAttribute.attribute;

        if ("Null".equals(localName)) {
            if (relationship.getCardinality() == RelationshipCardinality.MANY) {
                relationshipAttribute.value = Collections.emptyList();
            } else {
                LOGGER.warn("startElement ignoring tag " + localName + ". Stack: " + stack);
            }
        } else {
            ContentType type = parseType(localName);
            String ref = attributes.getValue("ref");

            ContentKey target = null;
            if (ref != null) {
                target = ContentKeyFactory.get(type, ref);
            } else {
                target = parseContentKey(localName, attributes);
                if (target == null) {
                    LOGGER.warn("startElement ignoring tag " + localName + ". Stack: " + stack);
                }
            }

            if (relationship.getCardinality() == RelationshipCardinality.ONE) {
                relationshipAttribute.value = target;
            } else {
                if (relationshipAttribute.value == null) {
                    relationshipAttribute.value = new ArrayList<ContentKey>();
                }
                ((List<ContentKey>) relationshipAttribute.value).add(target);
            }
        }
    }

    private ContentType parseType(String localName) {
        ContentType type = ContentType.valueOf(localName);
        return type;
    }

    private ContentKey parseContentKey(String localName, Attributes attributes) {
        if ("Null".equals(localName)) {
            return NULL_KEY;
        }

        ContentType type = parseType(localName);
        String id = attributes.getValue("id");
        if (id == null) {
            id = "flex_" + generatedId++;
        }
        ContentKey key = ContentKeyFactory.get(type, id);
        return key;
    }

    private Attribute parseAttribute(String localName) {
        ContentKey contentKey = (ContentKey) stack.peek();
        Optional<Attribute> attribute = null;

        if (NULL_KEY.equals(contentKey)) {
            attribute = Optional.absent();
        } else {
            attribute = contentTypeInfoService.findAttributeByName(contentKey.type, localName);
        }
        return attribute.orNull();
    }

    /**
     * Inherited method from SAX DefaultHandler. When the parser finds non-tag characters, this method gets called. As a developer, you don't have to call this method manually, the
     * SAX parser calls this.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String chars = new String(ch, start, length);
        Object last = stack.peek();
        if (last == IGNORE) {
            return;
        } else if (last instanceof AttributeWithValue && ((AttributeWithValue) last).attribute instanceof Scalar) {
            AttributeWithValue attributeWithValue = (AttributeWithValue) stack.peek();
            if (attributeWithValue.value == null) {
                attributeWithValue.value = chars;
            } else {
                attributeWithValue.value = attributeWithValue.value.toString() + chars;
            }

        } else if (last instanceof Metadata) {
            Metadata meta = (Metadata) last;
            meta.value += chars;
        } else if (chars.trim().length() != 0) {
            throw new SAXException("No text allowed here: '" + chars + "'");
        }
    }

    /**
     * Inherited method from SAX DefaultHandler. When a END_ELEMENT event incoming, this method gets called. As a developer, you don't have to call this method manually, the SAX
     * parser calls this.
     */
    @SuppressWarnings("serial")
    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        final Object last = stack.pop();

        if (last == IGNORE) {
            return;
        } else if (last instanceof Metadata) {
            Metadata meta = (Metadata) last;
            metadatas.put(meta.key, meta.value);
            LOGGER.debug(meta.toString());
        } else if (last instanceof ContentKey) {
            final ContentKey contentKey = (ContentKey) last;
            if (!contentKey.type.name().equals(localName)) {
                stack.push(last);
                return;
            }
            if (!nodes.containsKey(contentKey)) {
                // open a new payload entry for the given content key
                nodes.put(contentKey, new HashMap<Attribute, Object>());
            }
        } else if (last instanceof AttributeWithValue) {
            Object beforeLast = stack.peek();
            if (!localName.equals(((AttributeWithValue) last).attribute.getName())) {
                stack.push(last);

            } else if (beforeLast instanceof ContentKey) {
                ContentKey contentKey = (ContentKey) beforeLast;

                final Attribute attribute = ((AttributeWithValue) last).attribute;
                final Object value = ((AttributeWithValue) last).value;

                if (nodes.containsKey(contentKey)) {
                    nodes.get(contentKey).put(attribute, value);
                } else {
                    nodes.put(contentKey, new HashMap<Attribute, Object>() {
                        {
                            put(attribute, value);
                        }
                    });
                }
            }
        }
    }

    /**
     *
     * This class is intentionally private. Not by any circumstances meant to be used by other classes except the FlexContentHandler
     *
     */
    private static final class Metadata {

        final String key;
        String value = "";

        private Metadata(String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    /**
     *
     * This class is intentionally private. Not by any circumstances meant to be used by other classes except the FlexContentHandler
     *
     */
    private static final class AttributeWithValue {

        Attribute attribute;
        Object value;

        private AttributeWithValue(Attribute attribute, Object value) {
            this.attribute = attribute;
            this.value = value;
        }

        @Override
        public String toString() {
            return "AttributeWithValue [attribute=" + attribute + ", value=" + value + "]";
        }
    }
}
