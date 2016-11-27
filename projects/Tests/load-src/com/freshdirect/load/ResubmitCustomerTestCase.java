/*
 * TransferOrders.java
 *
 * Created on July 29, 2003, 8:02 PM
 */

package com.freshdirect.load;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.adapter.CustomerAdapter;
import com.freshdirect.customer.ejb.ErpCustomerEB;
import com.freshdirect.customer.ejb.ErpCustomerHome;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.sap.SapCustomerI;
import com.freshdirect.sap.ejb.SapException;
import com.freshdirect.sap.ejb.SapGatewayHome;
import com.freshdirect.sap.ejb.SapGatewaySB;



/**
 *
 * @author  mrose
 * @version
 */
public class ResubmitCustomerTestCase extends TestCase {
    
    public ResubmitCustomerTestCase(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        //junit.textui.TestRunner.run(suite());
        junit.textui.TestRunner.run(ResubmitCustomerTestCase.class);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ResubmitCustomerTestCase.class);
        return suite;
    }
    
    private static Object syncOnMe = new Object();
    
    private static List customerIds = new ArrayList();
    
    protected void setUp() {
        synchronized (syncOnMe) {
            if (customerIds.size() == 0) {
                synchronized (customerIds) {
                    customerIds = getNotSubmittedCustomers();
                }
            }
        }
    }
    
    
    public void testResubmitCustomer() {
        
        // pick an order to transfer
        String custId = null;
        synchronized (customerIds) {
            if (customerIds.size() == 0) {
                throw new RuntimeException("No customers to choose from");
            }
            int idx = (int)(Math.random() * customerIds.size());
            custId = (String) customerIds.remove(idx);
        }
        if (custId == null) {
            throw new RuntimeException("Unable to get an custId to resubmit");
        }
        
        Context ctx = null;
        
        try {
          
            ErpCustomerHome custHome = null;
            SapGatewayHome sapHome = null;
            
            try {
                ctx = ErpServicesProperties.getInitialContext();
                custHome = (ErpCustomerHome) ctx.lookup("freshdirect.erp.Customer");
                sapHome = (SapGatewayHome) ctx.lookup("freshdirect.sap.Gateway");
                
                ErpCustomerEB custEB = custHome.findByPrimaryKey(new PrimaryKey(custId));
                ErpCustomerModel custModel = (ErpCustomerModel) custEB.getModel();
                
                SapCustomerI customer = new CustomerAdapter( custModel, null, (ErpAddressModel)custModel.getShipToAddresses().get(0) );

                SapGatewaySB sapSB = sapHome.create();
                sapSB.sendCreateCustomer( custId, customer );
                
            } catch (CreateException ce) {
                throw new FDResourceException(ce);
            } catch (NamingException e) {
                throw new FDResourceException(e);
            } catch (FinderException fe) {
                throw new FDResourceException(fe);
            } catch (RemoteException re) {
                throw new FDResourceException(re);
            } catch (SapException se) {
                throw new FDResourceException(se);
            }
            
        } catch (FDResourceException fdre) {
            fdre.printStackTrace();
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ne) {
                    ne.printStackTrace();
                }
            }
            
        }
        
    }
    
    private final static String notSubmittedCustomersQuery = "select id from cust.customer where sap_id is null";
    
    
    private static List getNotSubmittedCustomers() {
        
        List custIds = new ArrayList();
        Connection conn = null;
        try {
            //
            //  grab a database connection
            //
            conn = DriverManager.getConnection("jdbc:oracle:thin:@db1.nyc2.freshdirect.com:1521:DBSTO01", "FDSTORE_PRDA", "OUT123LOOK");
            //
            // get the sku codes from the last batch loaded
            //
            PreparedStatement ps = conn.prepareStatement(notSubmittedCustomersQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                custIds.add(rs.getString(1));
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
        
        return custIds;
        
        
    }
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
}
