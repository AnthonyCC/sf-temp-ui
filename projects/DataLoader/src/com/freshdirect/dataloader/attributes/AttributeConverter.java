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
import java.util.ListIterator;

/**
 * converts and loads atributes from old db into new db
 *
 * @author  mrose
 * @version
 */
public class AttributeConverter {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        AttributeConverter s = new AttributeConverter();
        s.go();
    }
    
    /** Creates new slurp */
    public AttributeConverter() {
    }
    
    public void go() {
        Connection newConn = null;
        Connection oldConn = null;
        try {
            newConn = getNewConnection();
            oldConn = getOldConnection();
            
            doMaterialAttributes(oldConn, newConn);
            doCharacteristicAttributes(oldConn, newConn);
            doCharacteristicValueAttributes(oldConn, newConn);
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try{
                if (newConn != null)
                    newConn.close();
            } catch (SQLException sqle) { }
            try {
                if (oldConn != null)
                    oldConn.close();
            } catch (SQLException sqle) { }
        }
    }
    
    public void doCharacteristicValueAttributes(Connection oldConn, Connection newConn) throws SQLException {
        //
        // find the corresponding material and children in the old system
        //
        Statement oldStmt = oldConn.createStatement();
        ResultSet rs = oldStmt.executeQuery("select ch.NAME, cv.CHAR_VALUE, a.KEY, a.VALUE, SYSDATE " +
        "from characteristic ch, characteristic_value cv, attributes a " +
        "where ch.id=cv.CHARACTERISTIC_ID and cv.ID=a.ENTITY_ID");
        ArrayList<AttributeRow> charAttrs = new ArrayList<AttributeRow>();
        while (rs.next()) {
            AttributeRow ar = new AttributeRow();
            ar.child1Id = rs.getString(1);
            ar.child2Id = rs.getString(2);
            ar.atrType = "S";
            ar.atrName = rs.getString(3);
            ar.atrValue = rs.getString(4);
            ar.dateModified = rs.getTimestamp(5);
            if ((ar.atrValue != null) && !"".equals(ar.atrValue)) {
                massageAttribute(ar);
                charAttrs.add(ar);
            }
        }
        rs.close();
        oldStmt.close();
        System.out.println("Read " + charAttrs.size() + " attributes from old db");
        //
        // find the classes that these characteristics belong to in the new system
        //
        PreparedStatement newPs = newConn.prepareStatement("select cl.sap_id, ch.NAME, cv.NAME from " +
        "class cl, characteristic ch, charvalue cv where ch.name=? and cv.name=? and cl.ID=ch.CLASS_ID and cv.CHAR_ID=ch.ID");
        ListIterator<AttributeRow> oldIter = charAttrs.listIterator();
        while (oldIter.hasNext()) {
            AttributeRow ar = oldIter.next();
            newPs.clearParameters();
            newPs.setString(1, ar.child1Id);
            newPs.setString(2, ar.child2Id);
            rs = newPs.executeQuery();
            if (rs.next()) {
                ar.rootId = rs.getString(1);
            } else {
                // no match, discard the attribute
                oldIter.remove();
            }
            rs.close();
        }
        newPs.close();
        //
        // assign ids to the rows in the new system
        //
        Iterator<AttributeRow> charAttrIter = charAttrs.iterator();
        while (charAttrIter.hasNext()) {
            AttributeRow ar = charAttrIter.next();
            ar.id = getNextId(newConn);
        }
        //
        // insert the rows into the new system
        //
        newPs = newConn.prepareStatement("insert into attributes (id,root_id,child1_id,child2_id,atr_type,atr_name,atr_value,date_modified) values (?,?,?,?,?,?,?,?)");
        charAttrIter = charAttrs.iterator();
        while (charAttrIter.hasNext()) {
            AttributeRow ar = charAttrIter.next();
            System.out.println("Inserting attribute " + ar.rootId + " " + ar.child1Id + " " + ar.child2Id + " " + ar.atrName + " into new db");
            newPs.clearParameters();
            newPs.setString(1, ar.id);
            newPs.setString(2, ar.rootId);
            newPs.setString(3, ar.child1Id);
            newPs.setString(4, ar.child2Id);
            newPs.setString(5, ar.atrType);
            newPs.setString(6, ar.atrName);
            newPs.setString(7, ar.atrValue);
            newPs.setTimestamp(8, ar.dateModified);
            int rowsAffected = newPs.executeUpdate();
            if (rowsAffected != 1)
                throw new SQLException("Couldn't insert a row in new db");
        }
        newPs.close();
    }
    
    public void doCharacteristicAttributes(Connection oldConn, Connection newConn) throws SQLException {
        //
        // find the corresponding material and children in the old system
        //
        Statement oldStmt = oldConn.createStatement();
        ResultSet rs = oldStmt.executeQuery("select c.NAME, a.KEY, a.VALUE, SYSDATE " +
        "from characteristic c, attributes a " +
        "where c.ID=a.ENTITY_ID");
        ArrayList<AttributeRow> charAttrs = new ArrayList<AttributeRow>();
        while (rs.next()) {
            AttributeRow ar = new AttributeRow();
            ar.child1Id = rs.getString(1);
            ar.atrType = "S";
            ar.atrName = rs.getString(2);
            ar.atrValue = rs.getString(3);
            ar.dateModified = rs.getTimestamp(4);
            if ((ar.atrValue != null) && !"".equals(ar.atrValue)) {
                massageAttribute(ar);
                charAttrs.add(ar);
            }
        }
        rs.close();
        oldStmt.close();
        System.out.println("Read " + charAttrs.size() + " attributes from old db");
        //
        // find the classes that these characteristics belong to in the new system
        //
        PreparedStatement newPs = newConn.prepareStatement("select cl.sap_id, ch.NAME from class cl, characteristic ch where ch.name=? and cl.ID=ch.CLASS_ID");
        ListIterator<AttributeRow> oldIter = charAttrs.listIterator();
        while (oldIter.hasNext()) {
            AttributeRow ar = oldIter.next();
            newPs.clearParameters();
            newPs.setString(1, ar.child1Id);
            rs = newPs.executeQuery();
            if (rs.next()) {
                ar.rootId = rs.getString(1);
            } else {
                // no match, discard the attribute
                oldIter.remove();
            }
            rs.close();
        }
        newPs.close();
        //
        // assign ids to the rows in the new system
        //
        Iterator<AttributeRow> charAttrIter = charAttrs.iterator();
        while (charAttrIter.hasNext()) {
            AttributeRow ar = charAttrIter.next();
            ar.id = getNextId(newConn);
        }
        //
        // insert the rows into the new system
        //
        newPs = newConn.prepareStatement("insert into attributes (id,root_id,child1_id,atr_type,atr_name,atr_value,date_modified) values (?,?,?,?,?,?,?)");
        charAttrIter = charAttrs.iterator();
        while (charAttrIter.hasNext()) {
            AttributeRow ar = charAttrIter.next();
            System.out.println("Inserting attribute " + ar.rootId + " " + ar.child1Id + " " + ar.atrName + " into new db");
            newPs.clearParameters();
            newPs.setString(1, ar.id);
            newPs.setString(2, ar.rootId);
            newPs.setString(3, ar.child1Id);
            newPs.setString(4, ar.atrType);
            newPs.setString(5, ar.atrName);
            newPs.setString(6, ar.atrValue);
            newPs.setTimestamp(7, ar.dateModified);
            int rowsAffected = newPs.executeUpdate();
            if (rowsAffected != 1)
                throw new SQLException("Couldn't insert a row in new db");
        }
        newPs.close();
        
    }
    
    public void doMaterialAttributes(Connection oldConn, Connection newConn) throws SQLException {
        //
        // find the corresponding material and children in the old system
        //
        Statement oldStmt = oldConn.createStatement();
        ResultSet rs = oldStmt.executeQuery("select m.MATERIAL_NUMBER, su.ALTERNATIVE_UMO, a.KEY, a.VALUE, SYSDATE " +
        "from material m, sales_unit su, attributes a " +
        "where a.OBJECT_NAME=6 and m.ID=su.MATERIAL_ID and su.ID=a.ENTITY_ID");
        ArrayList<AttributeRow> matlAttrs = new ArrayList<AttributeRow>();
        while (rs.next()) {
            AttributeRow ar = new AttributeRow();
            ar.rootId = rs.getString(1);
            ar.child1Id = rs.getString(2);
            ar.atrType = "S";
            ar.atrName = rs.getString(3);
            ar.atrValue = rs.getString(4);
            ar.dateModified = rs.getTimestamp(5);
             if ((ar.atrValue != null) && !"".equals(ar.atrValue)) {
                massageAttribute(ar);
                matlAttrs.add(ar);
            }
        }
        rs.close();
        oldStmt.close();
        System.out.println("Read " + matlAttrs.size() + " attributes from old db");
        //
        // assign ids to the rows in the new system
        //
        Iterator<AttributeRow> matlAttrIter = matlAttrs.iterator();
        while (matlAttrIter.hasNext()) {
            AttributeRow ar = matlAttrIter.next();
            ar.id = getNextId(newConn);
        }
        //
        // insert the rows into the new system
        //
        PreparedStatement newPs = newConn.prepareStatement("insert into attributes (id,root_id,child1_id,atr_type,atr_name,atr_value,date_modified) values (?,?,?,?,?,?,?)");
        matlAttrIter = matlAttrs.iterator();
        while (matlAttrIter.hasNext()) {
            AttributeRow ar = matlAttrIter.next();
            System.out.println("Inserting attribute " + ar.rootId + " " + ar.child1Id + " " + ar.atrName + " into new db");
            newPs.clearParameters();
            newPs.setString(1, ar.id);
            newPs.setString(2, ar.rootId);
            newPs.setString(3, ar.child1Id);
            newPs.setString(4, ar.atrType);
            newPs.setString(5, ar.atrName);
            newPs.setString(6, ar.atrValue);
            newPs.setTimestamp(7, ar.dateModified);
            int rowsAffected = newPs.executeUpdate();
            if (rowsAffected != 1)
                throw new SQLException("Couldn't insert a row in new db");
        }
        
    }
    
    public void massageAttribute(AttributeRow ar) {
        // make attribute names lower case
        ar.atrName = ar.atrName.toLowerCase();
        // set the attribute type
        if ("optional".equals(ar.atrName))
            ar.atrType = "B";
        else if ("priority".equals(ar.atrName))
            ar.atrType = "I";
        else
            ar.atrType = "S";
        // fix display_format a touch
        if ("display_format".equals(ar.atrName))
            ar.atrValue = ar.atrValue.toLowerCase();
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
    
    public Connection getOldConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01", "BMSSTG", "BMSSTG");
    }
    
    public Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01", "ERPS_DEV", "ERPS_DEV");
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
