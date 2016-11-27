package com.freshdirect.event;

import java.util.Date;

import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;

import com.freshdirect.framework.util.QuickDateFormat;

public class CSVwithDateLogFormat extends SimpleLayout {

    public String format(LoggingEvent event) {
        return QuickDateFormat.ISO_FORMATTER_2.format(new Date(event.getTimeStamp()))+","+event.getMessage()+'\n';
    }
}
