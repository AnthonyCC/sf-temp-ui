package com.freshdirect.cms.publish.flow.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.service.xml.ContentNodeSerializer;
import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.ConsumerTask;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.util.QuickDateFormat;

public abstract class PublisherTask extends ConsumerTask<Collection<ContentNodeI>> {

    private static final Logger LOGGER = Logger.getLogger(PublisherTask.class);
    private static final String TAB = "\t";

    public static final String DC_NAMESPACE = "http://purl.org/dc/elements/1.1";

    protected final PublishMessageLoggerService publishMessageLoggerService = PublishMessageLoggerService.getInstance();

    protected final String targetXmlFileName;
    protected String targetPath;
    protected EnumEStoreId storeId;

    protected PublisherTask(String publishId, Phase phase, Collection<ContentNodeI> input, String targetPath, String targetXmlFileName, EnumEStoreId storeId) {
        super(publishId, phase, input);
        this.targetPath = targetPath;
        this.targetXmlFileName = targetXmlFileName;
        this.storeId = storeId;
    }

    @Override
    public void run() {
        publishMessageLoggerService.log(publishId, new PublishMessage(PublishMessage.INFO, "Generating " + targetXmlFileName, storeId.getContentId(), getClass().getSimpleName()));
        ContentNodeSerializer serializer = new ContentNodeSerializer();

        Document doc = serializer.visitNodes(new ArrayList<ContentNodeI>(input));

        Element rootElement = doc.getRootElement();
        rootElement.addNamespace("dc", DC_NAMESPACE);
        rootElement.addElement("dc:type").addText(targetPath);
        // IF YOU CHANGE DESCRIPTION THEN UPDATE FlexContentHandler ACCORDINGLY !!!
        rootElement.addElement("dc:description").addText("PublishId: " + publishId);
        rootElement.addElement("dc:date").addText(QuickDateFormat.ISO_FORMATTER.format(new Date()));

        Writer writer = null;
        XMLWriter xmlWriter = null;
        try {
            File targetFile = new File(targetPath + File.separator + targetXmlFileName);

            if (targetXmlFileName.endsWith(".xml.gz")) {
                writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(targetFile)));
            } else if (targetXmlFileName.endsWith(".xml")) {
                writer = new OutputStreamWriter(new FileOutputStream(targetFile));
            } else {
                publishMessageLoggerService.log(publishId,
                        new PublishMessage(PublishMessage.ERROR, "Unrecognized output file format of " + targetXmlFileName, storeId.getContentId(), getClass().getSimpleName()));
                throw new CmsRuntimeException("Unrecognized output file format!");
            }

            OutputFormat format = new OutputFormat();
            format.setEncoding(CharEncoding.UTF_8);
            format.setIndent(TAB);
            format.setNewlines(true);

            xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(doc);
            xmlWriter.flush();
            publishMessageLoggerService.log(publishId,
                    new PublishMessage(PublishMessage.INFO, "Generated " + targetXmlFileName + " successfully", storeId.getContentId(), getClass().getSimpleName()));

        } catch (IOException e) {
            publishMessageLoggerService.log(publishId,
                    new PublishMessage(PublishMessage.ERROR, "Error while generating " + targetXmlFileName, storeId.getContentId(), getClass().getSimpleName()));
            throw new CmsRuntimeException(e);
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
