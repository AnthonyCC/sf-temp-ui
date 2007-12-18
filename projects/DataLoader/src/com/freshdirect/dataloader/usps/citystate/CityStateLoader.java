/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.usps.citystate;

import java.util.*;
import java.sql.*;

import com.freshdirect.dataloader.*;
import com.freshdirect.dataloader.usps.USPSLoader;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class CityStateLoader implements SynchronousParserClient {
    
    //
    // parser for reconcilation files from Chase
    //
    /** a tab-delimited file parser
     */
    CityStateParser parser = null;
    
    /** default constructor
     */
    public CityStateLoader() {
        //
        // create parser
        //
        this.parser = new CityStateParser();
        parser.setClient(this);
    }
    
    public void load(String filename) {
        
        try {
            System.out.println("\n----- CityState starting -----");
            
            parser.parseFile(filename);
            
            System.out.println("\n----- CityState done -----");
            
            if (parser.getExceptions().size() > 0) {
                Iterator exIter = parser.getExceptions().iterator();
                while (exIter.hasNext()) {
                    BadDataException bde = (BadDataException) exIter.next();
                    System.out.println(bde);
                }
            }
            
            System.out.println("\n----- " + count + " records loaded -----");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException sqle2) { }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle3) { }
            }
        }
        
    }
    
    Connection conn;
    PreparedStatement ps;
    int count = 0;
    
    String cityStateInsert = "insert into CITY_STATE_LOAD (CITY, STATE, CITY_STATE_KEY, COUNTY) values (?,?,?,?)";
    
    public void accept(Object o) {
        
        CityStateRecord record = (CityStateRecord) o;
        
        if ((count % 100) == 0) System.out.println(count + " records...");
        try {
         
            if ((count % 10000) == 0) {
                // refresh connection every 10000 records
                if (ps != null) ps.close();
                if (conn != null) conn.close();
                conn = USPSLoader.getConnection();
                ps = conn.prepareStatement(cityStateInsert);
            }
         	
            ps.clearParameters();
            ps.setString(1, record.getCity());
            ps.setString(2, record.getState());
            ps.setString(3, record.getCityStateKey());
            ps.setString(4, record.getCountyName());
            ps.executeUpdate();
            count++;

        } catch (SQLException sqle) {
            String message = sqle.getMessage();
            if ((record != null) && (message.indexOf("unique constraint") > -1)) {
                System.out.println(record.getCityStateKey() + " " + record.getState() + " " + record.getCity()+ " " + record.getCountyName());
            } else {
                sqle.printStackTrace();
            }
        }
        
    }    
}
