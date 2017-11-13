package com.freshdirect.framework.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.log.LoggerFactory;

public class DaoUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = LoggerFactory.getInstance(DaoUtil.class);
	
	public final static void close(Connection conn) {
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

    
    public final static void close(Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException sqle) {
                LOGGER.error("problem in closing db prepared statement: " + " err: " + sqle.getMessage(), sqle);
            }
        }
    }
	public final static void close(ResultSet rs, Statement ps) {
		close(rs, ps, null);
	}
    
	public final static void close(ResultSet rs, Statement ps, Connection conn) {
		try {
			closePreserveException(rs, ps, conn);
		} catch (SQLException sqle) {
			LOGGER.error("error while cleaning up the db resources");
		}
	}

	public final static void closePreserveException(ResultSet rs, Statement ps) throws SQLException {
		closePreserveException(rs, ps, null);
	}
	public final static void closePreserveException(ResultSet rs, Statement ps, Connection conn) throws SQLException {
		try {
			try {
				if (rs != null) {
					rs.close();
				}				
			} finally {
				if (ps != null) {
					ps.close();
				}
			}
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
    
}
