package com.freshdirect.cms.publish.flow.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.flow.ConsumerTask;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.PublishMessageLoggerService;

public class MediaDeltaPublisherTask extends ConsumerTask<List<String>> {

    private static final String MEDIA_DELTA_FOLDERNAME = "media_deltas";

    private static final Logger LOGGER = Logger.getLogger(MediaDeltaPublisherTask.class);

    private final PublishMessageLoggerService publishMessageLogger = PublishMessageLoggerService.getInstance();

    private final String targetPath;
    private final String repositoryUrl;

    public MediaDeltaPublisherTask(String publishId, Phase phase, List<String> mediaUris, String targetPath, String repositoryUrl) {
        super(publishId, phase, mediaUris);
        this.targetPath = targetPath;
        this.repositoryUrl = repositoryUrl;
    }

    @Override
    public void run() {
        publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.INFO, "Generating media delta", MediaDeltaPublisherTask.class.getSimpleName()));
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
            		publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.WARNING, "There is a missing URL " + baseUrl.getPath() + childPath, MediaDeltaPublisherTask.class.getSimpleName()));
            	}
            }
        } catch (MalformedURLException e) {
        	LOGGER.error("There is a missing URL " + repositoryUrl, e);
        	publishMessageLogger.log(publishId, new PublishMessage(PublishMessage.WARNING, "There is a missing URL " + repositoryUrl, MediaDeltaPublisherTask.class.getSimpleName()));
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
