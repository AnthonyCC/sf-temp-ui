/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2002 FreshDirect, Inc.
 *
 */
package com.freshdirect.dataloader.usps.zipcode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.usps.USPSLoader;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ZipPlus4Loader implements SynchronousParserClient {

	//
	// parser for reconcilation files from Chase
	//
	/** a tab-delimited file parser
	 */
	ZipPlus4Parser parser = null;

	/** default constructor
	 */
	public ZipPlus4Loader() {
		//
		// create parser
		//
		this.parser = new ZipPlus4Parser();
		parser.setClient(this);
	}

	public void load(String filename) {

		try {
			System.out.println("\n----- ZIP+4 starting -----");

			parser.parseFile(filename);

			System.out.println("\n----- ZIP+4 done -----");

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
				} catch (SQLException sqle2) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle3) {
				}
			}
		}

	}

	Connection conn;
	PreparedStatement ps;
	int count = 0;

	String zp4insert =
		"insert into ZIPPLUSFOUR_LOAD (BLDG_NUM_LOW, BLDG_NUM_HIGH, STREET_PRE_DIR, STREET_NAME, STREET_SUFFIX, STREET_POST_DIR, "
			+ "APT_NUM_LOW, APT_NUM_HIGH, ZIPCODE, PLUSFOUR, STREET_NORMAL, ADDRESS_TYPE, CITY_STATE_KEY) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public void accept(Object o) {
		ZipPlus4Record record = (ZipPlus4Record) o;
		try {
			if ((count % 10000) == 0) {
				// refresh connection every 10000 records
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
				conn = USPSLoader.getConnection();
				ps = conn.prepareStatement(zp4insert);
			}
				
			ps.clearParameters();
			ps.setString(1, record.getAddrPrimaryLow());
			ps.setString(2, record.getAddrPrimaryHigh());
			ps.setString(3, record.getStreetPreDirectional());
			ps.setString(4, record.getStreetName());
			ps.setString(5, record.getStreetSuffix());
			ps.setString(6, record.getStreetPostDirectional());
			ps.setString(7, record.getAddrSecondaryLow());
			ps.setString(8, record.getAddrSecondaryHigh());
			ps.setString(9, record.getZipCode());
			ps.setString(10, record.getZipAddonLow());
			ps.setString(11, getStreetNormal(record));
			ps.setString(12, record.getRecordType());
			ps.setString(13, record.getCityStateKey());
			ps.execute();

			count++;
			if ((count % 100) == 0)
				System.out.println(count + " records...");

		} catch (SQLException sqle) {
			String message = sqle.getMessage();
			System.out.println(
				record.getAddrPrimaryLow()
					+ " "
					+ record.getAddrPrimaryHigh()
					+ " "
					+ record.getStreetPreDirectional()
					+ " "
					+ record.getStreetName()
					+ " "
					+ record.getStreetSuffix()
					+ " "
					+ record.getStreetPostDirectional()
					+ " "
					+ record.getAddrSecondaryLow()
					+ " "
					+ record.getAddrSecondaryHigh()
					+ " "
					+ record.getZipCode()
					+ " "
					+ record.getZipAddonLow()
					+ " "
					+ record.getZipAddonHigh());
			if ((record != null) && (message.indexOf("unique constraint") > -1)) {

			} else {
				sqle.printStackTrace();
			}
		}

	}

	StringBuffer normalBuff = new StringBuffer();
	public String getStreetNormal(ZipPlus4Record zpf) {
		normalBuff.delete(0, normalBuff.length());
		String nws =
			normalBuff
				.append(zpf.getStreetPreDirectional())
				.append(zpf.getStreetName())
				.append(zpf.getStreetSuffix())
				.append(zpf.getStreetPostDirectional())
				.toString();
		normalBuff.delete(0, normalBuff.length());
		for (int i = 0; i < nws.length(); i++) {
			char c = nws.charAt(i);
			if (!Character.isSpaceChar(c))
				normalBuff.append(c);
		}
		return normalBuff.toString();
	}

}
