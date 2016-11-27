/*
 * slurp.java
 *
 * Created on August 22, 2001, 4:07 PM
 */

package com.freshdirect.dataloader.attributes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * loads atributes from a textfile into erps db
 *
 * @author  mrose
 * @version
 */
public class AttributeSpreadsheetLoader {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        AttributeSpreadsheetLoader s = new AttributeSpreadsheetLoader();
        s.go();
    }
    
    AttributeSpreadsheetParser parser = null;
    
    /** Creates new slurp */
    public AttributeSpreadsheetLoader() {
        parser = new AttributeSpreadsheetParser();
    }
    
    public void go() {
        Connection conn = null;
        try {
            conn = getConnection();
            
            parser.parseFile("d:/sourcesafe/dairy_package.txt");
            List exceps = parser.getExceptions();
            if (exceps.size() > 0) {
                Iterator excepIter = exceps.iterator();
                while (excepIter.hasNext()) {
                    Exception ex = (Exception) excepIter.next();
                    System.out.println(ex.getMessage());
                }
                return;
            }
            
            List attrs = parser.getAttributes();
            Iterator attIter = attrs.iterator();
            while (attIter.hasNext()) {
                AttributeRow ar = (AttributeRow) attIter.next();
                if ((ar.atrValue == null) || (ar.atrValue.equals(""))) continue; // skip it
                //
                // convert rootId from skucode to SAP material number
                //
                String matlNum = getSapId(conn, ar.rootId);
                if (matlNum == null) continue; // skip it
                ar.rootId = matlNum;
                ar.child1Id = "EA";
                ar.atrName = "description";
                ar.atrType = "S";
                while (ar.rootId.length() < 18)
                    ar.rootId = "0" + ar.rootId;
                
                System.out.println(ar);
                
                //
                // see if this attribute is already defined
                //
                String id = "";
                PreparedStatement ps = conn.prepareStatement("select id from attributes where root_id=? and child1_id=? and child2_id is null and atr_name=?");
                ps.setString(1, ar.rootId);
                ps.setString(2, ar.child1Id);
                ps.setString(3, ar.atrName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) id = rs.getString(1);
                rs.close();
                ps.close();
                
                if (!id.equals("")) {
                    //
                    // do an update
                    //
                    ps = conn.prepareStatement("update attributes set atr_value=?, date_modified=SYSDATE where id=?");
                    ps.setString(1, ar.atrValue);
                    ps.setString(2, id);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Updated " + ar);
                } else {
                    //
                    // do an insert
                    //
                    id = getNextId(conn);
                    ps = conn.prepareStatement("insert into attributes (id,root_id,child1_id,atr_type,atr_name,atr_value,date_modified) values (?,?,?,?,?,?,SYSDATE)");
                    ps.setString(1, id);
                    ps.setString(2, ar.rootId);
                    ps.setString(3, ar.child1Id);
                    ps.setString(4, ar.atrType);
                    ps.setString(5, ar.atrName);
                    ps.setString(6, ar.atrValue);
                    try {
                        ps.executeUpdate();
                        System.out.println("Inserted " + ar);
                    } catch (SQLException sqle) {
                        System.out.println("Error!  Probably tried to insert a duplicate attribute row...");
                    }
                    ps.close();
                    
                }
                
            }
            
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try{
                if (conn != null) conn.close();
            } catch (SQLException sqle) { }
        }
    }
    
    
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@ems1.nyc1.freshdirect.com:1521:DBEMS01", "ERPS_TST", "ERPS_TST");
    }
    
    protected String getNextId(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT SYSTEM_SEQ.nextval FROM DUAL");
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
    
    private String skuQuery =   "select mtl.sap_id from product prd, materialproxy mpx, material mtl " +
                                "where prd.id=mpx.product_id and mpx.mat_id=mtl.id and prd.sku_code = ? and prd.id in " +
                                "(select prd2.id from product prd2 where prd2.version=(select max(version) from product where sku_code=prd.sku_code))";
    
    protected String getSapId(Connection conn, String skuCode) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(skuQuery);
        ps.setString(1, skuCode);
        ResultSet rs = ps.executeQuery();
        String matlNum = null;
        if (rs.next()) {
            matlNum = rs.getString(1);
        }
        rs.close();
        ps.close();
        return matlNum;
    }
    
}
