package com.freshdirect.webapp.unbxdanalytics.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.eventsink.EventSinkI;

public final class EventLoggerService implements EventSinkI {

    private static final Logger LOGGER = LoggerFactory.getInstance(EventLoggerService.class);

    private static EventLoggerService sharedInstance = null;

    private EventLoggerService() {
        // FIXME change this to the final consumer
        this.eventConsumer = new EventSinkI() {

            @Override
            public boolean log(AnalyticsEventI event) {
                LOGGER.debug("Consuming event " + event.getType());
                return false;
            }
        };

        // event consumer function
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        AnalyticsEventI event = buffer.take();
                        eventConsumer.log(event);
                    } catch (RuntimeException e) {
                        LOGGER.warn("Could not log event due to: ", e);
                    } catch (InterruptedException e) {
                        LOGGER.warn(e);
                    }
                }
            }

        };

        // run the consumer thread
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    }

    public static EventLoggerService getInstance() {
        if (sharedInstance == null) {
            synchronized (EventLoggerService.class) {
                if (sharedInstance == null) {
                    sharedInstance = new EventLoggerService();
                }
            }
        }
        return sharedInstance;
    }

    private final BlockingQueue<AnalyticsEventI> buffer = new LinkedBlockingQueue<AnalyticsEventI>();

    private final EventSinkI eventConsumer;

    @Override
    public boolean log(AnalyticsEventI event) {

        return this.buffer.offer(event);

    }
}
