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

import javax.ejb.ObjectNotFoundException;

import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;

/**
 * @author rgayle
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CustomerEmailDAO  {
	
	private static int MAX_MESSAGE_LENGTH = 1001;
	
	public PrimaryKey create(Connection conn, ModelI model) throws SQLException {
		PrimaryKey pk = new PrimaryKey(SequenceGenerator.getNextId(conn,"CUST")); 
			ErpCustomerEmailModel em = (ErpCustomerEmailModel) model;
			PreparedStatement ps =
				conn.prepareStatement(
					"INSERT INTO CUST.CRM_CUSTOMER_EMAIL(ID, EMAIL_SENT, EMAIL_TEMPLATE_CODE, CUSTOM_PARAGRAPH, SIGNATURE)" +					" VALUES(?,?,?,?,?)");

		ps.setString(1, pk.getId());
		ps.setString(2, (em.isMailSent() ? "X" : ""));
		ps.setString(3, em.getEmailTemplateCode());
		ps.setString(4, em.getCustomMessage()!=null ? em.getCustomMessage().substring(0,Math.min(em.getCustomMessage().length(),MAX_MESSAGE_LENGTH)) : "" );
		ps.setString(5, em.getSignature());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("CRM_CUSTOMER_EMAIL Row not created");
		}

		ps.close();
		return pk;
	}

	public PrimaryKey findByPrimaryKey(Connection conn, PrimaryKey pk) throws SQLException, ObjectNotFoundException {
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.CRM_CUSTOMER_EMAIL WHERE ID=?");
		ps.setString(1, pk.getId());
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			throw new ObjectNotFoundException("Unable to find ErpCustomerEmail with PK " + pk);
		}

		String id = rs.getString(1);

		rs.close();
		ps.close();

		return new PrimaryKey(id);
	}

	public ModelI load(Connection conn, PrimaryKey pk) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select * from cust.CRM_CUSTOMER_EMAIL where id = ?");
		ps.setString(1, pk.getId());
		ResultSet rs = ps.executeQuery();

		if (!rs.next()) {
			throw new SQLException("No such ERPCustomerEMail: " + pk);
		}

		ErpCustomerEmailModel em = new ErpCustomerEmailModel(pk);
		String mailSent = rs.getString("email_sent");
		em.setMailSent("X".equalsIgnoreCase(mailSent));
		em.setEmailTemplateCode(rs.getString("email_template_code"));
		em.setCustomMessage(rs.getString("Custom_paragraph"));
		em.setSignature(rs.getString("signature"));
		
		rs.close();
		ps.close();
		return em;
	}


	public void updateMailSentFlag(Connection conn, ModelI model) throws SQLException {
		ErpCustomerEmailModel em = (ErpCustomerEmailModel) model;
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.CRM_CUSTOMER_EMAIL SET EMAIL_SENT=? " +			"  WHERE ID=?");
		ps.setString(1, (em.isMailSent() ? "X" : ""));
		ps.setString(2, em.getPK().getId());

		if (ps.executeUpdate() != 1) {
			throw new SQLException("CRM_CUSTOMER_EMAIL Row not updated");
		}

		ps.close();
	}

}
