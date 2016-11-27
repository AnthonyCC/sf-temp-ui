/*
 * Created on Aug 2, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.ErpCannedText;

/**
 * @author csongor
 */
public class CrmCannedTextDAO {
	public ErpCannedText create(Connection conn, ErpCannedText cannedText)
			throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("INSERT INTO CUST.CRM_CANNED_TEXT (ID, NAME, CATEGORY, CANNED_TEXT)"
						+ " VALUES (?, ?, ?, ?)");

		ps.setString(1, cannedText.getId());
		ps.setString(2, cannedText.getName());
		ps.setString(3, cannedText.getCategory() != null ? cannedText.getCategory().getName() : null);
		ps.setString(4, cannedText.getText());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("CRM_CANNED_TEXT Row not created");
		}

		ps.close();
		return cannedText;
	}

	public ErpCannedText load(Connection conn, String id) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("select * from cust.CRM_CANNED_TEXT where id = ?");
		ps.setString(1, id);
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			return null;
		}

		String fid = rs.getString("ID");
		String name = rs.getString("NAME");
		String categoryString = rs.getString("CATEGORY");
		EnumCannedTextCategory category = EnumCannedTextCategory.getEnum(categoryString);
		if (categoryString != null && category == null)
			throw new SQLException("Unknown category '" + categoryString + "' for ErpCannedText: " + id);
		String text = rs.getString("CANNED_TEXT");
		
		rs.close();
		ps.close();
		return new ErpCannedText(fid, name, category, text);
	}

	public Collection<ErpCannedText> loadAll(Connection conn) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("select * from cust.CRM_CANNED_TEXT ORDER BY ID");
		ResultSet rs = ps.executeQuery();

		Collection<ErpCannedText> all = new ArrayList<ErpCannedText>(10); 
		while (rs.next()) {
			String id = rs.getString("ID");
			String name = rs.getString("NAME");
			String categoryString = rs.getString("CATEGORY");
			EnumCannedTextCategory category = EnumCannedTextCategory.getEnum(categoryString);
			if (categoryString != null && category == null)
				throw new SQLException("Unknown category '" + categoryString + "' for ErpCannedText: " + id);
			String text = rs.getString("CANNED_TEXT");
			
			all.add(new ErpCannedText(id, name, category, text));
		}
		
		rs.close();
		ps.close();

		return all;
	}

	public Collection<ErpCannedText> loadAllInCategory(Connection conn, EnumCannedTextCategory category) throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("select * from cust.CRM_CANNED_TEXT WHERE CATEGORY = ?" +
						" ORDER BY NAME");
		ps.setString(1, category.getName());
		ResultSet rs = ps.executeQuery();

		Collection<ErpCannedText> all = new ArrayList<ErpCannedText>(10); 
		while (rs.next()) {
			String id = rs.getString("ID");
			String name = rs.getString("NAME");
			String categoryString = rs.getString("CATEGORY");
			EnumCannedTextCategory fcategory = EnumCannedTextCategory.getEnum(categoryString);
			if (categoryString != null && fcategory == null)
				throw new SQLException("Unknown category '" + categoryString + "' for ErpCannedText: " + id);
			String text = rs.getString("CANNED_TEXT");
			
			all.add(new ErpCannedText(id, name, fcategory, text));
		}
		
		rs.close();
		ps.close();

		return all;
	}
	
	public void update(Connection conn, ErpCannedText cannedText, String id)
			throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("UPDATE CUST.CRM_CANNED_TEXT SET ID = ?, NAME = ?, CATEGORY = ?,"
						+ " CANNED_TEXT = ? WHERE ID = ?");
		ps.setString(1, cannedText.getId());
		ps.setString(2, cannedText.getName());
		ps.setString(3, cannedText.getCategory().getName());
		ps.setString(4, cannedText.getText());
		ps.setString(5, id);

		if (ps.executeUpdate() != 1) {
			throw new SQLException("CRM_CANNED_TEXT Row not updated");
		}

		ps.close();
	}

	public void delete(Connection conn, String id)
			throws SQLException {
		PreparedStatement ps = conn
				.prepareStatement("DELETE FROM CUST.CRM_CANNED_TEXT WHERE ID = ?");
		ps.setString(1, id);

		if (ps.executeUpdate() != 1) {
			throw new SQLException("CRM_CANNED_TEXT Row not deleted");
		}

		ps.close();
	}
}
