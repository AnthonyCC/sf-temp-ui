/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.monitor.ejb;

import java.sql.*;

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
    
    
}