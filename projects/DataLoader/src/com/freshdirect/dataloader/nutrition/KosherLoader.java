/*
 * slurp.java
 *
 * Created on August 22, 2001, 4:07 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.util.*;
import java.sql.*;

/**
 * loads atributes from a textfile into erps db
 *
 * @author  mrose
 * @version
 */
public class KosherLoader {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        KosherLoader s = new KosherLoader();
        s.go();
    }
    
    KosherParser parser = null;
    
    /** Creates new slurp */
    public KosherLoader() {
        parser = new KosherParser();
    }
    
    public void go() {
        Connection conn = null;
        try {
            conn = getConnection();
            
            parser.parseFile("d:/nutrition/kosher/dairy_kosher.txt");
            List exceps = parser.getExceptions();
            if (exceps.size() > 0) {
                Iterator excepIter = exceps.iterator();
                while (excepIter.hasNext()) {
                    Exception ex = (Exception) excepIter.next();
                    System.out.println(ex.getMessage());
                }
                return;
            }
            
            List kosherInfo = parser.getKosherInfo();
            Iterator iIter = kosherInfo.iterator();
            while (iIter.hasNext()) {
                HashMap kosher = (HashMap) iIter.next();
                String skuCode = (String) kosher.get("sku_code");
                String kosherSymbol = (String) kosher.get("kosher_symbol");
                String kosherType = (String) kosher.get("kosher_type");
                if ((skuCode == null) || (skuCode.equals(""))) continue; // skip it
                
                //
                // delete old info
                //
                PreparedStatement ps = conn.prepareStatement("delete from nutrition_info where skucode=? and (type='KSYM' or type='KTYP')");
                ps.setString(1, skuCode);
                ps.executeUpdate();
                ps.close();
                
                //
                // insert kosher symbol
                //
                if ((kosherSymbol != null) && !"".equals(kosherSymbol) && !"NOT_KOSHER".equals(kosherSymbol)) {
                    ps = conn.prepareStatement("insert into nutrition_info (skuCode, type, info) values (?, 'KSYM', upper(?))");
                    ps.setString(1, skuCode);
                    ps.setString(2, kosherSymbol);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Inserted " + skuCode + " : " + kosherSymbol);
                }
                
                //
                // insert kosher type
                //
                if ((kosherType != null) && !"".equals(kosherType)) {
                    kosherType = kosherType.toUpperCase();
                    if (kosherType.equals("DAIRY EQUIPMENT")) kosherType = "DAIRY_EQ";
                    if (kosherType.equals("PAREVE")) kosherType = "PARVE";
                    if (kosherType.equals("DE")) kosherType = "DAIRY_EQ";
                    ps = conn.prepareStatement("insert into nutrition_info (skuCode, type, info) values (?, 'KTYP', upper(?))");
                    ps.setString(1, skuCode);
                    ps.setString(2, kosherType);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Inserted " + skuCode + " : " + kosherType);
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
        return DriverManager.getConnection("jdbc:oracle:thin:@db1.stage.nyc2.freshdirect.com:1521:DBSTG01", "ERPS", "ERPS");
    }
    
}
