package com.freshdirect.framework.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class MemcacheConfiguration {

    final static Logger LOG = LoggerFactory.getInstance(MemcacheConfiguration.class);

    private static MemcachedClient client;

    private static String host;
    
    public final static int DEFAULT_PORT = 11211;
    public final static int ERROR_COUNT = 3;
    public final static long REENABLE_TIMELIMIT = 10*60*1000;
    
    private static int port = DEFAULT_PORT;
    
    private static boolean enabled = true;
    private static int errorCount = 0;
    private static long disabledTimeStamp = 0;
    
    public static MemcachedClient getClient() {
        return client;
    }
    
    public static void setClient(MemcachedClient client) {
        LOG.info("setting up memcached client ["+client+']');
        MemcacheConfiguration.client = client;
    }

    /**
     * 
     */
    public synchronized static void configureClient(String newHost, int newPort) {
        if (StringUtils.equals(host, newHost) && port == newPort) {
            return;
        }
        stopClient();
        host = newHost;
        port = newPort;
        if (port <= 0) {
            port = DEFAULT_PORT; 
        }
        LOG.info("initalizing memcached server to " + host + ":" + port);
        
        if ((host != null) && (host.trim().length() > 0)) {
            try {
                client = new MemcachedClient(new InetSocketAddress(host, port));
                setEnabled(true);
            } catch (IOException e) {
                LOG.error("error during accessing " + host, e);
            }
        }
    }

    /**
     * 
     */
    public synchronized static void stopClient() {
        if (client != null) {
            LOG.info("closing connection to memcached server ["+client+']');
            client.shutdown(1, TimeUnit.SECONDS);
        } 
        client = null;
    }
    
    public static String getHost() {
        return host;
    }
    
    public static int getPort() {
        return port;
    }
    
    public static boolean isEnabled() {
        if (!enabled) {
            final long time = System.currentTimeMillis() - disabledTimeStamp;
            if (time > REENABLE_TIMELIMIT) {
                LOG.error("Elapsed " + time + " since we disabled the external cache, try again ...");
                setEnabled(true);
            }
        }
        return enabled;
    }
    
    static void setEnabled(boolean e) {
        if (e) {
            enabled = true;
            errorCount= 0;
        } else {
            enabled = false;
            disabledTimeStamp = System.currentTimeMillis();
        }
    }
    
    public static void timeoutError() {
        errorCount++;
        if (errorCount > ERROR_COUNT) {
            LOG.error("Timeout counter reached " + errorCount + " disable external cache");
            setEnabled(false);
        }
    }
    

}
