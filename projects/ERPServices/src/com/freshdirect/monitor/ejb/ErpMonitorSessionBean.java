/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.monitor.ejb;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.Properties;

import org.apache.log4j.*;
import com.freshdirect.framework.util.log.LoggerFactory;

import com.freshdirect.framework.core.*;
import com.freshdirect.fdstore.FDResourceException;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class ErpMonitorSessionBean extends SessionBeanSupport {
    
    private final static Category LOGGER = LoggerFactory.getInstance( ErpMonitorSessionBean.class );
    
    public void healthCheck() throws FDResourceException {
        LOGGER.info("starting healthCheck");
        Connection conn = null;
        try {
            //
            // do health check
            //
            conn = getConnection();
            java.sql.PreparedStatement ps = conn.prepareStatement("select count(*) from dual");
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new FDResourceException("healthCheck could not \"select count(*) from dual\"");
            } else {
                LOGGER.info("healthCheck performed query OK");
            }
            rs.close();
            rs = null;
            ps.close();
            ps = null;
            
        } catch (java.sql.SQLException sqle) {
            throw new FDResourceException(sqle);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (java.sql.SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after associating cookie to registration.");
                    throw new FDResourceException(sqle2);
                }
            }
        }
        LOGGER.info("finished healthCheck");
    }
    
    public Properties monitorAndLoadProperties(String type,
			String clusterName, String nodeName, Properties defaults)throws FDResourceException, RemoteException {
        Properties props = new Properties(defaults);
        Properties propss =null;
       Connection conn =null;
        try{
        	conn = getConnection();
        	 propss = PropertyDao.loadProperties(conn,type, clusterName, nodeName);
            if (propss != null) {
                        
	           for(String key:propss.stringPropertyNames())
	           {
	        	   props.put(key, propss.getProperty(key));
	           }
            }
            
 
            }catch( SQLException e){
                throw new FDResourceException(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (java.sql.SQLException sqle2) {
                        LOGGER.warn("Unable to close connection after associating cookie to registration.");
                        throw new FDResourceException(sqle2);
                    }
                }
            }
            return props;

    }
}