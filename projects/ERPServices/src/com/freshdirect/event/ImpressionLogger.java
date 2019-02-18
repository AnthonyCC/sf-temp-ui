package com.freshdirect.event;

import java.io.IOException;

import org.apache.log4j.Appender;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ImpressionLogger {

    final static Logger LOG = LoggerFactory.getInstance(ImpressionLogger.class);
    
    
    public final static ImpressionLogger REQUEST = new ImpressionLogger(LoggerFactory.getInstance("freshdirect.request"), "request.csv", false);
    public final static ImpressionLogger PRODUCT = new ImpressionLogger(LoggerFactory.getInstance("freshdirect.product"), "product_impression.csv", false);
    public final static ImpressionLogger TAB     = new ImpressionLogger(LoggerFactory.getInstance("freshdirect.tab"), "tab_impression.csv", false);
    public final static ImpressionLogger FEATURE = new ImpressionLogger(LoggerFactory.getInstance("freshdirect.feature"), "feature_impression.csv", false);
    public final static ImpressionLogger TAB_CLICK = new ImpressionLogger(LoggerFactory.getInstance("freshdirect.click.tab"), "tab_clicks.csv", false);
    public final static ImpressionLogger PROD_CLICK = new ImpressionLogger(LoggerFactory.getInstance("freshdirect.click.product"), "product_clicks.csv", false);

    
    final static ImpressionLogger[] ALL = { 
        PRODUCT, REQUEST, TAB, FEATURE, TAB_CLICK, PROD_CLICK
    };
    
    static boolean                       globalEnabled = false; 
    static {
        setGlobalEnabled(FDStoreProperties.isDetailedImpressionLoggingOn());
//        Thread t = new Thread(new Checker(), "impression-logger-rollover-thread");
//        t.setDaemon(true);
//        t.start();
    }

    FileAppender             rfa;
    AsyncAppender                        asyncAppender;
    final Logger                         log;
    final String                         fileName;

    boolean                              enabled = false;

    boolean                              timestamp;
    
    ImpressionLogger(Logger log, String fileName, boolean timestamp) {
        this.log = log;
        this.fileName = fileName;
        this.timestamp = timestamp;
    }

    void init() {
        try {
            // rfa = new DailyRollingFileAppender(timestamp ? (SimpleLayout)new CSVwithDateLogFormat() : new CSVLogFormat(), fileName, ".yyyy_MM_dd");
            rfa = new FileAppender(timestamp ? (SimpleLayout) new CSVwithDateLogFormat() : new CSVLogFormat(), fileName, true);
            rfa.setName("impression logger to file[" + fileName + ']');
        } catch (IOException e) {
            e.printStackTrace();
        }

        asyncAppender = new AsyncAppender();
        asyncAppender.addAppender(rfa);
    }

    public synchronized void setEnabled(final boolean flag) {
        if (flag != enabled) {
            if (flag) {
                init();
                log.addAppender(asyncAppender);
            } else {
                log.removeAllAppenders();
                rfa = null;
                asyncAppender = null;
            }
        }
        enabled = flag;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void logEvent(Object message) {
        if (enabled) {
            log.debug(message);
        }
    }
    
    public void debug(Object message) {
        log.debug(message);
    }
    
    
    public static boolean isGlobalEnabled() {
        return globalEnabled;
    }
    
    public static void setGlobalEnabled(boolean ge) {
        globalEnabled = ge;
        for (int i=0;i<ALL.length;i++) {
            ALL[i].setEnabled(globalEnabled);
        }
    }
    
    public void addAppender(Appender appender) {
        log.addAppender(appender);
    }
    
}
