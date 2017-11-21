package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.CharEncoding;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.freshdirect.cms.contentio.xml.ContentToXmlDocumentConverter;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.EStoreId;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.ConsumerTask;
import com.google.common.base.Charsets;

public abstract class PublisherTask extends ConsumerTask<Map<ContentKey, Map<Attribute, Object>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublisherTask.class);
    private static final String TAB = "\t";

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.US);

    public static final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1";

    @Autowired
    protected PublishMessageLoggerService publishMessageLoggerService;

    @Autowired
    private ContentToXmlDocumentConverter converter;

    protected String targetXmlFileName;

    protected String targetPath;

    /**
     * Store ID, only used for logging
     */
    protected EStoreId storeId = EStoreId.FD;

    public void setTargetXmlFileName(String targetXmlFileName) {
        this.targetXmlFileName = targetXmlFileName;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setStoreId(EStoreId storeId) {
        this.storeId = storeId;
    }

    @Override
    public void run() {
        publishMessageLoggerService.log(publishId,
                new StorePublishMessage(StorePublishMessageSeverity.INFO, "Generating " + targetXmlFileName, storeId.getContentId(), getClass().getSimpleName()));

        Document doc = converter.convert(input);

        Element rootElement = doc.getRootElement();
        rootElement.addNamespace("dc", DC_NAMESPACE);
        rootElement.addElement("dc:type").addText(targetPath);
        // IF YOU CHANGE DESCRIPTION THEN UPDATE FlexContentHandler ACCORDINGLY !!!
        rootElement.addElement("dc:description").addText("PublishId: " + publishId);
        rootElement.addElement("dc:date").addText(DATETIME_FORMATTER.print(new DateTime()));

        Writer writer = null;
        XMLWriter xmlWriter = null;
        try {
            File targetFile = new File(targetPath + File.separator + targetXmlFileName);

            if (targetXmlFileName.endsWith(".xml.gz")) {
                writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(targetFile)), Charsets.UTF_8);
            } else if (targetXmlFileName.endsWith(".xml")) {
                writer = new OutputStreamWriter(new FileOutputStream(targetFile), Charsets.UTF_8);
            } else {
                publishMessageLoggerService.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.ERROR, "Unrecognized output file format of " + targetXmlFileName,
                        storeId.getContentId(), getClass().getSimpleName()));

                throw new RuntimeException("Unrecognized output file format!");
            }

            OutputFormat format = new OutputFormat();
            format.setEncoding(CharEncoding.UTF_8);
            format.setIndent(TAB);
            format.setNewlines(true);

            xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(doc);
            xmlWriter.flush();
            publishMessageLoggerService.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Generated " + targetXmlFileName + " successfully",
                    storeId.getContentId(), getClass().getSimpleName()));

        } catch (IOException e) {
            publishMessageLoggerService.log(publishId,
                    new StorePublishMessage(StorePublishMessageSeverity.ERROR, "Error while generating " + targetXmlFileName, storeId.getContentId(), getClass().getSimpleName()));
            throw new RuntimeException(e);
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException e) {
                    LOGGER.error("Exception while closing xmlWriter", e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOGGER.error("Exception while closing writer", e);
                }
            }
        }
    }
}
