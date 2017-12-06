package com.freshdirect.cms.contentio.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.xml.sax.SAXException;

import com.freshdirect.cms.contentio.xml.FlexContentHandler;
import com.freshdirect.cms.contentio.xml.XmlContentMetadataService;
import com.freshdirect.cms.core.converter.SerializedScalarValueToObjectConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentKeyParentsCollectorService;
import com.freshdirect.cms.core.service.ContentProvider;
import com.freshdirect.cms.core.service.ContentSource;
import com.google.common.base.Optional;

@Profile("xml")
@Service
@Lazy
public class XmlContentProvider implements ContentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlContentProvider.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${cms.resource.basePath}")
    private String resourceBasePath;

    @Value("${cms.resource.storexml.name:Store.xml.gz}")
    private String storeXmlName;

    @Autowired
    private SerializedScalarValueToObjectConverter serializedScalarValueToObjectConverter;

    @Autowired
    private XmlContentMetadataService xmlContentMetadataService;

    @Autowired
    private ContentKeyParentsCollectorService contentKeyParentsCollectorService;

    private Map<ContentKey, Map<Attribute, Object>> contentNodes;

    private Map<ContentKey, Set<ContentKey>> parentKeys;

    private Map<ContentType, Set<ContentKey>> keysOfType;

    @Override
    public ContentSource getSource() {
        return ContentSource.XML;
    }

    @Override
    public Map<Attribute, Object> getAllAttributesForContentKey(ContentKey contentKey) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");

        return contentNodes.containsKey(contentKey) ? contentNodes.get(contentKey) : Collections.<Attribute, Object> emptyMap();
    }

    @Override
    public Map<ContentKey, Map<Attribute, Object>> getAllAttributesForContentKeys(Set<ContentKey> contentKeys) {
        Assert.notNull(contentKeys, "ContentKeys parameter can't be null!");
        Map<ContentKey, Map<Attribute, Object>> results = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey contentKey : contentKeys) {
            results.put(contentKey, getAllAttributesForContentKey(contentKey));
        }
        return results;
    }

    @Override
    public Optional<Object> getAttributeValue(ContentKey contentKey, Attribute attribute) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Assert.notNull(attribute, "Attribute parameter can't be null!");

        Object value = null;
        if (contentNodes.containsKey(contentKey)) {
            Map<Attribute, Object> payload = contentNodes.get(contentKey);
            value = payload.get(attribute);
        }

        return Optional.fromNullable(value);
    }

    @Override
    public Map<Attribute, Object> getAttributeValues(ContentKey contentKey, List<? extends Attribute> attributes) {
        Assert.notNull(contentKey, "ContentKey parameter can't be null!");
        Assert.notNull(attributes, "Attributes parameter can't be null!");

        Map<Attribute, Object> payload = contentNodes.get(contentKey);
        if (payload == null) {
            return Collections.emptyMap();
        }

        Map<Attribute, Object> results = new HashMap<Attribute, Object>();
        for (Attribute attribute : attributes) {
            if (payload.containsKey(attribute)) {
                results.put(attribute, payload.get(attribute));
            }
        }
        return results;
    }

    @Override
    public Set<ContentKey> getContentKeys() {
        return Collections.unmodifiableSet(contentNodes.keySet());
    }

    @Override
    public Set<ContentKey> getContentKeysByType(ContentType type) {
        Assert.notNull(type, "ContentType parameter can't be null!");

        return keysOfType.containsKey(type) ? keysOfType.get(type) : Collections.<ContentKey> emptySet();
    }

    @Override
    public Set<ContentKey> getParentKeys(ContentKey contentKey) {
        Assert.notNull(contentKey, "contentKey parameter can't be null!");
        return parentKeys.containsKey(contentKey) ? parentKeys.get(contentKey) : Collections.<ContentKey> emptySet();
    }

    @Override
    public Map<ContentKey, Map<Attribute, Object>> getAttributesForContentKeys(List<ContentKey> contentKeys, List<? extends Attribute> attributes) {
        Assert.notNull(contentKeys, "ContentKeys parameter can't be null!");
        Assert.notNull(attributes, "Attributes parameter can't be null!");

        Map<ContentKey, Map<Attribute, Object>> result = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (ContentKey contentKey : contentKeys) {
            Map<Attribute, Object> attributesForKey = getAllAttributesForContentKey(contentKey);

            if (attributesForKey != null && !attributesForKey.isEmpty()) {
                result.put(contentKey, new HashMap<Attribute, Object>());
                for (Attribute attribute : attributes) {
                    if (attributesForKey.containsKey(attribute)) {
                        result.get(contentKey).put(attribute, attributesForKey.get(attribute));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public void saveAttribute(ContentKey contentKey, Attribute attribute, Object attributeValue) {
        throw new UnsupportedOperationException("saveAttribute it not supported!");

    }

    @Override
    public void saveAttributes(ContentKey contentKey, Map<Attribute, Object> attributesWithValues) {
        throw new UnsupportedOperationException("saveAttributes it not supported!");
    }

    /**
     * Prepare store content with the following steps:
     * <ul>
     * <li>Get a new {@code FlexContentHandler} prototype</li>
     * <li>Load {@code storeXmlName} xml file</li>
     * <li>Load all attributes by key</li>
     * </ul>
     */
    @Override
    public Map<ContentKey, Map<Attribute, Object>> loadAll() {
        try {
            loadXmlContent(storeXmlName);
        } catch (FileNotFoundException e) {
            LOGGER.error("Loading " + storeXmlName + " failed", e);
            throw new RuntimeException("Loading " + storeXmlName + " failed", e);
        } catch (ParserConfigurationException e) {
            LOGGER.error("Loading " + storeXmlName + " failed", e);
            throw new RuntimeException("Loading " + storeXmlName + " failed", e);
        } catch (SAXException e) {
            LOGGER.error("Loading " + storeXmlName + " failed", e);
            throw new RuntimeException("Loading " + storeXmlName + " failed", e);
        } catch (IOException e) {
            LOGGER.error("Loading " + storeXmlName + " failed", e);
            throw new RuntimeException("Loading " + storeXmlName + " failed", e);
        }

        return contentNodes;
    }

    private void loadXmlContent(String xmlFileName) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
        Assert.notNull(xmlFileName, "XML File Name parameter can't be null!");

        LOGGER.info("Loading content from " + xmlFileName);

        FlexContentHandler flexContentHandler = applicationContext.getBean(FlexContentHandler.class);
        InputStream resourceInputStream = setupInputStream(xmlFileName);
        SAXParser saxParser = setupSaxParser();
        saxParser.parse(resourceInputStream, flexContentHandler);

        buildAll(flexContentHandler);
    }

    void buildAll(FlexContentHandler flexContentHandler) {
        buildNodes(flexContentHandler);
        buildMetadata(flexContentHandler);
        buildIndexes();
    }

    private void buildNodes(FlexContentHandler flexContentHandler) {
        // process nodes
        contentNodes = new HashMap<ContentKey, Map<Attribute, Object>>(flexContentHandler.getContentNodes().size());

        for (Map.Entry<ContentKey, Map<Attribute, Object>> rawPayload : flexContentHandler.getContentNodes().entrySet()) {

            ContentKey contentKey = rawPayload.getKey();

            Map<Attribute, Object> payload = Collections.emptyMap();
            if (!rawPayload.getValue().isEmpty()) {
                payload = new HashMap<Attribute, Object>(rawPayload.getValue().size());
                for (Map.Entry<Attribute, Object> rawEntry : rawPayload.getValue().entrySet()) {
                    Attribute attribute = rawEntry.getKey();
                    Object value = rawEntry.getValue();

                    if (attribute instanceof Scalar && value != null) {
                        value = serializedScalarValueToObjectConverter.convert(attribute, value.toString());
                    }

                    payload.put(attribute, value);
                }
            }

            contentNodes.put(contentKey, payload);
        }
    }

    private void buildIndexes() {
        // collect keys for type
        keysOfType = new HashMap<ContentType, Set<ContentKey>>(ContentType.values().length);
        for (ContentKey contentKey : contentNodes.keySet()) {
            Set<ContentKey> keySet = keysOfType.get(contentKey.type);
            if (keySet == null) {
                keySet = new HashSet<ContentKey>();
                keysOfType.put(contentKey.type, keySet);
            }
            keySet.add(contentKey);
        }

        // process parent keys
        parentKeys = contentKeyParentsCollectorService.createParentKeysMap(contentNodes);
    }

    private void buildMetadata(FlexContentHandler flexContentHandler) {
        // process store metadata
        xmlContentMetadataService.setDate(flexContentHandler.getMetadatas().get("date"));
        xmlContentMetadataService.setDescription(flexContentHandler.getMetadatas().get("description"));
        xmlContentMetadataService.setType(flexContentHandler.getMetadatas().get("type"));
    }

    @Override
    public Map<ContentKey, Set<ContentKey>> generateParentKeysMap() {
        return contentKeyParentsCollectorService.createParentKeysMap();
    }

    private InputStream setupInputStream(String fileName) throws IOException {
        InputStream resourceInputStream = null;
        if (resourceBasePath.startsWith("file:")) {
            resourceInputStream = new FileInputStream(new File(resourceBasePath.split(":", 2)[1] + File.separator + fileName));
        } else if (resourceBasePath.startsWith("classpath:")) {
            Resource resource = new ClassPathResource(resourceBasePath.split(":")[1] + File.separator + fileName);
            resourceInputStream = resource.getInputStream();
        }

        if (fileName.endsWith(".zip")) {
            resourceInputStream = new ZipInputStream(resourceInputStream);
        } else if (fileName.endsWith(".gz")) {
            resourceInputStream = new GZIPInputStream(resourceInputStream);
        }
        return resourceInputStream;
    }

    private SAXParser setupSaxParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser saxParser = factory.newSAXParser();
        return saxParser;
    }
}
