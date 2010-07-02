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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * duplicates attributes copied over from old system by AttributeConverter
 *
 * @author  mrose
 * @version
 */
public class AttributeDuplicator {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        AttributeDuplicator s = new AttributeDuplicator();
        s.go();
    }
    
    /** Creates new slurp */
    public AttributeDuplicator() {
    }
    
    public void go() {
        Connection conn = null;
        boolean success = false;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            
            //
            // duplicate characteristic attributes
            //
            List<AttributeRow> uniqCh = getUniqueCharacteristicAttributes(conn);
            duplicateCharacteristicAttributes(conn, uniqCh);
            //
            // duplicate characteristic value attributes
            //
            List<AttributeRow> uniqCv = getUniqueCharacteristicValueAttributes(conn);
            duplicateCharacteristicValueAttributes(conn, uniqCv);
            
            conn.commit();
            success = true;
        } catch (SQLException sqle) {
            
            sqle.printStackTrace();
        } finally {
            try{
                if (conn != null) {
                    if (!success) {
                        conn.rollback();
                    }
                    conn.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    }
    
    public List<AttributeRow> getUniqueCharacteristicAttributes(Connection conn) throws SQLException {
        //
        // find the unique characteristic attributes
        //
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select root_id, child1_id, atr_type, atr_name, atr_value from attributes " +
        "where root_id not like '000000%' and child2_id is null " +
        "group by root_id, child1_id, atr_type, atr_name, atr_value order by root_id, child1_id, atr_name, atr_value");
        ArrayList<AttributeRow> charAttrs = new ArrayList<AttributeRow>();
        while (rs.next()) {
            AttributeRow ar = new AttributeRow();
            ar.rootId = rs.getString(1);
            ar.child1Id = rs.getString(2);
            ar.atrType = rs.getString(3);
            ar.atrName = rs.getString(4);
            ar.atrValue = rs.getString(5);
            charAttrs.add(ar);
        }
        rs.close();
        stmt.close();
        System.out.println("Read " + charAttrs.size() + " unique attributes from db");
        return charAttrs;
    }
    
    public void duplicateCharacteristicAttributes(Connection conn, List<AttributeRow> uniq) throws SQLException {
        //
        // finds the classes that are missing the attributes for their characteristics that have the same name
        //
        PreparedStatement missingPs = conn.prepareStatement("select cls.SAP_ID, chr.name from class cls, characteristic chr " +
                "where cls.id=chr.CLASS_ID and cls.sap_id<>? and chr.NAME=? " +
                "group by cls.SAP_ID, chr.name");
        //
        // inserts a new attribute
        //
        PreparedStatement dupPs = conn.prepareStatement("insert into attributes (id, root_id, child1_id, atr_type, atr_name, atr_value, date_modified) values (?,?,?,?,?,?,SYSDATE)");
        //
        // loop through the unique stuff
        //
        Iterator<AttributeRow> atrIter = uniq.iterator();
        while (atrIter.hasNext()) {
            AttributeRow uniqAtr = atrIter.next();
            //
            // collect what needs to be duplicated for everything that's currently unique
            //
            ArrayList<AttributeRow> dups = new ArrayList<AttributeRow>();
            missingPs.clearParameters();
            missingPs.setString(1, uniqAtr.rootId);
            missingPs.setString(2, uniqAtr.child1Id);
            ResultSet rs = missingPs.executeQuery();
            while (rs.next()) {
                AttributeRow dup = new AttributeRow();
                dup.rootId = rs.getString(1);
                dup.child1Id = rs.getString(2);
                dups.add(dup);
            }
            rs.close();
            System.out.println("Need to insert attributes for " + dups.size() + " duplicates of characteristic " + uniqAtr.rootId + ":" + uniqAtr.child1Id);
            //
            // assign ids to the new duplicate attributes
            //
            Iterator<AttributeRow> idIter = dups.iterator();
            while (idIter.hasNext()) {
                AttributeRow ar = idIter.next();
                ar.id = getNextId(conn);
            }
            //
            // now loop through the collected dupes and write them to the db with the attributes from the original
            //
            Iterator<AttributeRow> dupIter = dups.iterator();
            while (dupIter.hasNext()) {
                AttributeRow dupAtr = dupIter.next();
                
                System.out.println("Dupe: " + dupAtr.rootId + " " + dupAtr.child1Id + " " + uniqAtr.atrType + " " + uniqAtr.atrName + " " + uniqAtr.atrValue);
                
                dupPs.clearParameters();
                // set the identity from the dup
                dupPs.setString(1, dupAtr.id);
                dupPs.setString(2, dupAtr.rootId);
                dupPs.setString(3, dupAtr.child1Id);
                // and the rest from the original
                dupPs.setString(4, uniqAtr.atrType);
                dupPs.setString(5, uniqAtr.atrName);
                dupPs.setString(6, uniqAtr.atrValue);
                if (1 != dupPs.executeUpdate()) {
                    throw new SQLException("Couldn't insert a row into ATTRIBUTES, id = " + dupAtr.id);
                }
            }
        }
        missingPs.close();
        dupPs.close();
    }
    
    public List<AttributeRow> getUniqueCharacteristicValueAttributes(Connection conn) throws SQLException {
        //
        // find the unique characteristic value attributes
        //
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select root_id, child1_id, child2_id, atr_type, atr_name, atr_value from attributes " +
                "where root_id not like '000000%' and child2_id is not null " +
                "group by root_id, child1_id, child2_id, atr_type, atr_name, atr_value " +
                "order by root_id, child1_id, child2_id, atr_name, atr_value");
        ArrayList<AttributeRow> charValAttrs = new ArrayList<AttributeRow>();
        while (rs.next()) {
            AttributeRow ar = new AttributeRow();
            ar.rootId = rs.getString(1);
            ar.child1Id = rs.getString(2);
            ar.child2Id = rs.getString(3);
            ar.atrType = rs.getString(4);
            ar.atrName = rs.getString(5);
            ar.atrValue = rs.getString(6);
            charValAttrs.add(ar);
        }
        rs.close();
        stmt.close();
        System.out.println("Read " + charValAttrs.size() + " unique attributes from db");
        return charValAttrs;
    }
    
    public void duplicateCharacteristicValueAttributes(Connection conn, List<AttributeRow> uniq) throws SQLException {
        //
        // finds the classes that are missing the attributes for their characteristic values that have the same name
        //
        PreparedStatement missingPs = conn.prepareStatement("select cls.SAP_ID, chr.name, chv.name from class cls, characteristic chr, charvalue chv " + 
                "where cls.id=chr.CLASS_ID and chr.id=chv.CHAR_ID and cls.sap_id<>? and chr.NAME<>? and chv.NAME=? " +
                "group by cls.SAP_ID, chr.name, chv.NAME " +
                "order by cls.SAP_ID, chr.name, chv.NAME");
        //
        // inserts a new attribute
        //
        PreparedStatement dupPs = conn.prepareStatement("insert into attributes (id, root_id, child1_id, child2_id, atr_type, atr_name, atr_value, date_modified) values (?,?,?,?,?,?,?,SYSDATE)");
        //
        // loop through the unique stuff
        //
        Iterator<AttributeRow> atrIter = uniq.iterator();
        while (atrIter.hasNext()) {
            AttributeRow uniqAtr = atrIter.next();
            //
            // collect what needs to be duplicated for everything that's currently unique
            //
            ArrayList<AttributeRow> dups = new ArrayList<AttributeRow>();
            missingPs.clearParameters();
            missingPs.setString(1, uniqAtr.rootId);
            missingPs.setString(2, uniqAtr.child1Id);
            missingPs.setString(3, uniqAtr.child2Id);
            ResultSet rs = missingPs.executeQuery();
            while (rs.next()) {
                AttributeRow dup = new AttributeRow();
                dup.rootId = rs.getString(1);
                dup.child1Id = rs.getString(2);
                dup.child2Id = rs.getString(3);
                dups.add(dup);
            }
            rs.close();
            System.out.println("Need to insert attributes for " + dups.size() + " duplicates of characteristic value " + uniqAtr.rootId + ":" + uniqAtr.child1Id + ":" + uniqAtr.child2Id);
            //
            // assign ids to the new duplicate attributes
            //
            Iterator<AttributeRow> idIter = dups.iterator();
            while (idIter.hasNext()) {
                AttributeRow ar = idIter.next();
                ar.id = getNextId(conn);
            }
            //
            // now loop through the collected dupes and write them to the db with the attributes from the original
            //
            Iterator<AttributeRow> dupIter = dups.iterator();
            while (dupIter.hasNext()) {
                AttributeRow dupAtr = dupIter.next();
                
                System.out.println("Dupe: " + dupAtr.rootId + " " + dupAtr.child1Id + " " + dupAtr.child2Id + " " + uniqAtr.atrType + " " + uniqAtr.atrName + " " + uniqAtr.atrValue);
                dupPs.clearParameters();
                // set the identity from the dup
                dupPs.setString(1, dupAtr.id);
                dupPs.setString(2, dupAtr.rootId);
                dupPs.setString(3, dupAtr.child1Id);
                dupPs.setString(4, dupAtr.child2Id);
                // and the rest from the original
                dupPs.setString(5, uniqAtr.atrType);
                dupPs.setString(6, uniqAtr.atrName);
                dupPs.setString(7, uniqAtr.atrValue);
                if (1 != dupPs.executeUpdate()) {
                    throw new SQLException("Couldn't insert a row into ATTRIBUTES, id = " + dupAtr.id);
                }
            }
        }
        missingPs.close();
        dupPs.close();
    }
    
    public class AttributeRow {
        public String id;
        public String rootId;
        public String child1Id;
        public String child2Id;
        public String atrType;
        public String atrName;
        public String atrValue;
        public java.sql.Timestamp dateModified;
    }
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01", "ERPS_INT", "ERPS_INT");
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
    
}
