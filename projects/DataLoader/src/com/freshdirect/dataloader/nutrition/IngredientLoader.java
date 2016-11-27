/*
 * slurp.java
 *
 * Created on August 22, 2001, 4:07 PM
 */

package com.freshdirect.dataloader.nutrition;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * loads atributes from a textfile into erps db
 *
 * @author  mrose
 * @version
 */
public class IngredientLoader {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        
        IngredientLoader s = new IngredientLoader();
        s.go();
    }
    
    IngredientParser parser = null;
    
    /** Creates new slurp */
    public IngredientLoader() {
        parser = new IngredientParser();
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
            
            List<HashMap<String, String>> ingredientList = parser.getIngredients();
            Iterator<HashMap<String, String>> iIter = ingredientList.iterator();
            while (iIter.hasNext()) {
                HashMap ingr = iIter.next();
                String skuCode = (String) ingr.get("sku_code");
                String ingredients = (String) ingr.get("notes");
                if ((skuCode == null) || (skuCode.equals(""))) continue; // skip it
                if ((ingredients == null) || (ingredients.equals(""))) continue; // skip it
                
                //
                // see if these ingredients are already defined
                //
                int count = 0;
                PreparedStatement ps = conn.prepareStatement("select count(*) from nutrition_info where skucode=? and type='HNGR'");
                ps.setString(1, skuCode);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) count = rs.getInt(1);
                rs.close();
                ps.close();
                
                if (count > 0) {
                    //
                    // do an update
                    //
                    ps = conn.prepareStatement("update nutrition_info set info=? where skucode=? and type='HNGR'");
                    ps.setString(1, ingredients);
                    ps.setString(2, skuCode);
                    ps.executeUpdate();
                    ps.close();
                    System.out.println("Updated " + skuCode + " : " + ingredients);
                } else {
                    //
                    // do an insert
                    //
                    ps = conn.prepareStatement("insert into nutrition_info (skuCode, type, info) values (?, 'HNGR', ?)");
                    ps.setString(1, skuCode);
                    ps.setString(2, ingredients);
                    try {
                        ps.executeUpdate();
                        System.out.println("Inserted " + skuCode + " : " + ingredients);
                    } catch (SQLException sqle) {
                        System.out.println("Error!  Probably tried to insert a duplicate ingredient row...");
                    }
                    ps.close();
                }
                //
                // unhide nutrition
                //
                //ps = conn.prepareStatement("delete from nutrition where sku_code=? and nutrition_type='IGNORE'");
                //ps.setString(1, skuCode);
                //ps.executeUpdate();
                //ps.close();
                
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
