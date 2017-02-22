package com.freshdirect.cms.publish.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

abstract class JdbcRepository {
    
    final static int FETCH_SIZE = 1000;
    
    Statement createFastReaderStatement(final Connection conn) throws SQLException {
        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        stmt.setFetchSize(FETCH_SIZE);

        return stmt;
    }

    
    /**
     * Close JDBC related resources safely
     * 
     * @param rs
     * @param stmt
     * @param conn
     */
    void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }
}
