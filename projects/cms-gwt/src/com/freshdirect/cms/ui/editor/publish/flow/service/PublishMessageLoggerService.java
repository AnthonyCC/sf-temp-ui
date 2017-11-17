package com.freshdirect.cms.ui.editor.publish.flow.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;

@Service
public class PublishMessageLoggerService implements PublishMessageLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishMessageLoggerService.class);

    private final BlockingQueue<Map<Long, StorePublishMessage>> messageQueue = new LinkedBlockingQueue<Map<Long, StorePublishMessage>>();

    @Autowired
    private PublishMessageDBLoggerService messageConsumer;

    private AtomicBoolean mute = new AtomicBoolean(false);

    @PostConstruct
    private void initializeLogger() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Map<Long, StorePublishMessage> messageByPublishId = messageQueue.take();

                        for (Long publishId : messageByPublishId.keySet()) {
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
    public void log(Long publishId, StorePublishMessage message) {
        if (!mute.get()) {
            HashMap<Long, StorePublishMessage> messageByPublishId = new HashMap<Long, StorePublishMessage>();
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
