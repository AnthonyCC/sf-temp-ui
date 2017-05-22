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

    private final BlockingQueue<AnalyticsEventI> buffer = new LinkedBlockingQueue<AnalyticsEventI>(10000); //limit buffer size to 10 k rather than default Integer.MAX_VALUE

    private final EventSinkI eventConsumer;

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

    private EventLoggerService() {
        this.eventConsumer = new WebSink();

        // event consumer function
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        AnalyticsEventI event = buffer.take();
                        // LOGGER.debug("[<<pop] " + event);
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
        Thread t = new Thread(r, "UNBXD Event Sink");
        t.setDaemon(true);
        t.start();
    }

    @Override
    public boolean log(AnalyticsEventI event) {
        // LOGGER.debug("[push>>] " + event);
        return this.buffer.offer(event);

    }
}
