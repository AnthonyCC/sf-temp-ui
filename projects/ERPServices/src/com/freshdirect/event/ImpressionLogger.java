package com.freshdirect.event;

import java.io.IOException;

import org.apache.log4j.AsyncAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

import com.freshdirect.framework.util.log.LoggerFactory;

public class ImpressionLogger {

    final static Logger LOG = LoggerFactory.getInstance("freshdirect.impressions");
    static RollingFileAppender rfa;
    static AsyncAppender asyncAppender;

    static boolean enabled = false;
    
    static void init() {
        try {
            rfa = new RollingFileAppender(new CSVLogFormat(), "impressions.csv", true);
            rfa.setName("impression logger to file");
            rfa.setMaxBackupIndex(1000);
            rfa.setMaximumFileSize(1024*1024*100);
            //rfa.setMaximumFileSize(1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        asyncAppender = new AsyncAppender();
        asyncAppender.addAppender(rfa);
    }

    
    public static synchronized void setEnabled(final boolean flag) {
        if (flag!=enabled) {
            if (flag) {
                init();
                LOG.addAppender(asyncAppender);
            } else {
                LOG.removeAllAppenders();
                rfa = null;
                asyncAppender = null;
            }
        }
        enabled = flag;
    }
    
    public static boolean isEnabled() {
        return enabled;
    }
    
    public static void logEvent(Object message) {
        if (enabled) {
            LOG.info(message);
        }
    }

    
}
