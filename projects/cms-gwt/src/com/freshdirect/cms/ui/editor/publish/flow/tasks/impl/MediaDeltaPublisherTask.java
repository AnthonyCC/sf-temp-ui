package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.ConsumerTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MediaDeltaPublisherTask extends ConsumerTask<List<String>> {

    private static final String MEDIA_DELTA_FOLDERNAME = "media_deltas";

    private static final Logger LOGGER = LoggerFactory.getLogger(MediaDeltaPublisherTask.class);

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    private String targetPath;

    private String repositoryUrl;

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    @Override
    public void run() {
        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Generating media delta", MediaDeltaPublisherTask.class.getSimpleName()));
        File rootDir = new File(targetPath, MEDIA_DELTA_FOLDERNAME);

        final URL baseUrl;
        try {
            baseUrl = new URL(repositoryUrl);

            for (String childPath : input) {
                File destinationFile = new File(rootDir, childPath);
                URL sourceURL = null;

                try {
                    sourceURL = new URL(baseUrl.getProtocol(), baseUrl.getHost(), baseUrl.getPort(), baseUrl.getPath() + childPath);
                    downloadFileFromRepository(sourceURL, destinationFile);
                } catch (MalformedURLException e) {
                    LOGGER.error("There is a missing URL " + baseUrl.getPath() + childPath, e);
                    publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.WARNING, "There is a missing URL " + baseUrl.getPath() + childPath,
                            MediaDeltaPublisherTask.class.getSimpleName()));
                }
            }
        } catch (MalformedURLException e) {
            LOGGER.error("There is a missing URL " + repositoryUrl, e);
            publishMessageLogger.log(publishId,
                    new StorePublishMessage(StorePublishMessageSeverity.WARNING, "There is a missing URL " + repositoryUrl, MediaDeltaPublisherTask.class.getSimpleName()));
        }
    }

    @Override
    public String getName() {
        return "Publish media delta files to " + targetPath;
    }

    private void downloadFileFromRepository(URL sourceURL, File destinationFile) {
        LOGGER.debug("Downloading " + sourceURL.getPath() + " to " + destinationFile.getPath());
        ReadableByteChannel readableByteChannel = null;
        FileOutputStream fileOutputStream = null;
        try {
            createParentDirectory(destinationFile);

            readableByteChannel = Channels.newChannel(sourceURL.openStream());
            fileOutputStream = new FileOutputStream(destinationFile);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException e) {
            LOGGER.error("Cannot check out missing media " + sourceURL, e);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Exception while closing fileOutputStream", e);
                }
            }
            if (readableByteChannel != null) {
                try {
                    readableByteChannel.close();
                } catch (IOException e) {
                    LOGGER.error("Exception while closing readableByteChannel", e);
                }
            }
        }
    }

    private void createParentDirectory(File file) {
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
    }
}
