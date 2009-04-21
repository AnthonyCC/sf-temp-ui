package com.freshdirect.event;

import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;

public class CSVLogFormat extends SimpleLayout {

    public String format(LoggingEvent event) {
        return event.getMessage().toString() + '\n';
    }

}
