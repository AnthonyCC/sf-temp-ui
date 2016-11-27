/*
 * TransferOrders.java
 *
 * Created on July 29, 2003, 8:02 PM
 */

package com.freshdirect.load;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.fdstore.CallCenterServices;
import com.freshdirect.fdstore.FDResourceException;


/**
 *
 * @author  mrose
 * @version
 */
public class ResubmitOrderTestCase extends TestCase {
    
    public ResubmitOrderTestCase(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        //junit.textui.TestRunner.run(suite());
        junit.textui.TestRunner.run(ResubmitOrderTestCase.class);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ResubmitOrderTestCase.class);
        return suite;
    }
    
    private static Object syncOnMe = new Object();
    
    //private static boolean warm = false;
    
    private static List orderIds = new ArrayList();
    
    protected void setUp() {
        synchronized (syncOnMe) {
            if (orderIds.size() == 0) {
                synchronized (orderIds) {
                    orderIds = getNotSubmittedOrders();
//                    if (!warm) {
//                        ContentFactory cf = ContentFactory.getInstance();
//                        try {
//                            StoreModel store = cf.getStore();
//                            warm = true;
//                        } catch (FDResourceException fdre) {
//                            warm = false;
//                            fdre.printStackTrace();
//                        }
//                    }
                }
            }
        }
    }
    
    
    public void testResubmitOrder() {
        
        // pick an order to transfer
        String orderId = null;
        synchronized (orderIds) {
            if (orderIds.size() == 0) {
                throw new RuntimeException("No orders to choose from");
            }
            int idx = (int)(Math.random() * orderIds.size());
            orderId = (String) orderIds.remove(idx);
        }
        if (orderId == null) {
            throw new RuntimeException("Unable to get an order id to resubmit");
        }
        
        
        try {
            
            CallCenterServices.resubmitOrder(orderId);
        } catch (ErpTransactionException ete) {
            ete.printStackTrace();
        } catch (FDResourceException fdre) {
            fdre.printStackTrace();
        }
        
    }
    
    //private final static String notSubmittedOrdersQuery = "select id from cust.sale where status in ('NEW','MOC','MOD','NSM','MAU')";
    
    
    private final static String notSubmittedOrdersQuery =
        "select s.id from cust.sale s, cust.salesaction sa " +
        "where s.id=sa.sale_id and s.status='MOC' and sa.requested_date=trunc(sysdate+1) and sa.action_type in ('CRO','MOD') " +
        "and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD'))";
     
    
    private static List getNotSubmittedOrders() {
        
        List orderIds = new ArrayList();
        Connection conn = null;
        try {
            //
            //  grab a database connection
            //
            conn = DriverManager.getConnection("jdbc:oracle:thin:@db1.nyc2.freshdirect.com:1521:DBSTO01", "FDSTORE_PRDA", "OUT123LOOK");
            //
            // get the sku codes from the last batch loaded
            //
            PreparedStatement ps = conn.prepareStatement(notSubmittedOrdersQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderIds.add(rs.getString(1));
            }
            rs.close();
            ps.close();
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) { }
            }
        }
        
        return orderIds;
        
        
    }
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
}
