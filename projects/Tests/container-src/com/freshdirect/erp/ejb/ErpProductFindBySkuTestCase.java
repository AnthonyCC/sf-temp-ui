/*
 * prdTest.java
 *
 * Created on August 13, 2001, 5:12 PM
 */

package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.TestCase;

import com.freshdirect.TestUtil;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductModel;

/**
 *
 * @author  mrose
 * @version
 */
public class ErpProductFindBySkuTestCase extends TestCase {
    
    protected Context ctx;
    protected Connection conn;
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(new ErpProductFindBySkuTestCase("testFindBySku"));
    }
    
    /** Creates new prdTest */
    public ErpProductFindBySkuTestCase(String testName) {
        super(testName);
    }
    
    protected void setUp() throws NamingException, SQLException {
        ctx = TestUtil.getInitialContext();
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        conn = DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01", "ERPS_DEV", "ERPS_DEV");
    }
    
    protected void tearDown() throws NamingException, SQLException {
        conn.close();
        ctx.close();
    }
    
    public void testFindBySku() throws RemoteException, CreateException, RemoveException, FinderException, SQLException, NamingException {
        
        //
        // get the sku codes from the last batch loaded
        //
        ArrayList skus = new ArrayList();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select sku_code from product where version=(select max(version) from product)");
        while (rs.next()) {
            skus.add(rs.getString(1));
        }
        rs.close();
        stmt.close();
        
        //
        // shuffle 
        //
        java.util.Collections.shuffle(skus);
        
        ErpProductHome prdHome = (ErpProductHome) ctx.lookup("freshdirect.erp.Product");
        Iterator skuIter = skus.iterator();
        while (skuIter.hasNext()) {
            String skuCode = (String) skuIter.next();
            
            ErpProductEB erpPrdEB = prdHome.findBySkuCode(skuCode);
            ErpProductModel erpPrdModel = (ErpProductModel) erpPrdEB.getModel();
            
            assertEquals("Sku codes are equal", skuCode, erpPrdModel.getSkuCode());
            
            ErpMaterialModel erpMatlModel = erpPrdModel.getProxiedMaterial();
            assertNotNull("Material Proxy has Proxied Material", erpMatlModel);
            assertEquals("sales unit count", erpPrdModel.getSalesUnits().size(), erpMatlModel.numberOfSalesUnits() - erpPrdModel.getHiddenSalesUnitPKs().length);
            
            
        }
        
    }
    
}
