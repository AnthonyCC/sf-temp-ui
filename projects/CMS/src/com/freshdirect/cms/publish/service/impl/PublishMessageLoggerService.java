package com.freshdirect.cms.publish.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.freshdirect.cms.publish.PublishMessage;
import com.freshdirect.cms.publish.service.PublishMessageLogger;

public class PublishMessageLoggerService implements PublishMessageLogger {

    private static final Logger LOGGER = Logger.getLogger(PublishMessageLoggerService.class);

    private static final PublishMessageLoggerService SHARED_INSTANCE = new PublishMessageLoggerService();

    private final BlockingQueue<Map<String, PublishMessage>> messageQueue = new LinkedBlockingQueue<Map<String, PublishMessage>>();

    private final PublishMessageDBLoggerService messageConsumer;

    private AtomicBoolean mute = new AtomicBoolean(false);

    public static PublishMessageLoggerService getInstance() {
        return SHARED_INSTANCE;
    }

    private PublishMessageLoggerService() {
        messageConsumer = new PublishMessageDBLoggerService();

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Map<String, PublishMessage> messageByPublishId = messageQueue.take();

                        for (String publishId : messageByPublishId.keySet()) {
                            messageConsumer.log(publishId, messageByPublishId.get(publishId));
                        }

                    } catch (InterruptedException e) {
                        LOGGER.warn("Interruped!", e);
                    }
                }
            }
        };

        Thread messageLoggerThread = new Thread(runnable, "PublishMessage logger thread");
        messageLoggerThread.setDaemon(true);
        messageLoggerThread.start();
    }

    @Override
    public void log(String publishId, PublishMessage message) {
        if (!mute.get()) {
            HashMap<String, PublishMessage> messageByPublishId = new HashMap<String, PublishMessage>();
            messageByPublishId.put(publishId, message);
            messageQueue.offer(messageByPublishId);
        }
    }

    /**
     * Mutes the logging, aka. no more messages written out.
     */
    public void disableLogging() {
        mute.set(true);
    }

    /**
     * Unmutes the logging, aka. messages are written out again.
     */
    public void enableLogging() {
        mute.set(false);
    }

}
