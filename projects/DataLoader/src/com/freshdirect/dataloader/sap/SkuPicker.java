/*
 * SkuPicker.java
 *
 * Created on November 9, 2001, 5:26 PM
 */

package com.freshdirect.dataloader.sap;

import java.sql.*;

/**
 *
 * @author  mrose
 * @version 
 */
public class SkuPicker {

    /** Creates new SkuPicker */
    public SkuPicker() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        SkuPicker sp = new SkuPicker();
        sp.pick(1);
    }
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    protected Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:oracle:thin:@db1.stage.nyc2.freshdirect.com:1521:DBSTG01", "ERPS", "ERPS");
        c.setAutoCommit(true);
        return c;
    }
    
    protected String getNextId(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT SKU_SEQ.nextval FROM DUAL");
        ResultSet rs = ps.executeQuery();
        int nextId = -1;
        if (rs.next()) {
            nextId = rs.getInt(1);
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        if (nextId == -1) {
            throw new SQLException("Unable to get next sequence number from Oracle sequence");
        } else {
            return String.valueOf(nextId);
        }
    }
    
    public void pick(int numToPick) {
        
        Connection conn = null;
        try {
            conn = getConnection();
            for (int i=0;i<numToPick;i++) {
                String skucode = getNextId(conn);
                System.out.println(pad(skucode));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) { }
            }
        }
    }
    
    int numWidth = 7;
    protected String pad(String orig) {
     
        if (orig.length() > numWidth) {
            return orig.substring(0, numWidth);
        } else {
            while (orig.length() < numWidth) {
                orig = "0" + orig;
            }
            return orig;
        }
        
    }
    
}
