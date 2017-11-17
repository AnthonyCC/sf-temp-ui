package com.freshdirect.cms.media.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.freshdirect.cms.contentio.xml.FlexContentHandler;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.media.converter.AttributeValueToMediaConverter;
import com.freshdirect.cms.media.domain.Media;
import com.google.common.base.Optional;

@Profile({"xml", "test"})
@Service
public class XmlMediaService implements MediaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlMediaService.class);

    @Autowired
    private FlexContentHandler flexContentHandler;

    @Value("${cms.resource.basePath}")
    private String resourceBasePath;

    @Autowired
    private AttributeValueToMediaConverter attributeValueToMediaConverter;

    @Value("${cms.resource.mediaxml.name:Media.xml.gz}")
    private String storeXmlName;

    private Map<ContentKey, Media> mediaNodes;

    @PostConstruct
    public void init() throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
        loadXmlContent(storeXmlName);

        mediaNodes = new HashMap<ContentKey, Media>();

        Map<ContentKey, Map<Attribute, Object>> mediaEntities = flexContentHandler.getContentNodes();
        for (final Map.Entry<ContentKey, Map<Attribute, Object>> entry : mediaEntities.entrySet()) {
            final ContentKey mediaKey = entry.getKey();
            mediaNodes.put(mediaKey, attributeValueToMediaConverter.convert(mediaKey, entry.getValue()));
        }
    }

    @Override
    public List<Media> loadAll() {
        return new ArrayList<Media>(mediaNodes.values());
    }

    @Override
    public Optional<Media> getMediaByContentKey(ContentKey mediaContentKey) {
        return Optional.fromNullable(mediaNodes.get(mediaContentKey));
    }

    @Override
    public Optional<Media> getMediaByUri(String uri) {
        for (Map.Entry<ContentKey, Media> entry : mediaNodes.entrySet()) {
            Media media = entry.getValue();
            if (media != null && media.getUri() != null && media.getUri().equals(uri)) {
                return Optional.of(media);
            }
        }
        return Optional.absent();
    }

    @Override
    public Map<ContentKey, Media> getMediasByContentKeys(List<ContentKey> mediaContentKeys) {
        Map<ContentKey, Media> medias = new HashMap<ContentKey, Media>();
        for (ContentKey contentKey : mediaContentKeys) {
            Optional<Media> media = getMediaByContentKey(contentKey);
            if (media.isPresent()) {
                medias.put(contentKey, media.get());
            }
        }
        return medias;
    }

    @Override
    public Set<ContentKey> getChildMediaKeys(ContentKey parentMediaContentKey) {
        throw new UnsupportedOperationException("Querying direct children is not supported!");
    }

    @Override
    public void saveMedia(Media media) {
        throw new UnsupportedOperationException("Save media is not supported!");
    }

    @Override
    public void deleteMedia(Media media) {
        throw new UnsupportedOperationException("Delete media is not supported!");
    }

    @Override
    public List<Media> getMediasByUriStartsWith(String uriPrefix) {
        List<Media> medias = new ArrayList<Media>();
        for (Map.Entry<ContentKey, Media> entry : mediaNodes.entrySet()) {
            Media media = entry.getValue();
            if (media != null && media.getUri() != null && media.getUri().startsWith(uriPrefix)) {
                medias.add(media);
            }
        }
        return medias;
    }

    public void loadXmlContent(String xmlFileName) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
        LOGGER.info("Loading content from " + xmlFileName);

        InputStream resourceInputStream = setupInputStream(xmlFileName);
        SAXParser saxParser = setupSaxParser();

        saxParser.parse(resourceInputStream, flexContentHandler);
    }

    @Override
    public List<Media> findMediaNewerThan(Date date) {
        throw new UnsupportedOperationException("Finding freshly updated media is not supported");
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
