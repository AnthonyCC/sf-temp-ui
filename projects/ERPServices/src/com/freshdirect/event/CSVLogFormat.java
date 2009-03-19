package com.freshdirect.event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;

public class CSVLogFormat extends SimpleLayout {

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    public String format(LoggingEvent event) {
        return dateFormat.format(new Date(event.getTimeStamp()))+','+event.getMessage()+'\n';
    }
}
