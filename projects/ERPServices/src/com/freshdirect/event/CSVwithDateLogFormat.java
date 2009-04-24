package com.freshdirect.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;

public class CSVwithDateLogFormat extends SimpleLayout {

    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US); 
    
    public String format(LoggingEvent event) {
        return DATE_FORMAT.format(new Date(event.getTimeStamp()))+","+event.getMessage()+'\n';
    }
}
