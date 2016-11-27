/*
 * Geocoder.java
 *
 * Created on February 11, 2002, 8:13 PM
 */

package com.freshdirect.delivery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.ejb.GeographyDAO;

/**
 *
 * @author  mrose
 * @version
 */
public class NonGeoAddressFinder {
    
    public static void main(String args[]) {
        NonGeoAddressFinder f = new NonGeoAddressFinder();
        f.go();
    }
    
    GeographyDAO gDao = null;
    
    public NonGeoAddressFinder() {
        super();
        gDao = new GeographyDAO();
    }
    
    public void go() {
        
        Connection conn = null;
        ArrayList zipcodes = new ArrayList();
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("select distinct zipcode from city_state where city='NEW YORK' order by zipcode");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                zipcodes.add(rs.getString(1));
            }
            ps.close();
            rs.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) { }
            }
        }
        
        int totalAddresses = 0;
        for (Iterator zIter = zipcodes.iterator(); zIter.hasNext(); ) {
            String z = (String) zIter.next();
            totalAddresses += this.doZipCode(z);
        }
        System.out.println(totalAddresses + " total addresses tested");
    }
    
    String addressSelectQuery = "select distinct bldg_num_low, street_pre_dir, street_name, street_suffix, street_post_dir, zipcode " +
    "from zipplusfour where zipcode=? and bldg_num_low=bldg_num_high " +
    "order by (street_pre_dir||street_name||street_suffix||street_post_dir), bldg_num_low";
    
    public int doZipCode(String zipCode) {
        System.out.println("\n\n\n------> Testing zipcode : " + zipCode);
        Connection conn = null;
        int count = 0;
        try {
            conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(addressSelectQuery);
            ps.setString(1, zipCode);
            ResultSet rs = ps.executeQuery();
            ArrayList addresses = new ArrayList();
            StringBuffer addBuffer = new StringBuffer();
            while (rs.next()) {
                AddressModel a = new AddressModel();
                addBuffer.delete(0, addBuffer.length());
                addBuffer.append(rs.getString(1));
                String streetPreDir = rs.getString(2);
                if ((streetPreDir != null) && (streetPreDir.trim().length() > 0))
                    addBuffer.append(' ').append(streetPreDir);
                String streetName = rs.getString(3);
                addBuffer.append(' ').append(streetName);
                String streetSuffix = rs.getString(4);
                if ((streetSuffix != null) && (streetSuffix.trim().length() > 0))
                    addBuffer.append(' ').append(streetSuffix);
                String streetPostDir = rs.getString(5);
                if ((streetPostDir != null) && (streetPostDir.trim().length() > 0))
                    addBuffer.append(' ').append(streetPostDir);
                a.setAddress1(addBuffer.toString());
                a.setCity("NEW YORK");
                a.setState("NY");
                a.setZipCode(zipCode);
                addresses.add(a);
            }
            ps.close();
            rs.close();
            
            System.out.println("Found " + addresses.size() + " addresses to test");
            for (Iterator aIter = addresses.iterator(); aIter.hasNext(); ) {
                AddressModel a = (AddressModel) aIter.next();
                
                try{
                	String result = gDao.geocode(a, conn);
                	if (!"GEOCODE_OK".equals(result)) {
                    	System.out.println("Can't geocode " + a.getAddress1() + " " + a.getCity() + " " + a.getState() + " " + a.getZipCode());
                	}
                }catch(InvalidAddressException ia){
                	System.out.println("Invalid address " + a.getAddress1() + " " + a.getCity() + " " + a.getState() + " " + a.getZipCode());
                }
                count++;
                if ((count % 100) == 0) System.out.println(count + " geocoded...");
                
            }
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) { }
            }
        }
        System.out.println(count + " addresses tested");
        return count;
    }
    
    protected Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01", "dlv_dev", "dlv_dev");
        return c;
    }
    
    static {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
    
}
