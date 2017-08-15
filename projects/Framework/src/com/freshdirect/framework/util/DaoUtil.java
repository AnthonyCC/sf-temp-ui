package com.freshdirect.framework.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class DaoUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = LoggerFactory.getInstance(DaoUtil.class);
	
	public final  static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException sqle) {
            LOGGER.error("problem in closing db connection : "+ " err: " + sqle.getMessage(), sqle);
        }
    }
    
	public final static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqle) {
                LOGGER.error("problem in closing db result set: " + " err: " + sqle.getMessage(), sqle);
            }
        }
    }

    
    public final static void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException sqle) {
                LOGGER.error("problem in closing db prepared statement: " + " err: " + sqle.getMessage(), sqle);
            }
        }
    }
}
