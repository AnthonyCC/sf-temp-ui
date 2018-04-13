package com.freshdirect.cms.ui.editor.publish.feed.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.CharEncoding;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.freshdirect.cms.contentio.xml.ContentToXmlDocumentConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.properties.service.PropertyResolverService;
import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishEntityToFeedPublishConverter;
import com.freshdirect.cms.ui.editor.publish.feed.converter.FeedPublishToGwtPublishDataConverter;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublish;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessage;
import com.freshdirect.cms.ui.editor.publish.feed.domain.FeedPublishMessageLevel;
import com.freshdirect.cms.ui.editor.publish.feed.entity.FeedPublishEntity;
import com.freshdirect.cms.ui.editor.publish.feed.entity.PublishStatus;
import com.freshdirect.cms.ui.editor.publish.feed.repository.FeedPublishEntityRepository;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;

@Service
public class FeedPublishService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedPublishService.class);

    private static final ContentType[] typesToPublish = new ContentType[] { ContentType.WebPage, ContentType.PickList, ContentType.PickListItem, ContentType.Anchor,
            ContentType.ImageBanner, ContentType.TextComponent, ContentType.Section, ContentType.Schedule, ContentType.DarkStore };

    public final static String NS_DC = "http://purl.org/dc/elements/1.1";

    private static final Date THE_EPOCH = new Date(0l);

    @Autowired
    private FeedDataLoaderService feedDataLoaderService;

    @Autowired
    private ContentToXmlDocumentConverter contentToXmlDocumentConverter;

    @Autowired
    private PropertyResolverService propertyResolverService;

    @Autowired
    private FeedPublishEntityToFeedPublishConverter entityToDomainConverter;

    @Autowired
    private FeedPublishEntityRepository repository;

    @Autowired
    private FeedPublishMessagingService feedPublishMessagingService;

    @Value("${cms.feedpublish.debug.enabled:false}")
    private boolean debugEnabled = false;

    @Async
    public void publishFeed(FeedPublish publish, List<ContentKey> storeKeys, String userId, String comment) {
    	
        LOGGER.debug("Feed publish Stores size ======= "+(storeKeys!=null?storeKeys.size():storeKeys));


        for (ContentKey storeKey : storeKeys) {

            publish.setStoreKey(storeKey);
            
            LOGGER.debug("Feed publish starts for the store ======= "+(storeKey!=null?storeKey.getId():storeKey));

            FeedPublishMessage message = new FeedPublishMessage(FeedPublishMessageLevel.INFO, "Starting feed publish", publish.getStoreKey().id);
            feedPublishMessagingService.addMessage(publish, message);

            if (debugEnabled) {
                feedPublishMessagingService.addMessage(publish, new FeedPublishMessage(FeedPublishMessageLevel.DEBUG, "Debug mode enabled via property", publish.getStoreKey().id));
            }

            Map<ContentKey, Map<Attribute, Object>> contentNodes = feedDataLoaderService.loadNodesToFeedPublish(typesToPublish, publish);

            logStats(publish, contentNodes);

            Document xmlDocument = createXMLDocument(publish, contentNodes);

            feedPublishMessagingService.addMessage(publish, new FeedPublishMessage(FeedPublishMessageLevel.INFO, "Feed document ready", publish.getStoreKey().id));

            StringWriter writer = new StringWriter();
            try {
                OutputFormat format = new OutputFormat();
                format.setEncoding(CharEncoding.UTF_8);
                XMLWriter xmlWriter = new XMLWriter(writer, format);
                xmlWriter.write(xmlDocument);

                RestTemplate postFeedTemplate = new RestTemplate();
                StringHttpMessageConverter stringMessageConverter = new StringHttpMessageConverter(Charsets.UTF_8);
                FormHttpMessageConverter httpMessageConverter = new FormHttpMessageConverter();

                // Workaround encoding inconsistency across Spring message converters
                {
                    List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();

                    partConverters.add(new ByteArrayHttpMessageConverter());
                    StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charsets.UTF_8);
                    stringHttpMessageConverter.setWriteAcceptCharset(false);
                    partConverters.add(stringHttpMessageConverter);
                    partConverters.add(new ResourceHttpMessageConverter());

                    httpMessageConverter.setPartConverters(partConverters);
                }

                postFeedTemplate.setMessageConverters(Arrays.asList(httpMessageConverter, stringMessageConverter));
                HttpHeaders headers = new HttpHeaders();

                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();

                params.set("feedId", String.valueOf(publish.getPublishId()));
                params.set("feedData", writer.toString());
                params.set("storeId", publish.getStoreKey().id.toString());

                HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);

                postFeedTemplate.postForObject(propertyResolverService.getFeedPublishUri(), httpEntity, String.class);

                xmlWriter.flush();
                xmlWriter.close();
                writer.close();

                feedPublishMessagingService.addMessage(publish, new FeedPublishMessage(FeedPublishMessageLevel.INFO, "Feed successfully dispatched", publish.getStoreKey().id));

                if (debugEnabled) {
                    writeFeedToFile(publish, xmlDocument);
                }

                // okay
                feedPublishMessagingService.addMessage(publish, new FeedPublishMessage(FeedPublishMessageLevel.INFO, "FeedPublish succesfully finished"));
                feedPublishMessagingService.modifyFeedPublishStatus(publish, PublishStatus.COMPLETE);
            } catch (Exception e) {
            	LOGGER.error("Exception during feed publish ===== "+e.getMessage(), e);
                feedPublishMessagingService.addMessage(publish,
                        new FeedPublishMessage(FeedPublishMessageLevel.FAILURE, "Exception happened: " + e.getMessage(), publish.getStoreKey().id));
                feedPublishMessagingService.modifyFeedPublishStatus(publish, PublishStatus.FAILED);
                // throw new RuntimeException(e);

                if (debugEnabled) {
                    writeFeedToFile(publish, xmlDocument);
                }
            }
        }
    }

    private Document createXMLDocument(FeedPublish publish, Map<ContentKey, Map<Attribute, Object>> contentNodes) {
        Document xmlDocument = contentToXmlDocumentConverter.convert(contentNodes);

        Element rootElement = xmlDocument.getRootElement();
        rootElement.addNamespace("dc", NS_DC);
        rootElement.addElement("dc:description").addText("PublishId: " + publish.getPublishId());
        rootElement.addElement("dc:date").addText(FeedPublishToGwtPublishDataConverter.US_DATE_FORMAT.format(publish.getCreationTimestamp()));

        return xmlDocument;
    }

    private void logStats(FeedPublish publish, Map<ContentKey, Map<Attribute, Object>> contentNodes) {
        Map<ContentType, Integer> statsMap = new HashMap<ContentType, Integer>();

        for (ContentKey key : contentNodes.keySet()) {
            ContentType t = key.type;

            Integer totalInstances = statsMap.get(t);
            if (totalInstances == null) {
                totalInstances = 0;
            }
            totalInstances += 1;

            statsMap.put(t, totalInstances);
        }

        for (Map.Entry<ContentType, Integer> statEntry : statsMap.entrySet()) {
            feedPublishMessagingService.addMessage(publish, new FeedPublishMessage(FeedPublishMessageLevel.DEBUG, "Contains " + statEntry.getValue() + " " + statEntry.getKey() + " instances" , publish.getStoreKey().id));
        }
    }

    private void writeFeedToFile(FeedPublish publish, Document xmlDocument) {
        final String xmlFileName = "feed_"+publish.getPublishId()+"_"+publish.getStoreKey().id+".xml";

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(xmlFileName));

            OutputFormat format = new OutputFormat();
            format.setEncoding(CharEncoding.UTF_8);
            format.setIndent(true);
            format.setIndentSize(2);
            format.setNewlines(true);

            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(xmlDocument);

            xmlWriter.flush();
            xmlWriter.close();

            feedPublishMessagingService.addMessage(publish, new FeedPublishMessage(FeedPublishMessageLevel.DEBUG, "Wrote feed to file " + xmlFileName, publish.getStoreKey().id));

        } catch (Exception exc) {
            LOGGER.error("Failed to write feed out to file", exc);

            feedPublishMessagingService.addMessage(publish, new FeedPublishMessage(FeedPublishMessageLevel.DEBUG, "Failed to write file " + xmlFileName, publish.getStoreKey().id));
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Returns a tuple consisting two timestamps
     *
     * values | id='latest' | id=store publish ID --------+-----------------------------+-------------------------------- Date[0] | Current Time | last publish.getTimestamp();
     * Date[1] | publish[id].getTimestamp(); | publish[prev.id].getTimestamp();
     *
     * @param id
     * @return
     */
    public Optional<Date[]> timeInfo(String id) {
        List<FeedPublishEntity> history = repository.findAllByOrderByCreationDateDesc();

        Date timeInfo[] = null;

        if (id == null) {

            timeInfo = new Date[2];

            timeInfo[0] = new Date();
            timeInfo[1] = history.isEmpty() ? THE_EPOCH : history.get(0).getTimestamp();

        } else {

            FeedPublishEntity publish = null;
            int index = 0;

            while (index < history.size()) {
                if (id.equals(String.valueOf(history.get(index).getId()))) {
                    publish = history.get(index);

                    timeInfo = new Date[2];
                    timeInfo[0] = publish.getTimestamp();
                    timeInfo[1] = (index + 1) < history.size() ? history.get(index + 1).getTimestamp() : THE_EPOCH;

                    break;
                }
                index++;
            }

        }

        return Optional.fromNullable(timeInfo);
    }

    public Optional<FeedPublish> findFeedPublish(String id) {
        FeedPublishEntity publish = null;

        if (id != null) {
            if ("latest".equals(id)) {
                publish = repository.findFirstByOrderByCreationDateDesc();
            } else {
                publish = repository.findById(Long.parseLong(id));
            }
        }

        FeedPublish feedPublish = null;
        if (publish != null) {
            feedPublish = entityToDomainConverter.convert(publish);
        }

        return Optional.fromNullable(feedPublish);
    }

    public List<FeedPublishMessage> filterMessagesByLevel(List<FeedPublishMessage> messages, FeedPublishMessageLevel level) {
        List<FeedPublishMessage> filteredMessages = null;
        if (level == null) {
            filteredMessages = messages;
        } else {
            filteredMessages = new ArrayList<FeedPublishMessage>();
            for (FeedPublishMessage message : messages) {
                if (message.getMessageLevel().equals(level)) {
                    filteredMessages.add(message);
                }
            }
        }
        return filteredMessages;
    }

    public FeedPublish findById(String id) {

        FeedPublish result = null;
        if (!id.equals("latest")) {
            FeedPublishEntity feedPublishEntity = repository.findById(Long.parseLong(id));
            if (feedPublishEntity != null) {
                result = entityToDomainConverter.convert(feedPublishEntity);
            }
        }
        return result;
    }

    public List<FeedPublish> loadAll() {
        return entityToDomainConverter.convert(repository.findAll());
    }

    public ContentType[] getTypestoPublish() {
        return typesToPublish;
    }
}
