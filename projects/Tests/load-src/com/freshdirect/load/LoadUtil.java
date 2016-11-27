/*
 * $Workfile:LoadUtil.java$
 *
 * $Date:3/7/03 3:57:49 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.load;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.freshdirect.fdstore.customer.FDIdentity;

import com.freshdirect.fdstore.content.*;
import com.freshdirect.fdstore.warmup.Warmup;
import com.freshdirect.fdstore.FDResourceException;

/**
 *
 *
 * @version $Revision:3$
 * @author $Author:Kashif Nadeem$
 */
public class LoadUtil {
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
    private LoadUtil() {} 

    
    private final static String DB_URL="jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = nyc1dbcl01-vip01.nyc1.freshdirect.com)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = nyc1dbcl01-vip02.nyc1.freshdirect.com)(PORT = 1521))(LOAD_BALANCE = yes)(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = devint)(FAILOVER_MODE =(TYPE = SELECT)(METHOD = BASIC)(RETRIES = 180)(DELAY = 5))))";
    
	//private final static String DB_URL = "jdbc:oracle:thin:@nyc1dbcl01-vip02.nyc1.freshdirect.com:1521:devint";
	//private final static String DB_URL = "jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBINT01";
	//private final static String DB_URL = "jdbc:oracle:thin:@db1.test.nyc1.freshdirect.com:1521:DBTST01"

    public static List getRandomSKUList() {
        List skus = new ArrayList();
        try {
            System.out.println("loading skus...");
            //
            //  grab a database connection
            //
            Connection conn = DriverManager.getConnection(DB_URL, "ERPS", "ERPS");
            //
            // get the sku codes from the last batch loaded
            //
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select sku_code from product prd where prd.unavailability_status is null " +
            "and prd.version = (select max(version) from product where sku_code=prd.sku_code)");
            while (rs.next()) {
                skus.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        //
        // shuffle
        //
        java.util.Collections.shuffle(skus);
        
        return skus;
    }
    
    public static List getRandomCustomerList() {
        List idents = new ArrayList();
        try {
            System.out.println("loading identities...");
            //
            //  grab a database connection
            //
            Connection conn = DriverManager.getConnection(DB_URL, "CUST", "CUST");
            //
            // get the identities of a bunch of active users
            //
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select cust.id, fdc.id from fdcustomer fdc, customer cust, creditcard cc, address ad, fduser fdu " +
                "where fdc.ERP_CUSTOMER_ID=cust.id and cc.CUSTOMER_ID=cust.id and ad.CUSTOMER_ID=cust.id and cust.ACTIVE='1' and rownum<5000 " +
                "and fdu.fdcustomer_id=fdc.id and fdu.depot_code is null and ad.scrubbed_address is not null");
            while (rs.next()) {
                idents.add(new FDIdentity(rs.getString(1), rs.getString(2)));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        //
        // shuffle
        //
        java.util.Collections.shuffle(idents);
        
        return idents;
    }
    
    public static List getZipCodeList() {
        List zips = new ArrayList();
        try {
            System.out.println("loading zipcodes...");
            //
            //  grab a database connection
            //
            Connection conn = DriverManager.getConnection(DB_URL, "DLV", "DLV");
            //
            // get the identities of a bunch of active users
            //
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select zipcode from zipcode");
            while (rs.next()) {
                zips.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        //
        // shuffle
        //
        java.util.Collections.shuffle(zips);
        
        return zips;
    }
    
    private static boolean warm = false;
    
    public static void doStoreWarmup() {
        if (warm) return;
        
        
			new  Warmup().warmup();
			warm = true;
		 
        
        /*
        ContentFactory cf = ContentFactory.getInstance();
        try {
            StoreModel store = cf.getStore();
            for(Iterator i=store.getDepartments().iterator(); i.hasNext(); ) {
                DepartmentModel dept = (DepartmentModel)i.next();
                for(Iterator j=dept.getCategories().iterator(); j.hasNext(); ) {
                    CategoryModel cat = (CategoryModel) j.next();
                    cf.getProducts(cat).iterator();
                }
            }
            warm = true;
        } catch (FDResourceException fdre) {
            warm = false;
            fdre.printStackTrace();
        }*/
    }
    
}