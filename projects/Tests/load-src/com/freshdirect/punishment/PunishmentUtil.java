/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.punishment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
class PunishmentUtil {
	
	private PunishmentUtil() {}

	private final static String DB_URL = "jdbc:oracle:thin:@db1.dev.nyc1.freshdirect.com:1521:DBDEV01";
	
	public static List getRandomSKUList() throws SQLException {
        //
        //  grab a database connection
        //
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        Connection conn = DriverManager.getConnection(DB_URL, "ERPS", "ERPS");
        //
        // get the sku codes from the last batch loaded
        //
        List skus = new ArrayList();
        Statement stmt = conn.createStatement();
        //ResultSet rs = stmt.executeQuery("select sku_code from product where version=(select max(version) from product)");
        ResultSet rs = stmt.executeQuery("select distinct sku_code from product");
        while (rs.next()) {
            skus.add(rs.getString(1));
        }
        rs.close();
        stmt.close();
        conn.close();
        //
        // shuffle
        //
        java.util.Collections.shuffle(skus);

        return skus;
	}

}