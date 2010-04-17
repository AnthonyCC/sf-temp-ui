package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.freshdirect.framework.core.PrimaryKey;

public class ErpPromotionDAO {

	public static void delete(Connection conn, PrimaryKey salePk) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.PROMOTION_PARTICIPATION WHERE SALE_ID=?");
		ps.setString(1, salePk.getId());
		ps.executeUpdate();
		ps.close();
	}

	public static void insert(Connection conn, PrimaryKey salePk, Set<String> promotionCodes) throws SQLException {
		if (promotionCodes.isEmpty()) {
			return;
		}

		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO CUST.PROMOTION_PARTICIPATION(SALE_ID, PROMOTION_ID) VALUES (?,(SELECT ID FROM CUST.PROMOTION WHERE CODE=?))");
		for ( String promoCode : promotionCodes ) {
			ps.setString(1, salePk.getId());
			ps.setString(2, promoCode);
			ps.addBatch();
		}

		ps.executeBatch();

		ps.close();
	}

	public static Set<String> select(Connection conn, PrimaryKey salePk) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT P.CODE FROM CUST.PROMOTION P, CUST.PROMOTION_PARTICIPATION PP WHERE P.ID=PP.PROMOTION_ID AND PP.SALE_ID=?");
		ps.setString(1, salePk.getId());

		Set<String> promotionCodes = new HashSet<String>();
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			promotionCodes.add(rs.getString(1));
		}

		rs.close();
		ps.close();

		return promotionCodes;
	}

	/** @return Map of saleId -> Set of promocodes */
	public static Map<String,Set<String>> loadPromotionParticipation(Connection conn, PrimaryKey customerPk) throws SQLException {
		Map<String,Set<String>> participation = new HashMap<String,Set<String>>();

		PreparedStatement ps =
			conn.prepareStatement(
				"select s.id as sale_id, p.code from cust.sale s, cust.promotion p, cust.promotion_participation pp where s.customer_id=? and s.id=pp.sale_id and p.id=pp.promotion_id");
		ps.setString(1, customerPk.getId());

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			String saleId = rs.getString(1);
			String promoCode = rs.getString(2);
			Set<String> promos = participation.get(saleId);
			if (promos == null) {
				promos = new HashSet<String>();
				participation.put(saleId, promos);
			}
			promos.add(promoCode);
		}

		rs.close();
		ps.close();

		return participation;

	}
}
