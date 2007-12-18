/*
 * AddressChecker.java
 *
 * Created on July 15, 2002, 10:05 AM
 */

package com.freshdirect.delivery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.ejb.GeographyDAO;

/**
 *
 * @author  mrose
 * @version 
 */
public class AddressChecker {

    /** Creates new AddressChecker */
    public AddressChecker() {
        super();
        dao = new GeographyDAO();
    }
    
    GeographyDAO dao;
    
    public static void main(String args[]) {
        AddressChecker ac = new AddressChecker();
     
        AddressModel a = new AddressModel();
        a.setAddress1("310 Greenwich Street");//280 East 2nd Street
        a.setApartment("21C");
        a.setCity("New York");
        a.setState("NY");
        a.setZipCode("10013");
     
        System.out.println("\n--- input address ---");
        System.out.println(a.getAddress1() + " " + (a.getApartment()!=null?a.getApartment():""));
        System.out.println(a.getCity() + " " + a.getState() + " " + a.getZipCode());
     
        System.out.println("\n--- verify address ---");
        EnumAddressVerificationResult result = ac.verify(a);
        System.out.println(result);
        if (!EnumAddressVerificationResult.ADDRESS_OK.equals(result)) return;
        System.out.println(a.getScrubbedStreet() + " " + (a.getApartment()!=null?a.getApartment():""));
        System.out.println(a.getCity() + " " + a.getState() + " " + a.getZipCode());
     
        System.out.println("\n--- geocode address ---");
        String geocodeResult = ac.geocode(a);
        System.out.println(geocodeResult);
        if (!result.equals(GeographyDAO.GEOCODE_OK)) return;
        System.out.println(a.getScrubbedStreet() + " " + (a.getApartment()!=null?a.getApartment():""));
        System.out.println(a.getCity() + " " + a.getState() + " " + a.getZipCode());
        System.out.println(a.getLongitude() + ", " + a.getLatitude());
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
    
    
    private EnumAddressVerificationResult verify(AddressModel address) {
        Connection conn = null;
        EnumAddressVerificationResult result = null;
        //String result = "SYSTEM_ERROR";
        try {
            conn = getConnection();
            result = dao.verify(address, conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                }
            }
        }
        return result;
    }
    
    
    public String geocode(AddressModel address) {
        Connection conn = null;
        String result = "GEOCODE_FAILED";
        try {
            conn = getConnection();
            result = dao.geocode(address, conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch(InvalidAddressException ae){
        	//have to nothing as result will still be GEOCODE_FAILED.	
        }finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                }
            }
        }
        return result;
    }

}
